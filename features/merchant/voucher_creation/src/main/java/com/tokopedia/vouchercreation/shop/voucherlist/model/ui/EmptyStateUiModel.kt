package com.tokopedia.vouchercreation.shop.voucherlist.model.ui

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.VoucherListAdapterFactory

/**
 * Created By @ilhamsuaib on 24/04/20
 */

data class EmptyStateUiModel(
        val isEligible: Boolean,
        val isActiveVoucher: Boolean,
        val onSeeHistoryClicked: () -> Unit = {},
        val onCreateVoucherClicked: () -> Unit = {},
        val impressHolder: ImpressHolder = ImpressHolder()
) : BaseVoucherListUiModel {

    companion object {
        const val DATA_KEY = "empty_state"
    }

    override fun type(typeFactory: VoucherListAdapterFactory): Int {
        return typeFactory.type(this)
    }
}