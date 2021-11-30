package com.tokopedia.hotel.cancellation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.cancellation.data.*
import com.tokopedia.hotel.common.util.HotelGqlMutation
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
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
            val graphqlRequest = GraphqlRequest(HotelGqlMutation.getCancellationQuery(), HotelCancellationModel.Response::class.java, params)
            graphqlUseCase.addRequest(graphqlRequest)

            val graphqlResponse = graphqlUseCase.executeOnBackground()
            val data = graphqlResponse.getSuccessData<HotelCancellationModel.Response>().response
            if (data.content.actionButton.isNotEmpty()) {
                val errMsg = Gson().toJson(data)
                mutableCancellationData.postValue(Fail(MessageErrorException(errMsg)))
            } else {
                mutableCancellationData.postValue(Success(data.data))
            }
        }) {
            mutableCancellationData.postValue(Fail(it))
        }
    }

    fun submitCancellationData(cancellationSubmitParam: HotelCancellationSubmitParam) {
        val params = mapOf(GET_CANCELLATION_SUBMIT_DATA_PARAM to cancellationSubmitParam)

        launchCatchError(block = {
            val data = withContext(dispatcher.main) {
                val graphqlRequest = GraphqlRequest(HotelGqlMutation.getSubmitCancellationQuery(), HotelCancellationSubmitResponse::class.java, params)
                graphqlRepository.response(listOf(graphqlRequest))
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