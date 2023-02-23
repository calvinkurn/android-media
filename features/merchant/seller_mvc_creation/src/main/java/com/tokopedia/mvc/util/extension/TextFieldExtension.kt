package com.tokopedia.mvc.util.extension

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.unifycomponents.TextFieldUnify2
import timber.log.Timber
import java.util.*

fun EditText.setToAllCapsMode() {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
            try {
                val formattedText =
                    charSequence.toString().uppercase(Locale.getDefault()).trim()
                val lengthDiff = formattedText.length - charSequence?.length.orZero()
                val cursorPosition = start + count + lengthDiff
                val fixedPosition = cursorPosition.coerceIn(Int.ZERO, formattedText.length)

                removeTextChangedListener(this)
                setText(formattedText)
                setSelection(fixedPosition)
                addTextChangedListener(this)
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    })
}

fun TextFieldUnify2.setMaxLength(maxLength : Int) {
    this.editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
}
