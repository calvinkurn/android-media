package com.tokopedia.mvc.presentation.list.adapter

import com.tokopedia.mvc.domain.entity.Voucher

interface VoucherAdapterListener {
    fun onVoucherListMoreMenuClicked(voucher: Voucher)
    fun onVoucherListCopyCodeClicked(voucher: Voucher)
    fun onVoucherListMultiPeriodClicked(voucher: Voucher)
    fun onVoucherListClicked(voucher: Voucher)
}
