package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.variant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.variant.viewholder.SelectedOptionVariantViewHolder
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.TypeVariantUiModel

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