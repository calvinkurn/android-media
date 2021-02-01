package com.tokopedia.sellerorder.list.presentation.adapter.viewholders

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.list.presentation.models.SomListBulkProcessOrderDescriptionUiModel
import com.tokopedia.unifyprinciples.Typography

class SomListBulkProcessOrderDescriptionViewHolder(
        itemView: View?
): AbstractViewHolder<SomListBulkProcessOrderDescriptionUiModel>(itemView) {
    companion object {
        val LAYOUT = R.layout.item_som_list_bulk_process_order_description
    }

    override fun bind(element: SomListBulkProcessOrderDescriptionUiModel?) {
        (itemView as? Typography)?.text = element?.text.orEmpty()
    }
}