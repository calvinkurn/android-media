package com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.CheckoutData
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel
import com.tokopedia.pdpsimulation.activateCheckout.listner.ActivationListner
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.GateWayCardClicked
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.SelectGateWayBottomSheet
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.android.synthetic.main.gateway_activation_individual_item.view.*
import kotlinx.android.synthetic.main.paylater_activation_individual_tenure.view.*

class GatewayViewHolder(itemView: View, private val gatewayCardClicked: GateWayCardClicked, val context: Context) :
    RecyclerView.ViewHolder(itemView) {


    fun bindData(checkoutData: CheckoutData, position: Int) {
        itemView.apply {
            changeColorToEnableDisable(checkoutData.disable)
            setIcon(checkoutData)
            inflateAllDetails(checkoutData)
            individualGatewayItemContainer.cardType = CardUnify.TYPE_BORDER
            radioGatewaySelector.isChecked = false
            if(!checkoutData.disable) {
                itemView.radioGatewaySelector.setOnClickListener {
                    individualGatewayItemContainer.cardType = CardUnify.TYPE_BORDER_ACTIVE
                    radioGatewaySelector.isChecked = true
                    gatewayCardClicked.gatewayCardSelected(checkoutData.gateway_id)
                }
                itemView.setOnClickListener {
                    individualGatewayItemContainer.cardType = CardUnify.TYPE_BORDER_ACTIVE
                    radioGatewaySelector.isChecked = true
                    gatewayCardClicked.gatewayCardSelected(checkoutData.gateway_id)

                }
            }
        }



    }

    private fun View.inflateAllDetails(checkoutData: CheckoutData) {
        if (checkoutData.gateway_name.isNotBlank())
            gatewayHeader.text = checkoutData.gateway_name
        else
            gatewayHeader.visibility = View.GONE

        if (checkoutData.subtitle.isNotBlank())
            gatewaySubHeader.text = checkoutData.subtitle
        else
            gatewaySubHeader.visibility = View.GONE

        if (checkoutData.subtitle2.isNotBlank())
            gatewaySubHeader2.text = checkoutData.subtitle2
        else
            gatewaySubHeader2.visibility = View.GONE
    }

    private fun View.setIcon(checkoutData: CheckoutData) {
        if (context.isDarkMode())
            gatewayImage.setImageUrl(checkoutData.dark_img_url)
        else
            gatewayImage.setImageUrl(checkoutData.light_img_url)
    }


    private fun changeColorToEnableDisable(disable: Boolean) {
        itemView.apply{
            if(disable)
            {
                gatewayHeader.isEnabled = false
                gatewaySubHeader.isEnabled = false
                radioGatewaySelector.isEnabled = false
                gatewaySubHeader2.isEnabled = false
            }
            else
            {
                gatewayHeader.isEnabled = true
                gatewaySubHeader.isEnabled = true
                radioGatewaySelector.isEnabled = true
                gatewaySubHeader2.isEnabled = true
            }
        }

    }


    companion object {
        private val LAYOUT_ID = R.layout.gateway_activation_individual_item

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            gatewayCardClicked: GateWayCardClicked,
            parentContext:Context

        ) =
            GatewayViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false),gatewayCardClicked,parentContext
            )
    }
}