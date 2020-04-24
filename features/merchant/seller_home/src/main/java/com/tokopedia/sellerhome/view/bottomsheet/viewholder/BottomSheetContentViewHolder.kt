package com.tokopedia.sellerhome.view.bottomsheet.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.view.bottomsheet.model.BottomSheetContentUiModel
import kotlinx.android.synthetic.main.sah_item_bottom_sheet_content.view.*

class BottomSheetContentViewHolder(view: View?) : AbstractViewHolder<BottomSheetContentUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.sah_item_bottom_sheet_content
    }

    override fun bind(element: BottomSheetContentUiModel) {
        with(element) {
            itemView.tv_bottom_sheet_content.text = content
        }
    }
}