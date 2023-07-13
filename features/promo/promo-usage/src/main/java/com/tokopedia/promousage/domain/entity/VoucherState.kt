package com.tokopedia.promousage.domain.entity

sealed class VoucherState {
    object Loading: VoucherState()
    object Normal : VoucherState()
    object Selected : VoucherState()
    object Disabled : VoucherState()
    data class Ineligible(val ineligibleReason: String) : VoucherState()
}
