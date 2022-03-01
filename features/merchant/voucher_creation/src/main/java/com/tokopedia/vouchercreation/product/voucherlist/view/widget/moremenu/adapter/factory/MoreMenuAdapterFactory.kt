package com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.adapter.factory

import com.tokopedia.vouchercreation.product.voucherlist.view.widget.moremenu.data.uimodel.MoreMenuUiModel

interface MoreMenuAdapterFactory {
    fun type(model: MoreMenuUiModel): Int
}