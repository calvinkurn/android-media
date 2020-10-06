package com.tokopedia.discovery2.viewcontrollers.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.crashlytics.android.Crashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.discovery2.Utils.Companion.preSelectedTab
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.discovery2.viewmodel.DiscoveryConfigViewModel
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.kotlin.extensions.view.hide
import javax.inject.Inject


const val NATIVE = "native"
const val REACT_NATIVE = "react-native"
const val DISCOVERY_RESULT_TRACE = "discovery_result_trace"
const val DISCOVERY_PLT_PREPARE_METRICS = "discovery_plt_prepare_metrics"
const val DISCOVERY_PLT_NETWORK_METRICS = "discovery_plt_network_metrics"
const val DISCOVERY_PLT_RENDER_METRICS = "discovery_plt_render_metrics"

class DiscoveryActivity : BaseViewModelActivity<DiscoveryViewModel>() {

    private lateinit var discoveryViewModel: DiscoveryViewModel

    @Inject
    lateinit var discoveryConfigViewModel: DiscoveryConfigViewModel

    @JvmField
    @Inject
    var pageLoadTimePerformanceInterface: PageLoadTimePerformanceInterface? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        const val END_POINT = "end_point"
        const val SOURCE_QUERY = "source"
        const val PINNED_COMPONENT_ID = "componentID"
        const val PINNED_ACTIVE_TAB = "activeTab"
        const val PINNED_COMP_ID = "pinnedcompID"
        const val PRODUCT_ID = "product_id"

        @JvmField
        var config: String = ""

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
        discoveryConfigViewModel.doOnCreate()
    }

    override fun initView() {
        toolbar?.hide()
        setObserver()
    }

    private fun setObserver() {
        discoveryConfigViewModel.getDiscoveryNativeConfigLiveData().observe(this, Observer{
            routeToNative()
        })
        discoveryConfigViewModel.getDiscoveryRNConfigLiveData().observe(this, Observer {
            routeToReactNativeDiscovery()
        })
    }

    private fun routeToNative() {
        inflateFragment()
        sendDiscoveryLaunchBradcast(true)
    }

    private fun routeToReactNativeDiscovery() {
        RouteManager.route(
                this,
                ApplinkConst.REACT_DISCOVERY_PAGE.replace("{page_id}", intent?.data?.lastPathSegment
                        ?: intent?.getStringExtra(END_POINT) ?: "")
        )
        sendDiscoveryLaunchBradcast(false)
        finish()
    }

    private fun sendDiscoveryLaunchBradcast(isNative:Boolean) {
        LocalBroadcastManager.getInstance(this).apply {
            val DISCO_INTENT_FILTER = "DISCO_ACTIVITY_SELECTION"
            sendBroadcast(Intent(DISCO_INTENT_FILTER).apply {
                val DISCO_IS_NATIVE = "DISCO_IS_NATIVE"
                putExtra(DISCO_IS_NATIVE,isNative)
            })
        }
    }
    override fun sendScreenAnalytics() {
        //Empty to remove double open screen events
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
            if (!GlobalConfig.DEBUG) Crashlytics.log(className + " " + intent?.data?.lastPathSegment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        super.onStop()
        preSelectedTab = -1
    }

}