package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.btm.BtmApi
import com.tokopedia.analytics.btm.Tokopedia
import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.analytics.byteio.IAdsLog
import com.tokopedia.analytics.byteio.IAppLogPdpActivity
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.TrackStayProductDetail
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.perf.BlocksPerformanceTrace
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper.Companion.PARAM_START_SUBID
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.ProductDetailPrefetch
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailLoadTimeMonitoringListener
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.tracking.ProductDetailServerLogger
import com.tokopedia.product.detail.view.fragment.ProductDetailFragment
import com.tokopedia.product.detail.view.fragment.ProductVideoDetailFragment
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * For navigating to this class
 * @see ApplinkConstInternalMarketplace.PRODUCT_DETAIL or
 * @see ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN
 */
open class ProductDetailActivity : BaseSimpleActivity(), ProductDetailActivityInterface, HasComponent<ProductDetailComponent>,
    IAppLogPdpActivity, AppLogInterface, IAdsLog {

    companion object {
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_SHOP_DOMAIN = "shop_domain"
        private const val PARAM_PRODUCT_KEY = "product_key"
        private const val PARAM_IS_FROM_DEEPLINK = "is_from_deeplink"
        private const val PARAM_TRACKER_ATTRIBUTION = "tracker_attribution"
        private const val PARAM_TRACKER_LIST_NAME = "tracker_list_name"
        private const val PARAM_AFFILIATE_STRING = "aff"
        private const val PARAM_AFFILIATE_UNIQUE_ID = "aff_unique_id"
        private const val PARAM_AFFILIATE_SOURCE = "source"
        private const val PARAM_LAYOUT_ID = "layoutID"
        const val PARAM_EXT_PARAM = "extParam"
        const val PARAM_CHANNEL = "channel"
        const val PRODUCT_PERFORMANCE_MONITORING_VARIANT_KEY = "isVariant"
        private const val PRODUCT_PERFORMANCE_MONITORING_VARIANT_VALUE = "variant"
        private const val PRODUCT_PERFORMANCE_MONITORING_NON_VARIANT_VALUE = "non-variant"
        private const val PRODUCT_VIDEO_DETAIL_TAG = "videoDetailTag"
        private const val PRODUCT_DETAIL_TAG = "productDetailTag"
        private const val P1_PREFETCH_PERF_TRACE_NAME = "pdp_p1_prefetch_perf_trace"
        private const val P1_CACHE_PERF_TRACE_NAME = "pdp_p1_cache_perf_trace"
        private const val P1_NETWORK_PERF_TRACE_NAME = "pdp_p1_network_perf_trace"

        const val P1_PREFETCH_KEY = "prefetch"
        const val P1_CACHE_KEY = "cache"
        const val P1_NETWORK_KEY = "network"

        private const val P1_state = "State"
        private const val P1_blocks_count = "blocks_count"

        private const val AFFILIATE_HOST = "affiliate"
        private const val PARAM_CAMPAIGN_ID = "campaign_id"
        private const val PARAM_VARIANT_ID = "variant_id"

        @JvmStatic
        fun createIntent(context: Context, productUrl: String) =
            Intent(context, ProductDetailActivity::class.java).apply {
                data = Uri.parse(productUrl)
            }

        @JvmStatic
        fun createIntent(context: Context, shopDomain: String, productKey: String) =
            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(PARAM_SHOP_DOMAIN, shopDomain)
                putExtra(PARAM_PRODUCT_KEY, productKey)
            }

        @JvmStatic
        fun createIntent(context: Context, productId: Long) =
            Intent(context, ProductDetailActivity::class.java).apply {
                putExtra(PARAM_PRODUCT_ID, productId.toString())
            }
    }

    private var isFromDeeplink = false
    private var shopDomain: String? = null
    private var productKey: String? = null
    private var productId: String? = null
    private var warehouseId: String? = null
    private var trackerAttribution: String? = null
    private var trackerListName: String? = null
    private var affiliateString: String? = null
    private var affiliateUniqueId: String? = null
    private var affiliateSubIds: Bundle? = null
    private var affiliateSource: String? = null
    private var deeplinkUrl: String? = null
    private var layoutId: String? = null
    private var extParam: String? = null
    private var affiliateChannel: String? = null
    private var userSessionInterface: UserSessionInterface? = null
    private var productDetailComponent: ProductDetailComponent? = null
    private var campaignId: String? = null
    private var variantId: String? = null
    private var prefetchDuration = 0L
    private var cacheDuration = 0L
    private var p1NetworkDuration = 0L

    // Performance Monitoring
    var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    private var performanceMonitoringP1: PerformanceMonitoring? = null
    private var performanceMonitoringP2Data: PerformanceMonitoring? = null

    private var p1PrefetchPerformanceMonitoring: PerformanceMonitoring? = PerformanceMonitoring()
    private var p1CachePerformanceMonitoring: PerformanceMonitoring? = PerformanceMonitoring()
    private var p1NetworkPerformanceMonitoring: PerformanceMonitoring? = PerformanceMonitoring()

    // Temporary (disscussion/talk, review/ulasan)
    private var performanceMonitoringP2Other: PerformanceMonitoring? = null
    private var performanceMonitoringP2Login: PerformanceMonitoring? = null

    private var blocksPerformanceTrace: BlocksPerformanceTrace? = null
    var productDetailLoadTimeMonitoringListener: ProductDetailLoadTimeMonitoringListener? = null

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    fun stopMonitoringP1() {
        performanceMonitoringP1?.stopTrace()
    }

    fun stopMonitoringP2Data() {
        performanceMonitoringP2Data?.stopTrace()
    }

    fun stopMonitoringP2Other() {
        performanceMonitoringP2Other?.stopTrace()
    }

    fun stopMonitoringP2Login() {
        performanceMonitoringP2Login?.stopTrace()
    }

    fun startMonitoringPltNetworkRequest() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    fun startMonitoringPltRenderPage() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    fun stopMonitoringPltRenderPage(isVariant: Boolean) {
        if (isVariant) {
            pageLoadTimePerformanceMonitoring?.addAttribution(
                PRODUCT_PERFORMANCE_MONITORING_VARIANT_KEY,
                PRODUCT_PERFORMANCE_MONITORING_VARIANT_VALUE
            )
        } else {
            pageLoadTimePerformanceMonitoring?.addAttribution(
                PRODUCT_PERFORMANCE_MONITORING_VARIANT_KEY,
                PRODUCT_PERFORMANCE_MONITORING_NON_VARIANT_VALUE
            )
        }
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        productDetailLoadTimeMonitoringListener?.onStopPltListener()
    }

    fun stopPLTRenderPageAndMonitoringP1(isVariant: Boolean) {
        stopMonitoringPltRenderPage(isVariant = isVariant)
        stopMonitoringP1()
    }

    fun getPltPerformanceResultData(): PltPerformanceData? =
        pageLoadTimePerformanceMonitoring?.getPltPerformanceData()

    fun getBlocksPerformanceMonitoring(): BlocksPerformanceTrace? = blocksPerformanceTrace
    fun goToHomePageClicked() {
        if (isTaskRoot) {
            RouteManager.route(this, ApplinkConst.HOME)
        } else {
            onBackPressed()
        }
        finish()
    }

    override fun getComponent(): ProductDetailComponent {
        return productDetailComponent ?: initializeComponent()
    }

    private fun initializeComponent(): ProductDetailComponent {
        val baseComponent = (applicationContext as BaseMainApplication).baseAppComponent
        return DaggerProductDetailComponent.builder()
            .baseAppComponent(baseComponent)
            .build()
    }

    override fun getParentViewResourceID(): Int {
        return R.id.product_detail_parent_view
    }

    fun addNewFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(parentViewResourceID, fragment, PRODUCT_VIDEO_DETAIL_TAG)
            .addToBackStack(PRODUCT_VIDEO_DETAIL_TAG)
            .commit()
        hidePdpFragment()
    }

    /**
     * Need to hide fragment to prevent fragment overdraw
     */
    private fun hidePdpFragment() {
        val fragmentVideoDetail = supportFragmentManager.findFragmentByTag(tagFragment)
        fragmentVideoDetail?.let {
            supportFragmentManager.beginTransaction().hide(it).commit()
        }
    }

    private fun showPdpFragment() {
        val fragmentVideoDetail = supportFragmentManager.findFragmentByTag(tagFragment)
        fragmentVideoDetail?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        }
    }

    override fun getTagFragment(): String {
        return PRODUCT_DETAIL_TAG
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            val fragmentVideoDetail =
                supportFragmentManager.findFragmentByTag(PRODUCT_VIDEO_DETAIL_TAG) as? ProductVideoDetailFragment
            if (fragmentVideoDetail?.isVisible == true) {
                showPdpFragment()
                fragmentVideoDetail.onBackButtonClicked()
            }
            supportFragmentManager.popBackStack()
        }
    }

    override fun getScreenName(): String {
        return "" // need only on success load data? (it needs custom dimension)
    }

    override fun getNewFragment(): Fragment = ProductDetailFragment.newInstance(
        productId,
        warehouseId,
        shopDomain,
        productKey,
        isFromDeeplink,
        trackerAttribution,
        trackerListName,
        affiliateString = affiliateString,
        affiliateUniqueId = affiliateUniqueId,
        deeplinkUrl = deeplinkUrl,
        layoutId = layoutId,
        extParam = extParam,
        query = getSource(),
        affiliateChannel = affiliateChannel,
        campaignId = campaignId,
        variantId = variantId,
        prefetchCacheId = intent.getStringExtra(ProductDetailPrefetch.PREFETCH_DATA_CACHE_ID),
        affiliateSubIds = affiliateSubIds,
        affiliateSource = affiliateSource
    )

    override fun getLayoutRes(): Int = R.layout.activity_product_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            initBlocksPLTMonitoring()
            userSessionInterface = UserSession(this)
            isFromDeeplink = intent.getBooleanExtra(PARAM_IS_FROM_DEEPLINK, false)

            parseApplink()
            initPLTMonitoring()
            initPerformanceMonitoring()
        } catch (e: Throwable) {
            onApplinkParseError(e)
        }

        productDetailComponent = initializeComponent()
        productDetailComponent?.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)
    }

    private fun parseApplink() {
        val uri = intent.data
        val bundle = intent.extras
        if (uri != null) {
            deeplinkUrl = generateApplink(uri.toString())
            if (uri.scheme == DeeplinkConstant.SCHEME_INTERNAL) {
                val segmentUri = uri.pathSegments
                if (segmentUri.size == 2) {
                    productId = uri.lastPathSegment
                } else {
                    shopDomain = segmentUri[segmentUri.size - 2]
                    productKey = segmentUri[segmentUri.size - 1]
                }
            } else if (uri.pathSegments.size >= 2 && // might be tokopedia.com/
                uri.host != AFFILIATE_HOST
            ) {
                val segmentUri = uri.pathSegments
                if (segmentUri.size > 1) {
                    shopDomain = segmentUri[segmentUri.size - 2]
                    productKey = segmentUri[segmentUri.size - 1]
                }
            } else { // affiliate
                productId = uri.lastPathSegment
            }
            trackerAttribution = uri.getQueryParameter(PARAM_TRACKER_ATTRIBUTION)
            trackerListName = uri.getQueryParameter(PARAM_TRACKER_LIST_NAME)
            affiliateString = uri.getQueryParameter(PARAM_AFFILIATE_STRING)
            affiliateUniqueId = uri.getQueryParameter(PARAM_AFFILIATE_UNIQUE_ID)
            affiliateSource = uri.getQueryParameter(PARAM_AFFILIATE_SOURCE)
            extParam = uri.getQueryParameter(PARAM_EXT_PARAM)
            affiliateChannel = uri.getQueryParameter(PARAM_CHANNEL)
            campaignId = uri.getQueryParameter(PARAM_CAMPAIGN_ID)
            variantId = uri.getQueryParameter(PARAM_VARIANT_ID)

            processAffiliateSubId(keys = uri.queryParameterNames, uri = uri)
        }
        bundle?.let {
            warehouseId = it.getString("warehouse_id")
            layoutId = it.getString(PARAM_LAYOUT_ID)

            if (productId.isNullOrBlank()) {
                productId = it.getString(PARAM_PRODUCT_ID)
            }
            if (shopDomain.isNullOrBlank()) {
                shopDomain = it.getString(PARAM_SHOP_DOMAIN)
            }
            if (productKey.isNullOrBlank()) {
                productKey = it.getString(PARAM_PRODUCT_KEY)
            }
            if (trackerAttribution.isNullOrBlank()) {
                trackerAttribution = it.getString(PARAM_TRACKER_ATTRIBUTION)
            }
            if (trackerListName.isNullOrBlank()) {
                trackerListName = it.getString(PARAM_TRACKER_LIST_NAME)
            }
            if (affiliateString.isNullOrBlank()) {
                affiliateString = it.getString(PARAM_AFFILIATE_STRING)
            }
            if (affiliateUniqueId.isNullOrBlank()) {
                affiliateUniqueId = it.getString(PARAM_AFFILIATE_UNIQUE_ID)
            }
            if (affiliateChannel.isNullOrBlank()) {
                affiliateChannel = it.getString(PARAM_CHANNEL)
            }
            if (affiliateSource.isNullOrBlank()) {
                affiliateSource = it.getString(PARAM_AFFILIATE_SOURCE)
            }
            if (affiliateSubIds == null) {
                processAffiliateSubId(keys = it.keySet(), bundle = it)
            }
        }

        if (productKey?.isNotEmpty() == true && shopDomain?.isNotEmpty() == true) {
            isFromDeeplink = true
        }
    }

    private fun onApplinkParseError(t: Throwable) {
        val uri = intent.data
        val uriString = uri?.toString() ?: ""
        ProductDetailServerLogger.logNewRelicProductCannotOpen(
            uriString,
            t
        )
        finish()
    }

    private fun initBlocksPLTMonitoring() {
        p1PrefetchPerformanceMonitoring?.startTrace(P1_PREFETCH_PERF_TRACE_NAME)
        p1CachePerformanceMonitoring?.startTrace(P1_CACHE_PERF_TRACE_NAME)
        p1NetworkPerformanceMonitoring?.startTrace(P1_NETWORK_PERF_TRACE_NAME)

        blocksPerformanceTrace = BlocksPerformanceTrace(
            activity = this,
            traceName = "perf_trace_pdp",
            scope = lifecycleScope,
            touchListenerActivity = this,
            onPerformanceTraceCancelled = { state ->
                p1PrefetchPerformanceMonitoring?.putCustomAttribute(P1_state, state.name)
                p1CachePerformanceMonitoring?.putCustomAttribute(P1_state, state.name)
                p1NetworkPerformanceMonitoring?.putCustomAttribute(P1_state, state.name)
            }
        ) { summaryModel, capturedBlocks -> }

        blocksPerformanceTrace?.onBlocksRendered =
            { summaryModel, capturedBlocks, elapsedTime, identifier ->
                recordP1PrefetchPerformance(capturedBlocks, identifier, elapsedTime)
                recordP1CachePerformance(capturedBlocks, identifier, elapsedTime)
                recordP1NetworkPerformance(capturedBlocks, identifier, elapsedTime)
            }
    }

    private fun recordP1PrefetchPerformance(
        capturedBlocks: Set<String>,
        identifier: String,
        elapsedTime: Long
    ) {
        if (capturedBlocks.isNotEmpty() && identifier == P1_PREFETCH_KEY && prefetchDuration == 0L) {
            this.prefetchDuration = elapsedTime
            p1PrefetchPerformanceMonitoring?.putMetric(
                P1_blocks_count,
                capturedBlocks.size.toLong()
            )
            p1PrefetchPerformanceMonitoring?.stopTrace()
            p1PrefetchPerformanceMonitoring = null
        }
    }

    private fun recordP1CachePerformance(
        capturedBlocks: Set<String>,
        identifier: String,
        elapsedTime: Long
    ) {
        if (identifier == P1_CACHE_KEY && cacheDuration == 0L) {
            this.cacheDuration = elapsedTime
            p1CachePerformanceMonitoring?.putMetric(P1_blocks_count, capturedBlocks.size.toLong())
            p1CachePerformanceMonitoring?.stopTrace()
            p1CachePerformanceMonitoring = null
        }
    }

    private fun recordP1NetworkPerformance(
        capturedBlocks: Set<String>,
        identifier: String,
        elapsedTime: Long
    ) {
        if (identifier == P1_NETWORK_KEY && p1NetworkDuration == 0L) {
            this.p1NetworkDuration = elapsedTime
            p1NetworkPerformanceMonitoring?.putMetric(P1_blocks_count, capturedBlocks.size.toLong())
            p1NetworkPerformanceMonitoring?.stopTrace()
            p1NetworkPerformanceMonitoring = null
        }
    }

    private fun initPLTMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
            ProductDetailConstant.PDP_RESULT_PLT_PREPARE_METRICS,
            ProductDetailConstant.PDP_RESULT_PLT_NETWORK_METRICS,
            ProductDetailConstant.PDP_RESULT_PLT_RENDER_METRICS
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(ProductDetailConstant.PDP_RESULT_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    private fun initPerformanceMonitoring() {
        performanceMonitoringP1 = PerformanceMonitoring.start(ProductDetailConstant.PDP_P1_TRACE)

        performanceMonitoringP2Data =
            PerformanceMonitoring.start(ProductDetailConstant.PDP_P2_DATA_TRACE)

        performanceMonitoringP2Other =
            PerformanceMonitoring.start(ProductDetailConstant.PDP_P2_OTHER_TRACE)

        if (userSessionInterface?.isLoggedIn == true) {
            performanceMonitoringP2Login =
                PerformanceMonitoring.start(ProductDetailConstant.PDP_P2_LOGIN_TRACE)
        }
    }

    private fun generateApplink(applink: String): String {
        return if (applink.contains(getString(tokopedia.applink.R.string.internal_scheme))) {
            applink.replace(getString(tokopedia.applink.R.string.internal_scheme), "tokopedia")
        } else {
            ""
        }
    }

    private fun getSource() = intent.data?.query ?: ""

    private fun processAffiliateSubId(keys: Set<String>, uri: Uri? = null, bundle: Bundle? = null) {
        if (uri == null && bundle == null) return

        keys.forEach {
            if (it.lowercase().startsWith(PARAM_START_SUBID)) {
                if (affiliateSubIds == null) affiliateSubIds = Bundle()
                affiliateSubIds?.putString(
                    it.substring(PARAM_START_SUBID.length),
                    uri?.getQueryParameter(it) ?: bundle?.getString(it) ?: ""
                )
            }
        }
    }

    override fun getProductTrack(): TrackStayProductDetail? {
        val pdpFragment = supportFragmentManager.findFragmentByTag(tagFragment) as? ProductDetailFragment
        return pdpFragment?.getStayAnalyticsData()
    }

    override fun getPageName(): String {
        return PageName.PDP
    }

    override fun getAdsPageName(): String {
        return PageName.PDP
    }

    override fun isEnterFromWhitelisted(): Boolean {
        return false

    }
}

interface ProductDetailActivityInterface {
    fun onBackPressed()
}
