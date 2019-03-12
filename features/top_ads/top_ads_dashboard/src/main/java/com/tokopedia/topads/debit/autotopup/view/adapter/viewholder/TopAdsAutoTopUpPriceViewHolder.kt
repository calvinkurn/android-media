package com.tokopedia.topads.debit.autotopup.view.adapter.viewholder

import android.support.v4.content.ContextCompat
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpItem
import kotlinx.android.synthetic.main.item_auto_topup_price_selection.view.*

class TopAdsAutoTopUpPriceViewHolder(val view: View,
                                     private val listener: ItemListener?)
    : AbstractViewHolder<AutoTopUpItem>(view) {

    override fun bind(element: AutoTopUpItem) {
        with(itemView){
            if (listener?.isActive() == true){
                title.setTextColor(ContextCompat.getColor(context, R.color.light_primary))
                radio_button.isEnabled = true
            } else {
                title.setTextColor(ContextCompat.getColor(context, R.color.black_24))
                radio_button.isEnabled = false
            }

            title.text = element.priceFmt
            radio_button.isChecked = listener?.isSelected(element.id) == true
            radio_button.setOnClickListener {
                if (listener?.isActive() == true) {
                    listener.onSelected(element)
                }
            }

            setOnClickListener {
                if (listener?.isActive() == true) {
                    listener.onSelected(element)
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_auto_topup_price_selection
    }

    interface ItemListener {
        fun isActive(): Boolean
        fun isSelected(id: Int): Boolean
        fun onSelected(element: AutoTopUpItem)
    }
}