package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.MoreMenuUiModel

/**
 * Created By @ilhamsuaib on 19/04/20
 */

class MenuDividerViewHolder(itemView: View?) : AbstractViewHolder<MoreMenuUiModel.ItemDivider>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_bottomsheet_menu_divider
    }

    override fun bind(element: MoreMenuUiModel.ItemDivider) {}
}