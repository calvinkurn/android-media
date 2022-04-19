package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.detail.model.FooterButtonUiModel
import kotlinx.android.synthetic.main.item_mvc_footer_button.view.*

/**
 * Created By @ilhamsuaib on 05/05/20
 */

class FooterButtonViewHolder(
        itemView: View?,
        private val onCtaClick: () -> Unit
) : AbstractViewHolder<FooterButtonUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_footer_button
    }

    override fun bind(element: FooterButtonUiModel) {
        with(itemView) {
            btnMvcFooterCta.run {
                text = element.ctaText
                isLoading = element.isLoading
                if (!isLoading) {
                    btnMvcFooterCta.setOnClickListener {
                        onCtaClick()
                        if (element.canLoad) {
                            element.isLoading = true
                            isLoading = true
                        }
                    }
                }
            }
        }
    }
}