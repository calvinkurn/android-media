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
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.paylater.PdpSimulationCallback
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.activation_gateway_brand.*

class SelectGateWayBottomSheet : BottomSheetUnify() {
    private val childLayoutRes = R.layout.activation_gateway_brand
    private var listOfGateway: List<CheckoutData> = ArrayList()
    private var gatewayListAdapter: GatewayListAdapter? = null

    private var quantitySelected = 0;
    private var variantSelected = ""

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
            quantitySelected = it.getInt(CURRENT_QUANTITY, 0)
            variantSelected = it.getString(CURRENT_VARINT, "")
            val gatewayList: PaylaterGetOptimizedModel? = it.getParcelable(GATEWAY_LIST)
            val selectedId = it.getString(SELECTED_GATEWAY) ?: ""
            gatewayList?.checkoutData?.let { listOfGateway ->
                setRecyclerData(listOfGateway, selectedId)
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
        sendAnalyticsImpression(gatewayList)
        listOfGateway = gatewayList
          gatewayListAdapter =
            context?.let { context->
                GatewayListAdapter(listOfGateway,object :GateWayCardClicked{
                    override fun gatewayCardSelected(
                        gatewayId: Int,
                        newPosition: Int
                    ) {
                        activity?.let { fragmentActivity ->
                            (fragmentActivity as GatewaySelectActivityListner).setGatewayValue(
                                gatewayId
                            )
                        }
                        for (i in listOfGateway.indices) {
                            listOfGateway[i].selectedGateway = i == newPosition
                        }
                        if (listOfGateway.size > newPosition) {
                            sendClickAnalytics(listOfGateway[newPosition])
                        }
                        gatewayListAdapter?.updateList(listOfGateway)

                    }

                }, context)
            }


        gatewayListRecycler.layoutManager = LinearLayoutManager(context)
        gatewayListAdapter?.let {
            gatewayListRecycler.adapter = it
        }


    }

    private fun sendClickAnalytics(checkoutData: CheckoutData) {
        activity?.let {
            checkoutData.userAmount?.let { limit ->
                checkoutData.userState?.let { userState ->
                   sendAnalyticEvent( PdpSimulationEvent.ClickChangePartnerEvent(
                        checkoutData.gateway_name,
                        limit, quantitySelected.toString(), variantSelected, userState
                    ))
                }
            }
        }
    }

    private fun sendAnalyticsImpression(gatewayList: List<CheckoutData>) {
        for (i in gatewayList.indices) {
            activity?.let {
                gatewayList[i].userAmount?.let { limit ->
                    gatewayList[i].userState?.let { userState ->
                        sendAnalyticEvent(PdpSimulationEvent.ChangePartnerImperssion(
                            gatewayList[i].gateway_name,
                            limit, quantitySelected.toString(), variantSelected, userState
                        ))
                    }
                }
            }
        }
    }

    private fun sendAnalyticEvent(event: PdpSimulationEvent) {
        activity?.let {
            (it as PdpSimulationCallback).sendOtherAnalytics(event)
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
        const val CURRENT_VARINT = "selected_variant"
        const val CURRENT_QUANTITY = "selected_quantity"

        fun show(
            bundle: Bundle,
            childFragmentManager: FragmentManager
        ): SelectGateWayBottomSheet {
            val actionStepsBottomSheet = SelectGateWayBottomSheet()
            actionStepsBottomSheet.arguments = bundle
            actionStepsBottomSheet.show(childFragmentManager, TAG)
            return actionStepsBottomSheet
        }
    }
}

interface GateWayCardClicked
{
    fun gatewayCardSelected(gatewayId: Int, newPosition: Int)
}