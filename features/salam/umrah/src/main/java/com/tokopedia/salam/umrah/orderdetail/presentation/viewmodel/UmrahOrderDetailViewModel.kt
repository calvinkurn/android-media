package com.tokopedia.salam.umrah.orderdetail.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.salam.umrah.orderdetail.data.UmrahOrderDetailsEntity
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * @author by furqan on 08/10/2019
 */
class UmrahOrderDetailViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                    dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val orderDetailData = MutableLiveData<Result<UmrahOrderDetailsEntity>>()

    fun getOrderDetail(rawQuery: String, orderId: String, orderCategory: String, response: String) {
        /*val params = mapOf(PARAM_ORDER_ID to orderId,
                PARAM_ORDER_CATEGORY_STR to orderCategory)

        launchCatchError(block = {
            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, UmrahOrderDetailsEntity.Response::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<UmrahOrderDetailsEntity.Response>()

            orderDetailData.value = Success(data.orderDetails)
        }) {
            orderDetailData.value = Fail(it)
        }*/

        launchCatchError(block = {
            delay(3000)
            val gson = Gson()
            orderDetailData.value = Success(gson.fromJson(response,
                    UmrahOrderDetailsEntity.Response::class.java).orderDetails)
        }){
            it.printStackTrace()
        }
    }

    companion object {
        const val PARAM_ORDER_ID = "orderId"
        const val PARAM_ORDER_CATEGORY_STR = "orderCategoryStr"
    }

}