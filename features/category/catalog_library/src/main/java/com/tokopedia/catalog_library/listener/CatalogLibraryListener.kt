package com.tokopedia.catalog_library.listener

import com.tokopedia.catalog_library.model.datamodel.CatalogLihatDM
import com.tokopedia.catalog_library.model.raw.CatalogBrandsPopularResponse
import com.tokopedia.catalog_library.model.raw.CatalogListResponse

interface CatalogLibraryListener {

    fun onLihatSemuaTextClick(applink: String) {}
    fun onProductCardClicked(applink: String?) {}
    fun onCategoryItemClicked(categoryIdentifier: String?) {}
    fun onCategoryItemClicked(categoryIdentifier: String?, categoryName: String = "") {}
    fun onPopularBrandsHomeClick(brandName: String, brandId: String, position: String) {}
    fun onPopularBrandsLihatSemuaClick(
        brandName: String,
        brandId: String,
        position: String,
        eventAction: String,
        trackerId: String
    ) {}
    fun onPopularBrandsClick(
        brandName: String,
        brandId: String,
        position: String,
        catalogName: String,
        catalogId: String,
        applink: String
    ) {}
    fun onBrandCategoryTabSelected(categoryName: String, categoryId: String, position: Int) {}
    fun onChangeCategory(categoryName: String, categoryId: String, isTabSelected: Boolean = false) {}
    fun onBrandCategoryArrowClick() {}

    fun categoryHorizontalCarouselImpression(
        creativeName: String,
        creativeSlot: Int,
        itemId: String,
        itemName: String,
        userId: String,
        trackerId: String,
        eventAction: String
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
        trackerId: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
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

    fun onAccordionStateChange(expanded: Boolean, element: CatalogLihatDM) {}

    fun onImpressedPopularPageItems(it: CatalogBrandsPopularResponse.CatalogGetBrandPopular.Brands, position: Int) {}
}
