package com.tokopedia.pdpsimulation.activateCheckout.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder.TenureViewHolder

class ActivationTenureAdapter(var tenureDetailList: List<TenureDetail>) : RecyclerView.Adapter<TenureViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TenureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TenureViewHolder.getViewHolder(inflater,parent)
    }


    override fun getItemCount(): Int {
      return  tenureDetailList.size
    }

    fun updateList(newTenureList:List<TenureDetail>)
    {
        this.tenureDetailList = newTenureList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TenureViewHolder, position: Int) {
        holder.bindData(tenureDetailList[position])
    }
}