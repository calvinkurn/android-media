package com.tokopedia.paylater.presentation.fragment

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
import com.tokopedia.paylater.R
import com.tokopedia.paylater.data.mapper.PayLater
import com.tokopedia.paylater.data.mapper.PayLaterSimulationTenureType
import com.tokopedia.paylater.data.mapper.PaymentMode
import com.tokopedia.paylater.di.component.PdpSimulationComponent
import com.tokopedia.paylater.domain.model.PayLaterSimulationGatewayItem
import com.tokopedia.paylater.domain.model.SimulationItemDetail
import com.tokopedia.paylater.helper.PdpSimulationException
import com.tokopedia.paylater.presentation.viewModel.PayLaterViewModel
import com.tokopedia.paylater.presentation.widget.*
import com.tokopedia.unifycomponents.ticker.TickerCallback
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
        SimulationTableViewFactory(context)
    }

    private var payLaterSimulationCallback: PayLaterSimulationCallback? = null

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
            payLaterSimulationCallback?.onRegisterPayLaterClicked()
        }
        paylaterDaftarWidget.setOnClickListener {
            payLaterSimulationCallback?.onRegisterPayLaterClicked()
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
                viewLifecycleOwner,
                {
                    when (it) {
                        is Success -> handleRegisterWidgetVisibility()
                        is Fail -> handleRegisterWidgetVisibility()
                    }
                }
        )
    }

    private fun onSimulationDataLoaded(data: ArrayList<PayLaterSimulationGatewayItem>) {
        payLaterSimulationCallback?.getPayLaterProductInfo()
        shimmerGroup.gone()
        simulationDataGroup.visible()
        tickerSimulation.visible()
        clearAllViews()
        populateSimulationTable(data)
        tickerSimulation.setTextDescription(context?.getString(R.string.pay_later_simulation_ticker_text)
                ?: "")
    }

    private fun onSimulationLoadingFail(throwable: Throwable) {
        payLaterSimulationCallback?.getPayLaterProductInfo()
        shimmerGroup.gone()
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> {
                payLaterSimulationCallback?.noInternetCallback()
                shimmerGroup.visible()
                supervisorWidget.gone()
                return
            }
            is IllegalStateException -> setGlobalErrors(GlobalError.PAGE_FULL)
            is PdpSimulationException.PayLaterNotApplicableException -> {
                payLaterTermsEmptyView.visible()
                tickerSimulation.visible()
                tickerSimulation.setHtmlDescription(context?.getString(R.string.pay_later_not_applicable_ticker_text)
                        ?: "")
                tickerSimulation.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        payLaterSimulationCallback?.switchPaymentMode()
                    }

                    override fun onDismiss() {}
                })
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
            payLaterSimulationCallback?.getSimulationProductInfo(PayLater)
        }
    }

    private fun handleRegisterWidgetVisibility() {
        supervisorWidget.visible()
        registerShimmer.gone()
        dividerVertical.visible()
        if (payLaterViewModel.isPayLaterProductActive) {
            btnDaftarPayLater.gone()
            paylaterDaftarWidget.visible()
        } else {
            btnDaftarPayLater.visible()
            paylaterDaftarWidget.gone()
            payLaterSimulationCallback?.showRegisterWidget()
        }
    }

    /**
     * max(0, rowIdx - 1) -> prevent index out of bound when rowIdx = 0
     */
    private fun populateSimulationTable(simulationDataList: ArrayList<PayLaterSimulationGatewayItem>) {
        context?.let {
            val tenureList = arrayOf(1, 3, 6, 9, 12)
            val rowCount = simulationDataList.size + 1
            val colCount = 5
            val contentLayoutParam = TableRow.LayoutParams(it.dpToPx(CONTENT_WIDTH).toInt(), it.dpToPx(TABLE_ITEM_HEIGHT).toInt())
            val tableLayoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT)
            for (rowIdx in 0 until rowCount) {
                val contentRow = TableRow(it)
                populateRowHeaders(it, simulationDataList[max(0, rowIdx - 1)], rowIdx)
                for (columnIdx in 0 until colCount) {
                    when (rowIdx) {
                        0 -> contentRow.addView(getColumnHeader(contentLayoutParam, columnIdx), contentLayoutParam)
                        else -> {
                            if (simulationDataList[rowIdx - 1].isRecommended)
                                contentRow.background = ContextCompat.getDrawable(it, R.drawable.ic_paylater_installment_green_border)
                            contentRow.addView(getInstallmentView(contentLayoutParam, simulationDataList[rowIdx - 1].installmentMap, tenureList, rowIdx, columnIdx), contentLayoutParam)
                        }
                    }
                }
                tlInstallmentTable.addView(contentRow, tableLayoutParams)
            }
        }
    }

    private fun populateRowHeaders(context: Context, simulationDataItem: PayLaterSimulationGatewayItem, position: Int) {
        val layoutParam = ViewGroup.LayoutParams(context.dpToPx(ROW_HEADER_WIDTH).toInt(), context.dpToPx(TABLE_ITEM_HEIGHT).toInt())
        llPayLaterPartner.apply {
            when (position) {
                0 -> addView(getBlankView(layoutParam))
                else -> {
                    if (simulationDataItem.isRecommended)
                        addView(getRecommendedPayLaterOption(layoutParam, simulationDataItem, position % 2 == 0))
                    else
                        addView(getPayLaterOption(layoutParam, simulationDataItem, position % 2 == 0))
                }
            }
        }
    }

    private fun getBlankView(layoutParam: ViewGroup.LayoutParams): View {
        val blankSimulationTableHeading = simulationViewFactory.create(BlankViewTableRowHeader::class.java, layoutParam)
        return blankSimulationTableHeading.initUI()
    }

    private fun getPayLaterOption(layoutParam: ViewGroup.LayoutParams, simulationDataItem: PayLaterSimulationGatewayItem, showBackGround: Boolean): View? {
        val noRecommendationViewSimulationTable = simulationViewFactory.create(NoRecommendationViewTableRowHeader::class.java, layoutParam)
        return noRecommendationViewSimulationTable.initUI(simulationDataItem, showBackGround)
    }

    private fun getRecommendedPayLaterOption(layoutParam: ViewGroup.LayoutParams, simulationDataItem: PayLaterSimulationGatewayItem, showBackGround: Boolean): View {
        val recommendationView = simulationViewFactory.create(RecommendationViewTableRowHeader::class.java, layoutParam)
        return recommendationView.initUI(simulationDataItem, showBackGround)
    }

    private fun getInstallmentView(contentLayoutParam: ViewGroup.LayoutParams, installmentMap: HashMap<PayLaterSimulationTenureType, SimulationItemDetail>, tenureList: Array<Int>, row: Int, col: Int): View {
        val installmentView = simulationViewFactory.create(InstallmentViewTableContent::class.java, contentLayoutParam)
        return installmentView.initUI(installmentMap, tenureList, row, col)
    }

    private fun getColumnHeader(contentLayoutParam: ViewGroup.LayoutParams, position: Int): View {
        val installmentColumnHeader = simulationViewFactory.create(InstallmentViewTableColumnHeader::class.java, contentLayoutParam)
        return installmentColumnHeader.initUI(position)
    }

    private fun clearAllViews() {
        tlInstallmentTable.removeAllViews()
        llPayLaterPartner.removeAllViews()
    }

    fun setSimulationListener(payLaterSimulationCallback: PayLaterSimulationCallback) {
        this.payLaterSimulationCallback = payLaterSimulationCallback
    }

    companion object {
        const val ROW_HEADER_WIDTH = 84
        const val TABLE_ITEM_HEIGHT = 52
        const val CONTENT_WIDTH = 110

        @JvmStatic
        fun newInstance() = PayLaterSimulationFragment()
    }

    interface PayLaterSimulationCallback {
        fun onRegisterPayLaterClicked()
        fun noInternetCallback()
        fun getPayLaterProductInfo()
        fun switchPaymentMode()
        fun getSimulationProductInfo(paymentMode: PaymentMode)
        fun showRegisterWidget()
    }
}