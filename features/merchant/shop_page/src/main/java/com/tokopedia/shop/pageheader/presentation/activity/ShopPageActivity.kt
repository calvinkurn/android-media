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
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_ACTIVITY_PREPARE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_MIDDLE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_PREPARE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.PltConstant.SHOP_TRACE_RENDER
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.SHOP_HEADER_TRACE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.SHOP_HOME_TAB_TRACE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.SHOP_HOME_WEB_VIEW_TRACE
import com.tokopedia.shop.common.constant.ShopPagePerformanceConstant.SHOP_PRODUCT_TAB_TRACE
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.info.view.activity.ShopInfoActivity
import com.tokopedia.shop.pageheader.presentation.fragment.InterfaceShopPageHeader
import com.tokopedia.shop.pageheader.presentation.fragment.NewShopPageFragment
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageFragment
import com.tokopedia.shop.pageheader.presentation.listener.ShopPagePerformanceMonitoringListener

class ShopPageActivity : BaseSimpleActivity(), HasComponent<ShopComponent>, ShopPagePerformanceMonitoringListener{

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_REF = "EXTRA_SHOP_REF"
        const val PATH_INFO = "info"
        const val FORCE_NOT_SHOWING_HOME_TAB = "FORCE_NOT_SHOWING_HOME_TAB"

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

    var bottomSheetSellerMigration: BottomSheetBehavior<LinearLayout>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring()
        checkIfAppLinkToShopInfo()
        checkIfApplinkRedirectedForMigration()
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_new_shop_page
    }

    override fun getNewFragment(): Fragment? {
        return if(ShopUtil.isUsingNewShopPageHeader(this))
            NewShopPageFragment.createInstance()
        else
            ShopPageFragment.createInstance()
    }

    override fun getComponent(): ShopComponent = ShopComponentHelper().getComponent(application, this)

    override fun onBackPressed() {
        super.onBackPressed()
        (fragment as? InterfaceShopPageHeader)?.onBackPressed()
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
        performanceMonitoringShop?.startCustomMetric(SHOP_TRACE_ACTIVITY_PREPARE)

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

    override fun getShopPageLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface? {
        return performanceMonitoringShop
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

    override fun invalidateMonitoringPlt(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface) {
        pageLoadTimePerformanceInterface.invalidate()
    }

    override fun startCustomMetric(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface, tag: String) {
        pageLoadTimePerformanceInterface.startCustomMetric(tag)
    }

    override fun stopCustomMetric(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface, tag: String) {
        pageLoadTimePerformanceInterface.stopCustomMetric(tag)
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }
}