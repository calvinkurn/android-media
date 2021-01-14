package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.PayLaterComponent
import com.tokopedia.paylater.domain.model.PayLaterProductData
import com.tokopedia.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.paylater.presentation.viewModel.CreditCardViewModel
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
        getComponent(PayLaterComponent::class.java).inject(this)
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
    }

    private fun observeViewModel() {
        /*payLaterViewModel.payLaterActivityResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onPayLaterDataLoaded(it.data)
                is Fail -> onPayLaterDataLoadingFail(it.throwable)
            }
        })

        payLaterViewModel.payLaterApplicationStatusResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onPayLaterApplicationStatusLoaded(it.data)
                is Fail -> onPayLaterApplicationLoadingFail(it.throwable)
            }
        })*/
    }

    override fun getScreenName(): String {
        return "Detail Penawaran"
    }


    private fun onPayLaterDataLoaded(data: PayLaterProductData) {
        creditCardTnCCallback?.getApplicationStatusInfo()
    }

    private fun onPayLaterDataLoadingFail(throwable: Throwable) {
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

    // set payLater + application status data in pager adapter
    private fun onPayLaterApplicationStatusLoaded(data: UserCreditApplicationStatus) {

    }

    private fun onPayLaterApplicationLoadingFail(throwable: Throwable) {
        // set payLater data in view pager

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