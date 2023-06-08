package com.tokopedia.catalog_library.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.catalog_library.model.datamodel.*
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.usecase.CatalogProductsUseCase
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_MOST_VIRAL
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_TOP_FIVE
import com.tokopedia.catalog_library.util.CatalogLibraryResponseException
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

    private val listOfComponents = mutableListOf<BaseCatalogLibraryDM>()

    fun getCatalogTopFiveData(
        categoryId: String,
        sortType: Int,
        rows: Int
    ) {
        catalogTopFiveUseCase.cancelJobs()
        catalogTopFiveUseCase.getCatalogProductsData(
            ::onAvailableCatalogTopFiveData,
            ::onFailLandingPageDataTopFive,
            categoryId,
            sortType,
            rows
        )
    }

    fun getCatalogMostViralData(
        categoryId: String,
        sortType: Int,
        rows: Int
    ) {
        catalogMostViralUseCase.cancelJobs()
        catalogMostViralUseCase.getCatalogProductsData(
            ::onAvailableCatalogMostViralData,
            ::onFailLandingPageDataMostViral,
            categoryId,
            sortType,
            rows
        )
    }

    private fun onAvailableCatalogTopFiveData(catalogListResponse: CatalogListResponse, page: Int = 1) {
        if (catalogListResponse.catalogGetList.catalogsProduct.isEmpty()) {
            onFailLandingPageData(CatalogLibraryResponseException("No Catalog Landing Page Data",
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE))
        } else {
            catalogListResponse.let {
                _catalogLandingPageLiveData.postValue(Success(mapCatalogTopFiveData(it)))
                _categoryName.postValue(it.catalogGetList.categoryName)
            }
        }
    }

    private fun onAvailableCatalogMostViralData(catalogListResponse: CatalogListResponse, page: Int = 1) {
        if (catalogListResponse.catalogGetList.catalogsProduct.isEmpty()) {
            onFailLandingPageData(CatalogLibraryResponseException("No Catalog Landing Page Data",
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL))
        } else {
            catalogListResponse.let {
                _catalogLandingPageLiveData.postValue(Success(mapCatalogMostViralData(it)))
            }
        }
    }

    private fun onFailLandingPageData(throwable: Throwable) {
        _catalogLandingPageLiveData.postValue(Fail(throwable))
    }

    private fun onFailLandingPageDataMostViral(throwable: Throwable) {
        _catalogLandingPageLiveData.postValue(Fail(CatalogLibraryResponseException(throwable.message ?: ""
            ,CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL)))
    }

    private fun onFailLandingPageDataTopFive(throwable: Throwable) {
        _catalogLandingPageLiveData.postValue(Fail(CatalogLibraryResponseException(throwable.message ?: ""
            ,CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE)))
    }

    private fun mapCatalogTopFiveData(data: CatalogListResponse): CatalogLibraryDataModel {
        val catalogTopFiveDataModel =
            CatalogContainerDM(
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE,
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_TOP_FIVE,
                "Top 5 ${data.catalogGetList.categoryName.lowercase()} terlaris di toped",
                getTopFiveVisitableList(
                    data.catalogGetList.catalogsProduct,
                    data.catalogGetList.categoryName
                ),
                marginForTitle = Margin(0, 16, 12, 16),
                marginForRV = Margin(12, 0, 0, 16)
            )
        listOfComponents.add(catalogTopFiveDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun getTopFiveVisitableList(
        catalogsProduct: ArrayList<CatalogListResponse.CatalogGetList.CatalogsProduct>,
        categoryName: String
    ): ArrayList<BaseCatalogLibraryDM> {
        val visitableList = arrayListOf<BaseCatalogLibraryDM>()
        catalogsProduct.forEachIndexed { index, catalogTopFive ->
            catalogTopFive.rank = (index + 1)
            visitableList.add(
                CatalogTopFiveDM(
                    CATALOG_TOP_FIVE,
                    CATALOG_TOP_FIVE,
                    catalogTopFive,
                    categoryName
                )
            )
        }
        return visitableList
    }

    private fun mapCatalogMostViralData(data: CatalogListResponse): CatalogLibraryDataModel {
        val catalogMostViralDataModel =
            CatalogContainerDM(
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL,
                CatalogLibraryConstant.CATALOG_CONTAINER_TYPE_MOST_VIRAL,
                "",
                getMostViralVisitableList(
                    data.catalogGetList.catalogsProduct,
                    data.catalogGetList.categoryName
                ),
                marginForTitle = Margin(16, 6, 0, 6)
            )
        listOfComponents.add(catalogMostViralDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun getMostViralVisitableList(
        catalogsProduct: ArrayList<CatalogListResponse.CatalogGetList.CatalogsProduct>,
        categoryName: String
    ): ArrayList<BaseCatalogLibraryDM> {
        val visitableList = arrayListOf<BaseCatalogLibraryDM>()
        catalogsProduct.forEach {
            visitableList.add(
                CatalogMostViralDM(
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
