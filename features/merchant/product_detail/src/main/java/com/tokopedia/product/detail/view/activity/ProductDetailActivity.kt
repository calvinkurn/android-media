package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailLoadTimeMonitoringListener
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment
import com.tokopedia.product.detail.view.fragment.ProductVideoDetailFragment
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface


/**
 * For navigating to this class
 * @see ApplinkConstInternalMarketplace.PRODUCT_DETAIL or
 * @see ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN
 */
open class ProductDetailActivity : BaseSimpleActivity(), ProductDetailActivityInterface, HasComponent<ProductDetailComponent> {

    companion object {
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_SHOP_DOMAIN = "shop_domain"
        private const val PARAM_PRODUCT_KEY = "product_key"
        private const val PARAM_IS_FROM_DEEPLINK = "is_from_deeplink"
        private const val IS_FROM_EXPLORE_AFFILIATE = "is_from_explore_affiliate"
        private const val PARAM_TRACKER_ATTRIBUTION = "tracker_attribution"
        private const val PARAM_TRACKER_LIST_NAME = "tracker_list_name"
        private const val PARAM_AFFILIATE_STRING = "aff"
        private const val PARAM_LAYOUT_ID = "layoutID"
        const val PRODUCT_PERFORMANCE_MONITORING_VARIANT_KEY = "isVariant"
        private const val PRODUCT_PERFORMANCE_MONITORING_VARIANT_VALUE = "variant"
        private const val PRODUCT_PERFORMANCE_MONITORING_NON_VARIANT_VALUE = "non-variant"
        private const val PRODUCT_VIDEO_DETAIL_TAG = "videoDetailTag"
        private const val PRODUCT_DETAIL_TAG = "productDetailTag"

        private const val AFFILIATE_HOST = "affiliate"

        @JvmStatic
        fun createIntent(context: Context, productUrl: String) =
                Intent(context, ProductDetailActivity::class.java).apply {
                    data = Uri.parse(productUrl)
                }

        @JvmStatic
        fun createIntent(context: Context, shopDomain: String, productKey: String) = Intent(context, ProductDetailActivity::class.java).apply {
            putExtra(PARAM_SHOP_DOMAIN, shopDomain)
            putExtra(PARAM_PRODUCT_KEY, productKey)
        }

        @JvmStatic
        fun createIntent(context: Context, productId: Long) = Intent(context, ProductDetailActivity::class.java).apply {
            putExtra(PARAM_PRODUCT_ID, productId.toString())
        }
    }

    private var isFromDeeplink = false
    private var isFromAffiliate: Boolean? = false
    private var shopDomain: String? = null
    private var productKey: String? = null
    private var productId: String? = null
    private var warehouseId: String? = null
    private var trackerAttribution: String? = null
    private var trackerListName: String? = null
    private var affiliateString: String? = null
    private var deeplinkUrl: String? = null
    private var layoutId: String? = null
    private var userSessionInterface: UserSessionInterface? = null
    private var productDetailComponent: ProductDetailComponent? = null
    var remoteConfig: RemoteConfig? = null

    //Performance Monitoring
    var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null
    private var performanceMonitoringP1: PerformanceMonitoring? = null
    private var performanceMonitoringP2Data: PerformanceMonitoring? = null

    //Temporary (disscussion/talk, review/ulasan)
    private var performanceMonitoringP2Other: PerformanceMonitoring? = null
    private var performanceMonitoringP2Login: PerformanceMonitoring? = null
    private var performanceMonitoringFull: PerformanceMonitoring? = null

    var productDetailLoadTimeMonitoringListener: ProductDetailLoadTimeMonitoringListener? = null

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

    fun stopMonitoringFull() {
        performanceMonitoringFull?.stopTrace()
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
            pageLoadTimePerformanceMonitoring?.addAttribution(PRODUCT_PERFORMANCE_MONITORING_VARIANT_KEY, PRODUCT_PERFORMANCE_MONITORING_VARIANT_VALUE)
        } else {
            pageLoadTimePerformanceMonitoring?.addAttribution(PRODUCT_PERFORMANCE_MONITORING_VARIANT_KEY, PRODUCT_PERFORMANCE_MONITORING_NON_VARIANT_VALUE)
        }
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        productDetailLoadTimeMonitoringListener?.onStopPltListener()
    }

    fun getPltPerformanceResultData(): PltPerformanceData? = pageLoadTimePerformanceMonitoring?.getPltPerformanceData()

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
        supportFragmentManager.beginTransaction().add(parentViewResourceID, fragment, PRODUCT_VIDEO_DETAIL_TAG)
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
            val fragmentVideoDetail = supportFragmentManager.findFragmentByTag(PRODUCT_VIDEO_DETAIL_TAG) as? ProductVideoDetailFragment
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

    override fun getNewFragment(): Fragment = DynamicProductDetailFragment.newInstance(productId, warehouseId, shopDomain,
            productKey, isFromDeeplink,
            isFromAffiliate ?: false, trackerAttribution,
            trackerListName, affiliateString, deeplinkUrl, layoutId)

    override fun getLayoutRes(): Int = R.layout.activity_product_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        userSessionInterface = UserSession(this)
        remoteConfig = FirebaseRemoteConfigImpl(this)
        isFromDeeplink = intent.getBooleanExtra(PARAM_IS_FROM_DEEPLINK, false)
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
                    uri.host != AFFILIATE_HOST) {
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
            isFromAffiliate = !uri.getQueryParameter(IS_FROM_EXPLORE_AFFILIATE).isNullOrEmpty()
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
        }

        if (productKey?.isNotEmpty() == true && shopDomain?.isNotEmpty() == true) {
            isFromDeeplink = true
        }
        initPLTMonitoring()
        initPerformanceMonitoring()

        super.onCreate(savedInstanceState)
    }

    private fun initPLTMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                ProductDetailConstant.PDP_RESULT_PLT_PREPARE_METRICS,
                ProductDetailConstant.PDP_RESULT_PLT_NETWORK_METRICS,
                ProductDetailConstant.PDP_RESULT_PLT_RENDER_METRICS)
        pageLoadTimePerformanceMonitoring?.startMonitoring(ProductDetailConstant.PDP_RESULT_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    private fun initPerformanceMonitoring() {
        performanceMonitoringP1 = PerformanceMonitoring.start(ProductDetailConstant.PDP_P1_TRACE)
        performanceMonitoringP2Data = PerformanceMonitoring.start(ProductDetailConstant.PDP_P2_DATA_TRACE)
        performanceMonitoringP2Other = PerformanceMonitoring.start(ProductDetailConstant.PDP_P2_OTHER_TRACE)

        if (userSessionInterface?.isLoggedIn == true) {
            performanceMonitoringP2Login = PerformanceMonitoring.start(ProductDetailConstant.PDP_P2_LOGIN_TRACE)
            performanceMonitoringFull = PerformanceMonitoring.start(ProductDetailConstant.PDP_P3_TRACE)
        }
    }

    private fun generateApplink(applink: String): String {
        return if (applink.contains(getString(tokopedia.applink.R.string.internal_scheme))) {
            applink.replace(getString(tokopedia.applink.R.string.internal_scheme), "tokopedia")
        } else {
            ""
        }
    }
}

interface ProductDetailActivityInterface {
    fun onBackPressed()
}