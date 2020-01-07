package com.tokopedia.product.detail.view.fragment

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.*
import android.view.Menu
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator
import com.tokopedia.abstraction.Actions.interfaces.ActionUIDelegate
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common_tradein.customviews.TradeInTextView
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.common_tradein.viewmodel.TradeInBroadcastReceiver
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.design.drawable.CountDrawable
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.discovery.common.manager.AdultManager
import com.tokopedia.gallery.ImageReviewGalleryActivity
import com.tokopedia.gallery.customview.BottomSheetImageReviewSlider
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.ProductDetailRouter
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.product.*
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.data.model.*
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneAddedProductDataModel
import com.tokopedia.product.detail.data.model.financing.FinancingDataResponse
import com.tokopedia.product.detail.data.util.ProductDetailConstant.URL_VALUE_PROPOSITION_GUARANTEE
import com.tokopedia.product.detail.data.util.ProductDetailConstant.URL_VALUE_PROPOSITION_GUARANTEE_7_DAYS
import com.tokopedia.product.detail.data.util.ProductDetailConstant.URL_VALUE_PROPOSITION_ORI
import com.tokopedia.product.detail.data.util.ProductDetailConstant.URL_VALUE_PROPOSITION_READY
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.data.util.origin
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.estimasiongkir.view.activity.RatesEstimationDetailActivity
import com.tokopedia.product.detail.imagepreview.view.activity.ImagePreviewPdpActivity
import com.tokopedia.product.detail.view.activity.CourierActivity
import com.tokopedia.product.detail.view.activity.WholesaleActivity
import com.tokopedia.product.detail.view.adapter.RecommendationProductAdapter
import com.tokopedia.product.detail.view.fragment.partialview.*
import com.tokopedia.product.detail.view.util.AppBarState
import com.tokopedia.product.detail.view.util.AppBarStateChangeListener
import com.tokopedia.product.detail.view.util.FlingBehavior
import com.tokopedia.product.detail.view.util.ProductDetailErrorHandler
import com.tokopedia.product.detail.view.viewmodel.Loaded
import com.tokopedia.product.detail.view.viewmodel.Loading
import com.tokopedia.product.detail.view.viewmodel.ProductInfoViewModel
import com.tokopedia.product.detail.view.widget.*
import com.tokopedia.product.detail.view.widget.AddToCartDoneBottomSheet.Companion.KEY_ADDED_PRODUCT_DATA_MODEL
import com.tokopedia.product.detail.view.widget.FtPDPInstallmentBottomSheet.Companion.KEY_PDP_FINANCING_DATA
import com.tokopedia.product.detail.view.widget.FtPDPInstallmentBottomSheet.Companion.KEY_PDP_IS_OFFICIAL
import com.tokopedia.product.detail.view.widget.FtPDPInstallmentBottomSheet.Companion.KEY_PDP_PRODUCT_PRICE
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.product.warehouse.view.viewmodel.ProductWarehouseViewModel
import com.tokopedia.purchase_platform.common.constant.*
import com.tokopedia.purchase_platform.common.constant.Constant.*
import com.tokopedia.purchase_platform.common.constant.NormalCheckoutConstant.Companion.RESULT_PRODUCT_DATA
import com.tokopedia.purchase_platform.common.constant.NormalCheckoutConstant.Companion.RESULT_PRODUCT_DATA_CACHE_ID
import com.tokopedia.purchase_platform.common.constant.NormalCheckoutConstant.Companion.RESULT_SELECTED_WAREHOUSE
import com.tokopedia.purchase_platform.common.data.model.request.atc.AtcRequestParam
import com.tokopedia.purchase_platform.common.sharedata.RESULT_CODE_ERROR_TICKET
import com.tokopedia.purchase_platform.common.sharedata.RESULT_TICKET_DATA
import com.tokopedia.purchase_platform.common.sharedata.helpticket.SubmitTicketResult
import com.tokopedia.purchase_platform.common.view.error_bottomsheet.ErrorBottomsheets
import com.tokopedia.purchase_platform.common.view.error_bottomsheet.ErrorBottomsheetsActionListenerWithRetry
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.referral.Constants.Action.Companion.ACTION_GET_REFERRAL_CODE
import com.tokopedia.referral.ReferralAction
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shopetalasepicker.constant.ShopParamConstant
import com.tokopedia.shopetalasepicker.view.activity.ShopEtalasePickerActivity
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.stickylogin.view.StickyLoginView
import com.tokopedia.topads.detail_sheet.TopAdsDetailSheet
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.menu_item_cart.view.*
import kotlinx.android.synthetic.main.partial_layout_button_action.*
import kotlinx.android.synthetic.main.partial_most_helpful_review_view.*
import kotlinx.android.synthetic.main.partial_product_detail_header.*
import kotlinx.android.synthetic.main.partial_product_detail_visibility.*
import kotlinx.android.synthetic.main.partial_product_detail_wholesale.*
import kotlinx.android.synthetic.main.partial_product_full_descr.*
import kotlinx.android.synthetic.main.partial_product_image_review.*
import kotlinx.android.synthetic.main.partial_product_latest_talk.*
import kotlinx.android.synthetic.main.partial_product_rating_talk_courier.*
import kotlinx.android.synthetic.main.partial_product_recom_1.*
import kotlinx.android.synthetic.main.partial_product_recom_2.*
import kotlinx.android.synthetic.main.partial_product_recom_3.*
import kotlinx.android.synthetic.main.partial_product_recom_4.*
import kotlinx.android.synthetic.main.partial_product_shop_info.*
import kotlinx.android.synthetic.main.partial_value_proposition_os.*
import kotlinx.android.synthetic.main.partial_variant_rate_estimation.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.roundToLong

class ProductDetailFragment : BaseDaggerFragment(), RecommendationProductAdapter.UserActiveListener, BottomSheetImageReviewSlider.Callback {

    private var productId: String? = null
    private var warehouseId: String? = null
    private var productKey: String? = null
    private var shopDomain: String? = null
    private var trackerAttribution: String? = ""
    private var trackerListName: String? = ""
    private var affiliateString: String? = null
    private var isFromDeeplink: Boolean = false
    private var isAffiliate: Boolean = false
    private var isLeasing: Boolean = false
    private var deeplinkUrl: String = ""

    lateinit var headerView: PartialHeaderView
    lateinit var productStatsView: PartialProductStatisticView
    lateinit var partialVariantAndRateEstView: PartialVariantAndRateEstView
    lateinit var productDescrView: PartialProductDescrFullView
    lateinit var actionButtonView: PartialButtonActionView
    lateinit var topAdsDetailSheet: TopAdsDetailSheet
    lateinit var productShopView: PartialShopView
    lateinit var attributeInfoView: PartialAttributeInfoView
    lateinit var imageReviewViewView: PartialImageReviewView
    lateinit var mostHelpfulReviewView: PartialMostHelpfulReviewView
    lateinit var latestTalkView: PartialLatestTalkView
    lateinit var recommendationSecondView: PartialRecommendationSecondView
    lateinit var recommendationFirstView: PartialRecommendationFirstView
    lateinit var recommendationThirdView: PartialRecommendationThirdView
    lateinit var recommendationFourthView: PartialRecommendationFourthView
    lateinit var valuePropositionView: PartialValuePropositionView
    lateinit var stickyLoginView: StickyLoginView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var productInfoViewModel: ProductInfoViewModel
    lateinit var productWarehouseViewModel: ProductWarehouseViewModel

    @Inject
    lateinit var productDetailTracking: ProductDetailTracking

    lateinit var performanceMonitoringP1: PerformanceMonitoring
    lateinit var performanceMonitoringP2: PerformanceMonitoring
    lateinit var performanceMonitoringP2General: PerformanceMonitoring
    lateinit var performanceMonitoringP2Login: PerformanceMonitoring
    lateinit var performanceMonitoringFull: PerformanceMonitoring
    lateinit var remoteConfig: RemoteConfig

    private var isAppBarCollapsed = false
    private var menu: Menu? = null
    private var useVariant = true
    private lateinit var varToolbar: Toolbar
    private lateinit var varPictureImage: PictureScrollingView
    private lateinit var bottomSheet: ValuePropositionBottomSheet

    private var userCod: Boolean = false
    private var shopCod: Boolean = false
    private var shouldShowCod = false
    private lateinit var tradeInParams: TradeInParams
    private lateinit var tradeInBroadcastReceiver: TradeInBroadcastReceiver

    var loadingProgressDialog: ProgressDialog? = null
    val errorBottomsheets: ErrorBottomsheets by lazy {
        ErrorBottomsheets()
    }

    private var userInputNotes = ""
    private var userInputQuantity = 0
    private var userInputVariant: String? = null
    private var delegateTradeInTracking = false

    private var shouldShowCartAnimation = false

    private lateinit var initToolBarMethod: () -> Unit

    var productInfo: ProductInfo? = null
    var topAdsGetProductManage: TopAdsGetProductManage = TopAdsGetProductManage()
    var shopInfo: ShopInfo? = null

    private var refreshLayout: SwipeToRefresh? = null

    private var tickerDetail: StickyLoginTickerPojo.TickerDetail? = null

    private var isWishlisted = false

    override val isUserSessionActive: Boolean
        get() = if (!::productInfoViewModel.isInitialized) false else productInfoViewModel.isUserSessionActive()

    companion object {
        const val REQUEST_CODE_TALK_PRODUCT = 1
        const val REQUEST_CODE_EDIT_PRODUCT = 2
        const val REQUEST_CODE_LOGIN = 561
        const val REQUEST_CODE_MERCHANT_VOUCHER_DETAIL = 563
        const val REQUEST_CODE_MERCHANT_VOUCHER = 564
        const val REQUEST_CODE_ETALASE = 565
        const val REQUEST_CODE_NORMAL_CHECKOUT = 566
        const val REQUEST_CODE_ATC_EXPRESS = 567
        const val REQUEST_CODE_LOGIN_THEN_BUY_EXPRESS = 569
        const val REQUEST_CODE_REPORT = 570
        const val REQUEST_CODE_SHOP_INFO = 998
        const val REQUEST_CODE_IMAGE_PREVIEW = 999

        const val CART_MAX_COUNT = 99
        const val CART_ALPHA_ANIMATION_FROM = 1f
        const val CART_ALPHA_ANIMATION_TO = 0f
        const val CART_SCALE_ANIMATION_FROM = 1f
        const val CART_SCALE_ANIMATION_TO = 2f
        const val CART_SCALE_ANIMATION_PIVOT = 0.5f
        const val CART_ANIMATION_DURATION = 700L

        private const val STICKY_SHOW_DELAY: Long = 3 * 60 * 1000

        const val EXTRA_IMAGE_URL_LIST = "EXTRA_IMAGE_URL_LIST"
        const val EXTRA_DEFAULT_POSITION = "EXTRA_DEFAULT_POSITION"

        const val SAVED_NOTE = "saved_note"
        const val SAVED_QUANTITY = "saved_quantity"
        const val SAVED_VARIANT = "saved_variant"

        private const val PDP_P1_TRACE = "mp_pdp_p1"
        private const val PDP_P2_TRACE = "mp_pdp_p2"
        private const val PDP_P2_GENERAL_TRACE = "mp_pdp_p2_general"
        private const val PDP_P2_LOGIN_TRACE = "mp_pdp_p2_login"
        private const val PDP_P3_TRACE = "mp_pdp_p3"
        private const val ENABLE_VARIANT = "mainapp_discovery_enable_pdp_variant"

        private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"
        private const val ARG_WAREHOUSE_ID = "ARG_WAREHOUSE_ID"
        private const val ARG_PRODUCT_KEY = "ARG_PRODUCT_KEY"
        private const val ARG_SHOP_DOMAIN = "ARG_SHOP_DOMAIN"
        private const val ARG_TRACKER_ATTRIBUTION = "ARG_TRACKER_ATTRIBUTION"
        private const val ARG_TRACKER_LIST_NAME = "ARG_TRACKER_LIST_NAME"
        private const val ARG_FROM_DEEPLINK = "ARG_FROM_DEEPLINK"
        private const val ARG_FROM_AFFILIATE = "ARG_FROM_AFFILIATE"
        private const val ARG_AFFILIATE_STRING = "ARG_AFFILIATE_STRING"
        private const val ARG_DEEPLINK_URL = "ARG_DEEPLINK_URL"

        private const val WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"

        fun newInstance(productId: String? = null,
                        warehouseId: String? = null,
                        shopDomain: String? = null,
                        productKey: String? = null,
                        isFromDeeplink: Boolean = false,
                        isAffiliate: Boolean = false,
                        trackerAttribution: String? = null,
                        trackerListName: String? = null,
                        affiliateString: String? = null,
                        deeplinkUrl: String? = null) =
                ProductDetailFragment().also {
                    it.arguments = Bundle().apply {
                        productId?.let { pid -> putString(ARG_PRODUCT_ID, pid) }
                        warehouseId?.let { whId -> putString(ARG_WAREHOUSE_ID, whId) }
                        productKey?.let { pkey -> putString(ARG_PRODUCT_KEY, pkey) }
                        shopDomain?.let { domain -> putString(ARG_SHOP_DOMAIN, domain) }
                        trackerAttribution?.let { attribution -> putString(ARG_TRACKER_ATTRIBUTION, attribution) }
                        trackerListName?.let { listName -> putString(ARG_TRACKER_LIST_NAME, listName) }
                        affiliateString?.let { affiliateString -> putString(ARG_AFFILIATE_STRING, affiliateString) }
                        deeplinkUrl?.let { deeplinkUrl -> putString(ARG_DEEPLINK_URL, deeplinkUrl) }
                        putBoolean(ARG_FROM_DEEPLINK, isFromDeeplink)
                        putBoolean(ARG_FROM_AFFILIATE, isAffiliate)
                    }
                }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(ProductDetailComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            userInputNotes = savedInstanceState.getString(SAVED_NOTE, "")
            userInputQuantity = savedInstanceState.getInt(SAVED_QUANTITY, 1)
            userInputVariant = savedInstanceState.getString(SAVED_VARIANT)
        }
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getString(ARG_PRODUCT_ID)
            warehouseId = it.getString(ARG_WAREHOUSE_ID)
            productKey = it.getString(ARG_PRODUCT_KEY)
            shopDomain = it.getString(ARG_SHOP_DOMAIN)
            trackerAttribution = it.getString(ARG_TRACKER_ATTRIBUTION)
            trackerListName = it.getString(ARG_TRACKER_LIST_NAME)
            affiliateString = it.getString(ARG_AFFILIATE_STRING)
            isFromDeeplink = it.getBoolean(ARG_FROM_DEEPLINK, false)
            isAffiliate = it.getBoolean(ARG_FROM_AFFILIATE, false)
            deeplinkUrl = it.getString(ARG_DEEPLINK_URL, "")
        }
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            productInfoViewModel = viewModelProvider.get(ProductInfoViewModel::class.java)
            productWarehouseViewModel = viewModelProvider.get(ProductWarehouseViewModel::class.java)
            remoteConfig = FirebaseRemoteConfigImpl(this)
            if (!remoteConfig.getBoolean(ENABLE_VARIANT))
                useVariant = false
        }
        setHasOptionsMenu(true)
    }

    override fun onPause() {
        super.onPause()
        productDetailTracking.sendAllQueue()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        productInfoViewModel.productInfoP1Resp.observe(this, Observer {
            refreshLayout?.isRefreshing = false
            performanceMonitoringP1.stopTrace()
            when (it) {
                is Success -> onSuccessGetProductInfo(it.data)
                is Fail -> onErrorGetProductInfo(it.throwable)
            }
        })

        productInfoViewModel.p2ShopDataResp.observe(this, Observer {
            if (!productInfoViewModel.isUserSessionActive() && ::performanceMonitoringFull.isInitialized)
                performanceMonitoringFull.stopTrace()

            performanceMonitoringP2.stopTrace()
            it?.run { renderProductInfo2Shop(this) }
        })

        productInfoViewModel.p2General.observe(this, Observer {
            performanceMonitoringP2General.stopTrace()
            it?.run {
                renderProductInfo2(this)
                if (variantResp == null)
                    onErrorGetProductVariantInfo()
                else
                    onSuccessGetProductVariantInfo(variantResp)
            }
        })

        productInfoViewModel.p2Login.observe(this, Observer {
            if (::performanceMonitoringFull.isInitialized)
                performanceMonitoringP2Login.stopTrace()
            it?.run { renderProductInfoP2Login(this) }
        })

        productInfoViewModel.productInfoP3resp.observe(this, Observer {
            if (::performanceMonitoringFull.isInitialized)
                performanceMonitoringFull.stopTrace()
            it?.run { renderProductInfo3(this) }
        })

        productInfoViewModel.loadTopAdsProduct.observe(this, Observer {
            when (it) {
                is Loading -> {
                    loadingRecommendationView()
                }
                is Loaded -> {
                    (it.data as? Success)?.data?.let { result ->
                        renderRecommendationData(result)
                    }
                }
            }
        })
        warehouseId?.let {
            productInfoViewModel.warehouseId = it
        }
    }

    private fun hideRecommendationView() {
        recommendationSecondView.hideView()
        recommendationFirstView.hideView()
        recommendationThirdView.hideView()
        recommendationFourthView.hideView()
    }

    private fun loadingRecommendationView() {
        recommendationSecondView.startLoading()
        recommendationFirstView.startLoading()
        recommendationThirdView.startLoading()
        recommendationFourthView.startLoading()
    }

    private fun renderRecommendationData(result: List<RecommendationWidget>) {
        result.getOrNull(0)?.let {
            recommendationFirstView.renderData(it)
        }

        result.getOrNull(1)?.let {
            recommendationSecondView.renderData(it)
        }

        result.getOrNull(2)?.let {
            recommendationThirdView.renderData(it)
        }

        result.getOrNull(3)?.let {
            recommendationFourthView.renderData(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        performanceMonitoringP1 = PerformanceMonitoring.start(PDP_P1_TRACE)
        performanceMonitoringP2 = PerformanceMonitoring.start(PDP_P2_TRACE)
        performanceMonitoringP2General = PerformanceMonitoring.start(PDP_P2_GENERAL_TRACE)

        if (productInfoViewModel.isUserSessionActive()) {
            performanceMonitoringP2Login = PerformanceMonitoring.start(PDP_P2_LOGIN_TRACE)
            performanceMonitoringFull = PerformanceMonitoring.start(PDP_P3_TRACE)
        }

        initializePartialView(view)
        initView()
        tv_trade_in_promo.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable(activity, R.drawable.tradein_white), null, null, null)
        tv_trade_in_promo.setOnClickListener {
            productId?.let {
                productDetailTracking.eventClickTradeInRibbon(it)
            }
            scrollToTradeInWidget()
        }
        refreshLayout = view.findViewById(R.id.swipeRefresh)
        if (GlobalConfig.isSellerApp()) {
            val linearLayout = view.findViewById<ViewGroup>(R.id.layout_search)
            linearLayout.hide()
        }

        et_search.setOnClickListener {
            productDetailTracking.eventClickSearchBar()
            RouteManager.route(context, ApplinkConstInternalDiscovery.AUTOCOMPLETE)
        }
        et_search.hint = String.format(getString(R.string.pdp_search_hint), "")

        tradeInBroadcastReceiver = TradeInBroadcastReceiver()
        tradeInBroadcastReceiver.setBroadcastListener {
            if (it) {
                if (tv_trade_in_promo != null) {
                    tv_trade_in_promo.visible()
                    tv_available_at?.visible()
                }
            }
            trackProductView(it)
        }
        context?.let {
            LocalBroadcastManager.getInstance(context!!).registerReceiver(tradeInBroadcastReceiver, IntentFilter(TradeInTextView.ACTION_TRADEIN_ELLIGIBLE))
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val layoutParams = appbar.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.behavior = FlingBehavior(nested_scroll)
        }

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset -> refreshLayout?.isEnabled = (verticalOffset == 0) })
        refreshLayout?.setOnRefreshListener {
            loadProductData(true)
            updateStickyContent()
        }

        if (isAffiliate) {
            actionButtonView.gone()
            base_btn_affiliate.visible()
            loadingAffiliate.visible()
        }

        merchantVoucherListWidget.setOnMerchantVoucherListWidgetListener(object : MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener {
            override val isOwner: Boolean
                get() = productInfo?.basic?.shopID?.let { productInfoViewModel.isShopOwner(it) }
                        ?: false

            override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {
                activity?.let {
                    //TOGGLE_MVC_OFF
                    productDetailTracking.eventClickMerchantVoucherUse(merchantVoucherViewModel, position)
                    showSnackbarClose(getString(R.string.title_voucher_code_copied))
                }
            }

            override fun onItemClicked(merchantVoucherViewModel: MerchantVoucherViewModel) {
                activity?.let {
                    productInfo?.run {
                        productDetailTracking.eventClickMerchantVoucherSeeDetail(basic.id)
                        val intent = MerchantVoucherDetailActivity.createIntent(it, merchantVoucherViewModel.voucherId,
                                merchantVoucherViewModel, basic.shopID.toString())
                        startActivityForResult(intent, REQUEST_CODE_MERCHANT_VOUCHER_DETAIL)
                    }
                }
            }

            override fun onSeeAllClicked() {
                activity?.let {
                    productInfo?.run {
                        productDetailTracking.eventClickMerchantVoucherSeeAll(basic.id)
                        if (shopInfo == null) return@let

                        val intent = MerchantVoucherListActivity.createIntent(it, basic.shopID.toString(),
                                shopInfo!!.shopCore.name)
                        startActivityForResult(intent, REQUEST_CODE_MERCHANT_VOUCHER)
                    }
                }
            }
        })
        fab_detail.setOnClickListener {
            if (productInfoViewModel.isUserSessionActive()) {
                val productP3value = productInfoViewModel.productInfoP3resp.value
                if (shopInfo != null && shopInfo?.isAllowManage == 1) {
                    if (productInfo?.basic?.status != ProductStatusTypeDef.PENDING) {
                        gotoEditProduct()
                    } else {
                        activity?.run {
                            val statusMessage = productInfo?.basic?.statusMessage(this)
                            if (statusMessage?.isNotEmpty() == true) {
                                ToasterError.showClose(this, getString(R.string.product_is_at_status_x, statusMessage))
                            }
                        }
                    }
                } else if (productP3value != null) {
                    if (it.isActivated) {
                        productId?.let {
                            productInfoViewModel.removeWishList(it,
                                    onSuccessRemoveWishlist = this::onSuccessRemoveWishlist,
                                    onErrorRemoveWishList = this::onErrorRemoveWishList)
                            productDetailTracking.eventPDPRemoveToWishlist(productInfo?.basic?.id.toString())
                        }

                    } else {
                        productId?.let {
                            productInfoViewModel.addWishList(it,
                                    onSuccessAddWishlist = this::onSuccessAddWishlist,
                                    onErrorAddWishList = this::onErrorAddWishList)
                            productInfo?.let {
                                productDetailTracking.eventPDPWishlistAppsFyler(it)
                            }
                            productDetailTracking.eventPDPAddToWishlist(productInfo?.basic?.id.toString())
                        }
                    }
                    if (isAffiliate && productId?.isNotEmpty() == true) {
                        productDetailTracking.eventClickWishlistOnAffiliate(
                                productInfoViewModel.userId,
                                productId!!
                        )
                    }
                }
            } else {
                productDetailTracking.eventPDPAddToWishlistNonLogin(productInfo?.basic?.id.toString())
                context?.run {
                    startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                            REQUEST_CODE_LOGIN)
                }
            }
        }

        actionButtonView.rincianTopAdsClick = {
            context?.let {
                topAdsDetailSheet.show(topAdsGetProductManage.data.adId)
            }
        }

        topAdsDetailSheet.detailTopAdsClick = {
            shopInfo?.let { shopInfo ->
                val applink = Uri.parse(ApplinkConst.SellerApp.TOPADS_PRODUCT_CREATE).buildUpon()
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID, shopInfo.shopCore.shopID)
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, productInfo?.basic?.id?.toString())
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE,
                                if (GlobalConfig.isSellerApp()) TopAdsSourceOption.SA_PDP else TopAdsSourceOption.MA_PDP).build().toString()

                context?.let {
                    startActivityForResult(RouteManager.getIntent(it, applink), REQUEST_CODE_EDIT_PRODUCT)
                }
            }
        }

        actionButtonView.promoTopAdsClick = {
            shopInfo?.let { shopInfo ->
                val applink = Uri.parse(ApplinkConst.SellerApp.TOPADS_PRODUCT_CREATE).buildUpon()
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID, shopInfo.shopCore.shopID)
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, productInfo?.basic?.id?.toString())
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE,
                                if (GlobalConfig.isSellerApp()) TopAdsSourceOption.SA_PDP else TopAdsSourceOption.MA_PDP).build().toString()

                context?.let { RouteManager.route(it, applink) }
            }
        }
        actionButtonView.addToCartClick = {
            productDetailTracking.eventClickAddToCart(productInfo?.basic?.id?.toString() ?: "",
                    productInfo?.variant?.isVariant ?: false)
            goToNormalCheckout(ATC_ONLY)
        }
        actionButtonView.buyNowClick = {
            // buy now / buy / preorder
            productDetailTracking.eventClickBuy(productInfo?.basic?.id?.toString() ?: "",
                    productInfo?.variant?.isVariant ?: false)
            doBuy()
        }
        valuePropositionView.hideBackgroundResource = {
            base_attribute.setBackgroundResource(0)
        }
        headerView.onGuaranteeOsClicked = {
            onValuePropositionClick(R.id.layout_guarantee)
        }

        open_shop.setOnClickListener {
            activity?.let {
                if (productInfoViewModel.isUserSessionActive()) {
                    val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.OPEN_SHOP)
                            ?: return@let
                    startActivity(intent)
                } else {
                    startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                            REQUEST_CODE_LOGIN)
                }
            }
        }

        loadProductData()

        stickyLoginView = view.findViewById(R.id.sticky_login_text)
        stickyLoginView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            updateStickyState()
        }
        stickyLoginView.setOnClickListener {
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
            stickyLoginView.tracker.clickOnLogin(StickyLoginConstant.Page.PDP)
        }
        stickyLoginView.setOnDismissListener(View.OnClickListener {
            stickyLoginView.dismiss(StickyLoginConstant.Page.PDP)
            stickyLoginView.tracker.clickOnDismiss(StickyLoginConstant.Page.PDP)
            updateStickyState()
        })

        updateStickyContent()
    }

    private fun scrollToTradeInWidget() {
        activity?.run {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val screenHeight = size.y
            nested_scroll.smoothScrollTo(0, tv_trade_in.bottom - (screenHeight / 2))
        }
    }

    override fun onResume() {
        super.onResume()
        updateStickyContent()
    }

    private fun doBuy() {
        val isExpressCheckout = (productInfoViewModel.p2Login.value)?.isExpressCheckoutType
                ?: false
        if (isExpressCheckout) {
            if (productInfoViewModel.isUserSessionActive()) {
                goToAtcExpress()
            } else {
                context?.let {
                    startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                            REQUEST_CODE_LOGIN_THEN_BUY_EXPRESS)
                }
            }
        } else {
            goToNormalCheckout()
        }
    }

    private fun goToNormalCheckout(@ProductAction action: Int = ATC_AND_BUY) {
        context?.let {
            productInfo?.run {
                val isOcsCheckoutType = (productInfoViewModel.p2Login.value)?.isOcsCheckoutType
                        ?: false
                val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.NORMAL_CHECKOUT).apply {
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_TITLE, basic.name)
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_PRICE, productInfo?.basic?.price)
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_CONDITION, basic.condition)
                    putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_ID, category.id)
                    putExtra(ApplinkConst.Transaction.EXTRA_CATEGORY_NAME, category.name)
                    putExtra(ApplinkConst.Transaction.EXTRA_SHOP_ID, basic.shopID.toString())
                    putExtra(ApplinkConst.Transaction.EXTRA_PRODUCT_ID, parentProductId)
                    putExtra(ApplinkConst.Transaction.EXTRA_NOTES, userInputNotes)
                    putExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, userInputQuantity)
                    putExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID, userInputVariant)
                    putExtra(ApplinkConst.Transaction.EXTRA_ACTION, action)
                    putExtra(ApplinkConst.Transaction.TRACKER_ATTRIBUTION, trackerAttribution)
                    putExtra(ApplinkConst.Transaction.TRACKER_LIST_NAME, trackerListName)
                    putExtra(ApplinkConst.Transaction.EXTRA_SHOP_TYPE, shopInfo?.goldOS?.shopTypeString)
                    putExtra(ApplinkConst.Transaction.EXTRA_SHOP_NAME, shopInfo?.shopCore?.name)
                    putExtra(ApplinkConst.Transaction.EXTRA_OCS, isOcsCheckoutType)
                    putExtra(ApplinkConst.Transaction.EXTRA_IS_LEASING, isLeasing)
                }
                if (::tradeInParams.isInitialized) {
                    intent.putExtra(ApplinkConst.Transaction.EXTRA_TRADE_IN_PARAMS, tradeInParams)
                }
                startActivityForResult(intent,
                        REQUEST_CODE_NORMAL_CHECKOUT)
            }
        }
    }

    private fun goToAtcExpress() {
        activity?.let {
            try {
                val productInfo = (productInfoViewModel.productInfoP1Resp.value as Success).data
                val warehouseId: Int = productInfoViewModel.multiOrigin.id.toIntOrZero()
                val atcRequestParam = AtcRequestParam()
                atcRequestParam.setShopId(productInfo.productInfo.basic.shopID)
                atcRequestParam.setProductId(productInfo.productInfo.basic.id)
                atcRequestParam.setNotes(userInputNotes)
                val qty = if (userInputQuantity == 0) productInfo.productInfo.basic.minOrder else userInputQuantity
                atcRequestParam.setQuantity(qty)
                atcRequestParam.setWarehouseId(warehouseId)

                val expressCheckoutUriString = ApplinkConstInternalMarketplace.EXPRESS_CHECKOUT
                val intent = RouteManager.getIntent(it, expressCheckoutUriString)
                intent?.run {
                    putExtra(EXTRA_ATC_REQUEST, atcRequestParam)
                    putExtra(TRACKER_ATTRIBUTION, trackerAttribution)
                    putExtra(TRACKER_LIST_NAME, trackerListName)
                    startActivityForResult(intent, REQUEST_CODE_ATC_EXPRESS)
                    it.overridePendingTransition(R.anim.pull_up, 0)
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun addLoadMoreImpression() {
        val impressionHolder = ImpressHolder()
        title_product_desc_label.addOnImpressionListener(impressionHolder, object : ViewHintListener {
            override fun onViewHint() {
                productInfoViewModel.loadMore()
            }
        })
    }

    private fun showSnackbarClose(string: String) {
        Snackbar.make(coordinator, string, Snackbar.LENGTH_LONG).apply {
            setAction(getString(R.string.close)) { dismiss() }
            setActionTextColor(Color.WHITE)
        }.show()
    }

    private fun initializePartialView(view: View) {
        if (!::headerView.isInitialized) {
            headerView = PartialHeaderView.build(base_header, activity)
        }

        if (!::partialVariantAndRateEstView.isInitialized) {
            partialVariantAndRateEstView = PartialVariantAndRateEstView.build(base_variant)
        }

        if (!::productStatsView.isInitialized) {
            productStatsView = PartialProductStatisticView.build(base_rating_talk_courier)
        }

        if (!::productDescrView.isInitialized) {
            productDescrView = PartialProductDescrFullView.build(base_info_and_description, activity, productDetailTracking)
            addLoadMoreImpression()
        }

        if (!::actionButtonView.isInitialized) {
            actionButtonView = PartialButtonActionView.build(base_btn_action, onViewClickListener)
        }

        if (!::productShopView.isInitialized) {
            productShopView = PartialShopView.build(base_shop_view, onViewClickListener)
        }

        if (!::attributeInfoView.isInitialized)
            attributeInfoView = PartialAttributeInfoView.build(base_attribute)

        if (!::imageReviewViewView.isInitialized)
            imageReviewViewView = PartialImageReviewView.build(base_image_review, this::onSeeAllReviewClick, this::onImageReviewClick, this::onReviewClicked)

        if (!::mostHelpfulReviewView.isInitialized) {
            mostHelpfulReviewView = PartialMostHelpfulReviewView.build(base_view_most_helpful_review)
            mostHelpfulReviewView.onImageReviewClicked = this::onImagehelpfulReviewClick
        }

        if (!::latestTalkView.isInitialized)
            latestTalkView = PartialLatestTalkView.build(base_latest_talk)

        if (!::recommendationSecondView.isInitialized) {
            recommendationSecondView = PartialRecommendationSecondView.build(base_recom_2, this, productDetailTracking, activity!!)
        }

        if (!::recommendationFirstView.isInitialized) {
            recommendationFirstView = PartialRecommendationFirstView.build(base_recom_1, this, productDetailTracking, activity!!)
        }

        if (!::recommendationThirdView.isInitialized) {
            recommendationThirdView = PartialRecommendationThirdView.build(base_recom_3, this, productDetailTracking, activity!!)
        }

        if (!::recommendationFourthView.isInitialized) {
            recommendationFourthView = PartialRecommendationFourthView.build(base_recom_4, this, productDetailTracking, activity!!)
        }

        if (!::valuePropositionView.isInitialized) {
            valuePropositionView = PartialValuePropositionView.build(layout_value_proposition, onViewClickListener)
        }

    }

    private fun onImageReviewClick(imageReview: List<ImageReviewItem>, position: Int) {
        context?.let {
            val productId = productInfo?.basic?.id ?: return
            productDetailTracking.eventClickReviewOnBuyersImage(productId, imageReview[position].reviewId)
            val listOfImage: List<String> = imageReview.map {
                it.imageUrlLarge ?: ""
            }

            ImageReviewGalleryActivity.moveTo(context, ArrayList(listOfImage), position)
        }
    }

    private fun onSeeAllReviewClick() {
        context?.let {
            val productId = productInfo?.basic?.id ?: return
            productDetailTracking.eventClickReviewOnSeeAllImage(productId)
            RouteManager.route(it, ApplinkConstInternalMarketplace.IMAGE_REVIEW_GALLERY, productId.toString())
        }
    }

    private fun onImagehelpfulReviewClick(images: List<String>, pos: Int, reviewId: String?) {
        productDetailTracking.eventClickReviewOnMostHelpfulReview(productInfo?.basic?.id, reviewId)
        context?.let { ImageReviewGalleryActivity.moveTo(it, ArrayList(images), pos) }
    }

    private val onViewClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_favorite -> onShopFavoriteClick()
            R.id.send_msg_shop, R.id.btn_topchat -> onShopChatClicked()
            R.id.shop_ava, R.id.shop_name -> gotoShopDetail()
            R.id.container_ready -> onValuePropositionClick(R.id.container_ready)
            R.id.container_ori -> onValuePropositionClick(R.id.container_ori)
            R.id.container_guarantee_7_days -> onValuePropositionClick(R.id.container_guarantee_7_days)
            R.id.btn_apply_leasing -> onApplyLeasingClicked()
            else -> {
            }
        }
    }

    private fun onApplyLeasingClicked() {
        productInfo?.run {
            productDetailTracking.eventClickApplyLeasing(
                    parentProductId,
                    variant.isVariant
            )
            goToNormalCheckout(APPLY_CREDIT)
        }
    }

    private fun gotoShopDetail() {
        activity?.let {
            val shopId = productInfo?.basic?.shopID?.toString() ?: return
            startActivityForResult(RouteManager.getIntent(it,
                    ApplinkConst.SHOP, shopId),
                    REQUEST_CODE_SHOP_INFO)
        }
    }

    private fun onShopFavoriteClick() {
        val shop = shopInfo ?: return
        activity?.let {
            if (productInfoViewModel.isUserSessionActive()) {
                productShopView.toggleClickableFavoriteBtn(false)
                productInfoViewModel.toggleFavorite(shop.shopCore.shopID,
                        this::onSuccessFavoriteShop, this::onFailFavoriteShop)
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                        REQUEST_CODE_LOGIN)
            }
        }
    }

    private fun onValuePropositionClick(view: Int) {
        val title: String
        val desc: String
        val url: String
        when (view) {
            R.id.container_ready -> {
                title = getString(R.string.value_proposition_title_ready)
                desc = getString(R.string.value_proposition_desc_ready)
                url = URL_VALUE_PROPOSITION_READY
            }
            R.id.container_ori -> {
                title = getString(R.string.value_proposition_title_original)
                desc = getString(R.string.value_proposition_desc_original)
                url = URL_VALUE_PROPOSITION_ORI
            }
            R.id.container_guarantee_7_days -> {
                title = getString(R.string.value_proposition_title_guarantee_7_days)
                desc = getString(R.string.value_proposition_desc_guarantee_7_days)
                url = URL_VALUE_PROPOSITION_GUARANTEE_7_DAYS
            }
            else -> {
                title = getString(R.string.value_proposition_title_guarantee)
                desc = getString(R.string.value_proposition_desc_guarantee)
                url = URL_VALUE_PROPOSITION_GUARANTEE
            }
        }

        bottomSheet = ValuePropositionBottomSheet.newInstance(title, desc, url)
        fragmentManager?.run {
            bottomSheet.show(this, "pdp_bs")
        }
    }

    private fun isViewVisible(view: View): Boolean {
        val scrollBounds = Rect()
        nested_scroll.getDrawingRect(scrollBounds)
        val top = view.y
        val bottom = top + view.height - 100;
        return scrollBounds.top <= bottom
    }

    private fun initView() {
        initShowSearchPDP()
        varToolbar.show()
        varPictureImage.show()
        varToolbar.title = ""
        activity?.let {
            varToolbar.setBackgroundColor(ContextCompat.getColor(it, R.color.white))
            (it as AppCompatActivity).setSupportActionBar(varToolbar)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setupByConfiguration(resources.configuration)


        appbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: Int) {
                when (state) {
                    AppBarState.EXPANDED -> {
                        isAppBarCollapsed = false
                        expandedAppBar()
                    }
                    AppBarState.COLLAPSED -> {
                        isAppBarCollapsed = true
                        collapsedAppBar()
                    }
                    AppBarState.IDLE -> {
                    }
                }
            }
        })
        context?.let {
            topAdsDetailSheet = TopAdsDetailSheet.newInstance(it)
        }
    }

    private fun initShowSearchPDP() {
        search_pdp_toolbar.show()
        varToolbar = search_pdp_toolbar
        varPictureImage = view_picture_search_bar
        varPictureImage.isNotVisibleOnTheScreen(object : ViewHintListener {
            override fun onViewHint() {
                varPictureImage.stopVideo()
            }
        })

        initToolBarMethod = ::initToolbarLight
        fab_detail.setAnchor(R.id.view_picture_search_bar)
        nested_scroll.viewTreeObserver.addOnScrollChangedListener {
            activity?.run {
                if (isAdded) {
                    if (isViewVisible(varPictureImage)) {
                        showFabDetailAfterLoadData()
                        label_cod?.visibility = if (shouldShowCod && userCod && shopCod) View.VISIBLE else View.GONE
                    } else {
                        fab_detail.hide()
                        label_cod?.visibility = View.GONE
                    }
                }
            }
        }

    }

    private fun collapsedAppBar() {
        initStatusBarLight()
        initToolbarLight()
        fab_detail?.hide()
        label_cod?.visibility = if (shouldShowCod && userCod && shopCod) View.INVISIBLE else View.GONE
    }

    private fun expandedAppBar() {
        initStatusBarDark()
        initToolBarMethod()
        showFabDetailAfterLoadData()
        label_cod?.visibility = if (shouldShowCod && userCod && shopCod) View.VISIBLE else View.GONE
    }

    private fun initToolbarLight() {
        activity?.run {
            if (isAdded) {
                varToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grey_icon_light_toolbar))
                varToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
                menu?.let {
                    if (it.size() > 2) {
                        it.findItem(R.id.action_share).icon = ContextCompat.getDrawable(this, R.drawable.ic_product_share_dark)
                        val menuCart = it.findItem(R.id.action_cart)
                        menuCart.actionView.cart_image_view.tag = R.drawable.ic_product_cart_counter_dark
                        setBadgeMenuCart(menuCart)
                    }
                }

                varToolbar.overflowIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_product_more_dark)
            }
        }
    }

    private fun initStatusBarLight() {
        activity?.run {
            if (window == null) return@run
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isAdded) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.green_600)
            }
        }
    }

    private fun initStatusBarDark() {
        activity?.run {
            if (window == null) return@run
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isAdded) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.transparent_dark_40)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setupByConfiguration(newConfig)
    }

    private fun setupByConfiguration(configuration: Configuration?) {
        appbar.visibility = View.VISIBLE
        configuration?.let {
            val screenWidth = resources.displayMetrics.widthPixels
            if (it.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val height = screenWidth / 3
                varPictureImage.layoutParams.height = height
                appbar.visibility = View.VISIBLE
            } else if (it.orientation == Configuration.ORIENTATION_PORTRAIT) {
                varPictureImage.layoutParams.height = screenWidth
                appbar.visibility = View.VISIBLE
            }
        }
    }

    private fun loadProductData(forceRefresh: Boolean = false) {
        if (forceRefresh) {
            hideRecommendationView()
        }
        if (productId != null || (productKey != null && shopDomain != null)) {
            productInfoViewModel.getProductInfo(ProductParams(productId, shopDomain, productKey), forceRefresh)
            // Add new Impression after refresh for lazy load
            addLoadMoreImpression()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activity != null) {
            AdultManager.handleActivityResult(activity!!, requestCode, resultCode, data)
        }
        when (requestCode) {
            REQUEST_CODE_ETALASE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedEtalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID)
                    val selectedEtalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME)
                    if (productId != null && !selectedEtalaseName.isNullOrEmpty()) {
                        showProgressDialog(onCancelClicked = { productWarehouseViewModel.flush() })
                        productWarehouseViewModel.moveToEtalase(productId!!, selectedEtalaseId, selectedEtalaseName!!,
                                onSuccessMoveToEtalase = this::onSuccessMoveToEtalase,
                                onErrorMoveToEtalase = this::onErrorMoveToEtalase)
                    }
                }
            }
            REQUEST_CODE_MERCHANT_VOUCHER_DETAIL,
            REQUEST_CODE_MERCHANT_VOUCHER -> {
                if (resultCode == Activity.RESULT_OK) {
                    // no op //TOGGLE_MVC_OFF
                }
            }
            REQUEST_CODE_NORMAL_CHECKOUT -> {
                if (resultCode == RESULT_CODE_ERROR_TICKET && data != null) {
                    activity?.also { activity ->
                        val result = data.getParcelableExtra<AddToCartDataModel>(RESULT_TICKET_DATA)
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
                                productInfoViewModel.hitSubmitTicket(result, this@ProductDetailFragment::onErrorSubmitHelpTicket, this@ProductDetailFragment::onSuccessSubmitHelpTicket)
                            }
                            show()
                        }
                        productDetailTracking.eventViewHelpPopUpWhenAtc()
                    }
                } else if (resultCode == Activity.RESULT_OK && data != null) {
                    if (data.hasExtra(RESULT_PRODUCT_DATA_CACHE_ID)) {
                        //refresh product by selected variant/product
                        val objectId: String = data.getStringExtra(RESULT_PRODUCT_DATA_CACHE_ID)
                        val cacheManager = SaveInstanceCacheManager(this@ProductDetailFragment.context!!, objectId)
                        val selectedProductInfo: ProductInfo? = cacheManager.get(RESULT_PRODUCT_DATA, ProductInfo::class.java)
                        val selectedWarehouse: MultiOriginWarehouse? = cacheManager.get(RESULT_SELECTED_WAREHOUSE,
                                MultiOriginWarehouse::class.java)
                        if (selectedProductInfo != null) {
                            userInputVariant = data.getStringExtra(ApplinkConst.Transaction.EXTRA_SELECTED_VARIANT_ID)
                            productInfoViewModel.productInfoP1Resp.value = Success(ProductInfoP1().apply { productInfo = selectedProductInfo })
                            selectedWarehouse?.let {
                                productInfoViewModel.multiOrigin = it.warehouseInfo
                                productInfoViewModel.p2ShopDataResp.value = productInfoViewModel.p2ShopDataResp.value?.copy(
                                        nearestWarehouse = it
                                )
                            }

                        }
                    }
                    //refresh variant
                    productInfoViewModel.p2General.value?.variantResp?.run {
                        onSuccessGetProductVariantInfo(this)
                    }
                    userInputNotes = data.getStringExtra(ApplinkConst.Transaction.EXTRA_NOTES)
                    userInputQuantity = data.getIntExtra(ApplinkConst.Transaction.EXTRA_QUANTITY, 0)
                    if (data.hasExtra(ApplinkConst.Transaction.RESULT_ATC_SUCCESS_MESSAGE)) {
                        val successMessage = data.getStringExtra(ApplinkConst.Transaction.RESULT_ATC_SUCCESS_MESSAGE)
                        showAddToCartDoneBottomSheet(successMessage)
                        updateCartNotification()
                    }
                }
            }
            REQUEST_CODE_ATC_EXPRESS -> {
                if (resultCode == RESULT_CODE_ERROR && data != null) {
                    val message = data.getStringExtra(EXTRA_MESSAGES_ERROR)
                    if (message != null && message.isNotEmpty()) {
                        ToasterError.make(view, data.getStringExtra(EXTRA_MESSAGES_ERROR), BaseToaster.LENGTH_SHORT)
                                .show()
                    } else {
                        errorBottomsheets.setData(
                                getString(R.string.bottomsheet_title_global_error),
                                getString(R.string.bottomsheet_message_global_error),
                                getString(R.string.bottomsheet_action_global_error),
                                true
                        )
                        errorBottomsheets.actionListener = object : ErrorBottomsheetsActionListenerWithRetry {
                            override fun onActionButtonClicked() {
                                errorBottomsheets.dismiss()
                                goToNormalCheckout()
                            }

                            override fun onRetryClicked() {
                                errorBottomsheets.dismiss()
                                goToAtcExpress()
                            }
                        }
                        fragmentManager?.run {
                            errorBottomsheets.show(this, "")
                        }
                    }
                } else if (resultCode == RESULT_CODE_NAVIGATE_TO_OCS) {
                    goToNormalCheckout()
                } else if (resultCode == RESULT_CODE_NAVIGATE_TO_NCF) {
                    goToNormalCheckout()
                }
            }
            REQUEST_CODE_EDIT_PRODUCT -> {
                loadProductData(true)
            }
            REQUEST_CODE_LOGIN_THEN_BUY_EXPRESS -> {
                doBuy()
            }
            REQUEST_CODE_REPORT -> {
                if (resultCode == Activity.RESULT_OK)
                    showToastSuccessReport()
            }
            REQUEST_CODE_IMAGE_PREVIEW -> {
                if (data != null) {
                    isWishlisted = data.getBooleanExtra(ImagePreviewPdpActivity.RESPONSE_CODE_IMAGE_RPEVIEW, false)
                    updateWishlist(isWishlisted)
                }
            }
            else ->
                super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun onErrorSubmitHelpTicket(e: Throwable?) {
        hideProgressDialog()
        view?.also {
            Toaster.showError(it, ErrorHandler.getErrorMessage(context, e), BaseToaster.LENGTH_SHORT)
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
            view?.also {
                Toaster.showError(it, result.message, BaseToaster.LENGTH_SHORT)
            }
        }
    }

    private fun openFtInstallmentBottomSheet(installmentData: FinancingDataResponse) {

        productDetailTracking.eventClickPDPInstallmentSeeMore(productId)

        val pdpInstallmentBottomSheet = FtPDPInstallmentBottomSheet()

        context?.let {
            val cacheManager = SaveInstanceCacheManager(it, true)
            cacheManager.put(FinancingDataResponse::class.java.simpleName, installmentData, TimeUnit.HOURS.toMillis(1))
            val bundleData = Bundle()

            bundleData.putString(KEY_PDP_FINANCING_DATA, cacheManager.id!!)
            bundleData.putFloat(KEY_PDP_PRODUCT_PRICE, productInfo?.basic?.price ?: 0f)
            bundleData.putBoolean(KEY_PDP_IS_OFFICIAL, shopInfo?.goldOS?.isOfficial == 1)

            pdpInstallmentBottomSheet.arguments = bundleData
            pdpInstallmentBottomSheet.show(childFragmentManager, "FT_TAG")
        }

    }

    private fun showAddToCartDoneBottomSheet(successMessage: String) {
        productInfo?.let {
            val addToCartDoneBottomSheet = AddToCartDoneBottomSheet()
            val productName = it.basic.name
            val productImageUrl = it.firstThumbnailPicture
            val addedProductDataModel = AddToCartDoneAddedProductDataModel(
                    it.basic.id.toString(),
                    productName,
                    productImageUrl,
                    it.variant.isVariant,
                    it.freeOngkir.isFreeOngkirActive,
                    it.freeOngkir.freeOngkirImgUrl
            )
            val bundleData = Bundle()
            bundleData.putParcelable(KEY_ADDED_PRODUCT_DATA_MODEL, addedProductDataModel)
            addToCartDoneBottomSheet.arguments = bundleData
            addToCartDoneBottomSheet.setDismissListener {
                shouldShowCartAnimation = true
                updateCartNotification()
            }
            fragmentManager?.run {
                addToCartDoneBottomSheet.show(this, "TAG")
            }
        }
    }

    override fun onDestroy() {
        productInfoViewModel.productInfoP1Resp.removeObservers(this)
        productInfoViewModel.p2ShopDataResp.removeObservers(this)
        productInfoViewModel.p2General.removeObservers(this)
        productInfoViewModel.p2Login.removeObservers(this)
        productInfoViewModel.productInfoP3resp.removeObservers(this)
        productInfoViewModel.loadTopAdsProduct.removeObservers(this)
        productInfoViewModel.flush()
        productWarehouseViewModel.flush()
        super.onDestroy()
    }

    private fun updateCartNotification() {
        productInfoViewModel.updateCartCounerUseCase(::onSuccessUpdateCartCounter)
    }

    private fun onSuccessUpdateCartCounter(count: Int) {
        val cache = LocalCacheHandler(context, CartConstant.CART);
        cache.putInt(CartConstant.IS_HAS_CART, if (count > 0) 1 else 0)
        cache.putInt(CartConstant.CACHE_TOTAL_CART, count);
        cache.applyEditor();
        if (isAdded) {
            if (isAppBarCollapsed) {
                initToolbarLight()
            } else {
                initToolBarMethod()
            }
        }
    }

    private fun renderProductInfoP2Login(p2Login: ProductInfoP2Login) {
        shopInfo?.let { updateWishlist(it, p2Login.isWishlisted) }
        p2Login.pdpAffiliate?.let { renderAffiliate(it) }
        isWishlisted = p2Login.isWishlisted
        actionButtonView.renderData(p2Login.isExpressCheckoutType)
    }


    private fun renderProductInfo3(productInfoP3: ProductInfoP3) {
        userCod = productInfoP3.userCod
        if (shouldShowCod && shopCod && productInfoP3.userCod) label_cod.visible() else label_cod.gone()
        headerView.renderCod(shouldShowCod && shopCod && productInfoP3.userCod)
    }

    private fun renderAffiliate(pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate) {
        if (isAffiliate) {
            base_btn_affiliate.visible()
            loadingAffiliate.gone()
            getCommission.visible()
            commission.visible()
            commission.text = pdpAffiliate.commissionValueDisplay
            btn_affiliate.setOnClickListener { onAffiliateClick(pdpAffiliate, false) }
            actionButtonView.gone()
        } else if (!GlobalConfig.isSellerApp()) {
            base_btn_affiliate.gone()
            actionButtonView.byMeClick = this::onAffiliateClick
            actionButtonView.showByMe(true, pdpAffiliate)
            actionButtonView.visibility = shopInfo?.statusInfo?.shopStatus == 1
        }
    }

    private fun onAffiliateClick(pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate,
                                 isRegularPdp: Boolean) {
        if (productInfo == null) return
        activity?.let {
            productDetailTracking.eventClickAffiliate(productInfoViewModel.userId, productInfo!!.basic.shopID,
                    pdpAffiliate.productId.toString(), isRegularPdp)
            if (productInfoViewModel.isUserSessionActive()) {
                RouteManager.getIntent(it,
                        ApplinkConst.AFFILIATE_CREATE_POST,
                        pdpAffiliate.productId.toString(),
                        pdpAffiliate.adId.toString())
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .let(::startActivity)
                it.setResult(Activity.RESULT_OK)
                it.finish()
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                        REQUEST_CODE_LOGIN)
            }
        }
    }

    private fun onShipmentClicked() {
        productDetailTracking.eventShippingClicked(productId ?: "")
        if (productInfoViewModel.isUserSessionActive()) {
            val productP3value = productInfoViewModel.productInfoP3resp.value
            if (!productP3value?.ratesModel?.services.isNullOrEmpty()) {
                gotoRateEstimation()
            } else {
                goToCourier()
            }
        } else {
            goToCourier()
        }
    }

    private fun goToCourier() {
        activity?.let {
            if (productInfo != null && shopInfo != null) {
                startActivity(CourierActivity.createIntent(it,
                        productInfo?.basic?.id?.toString() ?: "",
                        shopInfo?.shipments ?: listOf(),
                        shopInfo?.bbInfo ?: listOf()
                ))
            }
        }
    }

    private fun gotoRateEstimation() {
        productInfo?.let { productInfo ->
            shopInfo?.let { shopInfo ->
                context?.let { context ->
                    startActivity(RatesEstimationDetailActivity.createIntent(
                            context,
                            shopInfo.shopCore.domain,
                            productInfo.basic.weight,
                            productInfo.basic.weightUnit,
                            if (productInfoViewModel.multiOrigin.isFulfillment)
                                productInfoViewModel.multiOrigin.origin else null,
                            productInfo.freeOngkir.isFreeOngkirActive
                    ))
                }
            }
        }
    }

    private fun renderProductInfo2Shop(p2ShopData: ProductInfoP2ShopData) {
        p2ShopData.shopInfo?.let { shopInfo ->
            this.shopInfo = shopInfo
            productDescrView.shopInfo = shopInfo
            productShopView.renderShop(shopInfo, productInfoViewModel.isShopOwner(shopInfo.shopCore.shopID.toIntOrZero()))
            val data = productInfo ?: return

            actionButtonView.renderData(!data.basic.isActive(),
                    (productInfoViewModel.isShopOwner(data.basic.shopID)
                            || shopInfo.allowManage), (hasTopAds()),
                    data.preorder)
            actionButtonView.visibility = !isAffiliate && shopInfo.statusInfo.shopStatus == 1
            headerView.showOfficialStore(shopInfo.goldOS)
            valuePropositionView.renderData(shopInfo.goldOS)
            varPictureImage.renderShopStatus(shopInfo, productInfo?.basic?.status
                    ?: ProductStatusTypeDef.ACTIVE)
            activity?.let {
                productStatsView.renderClickShipping(it, ::onShipmentClicked)
            }
            productDetailTracking.sendScreen(
                    shopInfo.shopCore.shopID,
                    shopInfo.goldOS.shopTypeString,
                    productId ?: "")


            if (delegateTradeInTracking) {
                trackProductView(tradeInParams.isEligible == 1)
                delegateTradeInTracking = false
            }

            productDetailTracking.sendMoEngageOpenProduct(data,
                    shopInfo.goldOS.isOfficial == 1, shopInfo.shopCore.name)
            productDetailTracking.eventAppsFylerOpenProduct(data)
        }
        shopCod = p2ShopData.shopCod
        partialVariantAndRateEstView.renderFulfillment(p2ShopData.nearestWarehouse.warehouseInfo.isFulfillment)
        if (productInfo != null && p2ShopData.nearestWarehouse.warehouseInfo.id.isNotBlank())
            headerView.updateStockAndPriceWarehouse(p2ShopData.nearestWarehouse, productInfo!!.campaign)
    }

    private fun hasTopAds() =
            topAdsGetProductManage.data.adId.isNotEmpty() && !topAdsGetProductManage.data.adId.equals("0")

    private fun renderProductInfo2(productInfoP2: ProductInfoP2General) {
        productDescrView.productSpecificationResponse = productInfoP2.productSpecificationResponse

        if (productInfoP2.vouchers.isNotEmpty()) {
            merchantVoucherListWidget.setData(ArrayList(productInfoP2.vouchers))
            merchantVoucherListWidget.visible()
            if (!productInfoViewModel.isUserSessionActive() || !productInfoViewModel.isShopOwner(productInfo?.basic?.shopID
                            ?: 0)) {
                productDetailTracking.eventImpressionMerchantVoucherUse(productInfoP2.vouchers)
            }
        } else {
            merchantVoucherListWidget.gone()
        }

        productInfoP2.productFinancingRecommendationData.let {
            if (it.response.data.partnerCode.isNotBlank()) {
                iv_ovo_installment_icon.show()
                iv_arrow_next.show()

                ImageHandler.loadImage(context, iv_ovo_installment_icon, it.response.data.partnerIcon, R.drawable.ic_loading_image)

                iv_ovo_installment_icon.setOnClickListener {
                    openFtInstallmentBottomSheet(productInfoP2.productFinancingCalculationData)
                }

                iv_arrow_next.setOnClickListener {
                    openFtInstallmentBottomSheet(productInfoP2.productFinancingCalculationData)
                }

                label_installment.visible()
                label_installment.text = String.format(getString(R.string.new_installment_template),
                        CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                (if (shopInfo?.goldOS?.isOfficial == 1) it.response.data.osMonthlyPrice
                                else it.response.data.monthlyPrice).roundToLong(), false))

                label_desc_installment.text = getString(R.string.pdp_installment_desc)
                label_desc_installment.visible()

                label_desc_installment.setOnClickListener {
                    activity?.let {
                        openFtInstallmentBottomSheet(productInfoP2.productFinancingCalculationData)
                    }
                }

                if (label_min_wholesale.isVisible) {
                    wholesale_divider.visible()
                } else {
                    wholesale_divider.gone()
                }
                base_view_wholesale.visible()
            } else {
                label_installment.hide()
                label_desc_installment.hide()
                iv_ovo_installment_icon.hide()
                iv_arrow_next.hide()
                wholesale_divider.gone()
            }
        }

        productShopView.renderShopFeature(productInfoP2.shopFeature)
        productInfoP2.shopBadge?.let { productShopView.renderShopBadge(it) }
        productStatsView.renderRating(productInfoP2.rating)
        attributeInfoView.renderWishlistCount(productInfoP2.wishlistCount.count)
        partialVariantAndRateEstView.renderPriorityOrder(productInfoP2.shopCommitment)
        imageReviewViewView.renderData(productInfoP2)
        mostHelpfulReviewView.renderData(productInfoP2.helpfulReviews)
        latestTalkView.renderData(productInfoP2.latestTalk, productInfo?.stats?.countTalk ?: 0,
                productInfo?.basic?.shopID ?: 0, this::onDiscussionClicked)


        partialVariantAndRateEstView.renderPurchaseProtectionData(productInfoP2.productPurchaseProtectionInfo)
        productInfo?.run {
            productDetailTracking.eventBranchItemView(this, (UserSession(activity)).userId)
        }

    }


    private fun updateWishlist(shopInfo: ShopInfo, wishlisted: Boolean) {
        context?.let {
            fab_detail.show()
            if (shopInfo.isAllowManage == 1) {
                fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_edit))
            } else {
                updateWishlist(wishlisted)
            }
        }
    }

    private fun updateWishlist(wishlisted: Boolean) {
        context?.let {
            if (wishlisted) {
                fab_detail.hide()
                fab_detail.isActivated = true
                fab_detail.setImageDrawable(MethodChecker.getDrawable(it, R.drawable.ic_wishlist_checked))
                fab_detail.show()
            } else {
                fab_detail.hide()
                fab_detail.isActivated = false
                fab_detail.setImageDrawable(MethodChecker.getDrawable(it, R.drawable.ic_wishlist_unchecked))
                fab_detail.show()
            }
            isWishlisted = wishlisted
        }
    }

    private fun showFabDetailAfterLoadData() {
        // show wishlist (or edit product) button if
        // 1. getProduct api is finished, but user is not logged in
        // 2. getWishlist (P3) is success
        // 3. user is allowed to manage product
        if ((!productInfoViewModel.isUserSessionActive() &&
                        productInfoViewModel.productInfoP1Resp.value != null) ||
                productInfoViewModel.productInfoP3resp.value != null ||
                (shopInfo != null && shopInfo?.isAllowManage == 1)) {
            fab_detail?.show()
        }
    }

    private fun gotoEditProduct() {
        val id = productInfo?.parentProductId ?: return
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_EDIT_ITEM, id)
            intent?.run { startActivityForResult(this, REQUEST_CODE_EDIT_PRODUCT) }
        }
    }

    private fun onErrorGetProductInfo(throwable: Throwable) {
        context?.let { ToasterError.make(coordinator, ProductDetailErrorHandler.getErrorMessage(it, throwable)).show() }
    }

    private fun onSuccessGetProductInfo(productInfoP1: ProductInfoP1) {
        val data = productInfoP1.productInfo
        productId = data.basic.id.toString()
        productInfo = data
        et_search.hint = String.format(getString(R.string.pdp_search_hint), productInfo?.category?.name)
        topAdsGetProductManage = productInfoP1.topAdsGetProductManage
        shouldShowCod = data.shouldShowCod
        isLeasing = data.basic.isLeasing
        headerView.renderData(data)
        varPictureImage.renderData(data.media, this::onPictureProductClicked, this::onSwipePicture, childFragmentManager)
        productStatsView.renderData(data, this::onReviewClicked, this::onDiscussionClicked)
        productDescrView.renderData(data)
        attributeInfoView.renderData(data)
        txt_last_update.text = getString(R.string.template_last_update_price, data.basic.lastUpdatePrice)
        txt_last_update.visible()

        if (data.hasWholesale) {
            val minPrice = data.wholesale?.minBy { it.price }?.price ?: return
            label_min_wholesale.text = getString(R.string.label_format_wholesale, minPrice.getCurrencyFormatted())
            label_wholesale.visibility = View.VISIBLE
            label_min_wholesale.visibility = View.VISIBLE
            val onWholeSaleClick = { v: View ->
                val wholesaleList = data.wholesale
                if (wholesaleList != null && wholesaleList.isNotEmpty()) {
                    context?.run {
                        startActivity(WholesaleActivity.getIntent(this,
                                (wholesaleList as ArrayList<Wholesale>)))
                    }
                }
            }
            label_wholesale.setOnClickListener { onWholeSaleClick.invoke(it) }
            label_min_wholesale.setOnClickListener { onWholeSaleClick.invoke(it) }
            base_view_wholesale.visibility = View.VISIBLE
        } else {
            label_wholesale.visibility = View.GONE
            label_min_wholesale.visibility = View.GONE
            if (label_desc_installment.isVisible) base_view_wholesale.visibility = View.VISIBLE
            else base_view_wholesale.gone()
        }

        if (!productInfoViewModel.isUserSessionActive() || !productInfoViewModel.isUserHasShop) {
            open_shop.visible()
        } else {
            open_shop.gone()
        }

        if (data.category.isAdult) {
            AdultManager.showAdultPopUp(this, AdultManager.ORIGIN_PDP, productId ?: "")
        }

        var isHandPhone = false
        var categoryId = 0
        productInfoP1.productInfo.category.detail.forEach { detail: Category.Detail ->
            if (detail.name.equals("Handphone")) {
                isHandPhone = true
                val handfone = 24
                categoryId = if (detail.id.isNotEmpty())
                    detail.id.toIntOrZero()
                else
                    handfone

            }
        }
        tradeInParams = TradeInParams()
        tradeInParams.categoryId = productInfoP1.productInfo.category.id.toIntOrZero()
        tradeInParams.deviceId = (activity?.application as ProductDetailRouter).getDeviceId(activity as Context)
        tradeInParams.userId = if (productInfoViewModel.userId.isNotEmpty())
            productInfoViewModel.userId.toIntOrZero()
        else
            0
        tradeInParams.setPrice(productInfoP1.productInfo.basic.price.toInt())
        tradeInParams.productId = productInfoP1.productInfo.basic.id
        tradeInParams.shopId = productInfoP1.productInfo.basic.shopID
        tradeInParams.productName = productInfoP1.productInfo.basic.name
        val preorderstatus = productInfoP1.productInfo.isPreorderActive
        if (preorderstatus)
            tradeInParams.isPreorder = preorderstatus
        else
            tradeInParams.isPreorder = false
        tradeInParams.isOnCampaign = productInfoP1.productInfo.campaign.isActive
        tv_trade_in.tradeInReceiver.checkTradeIn(tradeInParams, false, activity?.application)
        tv_trade_in.setOnClickListener {
            goToNormalCheckout(TRADEIN_BUY)
            tradeInParams?.let {
                if (tradeInParams.usedPrice > 0)
                    productDetailTracking.sendGeneralEvent(" clickPDP",
                            "product detail page",
                            "click trade in widget",
                            "after diagnostic")
                else
                    productDetailTracking.sendGeneralEvent(" clickPDP",
                            "product detail page",
                            "click trade in widget",
                            "before diagnostic")

            }
        }

        // if when first time and the product is actually a variant product, then select the default variant
        if (userInputVariant == null && data.variant.isVariant && data.variant.parentID != productId) {
            userInputVariant = productId
        }
        actionButtonView.isLeasing = isLeasing
        if (affiliateString.hasValue()) {
            productInfoViewModel.hitAffiliateTracker(affiliateString
                    ?: "", productInfoViewModel.deviceId)
        }
        actionButtonView.renderData(!data.basic.isActive(),
                (productInfoViewModel.isShopOwner(data.basic.shopID)
                        || shopInfo?.allowManage == true), hasTopAds(),
                data.preorder)
        actionButtonView.visibility = !isAffiliate
        activity?.invalidateOptionsMenu()
    }

    private fun onErrorGetProductVariantInfo() {
        //variant error, do not show variant, but still can buy the product
        partialVariantAndRateEstView.renderData(null, "", this::onVariantClicked)
    }

    private fun onSuccessGetProductVariantInfo(data: ProductVariant?) {
        if (data == null || !data.hasChildren) {
            partialVariantAndRateEstView.renderData(null, "", this::onVariantClicked)
            return
        }
        // defaulting selecting variant
        if (userInputVariant == data.parentId.toString() && data.defaultChild > 0) {
            userInputVariant = data.defaultChild.toString()
        }
        val selectedVariantListString = data.getOptionListString(userInputVariant)?.joinToString(separator = ", ")
                ?: ""
        partialVariantAndRateEstView.renderData(data, selectedVariantListString, this::onVariantClicked)
    }

    private fun onSuccessWarehouseProduct() {
        hideProgressDialog()
        showToastSuccess(getString(R.string.success_warehousing_product))
        loadProductData(true)
    }

    private fun onSuccessFavoriteShop(isSuccess: Boolean) {
        val favorite = shopInfo?.favoriteData ?: return
        if (isSuccess) {
            val newFavorite =
                    if (favorite.alreadyFavorited == 1)
                        ShopInfo.FavoriteData(0, favorite.totalFavorite - 1)
                    else
                        ShopInfo.FavoriteData(1, favorite.totalFavorite + 1)
            shopInfo = shopInfo?.copy(favoriteData = newFavorite)
            productShopView.updateFavorite(favorite.alreadyFavorited != 1)
            productShopView.toggleClickableFavoriteBtn(true)
        }
    }

    private fun onFailFavoriteShop(t: Throwable) {
        context?.let {
            ToasterError.make(view, ProductDetailErrorHandler.getErrorMessage(it, t))
                    .setAction(R.string.retry_label) { onShopFavoriteClick() }
        }
        productShopView.toggleClickableFavoriteBtn(true)
    }

    @SuppressLint("Range")
    private fun onErrorWarehouseProduct(throwable: Throwable) {
        hideProgressDialog()
        showToastError(throwable)
    }

    private fun showToastError(throwable: Throwable) {
        activity?.run {
            ToasterError.make(findViewById(android.R.id.content),
                    ProductDetailErrorHandler.getErrorMessage(this, throwable),
                    ToasterError.LENGTH_LONG)
                    .show()
        }
    }

    private fun showToastSuccess(message: String) {
        activity?.run {
            ToasterNormal.make(findViewById(android.R.id.content),
                    message,
                    ToasterNormal.LENGTH_LONG)
                    .show()
        }
    }

    private fun showToastSuccessReport() {
        activity?.run {
            Toaster.showNormal(findViewById(android.R.id.content),
                    getString(R.string.success_to_report), Snackbar.LENGTH_LONG)
        }
    }

    /**
     * Event than happen after owner successfully move the warehoused product back to etalase
     */
    private fun onSuccessMoveToEtalase() {
        hideProgressDialog()
        showToastSuccess(getString(R.string.success_move_etalase))
        loadProductData(true)
    }

    private fun onErrorMoveToEtalase(throwable: Throwable) {
        hideProgressDialog()
        showToastError(throwable)
    }

    private fun onErrorRemoveWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    private fun onSuccessRemoveWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_remove_wishlist))
        productInfoViewModel.p2Login.value?.isWishlisted = false
        isWishlisted = false
        updateWishlist(false)
        //TODO clear cache
        sendIntentResusltWishlistChange(productId ?: "", false)

    }

    private fun sendIntentResusltWishlistChange(productId: String, isInWishlist: Boolean) {
        val resultIntent = Intent()
                .putExtra(WISHLIST_STATUS_UPDATED_POSITION, activity?.intent?.getIntExtra(WISHLIST_STATUS_UPDATED_POSITION, -1))
        resultIntent.putExtra(WIHSLIST_STATUS_IS_WISHLIST, isInWishlist)
        resultIntent.putExtra("product_id", productId)
        activity!!.setResult(Activity.RESULT_CANCELED, resultIntent)
    }

    private fun onErrorAddWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    private fun onSuccessAddWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_add_wishlist))
        productInfoViewModel.p2Login.value?.isWishlisted = true
        isWishlisted = true
        updateWishlist(true)
        productDetailTracking.eventBranchAddToWishlist(productInfo, (UserSession(activity)).userId)
        //TODO clear cache
        sendIntentResusltWishlistChange(productId ?: "", true)
    }

    private fun onDiscussionClicked() {
        activity?.let {
            val intent = RouteManager.getIntent(it,
                    ApplinkConst.PRODUCT_TALK, productInfo?.basic?.id.toString())
            startActivityForResult(intent, REQUEST_CODE_TALK_PRODUCT)
        }
        productInfo?.run {
            productDetailTracking.eventDiscussionClickedIris(this, deeplinkUrl, (shopInfo?.goldOS?.isOfficial
                    ?: 0) > 0, shopInfo?.shopCore?.name ?: "")
            productDetailTracking.sendMoEngageClickDiskusi(this,
                    (shopInfo?.goldOS?.isOfficial ?: 0) > 0,
                    shopInfo?.shopCore?.name ?: "")
        }
    }

    private fun onReviewClicked() {
        productInfo?.run {
            productDetailTracking.eventReviewClickedIris(this, deeplinkUrl, shopInfo?.goldOS?.isOfficial == 1, shopInfo?.shopCore?.name ?: "")
            productDetailTracking.sendMoEngageClickReview(this, shopInfo?.goldOS?.isOfficial == 1, shopInfo?.shopCore?.name
                    ?: "")
            context?.let {
                val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.PRODUCT_REVIEW, productInfo!!.basic.id.toString())
                intent?.run {
                    intent.putExtra("x_prd_nm", productInfo!!.basic.name)
                    startActivity(intent)
                }
            }
        }
    }

    private fun onShopChatClicked() {
        val shop = shopInfo ?: return
        val product = productInfo ?: return
        activity?.let {
            if (productInfoViewModel.isUserSessionActive()) {
                val intent = RouteManager.getIntent(it,
                        ApplinkConst.TOPCHAT_ASKSELLER,
                        shop.shopCore.shopID, "",
                        "product", shop.shopCore.name, shop.shopAssets.avatar)
                productInfoViewModel.putChatProductInfoTo(intent, productId, productInfo, userInputVariant)
                startActivity(intent)
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                        REQUEST_CODE_LOGIN)
            }
        }
        productDetailTracking.eventSendMessage()
    }

    /**
     * go to preview image activity to show larger image of Product
     */
    private fun onPictureProductClicked(position: Int) {
        productDetailTracking.eventProductImageClicked(productId)
        activity?.let {
            val intent = ImagePreviewPdpActivity.createIntent(it, productId
                    ?: "", isWishlisted, getImageURIPaths(), null, position)
            startActivityForResult(intent, REQUEST_CODE_IMAGE_PREVIEW)
        }
    }

    private fun onSwipePicture(swipeDirection: String) {
        productDetailTracking.eventProductImageOnSwipe(productId, swipeDirection)
    }

    private fun getImageURIPaths(): ArrayList<String> {
        return ArrayList(productInfo?.run {
            media.map {
                if (it.type == "image") {
                    it.urlOriginal
                } else {
                    it.urlThumbnail
                }
            }
        } ?: listOf())
    }

    private fun onVariantClicked() {
        productDetailTracking.eventClickVariant(generateVariantString(), productId ?: "")
        goToNormalCheckout(ATC_AND_BUY)
    }

    private fun generateVariantString(): String {
        return try {
            productInfoViewModel.p2General.value?.variantResp?.variant?.map { it.name }?.joinToString(separator = ", ")
                    ?: ""
        } catch (e: Throwable) {
            ""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(if (isAppBarCollapsed) R.menu.menu_product_detail_dark else
            R.menu.menu_product_detail_light, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
        initToolBarMethod()

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        activity?.let {
            handlingMenuPreparation(menu)
            // handling cart counter
        }
    }

    private fun handlingMenuPreparation(menu: Menu?) {
        if (menu == null) return

        val menuShare = menu.findItem(R.id.action_share)
        val menuCart = menu.findItem(R.id.action_cart)
        val menuReport = menu.findItem(R.id.action_report)
        val menuWarehouse = menu.findItem(R.id.action_warehouse)
        val menuEtalase = menu.findItem(R.id.action_etalase)

        if (productInfo == null) {
            menuShare.isVisible = false
            menuShare.isEnabled = false
            menuCart.isVisible = false
            menuCart.isEnabled = false
            menuReport.isVisible = false
            menuReport.isEnabled = false
            menuWarehouse.isVisible = false
            menuWarehouse.isEnabled = false
            menuEtalase.isEnabled = false
            menuEtalase.isVisible = false
        } else {
            menuShare.isVisible = true
            menuShare.isEnabled = true

            val isOwned = productInfoViewModel.isShopOwner(productInfo!!.basic.shopID)
            val isSellerApp = GlobalConfig.isSellerApp()

            val isValidCustomer = !isOwned && !isSellerApp
            menuCart.isVisible = isValidCustomer
            menuCart.isEnabled = isValidCustomer

            if (isValidCustomer) setBadgeMenuCart(menuCart)

            menuReport.isVisible = !isOwned && (productInfo!!.basic.status != ProductStatusTypeDef.WAREHOUSE)
            menuReport.isEnabled = !isOwned && (productInfo!!.basic.status != ProductStatusTypeDef.WAREHOUSE)
            menuWarehouse.isVisible = isOwned && (productInfo!!.basic.status !in arrayOf(ProductStatusTypeDef.WAREHOUSE, ProductStatusTypeDef.PENDING))
            menuWarehouse.isEnabled = isOwned && (productInfo!!.basic.status !in arrayOf(ProductStatusTypeDef.WAREHOUSE, ProductStatusTypeDef.PENDING))
            menuEtalase.isVisible = isOwned && (productInfo!!.basic.status !in arrayOf(ProductStatusTypeDef.ACTIVE, ProductStatusTypeDef.PENDING))
            menuEtalase.isEnabled = isOwned && (productInfo!!.basic.status !in arrayOf(ProductStatusTypeDef.ACTIVE, ProductStatusTypeDef.PENDING))
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

    private fun showBadgeMenuCart(cartImageView: ImageView, lottieCartView: LottieAnimationView, animate: Boolean) {
        activity?.run {
            val localCacheHandler = LocalCacheHandler(context, CartConstant.CART)
            val cartCount = localCacheHandler.getInt(CartConstant.CACHE_TOTAL_CART, 0)

            val icon = ContextCompat.getDrawable(this, cartImageView.tag as Int)
            if (icon is LayerDrawable) {
                val badge = CountDrawable(this)
                badge.setCount(if (cartCount > CART_MAX_COUNT) {
                    getString(R.string.pdp_label_cart_count_max)
                } else {
                    cartCount.toString()
                })
                icon.mutate()
                icon.setDrawableByLayerId(R.id.ic_cart_count, badge)
                cartImageView.setImageDrawable(icon)
                if (animate) {
                    val alphaAnimation = AlphaAnimation(CART_ALPHA_ANIMATION_FROM, CART_ALPHA_ANIMATION_TO)
                    val scaleAnimation = ScaleAnimation(CART_SCALE_ANIMATION_FROM, CART_SCALE_ANIMATION_TO, CART_SCALE_ANIMATION_FROM, CART_SCALE_ANIMATION_TO, Animation.RELATIVE_TO_SELF, CART_SCALE_ANIMATION_PIVOT, Animation.RELATIVE_TO_SELF, CART_SCALE_ANIMATION_PIVOT)
                    scaleAnimation.fillAfter = false
                    val animationSet = AnimationSet(false)
                    animationSet.addAnimation(alphaAnimation)
                    animationSet.addAnimation(scaleAnimation)
                    animationSet.duration = CART_ANIMATION_DURATION
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed(); true
            }
            R.id.action_share -> {
                shareProduct(); true
            }
            R.id.action_cart -> {
                gotoCart(); true
            }
            R.id.action_report -> {
                reportProduct(); true
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

    private fun moveProductToEtalase() {
        context?.run {
            val shopId = productInfo?.basic?.shopID.toString()
            if (shopId.isNotEmpty()) {
                val etalaseId = productInfo?.menu?.id ?: ""
                val shopEtalasePickerIntent = ShopEtalasePickerActivity.createIntent(this,
                        shopId, etalaseId, false, true)
                startActivityForResult(shopEtalasePickerIntent, REQUEST_CODE_ETALASE)
            }
        }
    }

    private fun warehouseProduct() {
        productId?.let {
            showProgressDialog(onCancelClicked = { productWarehouseViewModel.flush() })
            productWarehouseViewModel.moveToWarehouse(it,
                    onSuccessMoveToWarehouse = this::onSuccessWarehouseProduct,
                    onErrorMoveToWarehouse = this::onErrorWarehouseProduct)
        }
    }

    private fun reportProduct() {
        productInfo?.run {
            if (productInfoViewModel.isUserSessionActive()) {
                context?.let {
                    val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.REPORT_PRODUCT,
                            basic.id.toString())
                    startActivityForResult(intent, REQUEST_CODE_REPORT)
                }

                productDetailTracking.eventReportLogin()
            } else {
                context?.run {
                    startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                            REQUEST_CODE_LOGIN)
                }
                productDetailTracking.eventReportNoLogin()
            }
        }

    }

    private fun gotoCart() {
        activity?.let {
            if (productInfoViewModel.isUserSessionActive()) {
                startActivity(RouteManager.getIntent(it, ApplinkConst.CART))
            } else {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                        REQUEST_CODE_LOGIN)
            }
            productDetailTracking.eventCartMenuClicked(generateVariantString(), productId ?: "")
        }
    }

    private fun shareProduct() {
        if (productInfo == null && activity == null) return
        productId?.let {
            productDetailTracking.eventClickPdpShare(it)
        }
        val productData = ProductData(
                productInfo!!.basic.price.getCurrencyFormatted(),
                "${productInfo!!.cashback.percentage}%",
                MethodChecker.fromHtml(productInfo!!.basic.name).toString(),
                productInfo!!.basic.priceCurrency,
                productInfo!!.basic.url,
                shopInfo?.shopCore?.url ?: "",
                shopInfo?.shopCore?.name ?: "",
                productInfo!!.basic.id.toString(),
                productInfo!!.pictures?.getOrNull(0)?.urlOriginal ?: ""
        )
        checkAndExecuteReferralAction(productData)
    }

    private fun checkAndExecuteReferralAction(productData: ProductData) {
        val userSession = UserSession(activity)
        val remoteConfig = FirebaseRemoteConfigImpl(context)

        val fireBaseRemoteMsgGuest = remoteConfig.getString(RemoteConfigKey.fireBaseGuestShareMsgKey, "")
        if (!TextUtils.isEmpty(fireBaseRemoteMsgGuest)) productData.productShareDescription = fireBaseRemoteMsgGuest

        if (userSession.isLoggedIn && userSession.isMsisdnVerified) {
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
                    productDetailTracking.sendMoEngagePDPReferralCodeShareEvent()
                }
                executeProductShare(productData)
            }

            override fun actionError(actionId: Int, dataObj: Int?) {
                executeProductShare(productData)
            }
        }
        val referralAction = ReferralAction<Context, String, Int, String, String, String, Context>()
        referralAction.doAction(ACTION_GET_REFERRAL_CODE, context, actionCreator, object : ActionUIDelegate<String, String> {
            override fun waitForResult(actionId: Int, dataObj: String?) {
                showProgressDialog()
            }

            override fun stopWaiting(actionId: Int, dataObj: String?) {
                hideProgressDialog()
            }
        })

    }

    private fun executeProductShare(productData: ProductData) {
        val productShare = ProductShare(activity!!, ProductShare.MODE_TEXT)
        productShare.share(productData, {
            showProgressLoading()
        }, {
            hideProgressLoading()
        })

    }

    private fun trackProductView(isElligible: Boolean) {
        if (productInfo != null && shopInfo != null) {

            productDetailTracking.eventEnhanceEcommerceProductDetail(trackerListName, productInfo, shopInfo, trackerAttribution,
                    isElligible, tradeInParams.usedPrice > 0, productInfoViewModel.multiOrigin.isFulfillment, deeplinkUrl)
        } else if (shopInfo == null) {
            delegateTradeInTracking = true
        }

    }

    private fun hideProgressLoading() {
        pb_loading.gone()
    }

    private fun hideProgressDialog() {
        loadingProgressDialog?.dismiss()
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

    private fun showProgressLoading() {
        pb_loading.visible()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_NOTE, userInputNotes)
        outState.putInt(SAVED_QUANTITY, userInputQuantity)
        outState.putString(SAVED_VARIANT, userInputVariant)
    }

    override fun onStop() {
        super.onStop()
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(tradeInBroadcastReceiver)
        }
    }

    private fun updateStickyContent() {
        productInfoViewModel.getStickyLoginContent(
                onSuccess = {
                    this.tickerDetail = it
                    updateStickyState()
                    updateActionBarBackground()
                },
                onError = {
                    stickyLoginView.hide()
                }
        )
    }

    private fun updateStickyState() {
        if (this.tickerDetail == null) {
            stickyLoginView.hide()
            return
        }

        val isCanShowing = remoteConfig.getBoolean(StickyLoginConstant.REMOTE_CONFIG_FOR_PDP, true)
        if (!isCanShowing) {
            stickyLoginView.hide()
            return
        }

        val userSession = UserSession(activity)
        if (userSession.isLoggedIn) {
            stickyLoginView.hide()
            return
        }

        this.tickerDetail?.let { stickyLoginView.setContent(it) }
        stickyLoginView.show(StickyLoginConstant.Page.PDP)
        stickyLoginView.tracker.viewOnPage(StickyLoginConstant.Page.PDP)
    }

    private fun updateActionBarBackground() {
        val tv = TypedValue()
        var paddingBottom = 0
        val theme = context?.theme
        if (theme != null) {
            if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                paddingBottom = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            }
        }

        if (stickyLoginView.isShowing()) {
            actionButtonView.setBackground(R.color.white)
            nested_scroll.setPadding(0, 0, 0, paddingBottom + stickyLoginView.height)
        } else {
            nested_scroll.setPadding(0, 0, 0, paddingBottom)
            val drawable = context?.let { _context -> ContextCompat.getDrawable(_context, R.drawable.bg_shadow_top) }
            drawable?.let { actionButtonView.setBackground(it) }
        }
    }

    override val isAllowLoadMore: Boolean
        get() = false

    override fun onRequestLoadMore(page: Int) {
    }

    override fun onButtonBackPressed() {
    }
}
