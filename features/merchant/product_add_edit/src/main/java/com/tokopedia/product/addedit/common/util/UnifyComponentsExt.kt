package com.tokopedia.product.addedit.common.util

import com.tokopedia.kotlin.extensions.view.toFloatOrZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.unifycomponents.TextFieldUnify

fun TextFieldUnify?.getText(): String = this?.textFieldInput?.text.toString()

fun TextFieldUnify?.getTextIntOrZero(): Int = this?.textFieldInput?.text.toString().replace(".", "").toIntOrZero()

fun TextFieldUnify?.getTextFloatOrZero(): Float = this?.textFieldInput?.text.toString().replace(".", "").toFloatOrZero()

fun TextFieldUnify?.getTextLongOrZero(): Long = this?.textFieldInput?.text.toString().replace(".", "").toLongOrZero()