package com.tokopedia.promocheckout.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.promocheckout.list.model.listcoupon.DataPromoCheckoutList
import com.tokopedia.promocheckout.list.model.listlastseen.PromoCheckoutLastSeenModel
import com.tokopedia.promocheckout.util.PromoCheckoutQuery
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import java.util.HashMap
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

/**
 * @author: astidhiyaa on 30/07/21.
 */
class PromoCheckoutListViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                 private val graphqlRepository: GraphqlRepository): BaseViewModel(dispatcher.io) {

    private val _dataPromoCheckoutList = MutableLiveData<Result<DataPromoCheckoutList>>()
    val dataPromoCheckoutList: LiveData<Result<DataPromoCheckoutList>>
        get() = _dataPromoCheckoutList

    private val _dataPromoLastSeen = MutableLiveData<Result<List<PromoCheckoutLastSeenModel>>>()
    val dataPromoLastSeen: LiveData<Result<List<PromoCheckoutLastSeenModel>>>
        get() = _dataPromoLastSeen

    val showLoading = MutableLiveData<Boolean>()

    fun getPromoList(serviceId: String, categoryId: Int, page: Int){
        showLoading.postValue(true)
        launchCatchError(block = {
           val data =  withContext(dispatcher.io){
               val variables = HashMap<String, Any>()
               variables[INPUT_GQL] = generateInputList(page, serviceId, categoryId)
               val graphqlRequest = GraphqlRequest(PromoCheckoutQuery.promoCheckoutList(), DataPromoCheckoutList::class.java, variables, false)
               graphqlRepository.getReseponse(listOf(graphqlRequest))
           }.getSuccessData<DataPromoCheckoutList>()
            showLoading.postValue( false)
            _dataPromoCheckoutList.postValue(Success(data))
        }){
            showLoading.postValue( false)
            _dataPromoCheckoutList.postValue(Fail(it))
        }
    }

    fun getPromoLastSeen(categoryIDs: List<Int>){
        launchCatchError(block = {
            val data =  withContext(dispatcher.io){
                val variables = HashMap<String, Any>()
                variables[CATEGORY_IDS] = categoryIDs
                val graphqlRequest = GraphqlRequest(PromoCheckoutQuery.promoCheckoutLastSeen(), PromoCheckoutLastSeenModel.Response::class.java, variables, false)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<PromoCheckoutLastSeenModel.Response>()
            val lastSeenData = data.promoModels.filter { it.promoCode.isNotEmpty() && it.title.isNotEmpty() }
            _dataPromoLastSeen.postValue(Success(lastSeenData))
        }){
            _dataPromoLastSeen.postValue(Fail(it))
        }
    }

    private fun generateInputList(page: Int, serviceId: String, categoryId: Int): JsonObject {
        val input = JsonObject()
        input.addProperty(SERVICE_ID, serviceId)
        input.addProperty(CATEGORY_ID, categoryId)
        input.addProperty(CATEGORY_ID_COUPON, DEFAULT_VALUE_EMPTY)
        input.addProperty(PAGE, page)
        input.addProperty(LIMIT, PROMO_LIST_LIMIT)
        input.addProperty(INCLUDE_EXTRA_INFO, DEFAULT_VALUE_EMPTY)
        input.addProperty(
            API_VERSION,
            API_VERSION_VALUE
        )
        return input
    }

    companion object {
        const val API_VERSION = "apiVersion"
        const val INPUT_GQL = "input"
        const val SERVICE_ID = "serviceID"
        const val CATEGORY_ID = "categoryID"
        const val CATEGORY_IDS = "categoryIDs"
        const val CATEGORY_ID_COUPON = "categoryIDCoupon"
        const val PAGE = "page"
        const val LIMIT = "limit"
        const val INCLUDE_EXTRA_INFO = "includeExtraInfo"
        const val API_VERSION_VALUE = "2.0.0"
        const val DEFAULT_VALUE_EMPTY = 0
        const val PROMO_LIST_LIMIT = 0
    }
}