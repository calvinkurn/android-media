package com.tokopedia.exploreCategory.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelActivity
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.exploreCategory.viewmodel.AffiliateViewModel

class AffiliateActivity : BaseViewModelActivity<AffiliateViewModel>() {

    private lateinit var affiliateVM: AffiliateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutRes(): Int = R.layout.affiliate_layout

    override fun getViewModelType(): Class<AffiliateViewModel> {
        return AffiliateViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateVM = viewModel as AffiliateViewModel
    }

    override fun getNewFragment(): Fragment? {
        return null
    }
}