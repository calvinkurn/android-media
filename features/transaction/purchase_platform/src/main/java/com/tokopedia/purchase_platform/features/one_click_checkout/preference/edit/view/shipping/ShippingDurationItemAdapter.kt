package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ServicesItem
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ServicesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingprice.ServicesItemModelNoPrice
import kotlinx.android.synthetic.main.item_shipping_duration.view.*

class ShippingDurationItemAdapter(var listener: OnShippingMenuSelected) : RecyclerView.Adapter<ShippingDurationItemAdapter.ShippingDurationViewHolder>(){

    var shippingDurationList = mutableListOf<ServicesItem>()
    var lastCheckedPosition = -1
//    private var inflater: LayoutInflater = LayoutInflater.from(context)
//    private val listShippingDuration = emptyList<Preference>()
    var shippingDurationPositionId = -1

    interface OnShippingMenuSelected {
        fun onSelect(selection: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingDurationViewHolder {
        return ShippingDurationViewHolder(parent.inflateLayout(R.layout.item_shipping_duration))
    }

    override fun getItemCount(): Int {
        Log.d("itemList", shippingDurationList.size.toString())
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

    /*Inner View Holder*/
    inner class ShippingDurationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ServicesItemModelNoPrice) {
            with(itemView){
                text_shipping_item.text = data.servicesDuration
                item_shipping_price.visibility = View.GONE
                item_shipping_desc.visibility = View.GONE

                item_shipping_radio.isChecked = data.isSelected

                item_shipping_list.setOnClickListener {

                    listener.onSelect(data.serviceId)
                    /*data.serviceId?.let {
                        shippingDurationPositionId = it
                    }*/
                }


            }
        }

        fun bind(data: ServicesItemModel) {
            with(itemView){
                text_shipping_item.text = data.servicesName
                item_shipping_price.text = data.texts?.textRangePrice
                if(data.texts?.textsServiceDesc?.isNotEmpty() == true) {
                    item_shipping_desc.visible()
                    item_shipping_desc.text = data.texts?.textsServiceDesc
                } else {
                    item_shipping_desc.gone()
                }

                item_shipping_radio.isChecked = data.isSelected

                item_shipping_list.setOnClickListener {

                    listener.onSelect(data.servicesId)
                    /*data.serviceId?.let {
                        shippingDurationPositionId = it
                    }*/
                }


            }
        }
    }
}