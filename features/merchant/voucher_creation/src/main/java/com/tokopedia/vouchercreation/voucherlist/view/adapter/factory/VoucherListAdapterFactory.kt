package com.tokopedia.vouchercreation.voucherlist.view.adapter.factory

import com.tokopedia.vouchercreation.voucherlist.model.EmptyStateUiModel
import com.tokopedia.vouchercreation.voucherlist.model.ErrorStateUiModel
import com.tokopedia.vouchercreation.voucherlist.model.VoucherUiModel

/**
 * Created By @ilhamsuaib on 17/04/20
 */

interface VoucherListAdapterFactory {

    fun type(voucher: VoucherUiModel): Int

    fun type(emptyState: EmptyStateUiModel): Int

    fun type(errorStateUiModel: ErrorStateUiModel): Int
}