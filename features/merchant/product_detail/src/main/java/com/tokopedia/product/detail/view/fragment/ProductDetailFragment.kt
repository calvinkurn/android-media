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
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.ToasterError
import com.tokopedia.design.component.ToasterNormal
import com.tokopedia.imagepreview.ImagePreviewActivity
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.createDefaultProgressDialog
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.MerchantVoucherDetailActivity
import com.tokopedia.merchantvoucher.voucherList.MerchantVoucherListActivity
import com.tokopedia.merchantvoucher.voucherList.widget.MerchantVoucherListWidget
import com.tokopedia.product.detail.ProductDetailRouter
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.ProductInfoP1
import com.tokopedia.product.detail.data.model.ProductInfoP2
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.product.ProductInfo
import com.tokopedia.product.detail.data.model.product.ProductParams
import com.tokopedia.product.detail.data.model.shop.ShopInfo
import com.tokopedia.product.detail.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PRD_STATE_ACTIVE
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.fragment.partialview.PartialAttributeInfoView
import com.tokopedia.product.detail.view.fragment.partialview.PartialProductDescrFullView
import com.tokopedia.product.detail.view.fragment.partialview.PartialVariantAndRateEstView
import com.tokopedia.product.detail.view.fragment.partialview.PartialImageReviewView
import com.tokopedia.product.detail.view.fragment.partialview.PartialMostHelpfulReviewView
import com.tokopedia.product.detail.view.fragment.partialview.PartialLatestTalkView
import com.tokopedia.product.detail.view.fragment.partialview.PartialHeaderView
import com.tokopedia.product.detail.view.fragment.partialview.PartialProductStatisticView
import com.tokopedia.product.detail.view.fragment.partialview.PartialShopView
import com.tokopedia.product.detail.view.fragment.partialview.PartialButtonActionView
import com.tokopedia.product.report.view.dialog.ReportDialogFragment
import com.tokopedia.product.detail.view.util.AppBarState
import com.tokopedia.product.detail.view.util.AppBarStateChangeListener
import com.tokopedia.product.detail.view.util.FlingBehavior
import com.tokopedia.product.detail.view.viewmodel.ProductInfoViewModel
import com.tokopedia.product.share.ProductData
import com.tokopedia.product.share.ProductShare
import com.tokopedia.product.warehouse.view.viewmodel.ProductWarehouseViewModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shopetalasepicker.constant.ShopParamConstant
import com.tokopedia.shopetalasepicker.view.activity.ShopEtalasePickerActivity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_product_detail.*
import kotlinx.android.synthetic.main.partial_product_detail_wholesale.*
import kotlinx.android.synthetic.main.partial_product_latest_talk.*
import kotlinx.android.synthetic.main.partial_variant_rate_estimation.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ProductDetailFragment : BaseDaggerFragment() {
    private var productId: String? = null
    private var productKey: String? = null
    private var shopDomain: String? = null
    private var isFromDeeplink: Boolean = false

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

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var productInfoViewModel: ProductInfoViewModel
    lateinit var productWarehouseViewModel: ProductWarehouseViewModel

    lateinit var performanceMonitoring: PerformanceMonitoring
    lateinit var remoteConfig: RemoteConfig

    private var isAppBarCollapsed = false
    private var menu: Menu? = null
    private var useVariant = true

    var loadingProgressDialog: ProgressDialog? = null

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

        private const val PDP_TRACE = "pdp_trace"
        private const val ENABLE_VARIANT = "mainapp_discovery_enable_pdp_variant"

        private const val ARG_PRODUCT_ID = "ARG_PRODUCT_ID"
        private const val ARG_PRODUCT_KEY = "ARG_PRODUCT_KEY"
        private const val ARG_SHOP_DOMAIN = "ARG_SHOP_DOMAIN"
        private const val ARG_FROM_DEEPLINK = "ARG_FROM_DEEPLINK"

        fun newInstance(productId: String? = null, productKey: String? = null,
                        shopDomain: String? = null, isFromDeeplink: Boolean = false) =
                ProductDetailFragment().also {
                    it.arguments = Bundle().apply {
                        productId?.let { putString(ARG_PRODUCT_ID, it) }
                        productKey?.let { putString(ARG_PRODUCT_KEY, it) }
                        shopDomain?.let { putString(ARG_SHOP_DOMAIN, it) }
                        putBoolean(ARG_FROM_DEEPLINK, isFromDeeplink)
                    }
                }
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(ProductDetailComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        performanceMonitoring = PerformanceMonitoring.start(PDP_TRACE)
        super.onCreate(savedInstanceState)
        arguments?.let {
            productId = it.getString(ARG_PRODUCT_ID)
            productKey = it.getString(ARG_PRODUCT_KEY)
            shopDomain = it.getString(ARG_SHOP_DOMAIN)
            isFromDeeplink = it.getBoolean(ARG_FROM_DEEPLINK, false)
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
            headerView = PartialHeaderView.build(view, activity)
        }

        if (!::partialVariantAndRateEstView.isInitialized) {
            partialVariantAndRateEstView = PartialVariantAndRateEstView.build(base_variant)
        }

        if (!::productStatsView.isInitialized) {
            productStatsView = PartialProductStatisticView.build(view)
        }

        if (!::productDescrView.isInitialized) {
            productDescrView = PartialProductDescrFullView.build(view, activity)
        }

        if (!::actionButtonView.isInitialized) {
            actionButtonView = PartialButtonActionView.build(view)
        }

        if (!::productShopView.isInitialized) {
            productShopView = PartialShopView.build(view)
        }

        if (!::attributeInfoView.isInitialized)
            attributeInfoView = PartialAttributeInfoView.build(view)

        if (!::imageReviewViewView.isInitialized)
            imageReviewViewView = PartialImageReviewView.build(view)

        if (!::mostHelpfulReviewView.isInitialized)
            mostHelpfulReviewView = PartialMostHelpfulReviewView.build(view)

        if (!::latestTalkView.isInitialized)
            latestTalkView = PartialLatestTalkView.build(base_latest_talk)
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

    }

    private fun collapsedAppBar() {
        initStatusBarLight()
        initToolbarLight()
        fab_detail.hide()
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

    private fun expandedAppBar() {
        initStatusBarDark()
        initToolbarTransparent()
        fab_detail.show()
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
            productInfoViewModel.getProductInfo(GraphqlHelper.loadRawString(resources, R.raw.gql_get_product_info),
                    GraphqlHelper.loadRawString(resources, R.raw.gql_get_product_info),
                    ProductParams(productId, shopDomain, productKey), resources)
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
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onDestroy() {
        productInfoViewModel.productInfoP1Resp.removeObservers(this)
        productInfoViewModel.productInfoP2resp.removeObservers(this)
        productInfoViewModel.productInfoP3resp.removeObservers(this)
        productInfoViewModel.clear()
        productWarehouseViewModel.clear()
        super.onDestroy()
    }

    private fun renderProductInfo3(productInfoP3: ProductInfoP3) {
        productInfoP3.rateEstimation?.let {
            partialVariantAndRateEstView.renderRateEstimation(it.rates,
                    shopInfo?.location ?: "")
        }
        shopInfo?.let { updateWishlist(it, productInfoP3.isWishlisted) }
        imageReviewViewView.renderData(productInfoP3.imageReviews)
        mostHelpfulReviewView.renderData(productInfoP3.helpfulReviews, productInfo?.stats?.countReview
                ?: 0)
        latestTalkView.renderData(productInfoP3.latestTalk, productInfo?.stats?.countTalk ?: 0,
                productInfo?.basic?.shopID ?: 0)
    }

    private fun renderProductInfo2(productInfoP2: ProductInfoP2) {
        productInfoP2.shopInfo?.let { shopInfo ->
            this.shopInfo = shopInfo
            productShopView.renderShop(shopInfo, productInfoViewModel.isShopOwner(shopInfo.shopCore.shopID.toInt()))
            val data = productInfo ?: return

            actionButtonView.renderData(data.basic.status,
                    (productInfoViewModel.isShopOwner(data.basic.shopID)
                            || shopInfo.isAllowManage == 1) && GlobalConfig.isSellerApp(),
                    data.preorder)
            actionButtonView.visibility = shopInfo.statusInfo.shopStatus == 1
            headerView.showOfficialStore(shopInfo.goldOS.isOfficial == 1)
            view_picture.renderShopStatus(shopInfo, productInfo?.basic?.status ?: PRD_STATE_ACTIVE)
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
        }
        productStatsView.renderRating(productInfoP2.rating)
        attributeInfoView.renderWishlistCount(productInfoP2.wishlistCount.count)
    }

    private fun updateWishlist(shopInfo: ShopInfo, wishlisted: Boolean) {
        context?.let {
            if (shopInfo.isAllowManage == 1) {
                fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_edit))
                fab_detail.setOnClickListener {
                    if (productInfo?.basic?.status != ProductDetailConstant.PRD_STATE_PENDING) {
                        gotoEditProduct()
                    } else {

                    }
                }
            } else if (wishlisted) {
                fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_wishlist_checked))
            } else {
                fab_detail.setImageDrawable(ContextCompat.getDrawable(it, R.drawable.ic_wishlist_unchecked))
            }
        }
    }

    private fun gotoEditProduct() {
        val id = productVariant?.parentId?.toString() ?: productId ?: return
        context?.let {
            if (it.applicationContext is ProductDetailRouter) {
                val intent = (it.applicationContext as ProductDetailRouter).goToEditProduct(it, true, id)
                startActivityForResult(intent, REQUEST_CODE_EDIT_PRODUCT)
            }
        }
    }

    private fun onErrorGetProductInfo(throwable: Throwable) {

    }

    private fun onSuccessGetProductInfo(productInfoP1: ProductInfoP1) {
        val data = productInfoP1.productInfo
        productId = data.basic.id.toString()
        productInfo = data
        headerView.renderData(data)
        view_picture.renderData(data.pictures, this::onPictureProductClicked)
        productStatsView.renderData(data, this::onReviewClicked, this::onDiscussionClicked)
        productDescrView.renderData(data)
        attributeInfoView.renderData(data)
        txt_last_update.text = getString(R.string.template_last_update_price, data.basic.lastUpdatePrice)
        txt_last_update.visible()

        if (data.wholesale.isNotEmpty()) {
            val minPrice = data.wholesale.sortedBy { it.price }.get(0).price
            label_min_wholesale.text = getString(R.string.label_format_wholesale, minPrice.getCurrencyFormatted())
            label_wholesale.visibility = View.VISIBLE
            label_min_wholesale.visibility = View.VISIBLE
            base_view_wholesale.visibility = View.VISIBLE
        } else {
            label_wholesale.visibility = View.GONE
            label_wholesale.visibility = View.GONE
            base_view_wholesale.visibility = View.GONE
        }
        onSuccessGetProductVariantInfo(productInfoP1.productVariant)
        activity?.invalidateOptionsMenu()
    }

    private fun onErrorGetProductVariantInfo(throwable: Throwable) {
        //TODO variant error
    }

    private fun onSuccessGetProductVariantInfo(data: ProductVariant?) {
        productVariant = data
        partialVariantAndRateEstView.productVariant = data
        partialVariantAndRateEstView.renderData(this::onVariantClicked)
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

    @SuppressLint("Range")
    private fun onErrorMoveToEtalase(throwable: Throwable) {
        hideProgressDialog()
        showToastError(throwable)
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
        val arrayList = ArrayList(productInfo?.run { pictures.map { it.urlOriginal } } ?: listOf())

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
//        context?.let {
//            startActivityForResult(ProductModalActivity.getIntent(it,
//                    productInfo,
//                    productVariant,
//                    0, "", BUY), REQUEST_CODE_BUY)
//        }

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
            val isWareHousing = productInfo!!.basic.status == ProductDetailConstant.PRD_STATE_WAREHOUSE

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
                productInfo!!.pictures.getOrNull(0)?.urlOriginal ?: ""
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

    private fun showProgressDialog(onCancelClicked: (() -> Unit)?) {
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
}