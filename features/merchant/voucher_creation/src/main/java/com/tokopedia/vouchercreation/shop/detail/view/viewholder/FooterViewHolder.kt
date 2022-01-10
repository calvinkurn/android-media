package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.detail.model.FooterUiModel
import kotlinx.android.synthetic.main.item_mvc_footer.view.*

/**
 * Created By @ilhamsuaib on 06/05/20
 */

class FooterViewHolder(
        itemView: View?,
        private val onCtaClick: () -> Unit
) : AbstractViewHolder<FooterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_footer
    }

    override fun bind(element: FooterUiModel) {
        with(itemView) {
            val text = element.footerText.parseAsHtml()
            val greenColor = context.getResColor(com.tokopedia.unifyprinciples.R.color.light_G500)
            val spannableStringColor = ForegroundColorSpan(greenColor)
            val spannableString = SpannableString(text)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(v: View) {
                    onCtaClick()
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                }
            }
            val start = text.indexOf(string = element.clickableText, ignoreCase = true)
            val end = start.plus(element.clickableText.length)
            spannableString.setSpan(spannableStringColor, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            tvMvcFooter.text = spannableString
            tvMvcFooter.isClickable = true
            tvMvcFooter.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}