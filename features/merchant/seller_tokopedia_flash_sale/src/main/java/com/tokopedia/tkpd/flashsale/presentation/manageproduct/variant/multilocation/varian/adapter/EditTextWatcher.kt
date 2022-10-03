package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.adapter

import android.text.Editable
import android.text.TextWatcher

class EditTextWatcher(
    val action: (String) -> Unit
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        action(s?.toString() ?: "")
    }

}