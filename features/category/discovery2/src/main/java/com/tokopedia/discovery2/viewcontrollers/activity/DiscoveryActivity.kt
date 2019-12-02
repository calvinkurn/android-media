package com.tokopedia.discovery2.viewcontrollers.activity

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.tradein_common.viewcontrollers.BaseViewModelActivity
import com.tokopedia.tradein_common.viewmodel.BaseViewModel

class DiscoveryActivity : BaseViewModelActivity<DiscoveryViewModel>() {

    private lateinit var discoveryViewModel: DiscoveryViewModel

    override fun getNewFragment(): Fragment? {
        val fragment = DiscoveryFragment()
        return fragment
    }

    override fun getViewModelType(): Class<DiscoveryViewModel> {
        return DiscoveryViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel?) {
        discoveryViewModel = viewModel as DiscoveryViewModel
    }

    override fun getVMFactory(): ViewModelProvider.AndroidViewModelFactory {
        return ViewModelProvider.AndroidViewModelFactory(this.application)
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