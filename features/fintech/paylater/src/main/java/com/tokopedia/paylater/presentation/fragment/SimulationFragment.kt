package com.tokopedia.paylater.presentation.fragment

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
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.paylater.PRODUCT_PRICE
import com.tokopedia.paylater.R
import com.tokopedia.paylater.di.component.PayLaterComponent
import com.tokopedia.paylater.domain.model.PayLaterSimulationGatewayItem
import com.tokopedia.paylater.domain.model.SimulationItemDetail
import com.tokopedia.paylater.domain.model.UserCreditApplicationStatus
import com.tokopedia.paylater.helper.PayLaterException
import com.tokopedia.paylater.helper.PayLaterHelper
import com.tokopedia.paylater.presentation.viewModel.PayLaterViewModel
import com.tokopedia.paylater.presentation.widget.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_simulation.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class SimulationFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireParentFragment(), viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    private val productPrice: Int by lazy {
        arguments?.getInt(PRODUCT_PRICE) ?: 0
    }

    private val simulationViewFactory: SimulationTableViewFactory by lazy {
        SimulationTableViewFactory(context)
    }

    private var registerPayLaterCallback: RegisterPayLaterCallback? = null

    override fun initInjector() {
        getComponent(PayLaterComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_simulation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        populateRowHeaders()
        fetchSimulationData()
    }

    override fun getScreenName(): String {
        return "Simulasi"
    }

    private fun initListeners() {
        btnDaftarPayLater.setOnClickListener {
            registerPayLaterCallback?.onRegisterPayLaterClicked()
        }
        paylaterDaftarWidget.setOnClickListener {
            registerPayLaterCallback?.onRegisterPayLaterClicked()
        }
    }

    private fun fetchSimulationData() {
        shimmerGroup.visible()
        payLaterViewModel.getPayLaterSimulationData(productPrice)
    }

    private fun observeViewModel() {
        payLaterViewModel.payLaterSimulationResultLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSimulationDataLoaded(it.data)
                is Fail -> onSimulationLoadingFail(it.throwable)
            }
        })
    }

    private fun onSimulationDataLoaded(data: ArrayList<PayLaterSimulationGatewayItem>) {
        shimmerGroup.gone()
        simulationDataGroup.visible()
        populateSimulationTable(data)
    }

    private fun onSimulationLoadingFail(throwable: Throwable) {
        shimmerGroup.gone()
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> {
                simulationGlobalError.setType(GlobalError.NO_CONNECTION)
            }
            is IllegalStateException -> {
                simulationGlobalError.setType(GlobalError.PAGE_FULL)
            }
            is PayLaterException.PayLaterNotApplicableException -> {
                payLaterTermsEmptyView.visible()
                return
            }
            else -> {
                simulationGlobalError.setType(GlobalError.SERVER_ERROR)
            }
        }
        simulationGlobalError.show()
        simulationGlobalError.setActionClickListener {
            simulationGlobalError.hide()
            shimmerGroup.visible()
            fetchSimulationData()
        }

    }

    private fun populateRowHeaders() {
        context?.let {
            val layoutParam = ViewGroup.LayoutParams(it.dpToPx(ROW_HEADER_WIDTH).toInt(), it.dpToPx(TABLE_ITEM_HEIGHT).toInt())
            llPayLaterPartner.apply {
                for (i in 0..4) {
                    when (i) {
                        0 -> addView(getBlankView(layoutParam))
                        1 -> addView(getRecomView(layoutParam))
                        else -> addView(getNoRecomView(layoutParam, i % 2 == 0))
                    }
                }
            }
        }
    }

    private fun populateSimulationTable(simulationDataList: ArrayList<PayLaterSimulationGatewayItem>) {
        context?.let {
            val tenureList = arrayOf(1, 3, 6, 9, 12)
            val rowCount = simulationDataList.size + 1
            val colCount = 5
            val contentLayoutParam = TableRow.LayoutParams(it.dpToPx(CONTENT_WIDTH).toInt(), it.dpToPx(TABLE_ITEM_HEIGHT).toInt())
            val tableLayoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT)
            for (i in 0 until rowCount) {
                val contentRow = TableRow(it)
                for (j in 0 until colCount) {
                    when (i) {
                        0 -> contentRow.addView(getColumnHeader(contentLayoutParam, j), contentLayoutParam)
                        1 -> {
                            contentRow.background = ContextCompat.getDrawable(it, R.drawable.ic_paylater_installment_green_border)
                            contentRow.addView(getInstallmentView(contentLayoutParam, simulationDataList[i - 1].installmentMap, tenureList, i, j), contentLayoutParam)
                        }
                        else -> contentRow.addView(getInstallmentView(contentLayoutParam, simulationDataList[i - 1].installmentMap, tenureList, i, j), contentLayoutParam)
                    }
                }
                tlInstallmentTable.addView(contentRow, tableLayoutParams)
            }
        }
    }

    private fun getBlankView(layoutParam: ViewGroup.LayoutParams): View {
        val blankSimulationTableHeading = simulationViewFactory.create(BlankViewTableRowHeader::class.java, layoutParam)
        return blankSimulationTableHeading.initUI()
    }

    private fun getNoRecomView(layoutParam: ViewGroup.LayoutParams, showBackGround: Boolean): View? {
        val noRecommendationViewSimulationTable = simulationViewFactory.create(NoRecommendationViewTableRowHeader::class.java, layoutParam)
        return noRecommendationViewSimulationTable.initUI(showBackGround)
    }

    private fun getRecomView(layoutParam: ViewGroup.LayoutParams): View {
        val recommendationView = simulationViewFactory.create(RecommendationViewTableRowHeader::class.java, layoutParam)
        return recommendationView.initUI()
    }

    private fun getInstallmentView(contentLayoutParam: ViewGroup.LayoutParams, installmentMap: HashMap<Int, SimulationItemDetail>, tenureList: Array<Int>, row: Int, col: Int): View {
        val installmentView = simulationViewFactory.create(InstallmentViewTableContent::class.java, contentLayoutParam)
        return installmentView.initUI(installmentMap, tenureList, row, col)
    }

    private fun getColumnHeader(contentLayoutParam: ViewGroup.LayoutParams, position: Int): View {
        val installmentColumnHeader = simulationViewFactory.create(InstallmentViewTableColumnHeader::class.java, contentLayoutParam)
        return installmentColumnHeader.initUI(position == 0)
    }

    fun setPayLaterClickedListener(registerPayLaterCallback: RegisterPayLaterCallback) {
        this.registerPayLaterCallback = registerPayLaterCallback
    }

    companion object {
        const val ROW_HEADER_WIDTH = 84
        const val TABLE_ITEM_HEIGHT = 54
        const val CONTENT_WIDTH = 110

        @JvmStatic
        fun newInstance(arguments: Bundle?): SimulationFragment {
            val fragment = SimulationFragment()
            arguments?.let { fragment.arguments = arguments }
            return fragment
        }
    }

    interface RegisterPayLaterCallback {
        fun onRegisterPayLaterClicked()
    }
}