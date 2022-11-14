package com.tokopedia.mvc.presentation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.mvc.databinding.SmvcItemVoucherBinding

class VouchersAdapter: RecyclerView.Adapter<VouchersViewHolder>() {

    private var data: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VouchersViewHolder {
        val binding = SmvcItemVoucherBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return VouchersViewHolder(binding)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: VouchersViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<String>) {
        data = newData.toMutableList()
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    fun addDataList(newData: List<String>) {
        data.addAll(newData)
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }
}
