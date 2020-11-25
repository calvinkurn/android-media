package com.tokopedia.vouchercreation.voucherlist.view.adapter.factory

import com.tokopedia.vouchercreation.voucherlist.model.ui.ShareVoucherUiModel

/**
 * Created By @ilhamsuaib on 28/04/20
 */

interface ShareVoucherFactory {

    fun type(model: ShareVoucherUiModel): Int
}