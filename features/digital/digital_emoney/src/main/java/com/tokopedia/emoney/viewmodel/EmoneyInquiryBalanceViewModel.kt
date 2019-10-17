package com.tokopedia.emoney.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.emoney.data.RechargeEmoneyInquiry
import com.tokopedia.emoney.data.RechargeEmoneyInquiryResponse
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmoneyInquiryBalanceViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                        val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    fun getEmoneyInquiryBalance(paramCommand: String, rawQuery: String, idCard: String,
                                mapAttributesParam: HashMap<String, Any>,
                                onSuccess: (HashMap<String, Any>, RechargeEmoneyInquiry) -> Unit,
                                onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            var mapParam = HashMap<String, kotlin.Any>()
            mapParam.put(TYPE_CARD, paramCommand)
            mapParam.put(ID_CARD, idCard)
            mapParam.put(ATTRIBUTES_CARD, mapAttributesParam)

            val data = withContext(Dispatchers.Default) {
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeEmoneyInquiryResponse::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<RechargeEmoneyInquiryResponse>()

            onSuccess(mapAttributesParam, data.rechargeEmoneyInquiry)
        }) {
            onError(it)
        }
    }

    companion object {
        const val TYPE_CARD = "type"
        const val ID_CARD = "id"
        const val ATTRIBUTES_CARD = "attributes"

        const val PARAM_INQUIRY = "smartcard_inquiry"
        const val PARAM_SEND_COMMAND = "smartcard_command"

        const val PARAM_CARD_UUID = "card_uuid"
        const val PARAM_ISSUER_ID = "card_issuer_id"
        const val PARAM_CARD_ATTRIBUTE = "card_attribute"
        const val PARAM_CARD_INFO = "card_info"
    }
}