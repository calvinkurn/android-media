package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.variant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.variant.viewholder.SelectedTypeVariantViewHolder
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.variant.viewholder.TypeVariantViewHolder
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.variant.listener.CheckoutVariantActionListener
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.SelectedTypeVariantUiModel
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.TypeVariantUiModel
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.VariantUiModel

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