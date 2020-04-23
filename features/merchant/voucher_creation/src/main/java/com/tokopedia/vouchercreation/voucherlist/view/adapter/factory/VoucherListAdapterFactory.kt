package com.tokopedia.vouchercreation.voucherlist.view.adapter.factory

import com.tokopedia.vouchercreation.voucherlist.model.VoucherUiModel

/**
 * Created By @ilhamsuaib on 17/04/20
 */

interface VoucherListAdapterFactory {

    fun type(voucher: VoucherUiModel): Int
}