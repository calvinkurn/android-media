package com.tokopedia.pdpsimulation.paylater.presentation.simulation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.helper.PdpSimulationException
import com.tokopedia.pdpsimulation.common.helper.onLinkClickedEvent
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterSimulationGatewayItem
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_paylater_simulation.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.max

class PayLaterSimulationFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireParentFragment(), viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    private val simulationViewFactory: SimulationTableViewFactory by lazy {
        SimulationTableViewFactory()
    }

    private var pdpSimulationCallback: PdpSimulationCallback? = null
    private var isSimulationSuccessFull = false

    override fun initInjector() {
        getComponent(PdpSimulationComponent::class.java).inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_paylater_simulation, container, false)
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
        btnDaftarPayLater.setOnClickListener {
            pdpSimulationCallback?.onRegisterWidgetClicked()
        }
        paylaterDaftarWidget.setOnClickListener {
            pdpSimulationCallback?.onRegisterWidgetClicked()
        }
    }

    private fun observeViewModel() {
        payLaterViewModel.payLaterSimulationResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSimulationDataLoaded(it.data)
                is Fail -> onSimulationLoadingFail(it.throwable)
            }
        })
        payLaterViewModel.payLaterApplicationStatusResultLiveData.observe(
                viewLifecycleOwner, {
            handleRegisterWidgetVisibility()
        }
        )
    }

    private fun onSimulationDataLoaded(data: ArrayList<PayLaterSimulationGatewayItem>) {
        isSimulationSuccessFull = true
        payLaterViewModel.getPayLaterProductData()
        shimmerGroup.gone()
        showSimulationData(data)
    }

    private fun showSimulationData(data: ArrayList<PayLaterSimulationGatewayItem>) {
        simulationDataGroup.visible()
        tickerSimulation.visible()
        tickerSimulation.setTextDescription(context?.getString(R.string.pay_later_simulation_ticker_text)
                ?: "")
        clearAllViews()
        populateSimulationTable(data)
    }

    private fun onSimulationLoadingFail(throwable: Throwable) {
        isSimulationSuccessFull = false
        payLaterViewModel.getPayLaterProductData()
        shimmerGroup.gone()
        showErrorLayout(throwable)
    }

    private fun showErrorLayout(throwable: Throwable) {
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> {
                pdpSimulationCallback?.showNoNetworkView()
                shimmerGroup.visible()
                supervisorWidget.gone()
                return
            }
            is IllegalStateException -> setGlobalErrors(GlobalError.PAGE_FULL)
            is PdpSimulationException.PayLaterNotApplicableException -> {
                payLaterTermsEmptyView.visible()
                context?.let {
                    ContextCompat.getDrawable(it, R.drawable.ic_paylater_terms_not_matched)?.let { drawable ->
                        payLaterTermsEmptyView.setImageDrawable(drawable)
                    }
                }
                tickerSimulation.visible()
                tickerSimulation.setHtmlDescription(context?.getString(R.string.pay_later_not_applicable_ticker_text)
                        ?: "")
                tickerSimulation.onLinkClickedEvent { pdpSimulationCallback?.switchPaymentMode() }
                return
            }
            else -> setGlobalErrors(GlobalError.SERVER_ERROR)
        }
    }

    private fun setGlobalErrors(errorType: Int) {
        simulationGlobalError.setType(errorType)
        simulationGlobalError.show()
        simulationGlobalError.setActionClickListener {
            simulationGlobalError.gone()
            shimmerGroup.visible()
            supervisorWidget.gone()
            pdpSimulationCallback?.getSimulationProductInfo()
        }
    }

    private fun handleRegisterWidgetVisibility() {
        registerShimmer.gone()
        showRegisterWidgetComponents()
        if (payLaterViewModel.isPayLaterProductActive) {
            btnDaftarPayLater.gone()
            paylaterDaftarWidget.visible()
        } else {
            btnDaftarPayLater.visible()
            paylaterDaftarWidget.gone()
            pdpSimulationCallback?.showRegisterWidget()
        }
    }

    private fun showRegisterWidgetComponents() {
        supervisorWidget.visible()
        if(!isSimulationSuccessFull) dividerVertical.visible()
    }

    /**
     * max(0, rowIdx - 1) -> prevent index out of bound when rowIdx = 0
     */
    private fun populateSimulationTable(simulationDataList: ArrayList<PayLaterSimulationGatewayItem>) {
        context?.let {
            val tenureList = arrayOf(1, 3, 6, 9, 12)
            val rowCount = simulationDataList.size + 1
            val contentLayoutParam = TableRow.LayoutParams(it.dpToPx(CONTENT_WIDTH).toInt(), it.dpToPx(TABLE_ITEM_HEIGHT).toInt())
            val tableLayoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT)
            for (rowIdx in 0 until rowCount) {
                populateRowHeaders(it, simulationDataList[max(0, rowIdx - 1)], rowIdx)
                tlInstallmentTable.addView(simulationViewFactory.getTableContentView(it, simulationDataList, tenureList, rowIdx, contentLayoutParam), tableLayoutParams)
            }
        }
    }

    private fun populateRowHeaders(
            context: Context,
            simulationDataItem: PayLaterSimulationGatewayItem,
            position: Int,
    ) {
        val layoutParam = ViewGroup.LayoutParams(context.dpToPx(ROW_HEADER_WIDTH).toInt(), context.dpToPx(TABLE_ITEM_HEIGHT).toInt())
        llPayLaterPartner.apply {
            when (position) {
                0 -> addView(simulationViewFactory.getBlankView(context, layoutParam))
                else -> addView(simulationViewFactory.getPayLaterOption(context, layoutParam, simulationDataItem, position))
            }
        }
    }

    private fun clearAllViews() {
        tlInstallmentTable.removeAllViews()
        llPayLaterPartner.removeAllViews()
    }

    companion object {
        const val ROW_HEADER_WIDTH = 84
        const val TABLE_ITEM_HEIGHT = 52
        const val CONTENT_WIDTH = 110

        @JvmStatic
        fun newInstance(pdpSimulationCallback: PdpSimulationCallback): PayLaterSimulationFragment {
            val simulationFragment = PayLaterSimulationFragment()
            simulationFragment.pdpSimulationCallback = pdpSimulationCallback
            return simulationFragment
        }
    }
}