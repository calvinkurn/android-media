package com.tokopedia.oneclickcheckout.order.view.model

/**
 * Created by fwidjaja on 06/08/21.
 */
data class CreditCardTenorListData (
    val processTime: String = "",
    val errorCode: String = "",
    val errorMsg : String = "",
    val tenorList: List<TenorListData> = emptyList()
)

data class TenorListData(
    val type: String = "",
    val bank: String = "",
    val desc: String = "",
    val amount: Float = 0F,
    val fee: Float = 0F,
    val rate: Float = 0F,
    val disable: Boolean = false
)