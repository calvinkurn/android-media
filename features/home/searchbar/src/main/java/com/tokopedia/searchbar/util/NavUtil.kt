package com.tokopedia.searchbar.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler

object NavUtil {
    fun resetFocusEditText(activity: Activity, editText: EditText) {
        KeyboardHandler.DropKeyboard(activity, editText)
    }
}