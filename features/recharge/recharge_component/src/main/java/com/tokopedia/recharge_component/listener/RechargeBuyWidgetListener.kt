package com.tokopedia.recharge_component.listener

import com.tokopedia.common.topupbills.data.product.CatalogProduct

interface RechargeBuyWidgetListener {
    fun onClickedButtonLanjutkan(product: CatalogProduct)
    fun onClickedChevron(product: CatalogProduct)
}