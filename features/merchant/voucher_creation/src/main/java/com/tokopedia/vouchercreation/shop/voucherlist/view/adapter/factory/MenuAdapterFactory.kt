package com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.factory

import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.MoreMenuUiModel

/**
 * Created By @ilhamsuaib on 18/04/20
 */

interface MenuAdapterFactory {

    fun type(model: MoreMenuUiModel): Int
}