package com.tokopedia.profilecompletion.common.ext

import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.tokopedia.pin.PinUnify

/**
 * Created by Yoris Prayogo on 01/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

fun PinUnify.focus() {
    requestFocus()
    // Show keyboard
    val inputMethodManager = context
	.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, 0)
}
