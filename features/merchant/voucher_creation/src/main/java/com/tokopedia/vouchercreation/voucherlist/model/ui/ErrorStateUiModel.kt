package com.tokopedia.vouchercreation.voucherlist.model.ui

import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.VoucherListAdapterFactory

/**
 * Created By @ilhamsuaib on 24/04/20
 */

object ErrorStateUiModel : BaseVoucherListUiModel {

    override fun type(typeFactory: VoucherListAdapterFactory): Int {
        return typeFactory.type(this)
    }
}