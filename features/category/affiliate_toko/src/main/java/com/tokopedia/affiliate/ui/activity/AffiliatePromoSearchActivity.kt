package com.tokopedia.affiliate.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.fragment.AffiliatePromoSearchFragment
import com.tokopedia.affiliate.viewmodel.AffiliatePromoViewModel
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import javax.inject.Inject

class AffiliatePromoSearchActivity : BaseViewModelActivity<AffiliatePromoViewModel>() {
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var affiliateVM: AffiliatePromoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<AffiliatePromoViewModel> {
        return AffiliatePromoViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateVM = viewModel as AffiliatePromoViewModel
    }

    override fun getNewFragment(): Fragment {
        return AffiliatePromoSearchFragment.newInstance()
    }

    override fun initInject() {
        getComponent().injectPromoSearchActivity(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
}
