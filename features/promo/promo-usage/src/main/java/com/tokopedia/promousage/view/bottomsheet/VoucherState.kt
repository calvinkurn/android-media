package com.tokopedia.promousage.view.bottomsheet

sealed class VoucherState {
    object Loading: VoucherState()
    object Normal : VoucherState()
    object Selected : VoucherState()
    object Disabled : VoucherState()
    data class Ineligible(val ineligibleReason: String) : VoucherState()
}
