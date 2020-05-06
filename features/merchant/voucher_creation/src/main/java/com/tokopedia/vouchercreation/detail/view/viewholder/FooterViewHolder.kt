package com.tokopedia.vouchercreation.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.detail.model.FooterUiModel
import kotlinx.android.synthetic.main.item_mvc_footer.view.*

/**
 * Created By @ilhamsuaib on 06/05/20
 */

class FooterViewHolder(itemView: View?) : AbstractViewHolder<FooterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_footer
    }

    override fun bind(element: FooterUiModel) {
        with(itemView) {
            tvMvcFooter.text = element.footerText.parseAsHtml()
        }
    }
}