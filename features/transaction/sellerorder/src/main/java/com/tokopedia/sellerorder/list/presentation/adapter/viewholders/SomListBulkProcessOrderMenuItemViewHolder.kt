package com.tokopedia.sellerorder.list.presentation.adapter.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.list.presentation.bottomsheets.SomListBulkProcessOrderBottomSheet
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkProcessOrderMenuItemUiModel
import com.tokopedia.unifyprinciples.Typography

class SomListBulkProcessOrderMenuItemViewHolder(
        itemView: View?,
        private val listener: SomListBulkProcessOrderBottomSheet.SomListBulkProcessOrderBottomSheetListener?
): AbstractViewHolder<SomListBulkProcessOrderMenuItemUiModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_som_list_bulk_process_order_menu
    }

    override fun bind(element: SomListBulkProcessOrderMenuItemUiModel?) {
        (itemView as? Typography)?.run {
            text = element?.text.orEmpty()
            isEnabled = element?.enable ?: false
            setOnClickListener { listener?.onMenuItemClicked(element?.keyAction.orEmpty()) }
        }
    }
}