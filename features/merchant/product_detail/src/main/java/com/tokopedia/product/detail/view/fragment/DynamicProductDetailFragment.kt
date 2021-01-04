package com.tokopedia.product.detail.view.fragment

import android.animation.Animator
import android.app.Activity
import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.SparseIntArray
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator
import com.tokopedia.abstraction.Actions.interfaces.ActionUIDelegate
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.*
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.atc_common.data.model.request.AddToCartOccRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.utils.TradeInUtils
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.drawable.CountDrawable
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.device.info.permission.ImeiPermissionAsker
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.gallery.ImageReviewGalleryActivity
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.BuildConfig
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_APPLINK_AVAILABLE_VARIANT
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_APPLINK_IS_VARIANT_SELECTED
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_APPLINK_SHOP_ID
import com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.constant.TopAdsShopCategoryTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.product.TopAdsGetProductManage
import com.tokopedia.product.detail.common.data.model.product.YoutubeVideo
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductDataModel
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.model.financing.FtInstallmentCalculationDataResponse
import com.tokopedia.product.detail.data.model.restrictioninfo.RestrictionInfoResponse
import com.tokopedia.product.detail.data.util.*
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAM_DIRECTED_FROM_MANAGE_OR_PDP
import com.tokopedia.product.detail.data.util.VariantMapper.generateVariantString
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.estimasiongkir.view.activity.RatesEstimationDetailActivity
import com.tokopedia.product.detail.imagepreview.view.activity.ImagePreviewPdpActivity
import com.tokopedia.product.detail.view.activity.CourierActivity
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.product.detail.view.activity.ProductYoutubePlayerActivity
import com.tokopedia.product.detail.view.activity.WholesaleActivity
import com.tokopedia.product.detail.view.adapter.dynamicadapter.DynamicProductDetailAdapter
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactoryImpl
import com.tokopedia.product.detail.view.bottomsheet.OvoFlashDealsBottomSheet
import com.tokopedia.product.detail.view.bottomsheet.ShopStatusInfoBottomSheet
import com.tokopedia.product.detail.view.fragment.partialview.PartialButtonActionView
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.listener.PartialButtonActionListener
import com.tokopedia.product.detail.view.util.*
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.detail.view.viewmodel.ProductDetailSharedViewModel
import com.tokopedia.product.detail.view.widget.*
import com.tokopedia.product.info.view.ProductFullDescriptionActivity
import com.tokopedia.product.info.view.bottomsheet.ProductDetailBottomSheetListener
import com.tokopedia.product.info.view.bottomsheet.ProductDetailInfoBottomSheet
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.constant.Constant
import com.tokopedia.purchase_platform.common.feature.checkout.ShipmentFormRequest
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.referral.Constants
import com.tokopedia.referral.ReferralAction
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant.EXTRA_BUNDLE
import com.tokopedia.shop.common.widget.PartialButtonShopFollowersListener
import com.tokopedia.shop.common.widget.PartialButtonShopFollowersView
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.stickylogin.view.StickyLoginView
import com.tokopedia.topads.detail_sheet.TopAdsDetailSheet
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.user.session.UserSession
import com.tokopedia.variant_common.model.ProductVariantCommon
import com.tokopedia.variant_common.model.VariantCategory
import com.tokopedia.variant_common.model.VariantOptionWithAttribute
import com.tokopedia.variant_common.util.VariantCommonMapper
import com.tokopedia.variant_common.view.ProductVariantListener
import kotlinx.android.synthetic.main.dynamic_product_detail_fragment.*
import kotlinx.android.synthetic.main.menu_item_cart.view.*
import kotlinx.android.synthetic.main.partial_layout_button_action.*
import kotlinx.android.synthetic.main.partial_layout_button_action.view.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Separator Rule
 * Without separator : ProductSnapshotViewHolder
 * Bottom separator : ProductVariantViewHolder, ProductNotifyMeViewHolder
 * Top separator : All of the view holder except above
 */

class DynamicProductDetailFragment : BaseListFragment<DynamicPdpDataModel, DynamicProductDetailAdapterFactoryImpl>(),
        DynamicProductDetailListener,
        ProductVariantListener,
        ProductAccessRequestDialogFragment.Listener,
        PartialButtonActionListener,
        ProductDetailBottomSheetListener,
        PartialButtonShopFollowersListener {

    companion object {
        fun newInstance(productId: String? = null,
                        warehouseId: String? = null,
                        shopDomain: String? = null,
                        productKey: String? = null,
                        isFromDeeplink: Boolean = false,
                        isAffiliate: Boolean = false,
                        trackerAttribution: String? = null,
                        trackerListName: String? = null,
                        affiliateString: String? = null,
                        deeplinkUrl: String? = null,
                        layoutId: String? = null) = DynamicProductDetailFragment().also {
            it.arguments = Bundle().apply {
                productId?.let { pid -> putString(ProductDetailConstant.ARG_PRODUCT_ID, pid) }
                warehouseId?.let { whId -> putString(ProductDetailConstant.ARG_WAREHOUSE_ID, whId) }
                productKey?.let { pkey -> putString(ProductDetailConstant.ARG_PRODUCT_KEY, pkey) }
                shopDomain?.let { domain -> putString(ProductDetailConstant.ARG_SHOP_DOMAIN, domain) }
                trackerAttribution?.let { attribution -> putString(ProductDetailConstant.ARG_TRACKER_ATTRIBUTION, attribution) }
                trackerListName?.let { listName -> putString(ProductDetailConstant.ARG_TRACKER_LIST_NAME, listName) }
                affiliateString?.let { affiliateString -> putString(ProductDetailConstant.ARG_AFFILIATE_STRING, affiliateString) }
                deeplinkUrl?.let { deeplinkUrl -> putString(ProductDetailConstant.ARG_DEEPLINK_URL, deeplinkUrl) }
                layoutId?.let { layoutId -> putString(ProductDetailConstant.ARG_LAYOUT_ID, layoutId) }
                putBoolean(ProductDetailConstant.ARG_FROM_DEEPLINK, isFromDeeplink)
                putBoolean(ProductDetailConstant.ARG_FROM_AFFILIATE, isAffiliate)
            }
        }
    }

    @Inject
    lateinit var productDetailTracking: ProductDetailTracking

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var sharedViewModel: ProductDetailSharedViewModel? = null

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DynamicProductDetailViewModel::class.java)
    }

    private val nplFollowersButton: PartialButtonShopFollowersView? by lazy {
        base_btn_follow?.run {
            PartialButtonShopFollowersView.build(this, this@DynamicProductDetailFragment)
        }
    }

    //Listener function
    private var initToolBarMethod: (() -> Unit)? = null

    //Data
    private var tickerDetail: StickyLoginTickerPojo.TickerDetail? = null
    private var topAdsGetProductManage: TopAdsGetProductManage = TopAdsGetProductManage()
    private lateinit var remoteConfig: RemoteConfig

    // This productId is only use for backend hit
    private var productId: String? = null
    private var productKey: String? = null
    private var shopDomain: String? = null
    private var isAffiliate = false
    private var affiliateString: String? = null
    private var deeplinkUrl: String = ""
    private var isFromDeeplink: Boolean = false
    private var layoutId: String = ""
    private var trackerAttributionPdp: String? = ""
    private var trackerListNamePdp: String? = ""
    private var warehouseId: String? = null
    private var doActivityResult = true
    private var shouldFireVariantTracker = true
    private var pdpUiUpdater: PdpUiUpdater? = PdpUiUpdater(mapOf())
    private var enableCheckImeiRemoteConfig = false
    private var alreadyPerformSellerMigrationAction = false
    private var isAutoSelectVariant = false
    private var alreadyHitSwipeTracker: DynamicProductDetailSwipeTrackingState? = null
    private var alreadyHitVideoTracker: Boolean = false
    private var shouldRefreshProductInfoBottomSheet = false

    //View
    private var varToolbar: Toolbar? = null
    private lateinit var actionButtonView: PartialButtonActionView
    private var stickyLoginView: StickyLoginView? = null
    private var shouldShowCartAnimation = false
    private var loadingProgressDialog: ProgressDialog? = null
    private var productVideoCoordinator: ProductVideoCoordinator? = null
    private val adapterFactory by lazy { DynamicProductDetailAdapterFactoryImpl(this, this) }
    private val dynamicAdapter by lazy { DynamicProductDetailAdapter(adapterFactory, this) }
    private var menu: Menu? = null
    private var navToolbar: NavToolbar? = null
    private var toasterWishlistText = ""

    private var productDaggerComponent: ProductDetailComponent? = null

    private val tradeinDialog: ProductAccessRequestDialogFragment? by lazy {
        setupTradeinDialog()
    }

    private val topAdsDetailSheet: TopAdsDetailSheet? by lazy {
        TopAdsDetailSheet.newInstance()
    }

    private val recommendationCarouselPositionSavedState = SparseIntArray()

    private val irisSessionId by lazy {
        context?.let { IrisSession(it).getSessionId() } ?: ""
    }

    private val shareProductInstance by lazy {
        activity?.let {
            ProductShare(it, ProductShare.MODE_TEXT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dynamic_product_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        initBtnAction()
        navAbTestCondition({ initNavToolbar() }, { initToolbar() })
        initStickyLogin(view)
        renderInitialAffiliate()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean = false

    override fun hasInitialSwipeRefresh(): Boolean = true

    override fun onSwipeRefresh() {
        productVideoCoordinator?.onDestroy()
        recommendationCarouselPositionSavedState.clear()
        shouldRefreshProductInfoBottomSheet = true
        isLoadingInitialData = true
        ticker_occ_layout.gone()
        loadProductData(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            doActivityResult = savedInstanceState.getBoolean(ProductDetailConstant.SAVED_ACTIVITY_RESULT, true)
        }
        activity?.let {
            productDaggerComponent = DaggerProductDetailComponent.builder()
                    .baseAppComponent((it.application as BaseMainApplication).baseAppComponent)
                    .build()
        }

        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getString(ProductDetailConstant.ARG_PRODUCT_ID)
            warehouseId = it.getString(ProductDetailConstant.ARG_WAREHOUSE_ID)
            productKey = it.getString(ProductDetailConstant.ARG_PRODUCT_KEY)
            shopDomain = it.getString(ProductDetailConstant.ARG_SHOP_DOMAIN)
            trackerAttributionPdp = it.getString(ProductDetailConstant.ARG_TRACKER_ATTRIBUTION)
            trackerListNamePdp = it.getString(ProductDetailConstant.ARG_TRACKER_LIST_NAME)
            affiliateString = it.getString(ProductDetailConstant.ARG_AFFILIATE_STRING)
            isAffiliate = it.getBoolean(ProductDetailConstant.ARG_FROM_AFFILIATE, false)
            deeplinkUrl = it.getString(ProductDetailConstant.ARG_DEEPLINK_URL, "")
            isFromDeeplink = it.getBoolean(ProductDetailConstant.ARG_FROM_DEEPLINK, false)
            layoutId = it.getString(ProductDetailConstant.ARG_LAYOUT_ID, "")
        }

        sharedViewModel = ViewModelProvider(requireActivity()).get(ProductDetailSharedViewModel::class.java)
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(androidx.core.content.ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
        setHasOptionsMenu(true)
        assignDeviceId()
        setupRemoteConfig()

        loadProductData()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // If the activity being destroyed and onActivityResult start afterward
        // Then just ignore onActivityResult with this variable
        outState.putBoolean(ProductDetailConstant.SAVED_ACTIVITY_RESULT, false)
    }

    override fun onPause() {
        super.onPause()
        trackVideoState()
        if (::trackingQueue.isInitialized) {
            trackingQueue.sendAll()
            if (alreadyHitSwipeTracker != null) {
                alreadyHitSwipeTracker = DynamicProductDetailAlreadyHit
            }
        }
    }

    override fun onResume() {
        super.onResume()
        reloadCartCounter()
    }

    private fun trackVideoState() {
        if (!alreadyHitVideoTracker && productVideoCoordinator != null) {
            val videoTrackerData = viewModel.videoTrackerData
            val isAutoPlay = if (context == null) false else DeviceConnectionInfo.isConnectWifi(requireContext())

            videoTrackerData?.let {
                DynamicProductDetailTracking.Click.eventVideoStateChange(
                        viewModel.getDynamicProductInfoP1, viewModel.userId, DynamicProductDetailTracking.generateComponentTrackModel(pdpUiUpdater?.mediaMap, 0),
                        videoTrackerData.first,videoTrackerData.second, isAutoPlay
                )
                alreadyHitVideoTracker = true
            }
        }
    }

    private fun reloadCartCounter() {
        activity?.run {
            if (isAdded) {
                navAbTestCondition({ setNavToolBarCartCounter() }, {
                    menu?.let {
                        if (it.size() > 2) {
                            val menuCart = it.findItem(R.id.action_cart)
                            menuCart.actionView.cart_image_view.tag = R.drawable.ic_product_cart_counter_dark
                            setBadgeMenuCart(menuCart)
                        }
                    }
                })
            }
        }
    }

    private fun setNavToolBarCartCounter(){
        val localCacheHandler = LocalCacheHandler(context, CartConstant.CART)
        val cartCount = localCacheHandler.getInt(CartConstant.CACHE_TOTAL_CART, 0)
        navToolbar?.setBadgeCounter(IconList.ID_CART, if (cartCount > ProductDetailConstant.CART_MAX_COUNT) {
            getString(R.string.pdp_label_cart_count_max).toIntOrZero()
        } else if (!viewModel.isUserSessionActive) {
            0
        } else {
            cartCount
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        // handling menu toolbar / cart counter / settings / etc
        activity?.let {
            handlingMenuPreparation(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed(); true
            }
            R.id.action_share -> {
                shareProductFromToolbar(); true
            }
            R.id.action_cart -> {
                gotoCart(); true
            }
            R.id.action_report -> {
                reportProductFromToolbar(); true
            }
            R.id.action_warehouse -> {
                warehouseProduct(); true
            }
            R.id.action_etalase -> {
                moveProductToEtalase(); true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSeeMoreDescriptionClicked(dataContent: List<ProductDetailInfoContent>) {
        activity?.let {
            val productDetailSheet = ProductDetailInfoBottomSheet()
            val cacheManager = SaveInstanceCacheManager(it, true)
            val parcelData = DynamicProductDetailMapper.generateProductInfoParcel(
                    viewModel.getDynamicProductInfoP1,
                    viewModel.variantData?.sizeChart ?: "",
                    dataContent, shouldRefreshProductInfoBottomSheet
            )
            cacheManager.put(ProductDetailInfoBottomSheet::class.java.simpleName, parcelData)

            productDetailSheet.arguments = Bundle().apply {
                putString(ProductDetailInfoBottomSheet.PRODUCT_DETAIL_INFO_PARCEL_KEY, cacheManager.id)
            }
            shouldRefreshProductInfoBottomSheet = false
            productDetailSheet.show(it.supportFragmentManager, productDaggerComponent, this)
        }
    }

    override fun createAdapterInstance(): BaseListAdapter<DynamicPdpDataModel, DynamicProductDetailAdapterFactoryImpl> {
        return dynamicAdapter
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.rv_pdp
    }

    override fun getSwipeRefreshLayoutResourceId(): Int {
        return R.id.swipeRefreshPdp
    }

    override fun getAdapterTypeFactory(): DynamicProductDetailAdapterFactoryImpl = adapterFactory

    override fun onItemClicked(t: DynamicPdpDataModel) {
        //No OP
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.let {
            productDaggerComponent?.inject(this)
        }
    }

    override fun loadData(page: Int) {
        //NO OP
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_product_detail_dark, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        initToolBarMethod?.invoke()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activity != null) {
            AdultManager.handleActivityResult(activity!!, requestCode, resultCode, data)
        }
        when (requestCode) {
            ApplinkConstInternalCategory.FINAL_PRICE_REQUEST_CODE,
            ApplinkConstInternalCategory.TRADEIN_HOME_REQUEST -> {
                data?.let {
                    val deviceId = data.getStringExtra(TradeInParams.PARAM_DEVICE_ID)
                    val phoneType = data.getStringExtra(TradeInParams.PARAM_PHONE_TYPE)
                            ?: "none/other"
                    val phonePrice = data.getStringExtra(TradeInParams.PARAM_PHONE_PRICE)
                    DynamicProductDetailTracking.TradeIn.eventAddToCartFinalPrice(phoneType,
                            phonePrice,
                            deviceId,
                            viewModel.userId,
                            viewModel.getDynamicProductInfoP1)
                    buyAfterTradeinDiagnose(deviceId, phoneType, phonePrice)
                }
            }
            ProductDetailConstant.REQUEST_CODE_CHECKOUT -> {
                updateCartNotification()
            }
            ProductDetailConstant.REQUEST_CODE_ETALASE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedEtalaseId = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_ID)
                    val selectedEtalaseName = data.getStringExtra(ShopShowcaseParamConstant.EXTRA_ETALASE_NAME)
                    val dynamicProductInfoData = viewModel.getDynamicProductInfoP1
                            ?: DynamicProductInfoP1()
                    if (dynamicProductInfoData.basic.productID.isNotEmpty() && !selectedEtalaseName.isNullOrEmpty()) {
                        showProgressDialog(onCancelClicked = { viewModel.cancelEtalaseUseCase() })
                        viewModel.moveProductToEtalase(dynamicProductInfoData.basic.productID, selectedEtalaseId, selectedEtalaseName)
                    }
                }
            }
            ProductDetailConstant.REQUEST_CODE_EDIT_PRODUCT -> {
                if (resultCode == Activity.RESULT_OK && doActivityResult) {
                    onSwipeRefresh()
                }
            }
            ProductDetailConstant.REQUEST_CODE_LOGIN -> {
                hideProgressDialog()
                if (resultCode == Activity.RESULT_OK && doActivityResult) {
                    onSwipeRefresh()
                }
                updateStickyState()
                updateActionButtonShadow()

                if (resultCode == Activity.RESULT_OK && viewModel.userSessionInterface.isLoggedIn) {
                    when (viewModel.talkLastAction) {
                        is DynamicProductDetailTalkGoToWriteDiscussion -> goToWriteActivity()
                        is DynamicProductDetailTalkGoToReplyDiscussion -> goToReplyActivity((viewModel.talkLastAction as DynamicProductDetailTalkGoToReplyDiscussion).questionId)
                    }
                }
            }
            ProductDetailConstant.REQUEST_CODE_REPORT -> {
                if (resultCode == Activity.RESULT_OK)
                    view?.showToasterSuccess(getString(R.string.success_to_report))
            }
            ProductDetailConstant.REQUEST_CODE_IMAGE_PREVIEW -> {
                if (data != null) {
                    val isWishlisted = data.getBooleanExtra(ImagePreviewPdpActivity.RESPONSE_CODE_IMAGE_RPEVIEW, false)
                    pdpUiUpdater?.updateWishlistData(isWishlisted)
                    dynamicAdapter.notifyBasicContentWithPayloads(pdpUiUpdater?.basicContentMap, ProductDetailConstant.PAYLOAD_WISHLIST)
                }
            }
            ProductDetailConstant.REQUEST_CODE_SHOP_INFO -> {
                if (data != null) {
                    val isFavoriteFromShopPage = data.getBooleanExtra(ProductDetailConstant.SHOP_STATUS_FAVOURITE, false)
                    val isUserLoginFromShopPage = data.getBooleanExtra(ProductDetailConstant.SHOP_STICKY_LOGIN, false)
                    val wasFavorite = pdpUiUpdater?.shopInfoMap?.isFavorite
                            ?: pdpUiUpdater?.shopCredibility?.isFavorite ?: return

                    if (isUserLoginFromShopPage) {
                        updateStickyState()
                        updateActionButtonShadow()
                    }
                    if (isFavoriteFromShopPage != wasFavorite) {
                        onSuccessFavoriteShop(true)
                    }
                }
            }
            ProductDetailConstant.REQUEST_CODE_TOP_CHAT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val favoriteData = data.getStringExtra(ApplinkConst.Chat.SHOP_FOLLOWERS_CHAT_KEY)
                    if (favoriteData != null) {
                        val isFavoriteFromTopChat = favoriteData == "true"
                        val wasFavorite = pdpUiUpdater?.shopInfoMap?.isFavorite
                                ?: pdpUiUpdater?.shopCredibility?.isFavorite ?: return

                        if (isFavoriteFromTopChat != wasFavorite) {
                            onSuccessFavoriteShop(true)
                        }
                    }
                }
            }
            else ->
                super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        hideProgressDialog()
        viewModel.p2Data.removeObservers(this)
        viewModel.p2Other.removeObservers(this)
        viewModel.productLayout.removeObservers(this)
        viewModel.p2Login.removeObservers(this)
        viewModel.productInfoP3.removeObservers(this)
        viewModel.loadTopAdsProduct.removeObservers(this)
        viewModel.moveToWarehouseResult.removeObservers(this)
        viewModel.moveToEtalaseResult.removeObservers(this)
        viewModel.updatedImageVariant.removeObservers(this)
        viewModel.initialVariantData.removeObservers(this)
        viewModel.onVariantClickedData.removeObservers(this)
        viewModel.toggleTeaserNotifyMe.removeObservers(this)
        viewModel.addToCartLiveData.removeObservers(this)
        viewModel.discussionMostHelpful.removeObservers(this)
        viewModel.flush()
        super.onDestroy()
    }

    override fun gotoVideoPlayer(youtubeVideos: List<YoutubeVideo>, index: Int) {
        context?.let {
            if (YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(it.applicationContext)
                    == YouTubeInitializationResult.SUCCESS) {
                startActivity(ProductYoutubePlayerActivity.createIntent(it, youtubeVideos.map { it.url }, index))
            } else {
                // Handle if user didn't have any apps to open Youtube * Usually rooted phone
                try {
                    startActivity(Intent(Intent.ACTION_VIEW,
                            Uri.parse(ProductDetailConstant.URL_YOUTUBE + youtubeVideos[index].url)))
                } catch (e: Throwable) {
                }
            }
        }
    }

    override fun getApplicationContext(): Application? {
        return activity?.application
    }

    override fun getLifecycleFragment(): Lifecycle {
        return lifecycle
    }

    override fun gotoDescriptionTab(textDescription: String, componentTrackDataModel: ComponentTrackDataModel) {
        viewModel.getDynamicProductInfoP1?.let {
            val data = ProductDetailUtil.generateDescriptionData(it, textDescription)
            context?.let { ctx ->
                startActivity(ProductFullDescriptionActivity.createIntent(ctx, data))
                activity?.overridePendingTransition(R.anim.pull_up, 0)
                DynamicProductDetailTracking.Click.eventClickProductDescriptionReadMore(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
            }
        }
    }

    /**
     * ProductMiniShopInfoViewHolder
     */
    override fun onMiniShopInfoClicked(componentTrackDataModel: ComponentTrackDataModel) {
        DynamicProductDetailTracking.Click.eventClickShopMiniShopInfo(viewModel.getDynamicProductInfoP1, componentTrackDataModel, viewModel.userId)
        val shopCredibilityIndex = dynamicAdapter.getItemComponentIndex(pdpUiUpdater?.shopCredibility)

        if (shopCredibilityIndex != RecyclerView.NO_POSITION) {
            scrollToPosition(shopCredibilityIndex)
        } else {
            scrollToPosition(dynamicAdapter.getItemComponentIndex(pdpUiUpdater?.shopInfoMap))
        }
    }

    /**
     * ImpressionComponent
     */
    override fun onImpressComponent(componentTrackDataModel: ComponentTrackDataModel) {
        when (componentTrackDataModel.componentName) {
            ProductDetailConstant.PRODUCT_PROTECTION -> DynamicProductDetailTracking.Impression
                    .eventEcommerceDynamicComponent(trackingQueue, componentTrackDataModel,
                            viewModel.getDynamicProductInfoP1, getPPTitleName(), getPurchaseProtectionUrl())
            else -> DynamicProductDetailTracking.Impression
                    .eventEcommerceDynamicComponent(trackingQueue, componentTrackDataModel,
                            viewModel.getDynamicProductInfoP1, "", "")

        }
    }

    /**
     * ProductShopInfoViewHolder
     */
    override fun onShopInfoClicked(itemId: Int, componentTrackDataModel: ComponentTrackDataModel) {
        when (itemId) {
            R.id.btn_favorite, R.id.btn_follow -> onShopFavoriteClick()
            R.id.send_msg_shop -> {
                DynamicProductDetailTracking.Click.eventButtonChatShopClicked(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                onShopChatClicked()
            }
            R.id.shop_ava, R.id.shop_name -> gotoShopDetail(componentTrackDataModel)
            else -> {

            }
        }
    }

    /**
     * ProductInfoViewHolder
     */
    override fun onCategoryClicked(url: String, componentTrackDataModel: ComponentTrackDataModel) {
        if (!GlobalConfig.isSellerApp()) {

            viewModel.getDynamicProductInfoP1?.basic?.category?.detail?.let {
                val categoryId = it.lastOrNull()?.id ?: ""
                val categoryName = it.lastOrNull()?.name ?: ""
                DynamicProductDetailTracking.Click.eventCategoryClicked(categoryId, categoryName, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
            }
            RouteManager.route(context, url)
        }
    }

    override fun onEtalaseClicked(url: String, componentTrackDataModel: ComponentTrackDataModel) {
        val etalaseId = viewModel.getDynamicProductInfoP1?.basic?.menu?.id ?: ""
        val etalaseName = viewModel.getDynamicProductInfoP1?.basic?.menu?.name ?: ""
        val shopId = viewModel.getDynamicProductInfoP1?.basic?.shopID ?: ""

        val intent = RouteManager.getIntent(context, if (etalaseId.isNotEmpty()) {
            UriUtil.buildUri(ApplinkConst.SHOP_ETALASE, shopId, etalaseId)
        } else {
            UriUtil.buildUri(ApplinkConst.SHOP, shopId)
        })
        DynamicProductDetailTracking.Click.eventEtalaseClicked(etalaseId, etalaseName, viewModel.getDynamicProductInfoP1, componentTrackDataModel)

        startActivity(intent)
    }

    override fun goToApplink(url: String) {
        RouteManager.route(context, url)
    }

    override fun onBbiInfoClick(url: String, title: String, componentTrackDataModel: ComponentTrackDataModel) {
        if (url.isNotEmpty()) {
            DynamicProductDetailTracking.Click.eventClickCustomInfo(title, viewModel.userId, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
            goToApplink(url)
        }
    }

    /**
     * ProductGeneralInfoViewHolder Listener
     */
    override fun onInfoClicked(name: String, componentTrackDataModel: ComponentTrackDataModel) {
        when (name) {
            ProductDetailConstant.PRODUCT_SHIPPING_INFO -> {
                val productP3Resp = viewModel.productInfoP3.value ?: ProductInfoP3()

                DynamicProductDetailTracking.Click.eventShippingRateEstimationClicked(
                        productP3Resp.addressModel?.postalCode ?: "",
                        productP3Resp.addressModel?.districtName ?: "",
                        viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                onShipmentClicked()
            }
            ProductDetailConstant.TRADE_IN -> {
                DynamicProductDetailTracking.Click.trackTradein(viewModel.tradeInParams.usedPrice, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                doAtc(ProductDetailConstant.TRADEIN_BUTTON)
            }
            ProductDetailConstant.PRODUCT_INSTALLMENT_INFO -> {
                DynamicProductDetailTracking.Click.eventClickPDPInstallmentSeeMore(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                openFtInstallmentBottomSheet(viewModel.p2Data.value?.productFinancingCalculationData
                        ?: FtInstallmentCalculationDataResponse())
            }
            ProductDetailConstant.PRODUCT_VARIANT_INFO -> {
                if (!GlobalConfig.isSellerApp()) {
                    DynamicProductDetailTracking.Click.eventClickVariant(generateVariantString(viewModel.variantData), viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                }
            }
            ProductDetailConstant.PRODUCT_WHOLESALE_INFO -> {
                val data = DynamicProductDetailMapper.mapToWholesale(viewModel.getDynamicProductInfoP1?.data?.wholesale)
                if (data != null && data.isNotEmpty()) {
                    DynamicProductDetailTracking.Click.eventClickWholesale(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                    context?.run {
                        startActivity(WholesaleActivity.getIntent(this, ArrayList(data)))
                    }
                }
            }
            ProductDetailConstant.BY_ME -> {
                onAffiliateClick()
                DynamicProductDetailTracking.Click.eventClickByMe(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
            }
            ProductDetailConstant.PRODUCT_PROTECTION -> {
                DynamicProductDetailTracking.Click.eventClickPDPInsuranceProtection(viewModel.getDynamicProductInfoP1, getPurchaseProtectionUrl(), componentTrackDataModel)
                openFtInsuranceBottomSheet(getPurchaseProtectionUrl())
            }
        }
    }

    private fun getPurchaseProtectionUrl(): String {
        pdpUiUpdater?.productProtectionMap?.let {
            return it.data.getOrNull(1)?.applink ?: ""
        }
        return ""
    }

    private fun getPPTitleName(): String {
        return pdpUiUpdater?.productProtectionMap?.title ?: ""
    }

    /**
     * ProductRecommendationViewHolder Listener
     */
    override fun sendTopAdsClick(topAdsUrl: String, productId: String, productName: String, productImageUrl: String) {
        context?.run {
            TopAdsUrlHitter(this::class.java.name).hitClickUrl(
                    this,
                    topAdsUrl,
                    productId,
                    productName,
                    productImageUrl
            )
        }
    }

    override fun sendTopAdsImpression(topAdsUrl: String, productId: String, productName: String, productImageUrl: String) {
        context?.run {
            TopAdsUrlHitter(this::class.java.name).hitImpressionUrl(
                    this,
                    topAdsUrl,
                    productId,
                    productName,
                    productImageUrl
            )
        }
    }

    override fun onChipFilterClicked(recommendationDataModel: ProductRecommendationDataModel, annotationChip: AnnotationChip, position: Int, filterPosition: Int) {
        DynamicProductDetailTracking.Click.eventClickSeeFilterAnnotation(annotationChip.recommendationFilterChip.value)
        viewModel.getRecommendation(recommendationDataModel, annotationChip, position, filterPosition)
    }

    override fun onSeeAllRecomClicked(pageName: String, applink: String, componentTrackDataModel: ComponentTrackDataModel) {
        DynamicProductDetailTracking.Click.eventClickSeeMoreRecomWidget(pageName, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
        RouteManager.route(context, applink)
    }

    override fun eventRecommendationClick(recomItem: RecommendationItem, position: Int, pageName: String, title: String, componentTrackDataModel: ComponentTrackDataModel) {
        DynamicProductDetailTracking.Click.eventRecommendationClick(
                recomItem, position, viewModel.isUserSessionActive, pageName, title, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
    }

    override fun eventRecommendationImpression(recomItem: RecommendationItem, position: Int, pageName: String, title: String, componentTrackDataModel: ComponentTrackDataModel) {
        if (::trackingQueue.isInitialized) {
            DynamicProductDetailTracking.Impression.eventRecommendationImpression(
                    trackingQueue, position, recomItem, viewModel.isUserSessionActive, pageName, title,
                    viewModel.getDynamicProductInfoP1, componentTrackDataModel)
        }
    }

    override fun getParentRecyclerViewPool(): RecyclerView.RecycledViewPool? {
        return getRecyclerView(view).recycledViewPool
    }

    override fun getRecommendationCarouselSavedState(): SparseIntArray {
        return recommendationCarouselPositionSavedState
    }

    override fun loadTopads(pageName: String) {
        viewModel.loadRecommendation(pageName)
    }

    /**
     * PageErrorViewHolder
     */
    override fun onRetryClicked(forceRefresh: Boolean) {
        clearAllData()
        showLoading()
        onSwipeRefresh()
    }

    override fun goToHomePageClicked() {
        (activity as? ProductDetailActivity)?.goToHomePageClicked()
    }

    override fun goToWebView(url: String) {
        RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
    }

    /**
     * ProductReviewViewHolder
     */
    override fun onSeeAllLastItemImageReview(componentTrackDataModel: ComponentTrackDataModel?) {
        DynamicProductDetailTracking.Click.onSeeAllLastItemImageReview(viewModel.getDynamicProductInfoP1, componentTrackDataModel
                ?: ComponentTrackDataModel())
        goToReviewImagePreview()
    }

    override fun onSeeAllTextView(componentTrackDataModel: ComponentTrackDataModel?) {
        viewModel.getDynamicProductInfoP1?.run {
            DynamicProductDetailTracking.Click.onSeeAllReviewTextView(this, viewModel.userId, componentTrackDataModel
                    ?: ComponentTrackDataModel())
            goToReviewDetail(basic.productID, getProductName)
        }
    }

    override fun onImageReviewClick(listOfImage: List<ImageReviewItem>, position: Int, componentTrackDataModel: ComponentTrackDataModel?) {
        context?.let {
            DynamicProductDetailTracking.Click.eventClickReviewOnBuyersImage(viewModel.getDynamicProductInfoP1, componentTrackDataModel
                    ?: ComponentTrackDataModel(), listOfImage[position].reviewId)
            val listOfImageReview: List<String> = listOfImage.map {
                it.imageUrlLarge ?: ""
            }

            ImageReviewGalleryActivity.moveTo(context, ArrayList(listOfImageReview), position)
        }
    }

    override fun onReviewClick() {
        viewModel.getDynamicProductInfoP1?.run {
            DynamicProductDetailTracking.Iris.eventReviewClickedIris(this, deeplinkUrl, basic.shopName)
            DynamicProductDetailTracking.Moengage.sendMoEngageClickReview(this)
            goToReviewDetail(basic.productID, getProductName)
        }
    }

    override fun onImageHelpfulReviewClick(listOfImages: List<String>, position: Int, reviewId: String?, componentTrackDataModel: ComponentTrackDataModel?) {
        DynamicProductDetailTracking.Click.eventClickReviewOnMostHelpfulReview(viewModel.getDynamicProductInfoP1, componentTrackDataModel
                ?: ComponentTrackDataModel(),
                reviewId)
        context?.let { ImageReviewGalleryActivity.moveTo(it, ArrayList(listOfImages), position) }
    }

    /**
     * ProductTickerViewHolder
     */
    override fun onTickerGeneralClicked(tickerTitle: String, tickerType: Int, url: String, componentTrackDataModel: ComponentTrackDataModel?, tickerDescription: String) {
        if (url.isEmpty()) return
        trackOnTickerClicked(tickerTitle, tickerType, componentTrackDataModel, tickerDescription)
        if (activity != null && RouteManager.isSupportApplink(activity, url)) {
            goToApplink(url)
        } else {
            openWebViewUrl(url)
        }
    }

    override fun onTickerShopClicked(tickerTitle: String, tickerType: Int, componentTrackDataModel: ComponentTrackDataModel?, tickerDescription: String) {
        activity?.let {
            trackOnTickerClicked(tickerTitle, tickerType, componentTrackDataModel, tickerDescription)
            //Make sure dont put your parameter inside constructor, it will cause crash when dont keep activity
            val shopStatusBs = ShopStatusInfoBottomSheet()
            shopStatusBs.statusInfo = viewModel.getShopInfo().statusInfo
            shopStatusBs.closedInfo = viewModel.getShopInfo().closedInfo
            shopStatusBs.isShopOwner = viewModel.isShopOwner()
            shopStatusBs.show(it.supportFragmentManager, "Shop Status BS")
        }
    }

    override fun onTickerGoToRecomClicked(tickerTitle: String, tickerType: Int, componentTrackDataModel: ComponentTrackDataModel?, tickerDescription: String) {
        trackOnTickerClicked(tickerTitle, tickerType, componentTrackDataModel, tickerDescription)
        goToRecommendation()
    }

    /**
     * ProductMerchantVoucherViewHolder
     */
    override fun isOwner(): Boolean = viewModel.isShopOwner()

    override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int, dataTrackDataModel: ComponentTrackDataModel) {
        activity?.let {
            //TOGGLE_MVC_OFF
            val shopId = viewModel.getDynamicProductInfoP1?.basic?.getShopId().orZero().toString()
            DynamicProductDetailTracking.Click.eventClickMerchantVoucherUse(merchantVoucherViewModel, shopId, position, viewModel.getDynamicProductInfoP1, dataTrackDataModel)
            showSnackbarClose(getString(R.string.title_voucher_code_copied))
        }
    }

    override fun onItemMerchantVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, componentTrackDataModel: ComponentTrackDataModel) {
        activity?.let {
            viewModel.getDynamicProductInfoP1?.run {
                val voucherId = merchantVoucherViewModel.voucherId
                DynamicProductDetailTracking.Click.eventClickMerchantVoucherSeeDetail(voucherId, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                val intent = MerchantVoucherDetailActivity.createIntent(it, voucherId,
                        merchantVoucherViewModel, basic.shopID)
                startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_MERCHANT_VOUCHER_DETAIL)
            }
        }
    }

    override fun onSeeAllMerchantVoucherClick(componentTrackDataModel: ComponentTrackDataModel) {
        activity?.let {
            viewModel.getDynamicProductInfoP1?.run {
                DynamicProductDetailTracking.Click.eventClickMerchantVoucherSeeAll(this, componentTrackDataModel)
                val intent = MerchantVoucherListActivity.createIntent(it, basic.shopID, basic.shopName)
                startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_MERCHANT_VOUCHER)
            }
        }
    }

    override fun onVideoFullScreenClicked() {
        activity?.let { activity ->
            productVideoCoordinator?.let {
                val trackerData = viewModel.getDynamicProductInfoP1
                it.pauseVideoAndSaveLastPosition()
                sharedViewModel?.updateVideoDetailData(ProductVideoDetailDataModel(it.getVideoDataModel(),
                        //Tracker Data
                        trackerData?.shopTypeString
                        ?: "",
                        trackerData?.basic?.shopID ?: "",
                        viewModel.userId, trackerData?.basic?.productID ?: ""))

                (activity as ProductDetailActivity).addNewFragment(ProductVideoDetailFragment())
                DynamicProductDetailTracking.Click.eventClickFullScreenVideo(viewModel.getDynamicProductInfoP1, viewModel.userId,
                        DynamicProductDetailTracking.generateComponentTrackModel(pdpUiUpdater?.mediaMap, 0))
            }
        }
    }

    override fun onVideoVolumeCLicked(isMute: Boolean) {
        DynamicProductDetailTracking.Click.eventClickVideoVolume(viewModel.getDynamicProductInfoP1, viewModel.userId,
                DynamicProductDetailTracking.generateComponentTrackModel(pdpUiUpdater?.mediaMap, 0), isMute)
    }

    override fun onVideoStateChange(stopDuration: Long, videoDuration:Long) {
        viewModel.updateVideoTrackerData(stopDuration, videoDuration)
    }

    override fun getProductVideoCoordinator(): ProductVideoCoordinator? {
        return productVideoCoordinator
    }

    /**
     * ProductSnapshotViewHolder
     */
    override fun onSwipePicture(type: String, url: String, position: Int, componentTrackDataModel: ComponentTrackDataModel?) {
        if (alreadyHitSwipeTracker != DynamicProductDetailAlreadyHit) {
            DynamicProductDetailTracking.Click.eventProductImageOnSwipe(viewModel.getDynamicProductInfoP1, componentTrackDataModel
                    ?: ComponentTrackDataModel(), trackingQueue, type, url, position)
            alreadyHitSwipeTracker = DynamicProductDetailAlreadySwipe
        }
    }

    override fun shouldShowWishlist(): Boolean {
        return !viewModel.isShopOwner()
    }

    override fun onMainImageClicked(componentTrackDataModel: ComponentTrackDataModel?, position: Int) {
        DynamicProductDetailTracking.Click.eventProductImageClicked(viewModel.getDynamicProductInfoP1, componentTrackDataModel
                ?: ComponentTrackDataModel())
        onImageClicked(position)
    }

    override fun onImageClicked(position: Int) {
        val isWishlisted = pdpUiUpdater?.basicContentMap?.isWishlisted ?: false
        val dynamicProductInfoData = viewModel.getDynamicProductInfoP1 ?: DynamicProductInfoP1()

        activity?.let {
            val images = dynamicProductInfoData.data.getImagePathExceptVideo() ?: return@let
            val intent = ImagePreviewPdpActivity.createIntent(it,
                    shopId = dynamicProductInfoData.basic.shopID,
                    productId = dynamicProductInfoData.basic.productID,
                    isWishlisted = isWishlisted,
                    imageUris = images,
                    imageDesc = null,
                    position = position)
            startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_IMAGE_PREVIEW)
        }
    }

    override fun txtTradeinClicked(componentTrackDataModel: ComponentTrackDataModel) {
        DynamicProductDetailTracking.Click.eventClickTradeInRibbon(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
        scrollToPosition(dynamicAdapter.getItemComponentIndex(pdpUiUpdater?.productTradeinMap))
    }

    override fun onAccept() {
        val usedPrice = viewModel.p2Data.value?.validateTradeIn?.usedPrice.toIntOrZero()
        if (usedPrice > 0) {
            goToHargaFinal()
        } else {
            viewModel.clearCacheP2Data()
            goToTradeInHome()
        }
    }

    override fun onDecline() {}

    override fun getProductFragmentManager(): FragmentManager {
        return childFragmentManager
    }

    override fun showAlertCampaignEnded() {
        dynamicAdapter.notifyBasicContentWithPayloads(pdpUiUpdater?.basicContentMap, null)
        activity?.let {
            Dialog(it, Dialog.Type.LONG_PROMINANCE).apply {
                setTitle(getString(R.string.campaign_expired_title))
                setDesc(getString(R.string.campaign_expired_descr))
                setBtnCancel(getString(R.string.close))
                setOnCancelClickListener {
                    onSwipeRefresh()
                    dismiss()
                }
            }.show()
        }
    }

    override fun onFabWishlistClicked(isActive: Boolean, componentTrackDataModel: ComponentTrackDataModel) {
        val productInfo = viewModel.getDynamicProductInfoP1
        if (viewModel.isUserSessionActive) {
            val productP3value = viewModel.productInfoP3.value
            if (productP3value != null) {
                if (isActive) {
                    productInfo?.basic?.productID?.let {
                        toasterWishlistText = getString(R.string.toaster_success_remove_wishlist)
                        viewModel.removeWishList(it,
                                onSuccessRemoveWishlist = this::onSuccessRemoveWishlist,
                                onErrorRemoveWishList = this::onErrorRemoveWishList)
                        DynamicProductDetailTracking.Click.eventPDPRemoveToWishlist(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                    }

                } else {
                    productInfo?.basic?.productID?.let {
                        toasterWishlistText = if (isProductOos()) getString(R.string.toaster_success_add_wishlist_from_fab) else getString(R.string.msg_success_add_wishlist)
                        addWishList()
                        productInfo.let {
                            DynamicProductDetailTracking.Moengage.eventPDPWishlistAppsFyler(it)
                        }
                        DynamicProductDetailTracking.Click.eventPDPAddToWishlist(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                    }
                }
                if (isAffiliate && productInfo?.basic?.productID?.isNotEmpty() == true) {
                    productDetailTracking.eventClickWishlistOnAffiliate(
                            viewModel.userId,
                            productInfo.basic.productID)
                }
            }
        } else {
            DynamicProductDetailTracking.Click.eventPDPAddToWishlistNonLogin(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
            goToLogin()
        }
    }

    override fun onDiscussionClicked(componentTrackDataModel: ComponentTrackDataModel?) {

        viewModel.getDynamicProductInfoP1?.run {
            DynamicProductDetailTracking.Iris.eventDiscussionClickedIris(this, deeplinkUrl, basic.shopName, componentTrackDataModel
                    ?: ComponentTrackDataModel())
            DynamicProductDetailTracking.Moengage.sendMoEngageClickDiskusi(this)
        }

        disscussionClicked()
    }

    override fun onDiscussionRefreshClicked() {
        viewModel.getDynamicProductInfoP1?.basic?.let {
            viewModel.getDiscussionMostHelpful(it.productID, it.shopID)
        }
    }

    override fun onDiscussionSendQuestionClicked(componentTrackDataModel: ComponentTrackDataModel?) {
        writeDiscussion {
            val totalAvailableVariants = (viewModel.variantData?.getBuyableVariantCount()
                    ?: 0).toString()
            viewModel.getDynamicProductInfoP1?.let {
                DynamicProductDetailTracking.Click.eventEmptyDiscussionSendQuestion(it, componentTrackDataModel, viewModel.userId, pdpUiUpdater?.productNewVariantDataModel?.isPartialySelected()?.not()
                        ?: false, totalAvailableVariants)
            }
        }
    }

    override fun goToTalkReading(componentTrackDataModel: ComponentTrackDataModel, numberOfThreadsShown: String) {
        viewModel.getDynamicProductInfoP1?.let {
            DynamicProductDetailTracking.Click.eventDiscussionSeeAll(it, componentTrackDataModel, viewModel.userId, numberOfThreadsShown)
        }
        goToReadingActivity()
    }

    override fun goToTalkReply(questionId: String, componentTrackDataModel: ComponentTrackDataModel, numberOfThreadsShown: String) {
        doActionOrLogin({
            viewModel.getDynamicProductInfoP1?.let {
                DynamicProductDetailTracking.Click.eventDiscussionDetails(it, componentTrackDataModel, viewModel.userId, questionId, numberOfThreadsShown)
            }
            goToReplyActivity(questionId)
        })
        viewModel.updateLastAction(DynamicProductDetailTalkGoToReplyDiscussion(questionId))
    }

    private fun goToReviewDetail(productId: String, productName: String) {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_REVIEW, productId)
            intent?.run {
                intent.putExtra(ProductDetailConstant.REVIEW_PRD_NM, productName)
                startActivity(intent)
            }
        }
    }

    private fun disscussionClicked() {
        goToReadingActivity()
    }

    private fun goToReviewImagePreview() {
        val productId = viewModel.getDynamicProductInfoP1?.basic?.getProductId() ?: 0
        ImageReviewGalleryActivity.moveTo(activity, productId)
    }

    private fun observeData() {
        observeP1()
        observeP2Data()
        observeP2Login()
        observeP3()
        observeToggleFavourite()
        observeToggleNotifyMe()
        observeMoveToWarehouse()
        observeMoveToEtalase()
        observeRecommendationProduct()
        observeImageVariantPartialyChanged()
        observeAddToCart()
        observeInitialVariantData()
        observeonVariantClickedData()
        observeDiscussionData()
        observeP2Other()
        observeTopAdsImageData()
        observeVideoDetail()
    }

    private fun observeVideoDetail() {
        sharedViewModel?.productVideoData?.observe(requireActivity(), {
            if (it.isEmpty()) return@observe
            productVideoCoordinator?.updateAndResume(it)
        })
    }

    private fun observeTopAdsImageData() {
        viewLifecycleOwner.observe(viewModel.topAdsImageView) { data ->
            data.doSuccessOrFail({
                if (!it.data.isNullOrEmpty()) {
                    pdpUiUpdater?.updateTopAdsImageData(it.data)
                    dynamicAdapter.notifyTopAdsBanner(pdpUiUpdater?.topAdsImageData)
                } else {
                    dynamicAdapter.removeComponentSection(pdpUiUpdater?.topAdsImageData)
                }
            }, {
                dynamicAdapter.removeComponentSection(pdpUiUpdater?.topAdsImageData)
            })
        }
    }

    private fun observeP2Other() {
        viewLifecycleOwner.observe(viewModel.p2Other) {
            if (it.helpfulReviews?.isEmpty() == true && viewModel.getDynamicProductInfoP1?.basic?.stats?.countReview.toIntOrZero() == 0) {
                dynamicAdapter.removeComponentSection(pdpUiUpdater?.productReviewMap)
                dynamicAdapter.removeComponentSection(pdpUiUpdater?.productReviewOldMap)
            }

            pdpUiUpdater?.updateDataP2General(it)
            dynamicAdapter.notifyItemComponentSections(pdpUiUpdater?.productDiscussionMostHelpfulMap, pdpUiUpdater?.productReviewMap, pdpUiUpdater?.productReviewOldMap)
            (activity as? ProductDetailActivity)?.stopMonitoringP2Other()
        }
    }

    private fun observeDiscussionData() {
        viewLifecycleOwner.observe(viewModel.discussionMostHelpful) { data ->
            data.doSuccessOrFail({
                pdpUiUpdater?.updateDiscussionData(it.data.discussionMostHelpful)
                dynamicAdapter.notifyItemComponentSections(pdpUiUpdater?.productDiscussionMostHelpfulMap)
            }, {
                // No Op
            })
        }
    }

    private fun observeImageVariantPartialyChanged() {
        viewLifecycleOwner.observe(viewModel.updatedImageVariant) {
            val mediaList = it.second.toMutableList()

            pdpUiUpdater?.updateVariantData(it.first)

            if (it.second.isNotEmpty()) {
                pdpUiUpdater?.updateImageAfterClickVariant(mediaList, enableVideo())
                viewModel.updateDynamicProductInfoData(VariantMapper.updateMediaToCurrentP1Data(viewModel.getDynamicProductInfoP1, mediaList))
                dynamicAdapter.notifyVariantSection(pdpUiUpdater?.productNewVariantDataModel, ProductDetailConstant.PAYLOAD_VARIANT_COMPONENT)
                dynamicAdapter.notifyMediaWithPayload(pdpUiUpdater?.mediaMap, ProductDetailConstant.PAYLOAD_UPDATE_IMAGE)
            } else {
                dynamicAdapter.notifyVariantSection(pdpUiUpdater?.productNewVariantDataModel, ProductDetailConstant.PAYLOAD_VARIANT_COMPONENT)
            }
        }
    }

    private fun observeonVariantClickedData() {
        viewLifecycleOwner.observe(viewModel.onVariantClickedData) {
            updateVariantDataToExistingProductData(it)
        }
    }

    private fun enableVideo(): Boolean {
        if (!::remoteConfig.isInitialized) return false

        return remoteConfig.getBoolean(ProductDetailConstant.ENABLE_VIDEO_PDP, true)
    }

    private fun updateVariantDataToExistingProductData(variantProcessedData: List<VariantCategory>?) {
        val selectedChildAndPosition = VariantCommonMapper.selectedProductData(viewModel.variantData
                ?: ProductVariantCommon())
        val selectedChild = selectedChildAndPosition?.second
        val updatedDynamicProductInfo = VariantMapper.updateDynamicProductInfo(viewModel.getDynamicProductInfoP1, selectedChild, viewModel.listOfParentMedia)

        viewModel.updateDynamicProductInfoData(updatedDynamicProductInfo)
        productId = updatedDynamicProductInfo?.basic?.productID

        pdpUiUpdater?.updateVariantData(variantProcessedData)
        pdpUiUpdater?.updateDataP1(context, updatedDynamicProductInfo, enableVideo())
        pdpUiUpdater?.updateNotifyMeUpcoming(selectedChild?.productId.toString(), viewModel.p2Data.value?.upcomingCampaigns)
        pdpUiUpdater?.updateFulfillmentData(context, viewModel.getMultiOriginByProductId().isFulfillment)
        pdpUiUpdater?.updateTickerData(viewModel.getDynamicProductInfoP1?.basic?.isWarehouse()
                ?: false, viewModel.getDynamicProductInfoP1?.data?.campaign?.isActive ?: false,
                viewModel.getDynamicProductInfoP1?.getFinalStock()?.toIntOrNull() == 0)

        /*
            If the p2 data is empty, dont update the button
            this condition will be reproduceable when variant auto select is faster then p2 data from network
            if this happen, the update button will be run in onSuccessGetP2Data
         */
        if (viewModel.p2Data.value != null || viewModel.p2Data.value == null && !isAutoSelectVariant) {
            updateButtonState()
        }

        if (pdpUiUpdater?.productNewVariantDataModel?.isPartialySelected() == false && shouldFireVariantTracker) {
            shouldFireVariantTracker = false
            DynamicProductDetailTracking.Click.onVariantLevel1Clicked(
                    viewModel.getDynamicProductInfoP1,
                    pdpUiUpdater?.productNewVariantDataModel,
                    viewModel.variantData,
                    dynamicAdapter.getItemComponentIndex(pdpUiUpdater?.productNewVariantDataModel))
        }

        dynamicAdapter.notifyMediaWithPayload(pdpUiUpdater?.mediaMap, ProductDetailConstant.PAYLOAD_UPDATE_IMAGE)
        dynamicAdapter.notifyGeneralInfo(pdpUiUpdater?.productFullfilmentMap)
        dynamicAdapter.notifyBasicContentWithPayloads(pdpUiUpdater?.basicContentMap, null)
        dynamicAdapter.notifyVariantSection(pdpUiUpdater?.productNewVariantDataModel, ProductDetailConstant.PAYLOAD_VARIANT_COMPONENT)
        dynamicAdapter.notifyNotifyMe(pdpUiUpdater?.notifyMeMap, null)
        dynamicAdapter.notifyItemComponentSections(pdpUiUpdater?.tickerInfoMap)
    }

    private fun updateButtonState() {
        viewModel.getDynamicProductInfoP1?.let {
            actionButtonView.renderData(!it.isProductActive(),
                    viewModel.hasShopAuthority(), viewModel.isShopOwner(), hasTopAds(),
                    viewModel.getCartTypeByProductId())
        }
        showOrHideButton()
    }

    private fun observeInitialVariantData() {
        viewLifecycleOwner.observe(viewModel.initialVariantData) {
            if (pdpUiUpdater?.productNewVariantDataModel?.isPartialySelected() == true) {
                pdpUiUpdater?.productNewVariantDataModel?.listOfVariantCategory = it
                dynamicAdapter.notifyVariantSection(pdpUiUpdater?.productNewVariantDataModel, null)
            } else {
                //If variant did auto select, we have to update the UI
                updateVariantDataToExistingProductData(it)
            }
        }
    }

    private fun observeAddToCart() {
        viewLifecycleOwner.observe(viewModel.addToCartLiveData) { data ->
            hideProgressDialog()
            data.doSuccessOrFail({
                if (it.data.errorReporter.eligible) {
                    logException(Throwable(it.data.errorReporter.texts.submitTitle))
                    showDialogErrorAtc(it.data)
                } else {
                    onSuccessAtc(it.data)
                }
            }, {
                DynamicProductDetailTracking.Impression.eventViewErrorWhenAddToCart(it.message
                        ?: "", viewModel.getDynamicProductInfoP1?.basic?.productID
                        ?: "", viewModel.userId)
                logException(it)
                if (it is AkamaiErrorException && it.message != null) {
                    view?.showToasterError(it.message
                            ?: "", ctaText = getString(R.string.label_oke_pdp))
                } else {
                    view?.showToasterError(getErrorMessage(it), ctaText = getString(R.string.label_oke_pdp))
                }
            })
        }
    }

    private fun observeP1() {
        viewLifecycleOwner.observe(viewModel.productLayout) { data ->
            (activity as? ProductDetailActivity)?.startMonitoringPltRenderPage()
            data.doSuccessOrFail({
                pdpUiUpdater = PdpUiUpdater(DynamicProductDetailMapper.hashMapLayout(it.data))
                onSuccessGetDataP1(it.data)
            }, {
                Timber.w("P2#LOAD_PAGE_FAILED#'pdp';desc='${it.message}';err='${Log.getStackTraceString(it).take(1000).trim()}'")
                logException(it)
                renderPageError(it)
            })
            (activity as? ProductDetailActivity)?.stopMonitoringP1()
            (activity as? ProductDetailActivity)?.stopMonitoringPltRenderPage(viewModel.getDynamicProductInfoP1?.isProductVariant()
                    ?: false)
        }
    }

    private fun observeP2Login() {
        viewLifecycleOwner.observe(viewModel.p2Login) {
            topAdsGetProductManage = it.topAdsGetProductManage
            if (it.pdpAffiliate == null) {
                dynamicAdapter.removeComponentSection(pdpUiUpdater?.productByMeMap)
            } else {
                pdpUiUpdater?.updateByMeData(context)
            }

            pdpUiUpdater?.updateShopFollow(it.isFollow)
            pdpUiUpdater?.updateWishlistData(it.isWishlisted)

            it.pdpAffiliate?.let { renderAffiliate(it) }
            actionButtonView.setTopAdsButton(hasTopAds())

            dynamicAdapter.notifyWithPayload(pdpUiUpdater?.shopInfoMap, ProductDetailConstant.PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)
            dynamicAdapter.notifyWithPayload(pdpUiUpdater?.shopCredibility, ProductDetailConstant.PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)
            dynamicAdapter.notifyBasicContentWithPayloads(pdpUiUpdater?.basicContentMap, ProductDetailConstant.PAYLOAD_WISHLIST)

            openBottomSheetTopAds()
            (activity as? ProductDetailActivity)?.stopMonitoringP2Login()
        }
    }

    private fun observeP2Data() {
        viewLifecycleOwner.observe(viewModel.p2Data) {
            trackProductView(viewModel.tradeInParams.isEligible == 1)

            viewModel.getDynamicProductInfoP1?.let { p1 ->
                DynamicProductDetailTracking.Moengage.sendMoEngageOpenProduct(p1)
                DynamicProductDetailTracking.Moengage.eventAppsFylerOpenProduct(p1)

                DynamicProductDetailTracking.sendScreen(
                        irisSessionId,
                        p1.basic.shopID,
                        p1.shopTypeString,
                        p1.basic.productID)
            }

            onSuccessGetDataP2(it)
            (activity as? ProductDetailActivity)?.stopMonitoringP2Data()
        }
    }

    private fun observeP3() {
        viewLifecycleOwner.observe(viewModel.productInfoP3) {
            onSuccessGetDataP3(it)
            (activity as? ProductDetailActivity)?.stopMonitoringFull()
        }
    }

    private fun openBottomSheetTopAds() {
        if (GlobalConfig.isSellerApp() && !activity?.intent?.data?.getQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME).isNullOrBlank() &&
                !alreadyPerformSellerMigrationAction && viewModel.isShopOwner()) {
            alreadyPerformSellerMigrationAction = true
            rincianTopAdsClicked()
        }
    }

    private fun observeToggleFavourite() {
        viewLifecycleOwner.observe(viewModel.toggleFavoriteResult) { data ->
            nplFollowersButton?.stopLoading()
            data.doSuccessOrFail({
                onSuccessFavoriteShop(it.data.first, it.data.second)
                setupShopFavoriteToaster(it.data.second)
            }, {
                onFailFavoriteShop(it)
            })
        }
    }

    private fun setupShopFavoriteToaster(isNplFollowerType: Boolean) {
        val isFavorite = pdpUiUpdater?.shopInfoMap?.isFavorite
                ?: pdpUiUpdater?.shopCredibility?.isFavorite ?: return
        val message = if (isFavorite) getString(R.string.merchant_product_detail_success_follow_shop) else getString(R.string.merchant_product_detail_success_unfollow_shop)

        view?.showToasterSuccess(if (isNplFollowerType) getString(R.string.merchant_product_detail_success_follow_shop_npl) else message)
    }

    private fun observeMoveToWarehouse() {
        viewLifecycleOwner.observe(viewModel.moveToWarehouseResult) { data ->
            data.doSuccessOrFail({
                onSuccessWarehouseProduct()
            }, {
                onErrorWarehouseProduct(it)
            })
        }
    }

    private fun observeMoveToEtalase() {
        viewLifecycleOwner.observe(viewModel.moveToEtalaseResult) { data ->
            data.doSuccessOrFail({
                onSuccessMoveToEtalase()
            }, {
                onErrorMoveToEtalase(it)
            })
        }
    }

    private fun observeRecommendationProduct() {
        viewLifecycleOwner.observe(viewModel.loadTopAdsProduct) { data ->
            data.doSuccessOrFail({
                dynamicAdapter.notifyRecomAdapter(pdpUiUpdater?.updateRecommendationData(it.data))
            }, {
                dynamicAdapter.removeRecommendation(pdpUiUpdater?.getRecommendationData(it.message
                        ?: ""))
            })
        }

        viewLifecycleOwner.observe(viewModel.statusFilterTopAdsProduct) {
            if (it is Fail) {
                view?.showToasterError(context?.getString(R.string.recom_filter_chip_click_error_network)
                        ?: "", ctaText = getString(R.string.label_oke_pdp))
            }
        }

        viewLifecycleOwner.observe(viewModel.filterTopAdsProduct) { data ->
            val recommendation = pdpUiUpdater?.updateFilterRecommendationData(data)
            recommendation?.let { dynamicAdapter.notifyFilterRecommendation(it) }
        }
    }


    private fun onSuccessAtc(result: AddToCartDataModel) {
        val cartId = result.data.cartId
        when (viewModel.buttonActionType) {
            ProductDetailConstant.OCS_BUTTON -> {
                if (result.data.success == 0) {
                    validateOvo(result)
                } else {
                    sendTrackingATC(cartId)
                    goToCheckout(ShipmentFormRequest
                            .BundleBuilder()
                            .deviceId("")
                            .build()
                            .bundle)
                }
            }
            ProductDetailConstant.OCC_BUTTON -> {
                sendTrackingATC(cartId)
                goToOneClickCheckout()
            }
            ProductDetailConstant.BUY_BUTTON -> {
                sendTrackingATC(cartId)
                goToCartCheckout(cartId)
            }
            ProductDetailConstant.ATC_BUTTON -> {
                sendTrackingATC(cartId)
                showAddToCartDoneBottomSheet()
            }
            ProductDetailConstant.TRADEIN_AFTER_DIAGNOSE -> {
                // Same with OCS but should send devideId
                sendTrackingATC(cartId)
                goToCheckout(ShipmentFormRequest.BundleBuilder()
                        .deviceId(viewModel.tradeinDeviceId)
                        .build()
                        .bundle)
            }
        }
    }

    private fun goToOneClickCheckout() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT).apply {
            putExtra(Constant.EXTRA_OCC_SOURCE_PDP, true)
        }
        startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_CHECKOUT)
    }

    private fun sendTrackingATC(cartId: String) {
        DynamicProductDetailTracking.Click.eventEcommerceBuy(viewModel.buttonActionType,
                viewModel.buttonActionText,
                viewModel.userId,
                cartId,
                trackerAttributionPdp ?: "",
                viewModel.getMultiOriginByProductId().isFulfillment,
                DynamicProductDetailTracking.generateVariantString(viewModel.variantData, viewModel.getDynamicProductInfoP1?.basic?.productID
                        ?: ""),
                viewModel.getDynamicProductInfoP1)
    }

    private fun validateOvo(result: AddToCartDataModel) {
        if (result.data.refreshPrerequisitePage) {
            onSwipeRefresh()
        } else {
            activity?.let {
                when (result.data.ovoValidationDataModel.status) {
                    ProductDetailConstant.OVO_INACTIVE_STATUS -> {
                        val applink = "${result.data.ovoValidationDataModel.applink}&product_id=${
                            viewModel.getDynamicProductInfoP1?.parentProductId
                                    ?: ""
                        }"
                        DynamicProductDetailTracking.Click.eventActivationOvo(
                                viewModel.getDynamicProductInfoP1?.parentProductId ?: "",
                                viewModel.userSessionInterface.userId)
                        RouteManager.route(it, applink)
                    }
                    ProductDetailConstant.OVO_INSUFFICIENT_BALANCE_STATUS -> {
                        val bottomSheetOvoDeals = OvoFlashDealsBottomSheet(
                                viewModel.getDynamicProductInfoP1?.parentProductId ?: "",
                                viewModel.userSessionInterface.userId,
                                result.data.ovoValidationDataModel)
                        bottomSheetOvoDeals.show(it.supportFragmentManager, "Ovo Deals")
                    }
                    else -> view?.showToasterError(getString(R.string.default_request_error_unknown), ctaText = getString(R.string.label_oke_pdp))
                }
            }
        }
    }

    private fun goToCheckout(shipmentFormRequest: Bundle) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT)
        intent.putExtra(CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT, true)
        intent.putExtras(shipmentFormRequest)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_CHECKOUT)
    }

    private fun goToCartCheckout(cartId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConst.CART)
        intent?.run {
            putExtra(ApplinkConst.Transaction.EXTRA_CART_ID, cartId)
            startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_CHECKOUT)
        }
    }

    private fun onSuccessGetDataP1(data: List<DynamicPdpDataModel>) {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            updateProductId()
            renderVariant(viewModel.variantData)
            val hint = String.format(getString(R.string.pdp_search_hint), productInfo.basic.category.name)
            navAbTestCondition({ setNavToolbarSearchHint(hint) }, { et_search.setHint(hint) })
            pdpUiUpdater?.updateDataP1(context, productInfo, enableVideo())
            actionButtonView.setButtonP1(productInfo.data.preOrder, productInfo.basic.isLeasing)

            if (productInfo.basic.category.isAdult) {
                AdultManager.showAdultPopUp(this, AdultManager.ORIGIN_PDP, productInfo.basic.productID)
            }

            if (affiliateString.hasValue()) {
                viewModel.hitAffiliateTracker(affiliateString ?: "", viewModel.deviceId)
            }

            setupProductVideoCoordinator()

            activity?.invalidateOptionsMenu()
            renderList(data)
        }
    }

    private fun setupProductVideoCoordinator() {
        if (pdpUiUpdater?.mediaMap?.isMediaContainsVideo() == true && enableVideo()) {
            if (productVideoCoordinator == null) {
                productVideoCoordinator = ProductVideoCoordinator(viewLifecycleOwner)
            }
        } else {
            productVideoCoordinator = null
        }
    }

    private fun showOrHideButton() {
        if (viewModel.shouldHideFloatingButton() && !viewModel.isShopOwner()) {
            actionButtonView.visibility = !viewModel.shouldHideFloatingButton()
            return
        }
        if (viewModel.getShopInfo().isShopInfoNotEmpty()) {
            val shopStatus = viewModel.getShopInfo().statusInfo.shopStatus
            val shouldShowSellerButtonByShopType = shopStatus != ProductShopStatusTypeDef.DELETED && shopStatus != ProductShopStatusTypeDef.MODERATED_PERMANENTLY
            if (viewModel.isShopOwner()) {
                actionButtonView.visibility = shouldShowSellerButtonByShopType
            } else {
                actionButtonView.visibility = !isAffiliate && viewModel.getShopInfo().statusInfo.shopStatus == ProductShopStatusTypeDef.OPEN
            }
            return
        }
        actionButtonView.visibility = !isAffiliate
    }

    private fun setupTickerOcc() {
        var willShowTicker = false
        if (actionButtonView.onSuccessGetCartType && actionButtonView.visibility) {
            val data = actionButtonView.cartTypeData
            if (data != null) {
                for (button in data.availableButtons.withIndex()) {
                    val onboardingMessage = button.value.onboardingMessage
                    if (onboardingMessage.isNotEmpty()) {
                        var selectedButton: View? = null
                        if (button.index == 0) {
                            selectedButton = actionButtonView.view.btn_buy_now
                        } else if (button.index == 1) {
                            selectedButton = actionButtonView.view.btn_add_to_cart
                        }
                        if (selectedButton != null && selectedButton.visibility == View.VISIBLE) {
                            view?.let {
                                ticker_occ_arrow.post {
                                    ticker_occ_text?.text = onboardingMessage
                                    ticker_occ_arrow?.translationX = selectedButton.x + (selectedButton.width / 2)
                                    ticker_occ?.setOnClickListener {
                                        ticker_occ_layout?.gone()
                                    }
                                    ticker_occ_layout?.visible()
                                }
                            }
                            willShowTicker = true
                            break
                        }
                    }
                }
            }
        }
        if (!willShowTicker) {
            ticker_occ_layout.gone()
        }
    }

    private fun onSuccessGetDataP2(it: ProductInfoP2UiData) {
        updateNplButtonFollowers(it.restrictionInfo)
        updateButtonState()
        setupTickerOcc()

        if (it.vouchers.isNullOrEmpty()) {
            dynamicAdapter.removeComponentSection(pdpUiUpdater?.productMerchantVoucherMap)
        } else {
            if (!viewModel.isUserSessionActive || !isOwner()) {
                DynamicProductDetailTracking.Impression.eventImpressionMerchantVoucherUse(
                        viewModel.getDynamicProductInfoP1?.basic?.shopID.toIntOrZero(),
                        it.vouchers, viewModel.getDynamicProductInfoP1)
            }
        }

        if (it.upcomingCampaigns.values.isEmpty()) {
            dynamicAdapter.removeComponentSection(pdpUiUpdater?.notifyMeMap)
        }

        if (!it.shopCommitment.isNowActive) {
            dynamicAdapter.removeComponentSection(pdpUiUpdater?.orderPriorityMap)
        }

        if (!it.productPurchaseProtectionInfo.ppItemDetailPage.isProtectionAvailable) {
            dynamicAdapter.removeComponentSection(pdpUiUpdater?.productProtectionMap)
        }

        if (!it.validateTradeIn.isEligible) {
            dynamicAdapter.removeComponentSection(pdpUiUpdater?.productTradeinMap)
        }

        it.productFinancingRecommendationData.let { financingData ->
            if (financingData.data.partnerCode.isNotBlank()) {
                pdpUiUpdater?.updateDataInstallment(context, financingData, viewModel.getDynamicProductInfoP1?.data?.isOS == true)
            } else {
                dynamicAdapter.removeComponentSection(pdpUiUpdater?.productInstallmentInfoMap)
            }
        }

        viewModel.getDynamicProductInfoP1?.run {
            DynamicProductDetailTracking.Branch.eventBranchItemView(this, viewModel.userId)
        }

        pdpUiUpdater?.updateFulfillmentData(context, viewModel.getMultiOriginByProductId().isFulfillment)
        pdpUiUpdater?.updateDataP2(context, it, viewModel.getDynamicProductInfoP1?.basic?.productID
                ?: "", viewModel.getDynamicProductInfoP1?.basic?.isWarehouse()
                ?: false, viewModel.getDynamicProductInfoP1?.data?.campaign?.isActive ?: false,
                viewModel.getDynamicProductInfoP1?.getFinalStock()?.toIntOrNull() == 0)

        dynamicAdapter.notifyItemRangeChangesExceptFirstPosition()
    }

    private fun updateNplButtonFollowers(restrictionInfo: RestrictionInfoResponse) {
        val alreadyFollowShop = restrictionInfo.restrictionData.firstOrNull()?.alreadyFollowShop
                ?: true
        val shouldShowRe = !alreadyFollowShop
        if (shouldShowRe && !viewModel.isShopOwner()) {
            if (!base_btn_follow.isShown) {
                base_btn_follow.translationY = 100.toPx().toFloat()
            }

            val title = restrictionInfo.restrictionData.firstOrNull()?.action?.firstOrNull()?.title
                    ?: ""
            val desc = restrictionInfo.restrictionData.firstOrNull()?.action?.firstOrNull()?.description
                    ?: ""
            nplFollowersButton?.renderView(title, desc, alreadyFollowShop)
        }
        setupNplVisibility(shouldShowRe)
    }

    override fun onButtonFollowNplClick() {
        DynamicProductDetailTracking.Click.eventClickFollowNpl(viewModel.getDynamicProductInfoP1, viewModel.userId)
        onShopFavoriteClick(isNplFollowType = true)
    }

    private fun onSuccessGetDataP3(it: ProductInfoP3) {
        if (it.ratesModel == null || it.ratesModel?.getServicesSize() == 0) {
            dynamicAdapter.removeComponentSection(pdpUiUpdater?.productShipingInfoMap)
        }

        if (pdpUiUpdater?.tickerInfoMap?.shouldRemoveComponent() == true) {
            dynamicAdapter.removeComponentSection(pdpUiUpdater?.tickerInfoMap)
        }

        updateStickyContent(it.tickerStickyLogin)

        pdpUiUpdater?.updateDataP3(context, it)
        dynamicAdapter.notifyItemComponentSections(pdpUiUpdater?.tickerInfoMap, pdpUiUpdater?.productShipingInfoMap)
    }

    private fun logException(t: Throwable) {
        try {
            if (!BuildConfig.DEBUG) {
                val errorMessage = String.format(
                        getString(R.string.on_error_p1_string_builder),
                        viewModel.userSessionInterface.userId,
                        viewModel.userSessionInterface.email,
                        t.message
                )
                FirebaseCrashlytics.getInstance().recordException(Exception(errorMessage))
            }
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
    }

    private fun renderPageError(t: Throwable) {
        context?.let { ctx ->
            dynamicAdapter.clearAllElements()
            dynamicAdapter.addElement(ProductDetailErrorHelper.getErrorType(ctx, t, isFromDeeplink, deeplinkUrl))
            if (swipeToRefresh != null) {
                swipeToRefresh.isEnabled = false
            }
            actionButtonView.visibility = false
            ticker_occ_layout.gone()
        }
    }

    private fun showSnackbarClose(string: String) {
        view?.let {
            Snackbar.make(it, string, Snackbar.LENGTH_LONG).apply {
                setAction(getString(R.string.close)) { dismiss() }
                setActionTextColor(androidx.core.content.ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            }.show()
        }
    }

    private fun goToTradein() {
        if (tradeinDialog?.isAdded == false) {
            tradeinDialog?.show(childFragmentManager, "ACCESS REQUEST")
        }
    }

    override fun onVariantGuideLineClicked(url: String) {
        activity?.let {
            DynamicProductDetailTracking.Click.onVariantGuideLineClicked(viewModel.getDynamicProductInfoP1, pdpUiUpdater?.productNewVariantDataModel,
                    dynamicAdapter.getItemComponentIndex(pdpUiUpdater?.productNewVariantDataModel))
            startActivity(ImagePreviewActivity.getCallingIntent(it, arrayListOf(url)))
        }
    }

    override fun getStockWording(): String {
        val variantStockWording = viewModel.getDynamicProductInfoP1?.data?.stock?.stockWording ?: ""
        val isPartialySelected = pdpUiUpdater?.productNewVariantDataModel?.isPartialySelected()
                ?: false

        return if (isPartialySelected) "" else variantStockWording
    }

    override fun onVariantClicked(variantOptions: VariantOptionWithAttribute) {
        pdpUiUpdater?.productNewVariantDataModel?.let {
            it.mapOfSelectedVariant[variantOptions.variantCategoryKey] = variantOptions.variantId
        }
        val isPartialySelected = pdpUiUpdater?.productNewVariantDataModel?.isPartialySelected()
                ?: false

        viewModel.onVariantClicked(viewModel.variantData, pdpUiUpdater?.productNewVariantDataModel?.mapOfSelectedVariant, isPartialySelected, variantOptions.level,
                variantOptions.imageOriginal)
    }

    private fun renderVariant(data: ProductVariantCommon?) {
        if (data == null || !data.hasChildren || pdpUiUpdater?.productNewVariantDataModel == null) {
            dynamicAdapter.clearElement(pdpUiUpdater?.productNewVariantDataModel)
        } else {
            if (data.errorCode > 0) {
                pdpUiUpdater?.updateVariantError()
            } else {
                autoSelectVariant()
                viewModel.processVariant(data, pdpUiUpdater?.productNewVariantDataModel?.mapOfSelectedVariant)
            }
        }
    }

    private fun autoSelectVariant() {
        viewModel.variantData?.let {
            //Auto select variant will be execute when there is only 1 child left
            val isOnlyHaveOneVariantLeftData = it.autoSelectedOptionIds()
            if (isOnlyHaveOneVariantLeftData.isNotEmpty()) {
                isAutoSelectVariant = true
                pdpUiUpdater?.productNewVariantDataModel?.mapOfSelectedVariant = VariantCommonMapper.mapVariantIdentifierWithDefaultSelectedToHashMap(it, isOnlyHaveOneVariantLeftData)
            } else {
                // If there's still many variant others, just render variant as is
                isAutoSelectVariant = false
                pdpUiUpdater?.productNewVariantDataModel?.mapOfSelectedVariant = VariantCommonMapper.mapVariantIdentifierToHashMap(it)
            }
        }
    }

    private fun showAddToCartDoneBottomSheet() {
        viewModel.getDynamicProductInfoP1?.let {
            val addToCartDoneBottomSheet = AddToCartDoneBottomSheet()
            val productName = it.getProductName
            val productImageUrl = it.data.getFirstProductImage()
            val addedProductDataModel = AddToCartDoneAddedProductDataModel(
                    it.basic.productID,
                    productName,
                    productImageUrl,
                    it.data.variant.isVariant,
                    it.basic.getShopId()
            )
            val bundleData = Bundle()
            bundleData.putParcelable(AddToCartDoneBottomSheet.KEY_ADDED_PRODUCT_DATA_MODEL, addedProductDataModel)
            addToCartDoneBottomSheet.arguments = bundleData
            addToCartDoneBottomSheet.setDismissListener(BottomSheets.BottomSheetDismissListener {
                shouldShowCartAnimation = true
                updateCartNotification()
            })
            fragmentManager?.let {
                addToCartDoneBottomSheet.show(
                        it, "TAG"
                )
            }
        }
    }

    private fun onShipmentClicked() {
        if (viewModel.isUserSessionActive) {
            val productP3value = viewModel.productInfoP3.value
            if (!productP3value?.ratesModel?.services.isNullOrEmpty()) {
                gotoRateEstimation()
            } else {
                goToCourier()
            }
        } else {
            goToCourier()
        }

        activity?.overridePendingTransition(0, 0)
    }

    private fun updateCartNotification() {
        viewModel.updateCartCounerUseCase(::onSuccessUpdateCartCounter)
    }

    private fun onSuccessUpdateCartCounter(count: Int) {
        val cache = LocalCacheHandler(context, CartConstant.CART)
        cache.putInt(CartConstant.IS_HAS_CART, if (count > 0) 1 else 0)
        cache.putInt(CartConstant.CACHE_TOTAL_CART, count)
        cache.applyEditor()
        if (isAdded) {
            initToolBarMethod?.invoke()
            setNavToolBarCartCounter()
        }
    }

    private fun onErrorSubmitHelpTicket(e: Throwable?) {
        hideProgressDialog()
        view?.let {
            it.showToasterError(ErrorHandler.getErrorMessage(it.context, e), ctaText = getString(R.string.label_oke_pdp))
        }
    }

    private fun onSuccessSubmitHelpTicket(result: SubmitTicketResult) {
        hideProgressDialog()
        if (result.status) {
            activity?.also {
                val successTicketDialog = DialogUnify(it, DialogUnify.SINGLE_ACTION, DialogUnify.NO_IMAGE)
                successTicketDialog.apply {
                    setTitle(result.texts.submitTitle)
                    setDescription(result.texts.submitDescription)
                    setPrimaryCTAText(result.texts.successButton)
                    setPrimaryCTAClickListener {
                        this.dismiss()
                    }
                    show()
                }
            }
        } else {
            view?.showToasterError(result.message, ctaText = getString(R.string.label_oke_pdp))
        }
    }

    override fun shareProductFromContent(componentTrackDataModel: ComponentTrackDataModel?) {
        DynamicProductDetailTracking.Click.eventClickShareFromContent(viewModel.getDynamicProductInfoP1, viewModel.userId, componentTrackDataModel)
        shareProduct()
    }

    private fun shareProductFromToolbar() {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            DynamicProductDetailTracking.Click.eventClickPdpShare(productInfo)
        }
        shareProduct()
    }

    private fun shareProduct() {
        activity?.let {
            viewModel.getDynamicProductInfoP1?.let { productInfo ->
                val productData = ProductData(
                        productInfo.finalPrice.getCurrencyFormatted(),
                        "${productInfo.data.isCashback.percentage}%",
                        MethodChecker.fromHtml(productInfo.getProductName).toString(),
                        productInfo.data.price.currency,
                        productInfo.basic.url,
                        viewModel.getShopInfo().shopCore.url ?: "",
                        productInfo.basic.shopName,
                        productInfo.basic.productID,
                        productInfo.data.getProductImageUrl() ?: ""
                )
                checkAndExecuteReferralAction(productData)
            }
        }
    }

    private fun checkAndExecuteReferralAction(productData: ProductData) {
        val fireBaseRemoteMsgGuest = remoteConfig.getString(RemoteConfigKey.fireBaseGuestShareMsgKey, "")
        if (!TextUtils.isEmpty(fireBaseRemoteMsgGuest)) productData.productShareDescription = fireBaseRemoteMsgGuest

        if (viewModel.userSessionInterface.isLoggedIn && viewModel.userSessionInterface.isMsisdnVerified) {
            val fireBaseRemoteMsg = remoteConfig.getString(RemoteConfigKey.fireBaseShareMsgKey, "")
            if (!TextUtils.isEmpty(fireBaseRemoteMsg) && fireBaseRemoteMsg.contains(ProductData.PLACEHOLDER_REFERRAL_CODE)) {
                doReferralShareAction(productData, fireBaseRemoteMsg)
                return
            }
        }
        executeProductShare(productData)
    }

    private fun doReferralShareAction(productData: ProductData, fireBaseRemoteMsg: String) {
        val actionCreator = object : ActionCreator<String, Int> {
            override fun actionSuccess(actionId: Int, dataObj: String) {
                if (!TextUtils.isEmpty(dataObj) && !TextUtils.isEmpty(fireBaseRemoteMsg)) {
                    productData.productShareDescription = FindAndReplaceHelper.findAndReplacePlaceHolders(fireBaseRemoteMsg,
                            ProductData.PLACEHOLDER_REFERRAL_CODE, dataObj)
                    DynamicProductDetailTracking.Moengage.sendMoEngagePDPReferralCodeShareEvent()
                }
                executeProductShare(productData)
            }

            override fun actionError(actionId: Int, dataObj: Int?) {
                executeProductShare(productData)
            }
        }
        val referralAction = ReferralAction<Context, String, Int, String, String, String, Context>()
        referralAction.doAction(Constants.Action.ACTION_GET_REFERRAL_CODE, context, actionCreator, object : ActionUIDelegate<String, String> {
            override fun waitForResult(actionId: Int, dataObj: String?) {
                showProgressDialog()
            }

            override fun stopWaiting(actionId: Int, dataObj: String?) {
                hideProgressDialog()
            }
        })

    }

    private fun executeProductShare(productData: ProductData) {
        activity?.let {
            shareProductInstance?.share(productData, {
                showProgressDialog {
                    shareProductInstance?.cancelShare(true)
                }
            }, {
                hideProgressDialog()
            })
        }
    }

    /**
     * Event than happen after owner successfully move the warehoused product back to etalase
     */
    private fun onSuccessMoveToEtalase() {
        hideProgressDialog()
        view?.showToasterSuccess(getString(R.string.success_move_etalase))
        onSwipeRefresh()
    }

    private fun onErrorMoveToEtalase(throwable: Throwable) {
        hideProgressDialog()
        view?.showToasterError(getErrorMessage(throwable), ctaText = getString(R.string.oke))
    }

    private fun onErrorWarehouseProduct(throwable: Throwable) {
        hideProgressDialog()
        view?.showToasterError(getErrorMessage(throwable), ctaText = getString(R.string.oke))
    }

    private fun onSuccessWarehouseProduct() {
        hideProgressDialog()
        view?.showToasterSuccess(getString(R.string.success_warehousing_product))
        onSwipeRefresh()
    }

    override fun reportProductFromComponent(componentTrackDataModel: ComponentTrackDataModel?) {
        reportProduct({
            DynamicProductDetailTracking.Click.eventClickReportFromComponent(viewModel.getDynamicProductInfoP1, viewModel.userId, componentTrackDataModel)
        }, {})
    }

    private fun reportProductFromToolbar() {
        reportProduct({
            DynamicProductDetailTracking.Click.eventReportLogin()
        }, {
            DynamicProductDetailTracking.Click.eventReportNoLogin()
        })
    }

    private fun reportProduct(trackerLogin: (() -> Unit)? = null, trackerNonLogin: (() -> Unit)? = null) {
        viewModel.getDynamicProductInfoP1?.run {
            doActionOrLogin({
                context?.let {
                    trackerLogin?.invoke()
                    var deeplink = UriUtil.buildUri(ApplinkConstInternalMarketplace.REPORT_PRODUCT, basic.productID)
                    deeplink = Uri.parse(deeplink).buildUpon().appendQueryParameter(ApplinkConst.DFFALLBACKURL_KEY,
                            DynamicProductDetailMapper.generateProductReportFallback(basic.url)).toString()
                    val intent = RouteManager.getIntent(it, deeplink)
                    startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_REPORT)
                }
            }, {
                trackerNonLogin?.invoke()
            })
        }
    }

    private fun warehouseProduct() {
        viewModel.getDynamicProductInfoP1?.basic?.productID?.let {
            showProgressDialog(onCancelClicked = { viewModel.cancelWarehouseUseCase() })
            viewModel.moveProductToWareHouse(it)
        }
    }

    private fun moveProductToEtalase() {
        context?.run {
            val shopId = viewModel.getDynamicProductInfoP1?.basic?.shopID ?: ""
            if (shopId.isNotEmpty()) {
                val etalaseId = viewModel.getDynamicProductInfoP1?.basic?.menu?.id ?: ""
                val bundle = Bundle()
                bundle.putString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, shopId)
                bundle.putString(ShopShowcaseParamConstant.EXTRA_ETALASE_ID, etalaseId)
                bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, false)
                bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, true)
                bundle.putBoolean(ShopShowcaseParamConstant.EXTRA_IS_SELLER_NEED_TO_HIDE_SHOWCASE_GROUP_VALUE, true)

                val shopEtalasePickerIntent: Intent = RouteManager.getIntent(context, ApplinkConstInternalMechant.MERCHANT_SHOP_SHOWCASE_LIST)
                shopEtalasePickerIntent.putExtra(EXTRA_BUNDLE, bundle)
                startActivityForResult(shopEtalasePickerIntent, ProductDetailConstant.REQUEST_CODE_ETALASE)
            }
        }
    }

    private fun loadProductData(forceRefresh: Boolean = false) {
        if (productId != null || (productKey != null && shopDomain != null)) {
            (context as? ProductDetailActivity)?.startMonitoringPltNetworkRequest()
            viewModel.getProductP1(ProductParams(productId = productId, shopDomain = shopDomain, productName = productKey, warehouseId = warehouseId), forceRefresh, isAffiliate, layoutId, isNavOld())
        }
    }

    private fun gotoRateEstimation() {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            if (viewModel.getShopInfo().isShopInfoNotEmpty()) {
                context?.let { context ->
                    val shopInfo = viewModel.getShopInfo()
                    startActivity(RatesEstimationDetailActivity.createIntent(
                            context,
                            shopInfo.shopCore.domain,
                            productInfo.basic.weight.toFloat(),
                            productInfo.basic.weightUnit,
                            if (viewModel.getMultiOriginByProductId().isFulfillment)
                                viewModel.getMultiOriginByProductId().getOrigin() else null,
                            productInfo.data.isFreeOngkir.isActive,
                            shopInfo.shopCore.shopID,
                            productInfo.basic.productID
                    ))
                }
            }
        }
    }

    private fun goToCourier() {
        activity?.let {
            viewModel.getDynamicProductInfoP1?.let { productInfo ->
                if (viewModel.getShopInfo().isShopInfoNotEmpty()) {
                    startActivity(CourierActivity.createIntent(it,
                            productInfo.basic.productID,
                            viewModel.getShopInfo().shipments))
                }
            }
        }
    }

    private fun handlingMenuPreparation(menu: Menu?) {
        if (menu == null) return

        val menuShare = menu.findItem(R.id.action_share)
        val menuCart = menu.findItem(R.id.action_cart)
        val menuReport = menu.findItem(R.id.action_report)
        val menuWarehouse = menu.findItem(R.id.action_warehouse)
        val menuEtalase = menu.findItem(R.id.action_etalase)

        if (viewModel.getDynamicProductInfoP1 == null) {
            menuShare.isVisible = false
            menuShare.isEnabled = false
            menuCart.isVisible = true
            menuCart.isEnabled = true
            menuReport.isVisible = false
            menuReport.isEnabled = false
            menuWarehouse.isVisible = false
            menuWarehouse.isEnabled = false
            menuEtalase.isEnabled = false
            menuEtalase.isVisible = false
        } else {
            menuShare.isVisible = true
            menuShare.isEnabled = true

            viewModel.getDynamicProductInfoP1?.run {
                val isOwned = viewModel.isShopOwner()
                val isSellerApp = GlobalConfig.isSellerApp()

                val isValidCustomer = !isOwned && !isSellerApp
                menuCart.isVisible = isValidCustomer
                menuCart.isEnabled = isValidCustomer

                if (isValidCustomer) setBadgeMenuCart(menuCart)

                menuReport.isVisible = !isOwned && (basic.status != ProductStatusTypeDef.WAREHOUSE)
                menuReport.isEnabled = !isOwned && (basic.status != ProductStatusTypeDef.WAREHOUSE)
                menuWarehouse.isVisible = isOwned && (basic.status !in arrayOf(ProductStatusTypeDef.WAREHOUSE, ProductStatusTypeDef.PENDING))
                menuWarehouse.isEnabled = isOwned && (basic.status !in arrayOf(ProductStatusTypeDef.WAREHOUSE, ProductStatusTypeDef.PENDING))
                menuEtalase.isVisible = isOwned && (basic.status !in arrayOf(ProductStatusTypeDef.ACTIVE, ProductStatusTypeDef.PENDING))
                menuEtalase.isEnabled = isOwned && (basic.status !in arrayOf(ProductStatusTypeDef.ACTIVE, ProductStatusTypeDef.PENDING))
            }
        }
    }

    private fun initRecyclerView(view: View) {
        context?.let {
            getRecyclerView(view).layoutManager = CenterLayoutManager(it, LinearLayoutManager.VERTICAL, false)
        }
        getRecyclerView(view).itemAnimator = null
    }

    private fun renderAffiliate(pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate) {
        if (isAffiliate) {
            base_btn_affiliate_dynamic.visible()
            loadingAffiliateDynamic.gone()
            getCommissionPdp.visible()
            commissionPdp.visible()
            commissionPdp.text = pdpAffiliate.commissionValueDisplay
            btn_affiliate_pdp.setOnClickListener {
                viewModel.getDynamicProductInfoP1?.let { productInfo ->
                    DynamicProductDetailTracking.Click.eventClickAffiliate(viewModel.userId, productInfo.basic.getShopId(), false,
                            viewModel.getDynamicProductInfoP1)
                }
                onAffiliateClick()
            }
            actionButtonView.gone()
            ticker_occ_layout.gone()
        } else if (!GlobalConfig.isSellerApp()) {
            base_btn_affiliate_dynamic.gone()
        }
    }

    private fun onAffiliateClick() {
        activity?.let {
            doActionOrLogin({
                viewModel.p2Login.value?.pdpAffiliate?.let { pdpAffiliate ->
                    RouteManager.getIntent(it,
                            ApplinkConst.AFFILIATE_CREATE_POST,
                            pdpAffiliate.productId.toString(),
                            pdpAffiliate.adId.toString())
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            .let(::startActivity)
                    it.setResult(Activity.RESULT_OK)
                    it.finish()
                }
            })
        }
    }

    private fun trackProductView(isElligible: Boolean) {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            if (viewModel.getShopInfo().isShopInfoNotEmpty()) {
                val sentBundle = DynamicProductDetailTracking.Impression.eventEnhanceEcommerceProductDetail(
                        irisSessionId,
                        trackerListNamePdp,
                        productInfo,
                        viewModel.getShopInfo(),
                        trackerAttributionPdp,
                        isElligible,
                        viewModel.tradeInParams.usedPrice > 0,
                        viewModel.getMultiOriginByProductId().isFulfillment,
                        deeplinkUrl,
                        viewModel.getDynamicProductInfoP1?.getFinalStock() ?: "0"
                )
                return
            }
        }
    }

    private fun openFtInstallmentBottomSheet(installmentData: FtInstallmentCalculationDataResponse) {
        val pdpInstallmentBottomSheet = FtPDPInstallmentBottomSheet()

        val productInfo = viewModel.getDynamicProductInfoP1

        context?.let {
            val cacheManager = SaveInstanceCacheManager(it, true)
            cacheManager.put(FtInstallmentCalculationDataResponse::class.java.simpleName, installmentData, TimeUnit.HOURS.toMillis(1))
            val bundleData = Bundle()

            bundleData.putString(FtPDPInstallmentBottomSheet.KEY_PDP_FINANCING_DATA, cacheManager.id!!)
            bundleData.putFloat(FtPDPInstallmentBottomSheet.KEY_PDP_PRODUCT_PRICE, productInfo?.data?.price?.value?.toFloat()
                    ?: 0f)
            bundleData.putBoolean(FtPDPInstallmentBottomSheet.KEY_PDP_IS_OFFICIAL, productInfo?.data?.isOS
                    ?: false)

            pdpInstallmentBottomSheet.arguments = bundleData
            pdpInstallmentBottomSheet.show(childFragmentManager, "FT_TAG")
        }
    }

    /**
     * @param url : linkUrl for insurance partner to be rendered in web-view
     */
    private fun openFtInsuranceBottomSheet(url: String) {
        FtPDPInsuranceBottomSheet.show(url, childFragmentManager)
    }

    private fun onSuccessRemoveWishlist(productId: String?) {
        view?.showToasterSuccess(toasterWishlistText)
        pdpUiUpdater?.updateWishlistData(false)
        dynamicAdapter.notifyBasicContentWithPayloads(pdpUiUpdater?.basicContentMap, ProductDetailConstant.PAYLOAD_WISHLIST)
        sendIntentResultWishlistChange(productId ?: "", false)
        if (isProductOos()) {
            refreshPage()
        }
    }

    private fun onErrorRemoveWishList(errorMessage: String?) {
        view?.showToasterError(getErrorMessage(MessageErrorException(errorMessage)),
                ctaText = getString(R.string.oke))
    }

    private fun onSuccessAddWishlist(productId: String?) {
        view?.showToasterSuccess(toasterWishlistText)
        pdpUiUpdater?.updateWishlistData(true)
        dynamicAdapter.notifyBasicContentWithPayloads(pdpUiUpdater?.basicContentMap, ProductDetailConstant.PAYLOAD_WISHLIST)
        DynamicProductDetailTracking.Branch.eventBranchAddToWishlist(viewModel.getDynamicProductInfoP1, (UserSession(activity)).userId, pdpUiUpdater?.productInfoMap?.data?.find { content ->
            content.row == "bottom"
        }?.listOfContent?.firstOrNull()?.subtitle ?: "")
        sendIntentResultWishlistChange(productId ?: "", true)
        if (isProductOos()) {
            refreshPage()
        }
    }

    private fun onErrorAddWishList(errorMessage: String?) {
        view?.showToasterError(getErrorMessage(MessageErrorException(errorMessage)),
                ctaText = getString(R.string.oke))
    }

    private fun sendIntentResultWishlistChange(productId: String, isInWishlist: Boolean) {
        val resultIntent = Intent()
                .putExtra(ProductDetailConstant.WISHLIST_STATUS_UPDATED_POSITION, activity?.intent?.getIntExtra(ProductDetailConstant.WISHLIST_STATUS_UPDATED_POSITION, -1))
        resultIntent.putExtra(ProductDetailConstant.WIHSLIST_STATUS_IS_WISHLIST, isInWishlist)
        resultIntent.putExtra("product_id", productId)
        activity!!.setResult(Activity.RESULT_CANCELED, resultIntent)
    }

    private fun gotoEditProduct() {
        val id = viewModel.parentProductId ?: return

        val applink = Uri.parse(ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)
                .buildUpon()
                .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_ID, id)
                .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_MODE, ApplinkConstInternalMechant.MODE_EDIT_PRODUCT)
                .build()
                .toString()
        context?.run {
            RouteManager.getIntent(this, applink)?.apply {
                startActivityForResult(this, ProductDetailConstant.REQUEST_CODE_EDIT_PRODUCT)
            }
        }
    }

    private fun initToolbar() {
        layout_search?.showWithCondition(!GlobalConfig.isSellerApp())

        varToolbar = search_pdp_toolbar
        initToolBarMethod = ::initToolbarLight
        activity?.let {
            varToolbar?.setBackgroundColor(ContextCompat.getColor(it, R.color.Unify_N0))
            (it as AppCompatActivity).setSupportActionBar(varToolbar)
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        (activity as AppCompatActivity).supportActionBar?.elevation = 10F

        et_search.setOnClickListener {
            DynamicProductDetailTracking.Click.eventSearchToolbarClicked(viewModel.getDynamicProductInfoP1)
            RouteManager.route(context, ApplinkConstInternalDiscovery.AUTOCOMPLETE)
        }
        et_search.hint = String.format(getString(R.string.pdp_search_hint), "")
    }

    private fun initNavToolbar() {
        search_pdp_toolbar?.hide()
        navToolbar = view?.findViewById(R.id.pdp_navtoolbar)
        navToolbar?.apply {
            setIcon(
                    IconBuilder()
                            .addIcon(IconList.ID_CART) {}
                            .addIcon(IconList.ID_NAV_GLOBAL) {}
            )
            setupSearchbar(listOf(HintData(getString(R.string.pdp_search_hint, ""))))
            setToolbarPageName(ProductTrackingConstant.Category.PDP)
            show()
        }
    }

    private fun setNavToolbarSearchHint(hint: String) {
        navToolbar?.setupSearchbar(listOf(HintData(hint)))
    }

    private fun initStickyLogin(view: View) {
        stickyLoginView = view.findViewById(R.id.sticky_login_pdp)
        updateStickyState()
        updateActionButtonShadow()
        stickyLoginView?.setOnClickListener {
            goToLogin()
            if (stickyLoginView?.isLoginReminder() == true) {
                stickyLoginView?.trackerLoginReminder?.clickOnLogin(StickyLoginConstant.Page.PDP)
            } else {
                stickyLoginView?.tracker?.clickOnLogin(StickyLoginConstant.Page.PDP)
            }
        }
        stickyLoginView?.setOnDismissListener(View.OnClickListener {
            stickyLoginView?.dismiss(StickyLoginConstant.Page.PDP)
            if (stickyLoginView?.isLoginReminder() == true) {
                stickyLoginView?.trackerLoginReminder?.clickOnDismiss(StickyLoginConstant.Page.PDP)
            } else {
                stickyLoginView?.tracker?.clickOnDismiss(StickyLoginConstant.Page.PDP)
            }
            updateStickyState()
        })
    }

    private fun updateStickyState() {
        if (tickerDetail == null) {
            stickyLoginView?.hide()
            return
        }

        if (viewModel.isUserSessionActive) {
            stickyLoginView?.hide()
            return
        }

        var isCanShowing = remoteConfig.getBoolean(StickyLoginConstant.KEY_STICKY_LOGIN_REMINDER_PDP, true)
        if (stickyLoginView?.isLoginReminder() == true && isCanShowing) {
            stickyLoginView?.showLoginReminder(StickyLoginConstant.Page.PDP)
            if (stickyLoginView?.isShowing() == true) {
                stickyLoginView?.trackerLoginReminder?.viewOnPage(StickyLoginConstant.Page.PDP)
            }
        } else {
            isCanShowing = remoteConfig.getBoolean(StickyLoginConstant.KEY_STICKY_LOGIN_WIDGET_PDP, true)
            if (!isCanShowing) {
                stickyLoginView?.visibility = View.GONE
                return
            }

            this.tickerDetail?.let { stickyLoginView?.setContent(it) }
            stickyLoginView?.show(StickyLoginConstant.Page.PDP)
            if (stickyLoginView?.isShowing() == true) {
                stickyLoginView?.tracker?.viewOnPage(StickyLoginConstant.Page.PDP)
            }
        }
    }

    private fun updateStickyContent(stickyData: StickyLoginTickerPojo.TickerDetail?) {
        if (stickyData == null) {
            stickyLoginView?.hide()
        } else {
            this.tickerDetail = stickyData
            updateStickyState()
            updateActionButtonShadow()
        }
    }

    private fun goToPdpSellerApp() {
        val appLink = UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        val parameterizedAppLink = Uri.parse(appLink).buildUpon()
                .appendQueryParameter(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, SellerMigrationFeatureName.FEATURE_ADS_DETAIL)
                .build()
                .toString()
        goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_ADS_DETAIL, arrayListOf(
                ApplinkConst.PRODUCT_MANAGE,
                parameterizedAppLink
        ))
    }

    override fun advertiseProductClicked() {
        DynamicProductDetailTracking.Click.eventTopAdsButtonClicked(
                viewModel.userId,
                btn_top_ads.text.toString(),
                viewModel.getDynamicProductInfoP1)
        val firstAppLink = UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
        val secondAppLink = when (viewModel.p2Login.value?.topAdsGetShopInfo?.category) {
            TopAdsShopCategoryTypeDef.MANUAL_USER -> {
                ApplinkConst.SellerApp.TOPADS_CREATE_ADS
            }
            TopAdsShopCategoryTypeDef.NO_ADS, TopAdsShopCategoryTypeDef.NO_PRODUCT -> {
                ApplinkConst.SellerApp.TOPADS_CREATE_ONBOARDING
            }
            else -> {
                ""
            }
        }

        if (GlobalConfig.isSellerApp()) {
            if (secondAppLink.isEmpty()) {
                showTopAdsBottomSheet()
            } else {
                val intent = RouteManager.getIntent(context, secondAppLink).apply {
                    putExtra(PARAM_DIRECTED_FROM_MANAGE_OR_PDP, true)
                }
                startActivity(intent)
            }
        } else {
            if (secondAppLink.isEmpty()) {
                goToPdpSellerApp()
            } else {
                goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_ADS, arrayListOf(
                        ApplinkConst.PRODUCT_MANAGE,
                        firstAppLink,
                        secondAppLink
                ))
            }
        }
    }

    override fun rincianTopAdsClicked() {
        DynamicProductDetailTracking.Click.eventTopAdsButtonClicked(
                viewModel.userId,
                btn_top_ads.text.toString(),
                viewModel.getDynamicProductInfoP1)
        if (GlobalConfig.isSellerApp()) {
            showTopAdsBottomSheet()
        } else {
            goToPdpSellerApp()
        }
    }

    override fun addToCartClick(buttonText: String) {
        viewModel.buttonActionText = buttonText
        viewModel.getDynamicProductInfoP1?.let {
            doAtc(ProductDetailConstant.ATC_BUTTON)
        }
    }

    override fun buyNowClick(buttonText: String) {
        viewModel.buttonActionText = buttonText
        // buy now / buy / preorder
        viewModel.getDynamicProductInfoP1?.let {
            doAtc(ProductDetailConstant.BUY_BUTTON)
        }
    }

    override fun buttonCartTypeClick(cartType: String, buttonText: String, isAtcButton: Boolean) {
        viewModel.buttonActionText = buttonText
        val isLeasing = viewModel.getDynamicProductInfoP1?.basic?.isLeasing ?: false
        val atcKey = DynamicProductDetailMapper.generateButtonAction(cartType, isAtcButton, isLeasing)
        doAtc(atcKey)
    }

    override fun topChatButtonClicked() {
        DynamicProductDetailTracking.Click.eventButtonChatClicked(viewModel.getDynamicProductInfoP1)
        onShopChatClicked()
    }

    override fun leasingButtonClicked() {
        doAtc(ProductDetailConstant.LEASING_BUTTON)
    }

    override fun editProductButtonClicked() {
        val shopInfo = viewModel.getShopInfo()
        val productInfo = viewModel.getDynamicProductInfoP1
        if (shopInfo.isShopInfoNotEmpty() && shopInfo.isAllowManage == 1) {
            if (productInfo?.basic?.status != ProductStatusTypeDef.PENDING) {
                DynamicProductDetailTracking.Click.eventEditProductClick(viewModel.isUserSessionActive, viewModel.getDynamicProductInfoP1, ComponentTrackDataModel())
                gotoEditProduct()
            } else {
                activity?.run {
                    val statusMessage = productInfo.basic.statusMessage(this)
                    if (statusMessage.isNotEmpty()) {
                        view.showToasterError(getString(R.string.product_is_at_status_x, statusMessage), ctaText = getString(R.string.close))
                    }
                }
            }
        }
    }

    /**
     * ProductDetailInfoBottomSheet Listener
     */
    override fun getPdpDataSource(): DynamicProductInfoP1? {
        return viewModel.getDynamicProductInfoP1
    }

    override fun goToTalkReadingBottomSheet() {
        viewModel.getDynamicProductInfoP1?.let {
            DynamicProductDetailTracking.ProductDetailSheet.onCheckDiscussionSheetClicked(it, viewModel.userId)
        }
        goToReadingActivity()
    }

    override fun onDiscussionSendQuestionBottomSheetClicked() {
        writeDiscussion {
            DynamicProductDetailTracking.ProductDetailSheet.onWriteDiscussionSheetClicked(viewModel.getDynamicProductInfoP1, viewModel.userId)
        }
    }

    private fun initBtnAction() {
        if (!::actionButtonView.isInitialized) {
            actionButtonView = PartialButtonActionView.build(base_btn_action, this)
        }
    }

    private fun showTopAdsBottomSheet() {
        context?.let {
            context?.let {
                topAdsDetailSheet?.show(childFragmentManager,
                        topAdsGetProductManage.data.adType,
                        topAdsGetProductManage.data.adId,
                        viewModel.p2Login.value?.topAdsGetShopInfo?.category ?: 0)
            }
        }
    }

    private fun doAtc(buttonAction: Int) {
        viewModel.buttonActionType = buttonAction
        context?.let {
            val isVariant = viewModel.getDynamicProductInfoP1?.data?.variant?.isVariant ?: false
            val isPartialySelected = pdpUiUpdater?.productNewVariantDataModel?.isPartialySelected()
                    ?: false

            if (viewModel.buttonActionType == ProductDetailConstant.REMIND_ME_BUTTON || viewModel.buttonActionType == ProductDetailConstant.CHECK_WISHLIST_BUTTON) {
                DynamicProductDetailTracking.Click.eventClickOosButton(btn_buy_now.text.toString(), isVariant, viewModel.getDynamicProductInfoP1, viewModel.userId)
            }

            if (!viewModel.isUserSessionActive) {
                doLoginWhenUserClickButton()
                return@let
            }

            if (viewModel.buttonActionType == ProductDetailConstant.REMIND_ME_BUTTON) {
                toasterWishlistText = getString(R.string.toaster_success_add_wishlist_from_button)
                addWishList()
                return@let
            }

            if (viewModel.buttonActionType == ProductDetailConstant.CHECK_WISHLIST_BUTTON) {
                goToWishlist()
                return@let
            }

            if (viewModel.buttonActionType == ProductDetailConstant.TRADEIN_BUTTON && viewModel.getDynamicProductInfoP1?.basic?.status == ProductStatusTypeDef.WAREHOUSE) {
                view?.showToasterError(getString(R.string.tradein_error_label), ctaText = getString(R.string.label_oke_pdp))
                return@let
            }

            if (isVariant && isPartialySelected) {
                if (pdpUiUpdater?.productNewVariantDataModel?.listOfVariantCategory == null) {
                    view.showToasterError(getString(R.string.variant_failed_load), ctaText = getString(R.string.product_refresh), ctaMaxWidth = 500, ctaListener = {
                        onSwipeRefresh()
                    })
                } else {
                    showErrorVariantUnselected()
                }
                return@let
            }

            when (viewModel.buttonActionType) {
                ProductDetailConstant.LEASING_BUTTON -> {
                    goToLeasing()
                    return@let
                }
                ProductDetailConstant.TRADEIN_BUTTON -> {
                    goToTradein()
                    return@let
                }
            }

            if (viewModel.getDynamicProductInfoP1?.checkImei(enableCheckImeiRemoteConfig) == true) {
                activity?.run {
                    ImeiPermissionAsker.checkImeiPermission(this, {
                        showImeiPermissionBottomSheet()
                    }, { hitAtc(buttonAction) }) { onNeverAskAgain() }
                }
            } else hitAtc(buttonAction)
        }
    }

    private fun doLoginWhenUserClickButton() {
        DynamicProductDetailTracking.Click.eventClickButtonNonLogin(viewModel.buttonActionType,
                viewModel.getDynamicProductInfoP1, viewModel.userId,
                viewModel.getDynamicProductInfoP1?.shopTypeString ?: "",
                viewModel.buttonActionText)
        goToLogin()
    }

    private fun showErrorVariantUnselected() {
        DynamicProductDetailTracking.Click.onVariantErrorPartialySelected(viewModel.getDynamicProductInfoP1, viewModel.buttonActionType)
        scrollToPosition(dynamicAdapter.getItemComponentIndex(pdpUiUpdater?.productNewVariantDataModel))
        val variantErrorMessage = if (viewModel.variantData?.getVariantsIdentifier()?.isEmpty() == true) {
            getString(R.string.add_to_cart_error_variant)
        } else {
            getString(R.string.add_to_cart_error_variant_builder, viewModel.variantData?.getVariantsIdentifier()
                    ?: "")
        }

        view?.showToasterError(variantErrorMessage, ctaText = getString(R.string.label_oke_pdp))
    }

    private fun buyAfterTradeinDiagnose(deviceId: String, phoneType: String, phonePrice: String) {
        viewModel.buttonActionType = ProductDetailConstant.TRADEIN_AFTER_DIAGNOSE
        viewModel.tradeinDeviceId = deviceId
        hitAtc(ProductDetailConstant.OCS_BUTTON)
    }

    private fun hitAtc(actionButton: Int) {
        val selectedWarehouseId = viewModel.getMultiOriginByProductId().id.toIntOrZero()

        viewModel.getDynamicProductInfoP1?.let { data ->
            showProgressDialog()
            when (actionButton) {
                ProductDetailConstant.OCS_BUTTON -> {
                    val addToCartOcsRequestParams = AddToCartOcsRequestParams().apply {
                        productId = data.basic.productID.toLongOrNull() ?: 0
                        shopId = data.basic.shopID.toIntOrZero()
                        quantity = data.basic.minOrder
                        notes = ""
                        customerId = viewModel.userId.toIntOrZero()
                        warehouseId = selectedWarehouseId
                        trackerAttribution = trackerAttributionPdp ?: ""
                        trackerListName = trackerListNamePdp ?: ""
                        isTradeIn = data.data.isTradeIn
                        shippingPrice = viewModel.shippingMinimumPrice
                        productName = data.getProductName
                        category = data.basic.category.name
                        price = data.finalPrice.toString()
                        userId = viewModel.userId
                    }
                    viewModel.addToCart(addToCartOcsRequestParams)
                }
                ProductDetailConstant.OCC_BUTTON -> {
                    addToCartOcc(data, selectedWarehouseId)
                }
                else -> {
                    val addToCartRequestParams = AddToCartRequestParams().apply {
                        productId = data.basic.productID.toLongOrNull() ?: 0
                        shopId = data.basic.shopID.toIntOrZero()
                        quantity = data.basic.minOrder
                        notes = ""
                        attribution = trackerAttributionPdp ?: ""
                        listTracker = trackerListNamePdp ?: ""
                        warehouseId = selectedWarehouseId
                        atcFromExternalSource = AddToCartRequestParams.ATC_FROM_PDP
                        productName = data.getProductName
                        category = data.basic.category.name
                        price = data.finalPrice.toString()
                        userId = viewModel.userId
                    }
                    viewModel.addToCart(addToCartRequestParams)
                }
            }
        }
    }

    private fun addToCartOcc(data: DynamicProductInfoP1, selectedWarehouseId: Int) {
        val addToCartOccRequestParams = AddToCartOccRequestParams(data.basic.productID, data.basic.shopID, data.basic.minOrder.toString()).apply {
            warehouseId = selectedWarehouseId.toString()
            attribution = trackerAttributionPdp ?: ""
            listTracker = trackerListNamePdp ?: ""
            productName = data.getProductName
            category = data.basic.category.name
            price = data.finalPrice.toString()
            userId = viewModel.userId
        }
        viewModel.addToCart(addToCartOccRequestParams)
    }

    private fun goToLeasing() {
        viewModel.getDynamicProductInfoP1?.run {
            val selectedProductId = basic.productID

            if (!viewModel.isUserSessionActive) {
                goToLogin()
                return@run
            }

            DynamicProductDetailTracking.Click.eventClickApplyLeasing(
                    viewModel.getDynamicProductInfoP1,
                    generateVariantString(viewModel.variantData)
            )

            val urlApplyLeasingWithProductId = String.format(
                    ProductDetailCommonConstant.URL_APPLY_LEASING,
                    selectedProductId
            )

            openWebViewUrl(urlApplyLeasingWithProductId)
        }
    }

    private fun openWebViewUrl(url: String) {
        val webViewUrl = String.format(
                "%s?titlebar=false&url=%s",
                ApplinkConst.WEBVIEW,
                url
        )
        RouteManager.route(context, webViewUrl)
    }

    private fun goToWishlist() {
        RouteManager.route(context, ApplinkConsInternalHome.HOME_WISHLIST)
    }

    override fun gotoShopDetail(componentTrackDataModel: ComponentTrackDataModel) {
        activity?.let {
            val shopId = viewModel.getDynamicProductInfoP1?.basic?.shopID ?: return
            DynamicProductDetailTracking.Click.eventImageShopClicked(viewModel.getDynamicProductInfoP1, shopId, componentTrackDataModel)
            startActivityForResult(RouteManager.getIntent(it,
                    ApplinkConst.SHOP, shopId),
                    ProductDetailConstant.REQUEST_CODE_SHOP_INFO)
        }
    }

    private fun onShopFavoriteClick(componentTrackDataModel: ComponentTrackDataModel? = null, isNplFollowType: Boolean = false) {
        if (viewModel.getShopInfo().isShopInfoNotEmpty()) {
            doActionOrLogin({
                nplFollowersButton?.startLoading()
                trackToggleFavoriteShop(componentTrackDataModel)
                pdpUiUpdater?.getShopInfo?.enableButtonFavorite = false
                pdpUiUpdater?.shopCredibility?.enableButtonFavorite = false
                dynamicAdapter.notifyWithPayload(pdpUiUpdater?.getShopInfo, ProductDetailConstant.PAYLOAD_TOOGLE_FAVORITE)
                dynamicAdapter.notifyWithPayload(pdpUiUpdater?.shopCredibility, ProductDetailConstant.PAYLOAD_TOOGLE_FAVORITE)
                viewModel.toggleFavorite(viewModel.getDynamicProductInfoP1?.basic?.shopID
                        ?: "", isNplFollowType)
            })
        }
    }

    private fun trackToggleFavoriteShop(componentTrackDataModel: ComponentTrackDataModel?) {
        val isFavorite = pdpUiUpdater?.getShopInfo?.isFavorite ?: return
        val shopName = pdpUiUpdater?.getShopInfo?.shopName ?: ""

        if (isFavorite)
            DynamicProductDetailTracking.Click.eventUnfollowShop(viewModel.getDynamicProductInfoP1, componentTrackDataModel, shopName)
        else
            DynamicProductDetailTracking.Click.eventFollowShop(viewModel.getDynamicProductInfoP1, componentTrackDataModel, shopName)
    }

    private fun onSuccessFavoriteShop(isSuccess: Boolean, isNplFollowerType: Boolean = false) {
        val isFavorite = pdpUiUpdater?.shopInfoMap?.isFavorite
                ?: pdpUiUpdater?.shopCredibility?.isFavorite ?: return
        if (isSuccess) {
            viewModel.clearCacheP2Data()
            pdpUiUpdater?.successUpdateShopFollow(if (isNplFollowerType) false else isFavorite)
            dynamicAdapter.notifyWithPayload(pdpUiUpdater?.shopInfoMap, ProductDetailConstant.PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)
            dynamicAdapter.notifyWithPayload(pdpUiUpdater?.shopCredibility, ProductDetailConstant.PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)

            setupNplVisibility(if (isNplFollowerType) ProductDetailConstant.HIDE_NPL_BS else isFavorite)
        }
    }

    private fun setupNplVisibility(isFavorite: Boolean) {
        val restrictionData = viewModel.p2Data.value?.restrictionInfo?.restrictionData
        nplFollowersButton?.setupVisibility = if (restrictionData?.isNotEmpty() == true && restrictionData.firstOrNull()?.action?.isNotEmpty() == true && !viewModel.isShopOwner()) {
            isFavorite
        } else {
            false
        }
    }

    private fun onFailFavoriteShop(t: Throwable) {
        view?.showToasterError(getErrorMessage(t), ctaText = getString(R.string.retry_label)) {
            onShopFavoriteClick()
        }
        pdpUiUpdater?.failUpdateShopFollow()
        dynamicAdapter.notifyWithPayload(pdpUiUpdater?.shopInfoMap, ProductDetailConstant.PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)
        dynamicAdapter.notifyWithPayload(pdpUiUpdater?.shopCredibility, ProductDetailConstant.PAYLOAD_TOOGLE_AND_FAVORITE_SHOP)
    }

    private fun onShopChatClicked() {
        if (viewModel.getShopInfo().isShopInfoNotEmpty()) {
            val product = viewModel.getDynamicProductInfoP1 ?: return
            doActionOrLogin({
                val shop = viewModel.getShopInfo()
                activity?.let {
                    val intent = RouteManager.getIntent(it,
                            ApplinkConst.TOPCHAT_ASKSELLER,
                            product.basic.shopID, "",
                            "product", shop.shopCore.name, shop.shopAssets.avatar)
                    VariantMapper.putChatProductInfoTo(intent, product.basic.productID, product, viewModel.variantData)
                    startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_TOP_CHAT)
                }
            })
        }
    }

    private fun initToolbarLight() {
        activity?.run {
            if (isAdded) {
                varToolbar?.setTitleTextColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N400))
                varToolbar?.setBackgroundColor(ContextCompat.getColor(this, R.color.Unify_N0))
                (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
                menu?.let {
                    if (it.size() > 2) {
                        it.findItem(R.id.action_share).icon = ContextCompat.getDrawable(this, R.drawable.ic_product_share_dark)
                        val menuCart = it.findItem(R.id.action_cart)
                        menuCart.actionView.cart_image_view.tag = R.drawable.ic_product_cart_counter_dark
                        setBadgeMenuCart(menuCart)
                    }
                }

                varToolbar?.overflowIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_product_more_dark)
            }
        }
    }

    private fun setBadgeMenuCart(menuCart: MenuItem) {
        activity?.run {
            val actionView = menuCart.actionView
            val cartImageView = actionView.cart_image_view
            val lottieCartView = actionView.cart_lottie_view
            cartImageView.setOnClickListener {
                gotoCart()
            }
            if (shouldShowCartAnimation) {
                if (actionView is SquareHFrameLayout) {
                    if (lottieCartView.visibility != View.VISIBLE) {
                        lottieCartView.addAnimatorListener(object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(animator: Animator?) {}

                            override fun onAnimationEnd(animator: Animator?) {
                                showBadgeMenuCart(cartImageView, lottieCartView, true)
                                shouldShowCartAnimation = false
                            }

                            override fun onAnimationCancel(animator: Animator?) {}

                            override fun onAnimationStart(animator: Animator?) {}
                        })
                        cartImageView.visibility = View.INVISIBLE
                        lottieCartView.visibility = View.VISIBLE
                        if (!lottieCartView.isAnimating) {
                            lottieCartView.playAnimation()
                        }
                    }
                }
            } else {
                showBadgeMenuCart(cartImageView, lottieCartView, false)
            }
        }
    }

    private fun gotoCart() {
        activity?.let {
            DynamicProductDetailTracking.Click.eventCartToolbarClicked(generateVariantString(viewModel.variantData),
                    viewModel.getDynamicProductInfoP1)
            doActionOrLogin({
                startActivity(RouteManager.getIntent(it, ApplinkConst.CART))
            })
        }
    }

    private fun showBadgeMenuCart(cartImageView: ImageView, lottieCartView: LottieAnimationView, animate: Boolean) {
        activity?.run {
            val localCacheHandler = LocalCacheHandler(context, "CART")
            val cartCount = localCacheHandler.getInt("CACHE_TOTAL_CART", 0)

            if (cartImageView.tag as? Int == null) {
                return@run
            }

            val icon = ContextCompat.getDrawable(this, cartImageView.tag as Int)
            if (icon is LayerDrawable) {
                val badge = CountDrawable(this)
                badge.setCount(if (cartCount > ProductDetailConstant.CART_MAX_COUNT) {
                    getString(R.string.pdp_label_cart_count_max)
                } else if (!viewModel.isUserSessionActive) {
                    "0"
                } else {
                    cartCount.toString()
                })
                icon.mutate()
                icon.setDrawableByLayerId(R.id.ic_cart_count, badge)
                cartImageView.setImageDrawable(icon)
                if (animate) {
                    val alphaAnimation = AlphaAnimation(ProductDetailConstant.CART_ALPHA_ANIMATION_FROM, ProductDetailConstant.CART_ALPHA_ANIMATION_TO)
                    val scaleAnimation = ScaleAnimation(ProductDetailConstant.CART_SCALE_ANIMATION_FROM, ProductDetailConstant.CART_SCALE_ANIMATION_TO, ProductDetailConstant.CART_SCALE_ANIMATION_FROM, ProductDetailConstant.CART_SCALE_ANIMATION_TO, Animation.RELATIVE_TO_SELF, ProductDetailConstant.CART_SCALE_ANIMATION_PIVOT, Animation.RELATIVE_TO_SELF, ProductDetailConstant.CART_SCALE_ANIMATION_PIVOT)
                    scaleAnimation.fillAfter = false
                    val animationSet = AnimationSet(false)
                    animationSet.addAnimation(alphaAnimation)
                    animationSet.addAnimation(scaleAnimation)
                    animationSet.duration = ProductDetailConstant.CART_ANIMATION_DURATION
                    animationSet.fillAfter = false
                    animationSet.fillBefore = false
                    animationSet.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationRepeat(p0: Animation?) {}

                        override fun onAnimationEnd(p0: Animation?) {
                            lottieCartView.clearAnimation()
                            cartImageView.clearAnimation()
                            lottieCartView.visibility = View.INVISIBLE
                            cartImageView.visibility = View.VISIBLE
                        }

                        override fun onAnimationStart(p0: Animation?) {}
                    })
                    lottieCartView.startAnimation(animationSet)
                } else {
                    lottieCartView.visibility = View.INVISIBLE
                    cartImageView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateActionButtonShadow() {
        if (stickyLoginView?.isShowing() == true) {
            actionButtonView.setBackground(R.color.Unify_N0)
        } else {
            val drawable = context?.let { _context -> ContextCompat.getDrawable(_context, R.drawable.bg_shadow_top) }
            drawable?.let { actionButtonView.setBackground(it) }
        }
    }

    private fun getErrorMessage(throwable: Throwable): String {
        return context?.let {
            ProductDetailErrorHandler.getErrorMessage(it, throwable)
        } ?: getString(R.string.merchant_product_detail_error_default)
    }

    private fun hideProgressDialog() {
        if (loadingProgressDialog != null && loadingProgressDialog?.isShowing == true) {
            loadingProgressDialog?.dismiss()
        }
    }

    private fun scrollToPosition(position: Int) {
        if (position >= 0) {
            getRecyclerView(view).post {
                try {
                    getRecyclerView(view).smoothScrollToPosition(position)
                } catch (e: Throwable) {

                }
            }
        }
    }

    private fun showProgressDialog(onCancelClicked: (() -> Unit)? = null) {
        if (loadingProgressDialog == null) {
            loadingProgressDialog = activity?.createDefaultProgressDialog(
                    getString(R.string.title_loading),
                    cancelable = onCancelClicked != null,
                    onCancelClicked = {
                        onCancelClicked?.invoke()
                    })
        }
        loadingProgressDialog?.run {
            if (!isShowing) {
                show()
            }
        }
    }

    private fun updateProductId() {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            productId = productInfo.basic.productID
        }
    }

    private fun hasTopAds() = topAdsGetProductManage.data.adId.isNotEmpty() && topAdsGetProductManage.data.adId != "0"

    private fun setupTradeinDialog(): ProductAccessRequestDialogFragment {
        val accessDialog = ProductAccessRequestDialogFragment()
        accessDialog.setBodyText(getString(R.string.pdp_tradein_text_permission_description))
        accessDialog.setTitle(getString(R.string.pdp_tradein_text_request_access))
        accessDialog.setNegativeButton("")
        accessDialog.setListener(this)
        return accessDialog
    }

    private fun assignDeviceId() {
        viewModel.deviceId = TradeInUtils.getDeviceId(context)
                ?: viewModel.userSessionInterface.deviceId ?: ""
    }

    private fun goToHargaFinal() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalCategory.FINAL_PRICE)
        val tradeinParam = viewModel.tradeInParams
        viewModel.getDynamicProductInfoP1?.let {
            tradeinParam.setPrice(it.data.price.value)
            tradeinParam.productId = it.basic.getProductId()
            tradeinParam.productName = it.data.name
        }

        intent.putExtra(TradeInParams.TRADE_IN_PARAMS, tradeinParam)
        startActivityForResult(intent, ApplinkConstInternalCategory.FINAL_PRICE_REQUEST_CODE)
    }

    private fun goToTradeInHome() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalCategory.TRADEIN)
        val tradeinParam = viewModel.tradeInParams

        viewModel.getDynamicProductInfoP1?.let {
            tradeinParam.setPrice(it.data.price.value)
            tradeinParam.productId = it.basic.getProductId()
            tradeinParam.productName = it.data.name
            tradeinParam.origin = viewModel.getMultiOriginByProductId().getOrigin()
        }
        intent.putExtra(TradeInParams.PARAM_PERMISSION_GIVEN, true)
        intent.putExtra(TradeInParams.TRADE_IN_PARAMS, tradeinParam)
        startActivityForResult(intent, ApplinkConstInternalCategory.TRADEIN_HOME_REQUEST)
    }

    private fun showDialogErrorAtc(result: AddToCartDataModel) {
        activity?.also { activity ->
            val createTicketDialog = DialogUnify(activity, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            createTicketDialog.apply {
                setTitle(result.errorReporter.texts.submitTitle)
                setDescription(result.errorReporter.texts.submitDescription)
                setSecondaryCTAText(result.errorReporter.texts.cancelButton)
                setSecondaryCTAClickListener {
                    this.dismiss()
                    productDetailTracking.eventClickCloseOnHelpPopUpAtc()
                }
                setPrimaryCTAText(result.errorReporter.texts.submitButton)
                setPrimaryCTAClickListener {
                    this.dismiss()
                    productDetailTracking.eventClickReportOnHelpPopUpAtc()
                    showProgressDialog()
                    viewModel.hitSubmitTicket(result, this@DynamicProductDetailFragment::onErrorSubmitHelpTicket, this@DynamicProductDetailFragment::onSuccessSubmitHelpTicket)
                }
                show()
            }
            productDetailTracking.eventViewHelpPopUpWhenAtc()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        activity?.run {
            ImeiPermissionAsker.onImeiRequestPermissionsResult(this, requestCode, permissions, grantResults,
                    onUserDenied = {}, onUserDeniedAndDontAskAgain = {}, onUserAcceptPermission = {}
            )
        }
    }

    private fun onNeverAskAgain() {
        DynamicProductDetailTracking.Click.eventClickBuyAskForImei(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS, viewModel.userId, viewModel.getDynamicProductInfoP1)
        activity?.run {
            CheckImeiBottomSheet.showPermissionDialog(this) {
                DynamicProductDetailTracking.Click.eventClickGiveAccessPhoneStatePermission(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS, viewModel.userId, viewModel.getDynamicProductInfoP1)
                showRationaleDialog()
            }
        }
    }

    private fun showRationaleDialog() {
        DynamicProductDetailTracking.Click.eventClickBuyAskForImei(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS_INFO, viewModel.userId, viewModel.getDynamicProductInfoP1)
        CheckImeiRationaleDialog.showRationaleDialog(activity, {
            DynamicProductDetailTracking.Click.eventClickGoToSetting(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS_INFO, viewModel.userId, viewModel.getDynamicProductInfoP1)
        }, {
            DynamicProductDetailTracking.Click.eventClickLater(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS_INFO, viewModel.userId, viewModel.getDynamicProductInfoP1)
        })
    }

    private fun observeToggleNotifyMe() {
        viewLifecycleOwner.observe(viewModel.toggleTeaserNotifyMe) { data ->
            data.doSuccessOrFail({
                viewModel.clearCacheP2Data()
                val messageSuccess = if (viewModel.notifyMeAction == ProductDetailCommonConstant.VALUE_TEASER_ACTION_REGISTER) getString(R.string.notify_me_success_registered_message) else getString(R.string.notify_me_success_unregistered_message)
                view?.showToasterSuccess(messageSuccess)
                viewModel.updateNotifyMeData()
            }, {
                onFailNotifyMe(it)
            })
        }
    }

    private fun onFailNotifyMe(t: Throwable) {
        val dataModel = pdpUiUpdater?.notifyMeMap
        view?.showToasterError(getErrorMessage(t), ctaText = getString(R.string.oke))
        if (dataModel != null) {
            pdpUiUpdater?.notifyMeMap?.notifyMe = !dataModel.notifyMe
            dynamicAdapter.notifyNotifyMe(pdpUiUpdater?.notifyMeMap, ProductDetailConstant.PAYLOAD_NOTIFY_ME)
        }
    }

    override fun onNotifyMeClicked(data: ProductNotifyMeDataModel, componentTrackDataModel: ComponentTrackDataModel) {
        try {
            activity?.let {
                if (viewModel.isUserSessionActive) {
                    viewModel.notifyMeAction = if (data.notifyMe) ProductDetailCommonConstant.VALUE_TEASER_ACTION_UNREGISTER else
                        ProductDetailCommonConstant.VALUE_TEASER_ACTION_REGISTER
                    pdpUiUpdater?.notifyMeMap?.notifyMe?.let { notifyMe -> trackToggleNotifyMe(componentTrackDataModel, notifyMe) }
                    pdpUiUpdater?.notifyMeMap?.notifyMe = !data.notifyMe
                    dynamicAdapter.notifyNotifyMe(pdpUiUpdater?.notifyMeMap, ProductDetailConstant.PAYLOAD_NOTIFY_ME)
                    viewModel.toggleTeaserNotifyMe(data.campaignID.toLongOrZero(), productId?.toLongOrZero()
                            ?: 0, ProductDetailCommonConstant.VALUE_TEASER_SOURCE)
                } else {
                    goToLogin()
                }
            }
        } catch (ex: Exception) {

        }
    }

    private fun trackToggleNotifyMe(componentTrackDataModel: ComponentTrackDataModel?, notifyMe: Boolean) {
        viewModel.getDynamicProductInfoP1?.let {
            DynamicProductDetailTracking.Click.eventNotifyMe(it, componentTrackDataModel, notifyMe, viewModel.userId)
        }
    }

    private fun trackOnTickerClicked(tickerTitle: String, tickerType: Int, componentTrackDataModel: ComponentTrackDataModel?, tickerDescription: String) {
        DynamicProductDetailTracking.Click.eventClickTicker(tickerTitle, tickerType, viewModel.getDynamicProductInfoP1, componentTrackDataModel, viewModel.userId, tickerDescription)
    }

    private fun showImeiPermissionBottomSheet() {
        DynamicProductDetailTracking.Click.eventClickBuyAskForImei(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS, viewModel.userId, viewModel.getDynamicProductInfoP1)
        activity?.run {
            CheckImeiBottomSheet.showPermissionDialog(this) {
                DynamicProductDetailTracking.Click.eventClickGiveAccessPhoneStatePermission(ProductTrackingConstant.ImeiChecker.CLICK_IMEI_PERMISSION_TITLE_NEED_ACCESS, viewModel.userId, viewModel.getDynamicProductInfoP1)
                ImeiPermissionAsker.askImeiPermissionFragment(this@DynamicProductDetailFragment)
            }
        }
    }

    private fun doActionOrLogin(actionLogin: () -> Unit, actionNonLogin: (() -> Unit)? = null) {
        if (viewModel.isUserSessionActive) {
            actionLogin.invoke()
        } else {
            actionNonLogin?.invoke()
            goToLogin()
        }
    }

    private fun goToLogin() {
        showProgressDialog()
        activity?.let {
            startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                    ProductDetailConstant.REQUEST_CODE_LOGIN)
        }
    }

    private fun goToReadingActivity() {
        viewModel.getDynamicProductInfoP1?.let {
            val intent = RouteManager.getIntent(context,
                    Uri.parse(UriUtil.buildUri(ApplinkConstInternalGlobal.PRODUCT_TALK, it.basic.productID))
                            .buildUpon()
                            .appendQueryParameter(PARAM_APPLINK_SHOP_ID, it.basic.shopID)
                            .appendQueryParameter(PARAM_APPLINK_IS_VARIANT_SELECTED, (pdpUiUpdater?.productNewVariantDataModel?.isPartialySelected()?.not()
                                    ?: false).toString())
                            .appendQueryParameter(PARAM_APPLINK_AVAILABLE_VARIANT, (viewModel.variantData?.getBuyableVariantCount()
                                    ?: 0).toString())
                            .build().toString()
            )
            startActivity(intent)
        }
    }

    private fun goToReplyActivity(questionID: String) {
        viewModel.getDynamicProductInfoP1?.let {
            val intent = RouteManager.getIntent(
                    context,
                    Uri.parse(UriUtil.buildUri(ApplinkConstInternalGlobal.TALK_REPLY, questionID))
                            .buildUpon()
                            .appendQueryParameter(PARAM_APPLINK_SHOP_ID, it.basic.shopID)
                            .build().toString()
            )
            startActivity(intent)
        }
    }

    private fun goToWriteActivity() {
        viewModel.getDynamicProductInfoP1?.basic?.productID?.let {
            val intent = RouteManager.getIntent(
                    context,
                    Uri.parse(ApplinkConstInternalGlobal.ADD_TALK)
                            .buildUpon()
                            .appendQueryParameter(ProductDetailConstant.PARAM_PRODUCT_ID, it)
                            .appendQueryParameter(PARAM_APPLINK_IS_VARIANT_SELECTED, (pdpUiUpdater?.productNewVariantDataModel?.isPartialySelected()?.not()
                                    ?: false).toString())
                            .appendQueryParameter(PARAM_APPLINK_AVAILABLE_VARIANT, (viewModel.variantData?.getBuyableVariantCount()
                                    ?: 0).toString())
                            .build().toString())
            startActivity(intent)
        }
    }

    private fun goToSellerMigrationPage(@SellerMigrationFeatureName featureName: String, appLinks: ArrayList<String>) {
        context?.run {
            val intent = RouteManager.getIntent(this, String.format("%s?${SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME}=%s", ApplinkConst.SELLER_MIGRATION, featureName))
            intent.putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, appLinks)
            intent.putExtra(SellerMigrationApplinkConst.EXTRA_SCREEN_NAME, screenName)
            startActivity(intent)
        }
    }

    private fun renderInitialAffiliate() {
        if (isAffiliate) {
            actionButtonView.gone()
            ticker_occ_layout.gone()
            base_btn_affiliate_dynamic.visible()
            loadingAffiliateDynamic.visible()
        }
    }

    private fun setupRemoteConfig() {
        activity?.run {
            remoteConfig = FirebaseRemoteConfigImpl(this)
        }

        if (::remoteConfig.isInitialized) {
            viewModel.enableCaching = remoteConfig.getBoolean(RemoteConfigKey.ANDROID_MAIN_APP_ENABLED_CACHE_PDP, true)
            enableCheckImeiRemoteConfig = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_CHECK_IMEI_PDP, false)
        }
    }

    //Will be delete soon
    override fun isNavOld(): Boolean {
        return GlobalConfig.isSellerApp()
    }

    private fun navAbTestCondition(ifNavRevamp: () -> Unit = {}, ifNavOld: () -> Unit = {}) {
        if (!isNavOld()) {
            ifNavRevamp.invoke()
        } else if (isNavOld()) {
            ifNavOld.invoke()
        }
    }

    private fun goToRecommendation() {
        val uri = UriUtil.buildUri(ProductDetailConstant.RECOM_URL, viewModel.getDynamicProductInfoP1?.basic?.productID).toString()
        RouteManager.route(context, uri)
    }

    private fun addWishList() {
        viewModel.addWishList(viewModel.getDynamicProductInfoP1?.basic?.productID
                ?: "", onSuccessAddWishlist = this::onSuccessAddWishlist, onErrorAddWishList = this::onErrorAddWishList)
    }

    private fun isProductOos(): Boolean {
        return pdpUiUpdater?.tickerInfoMap?.let {
            it.isOos() || it.isProductInactive()
        } ?: false
    }

    private fun writeDiscussion(tracker: () -> Unit) {
        doActionOrLogin({
            tracker.invoke()
            goToWriteActivity()
        })
        viewModel.updateLastAction(DynamicProductDetailTalkGoToWriteDiscussion)
    }

    override fun refreshPage() {
        activity?.let {
            onSwipeRefresh()
        }
    }

    override fun onTopAdsImageViewClicked(model: TopAdsImageDataModel, applink: String?, bannerId: String, bannerName: String) {
        applink?.let { goToApplink(it) }
        val position = dynamicAdapter.getTopAdsBannerPosition(model)
        DynamicProductDetailTracking.Click.eventTopAdsImageViewClicked(trackingQueue, viewModel.userId, bannerId, position, bannerName)
    }

    override fun onTopAdsImageViewImpression(model: TopAdsImageDataModel, bannerId: String, bannerName: String) {
        val position = dynamicAdapter.getTopAdsBannerPosition(model)
        DynamicProductDetailTracking.Impression.eventTopAdsImageViewImpression(trackingQueue, viewModel.userId, bannerId, position, bannerName)
    }
}