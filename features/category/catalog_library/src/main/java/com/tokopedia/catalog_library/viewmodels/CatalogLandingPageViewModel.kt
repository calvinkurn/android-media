package com.tokopedia.catalog_library.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.catalog_library.model.datamodel.*
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.usecase.CatalogListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogLandingPageViewModel @Inject constructor(private val catalogTopFiveUseCase: CatalogListUseCase,
                                                      private val catalogMostViralUseCase: CatalogListUseCase,
                                                      private val catalogProductListUseCase: CatalogListUseCase) : ViewModel(){

    private val _catalogLandingPageLiveData = MutableLiveData<Result<CatalogLibraryDataModel>>()
    val catalogLandingPageLiveDataResponse: LiveData<Result<CatalogLibraryDataModel>> = _catalogLandingPageLiveData

    private val listOfComponents = mutableListOf<BaseCatalogLibraryDataModel>()

    fun getCatalogTopFiveData(
        categoryIdentifier: String,
        sortType: String,
        rows: String
    ) {
        catalogTopFiveUseCase.cancelJobs()
        catalogTopFiveUseCase.getCatalogListData(
            ::onAvailableCatalogTopFiveData,
            ::onFailLandingPageData,
            categoryIdentifier,
            sortType,
            rows
        )
    }

    fun getCatalogMostViralData(
        categoryIdentifier: String,
        sortType: String,
        rows: String
    ) {
        catalogMostViralUseCase.cancelJobs()
        catalogMostViralUseCase.getCatalogListData(
            ::onAvailableCatalogMostViralData,
            ::onFailLandingPageData,
            categoryIdentifier,
            sortType,
            rows
        )
    }

    fun getCatalogListData(
        categoryIdentifier: String,
        sortType: String,
        rows: String
    ) {
        catalogProductListUseCase.cancelJobs()
        catalogProductListUseCase.getCatalogListData(
            ::onAvailableCatalogListData,
            ::onFailLandingPageData,
            categoryIdentifier,
            sortType,
            rows
        )
    }

    private fun onAvailableCatalogTopFiveData(categoryIdentifier : String ,catalogListResponse: CatalogListResponse) {
        if (catalogListResponse.catalogGetList.catalogsList.isEmpty()) {
            onFailLandingPageData(IllegalStateException("No Catalog Landing Page Data"))
        } else {
            catalogListResponse.let {
                _catalogLandingPageLiveData.postValue(Success(mapCatalogTopFiveData(it)))
            }
        }
    }

    private fun onAvailableCatalogMostViralData(categoryIdentifier : String, catalogListResponse: CatalogListResponse) {
        if (catalogListResponse.catalogGetList.catalogsList.isEmpty()) {
            onFailLandingPageData(IllegalStateException("No Catalog Landing Page Data"))
        } else {
            catalogListResponse.let {
                _catalogLandingPageLiveData.postValue(Success(mapCatalogMostViralData(it)))
            }
        }
    }

    private fun onAvailableCatalogListData(categoryIdentifier : String, catalogListResponse: CatalogListResponse) {
        if (catalogListResponse.catalogGetList.catalogsList.isEmpty()) {
            onFailLandingPageData(IllegalStateException("No Catalog Landing Page Data"))
        } else {
            catalogListResponse.let {
                _catalogLandingPageLiveData.postValue(Success(mapCatalogListData(categoryIdentifier, it)))
            }
        }
    }

    private fun onFailLandingPageData(throwable: Throwable) {
        _catalogLandingPageLiveData.postValue(Fail(throwable))
    }

    private fun mapCatalogTopFiveData(data: CatalogListResponse): CatalogLibraryDataModel {
        val catalogTopFiveDataModel =
            CatalogTopFiveDataModel(
                CatalogLibraryConstant.CATALOG_TOP_FIVE,
                CatalogLibraryConstant.CATALOG_TOP_FIVE,
                data.catalogGetList.catalogsList
            )
        listOfComponents.add(catalogTopFiveDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun mapCatalogMostViralData(data: CatalogListResponse): CatalogLibraryDataModel {
        val catalogMostViralDataModel =
            CatalogMostViralDataModel(
                CatalogLibraryConstant.CATALOG_MOST_VIRAL,
                CatalogLibraryConstant.CATALOG_MOST_VIRAL,
                data.catalogGetList.catalogsList
            )
        listOfComponents.add(catalogMostViralDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun mapCatalogListData(categoryIdentifier : String, data: CatalogListResponse): CatalogLibraryDataModel {
        val catalogLandingListDataModel =
            CatalogLandingListDataModel(
                CatalogLibraryConstant.CATALOG_LIST,
                CatalogLibraryConstant.CATALOG_LIST,
                categoryIdentifier,
                data.catalogGetList.catalogsList
            )
        listOfComponents.add(catalogLandingListDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }
}
