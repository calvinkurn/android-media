package com.tokopedia.shop.setting.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.analytic.ShopPageTrackingShopPageSetting
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.common.constant.ShopParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.EXTRA_BUNDLE
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.product.view.activity.ShopProductListResultActivity
import com.tokopedia.shop.setting.di.component.ShopPageSettingComponent
import com.tokopedia.shop.setting.view.adapter.ShopPageSettingAdapter
import com.tokopedia.shop.setting.view.model.*
import com.tokopedia.shop.setting.view.viewmodel.ShopPageSettingViewModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
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
        const val SELLER_CENTER_URL = "https://seller.tokopedia.com/edu/"

        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2
        private const val VIEW_ERROR = 3
        private const val REQUEST_CODE_ETALASE = 208

        fun createInstance(): Fragment {
            return ShopPageSettingFragment()
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var shopPageSettingViewModel: ShopPageSettingViewModel

    private lateinit var errorView: View
    private lateinit var dashboardView: View
    private lateinit var shopPageSettingView: RecyclerView
    private lateinit var retryMessageView: TextView
    private lateinit var retryButton: View
    private lateinit var loadingView: View
    private val isOfficial: Boolean
        get() = shopInfo?.goldOS?.isOfficial == 1
    private val isGold: Boolean
        get() = shopInfo?.goldOS?.isGold == 1
    private val customDimensionShopPage: CustomDimensionShopPage by lazy {
        CustomDimensionShopPage.create(shopId, isOfficial, isGold)
    }

    private var shopPageSettingTracking: ShopPageTrackingShopPageSetting? = null
    private var remoteConfig: RemoteConfig? = null

    private var shopId: String? = null
    private var shopDomain: String? = null
    private var shopInfo: ShopInfo? = null
    private var shopRef: String = ""
    private var shopSettingAccess = ShopSettingAccess()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        remoteConfig = FirebaseRemoteConfigImpl(activity)

        activity?.let {
            shopPageSettingTracking = ShopPageTrackingShopPageSetting(TrackingQueue(it))
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
            pathSegments?.let{
                if (it.size > 1) {
                    shopId = it.getOrNull(1).orEmpty()
                }
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
        retryMessageView = view.findViewById(com.tokopedia.abstraction.R.id.message_retry)
        retryButton = view.findViewById(com.tokopedia.abstraction.R.id.button_retry)
        loadingView = view.findViewById(R.id.loader_shop_page_setting)

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

        observeRoleAccess()

        // get shop info
        getShopInfo()
    }

    fun onBackPressed() {
        shopPageSettingTracking?.clickBackArrow(true, customDimensionShopPage)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        getComponent(ShopPageSettingComponent::class.java).inject(this)
    }

    private fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
        customDimensionShopPage.updateCustomDimensionData(shopId, isOfficial, isGold)
        setViewState(VIEW_CONTENT)
    }

    private fun onErrorGetShopInfo(e: Throwable?) {
        setViewState(VIEW_ERROR)
        retryMessageView.text = ErrorHandler.getErrorMessage(activity, e)
        retryButton.setOnClickListener { getShopInfo() }
    }

    private fun getShopInfo(isRefresh: Boolean = false) {
        if (userSession.isShopOwner) {
            setViewState(VIEW_CONTENT)
        } else {
            setViewState(VIEW_LOADING)
        }
        shopPageSettingViewModel.getShop(shopId, shopDomain, isRefresh)
    }

    private fun setViewState(viewState: Int) {
        when (viewState) {
            VIEW_LOADING -> {
                errorView.hide()
                loadingView.visible()
                dashboardView.visible()
                shopPageSettingView.gone()
            }
            VIEW_ERROR -> {
                errorView.visible()
                loadingView.gone()
                dashboardView.hide()
                shopPageSettingView.hide()
            }
            else -> {
                errorView.hide()
                loadingView.gone()
                dashboardView.visible()
                shopPageSettingView.visible()
            }
        }
    }

    // Dashboard Toko
    private fun onDashboardClick() {
        shopPageSettingTracking?.clickShopDashboard(customDimensionShopPage)
        if (GlobalConfig.isSellerApp()) {
            RouteManager.route(activity, ApplinkConst.SellerApp.SELLER_APP_HOME)
        } else {
            RouteManager.route(activity, ApplinkConst.HOME_ACCOUNT_SELLER)
        }
    }

    private fun observeRoleAccess() {
        shopPageSettingViewModel.shopSettingAccessLiveData.observe(viewLifecycleOwner) { result ->
            if (result is Success) {
                shopSettingAccess = result.data
            }
        }
    }

    private fun showErrorToaster(errorMessage: String) {
        view?.let {
            Toaster.build(it, errorMessage, type = Toaster.TYPE_ERROR).show()
        }
    }

    private fun whenRoleAuthorized(isAuthorized: Boolean, action: () -> Unit) {
        if (isAuthorized) {
            action()
        } else {
            showErrorToaster(context?.getString(R.string.shop_page_setting_no_access_error).orEmpty())
        }
    }

    // Ubah profil toko
    override fun onChangeProfileClicked() {
        shopPageSettingTracking?.clickChangeShopProfile(customDimensionShopPage)
        whenRoleAuthorized(shopSettingAccess.isInfoAccessAuthorized) {
            RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO)
        }
    }

    // Ubah catatan toko
    override fun onChangeShopNoteClicked() {
        shopPageSettingTracking?.clickChangeShopNote(customDimensionShopPage)
        whenRoleAuthorized(shopSettingAccess.isNotesAccessAuthorized) {
            RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES)
        }
    }

    // Atur jam buka toko
    override fun onEditShopScheduleClicked() {
        shopPageSettingTracking?.clickSetOpenShopTime(customDimensionShopPage)
        whenRoleAuthorized(shopSettingAccess.isInfoAccessAuthorized) {
            RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_EDIT_SCHEDULE)
        }
    }

    // Lihat daftar produk
    override fun onDisplayProductsClicked() {
        shopPageSettingTracking?.clickSeeProduct(customDimensionShopPage)
        whenRoleAuthorized(shopSettingAccess.isProductManageAccessAuthorized) {
            RouteManager.route(activity, ApplinkConst.PRODUCT_MANAGE)
        }
    }

    // Tambah dan ubah etalase
    override fun onEditEtalaseClicked() {
        shopPageSettingTracking?.clickAddAndEditEtalase(customDimensionShopPage)
        whenRoleAuthorized(shopSettingAccess.isEtalaseAccessAuthorized) {
            context?.let {
                val bundle = Bundle()
                bundle.putString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, "")
                bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
                bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
                bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopInfo!!.shopCore.shopID)
                bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_TYPE, customDimensionShopPage.shopType)

                val intent = RouteManager.getIntent(it, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
                intent.putExtra(EXTRA_BUNDLE, bundle)
                startActivityForResult(intent, REQUEST_CODE_ETALASE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> if (resultCode == Activity.RESULT_OK && data != null) {
                val etalaseId = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID)
                val etalaseName = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_NAME)
                val isNeedToReloadData = data.getBooleanExtra(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, false)

                val intent = ShopProductListResultActivity.createIntent(
                        activity,
                        shopId,
                        "",
                        etalaseId,
                        "",
                        "",
                        shopRef
                )
                intent.putExtra(ShopParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, isNeedToReloadData)
                startActivity(intent)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // Pusat bantuan
    override fun onGetSupportClicked() {
        shopPageSettingTracking?.clickPusatBantuan(customDimensionShopPage)
        RouteManager.route(activity, ApplinkConst.CONTACT_US_NATIVE)
    }

    // Pusat Seller
    override fun onGetTipsClicked() {
        shopPageSettingTracking?.clickPusatSeller(customDimensionShopPage)
        RouteManager.route(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW, SELLER_CENTER_URL))
    }

    // Tambah dan ubah lokasi toko
    override fun onEditLocationClicked() {
        shopPageSettingTracking?.clickAddAndEditShopLocation(customDimensionShopPage)
        whenRoleAuthorized(shopSettingAccess.isAddressAccessAuthorized) {
            RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS)
        }
    }

    // Atur layanan pengiriman
    override fun onManageShippingServiceClicked() {
        shopPageSettingTracking?.clickSetShippingService(customDimensionShopPage)
        whenRoleAuthorized(shopSettingAccess.isShipmentAccessAuthorized) {
            RouteManager.route(activity, ApplinkConst.SELLER_SHIPPING_EDITOR)
        }
    }
}
