package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping

import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping.ServicesItem
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping.ServicesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shipping.ServicesItemModelNoPrice
import com.tokopedia.unifyprinciples.Typography

class ShippingDurationItemAdapter(var listener: OnShippingMenuSelected) : RecyclerView.Adapter<ShippingDurationItemAdapter.ShippingDurationViewHolder>(){

    var shippingDurationList = mutableListOf<ServicesItem>()

    interface OnShippingMenuSelected {
        fun onSelect(selection: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingDurationViewHolder {
        return ShippingDurationViewHolder(parent.inflateLayout(R.layout.item_shipping_duration))
    }

    override fun getItemCount(): Int {
        return shippingDurationList.size
    }

    override fun onBindViewHolder(holder: ShippingDurationViewHolder, position: Int) {
        val servicesItem = shippingDurationList[position]
        if (servicesItem is ServicesItemModelNoPrice) {
            holder.bind(servicesItem)
        } else if (servicesItem is ServicesItemModel) {
            holder.bind(servicesItem)
        }
    }

    inner class ShippingDurationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var itemShippingText = itemView.findViewById<Typography>(R.id.text_shipping_item)
        private var itemShippingPrice = itemView.findViewById<Typography>(R.id.item_shipping_price)
        private var itemShippingDesc = itemView.findViewById<Typography>(R.id.item_shipping_desc)
        private var itemShippingRadio = itemView.findViewById<RadioButton>(R.id.item_shipping_radio)
        private var itemList = itemView.findViewById<ConstraintLayout>(R.id.item_shipping_list)


        fun bind(data: ServicesItemModelNoPrice) {
            itemShippingText.text = data.servicesDuration
            itemShippingPrice.gone()
            itemShippingDesc.gone()
            itemShippingRadio.isChecked = data.isSelected
            itemList.setOnClickListener {
                listener.onSelect(data.serviceId)
            }
        }

        fun bind(data: ServicesItemModel) {
            itemShippingText.text = data.servicesName
            itemShippingPrice.text = data.texts?.textRangePrice
            when {
                data.errorMessage.isNotEmpty() && data.errorMessage != "Belum PinPoint" -> {
                    itemShippingDesc.visible()
                    itemShippingDesc.text = data.errorMessage
                }
                data.texts?.textsServiceDesc?.isNotEmpty() == true -> {
                    itemShippingDesc.visible()
                    itemShippingDesc.text = data.texts?.textsServiceDesc
                }
                else -> {
                    itemShippingDesc.gone()
                }
            }
            itemShippingRadio.isChecked = data.isSelected

            itemList.setOnClickListener {
                listener.onSelect(data.servicesId)
            }
        }
    }
}