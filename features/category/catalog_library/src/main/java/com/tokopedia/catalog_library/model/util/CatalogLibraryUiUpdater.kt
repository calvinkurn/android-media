package com.tokopedia.catalog_library.model.util

import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogShimmerDataModel

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
                CatalogLibraryConstant.CATALOG_LIST,
                CatalogLibraryConstant.CATALOG_LIST,
                3
            )
        )
    }

    fun setUpForHomePage() {
        updateModel(
            CatalogShimmerDataModel(
                CatalogLibraryConstant.CATALOG_CONTAINER_SPECIAL,
                CatalogLibraryConstant.CATALOG_CONTAINER_SPECIAL,
                1
            )
        )
        updateModel(
            CatalogShimmerDataModel(
                CatalogLibraryConstant.CATALOG_CONTAINER_RELEVANT,
                CatalogLibraryConstant.CATALOG_CONTAINER_RELEVANT,
                2
            )
        )
        updateModel(
            CatalogShimmerDataModel(
                CatalogLibraryConstant.CATALOG_LIST,
                CatalogLibraryConstant.CATALOG_LIST,
                3
            )
        )
    }


    fun updateModel(model: BaseCatalogLibraryDataModel) {
        updateData(model.type(), model)
    }

    private fun updateData(key: String, baseCatalogHomeDataModel: BaseCatalogLibraryDataModel) {
        mapOfData[key] = baseCatalogHomeDataModel
    }
}
