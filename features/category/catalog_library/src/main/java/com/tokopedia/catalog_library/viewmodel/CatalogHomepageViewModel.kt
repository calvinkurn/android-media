package com.tokopedia.catalog_library.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.model.datamodel.*
import com.tokopedia.catalog_library.model.raw.CatalogBrandsPopularResponse
import com.tokopedia.catalog_library.model.raw.CatalogRelevantResponse
import com.tokopedia.catalog_library.model.raw.CatalogSpecialResponse
import com.tokopedia.catalog_library.usecase.CatalogBrandsPopularUseCase
import com.tokopedia.catalog_library.usecase.CatalogRelevantUseCase
import com.tokopedia.catalog_library.usecase.CatalogSpecialUseCase
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.APP_LINK_POPULAR_BRANDS
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogHomepageViewModel @Inject constructor(
    private val catalogSpecialUseCase: CatalogSpecialUseCase,
    private val catalogRelevantUseCase: CatalogRelevantUseCase,
    private val catalogBrandsPopularUseCase: CatalogBrandsPopularUseCase
) : ViewModel() {
    private val _catalogHomeLiveData = MutableLiveData<Result<CatalogLibraryDataModel>>()
    val catalogLibraryLiveDataResponse: LiveData<Result<CatalogLibraryDataModel>> =
        _catalogHomeLiveData

    private val listOfComponents = mutableListOf<BaseCatalogLibraryDM>()

    fun getSpecialData() {
        catalogSpecialUseCase.cancelJobs()
        catalogSpecialUseCase.getSpecialData(::onAvailableSpecialData, ::onFailHomeData)
    }

    private fun onAvailableSpecialData(specialDataResponse: CatalogSpecialResponse) {
        if (specialDataResponse.catalogCategorySpecial.catalogSpecialDataList.isNullOrEmpty()) {
            onFailHomeData(IllegalStateException("No Special Response Data"))
        } else {
            _catalogHomeLiveData.postValue(Success(mapSpecialData(specialDataResponse)))
        }
    }

    private fun mapSpecialData(data: CatalogSpecialResponse): CatalogLibraryDataModel {
        val specialDataModel =
            CatalogContainerDM(
                CatalogLibraryConstant.CATALOG_CONTAINER_SPECIAL,
                CatalogLibraryConstant.CATALOG_CONTAINER_SPECIAL,
                CatalogLibraryConstant.CATALOG_HOME_HEADING_KATEGORI_SPECIAL,
                getSpecialVisitableList(data.catalogCategorySpecial.catalogSpecialDataList),
                RecyclerView.HORIZONTAL,
                CatalogLibraryConstant.APP_LINK_KATEGORI,
                true,
                isGrid = true,
                columnCount = 4,
                marginForTitle = Margin(24, 52, 0, 16)
            )
        listOfComponents.add(specialDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun getSpecialVisitableList(catalogSpecialDataList: ArrayList<CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialData>?): ArrayList<BaseCatalogLibraryDM>? {
        val visitableList = arrayListOf<BaseCatalogLibraryDM>()
        catalogSpecialDataList?.forEach {
            visitableList.add(
                CatalogSpecialDM(
                    CatalogLibraryConstant.CATALOG_SPECIAL,
                    CatalogLibraryConstant.CATALOG_SPECIAL,
                    it
                )
            )
        }
        return visitableList
    }

    fun getRelevantData() {
        catalogRelevantUseCase.cancelJobs()
        catalogRelevantUseCase.getRelevantData(::onAvailableRelevantData, ::onFailHomeData)
    }

    private fun onAvailableRelevantData(relevantResponse: CatalogRelevantResponse) {
        if (relevantResponse.catalogGetRelevant.catalogsList.isNullOrEmpty()) {
            onFailHomeData(IllegalStateException("No Relevant Response Data"))
        } else {
            relevantResponse.let {
                _catalogHomeLiveData.postValue(Success(mapRelevantData(it)))
            }
        }
    }

    private fun mapRelevantData(data: CatalogRelevantResponse): CatalogLibraryDataModel {
        val relevantDataModel =
            CatalogContainerDM(
                CatalogLibraryConstant.CATALOG_CONTAINER_RELEVANT,
                CatalogLibraryConstant.CATALOG_CONTAINER_RELEVANT,
                CatalogLibraryConstant.CATALOG_HOME_HEADING_RELEVANT,
                getRelevantVisitableList(data.catalogGetRelevant.catalogsList),
                RecyclerView.HORIZONTAL,
                marginForTitle = Margin(48, 16, 0, 16),
                marginForRV = Margin(16, 0, 0, 16)
            )
        listOfComponents.add(relevantDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun getRelevantVisitableList(catalogsList: ArrayList<CatalogRelevantResponse.Catalogs>): ArrayList<BaseCatalogLibraryDM> {
        val visitableList = arrayListOf<BaseCatalogLibraryDM>()
        catalogsList.forEach {
            visitableList.add(
                CatalogRelevantDM(
                    CatalogLibraryConstant.CATALOG_RELEVANT,
                    CatalogLibraryConstant.CATALOG_RELEVANT,
                    it
                )
            )
        }
        return visitableList
    }

    fun getPopularBrands() {
        catalogBrandsPopularUseCase.cancelJobs()
        catalogBrandsPopularUseCase.getBrandPopular(::onAvailablePopularBrands, ::onFailHomeData)
    }

    private fun onAvailablePopularBrands(brandsPopularResponse: CatalogBrandsPopularResponse) {
        if (brandsPopularResponse.catalogGetBrandPopular.brands.isEmpty()) {
            onFailHomeData(IllegalStateException("No Brands Response Data"))
        } else {
            brandsPopularResponse.let {
                _catalogHomeLiveData.postValue(Success(mapPopularBrands(it)))
            }
        }
    }
    private fun mapPopularBrands(data: CatalogBrandsPopularResponse): CatalogLibraryDataModel {
        val popularBrandsDataModel = CatalogContainerDM(
            CatalogLibraryConstant.CATALOG_CONTAINER_POPULAR_BRANDS,
            CatalogLibraryConstant.CATALOG_CONTAINER_POPULAR_BRANDS,
            CatalogLibraryConstant.CATALOG_HOME_HEADING_POPULAR_BRANDS,
            getPopularBrandsVisitableList(data.catalogGetBrandPopular.brands),
            RecyclerView.HORIZONTAL,
            marginForTitle = Margin(36, 16, 0, 16),
            marginForRV = Margin(16, 0, 0, 16),
            hasMoreButtonEnabled = true,
            hasMoreButtonAppLink = APP_LINK_POPULAR_BRANDS
        )

        listOfComponents.add(popularBrandsDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun getPopularBrandsVisitableList(brands: ArrayList<CatalogBrandsPopularResponse.CatalogGetBrandPopular.Brands>): ArrayList<BaseCatalogLibraryDM> {
        val visitableList = arrayListOf<BaseCatalogLibraryDM>()
        brands.forEach {
            visitableList.add(
                CatalogPopularBrandsDM(
                    CatalogLibraryConstant.CATALOG_POPULAR_BRANDS,
                    CatalogLibraryConstant.CATALOG_POPULAR_BRANDS,
                    it
                )
            )
        }
        return visitableList
    }
    private fun onFailHomeData(throwable: Throwable) {
        _catalogHomeLiveData.postValue(Fail(throwable))
    }
}
