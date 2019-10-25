package com.tokopedia.sellerorder.detail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.detail.data.model.SomDetailData
import com.tokopedia.sellerorder.detail.data.model.SomDetailShipping
import com.tokopedia.sellerorder.detail.presentation.adapter.SomDetailAdapter
import kotlinx.android.synthetic.main.detail_shipping_item.view.*

/**
 * Created by fwidjaja on 2019-10-04.
 */
class SomDetailShippingViewHolder(itemView: View, private val actionListener: SomDetailAdapter.ActionListener) : SomDetailAdapter.BaseViewHolder<SomDetailData>(itemView) {

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
            if (item.dataObject.isFreeShipping) {
                itemView.label_harus_sesuai.visibility = View.VISIBLE
                itemView.ic_harus_sesuai.visibility = View.VISIBLE
                itemView.label_harus_sesuai.setOnClickListener {
                    println("++ HARUSNYA MUNCUL BOTTOMSHEET")
                    actionListener.onShowBottomSheetInfo(
                            itemView.context.getString(R.string.title_bottomsheet_immutable_courier),
                            R.string.desc_bottomsheet_immutable_courier)
                    }
                itemView.ic_harus_sesuai.setOnClickListener {
                    println("++ HARUSNYA MUNCUL BOTTOMSHEET")
                    actionListener.onShowBottomSheetInfo(
                            itemView.context.getString(R.string.title_bottomsheet_immutable_courier),
                            R.string.desc_bottomsheet_immutable_courier) }
            } else {
                itemView.label_harus_sesuai.visibility = View.GONE
                itemView.ic_harus_sesuai.visibility = View.GONE
            }
        }
    }
}