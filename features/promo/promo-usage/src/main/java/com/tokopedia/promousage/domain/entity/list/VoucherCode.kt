package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class VoucherCode(
    val userInputVoucherCode: String,
    val errorMessage: String,
    val voucher: Voucher? = null
) : DelegateAdapterItem {
    override fun id() = errorMessage
}
