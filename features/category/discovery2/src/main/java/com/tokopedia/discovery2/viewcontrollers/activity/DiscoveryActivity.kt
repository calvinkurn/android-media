package com.tokopedia.discovery2.viewcontrollers.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


const val NATIVE = "native"
const val REACT_NATIVE = "react-native"
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
    }

    override fun sendScreenAnalytics() {
        //Empty to remove double open screen events
    }

    override fun initView() {
        moveToRnIfRequired()
        toolbar?.hide()
        setObserver()
    }

    private fun moveToRnIfRequired() {
        if (config.isNotEmpty()) {
            if (config == NATIVE) {
                inflateFragment()
            } else {
                routeToReactNativeDiscovery()
            }
        } else {
            discoveryViewModel.getDiscoveryUIConfig()
        }
    }

    private fun setObserver() {
        discoveryViewModel.getDiscoveryUIConfigLiveData().observe(this, Observer {
            when (it) {
                is Success -> {
                    config = it.data
                    moveToRnIfRequired()
                }
            }
        })
    }

    override fun setupFragment(savedInstance: Bundle?) {
    }

    private fun routeToReactNativeDiscovery() {
        RouteManager.route(
                this,
                ApplinkConst.REACT_DISCOVERY_PAGE.replace("{page_id}", intent?.data?.lastPathSegment
                        ?: intent?.getStringExtra(END_POINT) ?: "")
        )
        finish()
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
                })
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