package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory

import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseHeaderChipUiModel

/**
 * Created By @ilhamsuaib on 20/04/20
 */

interface HeaderChipFactory {

    fun type(model: BaseHeaderChipUiModel): Int
}