package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.PdpSimulationComponent
import com.tokopedia.paylater.domain.model.CreditCardPdpMetaData
import com.tokopedia.paylater.presentation.adapter.CreditCardTncAdapter
import com.tokopedia.paylater.presentation.viewModel.CreditCardViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_credit_card_tnc.*
import javax.inject.Inject

class CreditCardTncFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val creditCardViewModel: CreditCardViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireParentFragment(), viewModelFactory.get())
        viewModelProvider.get(CreditCardViewModel::class.java)
    }

    private var creditCardTnCCallback: CreditCardTnCCallback? = null

    override fun initInjector() {
        getComponent(PdpSimulationComponent::class.java).inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_credit_card_tnc, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
        creditCardViewModel.getCreditCardTncData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvPdpInfo.adapter = CreditCardTncAdapter()
        rvPdpInfo.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        btnMoreInfo.setOnClickListener {

        }
    }

    private fun observeViewModel() {
        creditCardViewModel.creditCardPdpMetaInfoLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onPdpInfoMetaDataLoaded(it.data)
                is Fail -> onPdpInfoMetaDataLoadingFailed(it.throwable)
            }
        })
    }

    override fun getScreenName(): String {
        return "Detail Penawaran"
    }


    private fun onPdpInfoMetaDataLoaded(data: CreditCardPdpMetaData) {
        // pdpInfoContentList is non nullable here in case of Success
        (rvPdpInfo.adapter as CreditCardTncAdapter).setData(data.pdpInfoContentList!!)
    }

    private fun onPdpInfoMetaDataLoadingFailed(throwable: Throwable) {
        /*  payLaterOffersShimmerGroup.gone()
          when (throwable) {
              is UnknownHostException, is SocketTimeoutException -> {
                  creditCardTnCCallback?.noInternetCallback()
                  return
              }
              is IllegalStateException -> {
                  payLaterOffersGlobalError.setType(GlobalError.PAGE_FULL)
              }
              else -> {
                  payLaterOffersGlobalError.setType(GlobalError.SERVER_ERROR)
              }
          }
          payLaterOffersGlobalError.show()
          payLaterOffersGlobalError.setActionClickListener {
              payLaterOffersGlobalError.hide()
              // notify payLater fragment to invoke again
              payLaterOffersShimmerGroup.visible()
              creditCardTnCCallback?.getPayLaterProductInfo()
          }*/
    }

    fun setCreditCardTncCallback(creditCardTnCCallback: CreditCardTnCCallback) {
        this.creditCardTnCCallback = creditCardTnCCallback
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                CreditCardTncFragment()
    }

    interface CreditCardTnCCallback {
        fun getPayLaterProductInfo()
        fun getApplicationStatusInfo()
        fun noInternetCallback()
    }
}