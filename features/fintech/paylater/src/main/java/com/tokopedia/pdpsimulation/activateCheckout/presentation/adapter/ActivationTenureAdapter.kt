package com.tokopedia.pdpsimulation.activateCheckout.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.helper.DataMapper
import com.tokopedia.pdpsimulation.activateCheckout.listner.TenureSelectListner
import com.tokopedia.pdpsimulation.activateCheckout.presentation.viewHolder.TenureViewHolder

class ActivationTenureAdapter(var tenureDetailList: List<TenureDetail>,val listner:TenureSelectListner) : RecyclerView.Adapter<TenureViewHolder>() {

    var selectedPosition = 0;

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TenureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TenureViewHolder.getViewHolder(inflater,parent,listner)
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
        holder.bindData(tenureDetailList[position],
            DataMapper.mapToInstallationDetail(tenureDetailList[position]),position)
    }
}