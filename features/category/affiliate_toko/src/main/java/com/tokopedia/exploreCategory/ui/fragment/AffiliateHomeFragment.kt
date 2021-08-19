package com.tokopedia.exploreCategory.ui.fragment

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.exploreCategory.viewmodel.AffiliateHomeViewModel
import javax.inject.Inject

class AffiliateHomeFragment : BaseViewModelFragment<AffiliateHomeViewModel>() {
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private lateinit var affiliateHomeViewModel: AffiliateHomeViewModel

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<AffiliateHomeViewModel> {
        return AffiliateHomeViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateHomeViewModel = viewModel as AffiliateHomeViewModel
    }
}