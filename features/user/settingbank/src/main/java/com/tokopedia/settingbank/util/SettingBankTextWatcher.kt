package com.tokopedia.settingbank.util

import android.text.Editable
import android.text.TextWatcher

class SettingBankTextWatcher(var textWatcherListener: TextWatcherListener?) : TextWatcher {


    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.let {
            textWatcherListener?.onTextChanged(s.toString())
        } ?: run {
            textWatcherListener?.onTextChanged("")
        }
    }
}

interface TextWatcherListener {
    fun onTextChanged(str: String)
}