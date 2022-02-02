package com.tokopedia.pdpsimulation.activateCheckout.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.helper.DataMapper
import com.tokopedia.pdpsimulation.activateCheckout.listner.ActivationListner
import com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder.GatewayViewHolder
import com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder.TenureViewHolder

class GatewayListAdapter(
    var tenureDetailList: List<TenureDetail>,
) : RecyclerView.Adapter<GatewayViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GatewayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GatewayViewHolder.getViewHolder(inflater, parent)
    }


    override fun getItemCount(): Int {
        return tenureDetailList.size
    }


    override fun onBindViewHolder(holder: GatewayViewHolder, position: Int) {

    }

}