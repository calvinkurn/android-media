package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OptionVariantUiModel.Companion.STATE_NOT_AVAILABLE
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OptionVariantUiModel.Companion.STATE_NOT_SELECTED
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OptionVariantUiModel.Companion.STATE_SELECTED

class VariantOptionAdapter(var dataList: ArrayList<OptionVariantUiModel>, val listener: CheckoutVariantActionListener) : RecyclerView.Adapter<OptionVariantViewHolder>(), VariantChangeListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionVariantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(OptionVariantViewHolder.LAYOUT, parent, false)
        return OptionVariantViewHolder(view, this)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return OptionVariantViewHolder.LAYOUT
    }

    override fun onBindViewHolder(holder: OptionVariantViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun onSelectedVariantChanged(selectedVariant: OptionVariantUiModel) {
        for (item: OptionVariantUiModel in dataList) {
            if (item.equals(selectedVariant)) {
                item.currentState = STATE_SELECTED
            } else if (item.currentState != STATE_NOT_AVAILABLE) {
                item.currentState = STATE_NOT_SELECTED
            }
        }
        notifyDataSetChanged()
        listener.onChangeVariant(selectedVariant)
    }

}