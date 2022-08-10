package com.tokopedia.campaign.utils.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

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
