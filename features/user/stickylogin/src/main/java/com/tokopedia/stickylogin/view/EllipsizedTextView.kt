package com.tokopedia.stickylogin.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.stickylogin.R

class EllipsizedTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var content = ""
    private var highLight = getDefaultEllipsis().toString()
    private var highLightColor = getDefaultEllipsisColor()
    private lateinit var highLightSpannable: SpannableString

    init {
        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.EllipsizedTextView, 0, 0)
            typedArray?.let {
                highLight = typedArray.getString(R.styleable.EllipsizedTextView_highlight) ?: getDefaultEllipsis().toString()
                highLightColor = typedArray.getColor(R.styleable.EllipsizedTextView_highlight_color, getDefaultEllipsisColor())
                typedArray.recycle()
            }
        }
    }

    fun setContent(content: String) {
        setContent(content, "")
    }

    fun setContent(content: String, highLight: String) {
        this.content = content
        this.highLight = highLight
        highLightSpannable = SpannableString(highLight)
        highLightSpannable.setSpan(ForegroundColorSpan(highLightColor), 0, highLight.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        requestLayout()
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val ellipsizedText = TextUtils.ellipsize(content, paint, measuredWidth.toFloat(), ellipsize).toString()
        val ellipsizedFullText = TextUtils.ellipsize(content + highLight, paint, measuredWidth.toFloat(), ellipsize).toString()

        if (ellipsizedFullText.contains(getDefaultEllipsis())) {
            if (ellipsizedText.contains(getDefaultEllipsis())) {
                val start = ellipsizedText.indexOf(getDefaultEllipsis()) - highLightSpannable.length - 1
                text = SpannableStringBuilder()
                        .append(ellipsizedText)
                        .replace(start, start + 1, getDefaultEllipsis().toString())
                        .replace(start + 1, ellipsizedText.length, highLightSpannable)
            } else {
                val start = ellipsizedFullText.indexOf(getDefaultEllipsis()) - highLightSpannable.length - 1
                text = SpannableStringBuilder()
                        .append(ellipsizedFullText)
                        .replace(start, start + 1, getDefaultEllipsis().toString())
                        .replace(start + 1, ellipsizedFullText.length, highLightSpannable)
            }
        } else {
            text = SpannableStringBuilder().append(content).append(highLightSpannable)
        }
    }

    private fun getDefaultEllipsis(): Char {
        return Typography.ellipsis
    }

    private fun getDefaultEllipsisColor(): Int {
        return textColors.defaultColor
    }
}