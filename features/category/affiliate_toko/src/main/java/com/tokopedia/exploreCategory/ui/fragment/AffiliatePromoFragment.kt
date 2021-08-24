package com.tokopedia.exploreCategory.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.exploreCategory.di.AffiliateComponent
import com.tokopedia.exploreCategory.di.DaggerAffiliateComponent
import com.tokopedia.exploreCategory.viewmodel.AffiliatePromoViewModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.affiliate_home_fragment_layout.global_error
import kotlinx.android.synthetic.main.affiliate_promo_fragment_layout.*
import javax.inject.Inject

class AffiliatePromoFragment : BaseViewModelFragment<AffiliatePromoViewModel>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var affiliatePromoViewModel: AffiliatePromoViewModel

    companion object {
        fun getFragmentInstance(): Fragment {
            return AffiliatePromoFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        product_link_et.run {
            textInputLayout.isHelperTextEnabled = false
        }
        global_error.run {
            show()
            errorTitle.text = getString(R.string.affiliate_choose_product)
            errorDescription.text = getString(R.string.affiliate_choose_product_description)
            setButtonFull(true)
            errorAction.text = getString(R.string.affiliate_promote_affiliatw)
            errorSecondaryAction.gone()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.affiliate_promo_fragment_layout, container, false)
    }

    private fun setObservers() {
    }

    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectPromoFragment(this)
    }

    private fun getComponent(): AffiliateComponent =
            DaggerAffiliateComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    override fun getViewModelType(): Class<AffiliatePromoViewModel> {
        return AffiliatePromoViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliatePromoViewModel = viewModel as AffiliatePromoViewModel
    }
}