package com.tokopedia.catalog_library.model.util

import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogShimmerDataModel
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.CATALOG_SHIMMER_PRODUCTS
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant.CATALOG_SHIMMER_TOP_FIVE

class CatalogLibraryUiUpdater(var mapOfData: MutableMap<String, BaseCatalogLibraryDataModel>) {

    fun setUpForLandingPage() {
        updateModel(
            CatalogShimmerDataModel(
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE,
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE,
                1
            )
        )
        updateModel(
            CatalogShimmerDataModel(
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL,
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL,
                2
            )
        )
        updateModel(
            CatalogShimmerDataModel(
                CatalogLibraryConstant.CATALOG_PRODUCT,
                CatalogLibraryConstant.CATALOG_PRODUCT,
                3
            )
        )
    }

    fun setUpForHomePage() {
        updateModel(
            CatalogShimmerDataModel(
                CatalogLibraryConstant.CATALOG_CONTAINER_SPECIAL,
                CatalogLibraryConstant.CATALOG_CONTAINER_SPECIAL,
                CATALOG_SHIMMER_TOP_FIVE
            )
        )
        updateModel(
            CatalogShimmerDataModel(
                CatalogLibraryConstant.CATALOG_CONTAINER_RELEVANT,
                CatalogLibraryConstant.CATALOG_CONTAINER_RELEVANT,
                CATALOG_SHIMMER_TOP_FIVE
            )
        )
        updateModel(
            CatalogShimmerDataModel(
                CatalogLibraryConstant.CATALOG_PRODUCT,
                CatalogLibraryConstant.CATALOG_PRODUCT,
                CATALOG_SHIMMER_PRODUCTS
            )
        )
    }

    fun updateModel(model: BaseCatalogLibraryDataModel) {
        updateData(model.type(), model)
    }

    fun removeModel(type: String) {
        mapOfData.remove(type)
    }

    private fun updateData(key: String, baseCatalogHomeDataModel: BaseCatalogLibraryDataModel) {
        mapOfData[key] = baseCatalogHomeDataModel
    }
}
