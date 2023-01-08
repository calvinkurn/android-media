package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.digital_checkout.databinding.LayoutDigitalConsentWidgetBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import org.jetbrains.annotations.NotNull

/**
 * created by @bayazidnasir on 11/7/2022
 */

class DigitalConsentWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): BaseCustomView(context, attrs, defStyleAttr) {

    private companion object{

        @ColorRes
        val SPAN_COLOR = com.tokopedia.unifycomponents.R.color.Unify_GN500

        const val START_INDEX_LINK = -1
    }

    private val binding: LayoutDigitalConsentWidgetBinding = LayoutDigitalConsentWidgetBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    fun setOnTickCheckbox(listener: (Boolean) -> Unit){
        binding.cbConsentWidget.setOnCheckedChangeListener { _, isChecked ->
            listener.invoke(isChecked)
        }
    }

    fun setDescription(description: String){
        binding.tvContentConsentWidget.text = MethodChecker.fromHtml(description)
    }

    fun setOnClickUrl(vararg links: Pair<String, (View) -> Unit>){
        binding.tvContentConsentWidget.clickableLink(*links)
    }

    fun isChecked(): Boolean = binding.cbConsentWidget.isChecked

    private fun Typography.clickableLink(vararg links: Pair<String, (View) -> Unit>) {
        val spannableString = SpannableString(this.text)
        var startIndexOfLink = START_INDEX_LINK
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    ds.apply {
                        color = ResourcesCompat.getColor(resources, SPAN_COLOR, null)
                        isUnderlineText = false
                    }
                }

                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.invoke(view)
                }
            }
            startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)

            spannableString.apply {
                setSpan(
                    clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(
                    StyleSpan(Typeface.BOLD),
                    startIndexOfLink, startIndexOfLink + link.first.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        this.highlightColor = Color.TRANSPARENT
        this.movementMethod = LinkMovementMethod.getInstance()
        this.text = spannableString
    }
}