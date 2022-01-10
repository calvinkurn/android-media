package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.detail.model.DividerUiModel
import kotlinx.android.synthetic.main.item_mvc_divider.view.*

/**
 * Created By @ilhamsuaib on 04/05/20
 */

class DividerViewHolder(itemView: View?) : AbstractViewHolder<DividerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_divider
    }

    override fun bind(element: DividerUiModel) {
        with(itemView) {
            viewMvcDivider.layoutParams.height = context.dpToPx(element.dividerHeight).toInt()
            viewMvcDivider.requestLayout()
        }
    }
}