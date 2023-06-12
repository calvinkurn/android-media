package com.tokopedia.catalog_library.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDM
import com.tokopedia.catalog_library.model.datamodel.CatalogBrandCategoryDM
import com.tokopedia.catalog_library.model.datamodel.CatalogLibraryDataModel
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatDM
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse
import com.tokopedia.catalog_library.usecase.CatalogLibraryBrandCategoryUseCase
import com.tokopedia.catalog_library.usecase.CatalogLibraryUseCase
import com.tokopedia.catalog_library.util.CatalogLibraryConstant
import com.tokopedia.catalog_library.util.CatalogLibraryConstant.CATALOG_CONTAINER_CATEGORY_HEADER
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogLihatSemuaPageViewModel @Inject constructor(private val catalogLibraryUseCase: CatalogLibraryUseCase,
                                                         private val catalogLibraryBrandCategoryUseCase: CatalogLibraryBrandCategoryUseCase) :
    ViewModel() {

    private val _catalogLihatLiveData = MutableLiveData<Result<CatalogLibraryDataModel>>()
    val catalogLihatLiveDataResponse: LiveData<Result<CatalogLibraryDataModel>> =
        _catalogLihatLiveData

    private val _brandNameLiveData = MutableLiveData<String?>()
    val brandNameLiveData: LiveData<String?> =
        _brandNameLiveData

    private val listOfComponents = mutableListOf<BaseCatalogLibraryDM>()

    private var isAsc = true
    private var isTypeList = false
    private var categoryIdStr = ""

    fun getLihatSemuaPageData(sortOrder: String) {
        isAsc = sortOrder == "0"
        catalogLibraryUseCase.cancelJobs()
        catalogLibraryUseCase.getLibraryData(
            ::onAvailableLihatData,
            ::onFailLihatData,
            sortOrder
        )
    }

    fun getLihatSemuaByBrandData(categoryId : String, brandId : String, isOfTypeList : Boolean = true) {
        isTypeList = isOfTypeList
        categoryIdStr = categoryId
        catalogLibraryBrandCategoryUseCase.cancelJobs()
        catalogLibraryBrandCategoryUseCase.getBrandCategories(
            ::onAvailableBrandCategoryData,
            ::onFailLihatData, brandId
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

    private fun onAvailableBrandCategoryData(catalogLibraryResponse: CatalogLibraryResponse) {
        if (catalogLibraryResponse.categoryList.categoryDataList.isNullOrEmpty()) {
            onFailLihatData(IllegalStateException("No Lihat Semua Data"))
        } else {
            catalogLibraryResponse.let {
                _brandNameLiveData.postValue(it.categoryList.brandName)
                if(isTypeList){
                    _catalogLihatLiveData.postValue(Success(mapLihatData(it)))
                }else {
                    _catalogLihatLiveData.postValue(Success(mapBrandCategoryData(it)))
                }
            }
        }
    }

    private fun onFailLihatData(throwable: Throwable) {
        _catalogLihatLiveData.postValue(Fail(throwable))
    }

    private fun mapLihatData(data: CatalogLibraryResponse): CatalogLibraryDataModel {
        data.categoryList.categoryDataList?.forEachIndexed { index, categoryData ->
            listOfComponents.add(CatalogLihatDM(
                CatalogLibraryConstant.CATALOG_LIHAT_SEMUA,
                "${CatalogLibraryConstant.CATALOG_LIHAT_SEMUA}_$index",
                categoryData,
                isAsc , isTypeList,categoryIdStr
            ))
        }
        return CatalogLibraryDataModel(listOfComponents)
    }

    private fun mapBrandCategoryData(data: CatalogLibraryResponse): CatalogLibraryDataModel {
        listOfComponents.add(CatalogBrandCategoryDM(
            CATALOG_CONTAINER_CATEGORY_HEADER,
            CATALOG_CONTAINER_CATEGORY_HEADER,
            data.categoryList.categoryDataList
        ))
        return CatalogLibraryDataModel(listOfComponents)
    }

}
