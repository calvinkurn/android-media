package com.tokopedia.promousage.domain.entity.list

import com.tokopedia.promousage.util.composite.DelegateAdapterItem

data class ViewAllVoucher(val collapsedVoucherCount: Int) : DelegateAdapterItem {
    override fun id() = collapsedVoucherCount
}
