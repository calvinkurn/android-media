package com.tokopedia.vouchercreation.voucherlist.model

import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.VoucherListAdapterFactory

/**
 * Created By @ilhamsuaib on 17/04/20
 */

data class VoucherUiModel(
        val name: String,
        val description: String,
        val isOngoingStatus: Boolean
) : BaseVoucherListUiModel {

    override fun type(typeFactory: VoucherListAdapterFactory): Int {
        return typeFactory.type(this)
    }
}