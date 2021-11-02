package com.tokopedia.affiliate.ui.fragment.registration


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.viewmodel.AffiliateTermsAndConditionViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import kotlinx.android.synthetic.main.affiliate_terms_and_condition_fragment_layout.*
import javax.inject.Inject

class AffiliateTermsAndConditionFragment: BaseViewModelFragment<AffiliateTermsAndConditionViewModel>(){
    private lateinit var affiliateTermsAndConditionViewModel: AffiliateTermsAndConditionViewModel
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory())
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    override fun getViewModelType(): Class<AffiliateTermsAndConditionViewModel> {
        return AffiliateTermsAndConditionViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        affiliateTermsAndConditionViewModel=viewModel as AffiliateTermsAndConditionViewModel
    }
    override fun getVMFactory(): ViewModelProvider.Factory {
        return viewModelProvider
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.affiliate_terms_and_condition_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        afterViewCreated()
    }

    private fun afterViewCreated() {
        initClickListener()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        terms_condition_rv.layoutManager=layoutManager
        terms_condition_rv.adapter=adapter
        affiliateTermsAndConditionViewModel.createListForTermsAndCondition()
    }

    private fun initClickListener() {
        checkbox_terms.setOnCheckedChangeListener { _, isChecked ->
                terms_accept_btn.isEnabled=isChecked
        }
        terms_accept_btn.setOnClickListener {

        }
    }

    private fun initObserver() {
        affiliateTermsAndConditionViewModel.getTermsAndConditionList().observe(this,{data->
            setDataToRV(data)
        })
    }

    private fun setDataToRV(data: ArrayList<Visitable<AffiliateAdapterTypeFactory>>?) {
        adapter.addMoreData(data)
    }

    override fun initInject() {
        getComponent().injectTermAndConditionFragment(this)
    }
    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    companion object {
        fun getFragmentInstance(): Fragment {
            return AffiliateTermsAndConditionFragment()
        }
    }

}