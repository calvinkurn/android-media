package com.tokopedia.campaign.utils.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

inline fun EditText.doOnTextChanged(crossinline onTextChangeEvent: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChangeEvent(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {

        }
    })
}


fun EditText.textChangesAsFlow(): Flow<String> {
    return Channel<String>(capacity = Channel.UNLIMITED).also { channel ->
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null) return
                channel.trySend(this@textChangesAsFlow.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }.receiveAsFlow()
}