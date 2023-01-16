package com.tokopedia.catalog_library.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogLibraryDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatDataModel
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse
import com.tokopedia.catalog_library.model.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.usecase.CatalogLibraryUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogLihatSemuaPageViewModel @Inject constructor(private val catalogLibraryUseCase: CatalogLibraryUseCase) :
    ViewModel() {

    private val _catalogLihatLiveData = MutableLiveData<Result<CatalogLibraryDataModel>>()
    val catalogLihatLiveDataResponse: LiveData<Result<CatalogLibraryDataModel>> = _catalogLihatLiveData

    private val listOfComponents = mutableListOf<BaseCatalogLibraryDataModel>()

    fun getLihatSemuaPageData(sortOrder: String) {
        catalogLibraryUseCase.cancelJobs()
        catalogLibraryUseCase.getLibraryData(
            ::onAvailableLihatData,
            ::onFailLihatData,
            sortOrder
        )
    }

    private fun onAvailableLihatData(catalogLibraryResponse: CatalogLibraryResponse) {
        if (catalogLibraryResponse.categoryList.categoryDataList.isNullOrEmpty()) {
            onFailLihatData(IllegalStateException("No Lihat Semua Data"))
        } else {
            catalogLibraryResponse.let {
                _catalogLihatLiveData.postValue(Success(mapLihatData(it)))
            }
        }
    }

    private fun onFailLihatData(throwable: Throwable) {
        _catalogLihatLiveData.postValue(Fail(throwable))
    }

    private fun mapLihatData(data: CatalogLibraryResponse): CatalogLibraryDataModel {
        data.categoryList.categoryDataList?.forEachIndexed { index, categoryData ->
            val lihatDataModel = CatalogLihatDataModel(
                CatalogLibraryConstant.CATALOG_LIHAT_SEMUA,
                "${ CatalogLibraryConstant.CATALOG_LIHAT_SEMUA}_${index}",
                categoryData
            )
            listOfComponents.add(lihatDataModel)
        }
        return CatalogLibraryDataModel(listOfComponents)
    }
}
