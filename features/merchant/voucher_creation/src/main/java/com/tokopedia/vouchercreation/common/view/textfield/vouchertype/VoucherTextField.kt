package com.tokopedia.vouchercreation.common.view.textfield.vouchertype

interface VoucherTextField {
    val maxValue: Int
    val minValue: Int
    val minAlertRes: Int?
    val maxAlertRes: Int?
}