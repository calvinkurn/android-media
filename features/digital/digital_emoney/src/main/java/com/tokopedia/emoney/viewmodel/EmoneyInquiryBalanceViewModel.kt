package com.tokopedia.emoney.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.emoney.NFCUtils
import com.tokopedia.emoney.data.AttributesEmoneyInquiry
import com.tokopedia.emoney.data.RechargeEmoneyInquiry
import com.tokopedia.emoney.data.RechargeEmoneyInquiryError
import com.tokopedia.emoney.view.fragment.EmoneyCheckBalanceNFCFragment
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EmoneyInquiryBalanceViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                        val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    fun getEmoneyInquiryBalance(paramCommand: String, rawQuery: String, idCard: Int,
                                mapAttributesParam: HashMap<String, Any>,
                                onSuccess: (HashMap<String, Any>, RechargeEmoneyInquiry) -> Unit,
                                onError: (Throwable) -> Unit) {
//        launchCatchError(block = {
//            var mapParam = HashMap<String, kotlin.Any>()
//            mapParam.put(TYPE_CARD, paramCommand)
//            mapParam.put(ID_CARD, idCard)
//            mapParam.put(ATTRIBUTES_CARD, mapAttributesParam)
//
//            val data = withContext(Dispatchers.Default) {
//                val graphqlRequest = GraphqlRequest(rawQuery, RechargeEmoneyInquiryResponse::class.java, mapParam)
//                graphqlRepository.getReseponse(listOf(graphqlRequest))
//            }.getSuccessData<RechargeEmoneyInquiryResponse>()
//
//            data.rechargeEmoneyInquiry.attributesEmoneyInquiry?.let {
//                it.formattedCardNumber = NFCUtils.formatCardUID(it.cardNumber)
//            }
//            onSuccess(mapAttributesParam, data.rechargeEmoneyInquiry)
//        }) {
//            onError(it)
//        }
        onSuccess(mapAttributesParam, successMockData())
    }

    private fun successMockData(): RechargeEmoneyInquiry {
        return RechargeEmoneyInquiry(
                "1",
                "",
                AttributesEmoneyInquiry(
                        "Top Up",
                        "6032984075486468",
                        "https://ecs7.tokopedia.net/img/recharge/operator/e-money.png",
                        26000,
                        "",
                        1,
                        NFCUtils.formatCardUID("6032984075486468"),
                        EmoneyCheckBalanceNFCFragment.ISSUER_ID_EMONEY,
                        EmoneyCheckBalanceNFCFragment.ETOLL_EMONEY_OPERATOR_ID
                ), RechargeEmoneyInquiryError(
                1010,
                "Tidak ada pending balance",
                0
        )
        )
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
        const val PARAM_LAST_BALANCE = "last_balance"
        const val PARAM_PAYLOAD = "payload"
    }
}