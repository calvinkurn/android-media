package com.tokopedia.find_native.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.common_category.model.productModel.ProductListResponse
import com.tokopedia.common_category.model.productModel.ProductsItem
import com.tokopedia.common_category.model.productModel.SearchProduct
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.find_native.data.model.RelatedLinkData
import com.tokopedia.find_native.data.repository.FindNavRepository
import com.tokopedia.find_native.util.FindNavParamBuilder
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class FindNavViewModel @Inject constructor(private val findNavRepository: FindNavRepository,
                                           private val findNavParamBuilder: FindNavParamBuilder) : ViewModel() {

    private val mProductList = MutableLiveData<Result<List<ProductsItem>>>()
    private val mProductCount = MutableLiveData<List<String>>()
    private var mBannedData = MutableLiveData<Result<ArrayList<String>>>()
    private var mQuickFilterModel = MutableLiveData<Result<List<Filter>>>()
    private var mDynamicFilterModel = MutableLiveData<Result<DynamicFilterModel>>()
    private var mRelatedLinkList = MutableLiveData<Result<List<RelatedLinkData>>>()
    private var isQuerySafe: Boolean = false

    fun fetchProductList(start: Int, productId: String, rows: Int, uniqueId: String,
                         selectedSort: HashMap<String, String>, selectedFilter: HashMap<String, String>) {
        val reqParams = findNavParamBuilder.generateProductFilterParams(productId, start, rows, uniqueId, selectedSort, selectedFilter)
        viewModelScope.launchCatchError(block = {
            val productListResponse: ProductListResponse? = findNavRepository.getProductList(reqParams.paramsAllValueInString)
            productListResponse?.let { productResponse ->
                (productResponse.searchProduct)?.let { searchProduct ->
                    handleDataForSearchProduct(searchProduct)
                }
            }
        }, onError = {
            it.printStackTrace()
            mProductList.value = Fail(it)
        })
    }

    private fun handleDataForSearchProduct(searchProduct: SearchProduct) {
        if (checkForBannedData(searchProduct)) {
            mBannedData.value = Success(getBannedDataValue(searchProduct))
        } else {
            isQuerySafe = (searchProduct.isQuerySafe ?: true)
            searchProduct.products.let { productList ->
                mProductList.value = Success((productList) as List<ProductsItem>)
            }
            setProductCountValue(searchProduct)
        }
    }

    private fun getBannedDataValue(searchProduct: SearchProduct): ArrayList<String> {
        val list = ArrayList<String>()
        searchProduct.errorMessage?.let {
            list.add(it)
        }
        searchProduct.liteUrl?.let {
            list.add(it)
        }
        return list
    }

    private fun setProductCountValue(searchProduct: SearchProduct) {
        val list = ArrayList<String>()
        searchProduct.countText.let {
            list.add(it.toString())
        }
        searchProduct.totalData.let {
            list.add(it.toString())
        }
        mProductCount.value = list
    }

    private fun checkForBannedData(searchProduct: SearchProduct): Boolean {
        return searchProduct.errorMessage?.isNotEmpty() ?: false
    }

    fun fetchQuickFilterList(productId: String) {
        val reqParams = findNavParamBuilder.generateQuickFilterParams(productId)
        viewModelScope.launchCatchError(block = {
            findNavRepository.getQuickFilterList(reqParams.paramsAllValueInString)?.let {
                mQuickFilterModel.value = Success(it)
            }
        }, onError = {
            it.printStackTrace()
            mQuickFilterModel.value = Fail(it)
        })
    }

    fun fetchDynamicFilterList(productId: String) {
        val reqParams = findNavParamBuilder.generateDynamicFilterParams(productId)
        viewModelScope.launchCatchError(block = {
            findNavRepository.getDynamicFilterList(reqParams.paramsAllValueInString)?.let {
                mDynamicFilterModel.value = Success(it)
            }
        }, onError = {
            it.printStackTrace()
            mDynamicFilterModel.value = Fail(it)
        })
    }

    fun fetchRelatedLinkList(productId: String) {
        val reqParams = findNavParamBuilder.generateRelatedLinkParams(productId)
        viewModelScope.launchCatchError(block = {
            findNavRepository.getRelatedLinkList(reqParams.paramsAllValueInString)?.let {
                val relatedLinkList = ArrayList<RelatedLinkData>()
                relatedLinkList.addAll(it.categoryTkpdFindRelated.relatedCategory)
                relatedLinkList.addAll(it.categoryTkpdFindRelated.relatedHotlist)
                mRelatedLinkList.value = Success(relatedLinkList)
            }
        }, onError = {
            it.printStackTrace()
            mRelatedLinkList.value = Fail(it)
        })
    }

    fun getProductListLiveData(): LiveData<Result<List<ProductsItem>>> {
        return mProductList
    }

    fun getProductCountLiveData(): LiveData<List<String>> {
        return mProductCount
    }

    fun getBannedLiveData(): LiveData<Result<ArrayList<String>>> {
        return mBannedData
    }

    fun getQuickFilterListListLiveData(): LiveData<Result<List<Filter>>> {
        return mQuickFilterModel
    }

    fun getDynamicFilterListLiveData(): LiveData<Result<DynamicFilterModel>> {
        return mDynamicFilterModel
    }

    fun getRelatedLinkListLiveData(): LiveData<Result<List<RelatedLinkData>>> {
        return mRelatedLinkList
    }

    fun checkForAdultData(): Boolean {
        return isQuerySafe
    }
}