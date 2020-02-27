package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OrderProductVariantAdapter(val listener: CheckoutVariantActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<VariantUiModel> = emptyList()

    fun setList(list: List<VariantUiModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TypeVariantViewHolder.LAYOUT) {
            return TypeVariantViewHolder(LayoutInflater.from(parent.context).inflate(TypeVariantViewHolder.LAYOUT, parent, false), listener)
        }
        return SelectedTypeVariantViewHolder(LayoutInflater.from(parent.context).inflate(SelectedTypeVariantViewHolder.LAYOUT, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TypeVariantViewHolder) {
            holder.bind(list[position] as TypeVariantUiModel)
        } else {
            (holder as SelectedTypeVariantViewHolder).bind(list[position] as SelectedTypeVariantUiModel)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position] is TypeVariantUiModel) {
            return TypeVariantViewHolder.LAYOUT
        }
        return SelectedTypeVariantViewHolder.LAYOUT
    }
}