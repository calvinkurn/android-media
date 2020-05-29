package com.tokopedia.home.beranda.presentation.view.adapter.datamodel

data class CashBackData(
    val amount: Int = 0,
    val amountText: String? = ""
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CashBackData) return false

        if (amount != other.amount) return false
        if (amountText != other.amountText) return false

        return true
    }

    override fun hashCode(): Int {
        var result = amount
        result = 31 * result + (amountText?.hashCode() ?: 0)
        return result
    }


}