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
import com.tokopedia.affiliate.interfaces.PortfolioUrlTextUpdateInterface
import com.tokopedia.affiliate.viewmodel.AffiliatePortfolioViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import kotlinx.android.synthetic.main.affiliate_portfolio_fragment_layout.*
import javax.inject.Inject

class AffiliatePortfolioFragment: BaseViewModelFragment<AffiliatePortfolioViewModel>(),PortfolioUrlTextUpdateInterface{
    private lateinit var affiliatePortfolioViewModel: AffiliatePortfolioViewModel
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(onFoucusChangeInterface=this))
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
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
        afterViewCreated()
    }

    private fun afterViewCreated() {
        initClickListener()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        social_link_rv.layoutManager=layoutManager
        social_link_rv.adapter=adapter
        affiliatePortfolioViewModel.createDefaultListForSm()
    }

    private fun initClickListener() {
        portfolio_cnf_btn.setOnClickListener {
            affiliatePortfolioViewModel.checkDataAndMakeApiCall()
        }
    }

    private fun initObserver() {
        affiliatePortfolioViewModel.getPortfolioUrlList().observe(this,{data ->
            setDataToRV(data)
        })
        affiliatePortfolioViewModel.getUpdateItemIndex().observe(this,{
            index->
            adapter.notifyItemChanged(index)
        })
    }

    private fun setDataToRV(data: ArrayList<Visitable<AffiliateAdapterTypeFactory>>?) {
        adapter.addMoreData(data)
    }

    override fun initInject() {
        getComponent().injectPortfolioFragment(this)
    }
    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    companion object {
        fun getFragmentInstance(): Fragment {
            return AffiliatePortfolioFragment()
        }
    }

    override fun onUrlUpdate(position: Int, text: String) {
        portfolio_cnf_btn.isEnabled=true
        affiliatePortfolioViewModel.updateList(position,text)
    }

    override fun onError(position: Int) {
        affiliatePortfolioViewModel.updateListErrorState(position)
    }

    override fun onUrlSuccess(position: Int) {
        affiliatePortfolioViewModel.updateListSuccess(position)
    }
}