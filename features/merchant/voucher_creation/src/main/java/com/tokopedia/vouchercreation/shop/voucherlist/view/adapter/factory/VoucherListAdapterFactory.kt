package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory

import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.*

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