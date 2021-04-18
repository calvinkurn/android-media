package com.tokopedia.hotel.cancellation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.cancellation.HotelCancellationQuery
import com.tokopedia.hotel.cancellation.data.*
import com.tokopedia.hotel.common.data.HotelErrorException
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by jessica on 30/04/20
 */

class HotelCancellationViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                     private val graphqlUseCase: MultiRequestGraphqlUseCase,
                                                     val dispatcher: CoroutineDispatchers) : BaseViewModel(dispatcher.io) {

    private val mutableCancellationData = MutableLiveData<Result<HotelCancellationModel>>()
    val cancellationData: LiveData<Result<HotelCancellationModel>>
        get() = mutableCancellationData

    private val mutableCancellationSubmitData = MutableLiveData<Result<HotelCancellationSubmitModel>>()
    val cancellationSubmitData: LiveData<Result<HotelCancellationSubmitModel>>
        get() = mutableCancellationSubmitData

    fun getCancellationData(invoiceId: String, fromCloud: Boolean = true) {
        val params = mapOf(GET_CANCELLATION_DATA_PARAM to HotelCancellationParam(invoiceId))

        launchCatchError(block = {
            graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(if (fromCloud) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build())
            graphqlUseCase.clearRequest()
            val graphqlRequest = GraphqlRequest(HotelCancellationQuery.getCancellationQuery(), HotelCancellationModel.Response::class.java, params)
            graphqlUseCase.addRequest(graphqlRequest)

            val graphqlResponse = graphqlUseCase.executeOnBackground()
            val errors = graphqlResponse.getError(HotelCancellationModel.Response::class.java)

            if (errors != null && errors.isNotEmpty() && errors.first().extensions != null) {
                mutableCancellationData.postValue(Fail(HotelErrorException(errors.first().extensions.code, errors.first().message)))
            } else {
                val data = graphqlResponse.getSuccessData<HotelCancellationModel.Response>().response.data
                mutableCancellationData.postValue(Success(data))
            }

        }) {
            mutableCancellationData.postValue(Fail(it))
        }
    }

    fun submitCancellationData(cancellationSubmitParam: HotelCancellationSubmitParam) {
        val params = mapOf(GET_CANCELLATION_SUBMIT_DATA_PARAM to cancellationSubmitParam)

        launchCatchError(block = {
            val data = withContext(dispatcher.main) {
                val graphqlRequest = GraphqlRequest(HotelCancellationQuery.getSubmitCancellationQuery(), HotelCancellationSubmitResponse::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelCancellationSubmitResponse>()
            mutableCancellationSubmitData.postValue(Success(data.response.data))
        }) {
            mutableCancellationSubmitData.postValue(Fail(it))
        }
    }

    companion object {
        const val GET_CANCELLATION_DATA_PARAM = "data"
        const val GET_CANCELLATION_SUBMIT_DATA_PARAM = "data"
    }
}