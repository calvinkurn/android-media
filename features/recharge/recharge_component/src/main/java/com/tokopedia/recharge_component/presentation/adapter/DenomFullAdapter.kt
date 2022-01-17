package com.tokopedia.recharge_component.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomFullBinding
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.viewholder.denom.DenomFullViewHolder

class DenomFullAdapter: RecyclerView.Adapter<DenomFullViewHolder>(), RechargeDenomFullListener {

    private var listDenom = mutableListOf<DenomWidgetModel>()

    var selectedProductIndex: Int? = null

    var denomWidgetType: DenomWidgetEnum = DenomWidgetEnum.FULL_TYPE

    var listener: RechargeDenomFullListener? = null

    override fun getItemCount(): Int = listDenom.size

    override fun onBindViewHolder(holder: DenomFullViewHolder, position: Int) {
        holder.bind(
            listDenom[position],
            denomWidgetType,
            position == selectedProductIndex,
            position
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DenomFullViewHolder {
        val binding = ViewRechargeDenomFullBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DenomFullViewHolder(this, binding)
    }

    override fun onDenomFullClicked(denomGrid: DenomWidgetModel, position: Int) {
        if (selectedProductIndex == null) {
            selectedProductIndex = position
            notifyItemChanged(position)
        } else {
            selectedProductIndex?.let { selectedPos ->
                notifyItemChanged(selectedPos)
            }
            selectedProductIndex = position
            notifyItemChanged(position)
        }
        listener?.onDenomFullClicked(denomGrid, position)
    }

    override fun onChevronDenomClicked(denomGrid: DenomWidgetModel, position: Int) {
        listener?.onChevronDenomClicked(denomGrid, position)
    }

    fun setDenomFullList(listDenom: List<DenomWidgetModel>) {
        this.listDenom = listDenom.toMutableList()
        notifyDataSetChanged()
    }

    fun clearDenomFullData() {
        this.listDenom.clear()
    }
}