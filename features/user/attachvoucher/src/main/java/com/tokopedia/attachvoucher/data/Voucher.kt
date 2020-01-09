package com.tokopedia.attachvoucher.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.attachvoucher.view.adapter.AttachVoucherTypeFactory

class Voucher: Visitable<AttachVoucherTypeFactory> {
    override fun type(typeFactory: AttachVoucherTypeFactory?): Int {
        return -1
    }
}