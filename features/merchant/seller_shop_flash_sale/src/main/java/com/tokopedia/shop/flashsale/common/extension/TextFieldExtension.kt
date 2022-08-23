package com.tokopedia.shop.flashsale.common.extension

import android.text.InputFilter
import com.tokopedia.unifycomponents.TextFieldUnify2

fun TextFieldUnify2.setMaxLength(maxLength : Int) {
    this.editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
}
