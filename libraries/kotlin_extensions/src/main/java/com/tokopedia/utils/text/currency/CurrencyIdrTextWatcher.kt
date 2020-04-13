package com.tokopedia.utils.text.currency

import android.widget.EditText

open class CurrencyIdrTextWatcher : NumberTextWatcher {

    constructor(editText: EditText) : super(editText) {}

    constructor(editText: EditText, defaultValue: String) : super(editText, defaultValue) {}
}