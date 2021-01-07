package com.tokopedia.paylater.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.PayLaterComponent
import com.tokopedia.paylater.domain.model.CreditCardBank
import com.tokopedia.paylater.domain.model.SimulationTableResponse
import com.tokopedia.paylater.presentation.adapter.CreditCardAvailableBanksAdapter
import com.tokopedia.paylater.presentation.adapter.CreditCardSimulationAdapter
import com.tokopedia.paylater.presentation.viewModel.PayLaterViewModel
import com.tokopedia.paylater.presentation.widget.bottomsheet.CreditCardAvailableBanksBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_credit_card_simulation.*
import kotlinx.android.synthetic.main.paylater_daftar_widget.view.*
import javax.inject.Inject

class CreditCardSimulationFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireParentFragment(), viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    private var bankList = ArrayList<CreditCardBank>()
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
        initRegisterWidget()
        initListeners()
    }

    private fun initRegisterWidget() {
        creditCardRegisterWidget.apply {
            tvTitle.text = "Mau buat kartu kredit?"
            ivPaymentMode.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_credit_card_register))
        }
    }


    override fun getScreenName(): String {
        return "Simulasi"
    }

    private fun initListeners() {
        tvSeeAll.setOnClickListener { showAllBanksBottomSheet() }
        creditCardRegisterWidget.setOnClickListener { creditCardSimulationCallback?.onRegisterPayLaterClicked() }
    }

    private fun observeViewModel() {
        payLaterViewModel.creditCardSimulationResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSimulationDataLoaded(it.data)
                is Fail -> onSimulationLoadingFail(it.throwable)
            }
        })
    }

    private fun onSimulationDataLoaded(data: ArrayList<SimulationTableResponse>) {
        /*shimmerGroup.gone()
        simulationDataGroup.visible()*/
        rvCreditCardSimulation.adapter = CreditCardSimulationAdapter(data) { bankList ->
            this.bankList = bankList
            val smallList = ArrayList(data[0].installmentData.slice(0..3))
            (rvAvailableBanks.adapter as CreditCardAvailableBanksAdapter).setBankList(smallList)
        }
        rvCreditCardSimulation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvAvailableBanks.adapter = CreditCardAvailableBanksAdapter()
        rvAvailableBanks.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val smallList = ArrayList(data[0].installmentData.slice(0..3))
        (rvAvailableBanks.adapter as CreditCardAvailableBanksAdapter).setBankList(smallList)
        this.bankList = data[0].installmentData
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

    private fun showAllBanksBottomSheet() {
        if (bankList.isNotEmpty()) {
            val bundle = Bundle().apply {
                putParcelableArrayList(CreditCardAvailableBanksBottomSheet.CREDIT_CARD_BANK_DATA, bankList)
            }
            CreditCardAvailableBanksBottomSheet.show(bundle, childFragmentManager)
        }
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