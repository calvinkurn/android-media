package com.tokopedia.oneclickcheckout.preference.edit.view.shipping

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticdata.data.constant.CourierConstant
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.LogisticPromoInfo
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.ServicesItem
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.ServicesItemModel
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.ServicesItemModelNoPrice
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class ShippingDurationItemAdapter(var listener: OnShippingMenuSelected) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val shippingDurationList = mutableListOf<ServicesItem>()

    companion object {
        private const val SHIPPING_DURATION_VIEW_TYPE = 1
        private const val LOGISTIC_PROMO_INFO_VIEW_TYPE = 2
    }

    fun renderData(data: List<ServicesItem>) {
        shippingDurationList.clear()
        shippingDurationList.addAll(data)
        notifyDataSetChanged()
    }

    interface OnShippingMenuSelected {
        fun onSelect(selection: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == LOGISTIC_PROMO_INFO_VIEW_TYPE) {
            return LogisticPromoInfoViewHolder(parent.inflateLayout(R.layout.item_logistic_promo_info))
        }
        return ShippingDurationViewHolder(parent.inflateLayout(R.layout.item_shipping_duration))
    }

    override fun getItemCount(): Int {
        return shippingDurationList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (shippingDurationList[position] is LogisticPromoInfo) {
            return LOGISTIC_PROMO_INFO_VIEW_TYPE
        }
        return SHIPPING_DURATION_VIEW_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val servicesItem = shippingDurationList[position]
        if (servicesItem is ServicesItemModelNoPrice) {
            (holder as ShippingDurationViewHolder).bind(servicesItem)
        } else if (servicesItem is ServicesItemModel) {
            (holder as ShippingDurationViewHolder).bind(servicesItem)
        } else if (servicesItem is LogisticPromoInfo) {
            (holder as LogisticPromoInfoViewHolder).bind(servicesItem)
        }
    }

    inner class ShippingDurationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var itemShippingText = itemView.findViewById<Typography>(R.id.text_shipping_item)
        private var itemShippingPrice = itemView.findViewById<Typography>(R.id.item_shipping_price)
        private var itemShippingDesc = itemView.findViewById<Typography>(R.id.item_shipping_desc)
        private var itemShippingRadio = itemView.findViewById<RadioButtonUnify>(R.id.item_shipping_radio)
        private var itemList = itemView.findViewById<ConstraintLayout>(R.id.item_shipping_list)


        fun bind(data: ServicesItemModelNoPrice) {
            itemShippingText.text = data.servicesDuration
            itemShippingPrice.gone()
            itemShippingDesc.gone()
            itemShippingRadio.isChecked = data.isSelected
            itemShippingRadio.skipAnimation()
            itemList.setOnClickListener {
                listener.onSelect(data.serviceId)
            }
        }

        fun bind(data: ServicesItemModel) {
            itemShippingText.text = data.servicesName
            if (data.texts?.textRangePrice?.isNotBlank() == true) {
                itemShippingPrice.text = data.texts?.textRangePrice
                itemShippingPrice.visible()
            } else {
                itemShippingPrice.gone()
            }
            when {
                data.errorMessage.isNotEmpty() && data.errorId != ErrorProductData.ERROR_PINPOINT_NEEDED -> {
                    itemShippingDesc.visible()
                    itemShippingDesc.text = data.errorMessage
                }
                data.texts?.textsServiceDesc?.isNotEmpty() == true -> {
                    itemShippingDesc.visible()
                    itemShippingDesc.text = data.texts?.textsServiceDesc
                }
                isCourierInstantOrSameday(data.servicesId) -> {
                    itemShippingDesc.setText(com.tokopedia.logisticcart.R.string.label_shipping_information)
                    itemShippingDesc.visible()
                }
                else -> {
                    itemShippingDesc.gone()
                }
            }
            itemShippingRadio.isChecked = data.isSelected
            itemShippingRadio.skipAnimation()

            itemList.setOnClickListener {
                listener.onSelect(data.servicesId)
            }
        }

        private fun isCourierInstantOrSameday(shipperId: Int): Boolean {
            val ids = CourierConstant.INSTANT_SAMEDAY_DURATION
            for (id in ids) {
                if (shipperId == id) return true
            }
            return false
        }
    }
}