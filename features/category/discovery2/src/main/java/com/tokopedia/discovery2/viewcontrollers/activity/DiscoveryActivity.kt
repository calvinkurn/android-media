package com.tokopedia.discovery2.viewcontrollers.activity

import androidx.fragment.app.Fragment
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.tradein_common.viewmodel.BaseViewModel

class DiscoveryActivity : BaseDiscoveryActivity<DiscoveryViewModel>() {

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
}