package com.tokopedia.shop.newinfo.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
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
import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.newinfo.di.component.ShopNewInfoComponent
import com.tokopedia.shop.newinfo.view.adapter.ShopNewInfoAdapter
import com.tokopedia.shop.newinfo.view.model.*
import com.tokopedia.shop.newinfo.view.viewmodel.ShopNewInfoViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.activity_shop_new_info.*
import javax.inject.Inject

class ShopNewInfoFragment : BaseDaggerFragment(),
        ShopNewInfoAdapter.ProfileItemClickListener,
        ShopNewInfoAdapter.ProductItemClickListener,
        ShopNewInfoAdapter.SupportItemClickListener,
        ShopNewInfoAdapter.ShippingItemClickListener {

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_DOMAIN = "domain"
        const val SHOP_NAME_PLACEHOLDER = "{{shop_name}}"
        const val SHOP_LOCATION_PLACEHOLDER = "{{shop_location}}"
        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2
        private const val VIEW_ERROR = 3

        fun createInstance(): Fragment {
            return ShopNewInfoFragment()
        }
    }

    var shopId: String? = null
    var shopDomain: String? = null
    var isShareFunctionReady = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var shopNewInfoViewModel: ShopNewInfoViewModel

    lateinit var shopPageTracking: ShopPageTrackingBuyer
    lateinit var remoteConfig: RemoteConfig

    private lateinit var errorView: View
    private lateinit var dashboardView: View
    private lateinit var shopNewInfoView: RecyclerView
    private lateinit var retryMessageView: TextView
    private lateinit var retryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        remoteConfig = FirebaseRemoteConfigImpl(activity)

        shopPageTracking = ShopPageTrackingBuyer(activity?.applicationContext?.let { TrackingQueue(it) })

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
            shopNewInfoViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopNewInfoViewModel::class.java)
        }

        shopNewInfoViewModel.shopInfoResp.observe(this, Observer {
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
        return inflater.inflate(R.layout.fragment_shop_new_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup views
        errorView = view.findViewById(R.id.error_view)
        dashboardView = view.findViewById(R.id.dashboard_layout)
        dashboardView.setOnClickListener { onDashboardClick() }
        shopNewInfoView = view.findViewById(R.id.rv_new_info)
        retryMessageView = view.findViewById(R.id.message_retry)
        retryButton = view.findViewById(R.id.button_retry)

        // setup shop new info recycler view
        val newInfoView: RecyclerView = view.findViewById(R.id.rv_new_info)
        val linearLayoutManager = LinearLayoutManager(activity)
        val shopNewInfoAdapter = ShopNewInfoAdapter(this, this, this, this)
        newInfoView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = shopNewInfoAdapter
        }
        newInfoView.addItemDecoration(DividerItemDecoration(activity, linearLayoutManager.orientation))

        // create contents for shop new info page
        val newInfoList = mutableListOf<NewInfo>()
        newInfoList.add(Profile())
        newInfoList.add(Product())
        newInfoList.add(Support())
        newInfoList.add(Shipping())
        shopNewInfoAdapter.setNewInfoList(newInfoList)

        // get shop info
        getShopInfo()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_shop_new_info, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        menu?.findItem(R.id.action_share)?.isVisible = isShareFunctionReady
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_share) {
            onShareShop()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        getComponent(ShopNewInfoComponent::class.java).inject(this)
    }

    private fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        setViewState(VIEW_CONTENT)
        with(shopInfo) {
            shopPageTracking.sendScreenShopPage(activity,
                    CustomDimensionShopPage.create(shopCore.shopID, goldOS.isOfficial == 1,
                            goldOS.isGold == 1))
        }
    }

    private fun onErrorGetShopInfo(e: Throwable?) {
        setViewState(VIEW_ERROR)
        retryMessageView.text = ErrorHandler.getErrorMessage(activity, e)
        retryButton.setOnClickListener { getShopInfo() }
    }

    private fun getShopInfo(isRefresh: Boolean = false) {
        setViewState(VIEW_LOADING)
        shopNewInfoViewModel.getShop(shopId, shopDomain, isRefresh)
    }

    private fun setViewState(viewState: Int) {
        when (viewState) {
            VIEW_LOADING -> {
                errorView.hide()
                dashboardView.visible()
                shopNewInfoView.visible()
            }
            VIEW_ERROR -> {
                errorView.visible()
                dashboardView.hide()
                shopNewInfoView.hide()
            }
            else -> {
                errorView.hide()
                dashboardView.visible()
                shopNewInfoView.visible()
            }
        }
    }

    private fun displayShareButton() {
        isShareFunctionReady = true
        activity?.invalidateOptionsMenu()
    }

    private fun onShareShop() {
        (shopNewInfoViewModel.shopInfoResp.value as? Success)?.data?.let {
            shopPageTracking.clickShareButton(shopNewInfoViewModel.isMyShop(it.shopCore.shopID),
                    CustomDimensionShopPage.create(it.shopCore.shopID,
                            it.goldOS.isOfficial == 1,
                            it.goldOS.isGold == 1))
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
        RouteManager.route(activity, ApplinkConst.HOME_ACCOUNT_SELLER)
    }

    // Ubah profil toko
    override fun onChangeProfileClicked() {
        RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO)
    }

    // Ubah catatan toko
    override fun onChangeShopNoteClicked() {
        RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES)
    }

    // Atur jam buka toko
    override fun onEditShopScheduleClicked() {
        RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_EDIT_SCHEDULE)
    }

    // Lihat daftar produk
    override fun onDisplayProductsClicked() {
        RouteManager.route(activity, ApplinkConst.PRODUCT_MANAGE)
    }

    // Tambah dan ubah etalase
    override fun onEditEtalaseClicked() {
        RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ETALASE_ADD)
    }

    // Pusat bantuan
    override fun onGetSupportClicked() {
        RouteManager.route(activity, ApplinkConst.CONTACT_US_NATIVE)
    }

    // Pusat Seller
    override fun onGetTipsClicked() {
        RouteManager.route(activity, ApplinkConst.SELLER_CENTER)
    }

    // Tambah dan ubah lokasi toko
    override fun onEditLocationClicked() {
        RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS)
    }

    // Atur layanan pengiriman
    override fun onManageShippingServiceClicked() {
        RouteManager.route(activity, ApplinkConst.SELLER_SHIPPING_EDITOR)
    }
}
