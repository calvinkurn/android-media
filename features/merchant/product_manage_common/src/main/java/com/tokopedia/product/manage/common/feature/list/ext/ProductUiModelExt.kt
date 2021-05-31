package com.tokopedia.product.manage.common.feature.list.ext

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

internal fun ProductUiModel?.getId(): String {
    return this?.id.orEmpty()
}

internal fun ProductUiModel?.getName(): String {
    return this?.title.orEmpty()
}

internal fun ProductUiModel?.getStock(): Int {
    return this?.stock.orZero()
}

internal fun ProductUiModel?.getStatus(): ProductStatus {
    return this?.status ?: ProductStatus.INACTIVE
}

internal fun ProductUiModel?.isActive(): Boolean {
    return this?.status == ProductStatus.ACTIVE
}

internal fun ProductUiModel?.isCampaign(): Boolean {
    return this?.isCampaign == true
}

internal fun ProductUiModel?.hasEditStockAccess(): Boolean {
    return this?.access?.editStock == true
}

internal fun ProductUiModel?.hasEditProductAccess(): Boolean {
    return this?.access?.editProduct == true
}