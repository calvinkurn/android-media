package com.tokopedia.discovery2.viewcontrollers.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.discovery2.Utils.Companion.preSelectedTab
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.kotlin.extensions.view.hide
import javax.inject.Inject

const val DISCOVERY_RESULT_TRACE = "discovery_result_trace"
const val DISCOVERY_PLT_PREPARE_METRICS = "discovery_plt_prepare_metrics"
const val DISCOVERY_PLT_NETWORK_METRICS = "discovery_plt_network_metrics"
const val DISCOVERY_PLT_RENDER_METRICS = "discovery_plt_render_metrics"

class DiscoveryActivity : BaseViewModelActivity<DiscoveryViewModel>() {

    private lateinit var discoveryViewModel: DiscoveryViewModel

    @JvmField
    @Inject
    var pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        const val END_POINT = "end_point"
        const val SOURCE = "source"
        const val COMPONENT_ID = "componentID"
        const val ACTIVE_TAB = "activeTab"
        const val TARGET_COMP_ID = "targetcompID"
        const val PRODUCT_ID = "product_id"
        const val PIN_PRODUCT = "pinProduct"
        const val CATEGORY_ID = "category_id"
        const val EMBED_CATEGORY = "embedCategory"

        @JvmStatic
        fun createDiscoveryIntent(context: Context, endpoint: String): Intent {
            val intent = Intent(context, DiscoveryActivity::class.java)
            intent.putExtra(END_POINT, endpoint)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initDaggerInject()
        startPerformanceMonitoring()
        super.onCreate(savedInstanceState)
    }

    override fun sendScreenAnalytics() {
        //Empty to remove double open screen events
    }

    override fun initView() {
        inflateFragment()
        toolbar?.hide()
    }

    override fun setupFragment(savedInstance: Bundle?) {
    }

    private fun startPerformanceMonitoring() {
        pageLoadTimePerformanceInterface?.startMonitoring(DISCOVERY_RESULT_TRACE)
        pageLoadTimePerformanceInterface?.startPreparePagePerformanceMonitoring()
    }

    fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }


    override fun getNewFragment(): Fragment? {
        val intentData = intent?.data
        return DiscoveryFragment.getInstance(intentData?.lastPathSegment,
                intentData?.let { it ->
                    discoveryViewModel.getMapOfQueryParameter(it)
                }).apply {
            this.pageLoadTimePerformanceInterface = this@DiscoveryActivity.pageLoadTimePerformanceInterface
        }
    }

    override fun getViewModelType(): Class<DiscoveryViewModel> {
        return DiscoveryViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        discoveryViewModel = viewModel as DiscoveryViewModel
    }

    fun getViewModel(): DiscoveryViewModel {
        return discoveryViewModel
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelFactory
    }

    override fun setLogCrash() {
        super.setLogCrash()
        this.javaClass.canonicalName?.let { className ->
            if (!GlobalConfig.DEBUG) FirebaseCrashlytics.getInstance().log(className + " " + intent?.data?.lastPathSegment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        super.onStop()
        preSelectedTab = -1
    }

    fun getPltPerformanceResultData(): PltPerformanceData? {
        return pageLoadTimePerformanceInterface?.getPltPerformanceData()
    }
}