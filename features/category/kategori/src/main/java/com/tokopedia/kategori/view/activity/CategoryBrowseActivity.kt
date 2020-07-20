package com.tokopedia.kategori.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kategori.Constants.CATEGORY_PLT_NETWORK_METRICS
import com.tokopedia.kategori.Constants.CATEGORY_PLT_PREPARE_METRICS
import com.tokopedia.kategori.Constants.CATEGORY_PLT_RENDER_METRICS
import com.tokopedia.kategori.Constants.CATEGORY_RESULT_TRACE
import com.tokopedia.kategori.R
import com.tokopedia.kategori.analytics.CategoryAnalytics.Companion.categoryAnalytics
import com.tokopedia.kategori.view.PerformanceMonitoringListener
import com.tokopedia.kategori.view.fragments.CategoryLevelOneFragment
import com.tokopedia.kategori.view.fragments.CategoryLevelTwoFragment
import com.tokopedia.kategori.view.fragments.Listener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.trackingoptimizer.TrackingQueue
import kotlinx.android.synthetic.main.activity_category_browse.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class CategoryBrowseActivity : BaseSimpleActivity(), CategoryChangeListener, ActivityStateListener, PerformanceMonitoringListener {

    private val trackingQueue: TrackingQueue by lazy {
        TrackingQueue(this)
    }
    private lateinit var masterFragment: Fragment
    private lateinit var slaveFragment: Fragment
    private var deepLinkCategoryName: String = "0"
    private var toolbarName = "Kategori"
    private lateinit var globalError: GlobalError
    private val EXTRA_CATEGORY_NAME = "CATEGORY_NAME"
    private var pageLoadTimePerformanceMonitoring: PageLoadTimePerformanceInterface? = null

    override fun getScreenName(): String = getString(R.string.belanja_screen_name)

    override fun getNewFragment(): Fragment {
        return CategoryLevelTwoFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        startPerformanceMonitoring()
        val uri = intent.data
        uri?.getQueryParameter(EXTRA_CATEGORY_NAME)?.let {
            deepLinkCategoryName = it
        }
        super.onCreate(savedInstanceState)
    }

    override fun startPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring = PageLoadTimePerformanceCallback(
                CATEGORY_PLT_PREPARE_METRICS,
                CATEGORY_PLT_NETWORK_METRICS,
                CATEGORY_PLT_RENDER_METRICS,0,0,0,0,null
        )
        pageLoadTimePerformanceMonitoring?.startMonitoring(CATEGORY_RESULT_TRACE)
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun onPause() {
        super.onPause()
        getActivityTrackingQueue().sendAll()
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        toolbar.setNavigationIcon(R.drawable.cat_ic_action_back)
        updateTitle(toolbarName)
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    override fun setupFragment(savedInstance: Bundle?) {
        initView()
        inflateFragment()
    }

    private fun initView() {
        globalError = findViewById(R.id.global_error)
    }

    override fun inflateFragment() {
        slaveFragment = CategoryLevelTwoFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.slave_view, slaveFragment, tagFragment)
                .commit()


        masterFragment = CategoryLevelOneFragment.newInstance(deepLinkCategoryName)
        supportFragmentManager.beginTransaction()
                .replace(R.id.master_view, masterFragment, tagFragment)
                .commit()
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is CategoryLevelOneFragment) {
            fragment.activityStateListener = this
            fragment.performanceMonitoringListener = this
        } else if (fragment is CategoryLevelTwoFragment) {
            fragment.activityStateListener = this
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_category_browse
    }

    override fun onCategoryChanged(id: String, categoryName: String, applink: String?) {
        (slaveFragment as Listener).refreshView(id, categoryName, applink)
    }

    override fun onError(e: Throwable) {
        slave_view.hide()
        master_view.hide()

        if (e is UnknownHostException
                || e is SocketTimeoutException) {
            globalError.setType(GlobalError.NO_CONNECTION)
        } else {
            globalError.setType(GlobalError.SERVER_ERROR)
        }

        globalError.show()

        globalError.setOnClickListener {
            slave_view.show()
            master_view.show()
            globalError.hide()
            (masterFragment as CategoryLevelOneFragment).reloadData()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            categoryAnalytics.eventBackButtonClick()
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getActivityTrackingQueue(): TrackingQueue {
        return trackingQueue
    }

    override fun startPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startPreparePagePerformanceMonitoring()
    }

    override fun stopPreparePagePerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.startRenderPerformanceMonitoring()
    }

    override fun stopRenderPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopRenderPerformanceMonitoring()
    }

    override fun stopPerformanceMonitoring() {
        pageLoadTimePerformanceMonitoring?.stopMonitoring()
        pageLoadTimePerformanceMonitoring = null
    }
}

interface ActivityStateListener {
    fun getActivityTrackingQueue(): TrackingQueue
}

interface CategoryChangeListener {
    fun onCategoryChanged(id: String, categoryName: String, applink: String?)
    fun onError(e: Throwable)
}
