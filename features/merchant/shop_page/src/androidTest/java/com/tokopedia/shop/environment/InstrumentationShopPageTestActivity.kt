package com.tokopedia.shop.environment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageFragment
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageHeaderPerformanceMonitoringListener
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageHomeTabPerformanceMonitoringListener
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageProductTabPerformanceMonitoringListener
import com.tokopedia.shop.test.R

class InstrumentationShopPageTestActivity : AppCompatActivity(),
        ShopPageHeaderPerformanceMonitoringListener,
        ShopPageHomeTabPerformanceMonitoringListener,
        ShopPageProductTabPerformanceMonitoringListener {

    private var performanceMonitoringShop: PageLoadTimePerformanceInterface? = null
    private var shopPageHeaderLoadTimePerformanceCallback: PageLoadTimePerformanceInterface? = null
    private var shopPageHomeTabLoadTimePerformanceCallback: PageLoadTimePerformanceInterface? = null
    private var shopPageProductTabLoadTimePerformanceCallback: PageLoadTimePerformanceInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initPerformanceMonitoring()
        initShopPageHeaderPerformanceMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instrumentation_shop_page_test)

        val shopPageFragment: Fragment = ShopPageFragment()
        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
        fragmentTransaction
                .replace(R.id.container, shopPageFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun initPerformanceMonitoring() {
        performanceMonitoringShop = PageLoadTimePerformanceCallback(
                "prepare",
                "network",
                "render"
        )
        performanceMonitoringShop?.startMonitoring("mp_shop")
        performanceMonitoringShop?.startPreparePagePerformanceMonitoring()
    }

    override fun getShopPageLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface? {
        return performanceMonitoringShop
    }

    override fun initShopPageHeaderPerformanceMonitoring() {
        shopPageHeaderLoadTimePerformanceCallback = PageLoadTimePerformanceCallback(
                "prepare",
                "network",
                "render"
        )
        shopPageHeaderLoadTimePerformanceCallback?.startMonitoring("mp_shop_header")
        shopPageHeaderLoadTimePerformanceCallback?.startPreparePagePerformanceMonitoring()
    }

    override fun getShopPageHeaderLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface? {
        return shopPageHeaderLoadTimePerformanceCallback
    }

    override fun initShopPageHomeTabPerformanceMonitoring() {
        shopPageHomeTabLoadTimePerformanceCallback = PageLoadTimePerformanceCallback(
                "prepare",
                "network",
                "render"
        )
        shopPageHomeTabLoadTimePerformanceCallback?.startMonitoring("mp_shop_home")
        shopPageHomeTabLoadTimePerformanceCallback?.startPreparePagePerformanceMonitoring()
    }

    override fun getShopPageHomeTabLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface? {
        return shopPageHomeTabLoadTimePerformanceCallback
    }

    override fun initShopPageProductTabPerformanceMonitoring() {
        shopPageProductTabLoadTimePerformanceCallback = PageLoadTimePerformanceCallback(
                "prepare",
                "network",
                "render"
        )
        shopPageProductTabLoadTimePerformanceCallback?.startMonitoring("mp_shop_product")
        shopPageProductTabLoadTimePerformanceCallback?.startPreparePagePerformanceMonitoring()
    }

    override fun getShopPageProductTabLoadTimePerformanceCallback(): PageLoadTimePerformanceInterface? {
        return shopPageProductTabLoadTimePerformanceCallback
    }

    override fun startMonitoringPltNetworkRequest(pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface) {
        pageLoadTimePerformanceInterface.stopPreparePagePerformanceMonitoring()
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

}
