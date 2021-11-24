package com.tokopedia.affiliate.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.fragment.AffiliatePromotionHistoryFragment
import com.tokopedia.affiliate.viewmodel.AffiliateViewModel
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import javax.inject.Inject

class AffiliateComponentActivity : BaseViewModelActivity<AffiliateViewModel>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var affiliateVM: AffiliateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }
    private var isUserBlackListed = false
    private fun getBundle() {
        intent?.let {
            isUserBlackListed = it.getBooleanExtra("isUserBlackListed",false)
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }
    override fun getViewModelType(): Class<AffiliateViewModel> {
        return AffiliateViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateVM = viewModel as AffiliateViewModel
    }

    override fun getNewFragment(): Fragment? {
        getBundle()
        return AffiliatePromotionHistoryFragment.getFragmentInstance(isUserBlackListed)
    }
    override fun initInject() {
        getComponent().injectComponentActivity(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
}