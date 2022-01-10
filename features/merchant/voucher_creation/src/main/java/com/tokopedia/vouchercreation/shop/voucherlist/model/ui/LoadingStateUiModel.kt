package com.tokopedia.vouchercreation.shop.voucherlist.model.ui

import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.VoucherListAdapterFactory

/**
 * Created By @ilhamsuaib on 27/04/20
 */

data class LoadingStateUiModel(
        val isActiveVoucher: Boolean
) : BaseVoucherListUiModel {

    override fun type(typeFactory: VoucherListAdapterFactory): Int {
        return typeFactory.type(this)
    }
}