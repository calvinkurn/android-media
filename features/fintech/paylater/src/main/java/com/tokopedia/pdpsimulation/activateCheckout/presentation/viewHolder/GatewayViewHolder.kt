package com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel
import com.tokopedia.pdpsimulation.activateCheckout.listner.ActivationListner
import com.tokopedia.unifycomponents.CardUnify
import kotlinx.android.synthetic.main.paylater_activation_individual_tenure.view.*

class GatewayViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    fun bindData(

    ) {
        itemView.apply {


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