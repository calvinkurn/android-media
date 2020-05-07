package com.tokopedia.vouchercreation.detail.view.viewholder

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
import com.tokopedia.vouchercreation.detail.model.FooterUiModel
import kotlinx.android.synthetic.main.item_mvc_footer.view.*

/**
 * Created By @ilhamsuaib on 06/05/20
 */

class FooterViewHolder(
        itemView: View?,
        private val onCtaClick: (String, String) -> Unit
) : AbstractViewHolder<FooterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_footer
    }

    override fun bind(element: FooterUiModel) {
        with(itemView) {
            val text = element.footerText.parseAsHtml()
            val greenColor = context.getResColor(R.color.light_G500)
            val spannableStringColor = ForegroundColorSpan(greenColor)
            val spannableString = SpannableString(text)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(v: View) {
                    println("click xx")
                    onCtaClick(element.bottomSheetTitle, element.bottomSheetContent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                }
            }
            val clickableTextLength = 6
            val end = text.length
            val start = end.minus(clickableTextLength)
            spannableString.setSpan(spannableStringColor, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            tvMvcFooter.text = spannableString
            tvMvcFooter.isClickable = true
            tvMvcFooter.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}