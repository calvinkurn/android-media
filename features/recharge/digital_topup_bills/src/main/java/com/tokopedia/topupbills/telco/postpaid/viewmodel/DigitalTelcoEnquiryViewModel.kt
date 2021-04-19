package com.tokopedia.topupbills.telco.postpaid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.topupbills.data.TelcoEnquiryData
import com.tokopedia.common.topupbills.data.TopupBillsEnquiryQuery
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 27/05/19.
 */
class DigitalTelcoEnquiryViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                       private val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    private val _enquiryResult = MutableLiveData<Result<TelcoEnquiryData>>()
    val enquiryResult: LiveData<Result<TelcoEnquiryData>>
        get() = _enquiryResult

    fun getEnquiry(rawQuery: String, productId: String, clientNumber: String) {
        launchCatchError(block = {
            val enquiryParams = mutableListOf<TopupBillsEnquiryQuery>()
            enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_SOURCE_TYPE, ENQUIRY_PARAM_SOURCE_TYPE_DEFAULT_VALUE))
            enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_DEVICE_ID, ENQUIRY_PARAM_DEVICE_ID_DEFAULT_VALUE))
            enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_PRODUCT_ID, productId))
            enquiryParams.add(TopupBillsEnquiryQuery(ENQUIRY_PARAM_CLIENT_NUMBER, clientNumber))

            val params = mapOf(PARAM_FIELDS to enquiryParams)
            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, TelcoEnquiryData::class.java, params)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<TelcoEnquiryData>()

            val result = if (data?.enquiry != null && data.enquiry.attributes != null) {
                Success(data)
            } else {
                Fail(MessageErrorException(NULL_RESPONSE))
            }
            _enquiryResult.postValue(result)
        }) {
            var throwable = it

            throwable.message?.contains(GRPC_ERROR_MSG_RESPONSE, true)?.let { containsGrpc ->
                if (containsGrpc) {
                    throwable = MessageErrorException(GRPC_ERROR_MSG_RESPONSE)
                }
            }
            _enquiryResult.postValue(Fail(throwable))
        }
    }

    companion object {
        const val NULL_RESPONSE = "null response"
        const val GRPC_ERROR_MSG_RESPONSE = "grpc timeout"

        const val PARAM_FIELDS = "fields"

        const val ENQUIRY_PARAM_DEVICE_ID = "device_id"
        const val ENQUIRY_PARAM_DEVICE_ID_DEFAULT_VALUE = "5"
        const val ENQUIRY_PARAM_SOURCE_TYPE = "source_type"
        const val ENQUIRY_PARAM_SOURCE_TYPE_DEFAULT_VALUE = "c20ad4d76fe977"
        const val ENQUIRY_PARAM_PRODUCT_ID = "product_id"
        const val ENQUIRY_PARAM_CLIENT_NUMBER = "client_number"
    }
}