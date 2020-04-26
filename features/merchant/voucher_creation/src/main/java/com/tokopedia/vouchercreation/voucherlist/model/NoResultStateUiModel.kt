package com.tokopedia.vouchercreation.voucherlist.model

import com.tokopedia.vouchercreation.voucherlist.view.adapter.factory.VoucherListAdapterFactory

/**
 * Created By @ilhamsuaib on 26/04/20
 */

object NoResultStateUiModel : BaseVoucherListUiModel {

    override fun type(typeFactory: VoucherListAdapterFactory): Int {
        return typeFactory.type(this)
    }
}