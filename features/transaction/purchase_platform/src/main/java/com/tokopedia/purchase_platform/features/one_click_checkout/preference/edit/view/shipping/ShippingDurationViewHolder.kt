package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import kotlinx.android.synthetic.main.item_shipping_duration.view.*

class ShippingDurationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    private var lastCheckedPosition = -1
    var shippingItemList = mutableListOf<Preference>()

    init {
        itemView.apply {
            item_shipping_radio.setOnClickListener {
                shippingItemList[position].isSelected = true
                lastCheckedPosition = adapterPosition
            }
        }
    }

    fun bind(item: Preference) {
        with(itemView){
            text_shipping_item.text = item.text
            item_shipping_radio.isChecked = item.isSelected
        }
    }

    companion object{
        val Layout = R.layout.item_shipping_duration
    }
}