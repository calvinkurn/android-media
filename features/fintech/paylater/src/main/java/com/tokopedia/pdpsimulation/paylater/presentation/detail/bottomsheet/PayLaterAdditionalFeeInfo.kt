package com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp

class PayLaterAdditionalFeeInfo : BottomSheetUnify() {

    private var pdpSimulationCallback: PdpSimulationCallback? = null


    private val childLayoutRes = R.layout.paylater_car_additional_fee_info

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    private fun setDefaultParams() {
        setTitle(getString(R.string.pay_later_its_not_final_price))
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight()).toDp()
    }


    companion object {

        private const val TAG = "PayLaterAdditionalFeeInfo"
        fun show(
            pdpSimulationCallback: PdpSimulationCallback,
            childFragmentManager: FragmentManager
        ) {
            val actionStepsBottomSheet = PayLaterAdditionalFeeInfo()
            actionStepsBottomSheet.pdpSimulationCallback = pdpSimulationCallback
            actionStepsBottomSheet.show(childFragmentManager, TAG)
        }
    }


}