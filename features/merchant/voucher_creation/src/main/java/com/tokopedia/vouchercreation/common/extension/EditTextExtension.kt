package com.tokopedia.vouchercreation.common.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


internal fun EditText.onTextChanged(onTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s.toString())
        }
        override fun afterTextChanged(editable: Editable?) {}
    })
}