package com.tokopedia.loginregister.tkpddesign

import android.text.Editable
import android.text.TextWatcher

@Deprecated("removed soon if unify component ready")
abstract class AfterTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    abstract override fun afterTextChanged(s: Editable)
}