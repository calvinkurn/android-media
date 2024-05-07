package com.tokopedia.checkoutpayment.domain

import com.tokopedia.kotlin.extensions.view.toIntOrZero

data class CreditCardTenorListData(
    val processTime: String = "",
    val errorCode: String = "",
    val errorMsg: String = "",
    val tenorList: List<TenorListData> = emptyList()
)

data class TenorListData(
    val type: String = "",
    val bank: String = "",
    val desc: String = "",
    val amount: Double = 0.0,
    val fee: Double = 0.0,
    val rate: Double = 0.0,
    val disable: Boolean = false,
    val gatewayCode: String = ""
) {

    val tenure: Int
        get() {
            if (type != PAYMENT_CC_TYPE_TENOR_FULL) {
                return type.toIntOrZero()
            }
            return 0
        }

    companion object {
        const val PAYMENT_CC_TYPE_TENOR_FULL = "FULL"
    }
}
