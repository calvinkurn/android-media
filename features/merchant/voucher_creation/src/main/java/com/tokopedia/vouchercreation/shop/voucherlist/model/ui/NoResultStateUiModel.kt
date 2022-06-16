package com.tokopedia.vouchercreation.shop.voucherlist.model.ui

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory.VoucherListAdapterFactory

/**
 * Created By @ilhamsuaib on 26/04/20
 */

class NoResultStateUiModel(
        val impressHolder: ImpressHolder = ImpressHolder()
) : BaseVoucherListUiModel {

    companion object {
        const val DATA_KEY = "no_result"
    }

    override fun type(typeFactory: VoucherListAdapterFactory): Int {
        return typeFactory.type(this)
    }
}