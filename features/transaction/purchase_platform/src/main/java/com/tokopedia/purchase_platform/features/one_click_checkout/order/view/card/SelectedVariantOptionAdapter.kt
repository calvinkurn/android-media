package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SelectedVariantOptionAdapter(private val selectedList: ArrayList<TypeVariantUiModel>) : RecyclerView.Adapter<SelectedOptionVariantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedOptionVariantViewHolder {
        return SelectedOptionVariantViewHolder(LayoutInflater.from(parent.context).inflate(SelectedOptionVariantViewHolder.LAYOUT, parent, false))
    }

    override fun getItemCount(): Int {
        return selectedList.size
    }

    override fun onBindViewHolder(holder: SelectedOptionVariantViewHolder, position: Int) {
        holder.bind(selectedList[position])
    }
}