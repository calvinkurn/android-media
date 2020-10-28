package com.tokopedia.shop.pageheader.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.config.GlobalConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_HEADER_RESULT_PLT_NETWORK_METRICS
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_HEADER_RESULT_PLT_PREPARE_METRICS
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_HEADER_RESULT_PLT_RENDER_METRICS
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_HEADER_RESULT_TRACE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_HOME_TAB_RESULT_PLT_NETWORK_METRICS
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_HOME_TAB_RESULT_PLT_PREPARE_METRICS
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_HOME_TAB_RESULT_PLT_RENDER_METRICS
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_HOME_TAB_RESULT_TRACE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_PRODUCT_TAB_RESULT_PLT_NETWORK_METRICS
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_PRODUCT_TAB_RESULT_PLT_PREPARE_METRICS
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_PRODUCT_TAB_RESULT_PLT_RENDER_METRICS
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_PAGE_PRODUCT_TAB_RESULT_TRACE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_MIDDLE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_PREPARE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_RENDER
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.SHOP_HEADER_TRACE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.SHOP_HOME_TAB_TRACE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.SHOP_HOME_WEB_VIEW_TRACE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.SHOP_PRODUCT_TAB_TRACE
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.info.view.activity.ShopInfoActivity
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageFragment
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageHeaderPerformanceMonitoringListener
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageHomeTabPerformanceMonitoringListener
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageProductTabPerformanceMonitoringListener

class ShopPageActivity : BaseSimpleActivity(), HasComponent<ShopComponent>,
        ShopPageHeaderPerformanceMonitoringListener,
        ShopPageHomeTabPerformanceMonitoringListener,
        ShopPageProductTabPerformanceMonitoringListener {

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_REF = "EXTRA_SHOP_REF"
        const val PATH_INFO = "info"

        @JvmStatic
        fun createIntent(context: Context, shopId: String, shopRef: String) = Intent(context, ShopPageActivity::class.java)
                .apply {
                    putExtra(SHOP_ID, shopId)
                    putExtra(SHOP_REF, shopRef)
                }
    }

    private val sellerMigrationDestinationApplink by lazy {
        intent?.getStringExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA)
    }

    private var performanceMonitoringShop: PageLoadTimePerformanceInterface? = null
    private var performanceMonitoringShopHeader: PerformanceMonitoring? = null
    private var performanceMonitoringShopProductTab: PerformanceMonitoring? = null
    private var performanceMonitoringShopHomeTab: PerformanceMonitoring? = null
    private var performanceMonitoringShopHomeWebViewTab: PerformanceMonitoring? = null
    private var shopPageHeaderLoadTimePerformanceCallback: PageLoadTimePerformanceInterface? = null
    private var shopPageHomeTabLoadTimePerformanceCallback: PageLoadTimePerformanceInterface? = null
    private var shopPageProductTabLoadTimePerformanceCallback: PageLoadTimePerformanceInterface? = null

    var bottomSheetSellerMigration: BottomSheetBehavior<LinearLayout>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initShopPageHeaderPerformanceMonitoring()
        initPerformanceMonitoring()
        checkIfAppLinkToShopInfo()
        checkIfApplinkRedirectedForMigration()
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_new_shop_page
    }

    override fun getNewFragment(): Fragment? {
        return ShopPageFragment.createInstance()
    }

    override fun getComponent(): ShopComponent = ShopComponentHelper().getComponent(application, this)

    override fun onBackPressed() {
        super.onBackPressed()
        (fragment as? ShopPageFragment)?.onBackPressed()
    }

    override fun getShopPageLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface? {
        return performanceMonitoringShop
    }

    fun stopShopHeaderPerformanceMonitoring() {
        performanceMonitoringShopHeader?.stopTrace()
    }

    fun stopShopProductTabPerformanceMonitoring() {
        performanceMonitoringShopProductTab?.stopTrace()
    }

    fun stopShopHomeTabPerformanceMonitoring() {
        performanceMonitoringShopHomeTab?.stopTrace()
    }

    fun stopShopHomeWebViewTabPerformanceMonitoring() {
        performanceMonitoringShopHomeWebViewTab?.stopTrace()
    }

    private fun initPerformanceMonitoring() {
        performanceMonitoringShop = PageLoadTimePerformanceCallback(
                SHOP_TRACE_PREPARE,
                SHOP_TRACE_MIDDLE,
                SHOP_TRACE_RENDER
        )
        performanceMonitoringShop?.startMonitoring(SHOP_TRACE)
        performanceMonitoringShop?.startPreparePagePerformanceMonitoring()

        performanceMonitoringShopHeader = PerformanceMonitoring.start(SHOP_HEADER_TRACE)
        performanceMonitoringShopProductTab = PerformanceMonitoring.start(SHOP_PRODUCT_TAB_TRACE)
        performanceMonitoringShopHomeTab = PerformanceMonitoring.start(SHOP_HOME_TAB_TRACE)
        performanceMonitoringShopHomeWebViewTab = PerformanceMonitoring.start(SHOP_HOME_WEB_VIEW_TRACE)
    }

    private fun checkIfAppLinkToShopInfo() {
        intent?.data?.let {
            if (it.lastPathSegment.orEmpty() == PATH_INFO) {
                val shopId = it.pathSegments.getOrNull(1) ?: ""
                val intent = getShopInfoIntent(this).putExtra(SHOP_ID, shopId)
                startActivity(intent)
            }
        }
    }

    private fun checkIfApplinkRedirectedForMigration() {
        if (GlobalConfig.isSellerApp()) {
            sellerMigrationDestinationApplink?.let { applink ->
                intent?.removeExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA)
                RouteManager.getIntent(this, applink).run {
                    addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(this)
                }
            }
        }
    }

    private fun getShopInfoIntent(context: Context): Intent {
        return Intent(context, ShopInfoActivity::class.java)
    }

    override fun initShopPageHeaderPerformanceMonitoring() {
        shopPageHeaderLoadTimePerformanceCallback = PageLoadTimePerformanceCallback(
                SHOP_PAGE_HEADER_RESULT_PLT_PREPARE_METRICS,
                SHOP_PAGE_HEADER_RESULT_PLT_NETWORK_METRICS,
                SHOP_PAGE_HEADER_RESULT_PLT_RENDER_METRICS
        )
        performanceMonitoringShop?.startMonitoring(SHOP_PAGE_HEADER_RESULT_TRACE)
        performanceMonitoringShop?.startPreparePagePerformanceMonitoring()
    }

    override fun initShopPageHomeTabPerformanceMonitoring() {
        shopPageHomeTabLoadTimePerformanceCallback = PageLoadTimePerformanceCallback(
                SHOP_PAGE_HOME_TAB_RESULT_PLT_PREPARE_METRICS,
                SHOP_PAGE_HOME_TAB_RESULT_PLT_NETWORK_METRICS,
                SHOP_PAGE_HOME_TAB_RESULT_PLT_RENDER_METRICS
        )
        shopPageHomeTabLoadTimePerformanceCallback?.startMonitoring(SHOP_PAGE_HOME_TAB_RESULT_TRACE)
        shopPageHomeTabLoadTimePerformanceCallback?.startPreparePagePerformanceMonitoring()
    }

    override fun initShopPageProductTabPerformanceMonitoring() {
        shopPageProductTabLoadTimePerformanceCallback = PageLoadTimePerformanceCallback(
                SHOP_PAGE_PRODUCT_TAB_RESULT_PLT_PREPARE_METRICS,
                SHOP_PAGE_PRODUCT_TAB_RESULT_PLT_NETWORK_METRICS,
                SHOP_PAGE_PRODUCT_TAB_RESULT_PLT_RENDER_METRICS
        )
        shopPageProductTabLoadTimePerformanceCallback?.startMonitoring(SHOP_PAGE_PRODUCT_TAB_RESULT_TRACE)
        shopPageProductTabLoadTimePerformanceCallback?.startPreparePagePerformanceMonitoring()
    }

    override fun getShopPageHeaderLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface? {
        return shopPageHeaderLoadTimePerformanceCallback
    }

    override fun getShopPageHomeTabLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface? {
        return shopPageHomeTabLoadTimePerformanceCallback
    }


    override fun getShopPageProductTabLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface? {
        return shopPageProductTabLoadTimePerformanceCallback
    }

    override fun stopMonitoringPltPreparePage(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface) {
        pageLoadTimePerformanceInterface.stopPreparePagePerformanceMonitoring()
    }

    override fun startMonitoringPltNetworkRequest(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface) {
        pageLoadTimePerformanceInterface.startNetworkRequestPerformanceMonitoring()
    }

    override fun startMonitoringPltRenderPage(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface) {
        pageLoadTimePerformanceInterface.stopNetworkRequestPerformanceMonitoring()
        pageLoadTimePerformanceInterface.startRenderPerformanceMonitoring()
    }

    override fun stopMonitoringPltRenderPage(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface) {
        pageLoadTimePerformanceInterface.stopRenderPerformanceMonitoring()
        pageLoadTimePerformanceInterface.stopMonitoring()
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

}