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
        val LAYOUT = R.layout.setting_divider
    }

    override fun bind(element: DividerUiModel) {
        with(itemView) {
            when(element.dividerType) {
                DividerType.THICK -> {
                    thickDivider.visibility = View.VISIBLE
                    thinDivider.visibility = View.GONE
                }
                DividerType.THIN -> {
                    thickDivider.visibility = View.GONE
                    thinDivider.visibility = View.VISIBLE
                }
            }
        }
    }

}