package com.tokopedia.sellerhome.view.bottomsheet.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.bottomsheet.model.BottomSheetListItemUiModel
import kotlinx.android.synthetic.main.sah_item_bottom_sheet_list_item.view.*

class BottomSheetListItemViewHolder(view: View?) : AbstractViewHolder<BottomSheetListItemUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.sah_item_bottom_sheet_list_item
    }

    override fun bind(element: BottomSheetListItemUiModel) {
        with(element) {
            itemView.tv_title.text = title
            itemView.tv_description.text = description
        }
    }
}