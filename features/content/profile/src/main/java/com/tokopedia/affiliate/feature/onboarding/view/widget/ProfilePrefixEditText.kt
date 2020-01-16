package com.tokopedia.affiliate.feature.onboarding.view.widget

import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.widget.TextView
import com.tokopedia.profile.R

/**
 * Created by Hendry on 3/9/2017.
 */

class ProfilePrefixEditText @JvmOverloads constructor(context: Context,
                                                      attrs: AttributeSet? = null,
                                                      defStyle: Int = 0)
    : AppCompatEditText(context, attrs, defStyle), TextWatcher {
    private var mPrefix: String? = null
    private var mColor: Int = 0

    val textWithoutPrefix: String
        get() {
            val s = super.getText()
            val prefix = mPrefix.orEmpty()
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
                attrs, R.styleable.ProfilePrefixEditText, defStyle, 0)

        mPrefix = a.getString(R.styleable.ProfilePrefixEditText_prefix)
        mColor = a.getColor(R.styleable.ProfilePrefixEditText_prefixTextColor, 0)
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
        val prefix = mPrefix
        when {
            TextUtils.isEmpty(prefix) -> super.setText(text, type)
            TextUtils.isEmpty(text) && prefix != null -> {
                val prefixLength = prefix.length
                val spannable = SpannableString(prefix)
                if (mColor != 0) {
                    spannable.setSpan(ForegroundColorSpan(mColor),
                            0, prefix.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                super.setText(spannable, type)
                Selection.setSelection(spannable, prefixLength)
            }
            else -> {
                val textString = text.toString()
                val spannable: Spannable
                spannable = if (prefix != null && textString.startsWith(prefix)) {
                    SpannableString(textString)
                } else {
                    val combinedString = prefix.orEmpty() + textString
                    SpannableString(combinedString)
                }
                if (prefix != null && mColor != 0) {
                    spannable.setSpan(ForegroundColorSpan(mColor),
                            0, prefix.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                super.setText(spannable, type)
                super.getText()?.length?.let {
                    Selection.setSelection(super.getText(),
                            it)
                }
            }
        }
        addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        val prefix = mPrefix
        if (prefix != null && !s.toString().startsWith(prefix)) {
            removeTextChangedListener(this)
            super@ProfilePrefixEditText.setText(prefix)
            super@ProfilePrefixEditText.getText()?.length?.let {
                Selection.setSelection(super@ProfilePrefixEditText.getText(),
                        it)
            }
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
        val prefixLength = mPrefix?.length ?: 0
        if (selStart < prefixLength && selEnd == selStart) {
            super@ProfilePrefixEditText.getText()?.length?.let {
                Selection.setSelection(super@ProfilePrefixEditText.getText(),
                        it)
            }
        }
        super.onSelectionChanged(selStart, selEnd)
    }
}