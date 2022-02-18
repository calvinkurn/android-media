package com.tokopedia.catalog.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.catalog.model.datamodel.BaseCatalogDataModel
import com.tokopedia.catalog.model.datamodel.CatalogStaggeredProductModel
import com.tokopedia.catalog.model.datamodel.CatalogStaggeredShimmerModel
import com.tokopedia.catalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.catalog.model.util.CatalogConstant
import com.tokopedia.catalog.usecase.detail.CatalogComparisonProductUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogProductComparisonViewModel @Inject constructor(
    private val catalogComparisonProductUseCase: CatalogComparisonProductUseCase,
    ) : BaseViewModel() {

    private var shimmerData = MutableLiveData<ArrayList<BaseCatalogDataModel>>()
    private var dataList = MutableLiveData<ArrayList<BaseCatalogDataModel>>()
    val masterDataList = ArrayList<BaseCatalogDataModel>()
    private var hasMoreItems = MutableLiveData<Boolean>()
    private var error = MutableLiveData<Throwable>()
    private val pageFirst = 1

    fun getComparisonProducts(recommendedCatalogId : String, catalogId: String, brand : String, categoryId : String,
                                       limit: Int, page : Int, name : String) {
        addShimmer(page)
        viewModelScope.launchCatchError(block = {
            val result = catalogComparisonProductUseCase.getCatalogComparisonProducts(catalogId,brand,
                categoryId,limit.toString(),page.toString(),name)
            removeShimmer()
            when(result){
                is Success -> {
                    if(result.data.catalogComparisonList?.catalogComparisonList.isNullOrEmpty()){
                        dataList.value = masterDataList
                        hasMoreItems.value = false
                    }else {
                        addToMasterList(recommendedCatalogId,result.data.catalogComparisonList)
                        dataList.value = masterDataList
                        hasMoreItems.value = true
                    }
                }

                is Fail -> {
                    hasMoreItems.value = false
                }
            }
        }, onError = {
            it.printStackTrace()
            error.value = it
        })
    }

    private fun addToMasterList(recommendedCatalogId : String, it: CatalogComparisonProductsResponse.CatalogComparisonList?) {
        it?.catalogComparisonList?.let { items ->
            for (product in items){
                product?.let {
                    if(product.id == recommendedCatalogId){ product.isActive = false }
                    masterDataList.add(CatalogStaggeredProductModel(CatalogConstant.COMPARISON_PRODUCT,
                        CatalogConstant.COMPARISON_PRODUCT,product))
                }
            }
        } ?: kotlin.run {
            hasMoreItems.value = false
        }
    }

    private val shimmerItemCount = 4
    private val shimmerList = arrayListOf<BaseCatalogDataModel>()

    private fun addShimmer(page : Int) {
        if(shimmerList.size == 0){
            for (i in 1..shimmerItemCount) {
                shimmerList.add(CatalogStaggeredShimmerModel())
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
    }


    fun getShimmerData(): LiveData<ArrayList<BaseCatalogDataModel>> = shimmerData
    fun getError(): LiveData<Throwable> = error
    fun getHasMoreItems(): LiveData<Boolean> = hasMoreItems
    fun getDataItems() : LiveData<ArrayList<BaseCatalogDataModel>> = dataList

}