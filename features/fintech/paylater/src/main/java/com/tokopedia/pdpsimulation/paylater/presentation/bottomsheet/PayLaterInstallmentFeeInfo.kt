package com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PayLaterAnalyticsBase
import com.tokopedia.pdpsimulation.paylater.PdpSimulationCallback
import com.tokopedia.pdpsimulation.paylater.domain.model.InstallmentDetails
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterOptionInteraction
import com.tokopedia.pdpsimulation.paylater.presentation.adapter.PayLaterAdapterFactoryImpl
import com.tokopedia.pdpsimulation.paylater.presentation.adapter.PayLaterSimulationAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.paylater_additional_fee_info_bottomsheet.*

class PayLaterInstallmentFeeInfo : BottomSheetUnify() {

    private var installmentDetails: InstallmentDetails? = null
    private val childLayoutRes = R.layout.paylater_additional_fee_info_bottomsheet
    private var impression: PayLaterAnalyticsBase? = null

    private fun getAdapterTypeFactory() =
        PayLaterAdapterFactoryImpl(PayLaterOptionInteraction({}, {}, {}, {}, {}))

    private val simulationAdapter: PayLaterSimulationAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PayLaterSimulationAdapter(getAdapterTypeFactory())
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    private fun initArguments() {
        arguments?.let {
            installmentDetails = it.getParcelable(INSTALLMENT_DETAIL)
            if (it.containsKey(IMPRESSION_DETAIL))
                impression = it.getParcelable(IMPRESSION_DETAIL)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initArguments()
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvInstallmentDetail.adapter = simulationAdapter
        rvInstallmentDetail.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvInstallmentDetail.setHasFixedSize(true)
        simulationAdapter.addAllElements(installmentDetails?.content ?: listOf())
        sendEvent(impression)
        setTicker()
    }

    private fun setTicker() {
        groupTicker.shouldShowWithAction(installmentDetails?.ticker?.tickerWordings?.isNotEmpty() ?: false) {
            tvTicker.text = installmentDetails?.ticker?.tickerWordings
            ivTicker.setImageUrl(installmentDetails?.ticker?.image ?: String.EMPTY)
        }
    }

    private fun sendEvent(impression: PayLaterAnalyticsBase?) {
        if (impression != null) {
            activity?.let {
                (it as PdpSimulationCallback).sendAnalytics(impression)
            }
        }
    }

    private fun setDefaultParams() {
        setTitle(installmentDetails?.header ?: "Rincian pembayaran")
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight()).toDp()
    }

    companion object {

        private const val TAG = "PayLaterAdditionalFeeInfo"
        const val INSTALLMENT_DETAIL = "installment"
        const val IMPRESSION_DETAIL = "impression"

        fun show(
            bundle: Bundle,
            childFragmentManager: FragmentManager
        ) {
            val actionStepsBottomSheet = PayLaterInstallmentFeeInfo()
            actionStepsBottomSheet.arguments = bundle
            actionStepsBottomSheet.show(childFragmentManager, TAG)
        }
    }


}
