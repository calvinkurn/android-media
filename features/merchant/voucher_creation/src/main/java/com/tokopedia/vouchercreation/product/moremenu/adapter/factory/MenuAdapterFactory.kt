package com.tokopedia.vouchercreation.product.moremenu.adapter.factory

import com.tokopedia.vouchercreation.product.moremenu.data.model.MoreMenuUiModel

interface MenuAdapterFactory {
    fun type(model: MoreMenuUiModel): Int
}