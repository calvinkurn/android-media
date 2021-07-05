package com.tokopedia.pdpsimulation.creditcard.presentation.simulation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.helper.PdpSimulationException
import com.tokopedia.pdpsimulation.common.helper.onLinkClickedEvent
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardSimulationResult
import com.tokopedia.pdpsimulation.creditcard.domain.model.SimulationBank
import com.tokopedia.pdpsimulation.creditcard.presentation.registration.bottomsheet.CreditCardRegistrationBottomSheet
import com.tokopedia.pdpsimulation.creditcard.presentation.simulation.adapter.CreditCardAvailableBanksAdapter
import com.tokopedia.pdpsimulation.creditcard.presentation.simulation.adapter.CreditCardSimulationAdapter
import com.tokopedia.pdpsimulation.creditcard.presentation.simulation.bottomsheet.CreditCardAvailableBanksBottomSheet
import com.tokopedia.pdpsimulation.creditcard.viewmodel.CreditCardViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_credit_card_simulation.*
import kotlinx.android.synthetic.main.paylater_daftar_widget.view.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
    private var pdpSimulationCallback: PdpSimulationCallback? = null

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
        rvAvailableBanks.adapter = CreditCardAvailableBanksAdapter(arrayListOf())
        rvAvailableBanks.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initRegisterWidget() {
        creditCardRegisterWidget.apply {
            tvTitle.text = context.getString(R.string.credit_card_widget_title)
            ivPaymentMode.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_credit_card_register))
        }
    }

    override fun getScreenName(): String {
        return "Simulasi"
    }

    private fun initListeners() {
        tvSeeAll.setOnClickListener { showAllBanksBottomSheet() }
        creditCardRegisterWidget.setOnClickListener {
            creditCardViewModel.getBankCardList()
            pdpSimulationCallback?.sendAnalytics(PdpSimulationEvent.CreditCard.ApplyCreditCardEvent("click"))
            pdpSimulationCallback?.openBottomSheet(Bundle(), CreditCardRegistrationBottomSheet::class.java)
        }
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
        creditSimulationShimmerGroup.gone()
        simulationDataGroup.visible()
        rvAvailableBanks.visible()
        populateFields(data)
        data.creditCardInstallmentList?.let {
            if (it.isNotEmpty()) {
                rvCreditCardSimulation.adapter = CreditCardSimulationAdapter(it) { bankList ->
                    setBankData(bankList)
                }
                rvCreditCardSimulation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                if (!it.getOrNull(0)?.simulationBankList.isNullOrEmpty()) {
                    rvCreditCardSimulation.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    setBankData(it[0].simulationBankList ?: arrayListOf())
                }
            }
        }
    }

    private fun populateFields(data: CreditCardSimulationResult) {
        if (!data.tickerInformation.isNullOrEmpty()) {
            tickerSimulation.visible()
            tickerSimulation.setHtmlDescription(data.tickerInformation)
        } else tickerSimulation.gone()
        if (!data.ctaDescriptionText.isNullOrEmpty() && !data.ctaMainLabelText.isNullOrEmpty()) {
            creditCardRegisterWidget.visible()
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
            tvValidCreditCardBanks.text = context?.getString(R.string.credit_card_simulation_available_banks, it.size)?.parseAsHtml()
            if (it.size > MAX_BANK_LIST_VIEW_SIZE) {
                tvSeeAll.visible()
                tvSeeAll.text = context?.getString(R.string.credit_card_simulation_see_all_banks, it.size - smallBankListSize)
            }
            (rvAvailableBanks.adapter as CreditCardAvailableBanksAdapter).run {
                this.bankList = smallBankList
                notifyDataSetChanged()
            }
        }

    }

    private fun onSimulationLoadingFail(throwable: Throwable) {
        creditSimulationShimmerGroup.gone()
        rvAvailableBanks.gone()
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> {
                pdpSimulationCallback?.showNoNetworkView()
                creditSimulationShimmerGroup.visible()
                return
            }
            is IllegalStateException -> {
                setGlobalErrors(GlobalError.PAGE_FULL)
            }
            is PdpSimulationException.CreditCardSimulationNotAvailableException -> {
                pdpSimulationCallback?.sendAnalytics(PdpSimulationEvent.CreditCard.CCNotAvailableEvent("cc not available"))
                creditCardTermsEmptyView.visible()
                context?.let {
                    ContextCompat.getDrawable(it, R.drawable.ic_paylater_terms_not_matched)?.let { drawable -> creditCardTermsEmptyView.setImageDrawable(drawable) }
                }
                dividerVertical.visible()
                tickerSimulation.visible()
                creditCardRegisterWidget.visible()
                tickerSimulation.setHtmlDescription(context?.getString(R.string.credit_card_not_applicable_ticker_text)
                        ?: "")
                tickerSimulation.onLinkClickedEvent { pdpSimulationCallback?.switchPaymentMode() }
                return
            }
            else -> {
                setGlobalErrors(GlobalError.SERVER_ERROR)
            }
        }
    }

    private fun setGlobalErrors(errorType: Int) {
        simulationGlobalError.setType(errorType)
        simulationGlobalError.show()
        simulationGlobalError.setActionClickListener {
            simulationGlobalError.gone()
            creditSimulationShimmerGroup.visible()
            rvAvailableBanks.visible()
            pdpSimulationCallback?.getSimulationProductInfo()
        }
    }

    private fun showAllBanksBottomSheet() {
        if (bankList.isNotEmpty()) {
            val bundle = Bundle()
            bundle.putParcelableArrayList(CreditCardAvailableBanksBottomSheet.CREDIT_CARD_BANK_DATA, bankList)
            pdpSimulationCallback?.openBottomSheet(bundle, CreditCardAvailableBanksBottomSheet::class.java)
        }
    }

    companion object {
        const val MAX_BANK_LIST_VIEW_SIZE = 4

        @JvmStatic
        fun newInstance(pdpSimulationCallback: PdpSimulationCallback): CreditCardSimulationFragment {
            val simulationFragment = CreditCardSimulationFragment()
            simulationFragment.pdpSimulationCallback = pdpSimulationCallback
            return simulationFragment
        }
    }
}