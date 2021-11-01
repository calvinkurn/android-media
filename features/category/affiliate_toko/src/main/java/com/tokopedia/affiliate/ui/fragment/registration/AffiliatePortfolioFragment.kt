package com.tokopedia.affiliate.ui.fragment.registration


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.viewmodel.AffiliatePortfolioViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import javax.inject.Inject

class AffiliatePortfolioFragment: BaseViewModelFragment<AffiliatePortfolioViewModel>() {
    private lateinit var affiliatePortfolioViewModel: AffiliatePortfolioViewModel
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    override fun getViewModelType(): Class<AffiliatePortfolioViewModel> {
        return AffiliatePortfolioViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliatePortfolioViewModel=viewModel as AffiliatePortfolioViewModel
    }
    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.affiliate_portfolio_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun initInject() {
        getComponent().injectPortfolioFragment(this)
    }
    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
}