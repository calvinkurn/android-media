package com.tokopedia.recharge_component.widget

import android.content.Context
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.recharge_component.databinding.WidgetRechargeTickerBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class RechargeTickerWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rechargeTickerViewBinding = WidgetRechargeTickerBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        initView()
    }

    private fun initView() {
       rechargeTickerViewBinding.tickerWidgetIcon.setImageUrl(TOKOPEDIA_ICON_URL)
    }

    fun setText(text: String) {
        rechargeTickerViewBinding.tickerWidgetText.text = MethodChecker.fromHtml(text)
    }

    fun setLinks(comparableText: String, listener: View.OnClickListener){
        rechargeTickerViewBinding.tickerWidgetText.makeLinks(
            Pair(comparableText,
            listener)
        )
    }

    fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }
            val startIndexOfLink = this.text.toString().indexOf(link.first)
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        }
        this.movementMethod = LinkMovementMethod.getInstance()
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    companion object {
        private const val TOKOPEDIA_ICON_URL = "https://images.tokopedia.net/img/android/recharge/recharge_component/recharge_component_icon-tokopedia.webp"
    }
}