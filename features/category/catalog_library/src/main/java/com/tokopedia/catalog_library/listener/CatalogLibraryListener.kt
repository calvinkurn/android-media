package com.tokopedia.catalog_library.listener

import com.tokopedia.catalog_library.model.raw.CatalogListResponse

interface CatalogLibraryListener {

    fun onLihatSemuaTextClick(applink: String) {}
    fun onProductCardClicked(applink: String?) {}
    fun onCategoryItemClicked(categoryIdentifier: String?) {}

    fun specialCategoryImpression(
        creativeSlot: Int,
        itemId: String,
        itemName: String,
        userId: String
    ) {
    }

    fun relevantCategoryImpression(
        creativeSlot: Int,
        itemId: String,
        itemName: String,
        userId: String
    ) {
    }

    fun catalogProductsHomePageImpression(
        catgoryName: String,
        product: CatalogListResponse.CatalogGetList.CatalogsProduct,
        position: Int,
        userId: String
    ) {
    }

    fun catalogProductsCategoryLandingImpression(
        catgoryName: String,
        product: CatalogListResponse.CatalogGetList.CatalogsProduct,
        position: Int,
        userId: String
    ) {
    }

    fun topFiveImpressionCategoryLandingImpression(
        categoryName: String,
        categoryId: String,
        catalogName: String,
        catalogId: String,
        position: Int,
        userId: String
    ) {
    }

    fun categoryListImpression(
        parentCategoryName: String,
        parentCategoryId: String,
        categoryName: String,
        categoryId: String,
        isGrid: Boolean,
        isAsc: Boolean,
        position: Int,
        userId: String
    ) {
    }
}
