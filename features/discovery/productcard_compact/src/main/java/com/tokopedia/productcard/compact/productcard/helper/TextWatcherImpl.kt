package com.tokopedia.productcard.compact.productcard.helper

import android.text.Editable
import android.text.TextWatcher

internal class TextWatcherImpl(
    private val beforeTextChanged: (() -> Unit)? = null,
    private val onTextChanged: ((char: CharSequence?) -> Unit)? = null,
    private val afterTextChanged: ((editable: Editable?) -> Unit)? = null,
): TextWatcher {
    override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
        beforeTextChanged?.invoke()
    }

    override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
        onTextChanged?.invoke(char)
    }

    override fun afterTextChanged(editable: Editable?) {
        afterTextChanged?.invoke(editable)
    }
}
