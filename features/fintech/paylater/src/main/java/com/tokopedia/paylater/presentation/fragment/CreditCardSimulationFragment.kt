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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.PdpSimulationComponent
import com.tokopedia.paylater.domain.model.CreditCardSimulationResult
import com.tokopedia.paylater.domain.model.SimulationBank
import com.tokopedia.paylater.presentation.adapter.CreditCardAvailableBanksAdapter
import com.tokopedia.paylater.presentation.adapter.CreditCardSimulationAdapter
import com.tokopedia.paylater.presentation.viewModel.CreditCardViewModel
import com.tokopedia.paylater.presentation.widget.bottomsheet.CreditCardAvailableBanksBottomSheet
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_credit_card_simulation.*
import kotlinx.android.synthetic.main.paylater_daftar_widget.view.*
import javax.inject.Inject

class CreditCardSimulationFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val creditCardViewModel: CreditCardViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireParentFragment(), viewModelFactory.get())
        viewModelProvider.get(CreditCardViewModel::class.java)
    }

    private var bankList = ArrayList<SimulationBank>()
    private var smallBankList = ArrayList<SimulationBank>()
    private var creditCardSimulationCallback: CreditCardSimulationCallback? = null

    override fun initInjector() {
        getComponent(PdpSimulationComponent::class.java).inject(this)
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
        initAdapter()
    }

    private fun initAdapter() {
        rvAvailableBanks.adapter = CreditCardAvailableBanksAdapter()
        rvAvailableBanks.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //(rvAvailableBanks.adapter as CreditCardAvailableBanksAdapter).setBankList(bankList)
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
        creditCardRegisterWidget.setOnClickListener {
            fetchBankCardList()
            creditCardSimulationCallback?.onRegisterCreditCardClicked()
        }
    }

    private fun fetchBankCardList() {
        if (creditCardViewModel.creditCardBankResultLiveData.value !is Success<*>)
            creditCardViewModel.getBankCardList()
    }

    private fun observeViewModel() {
        creditCardViewModel.creditCardSimulationResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSimulationDataLoaded(it.data)
                is Fail -> onSimulationLoadingFail(it.throwable)
            }
        })
    }

    private fun onSimulationDataLoaded(data: CreditCardSimulationResult) {
        /*shimmerGroup.gone()
        simulationDataGroup.visible()*/
        populateFields(data)
        data.creditCardInstallmentList?.let {
            if (it.isNotEmpty()) {
                rvCreditCardSimulation.adapter = CreditCardSimulationAdapter(it) { bankList ->
                    setBankData(bankList)
                }
                rvCreditCardSimulation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                if (!it.getOrNull(0)?.simulationBankList.isNullOrEmpty()) {
                    rvCreditCardSimulation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    setBankData(it[0].simulationBankList!!)
                }
            }
        }
    }

    private fun populateFields(data: CreditCardSimulationResult) {
        if (!data.tickerInformation.isNullOrEmpty())
            tickerSimulation.setHtmlDescription(data.tickerInformation)
        else tickerSimulation.gone()
        if (!data.ctaDescriptionText.isNullOrEmpty() && !data.ctaMainLabelText.isNullOrEmpty()) {
            creditCardRegisterWidget.tvTitle.text = data.ctaMainLabelText
            creditCardRegisterWidget.tvDescription.text = data.ctaDescriptionText
        }
    }

    private fun setBankData(bankList: ArrayList<SimulationBank>?) {
        bankList?.let {
            this.bankList = it
            val smallBankListSize = it.size.coerceAtMost(4)
            smallBankList.clear()
            smallBankList.addAll(it.slice(0 until smallBankListSize))
            tvValidCreditCardBanks.text = "Berlaku kartu kredit dari: ${it.size} bank"
            tvSeeAll.text = "Lihat Semua (${it.size - smallBankListSize})"
            if (it.size > MAX_BANK_LIST_VIEW_SIZE) tvSeeAll.visible()
            (rvAvailableBanks.adapter as CreditCardAvailableBanksAdapter).setBankList(smallBankList)
        }

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
        const val MAX_BANK_LIST_VIEW_SIZE = 4

        @JvmStatic
        fun newInstance() = CreditCardSimulationFragment()

    }

    interface CreditCardSimulationCallback {
        fun onRegisterCreditCardClicked()
        fun noInternetCallback()
        fun getSimulationProductInfo()
    }
}