package com.tokopedia.discovery.find.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductListResponse
import com.tokopedia.discovery.categoryrevamp.data.productModel.ProductsItem
import com.tokopedia.discovery.find.data.repository.FindNavRepository
import com.tokopedia.discovery.find.data.model.RelatedLinkData
import com.tokopedia.discovery.find.util.FindNavParamBuilder
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class FindNavViewModel @Inject constructor() : ViewModel() {

    val mProductList = MutableLiveData<Result<List<ProductsItem>>>()
    val mProductCount = MutableLiveData<String>()
    var mBannedData = ArrayList<String>()
    var mQuickFilterModel = MutableLiveData<Result<List<Filter>>>()
    var mDynamicFilterModel = MutableLiveData<Result<DynamicFilterModel>>()
    var mRelatedLinkList = MutableLiveData<Result<List<RelatedLinkData>>>()
    private val findNavParamBuilder: FindNavParamBuilder by lazy { FindNavParamBuilder() }

    @Inject
    lateinit var findNavRepository: FindNavRepository

    fun fetchProductList(start: Int, productId: String, rows: Int, uniqueId: String,
                         selectedSort: HashMap<String, String>, selectedFilter: HashMap<String, String>) {
        val reqParams = findNavParamBuilder.generateProductFilterParams(productId, start, rows, uniqueId, selectedSort, selectedFilter)
        viewModelScope.launchCatchError(block = {
            val productListResponse: ProductListResponse? = findNavRepository.getProductList(reqParams.paramsAllValueInString)
            productListResponse?.let { productResponse ->
                (productResponse.searchProduct)?.let { searchProduct ->
                    val list = ArrayList<String>()
                    searchProduct.errorMessage?.let {
                        list.add(it)
                    }
                    searchProduct.liteUrl?.let {
                        list.add(it)
                    }
                    mBannedData = list
                    searchProduct.products.let { productList ->
                        mProductList.value = Success((productList) as List<ProductsItem>)
                    }
                    mProductCount.value = searchProduct.countText
                }
            }
        }, onError = {
            it.printStackTrace()
            mProductList.value = Fail(it)
        })
    }

    fun fetchQuickFilterList(productId: String) {
        val reqParams = findNavParamBuilder.generateQuickFilterParams(productId)
        viewModelScope.launchCatchError(block = {
            findNavRepository.getQuickFilterList(reqParams.paramsAllValueInString)?.let {
                mQuickFilterModel.value = Success(it as List<Filter>)
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
}