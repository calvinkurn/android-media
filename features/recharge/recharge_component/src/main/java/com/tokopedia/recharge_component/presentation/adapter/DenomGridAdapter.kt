package com.tokopedia.recharge_component.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.presentation.adapter.viewholder.denom.DenomGridViewHolder

class DenomGridAdapter (private val denomListener: RechargeDenomGridListener) :
    RecyclerView.Adapter<DenomGridViewHolder>() {

    private var listDenom = emptyList<DenomWidgetModel>()

    override fun getItemCount(): Int = listDenom.size

    override fun onBindViewHolder(holder: DenomGridViewHolder, position: Int) {
        holder.bind(listDenom[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DenomGridViewHolder {
        val binding = ViewRechargeDenomGridBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DenomGridViewHolder(denomListener, binding)
    }

    fun setDenomGridList(listDenom: List<DenomWidgetModel>) {
        this.listDenom = listDenom
    }
}