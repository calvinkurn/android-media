package com.tokopedia.pdpsimulation.activateCheckout.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.CheckoutData
import com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder.GatewayViewHolder

class GatewayListAdapter(
    var gatewayDetailList: List<CheckoutData>,
) : RecyclerView.Adapter<GatewayViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GatewayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GatewayViewHolder.getViewHolder(inflater, parent)
    }


    override fun getItemCount(): Int {
        return gatewayDetailList.size
    }


    override fun onBindViewHolder(holder: GatewayViewHolder, position: Int) {
        holder.bindData(gatewayDetailList[position])
    }

}