package com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.CheckoutData
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel
import com.tokopedia.pdpsimulation.activateCheckout.listner.ActivationListner
import com.tokopedia.unifycomponents.CardUnify
import kotlinx.android.synthetic.main.gateway_activation_individual_item.view.*
import kotlinx.android.synthetic.main.paylater_activation_individual_tenure.view.*

class GatewayViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bindData(checkoutData: CheckoutData) {
        itemView.apply {
            gatewayImage.setImageUrl(checkoutData.light_img_url)
            if(checkoutData.gateway_name.isNotBlank())
                gatewayHeader.text = checkoutData.gateway_name
            else
                gatewayHeader.visibility = View.GONE

            if(checkoutData.subtitle.isNotBlank())
                gatewaySubHeader.text = checkoutData.subtitle
            else
                gatewaySubHeader.visibility = View.GONE

            if(checkoutData.subtitle2.isNotBlank())
                gatewaySubHeader2.text = checkoutData.subtitle2
            else
                gatewaySubHeader2.visibility = View.GONE
        }
    }



    companion object {
        private val LAYOUT_ID = R.layout.gateway_activation_individual_item

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,

        ) =
            GatewayViewHolder(
                inflater.inflate(LAYOUT_ID, parent, false)
            )
    }
}