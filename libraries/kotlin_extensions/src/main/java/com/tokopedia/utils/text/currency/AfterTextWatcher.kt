package com.tokopedia.utils.text.currency

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by User on 10/23/2017.
 */

abstract class AfterTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    abstract override fun afterTextChanged(s: Editable)
}