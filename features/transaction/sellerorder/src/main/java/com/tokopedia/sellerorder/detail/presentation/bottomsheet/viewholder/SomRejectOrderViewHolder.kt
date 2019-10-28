package com.tokopedia.sellerorder.detail.presentation.bottomsheet.viewholder

import android.view.View
import com.tokopedia.sellerorder.detail.data.model.SomRejectData
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBottomSheetRejectOrderAdapter

/**
 * Created by fwidjaja on 2019-10-24.
 */
/*
class SomRejectOrderViewHolder(itemView: View, private val hasRadioBtn: Boolean, private val hasReasonEditText: Boolean) : SomBottomSheetRejectOrderAdapter.BaseViewHolder<SomRejectData>(itemView) {
    override fun bind(item: SomRejectData, position: Int, listener: SomBottomSheetRejectOrderAdapter.ActionListener) {
        val arrayValues = item.keyMap.values.toMutableList()
        val arrayKeys = item.keyMap.keys.toMutableList()
        itemView.label_reject.text = arrayValues[position]

        if (!hasRadioBtn) {
            itemView.rb_reject.visibility = View.GONE
        } else {
            itemView.rb_reject.visibility = View.VISIBLE
        }

        if (!hasReasonEditText) {
            itemView.reject_reason.visibility = View.GONE
        } else {
            itemView.reject_reason.visibility = View.VISIBLE
        }

        itemView.setOnClickListener {
            listener.onBottomSheetItemClick(arrayKeys[position])
        }
    }
}*/
