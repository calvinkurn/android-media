package com.tokopedia.pdpsimulation.activateCheckout.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.CheckoutData
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.GateWayCardClicked
import com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder.GatewayViewHolder

class GatewayListAdapter(
        var gatewayDetailList: List<CheckoutData>,
        private val gatewayClickListener: GateWayCardClicked,
        val context: Context,
) : RecyclerView.Adapter<GatewayViewHolder>() {


    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): GatewayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GatewayViewHolder.getViewHolder(inflater, parent, gatewayClickListener, context)
    }


    override fun getItemCount(): Int {
        return gatewayDetailList.size
    }


    override fun onBindViewHolder(holder: GatewayViewHolder, position: Int) {
        holder.bindData(gatewayDetailList[position], position)
    }

    fun updateFullList(listOfGateway: List<CheckoutData>) {
        this.gatewayDetailList = listOfGateway
        notifyDataSetChanged()
    }

}