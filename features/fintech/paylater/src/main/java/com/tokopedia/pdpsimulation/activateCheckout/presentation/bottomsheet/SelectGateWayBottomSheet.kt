package com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.CheckoutData
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.PaylaterGetOptimizedModel
import com.tokopedia.unifycomponents.BottomSheetUnify

class SelectGateWayBottomSheet : BottomSheetUnify() {
    private val childLayoutRes = R.layout.activation_gateway_brand


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        getArgumentData()
    }

    private fun getArgumentData() {
        arguments?.let {
            val gatewayList: PaylaterGetOptimizedModel? = it.getParcelable(GATEWAY_LIST)
            gatewayList?.checkoutData?.let { listOfGateway->
                 setRecyclerData(listOfGateway)
            }
        } ?: dismiss()
    }

    private fun setRecyclerData(gatewayList: List<CheckoutData>) {

    }


    private fun initView() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }


    companion object {

        private const val TAG = "SelectGatewayBottomsheet"
        const val GATEWAY_LIST = "gateway_list"

        fun show(
            bundle: Bundle,
            childFragmentManager: FragmentManager
        ) {
            val actionStepsBottomSheet = SelectGateWayBottomSheet()
            actionStepsBottomSheet.arguments = bundle
            actionStepsBottomSheet.show(childFragmentManager, TAG)
        }
    }
}