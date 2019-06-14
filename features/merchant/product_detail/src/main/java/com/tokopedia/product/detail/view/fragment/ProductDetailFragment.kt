package com.tokopedia.product.detail.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.util.ArrayMap
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.*
import com.tokopedia.abstraction.Actions.interfaces.ActionCreator
import com.tokopedia.abstraction.Actions.interfaces.ActionUIDelegate
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.abstraction.common.utils.FindAndReplaceHelper
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.design.widget.ObservableNestedScrollView
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheets
import com.tokopedia.expresscheckout.common.view.errorview.ErrorBottomsheetsActionListenerWithRetry
import com.tokopedia.gallery.ImageReviewGalleryActivity
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.createDefaultProgressDialog
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.normalcheckout.constant.ATC_AND_BUY
import com.tokopedia.normalcheckout.constant.ATC_ONLY
import com.tokopedia.normalcheckout.constant.ProductAction
import com.tokopedia.normalcheckout.constant.TRADEIN_BUY
import com.tokopedia.normalcheckout.view.NormalCheckoutActivity
import com.tokopedia.normalcheckout.view.NormalCheckoutFragment
import com.tokopedia.product.detail.ProductDetailRouter
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.product.Category
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.product.Wholesale
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse
import com.tokopedia.product.detail.data.model.ProductInfoP1
import com.tokopedia.product.detail.data.model.ProductInfoP2
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.data.util.numberFormatted
import com.tokopedia.product.detail.data.util.origin
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.estimasiongkir.view.activity.RatesEstimationDetailActivity
import com.tokopedia.product.detail.view.activity.ProductInstallmentActivity
import com.tokopedia.product.detail.view.activity.WholesaleActivity
import com.tokopedia.product.detail.view.fragment.partialview.*
import com.tokopedia.product.detail.view.util.AppBarState
import com.tokopedia.product.detail.view.util.AppBarStateChangeListener
import com.tokopedia.product.detail.view.util.FlingBehavior
import com.tokopedia.product.detail.view.util.ProductDetailErrorHandler
import com.tokopedia.product.detail.view.viewmodel.Loaded
import com.tokopedia.product.detail.view.viewmodel.Loading
import com.tokopedia.product.detail.view.viewmodel.ProductInfoViewModel
import com.tokopedia.product.detail.view.widget.CountDrawable
import com.tokopedia.product.report.view.dialog.ReportDialogFragment
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.product.warehouse.view.viewmodel.ProductWarehouseViewModel
import com.tokopedia.referral.Constants.Action.Companion.ACTION_GET_REFERRAL_CODE
import com.tokopedia.referral.ReferralAction
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shopetalasepicker.constant.ShopParamConstant
import com.tokopedia.shopetalasepicker.view.activity.ShopEtalasePickerActivity
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant
import com.tokopedia.transaction.common.TransactionRouter
import com.tokopedia.transactiondata.entity.shared.expresscheckout.AtcRequestParam
import com.tokopedia.transactiondata.entity.shared.expresscheckout.Constant.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.partial_layout_button_action.*
import kotlinx.android.synthetic.main.partial_most_helpful_review_view.*
import kotlinx.android.synthetic.main.partial_other_product.*
import kotlinx.android.synthetic.main.partial_product_detail_header.*
import kotlinx.android.synthetic.main.partial_product_detail_visibility.*
import kotlinx.android.synthetic.main.partial_product_detail_wholesale.*
import kotlinx.android.synthetic.main.partial_product_full_descr.*
import kotlinx.android.synthetic.main.partial_product_image_review.*
import kotlinx.android.synthetic.main.partial_product_latest_talk.*
import kotlinx.android.synthetic.main.partial_product_rating_talk_courier.*
import kotlinx.android.synthetic.main.partial_product_recommendation.*
import kotlinx.android.synthetic.main.partial_product_shop_info.*
import kotlinx.android.synthetic.main.partial_variant_rate_estimation.*
import model.TradeInParams
import view.customview.TradeInTextView
import viewmodel.TradeInBroadcastReceiver
import javax.inject.Inject

class ProductDetailFragment : BaseDaggerFragment() {
    private var productId: String? = null
    private var productKey: String? = null
    private var shopDomain: String? = null
    private var trackerAttribution: String? = ""
    private var trackerListName: String? = ""
    private var isFromDeeplink: Boolean = false
    private var isAffiliate: Boolean = false
    private var isSpecialPrize: Boolean = false

    lateinit var headerView: PartialHeaderView
    lateinit var productStatsView: PartialProductStatisticView
    lateinit var partialVariantAndRateEstView: PartialVariantAndRateEstView
    lateinit var productDescrView: PartialProductDescrFullView
    lateinit var actionButtonView: PartialButtonActionView
    lateinit var productShopView: PartialShopView
    lateinit var attributeInfoView: PartialAttributeInfoView
    lateinit var imageReviewViewView: PartialImageReviewView
    lateinit var mostHelpfulReviewView: PartialMostHelpfulReviewView
    lateinit var latestTalkView: PartialLatestTalkView
    lateinit var otherProductView: PartialOtherProductView
    lateinit var recommendationProductView: PartialRecommendationProductView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var productInfoViewModel: ProductInfoViewModel
    lateinit var productWarehouseViewModel: ProductWarehouseViewModel

    lateinit var performanceMonitoringP1: PerformanceMonitoring
    lateinit var performanceMonitoringP2: PerformanceMonitoring
    lateinit var performanceMonitoringFull: PerformanceMonitoring
    lateinit var remoteConfig: RemoteConfig

    private var isAppBarCollapsed = false
    private var menu: Menu? = null
    private var useVariant = true

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

    private val productDetailTracking: ProductDetailTracking by lazy {
        ProductDetailTracking()
    }

    var productInfo: ProductInfo? = null
    var shopInfo: ShopInfo? = null

    private var refreshLayout: SwipeToRefresh? = null

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
        const val REQUEST_CODE_SHOP_INFO = 998

        const val SAVED_NOTE = "saved_note"
        const val SAVED_QUANTITY = "saved_quantity"
        const val SAVED_VARIANT = "saved_variant"

        private const val PDP_P1_TRACE = "mp_pdp_p1"
        private const val PDP_P2_TRACE = "mp_pdp_p2"
        private const val PDP_P3_TRACE = "mp_pdp_p3"
        private const val ENABLE_VARIANT = "mainapp_discovery_enable_pdp_variant"

        private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"
        private const val ARG_PRODUCT_KEY = "ARG_PRODUCT_KEY"
        private const val ARG_SHOP_DOMAIN = "ARG_SHOP_DOMAIN"
        private const val ARG_TRACKER_ATTRIBUTION = "ARG_TRACKER_ATTRIBUTION"
        private const val ARG_TRACKER_LIST_NAME = "ARG_TRACKER_LIST_NAME"
        private const val ARG_FROM_DEEPLINK = "ARG_FROM_DEEPLINK"
        private const val ARG_FROM_AFFILIATE = "ARG_FROM_AFFILIATE"
        private const val ARG_IS_SPECIAL_PRIZE = "ARG_IS_SPECIAL_PRIZE"

        private const val WISHLIST_STATUS_UPDATED_POSITION = "wishlistUpdatedPosition"
        private const val WIHSLIST_STATUS_IS_WISHLIST = "isWishlist"

        fun newInstance(productId: String? = null,
                        shopDomain: String? = null,
                        productKey: String? = null,
                        isFromDeeplink: Boolean = false,
                        isAffiliate: Boolean = false,
                        isSpecialPrize: Boolean = false,
                        trackerAttribution: String? = null,
                        trackerListName: String? = null) =
                ProductDetailFragment().also {
                    it.arguments = Bundle().apply {
                        productId?.let { pid -> putString(ARG_PRODUCT_ID, pid) }
                        productKey?.let { pkey -> putString(ARG_PRODUCT_KEY, pkey) }
                        shopDomain?.let { domain -> putString(ARG_SHOP_DOMAIN, domain) }
                        trackerAttribution?.let { attribution -> putString(ARG_TRACKER_ATTRIBUTION, attribution) }
                        trackerListName?.let { listName -> putString(ARG_TRACKER_LIST_NAME, listName) }
                        putBoolean(ARG_FROM_DEEPLINK, isFromDeeplink)
                        putBoolean(ARG_FROM_AFFILIATE, isAffiliate)
                        putBoolean(ARG_IS_SPECIAL_PRIZE, isSpecialPrize)
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
            productKey = it.getString(ARG_PRODUCT_KEY)
            shopDomain = it.getString(ARG_SHOP_DOMAIN)
            trackerAttribution = it.getString(ARG_TRACKER_ATTRIBUTION)
            trackerListName = it.getString(ARG_TRACKER_LIST_NAME)
            isFromDeeplink = it.getBoolean(ARG_FROM_DEEPLINK, false)
            isAffiliate = it.getBoolean(ARG_FROM_AFFILIATE, false)
//            isSpecialPrize = it.getBoolean(ARG_IS_SPECIAL_PRIZE, false)
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
        productInfoViewModel.productVariantResp.observe(this, Observer {
            when (it) {
                is Success -> onSuccessGetProductVariantInfo(it.data)
                is Fail -> onErrorGetProductVariantInfo(it.throwable)
            }
        })

        productInfoViewModel.productInfoP2resp.observe(this, Observer {
            if (!productInfoViewModel.isUserSessionActive() && ::performanceMonitoringFull.isInitialized)
                performanceMonitoringFull.stopTrace()

            performanceMonitoringP2.stopTrace()
            it?.run { renderProductInfo2(this) }
        })

        productInfoViewModel.productInfoP3resp.observe(this, Observer {
            if (::performanceMonitoringFull.isInitialized)
                performanceMonitoringFull.stopTrace()
            it?.run { renderProductInfo3(this) }
        })

        productInfoViewModel.loadOtherProduct.observe(this, Observer {
            when (it) {
                is Loading -> otherProductView.startLoading()
                is Loaded -> {
                    otherProductView.renderData((it.data as? Success)?.data ?: listOf())
                }
            }
        })

        productInfoViewModel.loadTopAdsProduct.observe(this, Observer {
            when (it) {
                is Loading -> {
                    recommendationProductView.startLoading()
                }
                is Loaded -> {
                    recommendationProductView.renderData((it.data as? Success)?.data ?: return@Observer)
                    (it.data as? Success)?.data?.let { result ->
                        recommendationProductView.renderData(result) }
                }
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        performanceMonitoringP1 = PerformanceMonitoring.start(PDP_P1_TRACE)
        performanceMonitoringP2 = PerformanceMonitoring.start(PDP_P2_TRACE)
        if (productInfoViewModel.isUserSessionActive())
            performanceMonitoringFull = PerformanceMonitoring.start(PDP_P3_TRACE)

        initializePartialView(view)
        initView()
        refreshLayout = view.findViewById(R.id.swipeRefresh)

        tradeInBroadcastReceiver = TradeInBroadcastReceiver()
        tradeInBroadcastReceiver.setBroadcastListener {
            if (it) {
                trackTradeIn(it)

                if (tv_trade_in_promo != null) {
                    tv_trade_in_promo.visible()
                    tv_available_at?.visible()
                }
            } else
                trackTradeIn(it)

        }
        context?.let {
            LocalBroadcastManager.getInstance(context!!).registerReceiver(tradeInBroadcastReceiver, IntentFilter(TradeInTextView.ACTION_TRADEIN_ELLIGIBLE))
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val layoutParams = appbar.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.behavior = FlingBehavior(nested_scroll)
        }

        appbar.addOnOffsetChangedListener { _, verticalOffset -> refreshLayout?.isEnabled = (verticalOffset == 0) }
        refreshLayout?.setOnRefreshListener { loadProductData(true) }

        if (isAffiliate) {
            actionButtonView.gone()
            base_btn_affiliate.visible()
            loadingAffiliate.visible()
        }

        nested_scroll.listener = object : ObservableNestedScrollView.ScrollViewListener {
            override fun onScrollEnded(scrollView: ObservableNestedScrollView, x: Int, y: Int, oldX: Int, oldY: Int) {
                scrollView.startLoad()
                productInfoViewModel.loadMore()
            }
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
    }

    private fun doBuy() {
        val isExpressCheckout = (productInfoViewModel.productInfoP3resp.value)?.isExpressCheckoutType
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
                val isOcsCheckoutType = (productInfoViewModel.productInfoP3resp.value)?.isOcsCheckoutType
                    ?: false
                val intent = NormalCheckoutActivity.getIntent(it,
                    basic.shopID.toString(),
                    parentProductId,
                    userInputNotes,
                    userInputQuantity,
                    userInputVariant,
                    action,
                    null,
                    trackerAttribution,
                    trackerListName,
                    shopInfo?.goldOS?.shopTypeString,
                    shopInfo?.shopCore?.name,
                    isOcsCheckoutType)
                intent.putExtra(NormalCheckoutActivity.EXTRA_TRADE_IN_PARAMS, tradeInParams)
                startActivityForResult(intent,
                    REQUEST_CODE_NORMAL_CHECKOUT)
            }
        }
    }

    private fun goToAtcExpress() {
        activity?.let {
            try {
                val productInfo = (productInfoViewModel.productInfoP1Resp.value as Success).data
                val warehouseId: Int = productInfoViewModel.multiOrigin.id.toInt()
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
                    putExtra("EXTRA_ATC_REQUEST", atcRequestParam)
                    putExtra("tracker_attribution", trackerAttribution)
                    putExtra("tracker_list_name", trackerListName)
                    startActivityForResult(intent, REQUEST_CODE_ATC_EXPRESS)
                    it.overridePendingTransition(R.anim.pull_up, 0)
                }
            } catch (e: Exception) {

            }
        }
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
            productDescrView = PartialProductDescrFullView.build(base_info_and_description, activity)
        }

        if (!::actionButtonView.isInitialized) {
            actionButtonView = PartialButtonActionView.build(base_btn_action, onViewClickListener)
            actionButtonView.isSpecialPrize = isSpecialPrize
        }

        if (!::productShopView.isInitialized) {
            productShopView = PartialShopView.build(base_shop_view, onViewClickListener)
        }

        if (!::attributeInfoView.isInitialized)
            attributeInfoView = PartialAttributeInfoView.build(base_attribute)

        if (!::imageReviewViewView.isInitialized)
            imageReviewViewView = PartialImageReviewView.build(base_image_review, this::onImageReviewClick)

        if (!::mostHelpfulReviewView.isInitialized) {
            mostHelpfulReviewView = PartialMostHelpfulReviewView.build(base_view_most_helpful_review)
            mostHelpfulReviewView.onReviewClicked = this::onReviewClicked
            mostHelpfulReviewView.onImageReviewClicked = this::onImagehelpfulReviewClick
        }

        if (!::latestTalkView.isInitialized)
            latestTalkView = PartialLatestTalkView.build(base_latest_talk)

        if (!::otherProductView.isInitialized)
            otherProductView = PartialOtherProductView.build(base_other_product)

        if (!::recommendationProductView.isInitialized){
            recommendationProductView = PartialRecommendationProductView.build(base_recommen_product)
        }

    }

    private fun onImageReviewClick(imageReview: ImageReviewItem, isSeeAll: Boolean = false) {
        val productId = productInfo?.basic?.id ?: return
        if (isSeeAll) {
            productDetailTracking.eventClickReviewOnSeeAllImage(productId)
        } else {
            productDetailTracking.eventClickReviewOnBuyersImage(productId, imageReview.reviewId)
        }
        context?.let {
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
            else -> {
            }
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

    private fun initView() {
        collapsing_toolbar.title = ""
        toolbar.title = ""
        activity?.let {
            toolbar.setBackgroundColor(ContextCompat.getColor(it, R.color.white))
            (it as AppCompatActivity).setSupportActionBar(toolbar)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        setupByConfiguration(resources.configuration)
        appbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: Int) {
                when (state) {
                    AppBarState.EXPANDED -> {
                        isAppBarCollapsed = false
                        expandedAppBar()
                        nested_scroll.completeLoad()
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
    }

    private fun collapsedAppBar() {
        initStatusBarLight()
        initToolbarLight()
        fab_detail?.hide()
        label_cod?.visibility = if (shouldShowCod && userCod && shopCod) View.INVISIBLE else View.GONE
    }

    private fun expandedAppBar() {
        initStatusBarDark()
        initToolbarTransparent()
        showFabDetailAfterLoadData()
        label_cod?.visibility = if (shouldShowCod && userCod && shopCod) View.INVISIBLE else View.GONE
    }

    private fun initToolbarLight() {
        activity?.run {
            if (isAdded) {
                collapsing_toolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.grey_icon_light_toolbar))
                collapsing_toolbar.setExpandedTitleColor(Color.TRANSPARENT)
                toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grey_icon_light_toolbar))
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
                menu?.let {
                    if (it.size() > 2) {
                        it.findItem(R.id.action_share).icon = ContextCompat.getDrawable(this, R.drawable.ic_product_share_dark)
                        val menuCart = it.findItem(R.id.action_cart)
                        menuCart.icon = ContextCompat.getDrawable(this, R.drawable.ic_product_cart_counter_dark)
                        setBadgeMenuCart(menuCart)
                    }
                }

                toolbar.overflowIcon = ContextCompat.getDrawable(activity!!, R.drawable.ic_product_more_dark)
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

    private fun initToolbarTransparent() {
        activity?.run {
            if (isAdded) {
                collapsing_toolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white))
                collapsing_toolbar.setExpandedTitleColor(Color.TRANSPARENT)
                toolbar.background = ContextCompat.getDrawable(this, R.drawable.gradient_shadow_black_vertical)
                (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_light)
                menu?.let {
                    if (it.size() > 2) {
                        it.findItem(R.id.action_share).icon = ContextCompat.getDrawable(this, R.drawable.ic_product_share_light)
                        val menuCart = it.findItem(R.id.action_cart)
                        menuCart.icon = ContextCompat.getDrawable(this, R.drawable.ic_product_cart_counter_light)
                        setBadgeMenuCart(menuCart)
                    }
                }
                toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.ic_product_more_light)
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

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        setupByConfiguration(newConfig)
    }

    private fun setupByConfiguration(configuration: Configuration?) {
        appbar.visibility = View.VISIBLE
        configuration?.let {
            val screenWidth = resources.displayMetrics.widthPixels
            if (it.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val layoutParams = appbar.layoutParams as CoordinatorLayout.LayoutParams
                val height = screenWidth / 3
                layoutParams.height = height
                view_picture.layoutParams.height = height
                appbar.visibility = View.VISIBLE
            } else if (it.orientation == Configuration.ORIENTATION_PORTRAIT) {
                val layoutParams = appbar.layoutParams as CoordinatorLayout.LayoutParams
                layoutParams.height = screenWidth
                view_picture.layoutParams.height = screenWidth
                appbar.visibility = View.VISIBLE
            }
        }
    }

    private fun loadProductData(forceRefresh: Boolean = false) {
        if (forceRefresh) {
            otherProductView.renderData(listOf())
            recommendationProductView.hideView()
        }
        if (productId != null || (productKey != null && shopDomain != null)) {
            productInfoViewModel.getProductInfo(ProductParams(productId, shopDomain, productKey), forceRefresh)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ETALASE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedEtalaseId = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_ID)
                    val selectedEtalaseName = data.getStringExtra(ShopParamConstant.EXTRA_ETALASE_NAME)
                    if (productId != null && !selectedEtalaseName.isNullOrEmpty()) {
                        showProgressDialog(onCancelClicked = { productWarehouseViewModel.clear() })
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
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (data.hasExtra(NormalCheckoutFragment.RESULT_PRODUCT_DATA_CACHE_ID)) {
                        //refresh product by selected variant/product
                        val objectId: String = data.getStringExtra(NormalCheckoutFragment.RESULT_PRODUCT_DATA_CACHE_ID)
                        val cacheManager = SaveInstanceCacheManager(this@ProductDetailFragment.context!!, objectId)
                        val selectedProductInfo: ProductInfo? = cacheManager.get(NormalCheckoutFragment.RESULT_PRODUCT_DATA, ProductInfo::class.java)
                        val selectedWarehouse: MultiOriginWarehouse? = cacheManager.get(NormalCheckoutFragment.RESULT_SELECTED_WAREHOUSE,
                            MultiOriginWarehouse::class.java)
                        if (selectedProductInfo != null) {
                            userInputVariant = data.getStringExtra(NormalCheckoutFragment.EXTRA_SELECTED_VARIANT_ID)
                            productInfoViewModel.productInfoP1Resp.value = Success(ProductInfoP1().apply { productInfo = selectedProductInfo })
                            selectedWarehouse?.let {
                                productInfoViewModel.multiOrigin = it.warehouseInfo
                                productInfoViewModel.productInfoP2resp.value = productInfoViewModel.productInfoP2resp.value?.copy(
                                    nearestWarehouse = it
                                )
                            }

                        }
                    }
                    //refresh variant
                    val variantResult = productInfoViewModel.productVariantResp.value
                    if (variantResult is Success) {
                        variantResult.data.run {
                            onSuccessGetProductVariantInfo(this)
                        }
                    }
                    userInputNotes = data.getStringExtra(NormalCheckoutFragment.EXTRA_NOTES)
                    userInputQuantity = data.getIntExtra(NormalCheckoutFragment.EXTRA_QUANTITY, 0)

                    if (data.hasExtra(NormalCheckoutFragment.RESULT_ATC_SUCCESS_MESSAGE)) {
                        val successMessage = data.getStringExtra(NormalCheckoutFragment.RESULT_ATC_SUCCESS_MESSAGE)
                        showSnackbarSuccessAtc(successMessage)
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
                        errorBottomsheets.show(fragmentManager, "")
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
            else ->
                super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        productInfoViewModel.productInfoP1Resp.removeObservers(this)
        productInfoViewModel.productInfoP2resp.removeObservers(this)
        productInfoViewModel.productInfoP3resp.removeObservers(this)
        productInfoViewModel.productVariantResp.removeObservers(this)
        productInfoViewModel.loadOtherProduct.removeObservers(this)
        productInfoViewModel.loadTopAdsProduct.removeObservers(this)
        productInfoViewModel.clear()
        productWarehouseViewModel.clear()
        super.onDestroy()
    }

    private fun updateCartNotification() {
        activity?.run {
            (application as ProductDetailRouter).updateMarketplaceCartCounter(TransactionRouter.CartNotificationListener {
                if (isAdded) {
                    if (isAppBarCollapsed) {
                        initToolbarLight()
                    } else {
                        initToolbarTransparent()
                    }
                }
            })
        }
    }

    private fun showSnackbarSuccessAtc(message: String?) {
        activity?.run {
            val messageString: String = if (message.isNullOrEmpty()) {
                getString(R.string.default_request_error_unknown_short)
            } else {
                message!!
            }
            ToasterNormal.make(view, messageString.replace("\n", " "), BaseToaster.LENGTH_LONG)
                .setAction(getString(R.string.label_atc_open_cart)) { v ->
                    productDetailTracking.eventAtcClickLihat(productId)
                    val intent = RouteManager.getIntent(this, ApplinkConst.CART)
                    startActivity(intent)
                }
                .show()
        }
    }


    private fun renderProductInfo3(productInfoP3: ProductInfoP3) {
        userCod = productInfoP3.userCod
        if (shouldShowCod && shopCod && userCod) label_cod.visible() else label_cod.gone()
        headerView.renderCod(shouldShowCod && shopCod && userCod)
        productInfoP3.rateEstSummarizeText?.let {
            partialVariantAndRateEstView.renderRateEstimation(it, ::gotoRateEstimation)
        }
        shopInfo?.let { updateWishlist(it, productInfoP3.isWishlisted) }
        productInfoP3.pdpAffiliate?.let { renderAffiliate(it) }

        actionButtonView.renderData(productInfoP3.isExpressCheckoutType)
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
            actionButtonView.visible()
        }
    }

    private fun onAffiliateClick(pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate,
                                 isRegularPdp: Boolean) {
        if (productInfo == null) return
        activity?.let {
            productDetailTracking.eventClickAffiliate(productInfoViewModel.userId, productInfo!!.basic.shopID,
                pdpAffiliate.productId.toString(), isRegularPdp)
            if (productInfoViewModel.isUserSessionActive()) {
                RouteManager.route(it,
                    ApplinkConst.AFFILIATE_CREATE_POST,
                    pdpAffiliate.productId.toString(),
                    pdpAffiliate.adId.toString())
                it.setResult(Activity.RESULT_OK)
                it.finish()
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                    REQUEST_CODE_LOGIN)
            }
        }
    }

    private fun gotoRateEstimation() {
        if (productInfo == null && shopInfo == null) return
        activity?.let {
            startActivity(RatesEstimationDetailActivity.createIntent(it,
                shopInfo!!.shopCore.domain, productInfo!!.basic.weight,
                productInfo!!.basic.weightUnit,
                if (productInfoViewModel.multiOrigin.isFulfillment)
                    productInfoViewModel.multiOrigin.origin else null))
        }
    }

    private fun renderProductInfo2(productInfoP2: ProductInfoP2) {
        productInfoP2.shopInfo?.let { shopInfo ->
            this.shopInfo = shopInfo
            productDescrView.shopInfo = shopInfo
            productShopView.renderShop(shopInfo, productInfoViewModel.isShopOwner(shopInfo.shopCore.shopID.toInt()))
            val data = productInfo ?: return

            actionButtonView.renderData(!data.basic.isActive(),
                (productInfoViewModel.isShopOwner(data.basic.shopID)
                    || shopInfo.allowManage),
                data.preorder)
            actionButtonView.visibility = !isAffiliate && shopInfo.statusInfo.shopStatus == 1
            headerView.showOfficialStore(shopInfo.goldOS)
            view_picture.renderShopStatus(shopInfo, productInfo?.basic?.status
                ?: ProductStatusTypeDef.ACTIVE)
            activity?.let {
                productStatsView.renderClickShipment(it, productInfo?.basic?.id?.toString()
                    ?: "", shopInfo.shipments, shopInfo.bbInfo)
            }
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
            productInfoP2.minInstallment?.let {
                label_installment.visible()
                label_desc_installment.text = getString(R.string.installment_template, it.interest.numberFormatted(),
                    (if (shopInfo.goldOS.isOfficial == 1) it.osMonthlyPrice else it.monthlyPrice).getCurrencyFormatted())
                label_desc_installment.visible()
                label_desc_installment.setOnClickListener {
                    activity?.let {
                        val price = (
                            if (productInfo?.campaign?.isActive == true && (productInfo?.campaign?.id?.toIntOrNull()
                                    ?: 0) > 0)
                                productInfo?.campaign?.discountedPrice
                            else
                                productInfo?.basic?.price
                            ) ?: 0f
                        startActivity(ProductInstallmentActivity.createIntent(it,
                            shopInfo.goldOS.isOfficial == 1,
                            price))
                    }
                }
                if (label_min_wholesale.isVisible) {
                    wholesale_divider.visible()
                } else {
                    wholesale_divider.gone()
                }
                base_view_wholesale.visible()
            }

            productShopView.renderShopFeature(productInfoP2.shopFeature)
            trackTradeIn(tradeInParams.isEligible == 1)
            delegateTradeInTracking = false
        }
        shopCod = productInfoP2.shopCod
        productInfoP2.shopBadge?.let { productShopView.renderShopBadge(it) }
        productStatsView.renderRating(productInfoP2.rating)
        attributeInfoView.renderWishlistCount(productInfoP2.wishlistCount.count)
        partialVariantAndRateEstView.renderPriorityOrder(productInfoP2.shopCommitment)
        imageReviewViewView.renderData(productInfoP2.imageReviews)
        mostHelpfulReviewView.renderData(productInfoP2.helpfulReviews, productInfo?.stats?.countReview
            ?: 0)
        latestTalkView.renderData(productInfoP2.latestTalk, productInfo?.stats?.countTalk ?: 0,
            productInfo?.basic?.shopID ?: 0, this::onDiscussionClicked)

        if (!isAffiliate) {
            otherProductView.renderData(productInfoP2.productOthers)
        }

        partialVariantAndRateEstView.renderFulfillment(productInfoP2.nearestWarehouse.warehouseInfo.isFulfillment)
        if (productInfo != null && productInfoP2.nearestWarehouse.warehouseInfo.id.isNotBlank())
            headerView.updateStockAndPriceWarehouse(productInfoP2.nearestWarehouse, productInfo!!.campaign)

        partialVariantAndRateEstView.renderPurchaseProtectionData(productInfoP2.productPurchaseProtectionInfo)
        productInfo?.run {
            productDetailTracking.sendScreen(basic.shopID.toString(),
                shopInfo?.goldOS?.shopTypeString ?: "", productId ?: "")
            var isHandPhone = false
            this.category.detail.forEach { detail: Category.Detail ->
                if (detail.name.equals("Handphone")) {
                    isHandPhone = true
                }
            }
            if (!isHandPhone)
                productDetailTracking.eventEnhanceEcommerceProductDetail(trackerListName, this, productInfoP2.shopInfo, trackerAttribution,
                    false, false, productInfoP2.nearestWarehouse.warehouseInfo.isFulfillment)

            productDetailTracking.sendMoEngageOpenProduct(this, shopInfo?.goldOS?.isOfficial == 1, shopInfo?.shopCore?.name
                ?: "")
            productDetailTracking.eventAppsFylerOpenProduct(this)
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
                fab_detail.isActivated = true
                fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_wishlist_checked))
            } else {
                fab_detail.isActivated = false
                fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_wishlist_unchecked))
            }
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
        shouldShowCod = data.shouldShowCod
        headerView.renderData(data)
        view_picture.renderData(data.pictures, this::onPictureProductClicked)
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

        tradeInParams = TradeInParams()
        tradeInParams.categoryId = productInfoP1.productInfo.category.detail[0].id.toInt()
        tradeInParams.deviceId = (activity?.application as ProductDetailRouter).getDeviceId(activity as Context)
        val userSession = UserSession(activity)
        tradeInParams.userId = if (userSession.userId.isNotEmpty())
            userSession.userId.toInt()
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
        tv_trade_in.tradeInReceiver.checkTradeIn(tradeInParams, false)
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
        actionButtonView.renderData(!data.basic.isActive(),
                (productInfoViewModel.isShopOwner(data.basic.shopID)
                        || shopInfo?.allowManage == true),
                data.preorder)
        actionButtonView.visibility = !isAffiliate
        activity?.invalidateOptionsMenu()
    }

    private fun onErrorGetProductVariantInfo(throwable: Throwable) {
        //variant error, do not show variant, but still can buy the product
        partialVariantAndRateEstView.renderData(null, "", this::onVariantClicked)
    }

    private fun onSuccessGetProductVariantInfo(data: ProductVariant?) {
        if (data == null || !data.hasChildren) {
            partialVariantAndRateEstView.renderData(null, "", this::onVariantClicked)
            return
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
        productInfoViewModel.productInfoP3resp.value?.isWishlisted = false
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
        productInfoViewModel.productInfoP3resp.value?.isWishlisted = true
        updateWishlist(true)
        //TODO clear cache
        sendIntentResusltWishlistChange(productId ?: "", true)
    }

    private fun onDiscussionClicked() {

        productDetailTracking.eventTalkClicked()

        activity?.let {
            val intent = RouteManager.getIntent(it,
                ApplinkConst.PRODUCT_TALK, productInfo?.basic?.id.toString())
            startActivityForResult(intent, REQUEST_CODE_TALK_PRODUCT)
        }
        productInfo?.run {
            productDetailTracking.sendMoEngageClickDiskusi(this,
                (shopInfo?.goldOS?.isOfficial ?: 0) > 0,
                shopInfo?.shopCore?.name ?: "")
        }
    }

    private fun onReviewClicked() {
        productInfo?.run {
            productDetailTracking.eventReviewClicked()
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
                putChatProductInfoTo(intent)
                startActivity(intent)
            } else {
                startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN),
                    REQUEST_CODE_LOGIN)
            }
        }
        productDetailTracking.eventSendMessage()
        productDetailTracking.eventSendChat()
    }

    private fun putChatProductInfoTo(intent: Intent?) {
        if (intent == null) return
        val variants = mapSelectedProductVariants()
        val productImageUrl = getProductImageUrl()
        val productName = getProductName()
        val productPrice = getProductPrice()
        val productUrl = getProductUrl()
        val productColorVariant = variants?.get("colour")?.get("value")
        val productColorHexVariant = variants?.get("colour")?.get("hex")
        val productSizeVariant = variants?.get("size")?.get("value")
        with(intent) {
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_ID, productId)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_IMAGE_URL, productImageUrl)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_NAME, productName)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_PRICE, productPrice)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_URL, productUrl)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_COLOR_VARIANT, productColorVariant)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_HEX_COLOR_VARIANT, productColorHexVariant)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_SIZE_VARIANT, productSizeVariant)
        }
    }

    private fun getProductImageUrl(): String? {
        val images = productInfo?.pictures
        return images?.get(0)?.urlThumbnail
    }

    private fun getProductName(): String? {
        return productInfo?.basic?.name
    }

    private fun getProductPrice(): String? {
        return productInfo?.basic?.price?.getCurrencyFormatted()
    }

    private fun getProductUrl(): String? {
        return productInfo?.basic?.url
    }

    private fun mapSelectedProductVariants(): ArrayMap<String, ArrayMap<String, String>>? {
        return getProductVariant()?.mapSelectedProductVariants(userInputVariant)
    }

    private fun getProductVariant(): ProductVariant? {
        val productVariantResponse = productInfoViewModel.productVariantResp.value
        return if (productVariantResponse is Success) {
            productVariantResponse.data
        }  else { null }
    }

    /**
     * go to preview image activity to show larger image of Product
     */
    private fun onPictureProductClicked(position: Int) {
        startActivity(ImagePreviewActivity.getCallingIntent(context!!,
            getImageURIPaths(),
            null,
            position))
    }

    private fun getImageURIPaths(): ArrayList<String> {
        return ArrayList(productInfo?.run { pictures?.map { it.urlOriginal } } ?: listOf())
    }

    private fun onVariantClicked() {
        productDetailTracking.eventClickVariant(generateVariantString())
        goToNormalCheckout(ATC_AND_BUY)
    }

    private fun generateVariantString(): String {
        return try {
            val productVariant = (productInfoViewModel.productVariantResp.value as Success).data
            productVariant.variant.map { it.name }.joinToString(separator = ", ")
        } catch (e: Throwable) {
            ""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(if (isAppBarCollapsed) R.menu.menu_product_detail_dark else
            R.menu.menu_product_detail_light, menu)
        super.onCreateOptionsMenu(menu, inflater)
        this.menu = menu
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
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
            val cartCount = (application as ProductDetailRouter).getCartCount(this)
            if (cartCount > 0) {
                if (menuCart.icon is LayerDrawable) {
                    val icon = menuCart.icon as LayerDrawable
                    val badge = CountDrawable(context)
                    badge.setCount(if (cartCount > 99) {
                        getString(R.string.pdp_label_cart_count_max)
                    } else {
                        cartCount.toString()
                    })
                    icon.mutate()
                    icon.setDrawableByLayerId(R.id.ic_cart_count, badge)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
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
            showProgressDialog(onCancelClicked = { productWarehouseViewModel.clear() })
            productWarehouseViewModel.moveToWarehouse(it,
                onSuccessMoveToWarehouse = this::onSuccessWarehouseProduct,
                onErrorMoveToWarehouse = this::onErrorWarehouseProduct)
        }
    }

    private fun reportProduct() {
        productInfo?.run {
            if (productInfoViewModel.isUserSessionActive()) {
                fragmentManager?.let {
                    val fragment = ReportDialogFragment.newInstance(basic.id.toString())
                    fragment.show(it, ReportDialogFragment.TAG)
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
            productDetailTracking.eventCartMenuClicked(generateVariantString())
        }
    }

    private fun shareProduct() {
        if (productInfo == null && activity == null) return
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

    private fun trackTradeIn(isElligible: Boolean) {
        if (productInfo != null && shopInfo != null) {
            productDetailTracking.eventEnhanceEcommerceProductDetail(trackerListName, productInfo, shopInfo, trackerAttribution,
                    isElligible, tradeInParams?.usedPrice > 0, productInfoViewModel.multiOrigin.isFulfillment)
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
}
