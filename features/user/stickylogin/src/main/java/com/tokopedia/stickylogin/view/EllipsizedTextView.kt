package com.tokopedia.stickylogin.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.stickylogin.R
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

class EllipsizedTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), DarkModeListener {

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

    fun setContent(_content: String, _highLight: String) {
        if (context.isDarkMode()) {
            onDarkMode()
        } else {
            onLightMode()
        }

        content = _content
        highLight = _highLight
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
                if (start.isMoreThanZero()) {
                    text = SpannableStringBuilder()
                            .append(ellipsizedText)
                            .replace(start, start + 1, getDefaultEllipsis().toString())
                            .replace(start + 1, ellipsizedText.length, highLightSpannable)
                }
            } else {
                val start = ellipsizedFullText.indexOf(getDefaultEllipsis()) - highLightSpannable.length - 1
                if (start.isMoreThanZero()) {
                    text = SpannableStringBuilder()
                            .append(ellipsizedFullText)
                            .replace(start, start + 1, getDefaultEllipsis().toString())
                            .replace(start + 1, ellipsizedFullText.length, highLightSpannable)
                }
            }
        } else {
            if (content.isNotEmpty()) {
                text = SpannableStringBuilder().append(content).append(highLightSpannable)
            }
        }
    }

    private fun getDefaultEllipsis(): Char {
        return Typography.ellipsis
    }

    private fun getDefaultEllipsisColor(): Int {
        return textColors.defaultColor
    }

    override fun onDarkMode() {
        this.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N100))
        highLightColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
    }

    override fun onLightMode() {
        this.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N500))
        highLightColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
    }
}