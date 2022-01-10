package com.tokopedia.vouchercreation.shop.voucherlist.model.ui

import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.VoucherListAdapterFactory

/**
 * Created By @ilhamsuaib on 24/04/20
 */

class ErrorStateUiModel : BaseVoucherListUiModel {

    companion object {
        const val DATA_KEY = "error_state"
    }

    override fun type(typeFactory: VoucherListAdapterFactory): Int {
        return typeFactory.type(this)
    }
}