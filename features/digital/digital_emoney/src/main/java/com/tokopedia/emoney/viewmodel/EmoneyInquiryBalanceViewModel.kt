package com.tokopedia.emoney.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.emoney.NFCUtils
import com.tokopedia.emoney.data.EmoneyInquiry
import com.tokopedia.emoney.data.EmoneyInquiryResponse
import com.tokopedia.emoney.view.electronicmoney.MandiriCheckBalance
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

    fun getEmoneyInquiryBalance(paramCommand: String, rawQuery: String, idCard: Int,
                                mapAttributesParam: HashMap<String, Any>,
                                onSuccess: (HashMap<String, Any>, EmoneyInquiry) -> Unit,
                                onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            var mapParam = HashMap<String, kotlin.Any>()
            mapParam.put(TYPE_CARD, paramCommand)
            mapParam.put(ID_CARD, idCard)
            mapParam.put(ATTRIBUTES_CARD, mapAttributesParam)

            val data = withContext(Dispatchers.IO) {
                val graphqlRequest = GraphqlRequest(rawQuery, EmoneyInquiryResponse::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<EmoneyInquiryResponse>()

            data.emoneyInquiry.attributesEmoneyInquiry?.let {
                it.formattedCardNumber = NFCUtils.formatCardUID(it.cardNumber)
                it.operatorId = MandiriCheckBalance.EMONEY_OPERATOR_ID
            }
            onSuccess(mapAttributesParam, data.emoneyInquiry)
        }) {
            onError(it)
        }
    }

    companion object {
        const val TYPE_CARD = "type"
        const val ID_CARD = "id"
        const val ATTRIBUTES_CARD = "attributes"

        const val PARAM_INQUIRY = "SMARTCARD_INQUIRY"
        const val PARAM_SEND_COMMAND = "SMARTCARD_COMMAND"

        const val PARAM_CARD_UUID = "card_uuid"
        const val PARAM_ISSUER_ID = "card_issuer_id"
        const val PARAM_CARD_ATTRIBUTE = "card_attribute"
        const val PARAM_CARD_INFO = "card_info"
        const val PARAM_LAST_BALANCE = "last_balance"
        const val PARAM_PAYLOAD = "payload"
    }
}