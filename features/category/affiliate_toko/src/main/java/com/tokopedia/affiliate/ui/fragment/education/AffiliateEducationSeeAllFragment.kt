package com.tokopedia.affiliate.ui.fragment.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.PAGE_EDUCATION_EVENT
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.ui.activity.AffiliateEducationSeeAllActivity
import com.tokopedia.affiliate.viewmodel.AffiliateEducationSeeAllViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.basemvvm.viewmodel.ViewModelProviderFactory
import com.tokopedia.searchbar.navigation_component.NavToolbar
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class AffiliateEducationSeeAllFragment :
    BaseViewModelFragment<AffiliateEducationSeeAllViewModel>() {

    private var educationVM: AffiliateEducationSeeAllViewModel? = null
    private var pageType: String? = null

    @JvmField
    @Inject
    var viewModelProviderFactory: ViewModelProviderFactory? = null

    companion object {
        private const val PARAM_PAGE_TYPE = "param_page_type"
        fun newInstance(pageType: String?): Fragment {
            return AffiliateEducationSeeAllFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_PAGE_TYPE, pageType)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.affiliate_education_see_all_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageType = arguments?.getString(AffiliateEducationSeeAllActivity.PARAM_PAGE_TYPE)
        view.findViewById<NavToolbar>(R.id.education_see_all_navToolbar)?.run {
            viewLifecycleOwner.lifecycle.addObserver(this)
            getCustomViewContentView()?.findViewById<Typography>(R.id.navbar_tittle)?.text =
                if (pageType == PAGE_EDUCATION_EVENT) getString(R.string.affiliate_event)
                else getString(R.string.affiliate_artikel)
            setOnBackButtonClickListener {
                activity?.finish()
            }
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProviderFactory
    }

    override fun getViewModelType(): Class<AffiliateEducationSeeAllViewModel> {
        return AffiliateEducationSeeAllViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        educationVM = viewModel as AffiliateEducationSeeAllViewModel
    }

    override fun initInject() {
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()
            .injectEducationSeeMoreFragment(this)
    }
}
