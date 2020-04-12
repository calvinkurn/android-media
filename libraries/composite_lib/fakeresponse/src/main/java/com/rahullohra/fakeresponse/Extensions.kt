package com.rahullohra.fakeresponse

import android.app.Activity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast

fun Activity.toast(message: String?) {
    if (!TextUtils.isEmpty(message))
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}

interface FakeResponseTextWatcher:TextWatcher{
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}