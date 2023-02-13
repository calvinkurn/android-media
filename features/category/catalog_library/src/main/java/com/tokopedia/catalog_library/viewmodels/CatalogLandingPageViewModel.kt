package com.tokopedia.catalog_library.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.catalog_library.model.datamodel.*
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.usecase.CatalogProductsUseCase
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_MOST_VIRAL
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_TOP_FIVE
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogLandingPageViewModel @Inject constructor(
    private val catalogTopFiveUseCase: CatalogProductsUseCase,
    private val catalogMostViralUseCase: CatalogProductsUseCase
) : ViewModel() {

    private val _catalogLandingPageLiveData = MutableLiveData<Result<CatalogLibraryDataModel>>()
    val catalogLandingPageLiveDataResponse: LiveData<Result<CatalogLibraryDataModel>> =
        _catalogLandingPageLiveData

    private val _categoryName = MutableLiveData<String>()
    val categoryName: LiveData<String> = _categoryName

    private val listOfComponents = mutableListOf<BaseCatalogLibraryDataModel>()

    fun getCatalogTopFiveData(
        categoryIdentifier: String,
        sortType: Int,
        rows: Int
    ) {
        catalogTopFiveUseCase.cancelJobs()
        catalogTopFiveUseCase.getCatalogProductsData(
            ::onAvailableCatalogTopFiveData,
            ::onFailLandingPageData,
            categoryIdentifier,
            sortType,
            rows
        )
    }

    fun getCatalogMostViralData(
        categoryIdentifier: String,
        sortType: Int,
        rows: Int
    ) {
        catalogMostViralUseCase.cancelJobs()
        catalogMostViralUseCase.getCatalogProductsData(
            ::onAvailableCatalogMostViralData,
            ::onFailLandingPageData,
            categoryIdentifier,
            sortType,
            rows
        )
    }

    private fun onAvailableCatalogTopFiveData(catalogListResponse: CatalogListResponse, page: Int = 1) {
        if (catalogListResponse.catalogGetList.catalogsProduct.isEmpty()) {
            onFailLandingPageData(IllegalStateException("No Catalog Landing Page Data"))
        } else {
            catalogListResponse.let {
                _catalogLandingPageLiveData.postValue(Success(mapCatalogTopFiveData(it)))
                _categoryName.postValue(it.catalogGetList.categoryName)
            }
        }
    }

    private fun onAvailableCatalogMostViralData(catalogListResponse: CatalogListResponse, page: Int = 1) {
        if (catalogListResponse.catalogGetList.catalogsProduct.isEmpty()) {
            onFailLandingPageData(IllegalStateException("No Catalog Landing Page Data"))
        } else {
            catalogListResponse.let {
                _catalogLandingPageLiveData.postValue(Success(mapCatalogMostViralData(it)))
            }
        }
    }

    private fun onFailLandingPageData(throwable: Throwable) {
        _catalogLandingPageLiveData.postValue(Fail(throwable))
    }

    private fun mapCatalogTopFiveData(data: CatalogListResponse): CatalogLibraryDataModel {
        val catalogTopFiveDataModel =
            CatalogContainerDataModel(
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE,
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE,
                "Top 5 category terlaris di toped",
                getTopFiveVisitableList(data.catalogGetList.catalogsProduct)
            )
        listOfComponents.add(catalogTopFiveDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun getTopFiveVisitableList(catalogsProduct: ArrayList<CatalogListResponse.CatalogGetList.CatalogsProduct>): ArrayList<BaseCatalogLibraryDataModel> {
        val visitableList = arrayListOf<BaseCatalogLibraryDataModel>()
        catalogsProduct.forEachIndexed { index, catalogTopFive ->
            catalogTopFive.rank = (index + 1)
            visitableList.add(
                CatalogTopFiveDataModel(
                    CATALOG_TOP_FIVE,
                    CATALOG_TOP_FIVE,
                    catalogTopFive
                )
            )
        }
        return visitableList
    }

    private fun mapCatalogMostViralData(data: CatalogListResponse): CatalogLibraryDataModel {
        val catalogMostViralDataModel =
            CatalogContainerDataModel(
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL,
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL,
                "",
                getMostViralVisitableList(
                    data.catalogGetList.catalogsProduct,
                    data.catalogGetList.categoryName
                )
            )
        listOfComponents.add(catalogMostViralDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun getMostViralVisitableList(
        catalogsProduct: ArrayList<CatalogListResponse.CatalogGetList.CatalogsProduct>,
        categoryName: String
    ): ArrayList<BaseCatalogLibraryDataModel> {
        val visitableList = arrayListOf<BaseCatalogLibraryDataModel>()
        catalogsProduct.forEach {
            visitableList.add(
                CatalogMostViralDataModel(
                    CATALOG_MOST_VIRAL,
                    CATALOG_MOST_VIRAL,
                    it,
                    categoryName
                )
            )
        }
        return visitableList
    }
}
