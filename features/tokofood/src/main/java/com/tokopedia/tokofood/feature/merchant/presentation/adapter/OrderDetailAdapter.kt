package com.tokopedia.tokofood.feature.merchant.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.tokofood.databinding.TokofoodItemOrderInfoLayoutBinding
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.viewholder.OrderDetailViewHolder

class OrderDetailAdapter(private val clickListener: OrderDetailViewHolder.OnOrderDetailItemClickListener)
    : RecyclerView.Adapter<OrderDetailViewHolder>() {

    private val customOrderDetails: MutableList<CustomOrderDetail> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailViewHolder {
        val binding = TokofoodItemOrderInfoLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: OrderDetailViewHolder, position: Int) {
        holder.bindData(customOrderDetails[position], position)
    }

    override fun getItemCount(): Int {
        return customOrderDetails.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCustomOrderDetails(customOrderDetails: List<CustomOrderDetail>) {
        if (customOrderDetails.isEmpty()) return
        this.customOrderDetails.clear()
        this.customOrderDetails.addAll(customOrderDetails)
        this.notifyDataSetChanged()
    }

    fun getCustomOrderDetails(): MutableList<CustomOrderDetail> {
        return customOrderDetails
    }

    fun removeCustomProduct(cartId: String, adapterPosition: Int) {
        try {
            customOrderDetails.removeFirst { it.cartId == cartId }
            notifyItemRemoved(adapterPosition)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}