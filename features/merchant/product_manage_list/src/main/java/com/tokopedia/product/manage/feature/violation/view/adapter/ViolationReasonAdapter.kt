package com.tokopedia.product.manage.feature.violation.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ViolationReasonAdapter(private val context: Context,
                             private val detailList: List<String>,
                             private val listener: ViolationReasonItemViewHolder.Listener)
    : RecyclerView.Adapter<ViolationReasonItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViolationReasonItemViewHolder {
        return ViolationReasonItemViewHolder(
            LayoutInflater.from(context).inflate(
                ViolationReasonItemViewHolder.LAYOUT,
                parent,
                false),
            listener
        )
    }

    override fun onBindViewHolder(holder: ViolationReasonItemViewHolder, position: Int) {
        holder.bind(detailList[position])
    }

    override fun getItemCount(): Int = detailList.size
}