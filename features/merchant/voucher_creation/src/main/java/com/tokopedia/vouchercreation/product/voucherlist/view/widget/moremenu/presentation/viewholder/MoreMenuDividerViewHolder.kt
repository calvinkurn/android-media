package com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel

class MoreMenuDividerViewHolder(itemView: View?) : AbstractViewHolder<MoreMenuUiModel.ItemDivider>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_bottomsheet_more_menu_divider
    }

    override fun bind(element: MoreMenuUiModel.ItemDivider) {}
}