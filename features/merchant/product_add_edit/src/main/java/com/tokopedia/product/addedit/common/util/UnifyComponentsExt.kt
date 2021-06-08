package com.tokopedia.product.addedit.common.util

import android.text.*
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.unifycomponents.TextAreaUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.add_edit_product_insurance_input_layout.*
import java.math.BigInteger
import java.text.NumberFormat
import java.util.*

fun TextFieldUnify?.setText(text: String) = this?.textFieldInput?.setText(text)

fun TextAreaUnify?.setText(text: String) = this?.textAreaInput?.setText(text)

fun TextFieldUnify?.getText(): String = this?.textFieldInput?.text.toString()

fun TextFieldUnify?.getTextIntOrZero(): Int = this?.textFieldInput?.text.toString().replace(".", "").toIntOrZero()

fun TextFieldUnify?.getTextBigIntegerOrZero(): BigInteger = this?.textFieldInput?.text.toString().replace(".", "").toBigIntegerOrNull() ?: 0.toBigInteger()

fun TextFieldUnify?.setModeToNumberInput() {
    val textFieldInput = this?.textFieldInput
    val maxLength = Int.MAX_VALUE.toString().length
    val delimiterCount = maxLength / 3
    textFieldInput?.filters = arrayOf(InputFilter.LengthFilter(maxLength + delimiterCount - 2))
    textFieldInput?.addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            // clean any kind of number formatting here
            val productPriceInput = charSequence?.toString()?.replace(".", "")
            productPriceInput?.let {
                // format the number
                it.toLongOrNull()?.let { parsedLong ->
                    textFieldInput.removeTextChangedListener(this)
                    val formattedText: String = NumberFormat.getNumberInstance(Locale.US)
                            .format(parsedLong)
                            .toString()
                            .replace(",", ".")
                    val lengthDiff = formattedText.length - charSequence.length
                    val cursorPosition = start + count + lengthDiff
                    textFieldInput.setText(formattedText)
                    textFieldInput.setSelection(cursorPosition.coerceIn(0, formattedText.length))
                    textFieldInput.addTextChangedListener(this)
                }
            }
        }
    })
}

fun TextAreaUnify?.replaceTextAndRestoreCursorPosition(text: String) = this?.textAreaInput?.run {
    val cursorPosition = selectionEnd.orZero()
    setText(text)
    setSelection(cursorPosition.coerceAtMost(text.length))
}

fun Typography?.setTextOrGone(text: String) {
    this?.text = text
    this?.visibility = if (text.isNotEmpty()) View.VISIBLE else View.GONE
}

// Action listener for edittext inside the recyclerView
// https://stackoverflow.com/questions/13614101/fatal-crash-focus-search-returned-a-view-that-wasnt-able-to-take-focus/49433332
fun TextFieldUnify?.setRecyclerViewEditorActionListener() {
    this?.textFieldInput?.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            val view = textView.focusSearch(View.FOCUS_RIGHT)
            if (view != null) {
                if (!view.requestFocus(View.FOCUS_RIGHT)) {
                    return@OnEditorActionListener true
                }
            }
            return@OnEditorActionListener false
        }
        false
    })
}

fun RadioButtonUnify?.setTitle(title: String) {
    this?.context?.let { context ->
        val titleFontSize = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.fontSize_lvl3).toInt()
        val bodyFontSize = context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.fontSize_lvl2).toInt()
        val titleColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
        val bodyColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)

        val span1 = SpannableString(title)
        span1.setSpan(AbsoluteSizeSpan(titleFontSize), 0, title.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        span1.setSpan(ForegroundColorSpan(titleColor), 0, title.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        val span2 = SpannableString(text)
        span2.setSpan(AbsoluteSizeSpan(bodyFontSize), 0, text.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        span2.setSpan(ForegroundColorSpan(bodyColor), 0, text.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

        text = TextUtils.concat(span1, "\n", span2)
    }
}