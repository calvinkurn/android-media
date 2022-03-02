package com.tokopedia.catalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.catalog.model.datamodel.*
import com.tokopedia.catalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.catalog.model.util.CatalogConstant
import com.tokopedia.catalog.usecase.detail.CatalogComparisonProductUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogForYouViewModel @Inject constructor(
        private val catalogComparisonProductUseCase: CatalogComparisonProductUseCase
    ) : BaseViewModel() {

    private var shimmerData = MutableLiveData<ArrayList<BaseCatalogDataModel>>()
    private var dataList = MutableLiveData<ArrayList<BaseCatalogDataModel>>()
    val masterDataList = ArrayList<BaseCatalogDataModel>()
    private var hasMoreItems = MutableLiveData<Boolean>()
    private var error = MutableLiveData<Throwable>()
    private val pageFirst = 1
    var page = 1
    var lastScrollIndex = 0
    var isLoading = false

    fun getComparisonProducts(
        catalogId: String, brand: String, categoryId: String, limit: Int,
        page: Int, name: String
    ) {
        isLoading = true
        addShimmer(page)
        viewModelScope.launchCatchError(block = {
            val result = catalogComparisonProductUseCase.getCatalogComparisonProducts(catalogId,brand,
                categoryId,limit.toString(),page.toString(),name)
            removeShimmer()
            processResult(result)
            isLoading = false
        }, onError = {
            it.printStackTrace()
            error.value = it
            isLoading = false
        })
    }

    private fun processResult(result: Result<CatalogComparisonProductsResponse>) {
        when(result){
            is Success -> {
                if(result.data.catalogComparisonList?.catalogComparisonList.isNullOrEmpty()){
                    dataList.value = masterDataList
                    hasMoreItems.value = false
                }else {
                    addToMasterList(result.data.catalogComparisonList)
                    dataList.value = masterDataList
                    hasMoreItems.value = true
                    page ++
                }
            }

            is Fail -> {
                handleFail(result.throwable)
            }
        }
    }

    private fun handleFail(th: Throwable) {
        error.value = th
        hasMoreItems.value = true
    }

    private fun addToMasterList(list: CatalogComparisonProductsResponse.CatalogComparisonList?) {
        if(list != null) {
            for (product in list.catalogComparisonList){
                if(product != null){
                    masterDataList.add(CatalogForYouModel(CatalogConstant.COMPARISON_PRODUCT,
                        CatalogConstant.COMPARISON_PRODUCT,product))
                }
            }
        }
    }

    private val shimmerItemCount = 1
    private val shimmerList = arrayListOf<BaseCatalogDataModel>()

    private fun addShimmer(page : Int) {
        if(shimmerList.size == 0){
            for (i in 1..shimmerItemCount) {
                shimmerList.add(CatalogForYouShimmerModel())
            }
            if(page == pageFirst){
                masterDataList.clear()
            }
            masterDataList.addAll(shimmerList)
            shimmerData.value = masterDataList
        }
    }

    private fun removeShimmer() {
        masterDataList.removeAll(shimmerList)
        shimmerList.clear()
        shimmerData.value = masterDataList
    }

    fun getLoadedItemsSize() : Int{
        return dataList.value?.size ?: 0
    }


    fun getShimmerData(): LiveData<ArrayList<BaseCatalogDataModel>> = shimmerData
    fun getError(): LiveData<Throwable> = error
    fun getHasMoreItems(): LiveData<Boolean> = hasMoreItems
    fun getDataItems() : LiveData<ArrayList<BaseCatalogDataModel>> = dataList

}