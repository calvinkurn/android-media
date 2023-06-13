package com.tokopedia.mvc.presentation.bottomsheet.adapter.viewHolder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.mvc.R
import com.tokopedia.mvc.presentation.list.model.MoreMenuUiModel

/**
 * Created By @ilhamsuaib on 19/04/20
 */

class MenuDividerViewHolder(itemView: View?) : AbstractViewHolder<MoreMenuUiModel.ItemDivider>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.smvc_item_bottomsheet_menu_divider
    }

    override fun bind(element: MoreMenuUiModel.ItemDivider) {}
}
