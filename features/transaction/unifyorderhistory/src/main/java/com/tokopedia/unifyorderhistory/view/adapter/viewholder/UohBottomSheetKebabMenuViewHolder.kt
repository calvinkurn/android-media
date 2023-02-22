package com.tokopedia.unifyorderhistory.view.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.databinding.BottomsheetKebabUohItemBinding
import com.tokopedia.unifyorderhistory.view.bottomsheet.UohKebabMenuBottomSheet

/**
 * Created by fwidjaja on 05/10/21.
 */
class UohBottomSheetKebabMenuViewHolder(
    private val binding: BottomsheetKebabUohItemBinding,
    private val actionListener: UohKebabMenuBottomSheet.UohKebabMenuBottomSheetListener?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(label: String, position: Int, orderData: UohListOrder.UohOrders.Order, orderIndex: Int) {
        binding.labelKebabOption.text = label
        binding.rlKebabItem.setOnClickListener {
            actionListener?.onKebabItemClick(position, orderData, orderIndex)
        }
    }
}
