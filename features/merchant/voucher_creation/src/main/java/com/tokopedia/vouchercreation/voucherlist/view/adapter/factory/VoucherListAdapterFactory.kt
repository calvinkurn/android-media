package com.tokopedia.vouchercreation.voucherlist.view.adapter.factory

import com.tokopedia.vouchercreation.voucherlist.model.ui.*

/**
 * Created By @ilhamsuaib on 17/04/20
 */

interface VoucherListAdapterFactory {

    fun type(voucher: VoucherUiModel): Int

    fun type(emptyState: EmptyStateUiModel): Int

    fun type(errorStateUiModel: ErrorStateUiModel): Int

    fun type(noResultStateUiModel: NoResultStateUiModel): Int

    fun type(model: LoadingStateUiModel): Int
}