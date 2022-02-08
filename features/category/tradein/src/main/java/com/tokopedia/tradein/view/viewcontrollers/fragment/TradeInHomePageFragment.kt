package com.tokopedia.tradein.view.viewcontrollers.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.tradein.R
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.di.TradeInComponent
import com.tokopedia.tradein.view.viewcontrollers.activity.TradeInPromoActivity
import com.tokopedia.tradein.viewmodel.TradeInHomePageFragmentVM
import javax.inject.Inject

class TradeInHomePageFragment : BaseViewModelFragment<TradeInHomePageFragmentVM>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var viewModel: TradeInHomePageFragmentVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tradein_initial_price_parent_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        view.apply {
            findViewById<View>(R.id.tradein_promo_view).setOnClickListener {
                startActivity(TradeInPromoActivity.getIntent(context, "LAKU6EMANGASIX"))
            }
        }
    }

    private fun addObservers() {

    }


    override fun initInject() {
        getComponent().inject(this)
    }

    private fun getComponent(): TradeInComponent =
        DaggerTradeInComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun getViewModelType(): Class<TradeInHomePageFragmentVM> {
        return TradeInHomePageFragmentVM::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        this.viewModel = viewModel as TradeInHomePageFragmentVM
    }

    companion object {
        fun getFragmentInstance(): Fragment {
            return TradeInHomePageFragment()
        }
    }
}