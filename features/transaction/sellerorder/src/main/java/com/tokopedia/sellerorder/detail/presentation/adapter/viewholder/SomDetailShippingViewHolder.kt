package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailShipping
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import kotlinx.android.synthetic.main.detail_shipping_item.view.*

/**
 * Created by fwidjaja on 2019-10-04.
 */
class SomDetailShippingViewHolder(itemView: View) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {
    override fun bind(item: SomDetailData, position: Int) {
        if (item.dataObject is SomDetailShipping) {
            itemView.tv_shipping_name.text = item.dataObject.shippingName
            itemView.tv_shipping_price.text = item.dataObject.shippingPrice
            itemView.tv_receiver_name.text = item.dataObject.receiverName
            itemView.tv_receiver_phone.text = item.dataObject.receiverPhone
            itemView.tv_receiver_street.text = item.dataObject.receiverStreet
            itemView.tv_receiver_district.text = item.dataObject.receiverDistrict
            if (item.dataObject.shippingNotes.isNotEmpty()) {
                itemView.tv_receiver_notes.visibility = View.VISIBLE
                itemView.tv_notes_label.visibility = View.VISIBLE
                itemView.tv_receiver_notes.text = item.dataObject.shippingNotes
            } else {
                itemView.tv_receiver_notes.visibility = View.GONE
                itemView.tv_notes_label.visibility = View.GONE
            }
        }
    }
}