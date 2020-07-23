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
import com.tokopedia.topupbills.telco.data.TelcoProductComponentData
import com.tokopedia.topupbills.telco.data.TelcoProductDataCollection
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentName
import com.tokopedia.topupbills.telco.data.constant.TelcoProductType
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
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

    val productItem = MutableLiveData<TelcoProductDataCollection>()
    val showTotalPrice = MutableLiveData<Boolean>()
    val promoItem = MutableLiveData<Int>()
    val enquiryResult = MutableLiveData<TelcoEnquiryData>()

    private val _productList = MutableLiveData<Result<Map<String, TelcoProductComponentData>>>()
    val productList: LiveData<Result<Map<String, TelcoProductComponentData>>>
        get() = _productList

    private val _loadingProductList = MutableLiveData<Boolean>()
    val loadingProductList: LiveData<Boolean>
        get() = _loadingProductList

    fun setProductSelected(productItem: TelcoProductDataCollection) {
        this.productItem.value = productItem
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

    //cache for 10 minutes
    fun getProductList(rawQuery: String, mapParam: Map<String, Map<String, Any>>,
                       onSuccess:() -> Unit) {
        _loadingProductList.postValue(true)
        launchCatchError(block = {

            val mapResult = mutableMapOf<String, TelcoProductComponentData>()
            mapParam.map {
                val data = withContext(Dispatchers.IO) {
                    val graphqlRequest = GraphqlRequest(rawQuery, TelcoProductComponentData::class.java, it.value)
                    graphqlRepository.getReseponse(listOf(graphqlRequest),
                            GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
                                    .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_1.`val`() * 10).build())
                }.getSuccessData<TelcoProductComponentData>()


                if (data.rechargeProductData.productDataCollections.isEmpty()) {
                    mapResult.put(it.key, TelcoProductComponentData())
                } else {
                    if (it.key == TelcoComponentName.PRODUCT_PULSA) {
                        data.productType = TelcoProductType.PRODUCT_GRID
                    } else {
                        data.productType = TelcoProductType.PRODUCT_LIST
                    }
                    mapResult.put(it.key, data)
                }
            }
            onSuccess()
            _loadingProductList.postValue(false)
            _productList.postValue(Success(mapResult))
        }) {
            _loadingProductList.postValue(false)
            val mapResult = mutableMapOf<String, TelcoProductComponentData>()
            mapParam.map {
                mapResult.put(it.key, TelcoProductComponentData())
            }
            _productList.postValue(Success(mapResult))
        }
    }
}