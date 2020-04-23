package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.BottomSheetMenuUiModel
import kotlinx.android.synthetic.main.item_mvc_bottomsheet_menu_divider.view.*

/**
 * Created By @ilhamsuaib on 19/04/20
 */

class MenuDividerViewHolder(itemView: View?) : AbstractViewHolder<BottomSheetMenuUiModel.ItemDivider>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_bottomsheet_menu_divider
    }

    override fun bind(element: BottomSheetMenuUiModel.ItemDivider) {
        with(itemView) {
            viewMvcHorizontalLine.isVisible = (element.type == BottomSheetMenuUiModel.ItemDivider.DIVIDER)

            if (element.type == BottomSheetMenuUiModel.ItemDivider.EMPTY_SPACE) {
                viewMvcItemDivider.layoutParams.height = context.dpToPx(12).toInt()
                viewMvcItemDivider.requestLayout()
            }
        }
    }
}