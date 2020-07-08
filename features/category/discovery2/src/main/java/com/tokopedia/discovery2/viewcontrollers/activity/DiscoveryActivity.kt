package com.tokopedia.discovery2.viewcontrollers.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.crashlytics.android.Crashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


const val NATIVE = "native"
const val REACT_NATIVE = "react-native"
private const val TAG = "DiscoveryActivity"

class DiscoveryActivity : BaseViewModelActivity<DiscoveryViewModel>() {

    private lateinit var discoveryViewModel: DiscoveryViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        const val END_POINT = "end_point"

        @JvmField
        var config: String = NATIVE

        @JvmStatic
        fun createDiscoveryIntent(context: Context, endpoint: String): Intent {
            val intent = Intent(context, DiscoveryActivity::class.java)
            intent.putExtra(END_POINT, endpoint)
            return intent
        }
    }

    override fun sendScreenAnalytics() {
        //Empty to remove double open screen events
    }

    override fun initView() {
        if (config != NATIVE) {
            routeToReactNativeDiscovery()
        }
        toolbar?.hide()
        setObserver()
        //discoveryViewModel.getDiscoveryUIConfig()
    }

    private fun setObserver() {
        discoveryViewModel.getDiscoveryUIConfigLiveData().observe(this, Observer {
            when (it) {
                is Success -> {
                    config = it.data
                    if (it.data != NATIVE) {
                        routeToReactNativeDiscovery()
                    }
                }
            }
        })
    }

    private fun routeToReactNativeDiscovery() {
        RouteManager.route(
                this,
                ApplinkConst.REACT_DISCOVERY_PAGE.replace("{page_id}", intent?.data?.lastPathSegment
                        ?: intent?.getStringExtra(END_POINT) ?: "")
        )
        finish()
    }

    override fun initInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }


    override fun getNewFragment(): Fragment? {
        return DiscoveryFragment.getInstance(intent?.data?.lastPathSegment
                ?: intent?.getStringExtra(END_POINT))
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
}