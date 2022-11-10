package com.tokopedia.affiliate.ui.fragment.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.viewmodel.AffiliateEducationLandingViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import javax.inject.Inject

class AffiliateEducationLandingPage : BaseViewModelFragment<AffiliateEducationLandingViewModel>() {

    private var eduViewModel: AffiliateEducationLandingViewModel? = null

    @JvmField
    @Inject
    var viewModelFactory: ViewModelProvider.Factory? = null

    override fun getViewModelType(): Class<AffiliateEducationLandingViewModel> {
        return AffiliateEducationLandingViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        eduViewModel = viewModel as AffiliateEducationLandingViewModel
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelFactory
    }
    override fun initInject() {
        getComponent().injectEducationLandingPage(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    companion object {
        fun getFragmentInstance() = AffiliateEducationLandingPage()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.affiliate_education_landing_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eduViewModel?.getEducationPageData()?.observe(viewLifecycleOwner) {
            val adapter = AffiliateAdapter(AffiliateAdapterFactory())
            adapter.setVisitables(it)
            view.findViewById<RecyclerView>(R.id.rv_education_page).adapter = adapter
        }
    }


}
