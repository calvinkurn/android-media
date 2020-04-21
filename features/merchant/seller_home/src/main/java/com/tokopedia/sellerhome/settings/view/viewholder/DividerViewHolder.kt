package com.tokopedia.sellerhome.settings.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.DividerUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.DividerType
import kotlinx.android.synthetic.main.setting_divider.view.*

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

    override fun bind(element: DividerUiModel) {

    }

}