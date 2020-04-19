package com.tokopedia.vouchercreation.voucherlist.view.adapter.factory

import com.tokopedia.vouchercreation.voucherlist.model.BottomSheetMenuUiModel

/**
 * Created By @ilhamsuaib on 18/04/20
 */

interface MenuAdapterFactory {

    fun type(model: BottomSheetMenuUiModel): Int
}