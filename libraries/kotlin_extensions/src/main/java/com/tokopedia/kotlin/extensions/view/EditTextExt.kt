package com.tokopedia.kotlin.extensions.view

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber

/**
 * @author by milhamj on 30/11/18.
 */

private const val DEFAULT_REPLAY_COUNT = 1

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged(editable?.toString() ?: "")
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}

fun EditText.textChangesAsFlow(): Flow<String> {
    val flow = MutableSharedFlow<String>(replay = DEFAULT_REPLAY_COUNT)

    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s == null) return
            flow.tryEmit(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
        }
    })

    return flow
}

fun EditText.setModeToNumberDelimitedInput(maxLength: Int = Int.MAX_VALUE.getNumberFormatted().length) {
    filters = arrayOf(InputFilter.LengthFilter(maxLength))
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            try {
                val cleanedText = charSequence?.toString()?.filter { it.isDigit() }.orEmpty()
                if (cleanedText.isNotEmpty()) {
                    val parsedLong = cleanedText.toLongOrZero()
                    val formattedText = parsedLong.getNumberFormatted()
                    val lengthDiff = formattedText.length - charSequence?.length.orZero()
                    val cursorPosition = start + count + lengthDiff
                    val fixedPosition = cursorPosition.coerceIn(Int.ZERO, formattedText.length)

                    removeTextChangedListener(this)
                    setText(formattedText)
                    setSelection(fixedPosition)
                    addTextChangedListener(this)
                }
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    })
}