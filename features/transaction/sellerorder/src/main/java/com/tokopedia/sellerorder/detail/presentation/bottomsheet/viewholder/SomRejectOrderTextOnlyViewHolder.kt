package com.tokopedia.sellerorder.detail.presentation.bottomsheet.viewholder

import android.view.View
import com.tokopedia.sellerorder.detail.data.model.SomRejectData
import com.tokopedia.sellerorder.detail.presentation.bottomsheet.SomBottomSheetRejectOrderAdapter
import kotlinx.android.synthetic.main.bottomsheet_text_item.view.*

/**
 * Created by fwidjaja on 2019-10-24.
 */
class SomRejectOrderTextOnlyViewHolder(itemView: View) : SomBottomSheetRejectOrderAdapter.BaseViewHolder<SomRejectData>(itemView) {
    override fun bind(item: SomRejectData, position: Int, listener: SomBottomSheetRejectOrderAdapter.ActionListener) {
        val arrayValues = item.keyMap.values.toMutableList()
        val arrayKeys = item.keyMap.keys.toMutableList()
        itemView.label.text = arrayValues[position]
        itemView.setOnClickListener {
            listener.onBottomSheetItemClick(arrayKeys[position])
        }
    }
}