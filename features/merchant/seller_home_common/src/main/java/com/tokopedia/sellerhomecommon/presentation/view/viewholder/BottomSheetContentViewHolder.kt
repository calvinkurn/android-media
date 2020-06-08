package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.BottomSheetContentUiModel
import kotlinx.android.synthetic.main.shc_item_bottom_sheet_content.view.*

/**
 * Created By @ilhamsuaib on 27/05/20
 */

class BottomSheetContentViewHolder(view: View?) : AbstractViewHolder<BottomSheetContentUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_item_bottom_sheet_content
    }

    override fun bind(element: BottomSheetContentUiModel) {
        with(element) {
            itemView.tv_bottom_sheet_content.text = content
        }
    }
}