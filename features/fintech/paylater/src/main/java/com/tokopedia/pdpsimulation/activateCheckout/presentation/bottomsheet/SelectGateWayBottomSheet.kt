package com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.CheckoutData
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.PaylaterGetOptimizedModel
import com.tokopedia.pdpsimulation.activateCheckout.listner.GatewaySelectActivityListner
import com.tokopedia.pdpsimulation.activateCheckout.presentation.adapter.GatewayListAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.activation_gateway_brand.*

class SelectGateWayBottomSheet : BottomSheetUnify() {
    private val childLayoutRes = R.layout.activation_gateway_brand
    private  var listOfGateway: List<CheckoutData> = ArrayList()
    private  var gatewayListAdapter: GatewayListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getArgumentData()
        setDefaultParams()
        setTitle(getString(R.string.which_paylater_you_want))
    }

    private fun getArgumentData() {
        arguments?.let {
            val gatewayList: PaylaterGetOptimizedModel? = it.getParcelable(GATEWAY_LIST)
            val selectedId= it.getString(SELECTED_GATEWAY)?:""
            gatewayList?.checkoutData?.let { listOfGateway->
                 setRecyclerData(listOfGateway,selectedId)
            }
        } ?: dismiss()
    }

    private fun setRecyclerData(gatewayList: List<CheckoutData>,selectedGateWayId:String) {
        for(i in gatewayList.indices)
        {
            if(gatewayList[i].gateway_id.toString() == selectedGateWayId)
            {
                gatewayList[i].selectedGateway = true
                break
            }
        }
        listOfGateway = gatewayList
          gatewayListAdapter =
            context?.let { context->
                GatewayListAdapter(listOfGateway,object :GateWayCardClicked{
                    override fun gatewayCardSelected(
                        gatewayId: Int,
                        oldPosition: Int,
                        newPosition: Int
                    ) {
                        activity?.let { fragmentActivity->
                            (fragmentActivity as GatewaySelectActivityListner).setGatewayValue(gatewayId)
                        }
                        if(oldPosition != newPosition)
                        {
                            listOfGateway[oldPosition].selectedGateway = false
                            listOfGateway[newPosition].selectedGateway = true
                            gatewayListAdapter?.updateList(listOfGateway,oldPosition,newPosition)
                        }

                    }

                }, context)
            }


        gatewayListRecycler.layoutManager = LinearLayoutManager(context)
        gatewayListAdapter?.let {
            gatewayListRecycler.adapter = it
        }


    }


    private fun initView() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight()).toDp()
    }


    companion object {

        private const val TAG = "SelectGatewayBottomsheet"
        const val GATEWAY_LIST = "gateway_list"
        const val SELECTED_GATEWAY = "selected_gateway"

        fun show(
            bundle: Bundle,
            childFragmentManager: FragmentManager
        ):SelectGateWayBottomSheet  {
            val actionStepsBottomSheet = SelectGateWayBottomSheet()
            actionStepsBottomSheet.arguments = bundle
            actionStepsBottomSheet.show(childFragmentManager, TAG)
            return actionStepsBottomSheet
        }
    }
}

interface GateWayCardClicked
{
    fun gatewayCardSelected(gatewayId: Int,oldPosition: Int, newPosition: Int)
}