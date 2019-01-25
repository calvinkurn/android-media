package com.tokopedia.kotlin.extensions.view

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * @author by milhamj on 30/11/18.
 */

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged(editable?.toString() ?: "")
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    })
}