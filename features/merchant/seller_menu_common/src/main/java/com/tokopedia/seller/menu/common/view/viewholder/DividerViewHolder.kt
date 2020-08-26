package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.view.uimodel.DividerUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType

class DividerViewHolder(itemView: View) : AbstractViewHolder<DividerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val THICK_LAYOUT = R.layout.setting_divider
        @LayoutRes
        val THIN_LAYOUT_FULL = R.layout.setting_divider_thin
        @LayoutRes
        val THIN_LAYOUT_PARTIAL = R.layout.setting_divider_partial_thin
        @LayoutRes
        val THIN_LAYOUT_INDENTED = R.layout.setting_divider_indented_thin

        fun getDividerView(type: DividerType) : Int =
                when(type) {
                    DividerType.THICK -> THICK_LAYOUT
                    DividerType.THIN_FULL -> THIN_LAYOUT_FULL
                    DividerType.THIN_PARTIAL -> THIN_LAYOUT_PARTIAL
                    DividerType.THIN_INDENTED -> THIN_LAYOUT_INDENTED
                }
    }

    override fun bind(element: DividerUiModel) {}
}