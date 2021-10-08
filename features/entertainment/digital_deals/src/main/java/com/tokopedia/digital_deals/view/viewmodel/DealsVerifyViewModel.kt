package com.tokopedia.digital_deals.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.digital_deals.data.*
import com.tokopedia.digital_deals.view.model.response.DealsDetailsResponse
import com.tokopedia.digital_deals.view.utils.DealsQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DealsVerifyViewModel @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatchers): BaseViewModel(dispatcher.io) {

    private val mutableDealsVerify = MutableLiveData<Result<DealsVerifyResponse>>()
    val dealsVerify: LiveData<Result<DealsVerifyResponse>>
        get() = mutableDealsVerify

    fun verify(verifyRequest: DealsVerifyRequest) {
        val params = mapOf(eventVerify to verifyRequest)
        launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                val graphqlRequest = GraphqlRequest(DealsQuery.mutationVerifyV2(), DealsVerifyResponse::class.java, params)
                graphqlRepository.response(listOf(graphqlRequest))
            }.getSuccessData<DealsVerifyResponse>()
            if (data.eventVerify.error.isNullOrEmpty()) {
                mutableDealsVerify.postValue(Success(data))
            } else {
                mutableDealsVerify.postValue(Fail(MessageErrorException(data.eventVerify.errorDescription)))
            }
        }) {
            mutableDealsVerify.postValue(Fail(it))
        }
    }

    fun mapVerifyRequest(currentQuantity: Int, dealsResponse: DealsDetailsResponse): DealsVerifyRequest{
        return DealsVerifyRequest(
                book = true,
                checkout = false,
                cartdata = CartData(
                    metadata = MetaData(
                            categoryName = categoryName,
                            totalPrice = currentQuantity * dealsResponse.salesPrice,
                            quantity = currentQuantity,
                            productIds = listOf(dealsResponse.id.toString()),
                            productNames = listOf(dealsResponse.displayName),
                            providerIds = listOf(dealsResponse.providerId.toString()),
                            itemIds = listOf(dealsResponse.id.toString()),
                            itemMaps = listOf(
                                ItemMap(
                                        id = dealsResponse.id.toString(),
                                        name = dealsResponse.displayName,
                                        productId = dealsResponse.id.toString(),
                                        productName = dealsResponse.displayName,
                                        providerId = dealsResponse.providerId.toString(),
                                        categoryId = dealsResponse.categoryId.toString(),
                                        startTime = getDateMilis(dealsResponse.minStartDate.toString()),
                                        endTime = getDateMilis(dealsResponse.maxEndDate.toString()),
                                        price = dealsResponse.salesPrice,
                                        quantity = currentQuantity,
                                        totalPrice = currentQuantity * dealsResponse.salesPrice,
                                        scheduleTimestamp = dealsResponse.maxEndDate.toString(),
                                        productImage = dealsResponse.imageWeb,
                                        flagID = dealsResponse.customText1.toString()
                                )
                            )
                    )
                )
        )
    }

    private fun getDateMilis(date: String): String {
        try {
            val dateFormat = SimpleDateFormat(" dd MMM YYYY hh:mm")
            val dateMilis = Date(TimeUnit.SECONDS.toMillis(date.toLong()))
            return dateFormat.format(dateMilis).toString()
        } catch (e: Exception){
            return date
        }
    }

    companion object {
        const val eventVerify = "eventVerify"
        const val categoryName = "deal"
    }

}