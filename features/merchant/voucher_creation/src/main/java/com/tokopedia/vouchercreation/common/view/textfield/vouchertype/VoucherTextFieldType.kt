package com.tokopedia.vouchercreation.common.view.textfield.vouchertype

enum class VoucherTextFieldType(val maxLength: Int) {
    CURRENCY(EditTextMaxLength.CURRENCY),
    QUANTITY(EditTextMaxLength.QUANTITY),
    PERCENTAGE(EditTextMaxLength.PERCENTAGE)
}

object EditTextMaxLength {

    //Base max length of each text field if there are no backend definition/customization
    internal const val CURRENCY = 10
    internal const val QUANTITY = 3
    internal const val PERCENTAGE = 3
}