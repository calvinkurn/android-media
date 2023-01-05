package com.tokopedia.catalog_library.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.model.datamodel.*
import com.tokopedia.catalog_library.model.raw.CatalogListResponse
import com.tokopedia.catalog_library.model.raw.CatalogRelevantResponse
import com.tokopedia.catalog_library.model.raw.CatalogSpecialResponse
import com.tokopedia.catalog_library.usecase.CatalogListUseCase
import com.tokopedia.catalog_library.usecase.CatalogRelevantUseCase
import com.tokopedia.catalog_library.usecase.CatalogSpecialUseCase
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogHomepageViewModel @Inject constructor(
    private val catalogSpecialUseCase: CatalogSpecialUseCase,
    private val catalogRelevantUseCase: CatalogRelevantUseCase,
    private val catalogListUseCase: CatalogListUseCase
) : ViewModel() {

    private val _catalogHomeLiveData = MutableLiveData<Result<CatalogLibraryDataModel>>()
    val catalogLibraryLiveDataResponse: LiveData<Result<CatalogLibraryDataModel>> = _catalogHomeLiveData

    private val listOfComponents = mutableListOf<BaseCatalogLibraryDataModel>()

    fun getSpecialData(userId: String?) {
        catalogSpecialUseCase.cancelJobs()
        catalogSpecialUseCase.getSpecialData(::onAvailableSpecialData, ::onFailHomeData, userId)
    }

    fun getRelevantData() {
        catalogRelevantUseCase.cancelJobs()
        catalogRelevantUseCase.getRelevantData(::onAvailableRelevantData, ::onFailHomeData)
    }

    fun getCatalogListData(
        categoryIdentifier: String,
        sortType: String,
        rows: String
    ) {
        catalogListUseCase.cancelJobs()
        catalogListUseCase.getCatalogListData(
            ::onAvailableCatalogListData,
            ::onFailHomeData,
            categoryIdentifier,
            sortType,
            rows
        )
    }

    private fun onAvailableSpecialData(specialDataResponse: CatalogSpecialResponse) {
        if (specialDataResponse.catalogCategorySpecial.catalogSpecialDataList.isNullOrEmpty()) {
            onFailHomeData(IllegalStateException("No Special Response Data"))
        } else {
            specialDataResponse.let {
                _catalogHomeLiveData.postValue(Success(mapSpecialData(it)))
            }
        }
    }

    private fun onFailHomeData(throwable: Throwable) {
        _catalogHomeLiveData.postValue(Fail(throwable))
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

    private fun onAvailableCatalogListData(catalogIdentifier : String , catalogListResponse: CatalogListResponse) {
        if (catalogListResponse.catalogGetList.catalogsList.isNullOrEmpty()) {
            onFailHomeData(IllegalStateException("No Catalog List Response Data"))
        } else {
            catalogListResponse.let {
                _catalogHomeLiveData.postValue(Success(mapCatalogListData(it)))
            }
        }
    }

    private fun mapSpecialData(data: CatalogSpecialResponse): CatalogLibraryDataModel {
        val specialDataModel =
            CatalogContainerDataModel(
                CatalogLibraryConstant.CATALOG_CONTAINER_SPECIAL,
                CatalogLibraryConstant.CATALOG_CONTAINER_SPECIAL,
                "Kategori spesial buatmi",
                getSpecialVisitableList(data.catalogCategorySpecial.catalogSpecialDataList),
                RecyclerView.HORIZONTAL,
                "tokopedia://catalog-library/kategori",
                true,
                isGrid = true,
                4
            )
        listOfComponents.add(specialDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun getSpecialVisitableList(catalogSpecialDataList: ArrayList<CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialData>?): ArrayList<BaseCatalogLibraryDataModel>? {
        val visitableList = arrayListOf<BaseCatalogLibraryDataModel>()
        catalogSpecialDataList?.forEach {
            visitableList.add(CatalogSpecialDataModel(CatalogLibraryConstant.CATALOG_SPECIAL, CatalogLibraryConstant.CATALOG_SPECIAL,it))
        }
        return visitableList
    }

    private fun mapRelevantData(data: CatalogRelevantResponse): CatalogLibraryDataModel {
        val relevantDataModel =
            CatalogContainerDataModel(
                CatalogLibraryConstant.CATALOG_CONTAINER_RELEVANT,
                CatalogLibraryConstant.CATALOG_CONTAINER_RELEVANT,
                "Cek katalognya produk incaranmu",
                getRelevantVisitableList(data.catalogGetRelevant.catalogsList),
                RecyclerView.HORIZONTAL
            )
        listOfComponents.add(relevantDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun getRelevantVisitableList(catalogsList: ArrayList<CatalogRelevantResponse.Catalogs>): ArrayList<BaseCatalogLibraryDataModel>? {
        val visitableList = arrayListOf<BaseCatalogLibraryDataModel>()
        catalogsList.forEach {
            visitableList.add(CatalogRelevantDataModel(CatalogLibraryConstant.CATALOG_RELEVANT, CatalogLibraryConstant.CATALOG_RELEVANT,it))
        }
        return visitableList
    }

    private fun mapCatalogListData(data: CatalogListResponse): CatalogLibraryDataModel {
        val catalogListDataModel =
            CatalogListDataModel(
                CatalogLibraryConstant.CATALOG_LIST,
                CatalogLibraryConstant.CATALOG_LIST,
                data.catalogGetList.catalogsList
            )
        listOfComponents.add(catalogListDataModel)

        return CatalogLibraryDataModel(listOfComponents)
    }
}
