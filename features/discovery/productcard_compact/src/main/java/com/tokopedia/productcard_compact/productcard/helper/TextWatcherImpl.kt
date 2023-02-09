package com.tokopedia.productcard_compact.productcard.helper

import android.text.Editable
import android.text.TextWatcher

internal class TextWatcherImpl(
    private val beforeTextChanged: (() -> Unit)? = null,
    private val onTextChanged: (() -> Unit)? = null,
    private val afterTextChanged: ((editable: Editable?) -> Unit)? = null,
): TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        beforeTextChanged?.invoke()
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        onTextChanged?.invoke()
    }

    override fun afterTextChanged(editable: Editable?) {
        afterTextChanged?.invoke(editable)
    }
}
