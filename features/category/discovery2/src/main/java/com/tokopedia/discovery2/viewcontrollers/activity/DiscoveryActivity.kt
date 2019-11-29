package com.tokopedia.discovery2.viewcontrollers.activity

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.tradein_common.viewcontrollers.BaseViewModelActivity
import com.tokopedia.tradein_common.viewmodel.BaseViewModel

class DiscoveryActivity : BaseViewModelActivity<DiscoveryViewModel>() {

    private lateinit var discoveryViewModel: DiscoveryViewModel

    override fun getNewFragment(): Fragment? {
        return DiscoveryFragment()
    }

    override fun setViewModel(viewModel: BaseViewModel?) {
        discoveryViewModel = viewModel as DiscoveryViewModel
    }

    override fun getViewModelType(): Class<DiscoveryViewModel> {
        return DiscoveryViewModel::class.java
//        TODO("Set Observers")
    }

    override fun initView() {
        super.initView()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_discovery
    }

    override fun getTncFragmentInstance(TncResId: Int): Fragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBottomSheetLayoutRes(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun doNeedReattach(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getVMFactory(): ViewModelProvider.AndroidViewModelFactory {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMenuRes(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}