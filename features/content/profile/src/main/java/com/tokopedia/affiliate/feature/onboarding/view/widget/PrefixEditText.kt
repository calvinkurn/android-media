package com.tokopedia.affiliate.feature.onboarding.view.widget

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.widget.TextView
import com.tokopedia.profile.R


/**
 * Created by Hendry on 3/9/2017.
 */

class PrefixEditText @JvmOverloads constructor(context: Context,
                                               attrs: AttributeSet? = null,
                                               defStyle: Int = 0)
    : AppCompatEditText(context, attrs, defStyle), TextWatcher {
    private var mPrefix: String? = null
    private var mColor: Int = 0

    val textWithoutPrefix: String
        get() {
            val s = super.getText()
            val prefix = mPrefix ?: ""
            return s.toString().replaceFirst(prefix.toRegex(), "").trim { it <= ' ' }
        }

    var prefix: String?
        get() = mPrefix
        set(prefix) {
            val previousText = textWithoutPrefix
            mPrefix = prefix
            setText(concat(prefix, previousText))
        }

    var prefixTextColor: Int
        get() = this.mColor
        set(color) {
            this.mColor = color
            text = text
        }

    init {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        var a = context.obtainStyledAttributes(
                attrs, R.styleable.PrefixEditText, defStyle, 0)

        mPrefix = a.getString(
                R.styleable.PrefixEditText_prefix)
        mColor = a.getColor(
                R.styleable.PrefixEditText_prefixTextColor,
                0)
        a.recycle()

        val set = intArrayOf(android.R.attr.text        // idx 0
        )
        a = context.obtainStyledAttributes(
                attrs, set)
        val text = a.getString(0)
        a.recycle()

        setText(concat(mPrefix, text))
        addTextChangedListener(this)
    }

    override fun setText(text: CharSequence, type: TextView.BufferType) {
        removeTextChangedListener(this)
        when {
            TextUtils.isEmpty(mPrefix) -> super.setText(text, type)
            TextUtils.isEmpty(text) -> {
                val prefixLength = mPrefix!!.length
                val spannable = SpannableString(mPrefix)
                if (mColor != 0) {
                    spannable.setSpan(ForegroundColorSpan(mColor),
                            0, mPrefix!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                super.setText(spannable, type)
                Selection.setSelection(spannable, prefixLength)
            }
            else -> {
                val textString = text.toString()
                val spannable: Spannable
                spannable = if (textString.startsWith(mPrefix!!)) {
                    SpannableString(textString)
                } else {
                    val combinedString = mPrefix!! + textString
                    SpannableString(combinedString)
                }
                if (mColor != 0) {
                    spannable.setSpan(ForegroundColorSpan(mColor),
                            0, mPrefix!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                super.setText(spannable, type)
                Selection.setSelection(super.getText(),
                        super.getText().length)
            }
        }
        addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        if (!s.toString().startsWith(mPrefix!!)) {
            removeTextChangedListener(this)
            super@PrefixEditText.setText(mPrefix)
            Selection.setSelection(super@PrefixEditText.getText(),
                    super@PrefixEditText.getText().length)
            addTextChangedListener(this)
        }
    }

    private fun concat(a: String?, b: String?): String {
        val nonNullA = a ?: ""
        val nonNullB = b ?: ""
        return nonNullA + nonNullB
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        // if select on the prefix text, move selection to the end
        val prefixLength = if (mPrefix == null) 0 else mPrefix!!.length
        if (selStart < prefixLength && selEnd == selStart) {
            Selection.setSelection(super@PrefixEditText.getText(),
                    super@PrefixEditText.getText().length)
        }
        super.onSelectionChanged(selStart, selEnd)
    }
}