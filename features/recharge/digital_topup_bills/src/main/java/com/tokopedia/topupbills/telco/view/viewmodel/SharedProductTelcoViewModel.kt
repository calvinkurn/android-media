package com.tokopedia.topupbills.telco.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.data.TelcoEnquiryData
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 20/05/19.
 */
class SharedProductTelcoViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                      val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val productCatalogItem = MutableLiveData<TelcoProduct>()
    val showTotalPrice = MutableLiveData<Boolean>()
    val promoItem = MutableLiveData<Int>()
    val enquiryResult = MutableLiveData<TelcoEnquiryData>()
    val productList = MutableLiveData<List<TelcoCatalogProductInput>>()

    private val _errorProductList = MutableLiveData<Throwable>()
    val errorProductList: LiveData<Throwable>
        get() = _errorProductList

    private val _loadingProductList = MutableLiveData<Boolean>()
    val loadingProductList: LiveData<Boolean>
        get() = _loadingProductList

    fun setProductCatalogSelected(productCatalogItem: TelcoProduct) {
        this.productCatalogItem.value = productCatalogItem
    }

    fun setShowTotalPrice(show: Boolean) {
        this.showTotalPrice.value = show
    }

    fun setPromoSelected(promoId: Int) {
        this.promoItem.value = promoId
    }

    fun setEnquiryResult(telcoEnquiryData: TelcoEnquiryData) {
        this.enquiryResult.value = telcoEnquiryData
    }

    // cache in 10 minutes
    fun getCatalogProductList(rawQuery: String, menuId: Int, operatorId: String) {
        _loadingProductList.postValue(true)
        launchCatchError(block = {
            val mapParam = HashMap<String, Any>()
            mapParam[KEY_MENU_ID] = menuId
            mapParam[KEY_OPERATOR_ID] = operatorId

            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoCatalogProductInputMultiTab::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest),
                        GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
            }.getSuccessData<TelcoCatalogProductInputMultiTab>()

            _loadingProductList.postValue(false)
            if (data.rechargeCatalogProductDataData.productInputList.isEmpty()) {
                _errorProductList.postValue(MessageErrorException())
            } else {
                productList.postValue(data.rechargeCatalogProductDataData.productInputList)
            }
        }) {
            _loadingProductList.postValue(false)
            _errorProductList.postValue(it)
        }
    }

    companion object {
        const val KEY_MENU_ID = "menuID"
        const val KEY_OPERATOR_ID = "operatorID"
    }
}