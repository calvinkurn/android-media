package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory

import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseFilterUiModel

/**
 * Created By @ilhamsuaib on 22/04/20
 */

interface FilterAdapterFactory {

    fun type(model: BaseFilterUiModel): Int
}