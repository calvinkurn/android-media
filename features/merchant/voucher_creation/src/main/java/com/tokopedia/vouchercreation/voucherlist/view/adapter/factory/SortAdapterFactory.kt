package com.tokopedia.vouchercreation.voucherlist.view.adapter.factory

import com.tokopedia.vouchercreation.voucherlist.model.ui.SortUiModel

/**
 * Created By @ilhamsuaib on 22/04/20
 */

interface SortAdapterFactory {

    fun type(model: SortUiModel): Int
}