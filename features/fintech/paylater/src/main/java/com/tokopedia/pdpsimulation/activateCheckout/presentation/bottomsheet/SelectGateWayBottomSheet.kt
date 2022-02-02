package com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.CheckoutData
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.PaylaterGetOptimizedModel
import com.tokopedia.pdpsimulation.activateCheckout.presentation.adapter.GatewayListAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.activation_gateway_brand.*

class SelectGateWayBottomSheet : BottomSheetUnify() {
    private val childLayoutRes = R.layout.activation_gateway_brand

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        gatewayListRecycler.layoutManager = LinearLayoutManager(context)
        gatewayListRecycler.adapter = GatewayListAdapter(gatewayList)
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