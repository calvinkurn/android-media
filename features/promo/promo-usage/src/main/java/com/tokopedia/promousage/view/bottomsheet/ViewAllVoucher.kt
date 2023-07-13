package com.tokopedia.promousage.view.bottomsheet

data class ViewAllVoucher(val collapsedVoucherCount: Int) : DelegateAdapterItem {
    override fun id() = collapsedVoucherCount
}
