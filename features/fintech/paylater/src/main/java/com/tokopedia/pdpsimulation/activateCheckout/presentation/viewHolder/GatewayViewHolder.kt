package com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.CheckoutData
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.GateWayCardClicked
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.utils.resources.isDarkMode
import kotlinx.android.synthetic.main.gateway_activation_individual_item.view.*

class GatewayViewHolder(
    itemView: View,
    private val gatewayCardClicked: GateWayCardClicked,
    val context: Context
) :
    RecyclerView.ViewHolder(itemView) {


    fun bindData(checkoutData: CheckoutData, position: Int) {
        itemView.apply {
//            if(checkoutData.selectedGateway)
//                radioGatewaySelector.isChecked = true
            changeColorToEnableDisable(checkoutData.disable)
            setIcon(checkoutData)
            inflateAllDetails(checkoutData)

            if (!checkoutData.disable && checkoutData.selectedGateway) {
                individualInsideCardContainer.setBackgroundColor(context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_GN50))
                individualGatewayItemContainer.cardType = CardUnify.TYPE_BORDER_ACTIVE
                radioGatewaySelector.isChecked = true
            } else {
                individualInsideCardContainer.setBackgroundColor((context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0)))
                individualGatewayItemContainer.cardType = CardUnify.TYPE_BORDER
                radioGatewaySelector.isChecked = checkoutData.selectedGateway
            }

            if (!checkoutData.disable && !checkoutData.selectedGateway) {
                onClickLogic(checkoutData, position)
            }
        }

    }

    private fun View.onClickLogic(checkoutData: CheckoutData, position: Int) {
        itemView.radioGatewaySelector.setOnClickListener {
            individualInsideCardContainer.setBackgroundColor((context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_GN50)))
            individualGatewayItemContainer.cardType = CardUnify.TYPE_BORDER_ACTIVE
            radioGatewaySelector.isChecked = true
            gatewayCardClicked.gatewayCardSelected(checkoutData.gateway_id, newPosition = position)
        }
        itemView.setOnClickListener {
            individualInsideCardContainer.setBackgroundColor((context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_GN50)))
            individualGatewayItemContainer.cardType = CardUnify.TYPE_BORDER_ACTIVE
            radioGatewaySelector.isChecked = true
            gatewayCardClicked.gatewayCardSelected(checkoutData.gateway_id, newPosition = position)

        }
    }

    private fun View.inflateAllDetails(checkoutData: CheckoutData) {
        if (!checkoutData.gateway_name.isNullOrBlank())
            gatewayHeader.text = checkoutData.gateway_name
        else
            gatewayHeader.visibility = View.GONE

        if (!checkoutData.subtitle.isNullOrBlank())
            gatewaySubHeader.text = checkoutData.subtitle
        else
            gatewaySubHeader.visibility = View.GONE

        if (!checkoutData.subtitle2.isNullOrBlank())
            gatewaySubHeader2.text = checkoutData.subtitle2
        else
            gatewaySubHeader2.visibility = View.GONE
    }

    private fun View.setIcon(checkoutData: CheckoutData) {
        if (context.isDarkMode())
            checkoutData.dark_img_url?.let { gatewayImage.setImageUrl(it) }
        else
            checkoutData.light_img_url?.let { gatewayImage.setImageUrl(it) }
    }


    private fun changeColorToEnableDisable(disable: Boolean) {
        itemView.apply {
            if (disable) {
                gatewayHeader.isEnabled = false
                gatewaySubHeader.isEnabled = false
                gatewaySubHeader2.isEnabled = false
                radioGatewaySelector.isEnabled = false
                gatewayImage.alpha = 0.4f
            } else {
                gatewayImage.alpha = 1.0f
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
            parentContext: Context

        ) =
            GatewayViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false), gatewayCardClicked, parentContext
            )
    }
}