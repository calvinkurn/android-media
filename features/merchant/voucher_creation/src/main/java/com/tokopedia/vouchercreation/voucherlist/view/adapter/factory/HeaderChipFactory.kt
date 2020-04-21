package com.tokopedia.vouchercreation.voucherlist.view.adapter.factory

import com.tokopedia.vouchercreation.voucherlist.model.HeaderChipUiModel

/**
 * Created By @ilhamsuaib on 20/04/20
 */

interface HeaderChipFactory {

    fun type(model: HeaderChipUiModel): Int
}