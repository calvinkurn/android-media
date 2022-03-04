package com.tokopedia.shop.settings.setting.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seller.active.common.service.UpdateShopActiveService
import com.tokopedia.shop.common.constant.ShopCommonExtraConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.EXTRA_BUNDLE
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.analytics.ShopPageTrackingShopPageSetting
import com.tokopedia.shop.settings.analytics.model.CustomDimensionShopPageSetting
import com.tokopedia.shop.settings.common.di.Constant
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.shop.settings.databinding.FragmentShopPageSettingBinding
import com.tokopedia.shop.settings.setting.data.*
import com.tokopedia.shop.settings.setting.view.adapter.ShopPageSettingAdapter
import com.tokopedia.shop.settings.setting.view.viewmodel.ShopPageSettingViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import javax.inject.Named

class ShopPageSettingFragment : BaseDaggerFragment(),
        ShopPageSettingAdapter.ProfileItemClickListener,
        ShopPageSettingAdapter.ProductItemClickListener,
        ShopPageSettingAdapter.SupportItemClickListener,
        ShopPageSettingAdapter.ShippingItemClickListener {

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_DOMAIN = "domain"
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
    @Named(Constant.SHOP_SETTING_VIEW_MODEL_FACTORY)
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentShopPageSettingBinding>()

    private var shopPageSettingViewModel: ShopPageSettingViewModel? = null
    private var errorView: EmptyStateUnify? = null
    private var dashboardView: LinearLayout? = null
    private var shopPageSettingView: RecyclerView? = null
    private var loadingView: View? = null
    private val isOfficial: Boolean
        get() = shopInfo?.goldOS?.isOfficial == 1
    private val isGold: Boolean
        get() = shopInfo?.goldOS?.isGold == 1
    private val customDimensionShopPage: CustomDimensionShopPageSetting by lazy {
        CustomDimensionShopPageSetting.create(shopId, isOfficial, isGold)
    }

    private var remoteConfig: RemoteConfig? = null

    private var shopId: String? = null
    private var shopDomain: String? = null
    private var shopInfo: ShopInfo? = null
    private var shopSettingAccess = ShopSettingAccess()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        remoteConfig = FirebaseRemoteConfigImpl(activity)

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
            shopPageSettingViewModel = ViewModelProvider(this, viewModelFactory).get(ShopPageSettingViewModel::class.java)
        }

        shopPageSettingViewModel?.shopInfoResp?.observe(this, {
            when (it) {
                is Success -> onSuccessGetShopInfo(it.data)
                is Fail -> onErrorGetShopInfo(it.throwable)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentShopPageSettingBinding.inflate(inflater, container, false)
        return binding?.root as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateShopActiveStatus()
        // setup views
        errorView = binding?.errorView
        dashboardView = binding?.dashboardLayout
        dashboardView?.setOnClickListener { onDashboardClick() }
        shopPageSettingView = binding?.rvShopPageSetting
        loadingView = binding?.loaderShopPageSetting

        // setup shop page setting recycler view
        val shopPageSettingView = binding?.rvShopPageSetting
        val linearLayoutManager = LinearLayoutManager(activity)
        val shopPageSettingAdapter = ShopPageSettingAdapter(this, this, this, this)
        shopPageSettingView?.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = shopPageSettingAdapter
        }
        shopPageSettingView?.addItemDecoration(DividerItemDecoration(activity))

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

    private fun updateShopActiveStatus() {
        context?.let { UpdateShopActiveService.startService(it) }
    }

    fun onBackPressed() {
        ShopPageTrackingShopPageSetting.clickBackArrow(customDimensionShopPage)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
        getComponent(ShopSettingsComponent::class.java).inject(this)
    }

    private fun onSuccessGetShopInfo(shopInfo: ShopInfo) {
        this.shopInfo = shopInfo
        customDimensionShopPage.updateCustomDimensionData(shopId, isOfficial, isGold)
        setViewState(VIEW_CONTENT)
    }

    private fun onErrorGetShopInfo(e: Throwable?) {
        setViewState(VIEW_ERROR)
        errorView?.emptyStateDescription = ErrorHandler.getErrorMessage(activity, e)
        errorView?.setPrimaryCTAClickListener { getShopInfo() }
    }

    private fun getShopInfo(isRefresh: Boolean = false) {
        if (userSession.isShopOwner) {
            setViewState(VIEW_CONTENT)
        } else {
            setViewState(VIEW_LOADING)
        }
        shopPageSettingViewModel?.getShop(shopId, shopDomain, isRefresh)
    }

    private fun setViewState(viewState: Int) {
        when (viewState) {
            VIEW_LOADING -> {
                errorView?.hide()
                loadingView?.visible()
                dashboardView?.visible()
                shopPageSettingView?.gone()
            }
            VIEW_ERROR -> {
                errorView?.visible()
                loadingView?.gone()
                dashboardView?.hide()
                shopPageSettingView?.hide()
            }
            else -> {
                errorView?.hide()
                loadingView?.gone()
                dashboardView?.visible()
                shopPageSettingView?.visible()
            }
        }
    }

    // Dashboard Toko
    private fun onDashboardClick() {
        ShopPageTrackingShopPageSetting.clickShopDashboard(customDimensionShopPage)
        if (GlobalConfig.isSellerApp()) {
            RouteManager.route(activity, ApplinkConst.SellerApp.SELLER_APP_HOME)
        } else {
            RouteManager.route(activity, ApplinkConstInternalSellerapp.SELLER_MENU)
        }
    }

    private fun observeRoleAccess() {
        shopPageSettingViewModel?.shopSettingAccessLiveData?.observe(viewLifecycleOwner) { result ->
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
        ShopPageTrackingShopPageSetting.clickChangeShopProfile(customDimensionShopPage)
        whenRoleAuthorized(shopSettingAccess.isInfoAccessAuthorized) {
            RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO)
        }
    }

    // Ubah catatan toko
    override fun onChangeShopNoteClicked() {
        ShopPageTrackingShopPageSetting.clickChangeShopNote(customDimensionShopPage)
        whenRoleAuthorized(shopSettingAccess.isNotesAccessAuthorized) {
            RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES)
        }
    }

    // Atur jam buka toko
    override fun onEditShopScheduleClicked() {
        ShopPageTrackingShopPageSetting.clickSetOpenShopTime(customDimensionShopPage)
        whenRoleAuthorized(shopSettingAccess.isInfoAccessAuthorized) {
            RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_OPERATIONAL_HOURS)
        }
    }

    // Lihat daftar produk
    override fun onDisplayProductsClicked() {
        ShopPageTrackingShopPageSetting.clickSeeProduct(customDimensionShopPage)
        whenRoleAuthorized(shopSettingAccess.isProductManageAccessAuthorized) {
            RouteManager.route(activity, ApplinkConst.PRODUCT_MANAGE)
        }
    }

    // Tambah dan ubah etalase
    override fun onEditEtalaseClicked() {
        ShopPageTrackingShopPageSetting.clickAddAndEditEtalase(customDimensionShopPage)
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
                context?.let{
                    val etalaseId = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID)
                    val isNeedToReloadData = data.getBooleanExtra(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, false)
                    val shopPageProductListIntent = RouteManager.getIntent(
                            it,
                            ApplinkConstInternalMarketplace.SHOP_PAGE_PRODUCT_LIST,
                            shopId,
                            etalaseId
                    )
                    shopPageProductListIntent.putExtra(ShopCommonExtraConstant.EXTRA_IS_NEED_TO_RELOAD_DATA, isNeedToReloadData)
                    startActivity(shopPageProductListIntent)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // Pusat bantuan
    override fun onGetSupportClicked() {
        ShopPageTrackingShopPageSetting.clickPusatBantuan(customDimensionShopPage)
        RouteManager.route(activity, ApplinkConst.CONTACT_US_NATIVE)
    }

    // Pusat Seller
    override fun onGetTipsClicked() {
        ShopPageTrackingShopPageSetting.clickPusatSeller(customDimensionShopPage)
        RouteManager.route(activity, String.format("%s?url=%s", ApplinkConst.WEBVIEW, SELLER_CENTER_URL))
    }

    // Tambah dan ubah lokasi toko
    override fun onEditLocationClicked() {
        ShopPageTrackingShopPageSetting.clickAddAndEditShopLocation(customDimensionShopPage)
        whenRoleAuthorized(shopSettingAccess.isAddressAccessAuthorized) {
            RouteManager.route(activity, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS)
        }
    }

    // Atur layanan pengiriman
    override fun onManageShippingServiceClicked() {
        ShopPageTrackingShopPageSetting.clickSetShippingService(customDimensionShopPage)
        whenRoleAuthorized(shopSettingAccess.isShipmentAccessAuthorized) {
            RouteManager.route(activity, ApplinkConst.SELLER_SHIPPING_EDITOR)
        }
    }
}
