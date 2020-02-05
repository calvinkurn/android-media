package com.tokopedia.shop.setting.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopModuleRouter
import com.tokopedia.shop.analytic.NewShopPageTrackingShopPageSetting
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.setting.di.component.ShopPageSettingComponent
import com.tokopedia.shop.setting.view.adapter.ShopPageSettingAdapter
import com.tokopedia.shop.setting.view.model.*
import com.tokopedia.shop.setting.view.viewmodel.ShopPageSettingViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopPageSettingFragment : BaseDaggerFragment(),
        ShopPageSettingAdapter.ProfileItemClickListener,
        ShopPageSettingAdapter.ProductItemClickListener,
        ShopPageSettingAdapter.SupportItemClickListener,
        ShopPageSettingAdapter.ShippingItemClickListener {

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_DOMAIN = "domain"
        const val SHOP_NAME_PLACEHOLDER = "{{shop_name}}"
        const val SHOP_LOCATION_PLACEHOLDER = "{{shop_location}}"
        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2
        private const val VIEW_ERROR = 3

        fun createInstance(): Fragment {
            return ShopPageSettingFragment()
        }
    }

    var shopId: String? = null
    var shopDomain: String? = null
    var isShareFunctionReady = false
    var shopInfo: ShopInfo? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var shopPageSettingViewModel: ShopPageSettingViewModel

    lateinit var shopPageSettingTracking: NewShopPageTrackingShopPageSetting
    lateinit var remoteConfig: RemoteConfig

    private lateinit var errorView: View
    private lateinit var dashboardView: View
    private lateinit var shopPageSettingView: RecyclerView
    private lateinit var retryMessageView: TextView
    private lateinit var retryButton: Button
    val isOfficial: Boolean
        get() = shopInfo?.goldOS?.isOfficial == 1
    val isGold: Boolean
        get() = shopInfo?.goldOS?.isGold == 1
    private val customDimensionShopPage: CustomDimensionShopPage
        get() = CustomDimensionShopPage.create(shopId,isOfficial,isGold)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        remoteConfig = FirebaseRemoteConfigImpl(activity)

        activity?.let {
            shopPageSettingTracking = NewShopPageTrackingShopPageSetting(TrackingQueue(it))
        }

        // get data from extra
        activity?.intent?.run {
            shopId = getStringExtra(SHOP_ID)
            shopDomain = getStringExtra(SHOP_DOMAIN)
        }

        // get data from query parameter
        activity?.intent?.data?.run {
            if (shopId.isNullOrEmpty()) {
                shopId = getQueryParameter(SHOP_ID)
            }
            if (shopDomain.isNullOrEmpty()) {
                shopDomain = getQueryParameter(SHOP_DOMAIN)
            }
        }

        activity?.run {
            shopPageSettingViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopPageSettingViewModel::class.java)
        }

        shopPageSettingViewModel.shopInfoResp.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetShopInfo(it.data)
                is Fail -> onErrorGetShopInfo(it.throwable)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_page_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup views
        errorView = view.findViewById(R.id.error_view)
        dashboardView = view.findViewById(R.id.dashboard_layout)
        dashboardView.setOnClickListener { onDashboardClick() }
        shopPageSettingView = view.findViewById(R.id.rv_shop_page_setting)
        retryMessageView = view.findViewById(R.id.message_retry)
        retryButton = view.findViewById(R.id.button_retry)

        // setup shop page setting recycler view
        val shopPageSettingView: RecyclerView = view.findViewById(R.id.rv_shop_page_setting)
        val linearLayoutManager = LinearLayoutManager(activity)
        val shopPageSettingAdapter = ShopPageSettingAdapter(this, this, this, this)
        shopPageSettingView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = shopPageSettingAdapter
        }
        shopPageSettingView.addItemDecoration(DividerItemDecoration(activity))

        // create contents for shop page setting page
        val shopPageSettingList = mutableListOf<ShopPageSetting>()
        shopPageSettingList.add(Profile())
        shopPageSettingList.add(Product())
        shopPageSettingList.add(Support())
        shopPageSettingList.add(Shipping())
        shopPageSettingAdapter.setShopPageSettingList(shopPageSettingList)

        // get shop info
        getShopInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_shop_page_setting, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_share)?.isVisible = isShareFunctionReady
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_share ->  {
                clickShareButton()
            }
            android.R.id.home -> {
                clickBackButton()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun clickBackButton() {
        shopPageSettingTracking.clickBackArrow(true, customDimensionShopPage)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        getComponent(ShopPageSettingComponent::class.java).inject(this)
    }

    private fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
        setViewState(VIEW_CONTENT)
    }

    private fun onErrorGetShopInfo(e: Throwable?) {
        setViewState(VIEW_ERROR)
        retryMessageView.text = ErrorHandler.getErrorMessage(activity, e)
        retryButton.setOnClickListener { getShopInfo() }
    }

    private fun getShopInfo(isRefresh: Boolean = false) {
        setViewState(VIEW_LOADING)
        shopPageSettingViewModel.getShop(shopId, shopDomain, isRefresh)
    }

    private fun setViewState(viewState: Int) {
        when (viewState) {
            VIEW_LOADING -> {
                errorView.hide()
                dashboardView.visible()
                shopPageSettingView.visible()
            }
            VIEW_ERROR -> {
                errorView.visible()
                dashboardView.hide()
                shopPageSettingView.hide()
            }
            else -> {
                displayShareButton()
                errorView.hide()
                dashboardView.visible()
                shopPageSettingView.visible()
            }
        }
    }

    private fun displayShareButton() {
        isShareFunctionReady = true
        activity?.invalidateOptionsMenu()
    }

    private fun clickShareButton() {
        (shopPageSettingViewModel.shopInfoResp.value as? Success)?.data?.let {
            shopPageSettingTracking.clickShareButton(customDimensionShopPage)
            var shopShareMsg: String = remoteConfig.getString(RemoteConfigKey.SHOP_SHARE_MSG)
            shopShareMsg = if (!TextUtils.isEmpty(shopShareMsg)) {
                FindAndReplaceHelper.findAndReplacePlaceHolders(shopShareMsg,
                        SHOP_NAME_PLACEHOLDER, MethodChecker.fromHtml(it.shopCore.name).toString(),
                        SHOP_LOCATION_PLACEHOLDER, it.location)
            } else {
                getString(R.string.shop_label_share_formatted,
                        MethodChecker.fromHtml(it.shopCore.name).toString(), it.location)
            }
            (activity?.application as ShopModuleRouter).goToShareShop(activity,
                    shopId, it.shopCore.url, shopShareMsg)
        }
    }

    // Dashboard Toko
    private fun onDashboardClick() {
        shopPageSettingTracking.clickShopDashboard(customDimensionShopPage)
        RouteManager.route(activity, ApplinkConst.HOME_ACCOUNT_SELLER)
    }

    // Ubah profil toko
    override fun onChangeProfileClicked() {
        shopPageSettingTracking.clickChangeShopProfile(customDimensionShopPage)
        RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO)
    }

    // Ubah catatan toko
    override fun onChangeShopNoteClicked() {
        shopPageSettingTracking.clickChangeShopNote(customDimensionShopPage)
        RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES)
    }

    // Atur jam buka toko
    override fun onEditShopScheduleClicked() {
        shopPageSettingTracking.clickSetOpenShopTime(customDimensionShopPage)
        RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_EDIT_SCHEDULE)
    }

    // Lihat daftar produk
    override fun onDisplayProductsClicked() {
        shopPageSettingTracking.clickSeeProduct(customDimensionShopPage)
        RouteManager.route(activity, ApplinkConst.PRODUCT_MANAGE)
    }

    // Tambah dan ubah etalase
    override fun onEditEtalaseClicked() {
        shopPageSettingTracking.clickAddAndEditEtalase(customDimensionShopPage)
        RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ETALASE)
    }

    // Pusat bantuan
    override fun onGetSupportClicked() {
        shopPageSettingTracking.clickPusatBantuan(customDimensionShopPage)
        RouteManager.route(activity, ApplinkConst.CONTACT_US_NATIVE)
    }

    // Pusat Seller
    override fun onGetTipsClicked() {
        shopPageSettingTracking.clickPusatSeller(customDimensionShopPage)
        RouteManager.route(activity, ApplinkConst.SELLER_CENTER)
    }

    // Tambah dan ubah lokasi toko
    override fun onEditLocationClicked() {
        shopPageSettingTracking.clickAddAndEditShopLocation(customDimensionShopPage)
        RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS)
    }

    // Atur layanan pengiriman
    override fun onManageShippingServiceClicked() {
        shopPageSettingTracking.clickSetShippingService(customDimensionShopPage)
        RouteManager.route(activity, ApplinkConst.SELLER_SHIPPING_EDITOR)
    }
}
