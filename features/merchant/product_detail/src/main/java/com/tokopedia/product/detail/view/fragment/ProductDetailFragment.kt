package com.tokopedia.product.detail.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.affiliatecommon.data.pojo.productaffiliate.TopAdsPdpAffiliateResponse
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
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
import com.tokopedia.normalcheckout.constant.ATC_AND_SELECT
import com.tokopedia.normalcheckout.view.NormalCheckoutActivity
import com.tokopedia.product.detail.ProductDetailRouter
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.ProductInfo
import com.tokopedia.product.detail.common.data.model.ProductParams
import com.tokopedia.product.detail.common.data.model.constant.ProductStatusTypeDef
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.ProductInfoP1
import com.tokopedia.product.detail.data.model.ProductInfoP2
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.shop.ShopInfo
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.data.util.numberFormatted
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.estimasiongkir.view.activity.RatesEstimationDetailActivity
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.product.detail.view.fragment.partialview.*
import com.tokopedia.product.detail.view.util.AppBarState
import com.tokopedia.product.detail.view.util.AppBarStateChangeListener
import com.tokopedia.product.detail.view.util.FlingBehavior
import com.tokopedia.product.detail.view.viewmodel.ProductInfoViewModel
import com.tokopedia.product.report.view.dialog.ReportDialogFragment
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.product.warehouse.view.viewmodel.ProductWarehouseViewModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shopetalasepicker.constant.ShopParamConstant
import com.tokopedia.shopetalasepicker.view.activity.ShopEtalasePickerActivity
import com.tokopedia.topads.sdk.base.adapter.Item
import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.domain.model.Shop
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.listener.TopAdsListener
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceOption
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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
import kotlinx.android.synthetic.main.partial_product_shop_info.*
import kotlinx.android.synthetic.main.partial_variant_rate_estimation.*
import javax.inject.Inject

class ProductDetailFragment : BaseDaggerFragment() {
    private var productId: String? = null
    private var productKey: String? = null
    private var shopDomain: String? = null
    private var isFromDeeplink: Boolean = false
    private var isAffiliate: Boolean = false

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

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var productInfoViewModel: ProductInfoViewModel
    lateinit var productWarehouseViewModel: ProductWarehouseViewModel

    lateinit var performanceMonitoring: PerformanceMonitoring
    lateinit var remoteConfig: RemoteConfig

    private var isAppBarCollapsed = false
    private var menu: Menu? = null
    private var useVariant = true

    private var shouldShowCod = false

    var loadingProgressDialog: ProgressDialog? = null

    private var userInputNotes = ""
    private var userInputQuantity = 0
    private var userInputVariant: String? = null

    private val productDetailTracking: ProductDetailTracking by lazy {
        ProductDetailTracking((context?.applicationContext as? AbstractionRouter)?.analyticTracker)
    }

    var productInfo: ProductInfo? = null
    var shopInfo: ShopInfo? = null
    var productVariant: ProductVariant? = null

    companion object {
        const val REQUEST_CODE_TALK_PRODUCT = 1
        const val REQUEST_CODE_EDIT_PRODUCT = 2
        const val REQUEST_CODE_LOGIN = 561
        const val REQUEST_CODE_MERCHANT_VOUCHER_DETAIL = 563
        const val REQUEST_CODE_MERCHANT_VOUCHER = 564
        const val REQUEST_CODE_ETALASE = 565
        const val REQUEST_CODE_NORMAL_CHECKOUT = 566

        const val SAVED_NOTE = "saved_note"
        const val SAVED_QUANTITY = "saved_quantity"
        const val SAVED_VARIANT = "saved_variant"

        private const val PDP_TRACE = "pdp_trace"
        private const val ENABLE_VARIANT = "mainapp_discovery_enable_pdp_variant"

        private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"
        private const val ARG_PRODUCT_KEY = "ARG_PRODUCT_KEY"
        private const val ARG_SHOP_DOMAIN = "ARG_SHOP_DOMAIN"
        private const val ARG_FROM_DEEPLINK = "ARG_FROM_DEEPLINK"
        private const val ARG_FROM_AFFILIATE = "ARG_FROM_AFFILIATE"

        fun newInstance(productId: String? = null,
                        shopDomain: String? = null,
                        productKey: String? = null,
                        isFromDeeplink: Boolean = false,
                        isAffiliate: Boolean = false) =
                ProductDetailFragment().also {
                    it.arguments = Bundle().apply {
                        productId?.let { pid -> putString(ARG_PRODUCT_ID, pid) }
                        productKey?.let { pkey -> putString(ARG_PRODUCT_KEY, pkey) }
                        shopDomain?.let { domain -> putString(ARG_SHOP_DOMAIN, domain) }
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
        performanceMonitoring = PerformanceMonitoring.start(PDP_TRACE)
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
            isFromDeeplink = it.getBoolean(ARG_FROM_DEEPLINK, false)
            isAffiliate = it.getBoolean(ARG_FROM_AFFILIATE, false)
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
            it?.run { renderProductInfo2(this) }
        })

        productInfoViewModel.productInfoP3resp.observe(this, Observer {
            it?.run { renderProductInfo3(this) }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePartialView(view)
        initView()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val layoutParams = appbar.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.behavior = FlingBehavior(nested_scroll)
        }

        merchantVoucherListWidget.setOnMerchantVoucherListWidgetListener(object : MerchantVoucherListWidget.OnMerchantVoucherListWidgetListener {
            override val isOwner: Boolean
                get() = productInfo?.basic?.shopID?.let { productInfoViewModel.isShopOwner(it) }
                        ?: false

            override fun onMerchantUseVoucherClicked(merchantVoucherViewModel: MerchantVoucherViewModel, position: Int) {
                activity?.let {
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
                        // TODO show toast product status title
                    }
                } else if (productP3value != null) {
                    if (it.isActivated) {
                        productId?.let {
                            productInfoViewModel.removeWishList(it,
                                    onSuccessRemoveWishlist = this::onSuccessRemoveWishlist,
                                    onErrorRemoveWishList = this::onErrorRemoveWishList)
                            // TODO tracking
                        }

                    } else {
                        productId?.let {
                            productInfoViewModel.addWishList(it,
                                    onSuccessAddWishlist = this::onSuccessAddWishlist,
                                    onErrorAddWishList = this::onErrorAddWishList)
                            // TODO tracking
                        }
                    }
                    // TODO tracking for affiliate
                }
            } else {
                context?.run {
                    startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                            REQUEST_CODE_LOGIN)
                }
            }
        }
        loadProductData()
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
            actionButtonView = PartialButtonActionView.build(base_btn_action)
        }

        if (!::productShopView.isInitialized) {
            productShopView = PartialShopView.build(base_shop_view)
        }

        if (!::attributeInfoView.isInitialized)
            attributeInfoView = PartialAttributeInfoView.build(base_attribute)

        if (!::imageReviewViewView.isInitialized)
            imageReviewViewView = PartialImageReviewView.build(base_image_review)

        if (!::mostHelpfulReviewView.isInitialized)
            mostHelpfulReviewView = PartialMostHelpfulReviewView.build(base_view_most_helpful_review)

        if (!::latestTalkView.isInitialized)
            latestTalkView = PartialLatestTalkView.build(base_latest_talk)

        if (!::otherProductView.isInitialized)
            otherProductView = PartialOtherProductView.build(base_other_product)
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
        topads_carousel.setAdsItemClickListener(adsItemsClickListener)
        topads_carousel.setAdsListener(adsListener)
        topads_carousel.setAdsItemImpressionListener(object : TopAdsItemImpressionListener() {
            override fun onImpressionProductAdsItem(position: Int, product: Product?) {
                product?.let {
                    productDetailTracking.eventTopAdsImpression(position, it)
                }
            }
        })
    }

    private val adsItemsClickListener = object : TopAdsItemClickListener {
        override fun onShopItemClicked(position: Int, shop: Shop?) {}

        override fun onAddFavorite(position: Int, data: Data?) {}

        override fun onProductItemClicked(position: Int, product: Product?) {
            if (product == null) return
            activity?.let {
                productDetailTracking.eventTopAdsClicked(product, position)
                startActivity(ProductDetailActivity.createIntent(it, product.id.toInt()))
            }
        }
    }

    private val adsListener = object : TopAdsListener {
        override fun onTopAdsLoaded(list: MutableList<Item<Any>>?) {
            topads_carousel.visible()
        }

        override fun onTopAdsFailToLoad(errorCode: Int, message: String?) {
            topads_carousel.gone()
        }

    }

    private fun collapsedAppBar() {
        initStatusBarLight()
        initToolbarLight()
        fab_detail.hide()
        label_cod.visibility = if (shouldShowCod) View.INVISIBLE else View.GONE
    }

    private fun expandedAppBar() {
        initStatusBarDark()
        initToolbarTransparent()
        showFabDetailAfterLoadData()
        label_cod.visibility = if (shouldShowCod) View.INVISIBLE else View.GONE
    }

    private fun initToolbarLight() {
        activity?.run {
            if (isAdded) {
                collapsing_toolbar.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.grey_icon_light_toolbar))
                collapsing_toolbar.setExpandedTitleColor(Color.TRANSPARENT)
                toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.grey_icon_light_toolbar))
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                (this as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_dark)
                if (menu != null && menu!!.size() > 2) {
                    menu!!.findItem(R.id.action_share).icon = ContextCompat.getDrawable(this, R.drawable.ic_product_share_dark)
                    //val cartCount = (activity!!.applicationContext as PdpRouter).getCartCount(getActivityContext())
                    menu!!.findItem(R.id.action_cart).icon = ContextCompat.getDrawable(this, R.drawable.ic_product_cart_counter_dark)
                    /*if (cartCount > 0) {
                        setDrawableCount(context, cartCount)
                    }*/
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
                if (menu != null && menu!!.size() > 1) {
                    menu!!.findItem(R.id.action_share).icon = ContextCompat.getDrawable(this, R.drawable.ic_product_share_light)
                    //val cartCount = (activity!!.applicationContext as PdpRouter).getCartCount(getActivityContext())
                    menu!!.findItem(R.id.action_cart).icon = (ContextCompat.getDrawable(this, R.drawable.ic_product_cart_counter_light))
                    /*if (cartCount > 0) {
                        setDrawableCount(context, cartCount)
                    }*/
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
        configuration?.let {
            val screenWidth = resources.displayMetrics.widthPixels
            if (it.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                val layoutParams = appbar.layoutParams as CoordinatorLayout.LayoutParams
                layoutParams.height = screenWidth / 3
                appbar.visibility = View.VISIBLE
            } else if (it.orientation == Configuration.ORIENTATION_PORTRAIT) {
                val layoutParams = appbar.layoutParams as CoordinatorLayout.LayoutParams
                layoutParams.height = screenWidth
                appbar.visibility = View.VISIBLE
            }
        }
    }

    private fun loadProductData() {
        if (productId != null || (productKey != null && shopDomain != null)) {
            productInfoViewModel.getProductInfo(ProductParams(productId, shopDomain, productKey), resources)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //TODO REQUEST_CODE_BUY handling result
        //if OK will change the qty, remark notes, and variant selection

        //TODO merchant voucher
        when (requestCode) {
            REQUEST_CODE_ETALASE -> {
                //TODO need to check if product id is null when device don't keep activity
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
                    //TODO reload merchant voucher data;
                    //presenter.loadMerchantVoucher
                }
            }
            REQUEST_CODE_NORMAL_CHECKOUT -> {
                //TODO set the product = product variant selected
                //TODO set note, qty, and selected variant
                //TODO refresh the UI
            }
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onDestroy() {
        productInfoViewModel.productInfoP1Resp.removeObservers(this)
        productInfoViewModel.productInfoP2resp.removeObservers(this)
        productInfoViewModel.productInfoP3resp.removeObservers(this)
        productInfoViewModel.productVariantResp.removeObservers(this)
        productInfoViewModel.clear()
        productWarehouseViewModel.clear()
        super.onDestroy()
    }

    private fun renderProductInfo3(productInfoP3: ProductInfoP3) {
        productInfoP3.rateEstimation?.let {
            partialVariantAndRateEstView.renderRateEstimation(it.rates,
                    shopInfo?.location ?: "", ::gotoRateEstimation)
        }
        shopInfo?.let { updateWishlist(it, productInfoP3.isWishlisted) }
        imageReviewViewView.renderData(productInfoP3.imageReviews)
        mostHelpfulReviewView.renderData(productInfoP3.helpfulReviews, productInfo?.stats?.countReview
                ?: 0)
        latestTalkView.renderData(productInfoP3.latestTalk, productInfo?.stats?.countTalk ?: 0,
                productInfo?.basic?.shopID ?: 0)
        productInfoP3.displayAds?.let { topads_carousel.setData(it) }
        otherProductView.renderData(productInfoP3.productOthers)

        productInfoP3.pdpAffiliate?.let { renderAffiliate(it) }
    }

    private fun renderAffiliate(pdpAffiliate: TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate) {
        if (isAffiliate) {
            base_btn_affiliate.visible()
            btn_affiliate.setOnClickListener { onAffiliateClick(pdpAffiliate, false) }
            actionButtonView.gone()
        } else {
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
                RouteManager.route(it, ApplinkConst.AFFILIATE_CREATE_POST
                        .replace("{product_id}", pdpAffiliate.productId.toString())
                        .replace("{ad_id}", pdpAffiliate.adId.toString()))
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
                    shopInfo!!.shopCore.domain, productInfo!!.basic.weight, productInfo!!.basic.weightUnit))
        }
    }

    private fun renderProductInfo2(productInfoP2: ProductInfoP2) {
        productInfoP2.shopInfo?.let { shopInfo ->
            this.shopInfo = shopInfo
            productDescrView.shopInfo = shopInfo
            productShopView.renderShop(shopInfo, productInfoViewModel.isShopOwner(shopInfo.shopCore.shopID.toInt()))
            val data = productInfo ?: return

            actionButtonView.renderData(data.basic.isWarehouse(),
                    (productInfoViewModel.isShopOwner(data.basic.shopID)
                            || shopInfo.allowManage),
                    data.preorder)

            actionButtonView.promoTopAdsClick = {

                val applink = Uri.parse(ApplinkConst.SellerApp.TOPADS_PRODUCT_CREATE).buildUpon()
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_SHOP_ID, shopInfo.shopCore.shopID)
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_EXTRA_ITEM_ID, productInfo?.basic?.id?.toString())
                        .appendQueryParameter(TopAdsSourceTaggingConstant.PARAM_KEY_SOURCE,
                                if (GlobalConfig.isSellerApp()) TopAdsSourceOption.SA_PDP else TopAdsSourceOption.MA_PDP).build().toString()

                context?.let { RouteManager.route(it, applink) }
            }
            actionButtonView.visibility = shopInfo.statusInfo.shopStatus == 1
            headerView.showOfficialStore(shopInfo.goldOS.isOfficial == 1)
            view_picture.renderShopStatus(shopInfo, productInfo?.basic?.status
                    ?: ProductStatusTypeDef.ACTIVE)
            activity?.let {
                productStatsView.renderClickShipment(it, productInfo?.basic?.id?.toString()
                        ?: "", shopInfo.shipments)
            }
            if (productInfoP2.vouchers.isNotEmpty()) {
                merchantVoucherListWidget.setData(ArrayList(productInfoP2.vouchers))
                merchantVoucherListWidget.visible()
            } else {
                merchantVoucherListWidget.gone()
            }
            productInfoP2.minInstallment?.let {
                label_installment.visible()
                label_desc_installment.text = getString(R.string.installment_template, it.interest.numberFormatted(),
                        (if(shopInfo.goldOS.isOfficial == 1) it.osMonthlyPrice else it.monthlyPrice).getCurrencyFormatted())
                label_desc_installment.visible()
                if (label_min_wholesale.isVisible){
                    wholesale_divider.visible()
                } else {
                    wholesale_divider.gone()
                }
                base_view_wholesale.visible()
            }
        }
        productInfoP2.shopBadge?.let { productShopView.renderShopBadge(it) }
        productStatsView.renderRating(productInfoP2.rating)
        attributeInfoView.renderWishlistCount(productInfoP2.wishlistCount.count)
        partialVariantAndRateEstView.renderPriorityOrder(productInfoP2.shopCommitment)
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
        if (productInfoViewModel.productInfoP3resp.value != null ||
                (shopInfo != null && shopInfo?.isAllowManage == 1)) {
            fab_detail.show()
        }
    }

    private fun gotoEditProduct() {
        val id = productInfo?.parentProductId ?: return
        context?.let {
            if (it.applicationContext is ProductDetailRouter) {
                val intent = (it.applicationContext as ProductDetailRouter).goToEditProduct(it, true, id)
                startActivityForResult(intent, REQUEST_CODE_EDIT_PRODUCT)
            }
        }
    }

    private fun onErrorGetProductInfo(throwable: Throwable) {
        ToasterError.make(coordinator, throwable.localizedMessage).show()
    }

    private fun onSuccessGetProductInfo(productInfoP1: ProductInfoP1) {
        val data = productInfoP1.productInfo
        productId = data.basic.id.toString()
        productInfo = data
        shouldShowCod = data.shouldShowCod
        if (shouldShowCod) label_cod.visible() else label_cod.gone()
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
            base_view_wholesale.visibility = View.VISIBLE
        } else {
            label_wholesale.visibility = View.GONE
            label_min_wholesale.visibility = View.GONE
            if (label_desc_installment.isVisible) base_view_wholesale.visibility = View.VISIBLE
            else base_view_wholesale.gone()
        }
        activity?.invalidateOptionsMenu()
    }

    private fun onErrorGetProductVariantInfo(throwable: Throwable) {
        //TODO variant error
        partialVariantAndRateEstView.renderData(null, this::onVariantClicked)
    }

    private fun onSuccessGetProductVariantInfo(data: ProductVariant?) {
        //TODO shop status must be 1
        productVariant = data
        partialVariantAndRateEstView.renderData(data, this::onVariantClicked)
    }

    private fun onSuccessWarehouseProduct() {
        hideProgressDialog()
        showToastSuccess(getString(R.string.success_warehousing_product))
        //TODO refresh reload product page force from network
        loadProductData()
    }

    @SuppressLint("Range")
    private fun onErrorWarehouseProduct(throwable: Throwable) {
        hideProgressDialog()
        showToastError(throwable)
    }

    private fun showToastError(throwable: Throwable) {
        activity?.run {
            ToasterError.make(findViewById(android.R.id.content),
                    ErrorHandler.getErrorMessage(context, throwable),
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
        //TODO refresh reload product page force from network
        loadProductData()
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
        //TODO action success remove wishlist. in old version, will broadcast
    }

    private fun onErrorAddWishList(errorMessage: String?) {
        showToastError(MessageErrorException(errorMessage))
    }

    private fun onSuccessAddWishlist(productId: String?) {
        showToastSuccess(getString(R.string.msg_success_add_wishlist))
        productInfoViewModel.productInfoP3resp.value?.isWishlisted = true
        updateWishlist(true)
        //TODO clear cache
        //TODO action success add wishlist. in old version, will broadcast
    }

    private fun onDiscussionClicked() {

        productDetailTracking.eventTalkClicked()

        activity?.let {
            val router = it.applicationContext as? ProductDetailRouter ?: return
            startActivityForResult(router.getProductTalk(it, productInfo?.basic?.id.toString()), REQUEST_CODE_TALK_PRODUCT)
        }
        if (productInfo != null) {
            //TODO SENT MOENGAGE
        }
    }

    private fun onReviewClicked() {
        productDetailTracking.eventReviewClicked()
        if (productInfo != null) {
            //TODO SENT MOENGAGE
            activity?.let {
                val router = it.applicationContext as? ProductDetailRouter ?: return
                startActivity(router.getProductReputationIntent(it, productInfo!!.basic.id.toString(),
                        productInfo!!.basic.name))
            }
        }
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

    fun getImageURIPaths(): ArrayList<String> {
        val arrayList = ArrayList(productInfo?.run { pictures?.map { it.urlOriginal } } ?: listOf())

        if (hasVariant()) {
            //TODO
            /*for (child in productVariant.getChildren()) {
                if (!TextUtils.isEmpty(child.getPicture().getOriginal()) && child.getProductId() !== productData.getInfo().getProductId()) {
                    arrayList.add(child.getPicture().getOriginal())
                }
            }
            val imagesSet = LinkedHashSet(arrayList)
            val finalImage = ArrayList<String>()
            finalImage.addAll(imagesSet)
            return finalImage*/
        }
        return arrayList
    }

    private fun onVariantClicked() {
        //go to variant Activity
        context?.let {
            productInfo?.run {
                val id = parentProductId
                startActivityForResult(NormalCheckoutActivity.getIntent(it,
                        basic.shopID.toString(),
                        parentProductId,
                        userInputNotes,
                        userInputQuantity,
                        userInputVariant,
                        ATC_AND_SELECT,
                        null), REQUEST_CODE_NORMAL_CHECKOUT);
            }
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
            val isWareHousing = productInfo!!.basic.status == ProductStatusTypeDef.WAREHOUSE

            menuCart.isVisible = !isOwned && !isSellerApp
            menuCart.isEnabled = !isOwned && !isSellerApp

            // handled on P2 when shop is loaded
            //TODO show visible/hide Warehouse by other criteria?
            if (isOwned) {
                if (isWareHousing) {
                    menuWarehouse.isVisible = false
                    menuWarehouse.isEnabled = false
                    menuEtalase.isEnabled = true
                    menuEtalase.isVisible = true
                } else {
                    menuWarehouse.isVisible = true
                    menuWarehouse.isEnabled = true
                    menuEtalase.isEnabled = false
                    menuEtalase.isVisible = false
                }
            }


            menuReport.isVisible = !isOwned && !isWareHousing
            menuReport.isEnabled = !isOwned && !isWareHousing
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
                val etalaseId = productInfo?.menu?.id.toString() ?: ""
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
            val router = (it.applicationContext as? ProductDetailRouter) ?: return
            if (productInfoViewModel.isUserSessionActive()) {
                startActivity(router.getCartIntent(it))
            } else {
                startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN),
                        REQUEST_CODE_LOGIN)
            }
            if (hasVariant())
                productDetailTracking.eventCartMenuClicked(""/*generated variant */)
        }
    }

    private fun hasVariant(): Boolean {
        //TODO CHECK VARIAN
        return false
    }

    private fun shareProduct() {
        if (productInfo == null && activity == null) return

        val productShare = ProductShare(activity!!)
        val productData = ProductData(
                productInfo!!.basic.price.getCurrencyFormatted(),
                "${productInfo!!.cashback.percentage}%",
                MethodChecker.fromHtml(productInfo!!.basic.name).toString(),
                productInfo!!.basic.priceCurrency,
                productInfo!!.basic.url,
                "",
                productInfo!!.basic.id.toString(),
                productInfo!!.pictures?.getOrNull(0)?.urlOriginal ?: ""
        )

        productShare.share(productData, {
            showProgressLoading()
        }) {
            hideProgressLoading()
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
                    cancelable = true,
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
}