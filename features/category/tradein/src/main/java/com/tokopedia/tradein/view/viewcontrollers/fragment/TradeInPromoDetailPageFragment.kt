package com.tokopedia.tradein.view.viewcontrollers.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tradein.R
import com.tokopedia.tradein.di.DaggerTradeInComponent
import com.tokopedia.tradein.di.TradeInComponent
import com.tokopedia.tradein.model.PromoTradeInModel
import com.tokopedia.tradein.viewmodel.TradeInPromoDetailPageVM
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class TradeInPromoDetailPageFragment : BaseViewModelFragment<TradeInPromoDetailPageVM>() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var viewModel: TradeInPromoDetailPageVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tradein_promo_detail_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(EXTRA_CODE)?.let {
            viewModel.getPromo(it)
        }
        addObservers()
        view.apply {

        }
    }

    private fun addObservers() {
        viewModel.promoTradeInLiveData.observe(viewLifecycleOwner, Observer {
            setUpView(it.tradeInPromoDetail)
        })
        viewModel.getProgBarVisibility().observe(viewLifecycleOwner, Observer {
            if (it)
                view?.findViewById<RelativeLayout>(R.id.progress_bar_layout)?.show()
            else
                view?.findViewById<RelativeLayout>(R.id.progress_bar_layout)?.hide()
        })
    }

    private fun setUpView(tradeInPromoDetail: PromoTradeInModel.TradeInPromoDetail) {
        tradeInPromoDetail?.let {
            view?.apply {
                findViewById<ImageUnify>(R.id.image_promo).setImageUrl(it.imageURL)
                findViewById<Typography>(R.id.promo_title).text = it.title
                findViewById<Typography>(R.id.period_promo_time).text = it.periodFmt
                findViewById<Typography>(R.id.discount_text).text = it.benefitFmt
                findViewById<Typography>(R.id.minimum_transaction_text).text = it.conditionFmt
                findViewById<Typography>(R.id.code_promo).text = it.code
                findViewById<Typography>(R.id.terms_text).text = it.termsConditions
            }
        }
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

    override fun getViewModelType(): Class<TradeInPromoDetailPageVM> {
        return TradeInPromoDetailPageVM::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        this.viewModel = viewModel as TradeInPromoDetailPageVM
    }

    companion object {
        private const val EXTRA_CODE = "EXTRA_CODE"

        fun getFragmentInstance(code: String): Fragment {
            return TradeInPromoDetailPageFragment().also {
                it.arguments = Bundle().apply {
                    putString(EXTRA_CODE, code)
                }
            }
        }
    }
}