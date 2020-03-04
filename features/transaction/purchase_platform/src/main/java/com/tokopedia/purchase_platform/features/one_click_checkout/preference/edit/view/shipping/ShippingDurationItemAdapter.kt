package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice.ServicesItemModelNoPrice
import kotlinx.android.synthetic.main.item_shipping_duration.view.*

class ShippingDurationItemAdapter : RecyclerView.Adapter<ShippingDurationItemAdapter.ShippingDurationViewHolder>(){

    var shippingDurationList = mutableListOf<ServicesItemModelNoPrice>()
    var lasCheckedPosition = -1
//    private var inflater: LayoutInflater = LayoutInflater.from(context)
//    private val listShippingDuration = emptyList<Preference>()
    private val listener: OnShippingMenuSelected? = null

    interface OnShippingMenuSelected {
        fun onSelect(selection: Int?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingDurationViewHolder {
        return ShippingDurationViewHolder(parent.inflateLayout(R.layout.item_shipping_duration))
    }

    override fun getItemCount(): Int {
        return shippingDurationList.size
    }

    override fun onBindViewHolder(holder: ShippingDurationViewHolder, position: Int) {
        holder.bind(shippingDurationList[position])
    }

    /*Inner View Holder*/
    inner class ShippingDurationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: ServicesItemModelNoPrice) {
            with(itemView){

                val currentPosition = adapterPosition
                text_shipping_item.text = data.servicesDuration
                item_shipping_price.visibility = View.GONE
                item_shipping_desc.visibility = View.GONE

                item_shipping_radio.isChecked = lasCheckedPosition == currentPosition

                item_shipping_list.setOnClickListener {

                    val position = lasCheckedPosition
                    lasCheckedPosition = currentPosition
                    if(position > -1) notifyItemChanged(position)
                    notifyItemChanged(lasCheckedPosition)

                    listener?.onSelect(data.serviceId)
                    Log.d("ID_NIH", listener?.onSelect(data.serviceId).toString())
                }


            }
        }
    }
}