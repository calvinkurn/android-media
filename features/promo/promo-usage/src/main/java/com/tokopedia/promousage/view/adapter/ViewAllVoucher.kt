package com.tokopedia.promousage.view.adapter

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class ViewAllVoucher(val collapsedVoucherCount: Int) : DelegateAdapterItem {
    override fun id() = collapsedVoucherCount
}
