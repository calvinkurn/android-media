package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.PayLaterComponent
import com.tokopedia.paylater.domain.model.PayLaterSimulationGatewayItem
import com.tokopedia.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.paylater.helper.PayLaterException
import com.tokopedia.paylater.helper.PayLaterHelper
import com.tokopedia.paylater.presentation.viewModel.PayLaterViewModel
import com.tokopedia.paylater.presentation.widget.SimulationTableViewFactory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_credit_card_simulation.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class CreditCardSimulationFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireParentFragment(), viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    private var creditCardSimulationCallback: CreditCardSimulationCallback? = null

    override fun initInjector() {
        getComponent(PayLaterComponent::class.java).inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_credit_card_simulation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    override fun getScreenName(): String {
        return "Simulasi"
    }

    private fun initListeners() {
        creditCardRegisterWidget.setOnClickListener {
            creditCardSimulationCallback?.onRegisterPayLaterClicked()
        }
    }

    private fun observeViewModel() {
        /*payLaterViewModel.payLaterSimulationResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSimulationDataLoaded(it.data)
                is Fail -> onSimulationLoadingFail(it.throwable)
            }
        })
        payLaterViewModel.payLaterApplicationStatusResultLiveData.observe(
                viewLifecycleOwner,
                {
                    when (it) {
                        is Success -> onApplicationStatusLoaded(it.data)
                        is Fail -> onApplicationStatusLoadingFail(it.throwable)
                    }
                },
        )*/
    }

    private fun onSimulationDataLoaded(data: ArrayList<PayLaterSimulationGatewayItem>) {
        /*shimmerGroup.gone()
        simulationDataGroup.visible()*/
    }

    private fun onSimulationLoadingFail(throwable: Throwable) {
        /*shimmerGroup.gone()
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> {
                creditCardSimulationCallback?.noInternetCallback()
                return
            }
            is IllegalStateException -> {
                simulationGlobalError.setType(GlobalError.PAGE_FULL)
            }
            is PayLaterException.PayLaterNotApplicableException -> {
                payLaterTermsEmptyView.visible()
                tickerSimulation.setHtmlDescription(context?.getString(R.string.paylater_not_applicable_ticker_text)
                        ?: "")
                return
            }
            else -> {
                simulationGlobalError.setType(GlobalError.SERVER_ERROR)
            }
        }
        simulationGlobalError.show()
        simulationGlobalError.setActionClickListener {
            simulationGlobalError.gone()
            shimmerGroup.visible()
            creditCardSimulationCallback?.getSimulationProductInfo()
        }
*/
    }

    private fun onApplicationStatusLoadingFail(throwable: Throwable) {
        //paylaterDaftarWidget.gone()
    }

    private fun onApplicationStatusLoaded(data: UserCreditApplicationStatus) {

    }


    fun setCreditCardSimulationCallback(creditCardSimulationCallback: CreditCardSimulationCallback) {
        this.creditCardSimulationCallback = creditCardSimulationCallback
    }

    companion object {
        @JvmStatic
        fun newInstance() = CreditCardSimulationFragment()
    }

    interface CreditCardSimulationCallback {
        fun onRegisterPayLaterClicked()
        fun noInternetCallback()
        fun getSimulationProductInfo()
    }
}