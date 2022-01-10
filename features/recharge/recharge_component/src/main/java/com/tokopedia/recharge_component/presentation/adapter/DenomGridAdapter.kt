package com.tokopedia.recharge_component.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.viewholder.denom.DenomGridViewHolder

class DenomGridAdapter: RecyclerView.Adapter<DenomGridViewHolder>(), RechargeDenomGridListener {

    private var listDenom = emptyList<DenomWidgetModel>()

    var selectedProductIndex: Int? = null

    var listener: RechargeDenomGridListener? = null

    override fun getItemCount(): Int = listDenom.size

    override fun onBindViewHolder(holder: DenomGridViewHolder, position: Int) {
        holder.bind(listDenom[position], position == selectedProductIndex, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DenomGridViewHolder {
        val binding = ViewRechargeDenomGridBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DenomGridViewHolder(this, binding)
    }

    override fun onDenomGridClicked(denomGrid: DenomWidgetModel, position: Int) {
        if (selectedProductIndex == null){
            selectedProductIndex = position
            notifyItemChanged(position)
        } else {
            selectedProductIndex?.let { selectedPos ->
                notifyItemChanged(selectedPos)
            }
            selectedProductIndex = position
            notifyItemChanged(position)
        }
        listener?.onDenomGridClicked(denomGrid, position)
    }

    fun setDenomGridList(listDenom: List<DenomWidgetModel>) {
        this.listDenom = listDenom
        notifyDataSetChanged()
    }
}