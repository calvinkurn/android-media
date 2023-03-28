package com.tokopedia.catalog_library.util

import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDM
import com.tokopedia.catalog_library.model.datamodel.CatalogShimmerDM
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_SHIMMER_LIHAT_SEMUA_HORIZONTAL
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_SHIMMER_PRODUCTS
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_SHIMMER_TOP_FIVE
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_SHIMMER_VIRAL

class CatalogLibraryUiUpdater(var mapOfData: MutableMap<String, BaseCatalogLibraryDM>) {

    fun shimmerForLandingPage() {
        updateModel(
            CatalogShimmerDM(
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE,
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE,
                CATALOG_SHIMMER_TOP_FIVE
            )
        )
        updateModel(
            CatalogShimmerDM(
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL,
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL,
                CATALOG_SHIMMER_VIRAL
            )
        )
        updateModel(
            CatalogShimmerDM(
                CatalogLibraryConstant.CATALOG_PRODUCT,
                CatalogLibraryConstant.CATALOG_PRODUCT,
                CATALOG_SHIMMER_PRODUCTS
            )
        )
    }

    fun shimmerForHomePage() {
        updateModel(
            CatalogShimmerDM(
                CatalogLibraryConstant.CATALOG_CONTAINER_SPECIAL,
                CatalogLibraryConstant.CATALOG_CONTAINER_SPECIAL,
                CATALOG_SHIMMER_TOP_FIVE
            )
        )
        updateModel(
            CatalogShimmerDM(
                CatalogLibraryConstant.CATALOG_CONTAINER_RELEVANT,
                CatalogLibraryConstant.CATALOG_CONTAINER_RELEVANT,
                CATALOG_SHIMMER_TOP_FIVE
            )
        )
        updateModel(
            CatalogShimmerDM(
                CatalogLibraryConstant.CATALOG_CONTAINER_POPULAR_BRANDS,
                CatalogLibraryConstant.CATALOG_CONTAINER_POPULAR_BRANDS,
                CATALOG_SHIMMER_TOP_FIVE
            )
        )
        updateModel(
            CatalogShimmerDM(
                CatalogLibraryConstant.CATALOG_PRODUCT,
                CatalogLibraryConstant.CATALOG_PRODUCT,
                CATALOG_SHIMMER_PRODUCTS
            )
        )
    }

    fun shimmerForPopularBrands() {
        updateModel(
            CatalogShimmerDM(
                CatalogLibraryConstant.CATALOG_CONTAINER_POPULAR_BRANDS_WITH_CATALOGS,
                CatalogLibraryConstant.CATALOG_CONTAINER_POPULAR_BRANDS_WITH_CATALOGS,
                CATALOG_SHIMMER_PRODUCTS
            )
        )
    }

    fun shimmerForBrandLandingPage() {
        updateModel(
            CatalogShimmerDM(
                CatalogLibraryConstant.CATALOG_CONTAINER_CATEGORY_HEADER,
                CatalogLibraryConstant.CATALOG_CONTAINER_CATEGORY_HEADER,
                CATALOG_SHIMMER_LIHAT_SEMUA_HORIZONTAL
            )
        )
        updateModel(
            CatalogShimmerDM(
                CatalogLibraryConstant.CATALOG_PRODUCT,
                CatalogLibraryConstant.CATALOG_PRODUCT,
                CATALOG_SHIMMER_PRODUCTS
            )
        )
    }

    fun updateModel(model: BaseCatalogLibraryDM) {
        updateData(model.type(), model)
    }

    fun removeModel(type: String) {
        mapOfData.remove(type)
    }

    private fun updateData(key: String, baseCatalogHomeDataModel: BaseCatalogLibraryDM) {
        mapOfData[key] = baseCatalogHomeDataModel
    }

    fun clearAll(){
        mapOfData.clear()
    }

    fun hasData(): Boolean {
        return mapOfData.size > 5
    }
}
