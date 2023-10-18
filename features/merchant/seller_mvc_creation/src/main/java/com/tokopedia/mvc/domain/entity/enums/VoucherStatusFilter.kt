package com.tokopedia.mvc.domain.entity.enums

import androidx.annotation.StringRes
import com.tokopedia.mvc.R

enum class VoucherStatusFilter(val types: List<VoucherStatus>, @StringRes val captionRes: Int) {
    ALL_STATUS(
        listOf(VoucherStatus.NOT_STARTED, VoucherStatus.ONGOING, VoucherStatus.ENDED, VoucherStatus.STOPPED),
        R.string.smvc_bottomsheet_filter_voucher_all),
    NOT_STARTED(
        listOf(VoucherStatus.NOT_STARTED),
        R.string.smvc_bottomsheet_filter_voucher_notstarted),
    ONGOING(
        listOf(VoucherStatus.ONGOING),
        R.string.smvc_bottomsheet_filter_voucher_ongoing),
    FINISHED(
        listOf(VoucherStatus.ENDED, VoucherStatus.STOPPED),
        R.string.smvc_bottomsheet_filter_voucher_finished)
}
