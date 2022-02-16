package com.tokopedia.recharge_component.widget

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.toPx


private const val SORT_FILTER_PADDING_8 = 8
private const val SORT_FILTER_PADDING_16 = 16
private const val REGEX_IS_ALPHABET_AND_SPACE_ONLY = "^[a-zA-Z0-9\\s]*$"
private const val REGEX_IS_NUMERIC = "^[0-9\\s]*$"
private const val LABEL_MAX_CHAR = 18
private const val ELLIPSIZE = "..."
const val SORT_FILTER_LIMIT = 3
const val AUTOCOMPLETE_THRESHOLD = 1
const val AUTOCOMPLETE_DROPDOWN_VERTICAL_OFFSET = 10

fun TextFieldUnify2.clearErrorField() {
    with(this) {
        if (isInputError) {
            setMessage("")
            isInputError = false
        }
    }
}

fun TextFieldUnify2.onClickClearIconUnify(textFieldStaticLabel: String,
                                              onClickClearIcon: () -> Unit){
        editText.setText("")
        isInputError = false
        textInputLayout.hint = textFieldStaticLabel
        onClickClearIcon()
}

fun SortFilter.setMainPadding(){
    sortFilterHorizontalScrollView.setPadding(
        SORT_FILTER_PADDING_16.toPx(), 0 ,
        SORT_FILTER_PADDING_8.toPx() ,0)
    sortFilterHorizontalScrollView.clipToPadding = false
}

fun IconUnify.showClearIconUnify(){
    if (!isVisible) show()
}

fun IconUnify.hideClearIconUnify(){
    if (isVisible) hide()
}

fun String.isNumeric(): Boolean {
    return this.matches(REGEX_IS_NUMERIC.toRegex())
}

fun String.isAlphanumeric(): Boolean {
    return this.matches(REGEX_IS_ALPHABET_AND_SPACE_ONLY.toRegex())
}

fun validateContactName(textFieldStaticLabel: String, contactName: String): String {
    return if (contactName.isAlphanumeric() && contactName.isNotEmpty()) {
        if (contactName.length > LABEL_MAX_CHAR) {
            contactName.substring(0, LABEL_MAX_CHAR).plus(
                ELLIPSIZE
            )
        } else {
            contactName
        }
    } else {
        textFieldStaticLabel
    }
}

fun SortFilterItem.toggle() {
    type = if (type == ChipsUnify.TYPE_NORMAL) {
        ChipsUnify.TYPE_SELECTED
    } else {
        ChipsUnify.TYPE_NORMAL
    }
}
