package com.tokopedia.promousage.domain.entity

sealed class VoucherState {
    object Loading: VoucherState()
    object Normal : VoucherState()
    object Selected : VoucherState()
    object Disabled : VoucherState()
    data class Ineligible(val ineligibleReason: String) : VoucherState()
    data class Actionable(val actionableText: String, val appLink: String): VoucherState()
}
