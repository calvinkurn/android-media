package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.BottomSheetListItemUiModel
import kotlinx.android.synthetic.main.shc_item_bottom_sheet_list_item.view.*

/**
 * Created By @ilhamsuaib on 27/05/20
 */

class BottomSheetListItemViewHolder(view: View?) : AbstractViewHolder<BottomSheetListItemUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_item_bottom_sheet_list_item
    }

    override fun bind(element: BottomSheetListItemUiModel) {
        with(element) {
            itemView.tv_title.text = title
            itemView.tv_description.text = description
        }
    }
}