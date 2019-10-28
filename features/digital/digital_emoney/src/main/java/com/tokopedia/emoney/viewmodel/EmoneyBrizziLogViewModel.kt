package com.tokopedia.emoney.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.emoney.data.BrizziInquiryLogResponse
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmoneyBrizziLogViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                   val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    fun logDataBrizzi(rawQuery: String, issuerId: Int, inquiryId: Int, cardNumber: String,
                      rc: String, lastBalance: Int,
                      onSuccess: (Int) -> Unit) {
        launchCatchError(block = {
            var mapParam = HashMap<String, kotlin.Any>()
            mapParam.put(ISSUER_ID, issuerId)
            mapParam.put(INQUIRY_ID, inquiryId)
            mapParam.put(CARD_NUMBER, cardNumber)
            mapParam.put(RC, rc)
            mapParam.put(LAST_BALANCE, lastBalance)

            val data = withContext(Dispatchers.IO) {
                val graphqlRequest = GraphqlRequest(rawQuery, BrizziInquiryLogResponse::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<BrizziInquiryLogResponse>()

            data.emoneyInquiryLog?.let {
                onSuccess(it.inquiryId)
            }
        }) {
        }
    }

    companion object {
        const val ISSUER_ID = "issuer_id"
        const val INQUIRY_ID = "inquiry_id"
        const val CARD_NUMBER = "card_number"
        const val RC = "rc"
        const val LAST_BALANCE = "last_balance"
    }
}