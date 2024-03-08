package com.tokopedia.catalog.ui.mapper

import com.tokopedia.catalog.ui.model.CatalogComparisonProductsUiModel
import com.tokopedia.catalogcommon.uimodel.ComparisonUiModel
import com.tokopedia.oldcatalog.model.raw.CatalogComparisonProductsResponse

fun CatalogComparisonProductsResponse?.map() = CatalogComparisonProductsUiModel(
    catalogComparisonList = this?.catalogComparisonList?.catalogComparisonList?.map {
        CatalogComparisonProductsUiModel.CatalogComparisonUIModel(
            id = it.id.orEmpty(),
            price = it.marketPrice?.firstOrNull()?.minFmt + " - " +  it.marketPrice?.firstOrNull()?.maxFmt,
            name = it.name.orEmpty(),
            catalogImage = it.catalogImage?.firstOrNull()?.imageUrl.orEmpty()
        )
    }.orEmpty()
)


fun ComparisonUiModel.map() = CatalogComparisonProductsUiModel(
    catalogComparisonList = this.content.map { comparisonContent ->
        CatalogComparisonProductsUiModel.CatalogComparisonUIModel(
            id = comparisonContent.id.orEmpty(),
            name = comparisonContent.productTitle,
            catalogImage = comparisonContent.imageUrl,
            price= comparisonContent.price
        )
    }
)
