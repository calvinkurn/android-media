package com.tokopedia.settingbank.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText


fun EditText.textChangedListener(onTextChangeExt: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)?,
                                 afterTextChangedExt: ((s: Editable?) -> Unit)? = null,
                                 beforeTextChangedExt: ((s: CharSequence?,
                                                         start: Int, count: Int, after: Int) -> Unit)? = null) {
    addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    afterTextChangedExt?.invoke(s)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    beforeTextChangedExt?.invoke(s, start, count, after)

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    onTextChangeExt?.invoke(s, start, before, count)
                }
            }
    )
}
