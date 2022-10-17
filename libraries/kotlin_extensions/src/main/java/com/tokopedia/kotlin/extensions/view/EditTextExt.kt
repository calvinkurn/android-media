package com.tokopedia.kotlin.extensions.view

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

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