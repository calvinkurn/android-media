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

class SelectGateWayBottomSheet : BottomSheetUnify(), GateWayCardClicked {
    private val childLayoutRes = R.layout.activation_gateway_brand
    private var listOfGateway: List<CheckoutData> = ArrayList()
    private var gatewayListAdapter: GatewayListAdapter? = null

    private var quantitySelected = 0
    private var variantSelected = ""
    private var productId = ""
    private var selectedTenure = ""
    private var selectedEmiOption = ""


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
            productId = it.getString(CURRENT_PRODUCT_ID, "")
            selectedTenure = it.getString(CURRENT_SELECTED_TENURE, "")
            selectedEmiOption = it.getString(SELECTED_EMI, "")
            val gatewayList: PaylaterGetOptimizedModel? = it.getParcelable(GATEWAY_LIST)
            val selectedId = it.getString(SELECTED_GATEWAY) ?: ""
            gatewayList?.checkoutData?.let { listOfGateway ->
                setRecyclerData(listOfGateway, selectedId)
            }
        } ?: dismiss()
    }

    private fun setRecyclerData(gatewayList: List<CheckoutData>, selectedGateWayId: String) {
        gatewayList.filter {
            it.gateway_id.toString() == selectedGateWayId
        }.map {
            it.selectedGateway = true
        }.first()
        listOfGateway = gatewayList
        gatewayListAdapter =
            context?.let { context ->
                GatewayListAdapter(listOfGateway, this, context)
            }

        gatewayListRecycler.layoutManager = LinearLayoutManager(context)
        gatewayListAdapter?.let {
            gatewayListRecycler.adapter = it
        }


    }

    private fun sendClickAnalytics(checkoutData: CheckoutData) {
        activity?.let {
            sendAnalyticEvent(
                PdpSimulationEvent.ClickChangePartnerEvent(
                    productId, checkoutData.userState ?: "",
                    checkoutData.gateway_name.orEmpty(),
                    selectedEmiOption,
                    selectedTenure,
                    quantitySelected.toString(),
                    checkoutData.userAmount ?: "",
                    variantSelected
                )
            )
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

        const val SELECTED_EMI = "emi_amount"
        private const val TAG = "SelectGatewayBottomsheet"
        const val GATEWAY_LIST = "gateway_list"
        const val SELECTED_GATEWAY = "selected_gateway"
        const val CURRENT_VARINT = "selected_variant"
        const val CURRENT_QUANTITY = "selected_quantity"
        const val CURRENT_PRODUCT_ID = "selected_product_id"
        const val CURRENT_SELECTED_TENURE = "selected_tenure"

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

    override fun gatewayCardSelected(gatewayId: Int, newPosition: Int) {
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
        gatewayListAdapter?.updateFullList(listOfGateway)
    }
}

interface GateWayCardClicked {
    fun gatewayCardSelected(gatewayId: Int, newPosition: Int)
}