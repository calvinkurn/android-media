package com.tokopedia.managepassword.common.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * @author rival
 * @created 20/02/2020
 */

fun EditText.setAfterTextChanged(listener: (char: CharSequence?) -> Unit) {
    this.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    listener(s)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            }
    )
}