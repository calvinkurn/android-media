package com.tokopedia.catalog.ui.model


data class CatalogComparisonProductsUiModel(
    var catalogComparisonList: List<CatalogComparisonUIModel> = arrayListOf(),
) {
    data class CatalogComparisonUIModel(
        val catalogImage: String,
        val id: String?,
        val price: String,
        val name: String?,
        var isActive : Boolean? = true
    )
}
