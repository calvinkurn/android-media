package com.tokopedia.vouchercreation.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.detail.model.TipsUiModel
import kotlinx.android.synthetic.main.item_mvc_detail_tips.view.*

/**
 * Created By @ilhamsuaib on 04/05/20
 */

class TipsViewHolder(itemView: View?) : AbstractViewHolder<TipsUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_detail_tips
    }

    override fun bind(element: TipsUiModel) {
        with(itemView) {
            tvMvcTips.text = element.tips.parseAsHtml()
        }
    }
}