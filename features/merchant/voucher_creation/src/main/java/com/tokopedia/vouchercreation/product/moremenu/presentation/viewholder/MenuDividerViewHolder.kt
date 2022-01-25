package com.tokopedia.vouchercreation.product.moremenu.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel

class MenuDividerViewHolder(itemView: View?) : AbstractViewHolder<MoreMenuUiModel.ItemDivider>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_bottomsheet_more_menu_divider
    }

    override fun bind(element: MoreMenuUiModel.ItemDivider) {}
}