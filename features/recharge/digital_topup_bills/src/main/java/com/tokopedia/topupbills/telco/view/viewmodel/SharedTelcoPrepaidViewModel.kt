package com.tokopedia.topupbills.telco.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topupbills.telco.data.TelcoCatalogProductInput
import com.tokopedia.topupbills.telco.data.TelcoCatalogProductInputMultiTab
import com.tokopedia.topupbills.telco.data.TelcoProduct
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 20/05/19.
 */
class SharedTelcoPrepaidViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                      private val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val _productCatalogItem = MutableLiveData<TelcoProduct>()
    val productCatalogItem: LiveData<TelcoProduct>
        get() = _productCatalogItem

    private val _showTotalPrice = MutableLiveData<Boolean>()
    val showTotalPrice: LiveData<Boolean>
        get() = _showTotalPrice

    private val _productList = MutableLiveData<Result<List<TelcoCatalogProductInput>>>()
    val productList: LiveData<Result<List<TelcoCatalogProductInput>>>
        get() = _productList

    private val _selectedCategoryViewPager = MutableLiveData<String>()
    val selectedCategoryViewPager: LiveData<String>
        get() = _selectedCategoryViewPager

    fun setProductCatalogSelected(productCatalogItem: TelcoProduct) {
        _productCatalogItem.postValue(productCatalogItem)
    }

    fun setShowTotalPrice(show: Boolean) {
        _showTotalPrice.postValue(show)
    }

    fun setSelectedCategoryViewPager(categoryName: String) {
        _selectedCategoryViewPager.postValue(categoryName)
    }

    // cache in 10 minutes
    fun getCatalogProductList(rawQuery: String, menuId: Int, operatorId: String) {
        launchCatchError(block = {
            val mapParam = HashMap<String, Any>()
            mapParam[KEY_MENU_ID] = menuId
            mapParam[KEY_OPERATOR_ID] = operatorId

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoCatalogProductInputMultiTab::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            }.getSuccessData<TelcoCatalogProductInputMultiTab>()

            if (data.rechargeCatalogProductDataData.productInputList.isEmpty()) {
                _productList.postValue(Fail(MessageErrorException()))
            } else {
                _productList.postValue(Success(data.rechargeCatalogProductDataData.productInputList))
            }
        }) {
            _productList.postValue(Fail(it))
        }
    }

    companion object {
        const val KEY_MENU_ID = "menuID"
        const val KEY_OPERATOR_ID = "operatorID"
    }
}