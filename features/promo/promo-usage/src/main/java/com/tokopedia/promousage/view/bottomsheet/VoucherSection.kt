package com.tokopedia.promousage.view.bottomsheet

data class VoucherSection(
    val title: String,
    val isExpanded: Boolean,
    val vouchers : List<DelegateAdapterItem>
)
