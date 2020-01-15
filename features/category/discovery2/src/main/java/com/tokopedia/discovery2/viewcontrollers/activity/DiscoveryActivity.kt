package com.tokopedia.discovery2.viewcontrollers.activity

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery.categoryrevamp.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.tradein_common.viewcontrollers.BaseViewModelActivity
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import javax.inject.Inject

class DiscoveryActivity : BaseViewModelActivity<DiscoveryViewModel>() {

    private lateinit var discoveryViewModel: DiscoveryViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        const val END_POINT = "end_point"
    }


    override fun initDaggerInject() {
        DaggerDiscoveryComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun getNewFragment(): Fragment? {
        return DiscoveryFragment.getInstance(intent?.data?.lastPathSegment)
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

    override fun getMenuRes(): Int {
        return -1
    }

    override fun getRootView(): View? {
        return null
    }

    override fun showProgressBar() {
    }

    override fun hideProgressBar() {
    }


    override fun getTncFragmentInstance(TncResId: Int): Fragment? {
        return null
    }

    override fun getBottomSheetLayoutRes(): Int {
        return 0
    }

    override fun doNeedReattach(): Boolean {
        return false
    }

}