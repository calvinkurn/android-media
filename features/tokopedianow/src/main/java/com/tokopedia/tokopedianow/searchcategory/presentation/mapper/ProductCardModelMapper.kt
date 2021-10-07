package com.tokopedia.tokopedianow.searchcategory.presentation.mapper

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.searchcategory.presentation.model.LabelGroupDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.LabelGroupVariantDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.NonVariantATCDataView
import com.tokopedia.tokopedianow.searchcategory.presentation.model.VariantATCDataView

internal fun List<LabelGroupDataView>.mapToLabelGroup() = map {
    ProductCardModel.LabelGroup(
        position = it.position,
        title = it.title,
        type = it.type,
        imageUrl = it.url,
    )
}

internal fun List<LabelGroupVariantDataView>.mapToLabelGroupVariant() = map {
    ProductCardModel.LabelGroupVariant(
        type = it.type,
        typeVariant = it.typeVariant,
        title = it.title,
        hexColor = it.hexColor,
    )
}

internal fun VariantATCDataView.mapToVariant(): ProductCardModel.Variant {
    return ProductCardModel.Variant(
        quantity = this.quantity
    )
}

internal fun NonVariantATCDataView.mapToNonVariant(): ProductCardModel.NonVariant {
    return ProductCardModel.NonVariant(
        quantity = this.quantity,
        maxQuantity = this.maxQuantity,
        minQuantity = this.minQuantity,
    )
}