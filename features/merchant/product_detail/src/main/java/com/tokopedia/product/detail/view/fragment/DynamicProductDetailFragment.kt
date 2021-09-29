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
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.util.SparseIntArray
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
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
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator
import com.tokopedia.abstraction.Actions.interfaces.ActionUIDelegate
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
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.utils.TradeInUtils
import com.tokopedia.config.GlobalConfig
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.Dialog
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.device.info.permission.ImeiPermissionAsker
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.manager.*
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.utils.CoachMarkLocalCache
import com.tokopedia.gallery.ImageReviewGalleryActivity
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.mvcwidget.views.MvcView
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.mvcwidget.views.activities.TransParentActivity
import com.tokopedia.product.detail.BuildConfig
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.*
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_APPLINK_AVAILABLE_VARIANT
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_APPLINK_IS_VARIANT_SELECTED
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.PARAM_APPLINK_SHOP_ID
import com.tokopedia.product.detail.common.bottomsheet.OvoFlashDealsBottomSheet
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkir
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirImage
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.constant.TopAdsShopCategoryTypeDef
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.product.TopAdsGetProductManage
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimateData
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.getCurrencyFormatted
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductDataModel
import com.tokopedia.product.detail.data.model.datamodel.*
import com.tokopedia.product.detail.data.model.financing.FtInstallmentCalculationDataResponse
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.view.ProductDetailCommonBottomSheetBuilder
import com.tokopedia.product.detail.common.data.model.re.RestrictionData
import com.tokopedia.product.detail.common.data.model.re.RestrictionInfoResponse
import com.tokopedia.product.detail.data.model.upcoming.NotifyMeUiData
import com.tokopedia.product.detail.data.util.*
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper.generateUserLocationRequestRates
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PARAM_DIRECTED_FROM_MANAGE_OR_PDP
import com.tokopedia.product.detail.data.util.ProductDetailConstant.REMOTE_CONFIG_KEY_ENABLE_PDP_CUSTOM_SHARING
import com.tokopedia.product.detail.data.util.ProductDetailConstant.REMOTE_CONFIG_DEFAULT_ENABLE_PDP_CUSTOM_SHARING
import com.tokopedia.product.detail.data.util.VariantMapper.generateVariantString
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.imagepreview.view.activity.ImagePreviewPdpActivity
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.product.detail.view.activity.WholesaleActivity
import com.tokopedia.product.detail.view.adapter.diffutil.ProductDetailDiffUtil
import com.tokopedia.product.detail.view.adapter.dynamicadapter.ProductDetailAdapter
import com.tokopedia.product.detail.view.adapter.factory.DynamicProductDetailAdapterFactoryImpl
import com.tokopedia.product.detail.view.bottomsheet.ShopStatusInfoBottomSheet
import com.tokopedia.product.detail.view.fragment.partialview.PartialButtonActionView
import com.tokopedia.product.detail.view.fragment.partialview.TokoNowButtonData
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.listener.PartialButtonActionListener
import com.tokopedia.product.detail.view.util.*
import com.tokopedia.product.detail.view.viewholder.ProductSingleVariantViewHolder
import com.tokopedia.product.detail.view.viewmodel.DynamicProductDetailViewModel
import com.tokopedia.product.detail.view.viewmodel.ProductDetailSharedViewModel
import com.tokopedia.product.detail.view.widget.*
import com.tokopedia.product.estimasiongkir.data.model.RatesEstimateRequest
import com.tokopedia.product.estimasiongkir.view.bottomsheet.ProductDetailShippingBottomSheet
import com.tokopedia.product.info.util.ProductDetailBottomSheetBuilder
import com.tokopedia.product.info.view.bottomsheet.ProductDetailBottomSheetListener
import com.tokopedia.product.info.view.bottomsheet.ProductDetailInfoBottomSheet
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.checkout.ShipmentFormRequest
import com.tokopedia.recommendation_widget_common.RecommendationTypeConst
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.referral.Constants
import com.tokopedia.referral.ReferralAction
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.searchbar.navigation_component.icons.IconBuilder
import com.tokopedia.searchbar.navigation_component.icons.IconList
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.widget.PartialButtonShopFollowersListener
import com.tokopedia.shop.common.widget.PartialButtonShopFollowersView
import com.tokopedia.stickylogin.common.StickyLoginConstant
import com.tokopedia.stickylogin.view.StickyLoginAction
import com.tokopedia.stickylogin.view.StickyLoginView
import com.tokopedia.topads.detail_sheet.TopAdsDetailSheet
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.universal_sharing.view.bottomsheet.ScreenshotDetector
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ScreenShotListener
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.variant_common.util.VariantCommonMapper
import kotlinx.android.synthetic.main.dynamic_product_detail_fragment.*
import kotlinx.android.synthetic.main.menu_item_cart.view.*
import kotlinx.android.synthetic.main.partial_layout_button_action.*
import rx.subscriptions.CompositeSubscription
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Separator Rule
 * Without separator : ProductSnapshotViewHolder
 * Bottom separator : ProductVariantViewHolder, ProductNotifyMeViewHolder
 * Top separator : All of the view holder except above
 */

open class DynamicProductDetailFragment : BaseProductDetailFragment<DynamicPdpDataModel, DynamicProductDetailAdapterFactoryImpl>(),
        DynamicProductDetailListener,
        AtcVariantListener,
        ProductAccessRequestDialogFragment.Listener,
        PartialButtonActionListener,
        ProductDetailBottomSheetListener,
        PartialButtonShopFollowersListener,
        ScreenShotListener {

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
                        affiliateUniqueId: String? = null,
                        deeplinkUrl: String? = null,
                        layoutId: String? = null,
                        query: String? = null) = DynamicProductDetailFragment().also {
            it.arguments = Bundle().apply {
                productId?.let { pid -> putString(ProductDetailConstant.ARG_PRODUCT_ID, pid) }
                warehouseId?.let { whId -> putString(ProductDetailConstant.ARG_WAREHOUSE_ID, whId) }
                productKey?.let { pkey -> putString(ProductDetailConstant.ARG_PRODUCT_KEY, pkey) }
                shopDomain?.let { domain -> putString(ProductDetailConstant.ARG_SHOP_DOMAIN, domain) }
                trackerAttribution?.let { attribution -> putString(ProductDetailConstant.ARG_TRACKER_ATTRIBUTION, attribution) }
                trackerListName?.let { listName -> putString(ProductDetailConstant.ARG_TRACKER_LIST_NAME, listName) }
                affiliateString?.let { affiliateString -> putString(ProductDetailConstant.ARG_AFFILIATE_STRING, affiliateString) }
                affiliateUniqueId?.let { affiliateUniqueId -> putString(ProductDetailConstant.ARG_AFFILIATE_UNIQUE_ID, affiliateUniqueId) }
                deeplinkUrl?.let { deeplinkUrl -> putString(ProductDetailConstant.ARG_DEEPLINK_URL, deeplinkUrl) }
                layoutId?.let { layoutId -> putString(ProductDetailConstant.ARG_LAYOUT_ID, layoutId) }
                putBoolean(ProductDetailConstant.ARG_FROM_DEEPLINK, isFromDeeplink)
                putBoolean(ProductDetailConstant.ARG_FROM_AFFILIATE, isAffiliate)
                query?.let { qry -> putString(ProductDetailConstant.ARG_QUERY_PARAMS, qry) }
            }
        }
    }

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var sharedViewModel: ProductDetailSharedViewModel? = null
    private var screenshotDetector: ScreenshotDetector? = null

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
    private var topAdsGetProductManage: TopAdsGetProductManage = TopAdsGetProductManage()

    // This productId is only use for backend hit
    private var productId: String? = null
    private var productKey: String? = null
    private var shopDomain: String? = null
    private var isAffiliate = false
    private var affiliateString: String? = null
    private var affiliateUniqueId: String = ""
    private var deeplinkUrl: String = ""
    private var isFromDeeplink: Boolean = false
    private var layoutId: String = ""
    private var trackerAttributionPdp: String? = ""
    private var trackerListNamePdp: String? = ""
    private var warehouseId: String? = null
    private var doActivityResult = true
    private var shouldFireVariantTracker = true
    private var recomWishlistItem: RecommendationItem? = null
    private var pdpUiUpdater: PdpUiUpdater? = PdpUiUpdater(mutableMapOf())
    private var alreadyPerformSellerMigrationAction = false
    private var alreadyHitSwipeTracker: DynamicProductDetailSwipeTrackingState? = null
    private var alreadyHitVideoTracker: Boolean = false
    private var alreadyHitQtyTracker: Boolean = false
    private var shouldRefreshProductInfoBottomSheet = false
    private var shouldRefreshShippingBottomSheet = false
    private var uuid = ""
    private var urlQuery: String = ""

    //Prevent several method at onResume to being called when first open page.
    private var firstOpenPage: Boolean? = null

    //View
    private var varToolbar: Toolbar? = null
    private lateinit var actionButtonView: PartialButtonActionView
    private var stickyLoginView: StickyLoginView? = null
    private var shouldShowCartAnimation = false
    private var loadingProgressDialog: ProgressDialog? = null
    private var productVideoCoordinator: ProductVideoCoordinator? = null
    private val adapterFactory by lazy { DynamicProductDetailAdapterFactoryImpl(this, this) }
    private val coachMarkBoePref by lazy { CoachMarkLocalCache(context) }
    private val adapter by lazy {
        val asyncDifferConfig: AsyncDifferConfig<DynamicPdpDataModel> = AsyncDifferConfig.Builder(ProductDetailDiffUtil())
                .build()
        ProductDetailAdapter(asyncDifferConfig, this, adapterFactory)
    }
    private var menu: Menu? = null
    private var navToolbar: NavToolbar? = null
    private var toasterWishlistText = ""

    private var buttonActionType: Int = 0
    private var isTopadsDynamicsSlottingAlreadyCharged = false

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

    private val compositeSubscription by lazy { CompositeSubscription() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBtnAction()
        navAbTestCondition({ initNavToolbar() }, { initToolbar() })
        renderInitialAffiliate()

        if (!viewModel.isUserSessionActive) initStickyLogin(view)
        screenshotDetector = context?.let {
            UniversalShareBottomSheet.createAndStartScreenShotDetector(
                    it, this, this,
                    addFragmentLifecycleObserver = true,
                    permissionListener = shareProductInstance?.universalSharePermissionListener
            )
        }
    }

    override fun onSwipeRefresh() {
        recommendationCarouselPositionSavedState.clear()
        shouldRefreshProductInfoBottomSheet = true
        shouldRefreshShippingBottomSheet = true
        super.onSwipeRefresh()
    }

    override fun createAdapterInstance(): ProductDetailAdapter = adapter

    override fun getScreenName(): String = ""

    override fun initInjector() {
        productDaggerComponent = getComponent(ProductDetailComponent::class.java)
        productDaggerComponent?.inject(this)
    }

    override fun observeData() {
        observeP1()
        observeP2Data()
        observeP2Login()
        observeP3()
        observeToggleFavourite()
        observeToggleNotifyMe()
        observeRecommendationProduct()
        observeImageVariantPartialyChanged()
        observeAddToCart()
        observeInitialVariantData()
        observeSingleVariantData()
        observeonVariantClickedData()
        observeATCTokonowData()
        observeATCTokonowResetCard()
        observeATCRecomTokonowNonLogin()
        observeATCRecomSendTracker()
        observeDiscussionData()
        observeP2Other()
        observeTopAdsImageData()
        observeVideoDetail()
        observeShippingAddressChanged()
        observeUpdateCart()
        observeMiniCart()
        observeTopAdsIsChargeData()
        observeDeleteCart()
    }

    override fun loadData(forceRefresh: Boolean) {
        if (productId != null || (productKey != null && shopDomain != null)) {
            context?.let {
                (it as? ProductDetailActivity)?.startMonitoringPltNetworkRequest()
                viewModel.getProductP1(ProductParams(productId = productId, shopDomain = shopDomain, productName = productKey, warehouseId = warehouseId),
                        forceRefresh, isAffiliate, layoutId, isNavOld(), ChooseAddressUtils.getLocalizingAddressData(it)
                        ?: LocalCacheModel(), affiliateUniqueString = affiliateUniqueId, uuid, urlQuery)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            doActivityResult = savedInstanceState.getBoolean(ProductDetailConstant.SAVED_ACTIVITY_RESULT, true)
        }
        arguments?.let {
            productId = it.getString(ProductDetailConstant.ARG_PRODUCT_ID)
            warehouseId = it.getString(ProductDetailConstant.ARG_WAREHOUSE_ID)
            productKey = it.getString(ProductDetailConstant.ARG_PRODUCT_KEY)
            shopDomain = it.getString(ProductDetailConstant.ARG_SHOP_DOMAIN)
            trackerAttributionPdp = it.getString(ProductDetailConstant.ARG_TRACKER_ATTRIBUTION)
            trackerListNamePdp = it.getString(ProductDetailConstant.ARG_TRACKER_LIST_NAME)
            affiliateString = it.getString(ProductDetailConstant.ARG_AFFILIATE_STRING)
            affiliateUniqueId = it.getString(ProductDetailConstant.ARG_AFFILIATE_UNIQUE_ID, "")
            isAffiliate = it.getBoolean(ProductDetailConstant.ARG_FROM_AFFILIATE, false)
            deeplinkUrl = it.getString(ProductDetailConstant.ARG_DEEPLINK_URL, "")
            isFromDeeplink = it.getBoolean(ProductDetailConstant.ARG_FROM_DEEPLINK, false)
            layoutId = it.getString(ProductDetailConstant.ARG_LAYOUT_ID, "")
            urlQuery = it.getString(ProductDetailConstant.ARG_QUERY_PARAMS, "")
        }
        activity?.let {
            sharedViewModel = ViewModelProvider(it).get(ProductDetailSharedViewModel::class.java)
        }
        uuid = UUID.randomUUID().toString()
        firstOpenPage = true
        super.onCreate(savedInstanceState)
        assignDeviceId()
        loadData()
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
        reloadUserLocationChanged()
        reloadMiniCart()
    }

    override fun onDestroy() {
        hideProgressDialog()
        viewModel.p2Data.removeObservers(this)
        viewModel.p2Other.removeObservers(this)
        viewModel.productLayout.removeObservers(this)
        viewModel.p2Login.removeObservers(this)
        viewModel.productInfoP3.removeObservers(this)
        viewModel.loadTopAdsProduct.removeObservers(this)
        viewModel.updatedImageVariant.removeObservers(this)
        viewModel.initialVariantData.removeObservers(this)
        viewModel.onVariantClickedData.removeObservers(this)
        viewModel.toggleTeaserNotifyMe.removeObservers(this)
        viewModel.addToCartLiveData.removeObservers(this)
        viewModel.discussionMostHelpful.removeObservers(this)
        viewModel.topAdsRecomChargeData.removeObservers(this)
        viewModel.flush()
        compositeSubscription.clear()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_product_detail_dark, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        initToolBarMethod?.invoke()
    }

    private fun onResultVariantBottomSheet(data: ProductVariantResult) {
        if (data.shouldRefreshPreviousPage) {
            productId = data.selectedProductId
            //donot run onresume
            firstOpenPage = true
            onSwipeRefresh()
        } else {
            if (data.requestCode == ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT) {
                updateCartNotification()
            } else if (data.requestCode == ProductDetailCommonConstant.REQUEST_CODE_ATC_VAR_CHANGE_ADDRESS) {
                productId = data.selectedProductId
                onSuccessUpdateAddress()
                return
            }

            if (data.isFollowShop) {
                onSuccessFavoriteShop(true, true)
            }

            val isSelectedTheSame = pdpUiUpdater?.productSingleVariant?.mapOfSelectedVariant?.values?.containsAll(data.mapOfSelectedVariantOption?.values
                    ?: mutableListOf())
                    ?: false // means selected variant in bottom sheet is the same in pdp
            val noNeedToUpdateVariant = pdpUiUpdater?.productSingleVariant == null ||
                    pdpUiUpdater?.productSingleVariant?.isVariantError == true ||
                    data.mapOfSelectedVariantOption == null ||
                    isSelectedTheSame

            if (noNeedToUpdateVariant) {
                if (data.requestCode == ProductDetailCommonConstant.REQUEST_CODE_TRADEIN_PDP) {
                    onTradeinClickedAfter()
                }
                return
            }

            pdpUiUpdater?.updateVariantSelected(data.mapOfSelectedVariantOption)
            val variantLevelOne = ProductDetailVariantLogic.determineVariant(data.mapOfSelectedVariantOption
                    ?: mapOf(), viewModel.variantData)
            updateVariantDataAndUi(if (variantLevelOne != null) listOf(variantLevelOne) else listOf()) {
                if (data.requestCode == ProductDetailCommonConstant.REQUEST_CODE_TRADEIN_PDP) {
                    onTradeinClickedAfter()
                }
            }
            scrollVariantToSelectedPosition()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activity != null) {
            activity?.let { AdultManager.handleActivityResult(it, requestCode, resultCode, data) }
        }

        context?.let {
            AtcVariantHelper.onActivityResultAtcVariant(it, requestCode, data) {
                onResultVariantBottomSheet(this)
            }
        }

        when (requestCode) {
            ApplinkConstInternalCategory.FINAL_PRICE_REQUEST_CODE,
            ApplinkConstInternalCategory.TRADEIN_HOME_REQUEST -> {
                data?.let {
                    val deviceId = data.getStringExtra(TradeInParams.PARAM_DEVICE_ID) ?: ""
                    val phoneType = data.getStringExtra(TradeInParams.PARAM_PHONE_TYPE) ?: ""
                    ?: "none/other"
                    val phonePrice = data.getStringExtra(TradeInParams.PARAM_PHONE_PRICE) ?: ""
                    DynamicProductDetailTracking.TradeIn.eventAddToCartFinalPrice(phoneType,
                            phonePrice,
                            deviceId,
                            viewModel.userId,
                            viewModel.getDynamicProductInfoP1)
                    buyAfterTradeinDiagnose(deviceId, phoneType, phonePrice)
                }
            }
            ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT -> {
                updateCartNotification()
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
                    updateUi()
                }
            }
            ProductDetailConstant.REQUEST_CODE_SHOP_INFO -> {
                if (data != null) {
                    val isFavoriteFromShopPage = data.getBooleanExtra(ProductDetailConstant.SHOP_STATUS_FAVOURITE, false)
                    val isUserLoginFromShopPage = data.getBooleanExtra(ProductDetailConstant.SHOP_STICKY_LOGIN, false)
                    val wasFavorite = pdpUiUpdater?.shopCredibility?.isFavorite ?: return

                    if (isUserLoginFromShopPage) {
                        stickyLoginView?.hide()
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
                        val wasFavorite = pdpUiUpdater?.shopCredibility?.isFavorite ?: return

                        if (isFavoriteFromTopChat != wasFavorite) {
                            onSuccessFavoriteShop(true)
                        }
                    }
                }
            }
            PRODUCT_CARD_OPTIONS_REQUEST_CODE -> {
                handleProductCardOptionsActivityResult(requestCode, resultCode, data,
                        object : ProductCardOptionsWishlistCallback {
                            override fun onReceiveWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
                                handleWishlistAction(productCardOptionsModel)
                            }
                        })
            }
            MvcView.REQUEST_CODE -> {
                if (resultCode == MvcView.RESULT_CODE_OK && doActivityResult) {
                    onSwipeRefresh()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun scrollVariantToSelectedPosition() {
        val vh = getViewHolderByPosition(getComponentPositionBeforeUpdate(pdpUiUpdater?.productSingleVariant)) as? ProductSingleVariantViewHolder
        Handler().postDelayed({
            vh?.scrollToPosition(pdpUiUpdater?.productSingleVariant?.variantLevelOne?.getPositionOfSelected()
                    ?: -1)
        }, ProductDetailConstant.VARIANT_SCROLL_DELAY)
    }

    private fun trackVideoState() {
        if (!alreadyHitVideoTracker && productVideoCoordinator != null) {
            val videoTrackerData = viewModel.videoTrackerData
            val isAutoPlay = if (context == null) false else DeviceConnectionInfo.isConnectWifi(requireContext())

            videoTrackerData?.let {
                DynamicProductDetailTracking.Click.eventVideoStateChange(
                        viewModel.getDynamicProductInfoP1, viewModel.userId, DynamicProductDetailTracking.generateComponentTrackModel(pdpUiUpdater?.mediaMap, 0),
                        videoTrackerData.first, videoTrackerData.second, isAutoPlay
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

    private fun reloadMiniCart() {
        if (viewModel.getDynamicProductInfoP1 == null || context == null || viewModel.getDynamicProductInfoP1?.basic?.isTokoNow == false || firstOpenPage == true || !viewModel.isUserSessionActive) return
        val data = viewModel.getDynamicProductInfoP1
        viewModel.getMiniCart(data?.basic?.shopID ?: "")
    }

    private fun reloadUserLocationChanged() {
        if (viewModel.getDynamicProductInfoP1 == null || context == null || firstOpenPage == null || firstOpenPage == true) return
        val isUserLocationChanged = ChooseAddressUtils.isLocalizingAddressHasUpdated(requireContext(), viewModel.getUserLocationCache())
        if (isUserLocationChanged) {
            refreshPage()
        }
    }

    private fun setNavToolBarCartCounter() {
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
                onClickShareProduct(); true
            }
            R.id.action_cart -> {
                gotoCart(); true
            }
            R.id.action_report -> {
                reportProductFromToolbar(); true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSeeMoreDescriptionClicked(dataContent: List<ProductDetailInfoContent>, componentTrackDataModel: ComponentTrackDataModel) {
        activity?.let {
            DynamicProductDetailTracking.Click.eventClickProductDescriptionReadMore(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
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

    override fun getApplicationContext(): Application? {
        return activity?.application
    }

    override fun getLifecycleFragment(): Lifecycle {
        return lifecycle
    }

    /**
     * ImpressionComponent
     */
    override fun onImpressComponent(componentTrackDataModel: ComponentTrackDataModel) {
        when (componentTrackDataModel.componentName) {
            ProductDetailConstant.PRODUCT_PROTECTION -> DynamicProductDetailTracking.Impression
                    .eventEcommerceDynamicComponent(trackingQueue, componentTrackDataModel,
                            viewModel.getDynamicProductInfoP1, getPPTitleName(), getPurchaseProtectionUrl(), viewModel.userId)
            else -> DynamicProductDetailTracking.Impression
                    .eventEcommerceDynamicComponent(trackingQueue, componentTrackDataModel,
                            viewModel.getDynamicProductInfoP1, "", "", viewModel.userId)

        }
    }

    /**
     * ProductShopInfoViewHolder
     */
    override fun onShopInfoClicked(itemId: Int, componentTrackDataModel: ComponentTrackDataModel) {
        when (itemId) {
            R.id.btn_follow -> onShopFavoriteClick()
            R.id.shop_ava, R.id.shop_name -> gotoShopDetail(componentTrackDataModel)
            else -> {

            }
        }
    }

    override fun onCategoryCarouselImageClicked(url: String, categoryTitle: String, categoryId: String, componentTrackDataModel: ComponentTrackDataModel?) {
        DynamicProductDetailTracking.Click.onImageCategoryCarouselClicked(viewModel.getDynamicProductInfoP1, componentTrackDataModel, categoryTitle, categoryId)
        goToApplink(url)
    }

    override fun onCategoryCarouselSeeAllClicked(url: String, componentTrackDataModel: ComponentTrackDataModel?) {
        DynamicProductDetailTracking.Click.onSeeAllCategoryCarouselClicked(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
        val localizationWarehouseId = viewModel.getUserLocationCache().warehouse_id
        RouteManager.route(context, ApplinkConstInternalTokopediaNow.CATEGORY_LIST, localizationWarehouseId)
    }

    override fun onCategoryClicked(url: String, componentTrackDataModel: ComponentTrackDataModel) {
        if (!GlobalConfig.isSellerApp()) {

            viewModel.getDynamicProductInfoP1?.basic?.category?.detail?.let {
                val categoryId = it.lastOrNull()?.id ?: ""
                val categoryName = it.lastOrNull()?.name ?: ""
                DynamicProductDetailTracking.Click.eventCategoryClicked(categoryId, categoryName, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
            }
            goToApplink(url)
        }
    }

    override fun onEtalaseClicked(url: String, componentTrackDataModel: ComponentTrackDataModel) {
        viewModel.getDynamicProductInfoP1?.basic?.menu?.let {
            val etalaseId = it.id
            val etalaseName = it.name
            DynamicProductDetailTracking.Click.eventEtalaseClicked(etalaseId, etalaseName, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
        }

        goToApplink(url)
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
    override fun onInfoClicked(appLink: String, name: String, componentTrackDataModel: ComponentTrackDataModel) {
        when (name) {
            ProductDetailConstant.TRADE_IN -> {
                onTradeinClicked(componentTrackDataModel)
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
            ProductDetailConstant.PRODUCT_INSTALLMENT_PAYLATER_INFO -> {
                goToApplink(appLink)
                DynamicProductDetailTracking.Click.eventClickPDPInstallmentSeeMore(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
            }
        }
    }

    private fun onTradeinClicked(componentTrackDataModel: ComponentTrackDataModel?) {
        val data = viewModel.getDynamicProductInfoP1
        if (pdpUiUpdater?.productSingleVariant != null) {
            if (data?.data?.variant?.isVariant == false) {
                onTradeinClickedAfter(componentTrackDataModel)
            } else {
                if (!viewModel.isUserSessionActive) {
                    doLoginWhenUserClickButton()
                    return
                }

                viewModel.variantData?.let {
                    goToAtcVariant(AtcVariantHelper.generateSimpanCartRedirection(
                            productVariant = it,
                            buttonText = context?.getString(R.string.pdp_choose_variant) ?: "",
                            customCartType = ProductDetailCommonConstant.KEY_SAVE_TRADEIN_BUTTON)
                    )
                }
            }
        } else {
            onTradeinClickedAfter(componentTrackDataModel)
        }
    }

    private fun onTradeinClickedAfter(componentTrackDataModel: ComponentTrackDataModel? = null) {
        if (pdpUiUpdater?.productSingleVariant == null) {
            val isVariant = viewModel.getDynamicProductInfoP1?.data?.variant?.isVariant ?: false
            val isPartialySelected = pdpUiUpdater?.productNewVariantDataModel?.isPartialySelected()
                    ?: false

            if (isVariant && isPartialySelected) {
                showErrorVariantUnselected()
                return
            }
        }

        if (!viewModel.isUserSessionActive) {
            doLoginWhenUserClickButton()
            return
        }

        if (viewModel.getDynamicProductInfoP1?.basic?.status == ProductStatusTypeDef.WAREHOUSE) {
            view?.showToasterError(getString(R.string.tradein_error_label), ctaText = getString(R.string.label_oke_pdp))
            return
        }

        if (openShipmentBottomSheetWhenError()) return

        DynamicProductDetailTracking.Click.trackTradein(viewModel.tradeInParams.usedPrice.toDouble(), viewModel.getDynamicProductInfoP1, componentTrackDataModel)

        goToTradein()
    }

    private fun getPurchaseProtectionUrl(): String {
        return viewModel.p2Data.value?.productPurchaseProtectionInfo?.ppItemDetailPage?.linkURL
                ?: ""
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

    override fun onChannelRecommendationEmpty(channelPosition: Int, data: RecommendationWidget?) {
        data?.let {
            pdpUiUpdater?.removeEmptyRecommendation(it)
            updateUi()
        }
    }

    override fun onRecommendationBannerImpressed(data: RecommendationWidget, templateNameType: String) {
        DynamicProductDetailTracking.ImpulsiveBanner.impressImpulsiveBanner(
                widget = data,
                userId = viewModel.userId,
                productId = productId ?: "",
                templateNameType = templateNameType,
                basicData = ProductRecomLayoutBasicData(
                        generalLayoutName = getPdpDataSource()?.layoutName ?: "",
                        categoryName = getPdpDataSource()?.basic?.category?.name ?: "",
                        categoryId = getPdpDataSource()?.basic?.category?.id ?: ""
                )
        )
    }

    override fun onRecommendationBannerClicked(appLink: String, data: RecommendationWidget, templateNameType: String) {
        DynamicProductDetailTracking.ImpulsiveBanner.clickImpulsiveBanner(
                widget = data,
                userId = viewModel.userId,
                productId = productId ?: "",
                templateNameType = templateNameType,
                basicData = ProductRecomLayoutBasicData(
                        generalLayoutName = getPdpDataSource()?.layoutName ?: "",
                        categoryName = getPdpDataSource()?.basic?.category?.name ?: "",
                        categoryId = getPdpDataSource()?.basic?.category?.id ?: ""
                )
        )
        goToApplink(appLink)
    }

    override fun onRecomAddToCartNonVariantQuantityChangedClick(recomItem: RecommendationItem, quantity: Int, adapterPosition: Int, itemPosition: Int) {
        pdpUiUpdater?.updateCurrentQuantityRecomItem(recomItem)
        viewModel.onAtcRecomNonVariantQuantityChanged(recomItem, quantity)
    }

    override fun onRecomAddVariantClick(recomItem: RecommendationItem, adapterPosition: Int, itemPosition: Int) {
        requireContext().let {
            AtcVariantHelper.goToAtcVariant(
                    context = it,
                    productId = recomItem.productId.toString(),
                    pageSource = "tokonow",
                    isTokoNow = true,
                    shopId = recomItem.shopId.toString(),
                    startActivitResult = { data, _ ->
                        startActivity(data)
                    }
            )
        }

    }

    override fun onChipFilterClicked(recommendationDataModel: ProductRecommendationDataModel, annotationChip: AnnotationChip, position: Int, filterPosition: Int) {
        DynamicProductDetailTracking.Click.eventClickSeeFilterAnnotation(annotationChip.recommendationFilterChip.value)
        viewModel.getRecommendation(recommendationDataModel, annotationChip, position, filterPosition)
    }

    override fun onSeeAllRecomClicked(recommendationWidget: RecommendationWidget, pageName: String, applink: String, componentTrackDataModel: ComponentTrackDataModel) {
        DynamicProductDetailTracking.Click.eventClickSeeMoreRecomWidget(recommendationWidget, pageName, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
        RouteManager.route(context, applink)
    }

    override fun eventRecommendationClick(recomItem: RecommendationItem, chipValue: String, position: Int, pageName: String, title: String, componentTrackDataModel: ComponentTrackDataModel) {
        DynamicProductDetailTracking.Click.eventRecommendationClick(
                recomItem, chipValue, false, position, viewModel.isUserSessionActive, pageName, title, viewModel.getDynamicProductInfoP1, componentTrackDataModel)
    }

    override fun eventRecommendationImpression(recomItem: RecommendationItem, chipValue: String, position: Int, pageName: String, title: String, componentTrackDataModel: ComponentTrackDataModel) {
        if (::trackingQueue.isInitialized) {
            DynamicProductDetailTracking.Impression.eventRecommendationImpression(
                    trackingQueue, position, recomItem, chipValue, false, viewModel.isUserSessionActive, pageName, title,
                    viewModel.getDynamicProductInfoP1, componentTrackDataModel)
        }
    }

    override fun onThreeDotsClick(recomItem: RecommendationItem, adapterPosition: Int, carouselPosition: Int) {
        recomWishlistItem = recomItem
        showProductCardOptions(
                this,
                recomItem.createProductCardOptionsModel(adapterPosition))
    }

    override fun getParentRecyclerViewPool(): RecyclerView.RecycledViewPool? {
        return getRecyclerView()?.recycledViewPool
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

    private fun reportProductFromToolbar() {
        reportProduct({
            DynamicProductDetailTracking.Click.eventReportLogin()
        }, {
            DynamicProductDetailTracking.Click.eventReportNoLogin()
        })
    }

    override fun onSeeAllTextView(componentTrackDataModel: ComponentTrackDataModel?) {
        viewModel.getDynamicProductInfoP1?.run {
            DynamicProductDetailTracking.Click.onSeeAllReviewTextView(this, viewModel.userId, componentTrackDataModel
                    ?: ComponentTrackDataModel())
            goToReviewDetail(basic.productID, getProductName)
        }
    }

    override fun shouldShowRatingAndReviewCount(): Boolean {
        return shouldGoToNewGallery()
    }

    override fun onImageReviewClick(listOfImage: List<ImageReviewItem>, position: Int, componentTrackDataModel: ComponentTrackDataModel?, imageCount: String) {
        context?.let {
            DynamicProductDetailTracking.Click.eventClickReviewOnBuyersImage(viewModel.getDynamicProductInfoP1, componentTrackDataModel
                    ?: ComponentTrackDataModel(), listOfImage[position].reviewId)
            val listOfImageReview: List<String> = listOfImage.map {
                it.imageUrlLarge ?: ""
            }
            ImageReviewGalleryActivity.moveTo(context, ArrayList(listOfImageReview), position, viewModel.getDynamicProductInfoP1?.basic?.productID
                    ?: "", imageCount)
        }
    }

    override fun onReviewClick() {
        viewModel.getDynamicProductInfoP1?.run {
            DynamicProductDetailTracking.Iris.eventReviewClickedIris(this, deeplinkUrl, basic.shopName)
            DynamicProductDetailTracking.Moengage.sendMoEngageClickReview(this)
            goToReviewDetail(basic.productID, getProductName)
        }
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

    override fun onMerchantVoucherSummaryClicked(shopId: String, source: Int) {
        context?.let {
            startActivityForResult(TransParentActivity.getIntent(it, shopId, source), MvcView.REQUEST_CODE)
        }
    }

    override fun isOwner(): Boolean = viewModel.isShopOwner()

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

    override fun onVideoStateChange(stopDuration: Long, videoDuration: Long) {
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
                    position = position,
                    disableDownload = true)
            startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_IMAGE_PREVIEW)
        }
    }

    override fun txtTradeinClicked(componentTrackDataModel: ComponentTrackDataModel) {
        DynamicProductDetailTracking.Click.eventClickTradeInRibbon(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
        scrollToPosition(getComponentPosition(pdpUiUpdater?.productTradeinMap))
    }

    override fun onAccept() {
        val usedPrice = viewModel.p2Data.value?.validateTradeIn?.usedPrice.toDoubleOrZero()
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
        activity?.let {
            Dialog(it, Dialog.Type.LONG_PROMINANCE).apply {
                setTitle(getString(R.string.campaign_expired_title))
                setDesc(getString(R.string.campaign_expired_descr))
                setBtnCancel(getString(com.tokopedia.abstraction.R.string.close))
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
                        toasterWishlistText = if (isProductOos()) getString(R.string.toaster_success_add_wishlist_from_fab) else getString(com.tokopedia.wishlist.common.R.string.msg_success_add_wishlist)
                        addWishList()
                        productInfo.let {
                            DynamicProductDetailTracking.Moengage.eventPDPWishlistAppsFyler(it)
                        }
                        DynamicProductDetailTracking.Click.eventPDPAddToWishlist(viewModel.getDynamicProductInfoP1, componentTrackDataModel)
                    }
                }
                if (isAffiliate && productInfo?.basic?.productID?.isNotEmpty() == true) {
                    DynamicProductDetailTracking.Click.eventClickWishlistOnAffiliate(
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
            val intent = RouteManager.getIntent(it, ApplinkConst.PRODUCT_REVIEW, productId)
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
        val productId = viewModel.getDynamicProductInfoP1?.basic?.productID ?: ""
        if (shouldGoToNewGallery()) {
            RouteManager.route(context, ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY, productId)
            return
        }
        ImageReviewGalleryActivity.moveTo(activity, productId)
    }

    private fun observeVideoDetail() {
        activity?.let { activity ->
            sharedViewModel?.productVideoData?.observe(activity, {
                if (it.isEmpty()) return@observe
                productVideoCoordinator?.updateAndResume(it)
            })
        }
    }

    private fun observeMiniCart() {
        viewModel.miniCartData.observe(viewLifecycleOwner) {
            if (it) {
                updateButtonState()
                if (firstOpenPage == false) {
                    pdpUiUpdater?.updateRecomTokonowQuantityData(viewModel.p2Data.value?.miniCart)
                    updateUi()
                }
            }
        }
    }

    private fun observeDeleteCart() {
        viewModel.deleteCartLiveData.observe(viewLifecycleOwner) {
            hideProgressDialog()
            it.doSuccessOrFail({ message ->
                view?.showToasterSuccess(message.data, ctaText = getString(R.string.label_oke_pdp))
                updateButtonState()
            }) { throwable ->
                view?.showToasterError(
                        throwable.message ?: "",
                        ctaText = getString(com.tokopedia.design.R.string.oke)
                )
                logException(throwable)
            }
        }
    }

    private fun observeUpdateCart() {
        viewModel.updateCartLiveData.observe(viewLifecycleOwner) {
            it.doSuccessOrFail({ success ->
                view?.showToasterSuccess(success.data, ctaText = getString(R.string.label_oke_pdp))
            }) { throwable ->
                view?.showToasterError(
                        throwable.message ?: "",
                        ctaText = getString(com.tokopedia.design.R.string.oke)
                )
                logException(throwable)
            }
        }
    }

    private fun observeShippingAddressChanged() {
        activity?.let { activity ->
            sharedViewModel?.isAddressChanged?.observe(activity, {
                if (it) {
                    onSuccessUpdateAddress()
                }
            })
        }
    }

    private fun onSuccessUpdateAddress() {
        view?.showToasterSuccess(getString(R.string.pdp_shipping_success_change_address))
        onSwipeRefresh()
    }

    private fun observeTopAdsImageData() {
        viewLifecycleOwner.observe(viewModel.topAdsImageView) { data ->
            data.doSuccessOrFail({
                if (!it.data.isNullOrEmpty()) {
                    pdpUiUpdater?.updateTopAdsImageData(it.data)
                    updateUi()
                } else {
                    pdpUiUpdater?.removeComponent(ProductDetailConstant.KEY_TOP_ADS)
                }
            }, {
                pdpUiUpdater?.removeComponent(ProductDetailConstant.KEY_TOP_ADS)
                logException(it)
            })
        }
    }

    private fun observeTopAdsIsChargeData() {
        viewLifecycleOwner.observe(viewModel.topAdsRecomChargeData) { data ->
            data.doSuccessOrFail({ topAdsData ->
                if (!isTopadsDynamicsSlottingAlreadyCharged) {
                    context?.let {
                        TopAdsUrlHitter(it).hitImpressionUrl(
                                this::class.java.name,
                                topAdsData.data.product.image.m_url,
                                topAdsData.data.product.id,
                                topAdsData.data.product.name,
                                topAdsData.data.product.image.m_ecs)

                        TopAdsUrlHitter(it).hitClickUrl(
                                this::class.java.name,
                                topAdsData.data.clickUrl,
                                topAdsData.data.product.id,
                                topAdsData.data.product.name,
                                topAdsData.data.product.image.m_ecs)
                        isTopadsDynamicsSlottingAlreadyCharged = true
                    }
                }
            },
                    {
                        logException(it)
                    })
        }
    }

    private fun observeP2Other() {
        viewLifecycleOwner.observe(viewModel.p2Other) {
            pdpUiUpdater?.updateDataP2General(it)
            updateUi()
            (activity as? ProductDetailActivity)?.stopMonitoringP2Other()
        }
    }

    private fun observeDiscussionData() {
        viewLifecycleOwner.observe(viewModel.discussionMostHelpful) { data ->
            data.doSuccessOrFail({
                pdpUiUpdater?.updateDiscussionData(it.data.discussionMostHelpful)
                updateUi()
            }, {
                logException(it)
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
            }
            updateUi()
        }
    }

    private fun observeonVariantClickedData() {
        viewLifecycleOwner.observe(viewModel.onVariantClickedData) {
            updateVariantDataAndUi(it)
        }
    }

    private fun observeATCTokonowData() {
        viewLifecycleOwner.observe(viewModel.atcRecomTokonow) { data ->
            data.doSuccessOrFail({
                if (it.data.isNotEmpty()) {
                    view?.showToasterSuccess(it.data)
                }
            }, {
                view?.showToasterError(it.message
                        ?: "", ctaText = getString(R.string.label_oke_pdp))
                logException(it)
            })
        }
    }

    private fun observeATCRecomSendTracker() {
        viewLifecycleOwner.observe(viewModel.atcRecomTokonowSendTracker) { data ->
            data.doSuccessOrFail({
                DynamicProductDetailTracking.Click.eventClickRecomAddToCart(it.data, viewModel.userId, it.data.minOrder)
            }, {})
        }
    }

    private fun observeATCTokonowResetCard() {
        viewLifecycleOwner.observe(viewModel.atcRecomTokonowResetCard) {
            pdpUiUpdater?.resetFailedRecomTokonowCard(it)
            updateUi()
        }
    }

    private fun observeATCRecomTokonowNonLogin() {
        viewLifecycleOwner.observe(viewModel.atcRecomTokonowNonLogin) {
            goToLogin()
            pdpUiUpdater?.resetFailedRecomTokonowCard(it)
        }
    }

    private fun enableVideo(): Boolean {
        return remoteConfig()?.getBoolean(ProductDetailConstant.ENABLE_VIDEO_PDP, true) ?: false
    }

    private fun updateVariantDataAndUi(variantProcessedData: List<VariantCategory>?,
                                       doSomethingAfterVariantUpdated: (() -> Unit)? = null) {
        val selectedOptionIds = if (pdpUiUpdater?.productSingleVariant != null) pdpUiUpdater?.productSingleVariant?.mapOfSelectedVariant?.values?.toList()
                ?: listOf()
        else pdpUiUpdater?.productNewVariantDataModel?.mapOfSelectedVariant?.values?.toList()
                ?: listOf()

        val selectedChildAndPosition = VariantCommonMapper.selectedProductData(
                viewModel.variantData
                        ?: ProductVariant(), selectedOptionIds)
        val selectedChild = selectedChildAndPosition?.second
        val updatedDynamicProductInfo = VariantMapper.updateDynamicProductInfo(viewModel.getDynamicProductInfoP1, selectedChild, viewModel.listOfParentMedia)

        viewModel.updateDynamicProductInfoData(updatedDynamicProductInfo)
        productId = updatedDynamicProductInfo?.basic?.productID
        val boeData = viewModel.getBebasOngkirDataByProductId()

        pdpUiUpdater?.updateVariantData(variantProcessedData)
        pdpUiUpdater?.updateDataP1(context, updatedDynamicProductInfo, enableVideo())
        pdpUiUpdater?.updateNotifyMeAndContent(selectedChild?.productId.toString(), viewModel.p2Data.value?.upcomingCampaigns, boeData.imageURL)
        pdpUiUpdater?.updateFulfillmentData(context, viewModel.getMultiOriginByProductId().isFulfillment)
        val isUpcomingType = viewModel.p2Data.value?.upcomingCampaigns?.get(selectedChild?.productId.toString())?.isUpcomingNplType()
                ?: false
        pdpUiUpdater?.updateTickerData(viewModel.getDynamicProductInfoP1?.basic?.isWarehouse()
                ?: false, viewModel.getDynamicProductInfoP1?.data?.campaign?.isActive ?: false,
                viewModel.getDynamicProductInfoP1?.getFinalStock()?.toIntOrNull() == 0,
                isUpcomingType, viewModel.getDynamicProductInfoP1?.isProductParent ?: false)

        pdpUiUpdater?.updateShipmentData(
                viewModel.getP2RatesEstimateByProductId(),
                viewModel.getMultiOriginByProductId().isFulfillment,
                viewModel.getDynamicProductInfoP1?.data?.isCod ?: false,
                boeData,
                viewModel.getUserLocationCache()
        )

        renderRestrictionBottomSheet(viewModel.p2Data.value?.restrictionInfo
                ?: RestrictionInfoResponse())

        /*
            If the p2 data is empty, dont update the button
            this condition will be reproduceable when variant auto select is faster then p2 data from network
            if this happen, the update button will be run in onSuccessGetP2Data
         */
        if (viewModel.p2Data.value != null || viewModel.p2Data.value == null) {
            updateButtonState()
        }

        if (pdpUiUpdater?.productNewVariantDataModel?.isPartialySelected() == false && shouldFireVariantTracker) {
            shouldFireVariantTracker = false
            DynamicProductDetailTracking.Click.onVariantLevel1Clicked(
                    viewModel.getDynamicProductInfoP1,
                    pdpUiUpdater?.productNewVariantDataModel,
                    viewModel.variantData,
                    getComponentPositionBeforeUpdate(pdpUiUpdater?.productNewVariantDataModel))
        }
        updateUi()
        doSomethingAfterVariantUpdated?.invoke()
    }

    private fun updateButtonState() {
        viewModel.getDynamicProductInfoP1?.let {
            val cartTypeData = viewModel.getCartTypeByProductId()
            val selectedMiniCartItem = if (it.basic.isTokoNow && cartTypeData?.availableButtons?.firstOrNull()?.isCartTypeDisabledOrRemindMe() == false) {
                viewModel.getMiniCartItem()
            } else {
                null
            }

            val totalStockAtcVariant = viewModel.p2Data.value?.getTotalStockMiniCartByParentId(it.data.variant.parentID)

            val shouldShowTokoNow = if (it.basic.isTokoNow &&
                    cartTypeData?.availableButtons?.firstOrNull()?.isCartTypeDisabledOrRemindMe() == false &&
                    (totalStockAtcVariant != 0 || selectedMiniCartItem != null)) {
                true
            } else {
                false
            }

            val tokonowVariantButtonData = if (shouldShowTokoNow) {
                TokoNowButtonData(
                        totalStockAtcVariant = totalStockAtcVariant ?: 0,
                        productTitle = viewModel.getDynamicProductInfoP1?.data?.name ?: "",
                        isVariant = it.data.variant.isVariant,
                        minQuantity = it.basic.minOrder,
                        maxQuantity = it.basic.maxOrder,
                        selectedMiniCart = selectedMiniCartItem
                )
            } else {
                null
            }

            actionButtonView.renderData(
                    isWarehouseProduct = !it.isProductActive(),
                    hasShopAuthority = viewModel.hasShopAuthority(),
                    isShopOwner = viewModel.isShopOwner(),
                    hasTopAdsActive = hasTopAds(),
                    cartTypeData = cartTypeData,
                    tokonowButtonData = tokonowVariantButtonData)
        }
        showOrHideButton()
    }

    private fun observeSingleVariantData() {
        viewLifecycleOwner.observe(viewModel.singleVariantData) {
            val listOfVariantLevelOne = listOf(it)
            pdpUiUpdater?.updateVariantData(listOfVariantLevelOne)
        }
    }

    private fun observeInitialVariantData() {
        viewLifecycleOwner.observe(viewModel.initialVariantData) {
            pdpUiUpdater?.updateVariantData(it)
        }
    }

    private fun observeAddToCart() {
        viewLifecycleOwner.observe(viewModel.addToCartLiveData) { data ->
            hideProgressDialog()
            data.doSuccessOrFail({
                if (it.data.errorReporter.eligible) {
                    view?.showToasterError(it.data.errorReporter.texts.submitTitle, ctaText = getString(R.string.label_oke_pdp))
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
                firstOpenPage = false
                pdpUiUpdater = PdpUiUpdater(DynamicProductDetailMapper.hashMapLayout(it.data))
                onSuccessGetDataP1(it.data)
            }, {
                ServerLogger.log(Priority.P2, "LOAD_PAGE_FAILED",
                        mapOf("type" to "pdp",
                                "desc" to it.message.orEmpty(),
                                "err" to Log.getStackTraceString(it).take(ProductDetailConstant.LOG_MAX_LENGTH).trim()
                        ))
                logException(it)
                context?.let { ctx ->
                    renderPageError(ProductDetailErrorHelper.getErrorType(ctx, it, isFromDeeplink, deeplinkUrl))
                }
            })
            (activity as? ProductDetailActivity)?.stopMonitoringPltRenderPage(viewModel.getDynamicProductInfoP1?.isProductVariant()
                    ?: false)
            (activity as? ProductDetailActivity)?.stopMonitoringP1()
        }
    }

    private fun observeP2Login() {
        viewLifecycleOwner.observe(viewModel.p2Login) {
            topAdsGetProductManage = it.topAdsGetProductManage
            if (it.pdpAffiliate == null) {
                pdpUiUpdater?.removeComponent(ProductDetailCommonConstant.KEY_BYME)
            } else {
                pdpUiUpdater?.updateByMeData(context)
            }

            pdpUiUpdater?.updateShopFollow(it.isFollow)
            pdpUiUpdater?.updateWishlistData(it.isWishlisted)

            it.pdpAffiliate?.let { renderAffiliate(it) }
            actionButtonView.setTopAdsButton(hasTopAds())

            updateUi()
            openBottomSheetTopAds()
            (activity as? ProductDetailActivity)?.stopMonitoringP2Login()
        }
    }

    private fun observeP2Data() {
        viewLifecycleOwner.observe(viewModel.p2Data) {
            val boeData = viewModel.getBebasOngkirDataByProductId()
            val ratesData = viewModel.getP2RatesEstimateByProductId()

            trackProductView(viewModel.tradeInParams.isEligible == ProductDetailConstant.ELIGIBLE_TRADE_IN, boeData.boType)
            viewModel.getDynamicProductInfoP1?.let { p1 ->
                DynamicProductDetailTracking.Moengage.sendMoEngageOpenProduct(p1)
                DynamicProductDetailTracking.Moengage.eventAppsFylerOpenProduct(p1)

                DynamicProductDetailTracking.sendScreen(
                        irisSessionId,
                        p1.basic.shopID,
                        p1.shopTypeString,
                        p1.basic.productID)
            }

            onSuccessGetDataP2(it, boeData, ratesData)
            (activity as? ProductDetailActivity)?.stopMonitoringP2Data()
        }
    }

    override fun showCoachmark(view: Typography?, isBoeType: Boolean) {
        context?.let {
            if (view != null) {
                val showLocalization = ChooseAddressUtils.isLocalizingAddressNeedShowCoachMark(it)
                        ?: false
                val showBoe = if (!isBoeType) false else coachMarkBoePref.shouldShowBoeCoachmark()

                if (showLocalization || showBoe) {
                    val coachMarkList = arrayListOf<CoachMark2Item>()
                    if (showBoe) {
                        coachMarkList.add(CoachMark2Item(view,
                                it.getString(R.string.pdp_boe_coachmark_title),
                                it.getString(R.string.pdp_boe_coachmark_desc)
                        ).also {
                            it.position = 1
                        })
                    }

                    if (showLocalization) {
                        coachMarkList.add(ChooseAddressUtils.coachMark2Item(it, view).also {
                            it.position = 1
                        })
                        ChooseAddressUtils.coachMarkLocalizingAddressAlreadyShown(it)
                    }

                    if (coachMarkList.isNotEmpty()) {
                        val coachmark = CoachMark2(it)
                        coachmark.showCoachMark(coachMarkList, null, 0)
                    }
                }
            }
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
            setLoadingNplShopFollowers(false)
            data.doSuccessOrFail({
                onSuccessFavoriteShop(it.data.first, it.data.second)
                setupShopFavoriteToaster(it.data.second)
            }, {
                onFailFavoriteShop(it)
                logException(it)
            })
        }
    }

    private fun setupShopFavoriteToaster(isNplFollowerType: Boolean) {
        val isFavorite = pdpUiUpdater?.shopCredibility?.isFavorite ?: return
        val message = if (isFavorite) getString(com.tokopedia.product.detail.common.R.string.merchant_product_detail_success_follow_shop) else getString(com.tokopedia.product.detail.common.R.string.merchant_product_detail_success_unfollow_shop)

        view?.showToasterSuccess(if (isNplFollowerType) getString(com.tokopedia.product.detail.common.R.string.merchant_product_detail_success_follow_shop_npl) else message)
    }

    private fun observeRecommendationProduct() {
        viewLifecycleOwner.observe(viewModel.loadTopAdsProduct) { data ->
            data.doSuccessOrFail({
                if (it.data.recommendationItemList.isNotEmpty()) {
                    val enableComparisonWidget = remoteConfig()?.getBoolean(
                            RemoteConfigKey.RECOMMENDATION_ENABLE_COMPARISON_WIDGET, true) ?: true
                    if (enableComparisonWidget) {
                        if (it.data.layoutType == RecommendationTypeConst.TYPE_COMPARISON_WIDGET) {
                            pdpUiUpdater?.updateComparisonDataModel(it.data)
                            updateUi()
                        } else {
                            pdpUiUpdater?.updateRecommendationData(it.data)
                            updateUi()
                        }
                    } else {
                        pdpUiUpdater?.updateRecommendationData(it.data)
                        updateUi()
                    }
                } else {
                    //recomUiPageName used because there is possibilites gql recom return empty pagename
                    pdpUiUpdater?.removeComponent(it.data.recomUiPageName)
                    updateUi()
                }
            }, {
                pdpUiUpdater?.removeComponent(it.message ?: "")
                updateUi()
                logException(it)
            })
        }

        viewLifecycleOwner.observe(viewModel.statusFilterTopAdsProduct) {
            if (it is Fail) {
                view?.showToasterError(context?.getString(R.string.recom_filter_chip_click_error_network)
                        ?: "", ctaText = getString(R.string.label_oke_pdp))
            }
        }

        viewLifecycleOwner.observe(viewModel.filterTopAdsProduct) { data ->
            pdpUiUpdater?.updateFilterRecommendationData(data)
            updateUi()
        }
    }

    private fun onSuccessAtcTokoNow(result: AddToCartDataModel) {
        view?.showToasterSuccess(result.data.message.firstOrNull()
                ?: "", ctaText = getString(R.string.label_oke_pdp))
        sendTrackingATC(result.data.cartId)
        updateButtonState()
    }

    private fun onSuccessAtc(result: AddToCartDataModel) {
        val cartId = result.data.cartId
        if (viewModel.getDynamicProductInfoP1?.basic?.isTokoNow == true) {
            onSuccessAtcTokoNow(result)
            return
        }

        when (buttonActionType) {
            ProductDetailCommonConstant.OCS_BUTTON -> {
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
            ProductDetailCommonConstant.OCC_BUTTON -> {
                sendTrackingATC(cartId)
                goToOneClickCheckout()
            }
            ProductDetailCommonConstant.BUY_BUTTON -> {
                sendTrackingATC(cartId)
                goToCartCheckout(cartId)
            }
            ProductDetailCommonConstant.ATC_BUTTON -> {
                sendTrackingATC(cartId)
                showAddToCartDoneBottomSheet(result.data.cartId)
            }
            ProductDetailCommonConstant.TRADEIN_AFTER_DIAGNOSE -> {
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
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT)
        startActivityForResult(intent, ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT)
    }

    private fun sendTrackingATC(cartId: String) {
        val boData = viewModel.getBebasOngkirDataByProductId()
        DynamicProductDetailTracking.Click.eventEcommerceBuy(
                actionButton = buttonActionType,
                buttonText = viewModel.buttonActionText,
                userId = viewModel.userId,
                cartId = cartId,
                trackerAttribution = trackerAttributionPdp ?: "",
                multiOrigin = viewModel.getMultiOriginByProductId().isFulfillment,
                variantString = DynamicProductDetailTracking.generateVariantString(viewModel.variantData, viewModel.getDynamicProductInfoP1?.basic?.productID
                        ?: ""),
                productInfo = viewModel.getDynamicProductInfoP1,
                boType = boData.boType)
    }

    private fun validateOvo(result: AddToCartDataModel) {
        if (result.data.refreshPrerequisitePage) {
            onSwipeRefresh()
        } else {
            activity?.let {
                when (result.data.ovoValidationDataModel.status) {
                    ProductDetailCommonConstant.OVO_INACTIVE_STATUS -> {
                        val applink = "${result.data.ovoValidationDataModel.applink}&product_id=${
                            viewModel.getDynamicProductInfoP1?.parentProductId
                                    ?: ""
                        }"
                        DynamicProductDetailTracking.Click.eventActivationOvo(
                                viewModel.getDynamicProductInfoP1?.parentProductId ?: "",
                                viewModel.userSessionInterface.userId)
                        RouteManager.route(it, applink)
                    }
                    ProductDetailCommonConstant.OVO_INSUFFICIENT_BALANCE_STATUS -> {
                        val bottomSheetOvoDeals = OvoFlashDealsBottomSheet(
                                viewModel.getDynamicProductInfoP1?.parentProductId ?: "",
                                viewModel.userSessionInterface.userId,
                                result.data.ovoValidationDataModel)
                        bottomSheetOvoDeals.show(it.supportFragmentManager, "Ovo Deals")
                    }
                    else -> view?.showToasterError(getString(com.tokopedia.abstraction.R.string.default_request_error_unknown), ctaText = getString(R.string.label_oke_pdp))
                }
            }
        }
    }

    private fun goToCheckout(shipmentFormRequest: Bundle) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.CHECKOUT)
        intent.putExtra(CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT, true)
        intent.putExtras(shipmentFormRequest)
        startActivityForResult(intent, ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT)
    }

    private fun goToCartCheckout(cartId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConst.CART)
        intent?.run {
            putExtra(ApplinkConst.Transaction.EXTRA_CART_ID, cartId)
            startActivityForResult(intent, ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT)
        }
    }

    private fun updateUi() {
        val newData = pdpUiUpdater?.mapOfData?.values?.toList()
        submitList(newData ?: listOf())
    }

    private fun onSuccessGetDataP1(data: List<DynamicPdpDataModel>) {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            updateProductId()
            renderVariant(viewModel.variantData, pdpUiUpdater?.productSingleVariant != null)
            val hint = if (viewModel.getDynamicProductInfoP1?.basic?.isTokoNow == true) {
                String.format(getString(R.string.pdp_search_hint_tokonow), productInfo.basic.category.name)
            } else {
                String.format(getString(R.string.pdp_search_hint), productInfo.basic.category.name)
            }
            navAbTestCondition({ setNavToolbarSearchHint(hint) }, { et_search.setHint(hint) })
            pdpUiUpdater?.updateDataP1(context, productInfo, enableVideo(), true)
            actionButtonView.setButtonP1(productInfo.data.preOrder)

            if (productInfo.basic.category.isAdult) {
                AdultManager.showAdultPopUp(this, AdultManager.ORIGIN_PDP, productInfo.basic.productID)
            }

            if (affiliateString.hasValue()) {
                viewModel.hitAffiliateTracker(affiliateString ?: "", viewModel.deviceId)
            }

            setupProductVideoCoordinator()

            activity?.invalidateOptionsMenu()
            submitInitialList(pdpUiUpdater?.mapOfData?.values?.toList() ?: listOf())
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
            val shouldShowSellerButtonByShopType = shopStatus != ShopStatusDef.DELETED && shopStatus != ShopStatusDef.MODERATED_PERMANENTLY
            if (viewModel.isShopOwner()) {
                actionButtonView.visibility = shouldShowSellerButtonByShopType
            } else {
                actionButtonView.visibility = !isAffiliate && viewModel.getShopInfo().statusInfo.shopStatus == ShopStatusDef.OPEN
            }
            return
        }
        actionButtonView.visibility = !isAffiliate
    }

    private fun renderRestrictionBottomSheet(data: RestrictionInfoResponse) {
        val reData = data.getReByProductId(viewModel.getDynamicProductInfoP1?.basic?.productID
                ?: "")
        if (reData == null) {
            nplFollowersButton?.setupVisibility = false
            return
        }

        when {
            reData.restrictionExclusiveType() -> {
                renderExclusiveBottomSheet(reData)
            }
            reData.restrictionShopFollowersType() -> {
                updateNplButtonFollowers(reData)
            }
            else -> {
                nplFollowersButton?.setupVisibility = false
            }
        }
    }

    private fun onSuccessGetDataP2(it: ProductInfoP2UiData, boeData: BebasOngkirImage, ratesData: P2RatesEstimateData?) {
        val minimumShippingPriceP2 = ratesData?.cheapestShippingPrice ?: 0.0
        if (minimumShippingPriceP2 != 0.0) {
            viewModel.shippingMinimumPrice = minimumShippingPriceP2
        }

        renderRestrictionBottomSheet(it.restrictionInfo)
        updateButtonState()

        if (it.helpfulReviews?.isEmpty() == true && viewModel.getDynamicProductInfoP1?.basic?.stats?.countReview.toIntOrZero() == 0) {
            pdpUiUpdater?.removeComponent(ProductDetailConstant.REVIEW)
        }

        pdpUiUpdater?.updateShipmentData(
                ratesData,
                viewModel.getMultiOriginByProductId().isFulfillment,
                viewModel.getDynamicProductInfoP1?.data?.isCod ?: false,
                boeData,
                viewModel.getUserLocationCache()
        )

        if (it.upcomingCampaigns.values.isEmpty()) {
            pdpUiUpdater?.removeComponent(ProductDetailConstant.NOTIFY_ME)
        }

        if (!it.shopCommitment.isNowActive) {
            pdpUiUpdater?.removeComponent(ProductDetailConstant.ORDER_PRIORITY)
        }

        if (it.productPurchaseProtectionInfo.ppItemDetailPage.isProtectionAvailable) {
            DynamicProductDetailTracking.Impression.eventPurchaseProtectionAvailable(viewModel.userId,
                    viewModel.getDynamicProductInfoP1, getPPTitleName())
        } else {
            pdpUiUpdater?.removeComponent(ProductDetailConstant.PRODUCT_PROTECTION)
        }

        if (!it.validateTradeIn.isEligible) {
            pdpUiUpdater?.removeComponent(ProductDetailConstant.TRADE_IN)
        }

        if (!it.merchantVoucherSummary.isShown) {
            pdpUiUpdater?.removeComponent(ProductDetailConstant.MVC)
        }

        viewModel.getDynamicProductInfoP1?.run {
            DynamicProductDetailTracking.Branch.eventBranchItemView(this, viewModel.userId)
        }

        pdpUiUpdater?.updateFulfillmentData(context, viewModel.getMultiOriginByProductId().isFulfillment)
        pdpUiUpdater?.updateDataP2(
                context = context,
                p2Data = it,
                productId = viewModel.getDynamicProductInfoP1?.basic?.productID ?: "",
                isProductWarehouse = viewModel.getDynamicProductInfoP1?.basic?.isWarehouse()
                        ?: false,
                isProductInCampaign = viewModel.getDynamicProductInfoP1?.data?.campaign?.isActive
                        ?: false,
                isOutOfStock = viewModel.getDynamicProductInfoP1?.getFinalStock()?.toIntOrNull() == 0,
                boeImageUrl = boeData.imageURL,
                isProductParent = viewModel.getDynamicProductInfoP1?.isProductParent ?: false)

        updateUi()
    }

    private fun updateNplButtonFollowers(reData: RestrictionData?) {
        val alreadyFollowShop = reData?.isEligible ?: true
        val shouldShowRe = !alreadyFollowShop && pdpUiUpdater?.shopCredibility?.isFavorite == false //show when user not follow the shop
        if (shouldShowRe && !viewModel.isShopOwner() && reData?.restrictionShopFollowersType() == true) {
            if (!base_btn_follow.isShown) {
                base_btn_follow.translationY = 100.toPx().toFloat()
            }

            val title = reData.action.firstOrNull()?.title ?: ""
            val desc = reData.action.firstOrNull()?.description ?: ""
            nplFollowersButton?.renderView(title, desc, alreadyFollowShop)
        }
        setupNplVisibility(shouldShowRe)
    }

    override fun onButtonFollowNplClick() {
        DynamicProductDetailTracking.Click.eventClickFollowNpl(viewModel.getDynamicProductInfoP1, viewModel.userId)
        onShopFavoriteClick(isNplFollowType = true)
    }

    private fun onSuccessGetDataP3(it: ProductInfoP3) {
        stickyLoginView?.loadContent()
        pdpUiUpdater?.updateDataP3(it)
        updateUi()
    }

    private fun logException(t: Throwable) {
        if (!BuildConfig.DEBUG) {
            val errorMessage = String.format(
                    getString(R.string.on_error_p1_string_builder),
                    viewModel.userSessionInterface.userId,
                    viewModel.userSessionInterface.email,
                    t.message
            )
            FirebaseCrashlytics.getInstance().recordException(Exception(errorMessage, t))
        } else {
            t.printStackTrace()
        }
    }

    private fun showSnackbarClose(string: String) {
        view?.let {
            Snackbar.make(it, string, Snackbar.LENGTH_LONG).apply {
                setAction(getString(com.tokopedia.abstraction.R.string.close)) { dismiss() }
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
                    getComponentPosition(pdpUiUpdater?.productNewVariantDataModel))
            startActivity(getIntentImagePreviewWithoutDownloadButton(it, arrayListOf(url)))
        }
    }

    override fun getStockWording(): String {
        val variantStockWording = viewModel.getDynamicProductInfoP1?.data?.stock?.stockWording ?: ""
        val isPartialySelected = pdpUiUpdater?.productNewVariantDataModel?.isPartialySelected()
                ?: false

        return if (isPartialySelected) "" else variantStockWording
    }

    override fun onVariantClicked(variantOptions: VariantOptionWithAttribute, state: Int) {
        if (pdpUiUpdater?.productSingleVariant != null) {
            goToAtcVariant()
        } else {
            selectVariantInPdp(variantOptions, state)
        }
    }

    private fun selectVariantInPdp(variantOptions: VariantOptionWithAttribute, state: Int) {
        if (state == VariantConstant.STATE_SELECTED || state == VariantConstant.STATE_SELECTED_EMPTY) return
        pdpUiUpdater?.updateVariantSelected(variantOptions.variantId, variantOptions.variantCategoryKey)
        val isPartialySelected = pdpUiUpdater?.productNewVariantDataModel?.isPartialySelected()
                ?: false

        viewModel.onVariantClicked(viewModel.variantData, pdpUiUpdater?.productNewVariantDataModel?.mapOfSelectedVariant, isPartialySelected, variantOptions.level,
                variantOptions.imageOriginal)
    }

    private fun goToAtcVariant(customCartRedirection: Map<String, CartTypeData>? = null) {
        SingleClick.doSomethingBeforeTime {
            context?.let {
                if (viewModel.getDynamicProductInfoP1 != null) {
                    DynamicProductDetailTracking.Click.onSingleVariantClicked(
                            viewModel.getDynamicProductInfoP1,
                            pdpUiUpdater?.productSingleVariant,
                            viewModel.variantData,
                            getComponentPositionBeforeUpdate(pdpUiUpdater?.productSingleVariant))

                    val p2Data = viewModel.p2Data.value
                    val cartTypeData = if (customCartRedirection != null) customCartRedirection else
                        p2Data?.cartRedirection

                    viewModel.clearCacheP2Data()

                    AtcVariantHelper.pdpToAtcVariant(
                            context = it,
                            pageSource = AtcVariantHelper.PDP_PAGESOURCE,
                            productInfoP1 = viewModel.getDynamicProductInfoP1!!,
                            warehouseId = warehouseId ?: "",
                            pdpSession = viewModel.getDynamicProductInfoP1?.pdpSession ?: "",
                            isTokoNow = viewModel.getDynamicProductInfoP1?.basic?.isTokoNow
                                    ?: false,
                            isShopOwner = viewModel.isShopOwner(),
                            productVariant = viewModel.variantData ?: ProductVariant(),
                            warehouseResponse = p2Data?.nearestWarehouseInfo ?: mapOf(),
                            cartRedirection = cartTypeData ?: mapOf(),
                            miniCart = p2Data?.miniCart,
                            alternateCopy = p2Data?.alternateCopy,
                            boData = p2Data?.bebasOngkir ?: BebasOngkir(),
                            rates = p2Data?.ratesEstimate ?: listOf(),
                            restrictionData = p2Data?.restrictionInfo,
                            isFavorite = pdpUiUpdater?.shopCredibility?.isFavorite ?: false,
                            uspImageUrl = p2Data?.uspImageUrl ?: ""
                    ) { data, code ->
                        startActivityForResult(data, code)
                    }
                }
            }
        }
    }

    private fun renderVariant(data: ProductVariant?, shouldRenderNewVariant: Boolean) {
        if (data == null || !data.hasChildren) {
            pdpUiUpdater?.removeComponent(ProductDetailConstant.VARIANT_OPTIONS)
            pdpUiUpdater?.removeComponent(ProductDetailConstant.MINI_VARIANT_OPTIONS)
        } else {
            if (shouldRenderNewVariant) {
                pdpUiUpdater?.removeComponent(ProductDetailConstant.VARIANT_OPTIONS)
            } else {
                pdpUiUpdater?.removeComponent(ProductDetailConstant.MINI_VARIANT_OPTIONS)
            }

            if (data.errorCode > 0) {
                pdpUiUpdater?.updateVariantError()
                updateUi()
            } else {
                val selectedOptionIds = autoSelectVariant(productId)
                viewModel.processVariant(data, selectedOptionIds, shouldRenderNewVariant)
            }
        }
    }

    private fun autoSelectVariant(productId: String?): MutableMap<String, String> {
        viewModel.variantData?.let {
            //Auto select variant will be execute when there is only 1 child left
            val selectedChild = it.children.firstOrNull { it.productId == productId ?: "" }
            val mapOfSelectedVariant = DynamicProductDetailMapper.determineSelectedOptionIds(it, selectedChild)

            pdpUiUpdater?.productNewVariantDataModel?.mapOfSelectedVariant = mapOfSelectedVariant
            pdpUiUpdater?.productSingleVariant?.mapOfSelectedVariant = mapOfSelectedVariant
        }
        return pdpUiUpdater?.productNewVariantDataModel?.mapOfSelectedVariant
                ?: pdpUiUpdater?.productSingleVariant?.mapOfSelectedVariant ?: mutableMapOf()
    }

    private fun showAddToCartDoneBottomSheet(cartId: String) {
        viewModel.getDynamicProductInfoP1?.let {
            val addToCartDoneBottomSheet = AddToCartDoneBottomSheet()
            val productName = it.getProductName
            val productImageUrl = it.data.getFirstProductImage()
            val addedProductDataModel = AddToCartDoneAddedProductDataModel(
                    it.basic.productID,
                    productName,
                    productImageUrl,
                    it.data.variant.isVariant,
                    it.basic.getShopId(),
                    viewModel.getBebasOngkirDataByProductId().imageURL,
                    cartId = if (viewModel.getDynamicProductInfoP1?.basic?.isTokoNow == true) "" else cartId
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

    override fun openShipmentClickedBottomSheet(title: String, labelShipping: String, isCod: Boolean,
                                                componentTrackDataModel: ComponentTrackDataModel?) {
        viewModel.getDynamicProductInfoP1?.let {
            DynamicProductDetailTracking.Click.eventClickShipment(viewModel.getDynamicProductInfoP1, viewModel.userId, componentTrackDataModel, title, labelShipping, isCod)
            val boData = viewModel.getBebasOngkirDataByProductId()
            sharedViewModel?.setRequestData(RatesEstimateRequest(
                    productWeight = it.basic.weight.toFloat(),
                    shopDomain = viewModel.getShopInfo().shopCore.domain,
                    origin = viewModel.getMultiOriginByProductId().getOrigin(),
                    shopId = it.basic.shopID,
                    productId = it.basic.productID,
                    productWeightUnit = it.basic.weightUnit,
                    isFulfillment = viewModel.getMultiOriginByProductId().isFulfillment,
                    destination = generateUserLocationRequestRates(viewModel.getUserLocationCache()),
                    boType = boData.boType,
                    freeOngkirUrl = boData.imageURL,
                    poTime = it.data.preOrder.preorderInDays,
                    uspImageUrl = viewModel.p2Data.value?.uspImageUrl ?: "",
                    userId = viewModel.userId,
                    forceRefresh = shouldRefreshShippingBottomSheet,
                    shopTier = viewModel.getShopInfo().shopTier,
                    isTokoNow = it.basic.isTokoNow
            ))
            shouldRefreshShippingBottomSheet = false
            val shippingBs = ProductDetailShippingBottomSheet()
            shippingBs.show(getProductFragmentManager())
        }
    }

    override fun clickShippingComponentError(errorCode: Int, title: String, componentTrackDataModel: ComponentTrackDataModel?) {
        DynamicProductDetailTracking.Click.eventClickShipmentErrorComponent(viewModel.getDynamicProductInfoP1, viewModel.userId, title, componentTrackDataModel)
        goToShipmentErrorAddressOrChat(errorCode)
    }

    private fun openShipmentBottomSheetWhenError(): Boolean {
        context?.let {
            val rates = viewModel.getP2RatesEstimateByProductId()
            val bottomSheetData = viewModel.getP2RatesBottomSheetData()

            if (rates?.p2RatesError?.isEmpty() == true || rates?.p2RatesError?.firstOrNull()?.errorCode == 0 || bottomSheetData == null) return false

            DynamicProductDetailTracking.BottomSheetErrorShipment.impressShipmentErrorBottomSheet(viewModel.getDynamicProductInfoP1, viewModel.userId, bottomSheetData.title)
            ProductDetailCommonBottomSheetBuilder.getShippingErrorBottomSheet(
                    it,
                    bottomSheetData,
                    rates?.p2RatesError?.firstOrNull()?.errorCode ?: 0,
                    onButtonClicked = { errorCode ->
                        DynamicProductDetailTracking.BottomSheetErrorShipment.eventClickButtonShipmentErrorBottomSheet(viewModel.getDynamicProductInfoP1, viewModel.userId, bottomSheetData.title, errorCode)
                        goToShipmentErrorAddressOrChat(errorCode)
                    },
                    onHomeClicked = { goToHomePage() }
            ).show(childFragmentManager, ProductDetailConstant.BS_SHIPMENT_ERROR_TAG)
            return true
        } ?: return false
    }

    /**
     * Go To Home Page and Clear Back Stack
     */
    private fun goToHomePage() {
        val intent = RouteManager.getIntent(context, ApplinkConst.HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        activity?.finish()
    }

    private fun goToShipmentErrorAddressOrChat(errorCode: Int) {
        if (errorCode == ProductDetailCommonConstant.SHIPPING_ERROR_WEIGHT) {
            onShopChatClicked()
        } else {
            ProductDetailBottomSheetBuilder.openChooseAddressBottomSheet(object : ChooseAddressBottomSheet.ChooseAddressBottomSheetListener {
                override fun onLocalizingAddressServerDown() {
                }

                override fun onAddressDataChanged() {
                    onSuccessUpdateAddress()
                }

                override fun getLocalizingAddressHostSourceBottomSheet(): String = ProductDetailCommonConstant.KEY_PRODUCT_DETAIL

                override fun onLocalizingAddressLoginSuccessBottomSheet() {
                }

                override fun onDismissChooseAddressBottomSheet() {
                }

            }, childFragmentManager)
        }
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

    private fun onClickShareProduct() {
        viewModel.getDynamicProductInfoP1?.let { productInfo ->
            DynamicProductDetailTracking.Click.eventClickPdpShare(
                    productInfo.basic.productID, viewModel.userId
            )
            shareProduct(productInfo)
        }
    }

    private fun shareProduct(dynamicProductInfoP1: DynamicProductInfoP1? = null) {
        val productInfo = dynamicProductInfoP1 ?: viewModel.getDynamicProductInfoP1
        if (productInfo != null) {
            val productData = ProductData(
                    viewModel.userId,
                    productInfo.finalPrice.getCurrencyFormatted(),
                    "${productInfo.data.isCashback.percentage}%",
                    MethodChecker.fromHtml(productInfo.getProductName).toString(),
                    productInfo.data.price.currency,
                    productInfo.basic.url,
                    viewModel.getShopInfo().shopCore.url,
                    productInfo.basic.shopName,
                    productInfo.basic.productID,
                    productInfo.data.getProductImageUrl() ?: ""
            )
            checkAndExecuteReferralAction(productData)
        }
    }

    private fun checkAndExecuteReferralAction(productData: ProductData) {
        val fireBaseRemoteMsgGuest = remoteConfig()?.getString(RemoteConfigKey.fireBaseGuestShareMsgKey, "")
                ?: ""
        if (!TextUtils.isEmpty(fireBaseRemoteMsgGuest)) productData.productShareDescription = fireBaseRemoteMsgGuest

        if (viewModel.userSessionInterface.isLoggedIn && viewModel.userSessionInterface.isMsisdnVerified) {
            val fireBaseRemoteMsg = remoteConfig()?.getString(RemoteConfigKey.fireBaseShareMsgKey, "")
                    ?: ""
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
        val enablePdpCustomSharing = remoteConfig()?.getBoolean(
                REMOTE_CONFIG_KEY_ENABLE_PDP_CUSTOM_SHARING,
                REMOTE_CONFIG_DEFAULT_ENABLE_PDP_CUSTOM_SHARING
        ) ?: REMOTE_CONFIG_DEFAULT_ENABLE_PDP_CUSTOM_SHARING
        if (UniversalShareBottomSheet.isCustomSharingEnabled(context) && enablePdpCustomSharing) {
            val description = pdpUiUpdater?.productDetailInfoData?.getDescription()?.take(100)?.replace("(\r\n|\n)".toRegex(), " ")
                    ?: ""
            productData.productShareDescription = "$description..."
            executeUniversalShare(productData)
        } else {
            executeNativeShare(productData)
        }
    }

    private fun executeNativeShare(productData: ProductData) {
        shareProductInstance?.share(productData, {
            showProgressDialog {
                shareProductInstance?.cancelShare(true)
            }
        }, {
            hideProgressDialog()
        }, true)
    }

    private fun executeUniversalShare(productData: ProductData) {
        activity?.let {
            val imageUrls = pdpUiUpdater?.mediaMap?.listOfMedia
                    ?.filter { it.type == ProductMediaDataModel.IMAGE_TYPE }
                    ?.map { it.urlOriginal } ?: emptyList()

            shareProductInstance?.showUniversalShareBottomSheet(
                    fragmentManager = it.supportFragmentManager,
                    fragment = this,
                    data = productData,
                    isLog = true,
                    view = view,
                    productImgList = ArrayList(imageUrls),
                    preBuildImg = {
                        showProgressDialog {
                            shareProductInstance?.cancelShare(true)
                        }
                    },
                    postBuildImg = { hideProgressDialog() },
                    screenshotDetector
            )
        }
    }

    override fun reportProductFromComponent(componentTrackDataModel: ComponentTrackDataModel?) {
        reportProduct({
            DynamicProductDetailTracking.Click.eventClickReportFromComponent(viewModel.getDynamicProductInfoP1, viewModel.userId, componentTrackDataModel)
        }, {})
    }

    override fun onBuyerPhotosClicked(componentTrackDataModel: ComponentTrackDataModel?) {
        DynamicProductDetailTracking.Click.eventClickBuyerPhotosClicked(viewModel.getDynamicProductInfoP1, viewModel.userId, componentTrackDataModel
                ?: ComponentTrackDataModel())
        goToReviewImagePreview()
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

    private fun handlingMenuPreparation(menu: Menu?) {
        if (menu == null) return

        val menuShare = menu.findItem(R.id.action_share)
        val menuCart = menu.findItem(R.id.action_cart)
        val menuReport = menu.findItem(R.id.action_report)

        if (viewModel.getDynamicProductInfoP1 == null) {
            menuShare.isVisible = false
            menuShare.isEnabled = false
            menuCart.isVisible = true
            menuCart.isEnabled = true
            menuReport.isVisible = false
            menuReport.isEnabled = false
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

                menuReport.isVisible = !isOwned && (basic.status != ProductStatusTypeDef.WAREHOUSE && !viewModel.isShopOwner())
                menuReport.isEnabled = !isOwned && (basic.status != ProductStatusTypeDef.WAREHOUSE && !viewModel.isShopOwner())
            }
        }
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
        } else if (!GlobalConfig.isSellerApp()) {
            base_btn_affiliate_dynamic.gone()
        }
    }

    private fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?) {
        if (productCardOptionsModel == null) return

        val wishlistResult = productCardOptionsModel.wishlistResult
        if (wishlistResult.isUserLoggedIn) {
            if (wishlistResult.isSuccess) {
                recomWishlistItem?.isWishlist = !(recomWishlistItem?.isWishlist ?: false)
                recomWishlistItem?.let { DynamicProductDetailTracking.Click.eventAddToCartRecommendationWishlist(it, viewModel.userSessionInterface.isLoggedIn, wishlistResult.isAddWishlist) }
                view?.showToasterSuccess(
                        message = if (wishlistResult.isAddWishlist) getString(com.tokopedia.topads.sdk.R.string.msg_success_add_wishlist) else getString(com.tokopedia.topads.sdk.R.string.msg_success_remove_wishlist),
                        ctaText = getString(R.string.recom_go_to_wishlist),
                        ctaListener = {
                            goToWishlist()
                        }
                )
            } else {
                view?.showToasterError(
                        if (wishlistResult.isAddWishlist) getString(com.tokopedia.topads.sdk.R.string.msg_error_add_wishlist) else getString(com.tokopedia.topads.sdk.R.string.msg_error_remove_wishlist)
                )
            }
        } else {
            RouteManager.route(context, ApplinkConst.LOGIN)
        }

        recomWishlistItem = null
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

    private fun trackProductView(isElligible: Boolean, boType: Int) {
        DynamicProductDetailTracking.Impression.eventProductView(
                productInfo = viewModel.getDynamicProductInfoP1,
                shopInfo = viewModel.getShopInfo(),
                irisSessionId = irisSessionId,
                trackerListName = trackerListNamePdp,
                trackerAttribution = trackerAttributionPdp,
                isTradeIn = isElligible,
                isDiagnosed = viewModel.tradeInParams.usedPrice > 0,
                multiOrigin = viewModel.getMultiOriginByProductId().isFulfillment,
                deeplinkUrl = deeplinkUrl,
                isStockAvailable = viewModel.getDynamicProductInfoP1?.getFinalStock() ?: "0",
                boType = boType,
                affiliateUniqueId = if (affiliateUniqueId.isNotBlank()) "$affiliateUniqueId - $uuid" else ""
        )
    }

    private fun openFtInstallmentBottomSheet(installmentData: FtInstallmentCalculationDataResponse) {
        val pdpInstallmentBottomSheet = FtPDPInstallmentBottomSheet()

        val productInfo = viewModel.getDynamicProductInfoP1

        context?.let {
            val cacheManager = SaveInstanceCacheManager(it, true)
            cacheManager.put(FtInstallmentCalculationDataResponse::class.java.simpleName, installmentData, TimeUnit.HOURS.toMillis(1))
            val bundleData = Bundle()

            bundleData.putString(FtPDPInstallmentBottomSheet.KEY_PDP_FINANCING_DATA, cacheManager.id!!)
            bundleData.putDouble(FtPDPInstallmentBottomSheet.KEY_PDP_PRODUCT_PRICE, productInfo?.data?.price?.value
                    ?: 0.0)
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
        updateUi()
        sendIntentResultWishlistChange(productId ?: "", false)
        if (isProductOos()) {
            refreshPage()
        }
    }

    private fun onErrorRemoveWishList(errorMessage: String?) {
        view?.showToasterError(getErrorMessage(errorMessage),
                ctaText = getString(com.tokopedia.design.R.string.oke))
    }

    private fun onSuccessAddWishlist(productId: String?) {
        view?.showToasterSuccess(
                message = toasterWishlistText,
                ctaText = getString(com.tokopedia.wishlist.common.R.string.lihat_label),
                ctaListener = {
                    goToWishlist()
                }
        )
        pdpUiUpdater?.updateWishlistData(true)
        updateUi()
        DynamicProductDetailTracking.Branch.eventBranchAddToWishlist(viewModel.getDynamicProductInfoP1, viewModel.userId, pdpUiUpdater?.productDetailInfoData?.getDescription()
                ?: "")
        sendIntentResultWishlistChange(productId ?: "", true)
        if (isProductOos()) {
            refreshPage()
        }
    }

    private fun onErrorAddWishList(errorMessage: String?) {
        view?.showToasterError(getErrorMessage(errorMessage),
                ctaText = getString(com.tokopedia.design.R.string.oke))
    }

    private fun sendIntentResultWishlistChange(productId: String, isInWishlist: Boolean) {
        val resultIntent = Intent()
                .putExtra(ProductDetailConstant.WISHLIST_STATUS_UPDATED_POSITION, activity?.intent?.getIntExtra(ProductDetailConstant.WISHLIST_STATUS_UPDATED_POSITION, -1))
        resultIntent.putExtra(ProductDetailConstant.WIHSLIST_STATUS_IS_WISHLIST, isInWishlist)
        resultIntent.putExtra("product_id", productId)
        activity?.let { it.setResult(Activity.RESULT_CANCELED, resultIntent) }
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
            varToolbar?.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            (it as AppCompatActivity).setSupportActionBar(varToolbar)
            it.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        (activity as AppCompatActivity).supportActionBar?.elevation = ProductDetailConstant.TOOLBAR_ELEVATION

        et_search.setOnClickListener {
            DynamicProductDetailTracking.Click.eventSearchToolbarClicked(viewModel.getDynamicProductInfoP1)
            RouteManager.route(context, getLocalSearchApplink())
        }
        et_search.hint = String.format(getString(R.string.pdp_search_hint), "")
    }

    private fun initNavToolbar() {
        search_pdp_toolbar?.hide()
        navToolbar = view?.findViewById(R.id.pdp_navtoolbar)
        navToolbar?.apply {
            viewLifecycleOwner.lifecycle.addObserver(this)
            setIcon(
                    IconBuilder()
                            .addIcon(IconList.ID_SHARE) {
                                onClickShareProduct()
                            }
                            .addIcon(IconList.ID_CART) {}
                            .addIcon(IconList.ID_NAV_GLOBAL) {}
            )

            setNavToolbarSearchHint(getString(R.string.pdp_search_hint, ""))
            setToolbarPageName(ProductTrackingConstant.Category.PDP)
            show()
        }
    }

    private fun getLocalSearchApplink(): String {
        val isTokoNow = viewModel.getDynamicProductInfoP1?.basic?.isTokoNow == true
        val applink = ApplinkConstInternalDiscovery.AUTOCOMPLETE +
                if (isTokoNow) "?${SearchApiConst.NAVSOURCE}=tokonow&${SearchApiConst.BASE_SRP_APPLINK}=${ApplinkConstInternalTokopediaNow.SEARCH}"
                else ""
        return applink
    }

    private fun setNavToolbarSearchHint(hint: String) {
        navToolbar?.setupSearchbar(listOf(HintData(hint)), applink = getLocalSearchApplink())
    }

    private fun initStickyLogin(view: View) {
        stickyLoginView = view.findViewById(R.id.sticky_login_pdp)
        stickyLoginView?.page = StickyLoginConstant.Page.PDP
        stickyLoginView?.lifecycleOwner = viewLifecycleOwner
        stickyLoginView?.setStickyAction(object : StickyLoginAction {
            override fun onClick() {
                goToLogin()
            }

            override fun onDismiss() {

            }

            override fun onViewChange(isShowing: Boolean) {
                updateActionButtonShadow()
            }
        })

        stickyLoginView?.hide()
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
            doAtc(ProductDetailCommonConstant.ATC_BUTTON)
        }
    }

    override fun buyNowClick(buttonText: String) {
        viewModel.buttonActionText = buttonText
        // buy now / buy / preorder
        viewModel.getDynamicProductInfoP1?.let {
            doAtc(ProductDetailCommonConstant.BUY_BUTTON)
        }
    }

    override fun buttonCartTypeClick(cartType: String, buttonText: String, isAtcButton: Boolean) {
        viewModel.buttonActionText = buttonText
        val atcKey = ProductCartHelper.generateButtonAction(cartType, isAtcButton)
        doAtc(atcKey)
    }

    override fun topChatButtonClicked() {
        DynamicProductDetailTracking.Click.eventButtonChatClicked(viewModel.getDynamicProductInfoP1)
        onShopChatClicked()
    }

    override fun onDeleteAtcClicked() {
        if (!viewModel.isUserSessionActive) {
            doLoginWhenUserClickButton()
            return
        }
        showProgressDialog()
        viewModel.deleteProductInCart(viewModel.getDynamicProductInfoP1?.basic?.productID ?: "")
    }

    override fun updateQuantityNonVarTokoNow(quantity: Int, miniCart: MiniCartItem, oldValue: Int) {
        if (!viewModel.isUserSessionActive) {
            doLoginWhenUserClickButton()
            return
        }

        if (openShipmentBottomSheetWhenError()) return
        if (!alreadyHitQtyTracker) {
            alreadyHitQtyTracker = true
            DynamicProductDetailTracking.Click.onQuantityEditorClicked(viewModel.getDynamicProductInfoP1?.basic?.productID
                    ?: "", oldValue, quantity)
        }

        viewModel.updateQuantity(quantity, miniCart)
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
                        view.showToasterError(getString(R.string.product_is_at_status_x, statusMessage), ctaText = getString(com.tokopedia.abstraction.R.string.close))
                    }
                }
            }
        }
    }

    override fun getRxCompositeSubcription(): CompositeSubscription {
        return compositeSubscription
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

    private fun clickButtonWhenVariantTokonow(isVariant: Boolean) {
        if (buttonActionType == ProductDetailCommonConstant.CHECK_WISHLIST_BUTTON) {
            DynamicProductDetailTracking.Click.eventClickOosButton(btn_buy_now.text.toString(), isVariant, viewModel.getDynamicProductInfoP1, viewModel.userId)
            goToWishlist()
        } else {
            DynamicProductDetailTracking.Click.eventClickAtcToVariantBottomSheet(viewModel.getDynamicProductInfoP1?.basic?.productID
                    ?: "")
            goToAtcVariant()
        }
    }

    private fun doAtc(buttonAction: Int) {
        buttonActionType = buttonAction
        context?.let {
            val isVariant = viewModel.getDynamicProductInfoP1?.data?.variant?.isVariant ?: false
            val isPartialySelected = pdpUiUpdater?.productNewVariantDataModel?.isPartialySelected()
                    ?: false

            if (isVariant && pdpUiUpdater?.productSingleVariant != null) {
                clickButtonWhenVariantTokonow(isVariant)
                return@let
            }

            if (buttonActionType == ProductDetailCommonConstant.REMIND_ME_BUTTON || buttonActionType == ProductDetailCommonConstant.CHECK_WISHLIST_BUTTON) {
                DynamicProductDetailTracking.Click.eventClickOosButton(btn_buy_now.text.toString(), isVariant, viewModel.getDynamicProductInfoP1, viewModel.userId)
            }

            if (!viewModel.isUserSessionActive) {
                doLoginWhenUserClickButton()
                return@let
            }

            if (buttonActionType == ProductDetailCommonConstant.REMIND_ME_BUTTON) {
                toasterWishlistText = getString(com.tokopedia.product.detail.common.R.string.toaster_success_add_wishlist_from_button)
                addWishList()
                return@let
            }

            if (buttonActionType == ProductDetailCommonConstant.CHECK_WISHLIST_BUTTON) {
                goToWishlist()
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

            if (openShipmentBottomSheetWhenError()) return@let

            hitAtc(buttonAction)
        }
    }

    private fun doLoginWhenUserClickButton() {
        DynamicProductDetailTracking.Click.eventClickButtonNonLogin(buttonActionType,
                viewModel.getDynamicProductInfoP1, viewModel.userId,
                viewModel.getDynamicProductInfoP1?.shopTypeString ?: "",
                viewModel.buttonActionText)
        goToLogin()
    }

    private fun showErrorVariantUnselected() {
        DynamicProductDetailTracking.Click.onVariantErrorPartialySelected(viewModel.getDynamicProductInfoP1, buttonActionType)
        scrollToPosition(getComponentPosition(pdpUiUpdater?.productNewVariantDataModel))
        val variantErrorMessage = if (viewModel.variantData?.getVariantsIdentifier()?.isEmpty() == true) {
            getString(com.tokopedia.product.detail.common.R.string.add_to_cart_error_variant)
        } else {
            getString(com.tokopedia.product.detail.common.R.string.add_to_cart_error_variant_builder, viewModel.variantData?.getVariantsIdentifier()
                    ?: "")
        }

        view?.showToasterError(variantErrorMessage, ctaText = getString(R.string.label_oke_pdp))
    }

    private fun buyAfterTradeinDiagnose(deviceId: String, phoneType: String, phonePrice: String) {
        buttonActionType = ProductDetailCommonConstant.TRADEIN_AFTER_DIAGNOSE
        viewModel.tradeinDeviceId = deviceId
        hitAtc(ProductDetailCommonConstant.OCS_BUTTON)
    }

    private fun hitAtc(actionButton: Int) {
        val selectedWarehouseId = viewModel.getMultiOriginByProductId().id.toIntOrZero()

        viewModel.getDynamicProductInfoP1?.let { data ->
            showProgressDialog()
            when (actionButton) {
                ProductDetailCommonConstant.OCS_BUTTON -> {
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
                        shippingPrice = viewModel.shippingMinimumPrice.roundToIntOrZero()
                        productName = data.getProductName
                        category = data.basic.category.name
                        price = data.finalPrice.toString()
                        userId = viewModel.userId
                    }
                    viewModel.addToCart(addToCartOcsRequestParams)
                }
                ProductDetailCommonConstant.OCC_BUTTON -> {
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
                        atcFromExternalSource = AtcFromExternalSource.ATC_FROM_PDP
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
        val addToCartOccRequestParams = AddToCartOccMultiRequestParams(
                carts = listOf(
                        AddToCartOccMultiCartParam(
                                productId = data.basic.productID,
                                shopId = data.basic.shopID,
                                quantity = data.basic.minOrder.toString()
                        ).apply {
                            warehouseId = selectedWarehouseId.toString()
                            attribution = trackerAttributionPdp ?: ""
                            listTracker = trackerListNamePdp ?: ""
                            productName = data.getProductName
                            category = data.basic.category.name
                            price = data.finalPrice.toString()
                        }
                ),
                userId = viewModel.userId,
                atcFromExternalSource = AtcFromExternalSource.ATC_FROM_PDP
        )
        viewModel.addToCart(addToCartOccRequestParams)
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
                setLoadingNplShopFollowers(true)
                trackToggleFavoriteShop(componentTrackDataModel)
                pdpUiUpdater?.shopCredibility?.enableButtonFavorite = false
                viewModel.toggleFavorite(viewModel.getDynamicProductInfoP1?.basic?.shopID
                        ?: "", isNplFollowType)
            })
        }
    }

    private fun trackToggleFavoriteShop(componentTrackDataModel: ComponentTrackDataModel?) {
        val isFavorite = pdpUiUpdater?.shopCredibility?.isFavorite ?: return
        val shopName = pdpUiUpdater?.shopCredibility?.shopName ?: ""

        if (isFavorite)
            DynamicProductDetailTracking.Click.eventUnfollowShop(viewModel.getDynamicProductInfoP1, componentTrackDataModel, shopName)
        else
            DynamicProductDetailTracking.Click.eventFollowShop(viewModel.getDynamicProductInfoP1, componentTrackDataModel, shopName)
    }

    private fun onSuccessFavoriteShop(isSuccess: Boolean, isNplFollowerType: Boolean = false) {
        val isFavorite = pdpUiUpdater?.shopCredibility?.isFavorite ?: return
        if (isSuccess) {
            viewModel.clearCacheP2Data()
            pdpUiUpdater?.successUpdateShopFollow(if (isNplFollowerType) false else isFavorite)
            updateUi()
            setupNplVisibility(if (isNplFollowerType) ProductDetailConstant.HIDE_NPL_BS else isFavorite)
        }
    }

    private fun setupNplVisibility(isFavorite: Boolean) {
        val reData = viewModel.p2Data.value?.restrictionInfo?.getReByProductId(viewModel.getDynamicProductInfoP1?.basic?.productID
                ?: "")
        if (reData?.restrictionShopFollowersType() == false) return

        nplFollowersButton?.setupVisibility = if (reData != null && reData.action.isNotEmpty() && !viewModel.isShopOwner()) {
            isFavorite
        } else {
            false
        }
    }

    private fun setupExclusiveVisibility(shouldVisible: Boolean, isExclusiveType: Boolean) {
        if (!isExclusiveType) return
        nplFollowersButton?.setupVisibility = shouldVisible
    }

    private fun renderExclusiveBottomSheet(restrictionData: RestrictionData?) {
        if (restrictionData == null) return
        val isExclusiveType = restrictionData.restrictionExclusiveType()
        if (restrictionData.action.isNotEmpty()) {
            val title = restrictionData.action.firstOrNull()?.title ?: ""
            val desc = restrictionData.action.firstOrNull()?.description ?: ""
            val badgeUrl = restrictionData.action.firstOrNull()?.badgeURL ?: ""
            nplFollowersButton?.renderView(title = title,
                    alreadyFollowShop = false,
                    desc = desc,
                    iconUrl = badgeUrl,
                    hideButton = true,
                    maxLine = 2,
                    centerImage = true)
        }
        setupExclusiveVisibility(restrictionData.isNotEligibleExclusive(), isExclusiveType)
    }

    private fun setLoadingNplShopFollowers(isLoading: Boolean) {
        val restrictionData = viewModel.p2Data.value?.restrictionInfo
        if (restrictionData?.restrictionData?.firstOrNull()?.restrictionShopFollowersType() == false) return
        if (isLoading) {
            nplFollowersButton?.startLoading()
        } else {
            nplFollowersButton?.stopLoading()
        }
    }

    private fun onFailFavoriteShop(t: Throwable) {
        view?.showToasterError(getErrorMessage(t), ctaText = getString(com.tokopedia.abstraction.R.string.retry_label)) {
            onShopFavoriteClick()
        }
        pdpUiUpdater?.failUpdateShopFollow()
        updateUi()
    }

    private fun onShopChatClicked() {
        if (viewModel.getShopInfo().isShopInfoNotEmpty()) {
            val product = viewModel.getDynamicProductInfoP1 ?: return
            doActionOrLogin({
                val shop = viewModel.getShopInfo()
                activity?.let {
                    val boData = viewModel.getBebasOngkirDataByProductId()
                    val intent = RouteManager.getIntent(it,
                            ApplinkConst.TOPCHAT_ASKSELLER,
                            product.basic.shopID, "",
                            "product", shop.shopCore.name, shop.shopAssets.avatar)
                    VariantMapper.putChatProductInfoTo(intent, product.basic.productID, product, viewModel.variantData, boData.imageURL)
                    startActivityForResult(intent, ProductDetailConstant.REQUEST_CODE_TOP_CHAT)
                }
            })
        }
    }

    private fun initToolbarLight() {
        activity?.run {
            if (isAdded) {
                varToolbar?.setTitleTextColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N400))
                varToolbar?.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
                menu?.let {
                    if (it.size() > 2) {
                        it.findItem(R.id.action_share).icon = ContextCompat.getDrawable(this, R.drawable.ic_product_share_dark)
                        val menuCart = it.findItem(R.id.action_cart)
                        menuCart.actionView.cart_image_view.tag = R.drawable.ic_product_cart_counter_dark
                        setBadgeMenuCart(menuCart)
                    }
                }

                varToolbar?.overflowIcon = activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_product_more_dark) }
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
            actionButtonView.setBackground(com.tokopedia.unifyprinciples.R.color.Unify_N0)
        } else {
            val drawable = context?.let { _context -> ContextCompat.getDrawable(_context, com.tokopedia.product.detail.common.R.drawable.bg_shadow_top) }
            drawable?.let { actionButtonView.setBackground(it) }
        }
    }

    private fun getErrorMessage(throwable: Throwable): String {
        return context?.let {
            ProductDetailErrorHandler.getErrorMessage(it, throwable)
        }
                ?: getString(com.tokopedia.product.detail.common.R.string.merchant_product_detail_error_default)
    }

    private fun getErrorMessage(errorMessage: String?): String {
        return errorMessage
                ?: getString(com.tokopedia.product.detail.common.R.string.merchant_product_detail_error_default)
    }

    private fun hideProgressDialog() {
        if (loadingProgressDialog != null && loadingProgressDialog?.isShowing == true) {
            loadingProgressDialog?.dismiss()
        }
    }

    private fun showProgressDialog(onCancelClicked: (() -> Unit)? = null) {
        if (loadingProgressDialog == null) {
            loadingProgressDialog = activity?.createDefaultProgressDialog(
                    getString(com.tokopedia.abstraction.R.string.title_loading),
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
            tradeinParam.setPrice(it.data.price.value.roundToIntOrZero())
            tradeinParam.productId = it.basic.productID
            tradeinParam.productName = it.data.name
        }

        intent.putExtra(TradeInParams.TRADE_IN_PARAMS, tradeinParam)
        startActivityForResult(intent, ApplinkConstInternalCategory.FINAL_PRICE_REQUEST_CODE)
    }

    private fun goToTradeInHome() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalCategory.TRADEIN)
        val tradeinParam = viewModel.tradeInParams

        viewModel.getDynamicProductInfoP1?.let {
            tradeinParam.setPrice(it.data.price.value.roundToIntOrZero())
            tradeinParam.productId = it.basic.productID
            tradeinParam.productName = it.data.name
            tradeinParam.origin = viewModel.getMultiOriginByProductId().getOrigin()
        }
        intent.putExtra(TradeInParams.PARAM_PERMISSION_GIVEN, true)
        intent.putExtra(TradeInParams.TRADE_IN_PARAMS, tradeinParam)
        startActivityForResult(intent, ApplinkConstInternalCategory.TRADEIN_HOME_REQUEST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        activity?.run {
            ImeiPermissionAsker.onImeiRequestPermissionsResult(this, requestCode, permissions, grantResults,
                    onUserDenied = {}, onUserDeniedAndDontAskAgain = {}, onUserAcceptPermission = {}
            )
        }
        screenshotDetector?.onRequestPermissionsResult(requestCode, grantResults, this)
    }

    private fun observeToggleNotifyMe() {
        viewLifecycleOwner.observe(viewModel.toggleTeaserNotifyMe) { data ->
            data.doSuccessOrFail({
                onSuccessToggleNotifyMe(it.data)
            }, {
                onFailNotifyMe(it)
                logException(it)
            })
        }
    }

    private fun onSuccessToggleNotifyMe(data: NotifyMeUiData) {
        viewModel.clearCacheP2Data()
//        val messageSuccess = if (data.upcomingType == "fs toko") {
//            if (data.notifyMeAction == ProductDetailCommonConstant.VALUE_TEASER_ACTION_REGISTER)
//                getString(R.string.notify_me_fs_toko_success_registered_message)
//            else {
//                getString(R.string.notify_me_success_unregistered_message)
//            }
//        } else {
//            if (viewModel.notifyMeAction == ProductDetailCommonConstant.VALUE_TEASER_ACTION_REGISTER)
//                getString(R.string.notify_me_success_registered_message)
//            else
//                getString(R.string.notify_me_success_unregistered_message)
//        }

        view?.showToasterSuccess(data.successMessage)
        viewModel.updateNotifyMeData()
    }

    private fun onFailNotifyMe(t: Throwable) {
        val dataModel = pdpUiUpdater?.notifyMeMap
        view?.showToasterError(getErrorMessage(t), ctaText = getString(com.tokopedia.design.R.string.oke))
        if (dataModel != null) {
            pdpUiUpdater?.updateNotifyMeButton(dataModel.notifyMe)
            updateUi()
        }
    }

    override fun onNotifyMeClicked(data: ProductNotifyMeDataModel, componentTrackDataModel: ComponentTrackDataModel) {
        doActionOrLogin({
            pdpUiUpdater?.notifyMeMap?.notifyMe?.let { notifyMe ->
                trackToggleNotifyMe(componentTrackDataModel, notifyMe)
            }
            pdpUiUpdater?.updateNotifyMeButton(data.notifyMe)
            updateUi()
            viewModel.toggleTeaserNotifyMe(data.notifyMe,
                    data.campaignID.toLongOrZero(),
                    productId?.toLongOrZero() ?: 0)
        })
    }

    private fun trackToggleNotifyMe(componentTrackDataModel: ComponentTrackDataModel?, notifyMe: Boolean) {
        viewModel.getDynamicProductInfoP1?.let {
            DynamicProductDetailTracking.Click.eventNotifyMe(it, componentTrackDataModel, notifyMe, viewModel.userId)
        }
    }

    private fun trackOnTickerClicked(tickerTitle: String, tickerType: Int, componentTrackDataModel: ComponentTrackDataModel?, tickerDescription: String) {
        DynamicProductDetailTracking.Click.eventClickTicker(tickerTitle, tickerType, viewModel.getDynamicProductInfoP1, componentTrackDataModel, viewModel.userId, tickerDescription)
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
            base_btn_affiliate_dynamic.visible()
            loadingAffiliateDynamic.visible()
        }
    }

    private fun getAbTestPlatform(): AbTestPlatform? {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform
        } catch (e: java.lang.IllegalStateException) {
            null
        }
    }

    //Will be delete soon
    override fun isNavOld(): Boolean {
        return try {
            getAbTestPlatform()?.let {
                return it.getString(RollenceKey.NAVIGATION_EXP_TOP_NAV, it.getString(RollenceKey.NAVIGATION_EXP_TOP_NAV2, RollenceKey.NAVIGATION_VARIANT_OLD)) == RollenceKey.NAVIGATION_VARIANT_OLD || GlobalConfig.isSellerApp()
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }

    override fun getFragmentTrackingQueue(): TrackingQueue? {
        return trackingQueue
    }

    override fun getVariantString(): String {
        return viewModel.variantData?.getVariantCombineIdentifier() ?: ""
    }

    private fun navAbTestCondition(ifNavRevamp: () -> Unit = {}, ifNavOld: () -> Unit = {}) {
        if (!isNavOld()) {
            ifNavRevamp.invoke()
        } else if (isNavOld()) {
            ifNavOld.invoke()
        }
    }

    private fun goToRecommendation() {
        val uri = UriUtil.buildUri(ProductDetailConstant.RECOM_URL, viewModel.getDynamicProductInfoP1?.basic?.productID)
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
        val position = getComponentPosition(model)
        DynamicProductDetailTracking.Click.eventTopAdsImageViewClicked(trackingQueue, viewModel.userId, bannerId, position, bannerName)
    }

    override fun onTopAdsImageViewImpression(model: TopAdsImageDataModel, bannerId: String, bannerName: String) {
        val position = getComponentPosition(model)
        DynamicProductDetailTracking.Impression.eventTopAdsImageViewImpression(trackingQueue, viewModel.userId, bannerId, position, bannerName)
    }

    override fun onClickBestSeller(componentTrackDataModel: ComponentTrackDataModel, appLink: String) {
        DynamicProductDetailTracking.Click.eventClickBestSeller(componentTrackDataModel, viewModel.getDynamicProductInfoP1, "", viewModel.userId)
        goToApplink(appLink)
    }

    private fun shouldGoToNewGallery(): Boolean {
        getAbTestPlatform()?.let {
            return it.getString(RollenceKey.EXPERIMENT_NAME_REVIEW_PRODUCT_READING, RollenceKey.VARIANT_OLD_REVIEW_PRODUCT_READING) == RollenceKey.VARIANT_NEW_REVIEW_PRODUCT_READING
        }
        return false
    }

    override fun screenShotTaken() {
        shareProduct()
    }

}
