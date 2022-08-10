package com.tokopedia.brizzi.viewmodel

import android.content.Intent
import android.util.Log
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.network.authentication.AuthKey
import com.tokopedia.brizzi.data.BrizziInquiryLogResponse
import com.tokopedia.brizzi.data.BrizziTokenResponse
import com.tokopedia.brizzi.mapper.BrizziCardObjectMapper
import com.tokopedia.common_electronic_money.data.EmoneyInquiry
import com.tokopedia.common_electronic_money.data.EmoneyInquiryError
import com.tokopedia.common_electronic_money.util.NfcCardErrorTypeDef
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import id.co.bri.sdk.Brizzi
import id.co.bri.sdk.BrizziCardObject
import id.co.bri.sdk.Callback
import id.co.bri.sdk.exception.BrizziException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class BrizziBalanceViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                 val brizziCardObjectMapper: BrizziCardObjectMapper,
                                                 val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    var inquiryIdBrizzi: Int = 0
    var token: String = ""

    val issuerId = SingleLiveEvent<Int>()
    val emoneyInquiry = SingleLiveEvent<EmoneyInquiry>()
    val tokenNeedRefresh = SingleLiveEvent<Boolean>()
    val cardIsNotBrizzi = SingleLiveEvent<Boolean>()
    val errorCardMessage = SingleLiveEvent<Throwable>()
    val errorCommonBrizzi = SingleLiveEvent<Throwable>()
    val mapLoggerDebugData = HashMap<String, String>()

    fun processBrizziTagIntent(intent: Intent, brizziInstance: Brizzi,
                               rawTokenQuery: String, rawLogBrizzi: String, refreshToken: Boolean,
                               startTimeBeforeTokenCallGql: Long,
                               timeCheckDuration: String) {

        //token on server will refresh automatically per 30 minutes
        launchCatchError(block = {
            mapLoggerDebugData.put(EMONEY_TIME_CHECK_LOGIC_TAG, timeCheckDuration)

            val mapParam = HashMap<String, Any>()
            mapParam[REFRESH_TOKEN] = refreshToken

            val endTimeBeforeCallTokenGql = System.currentTimeMillis()
            mapLoggerDebugData.put(EMONEY_BRI_BEFORE_CALL_TOKEN_TAG, getTimeDifferences(startTimeBeforeTokenCallGql, endTimeBeforeCallTokenGql))

            val startTimeTokenCall = System.currentTimeMillis()
            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawTokenQuery, BrizziTokenResponse::class.java, mapParam)
                graphqlRepository.response(listOf(graphqlRequest))
            }.getSuccessData<BrizziTokenResponse>()

            val endTimeTokenCall = System.currentTimeMillis()
            mapLoggerDebugData.put(EMONEY_BRI_BEFORE_TOKEN_TAG, getTimeDifferences(startTimeTokenCall, endTimeTokenCall))

            val startTimeBeforeCallGql = System.currentTimeMillis()
            if (data.tokenResponse.token != token) {
                token = data.tokenResponse.token
            }
            if (token.isNotEmpty()) {
                //initiate token and secret key
                brizziInstance.Init(token, AuthKey.BRIZZI_CLIENT_SECRET)
                brizziInstance.setUserName(AuthKey.BRIZZI_CLIENT_ID)
            }
            val endTimeBeforeCallGql = System.currentTimeMillis()
            mapLoggerDebugData.put(EMONEY_BRI_BEFORE_CALL_TAG, getTimeDifferences(startTimeBeforeCallGql, endTimeBeforeCallGql))
            val startTimeCallGql = System.currentTimeMillis()
            brizziInstance.getBalanceInquiry(intent, object : Callback {
                override fun OnFailure(brizziException: BrizziException?) {
                    brizziException?.let {
                        handleError(it)
                    }
                }

                override fun OnSuccess(brizziCardObject: BrizziCardObject) {
                    issuerId.postValue(ISSUER_ID_BRIZZI)
                    val balanceInquiry = brizziCardObjectMapper.mapperBrizzi(brizziCardObject, EmoneyInquiryError(title = BRIZZI_SUCCESS_LAST_BALANCE))
                    balanceInquiry.attributesEmoneyInquiry?.let {
                        logBrizzi(0, it.cardNumber, rawLogBrizzi, "success", it.lastBalance.toDouble())

                        val endTimeCallGql = System.currentTimeMillis()
                        mapLoggerDebugData.put(EMONEY_BRI_TIME_CALL_TAG, getTimeDifferences(startTimeCallGql, endTimeCallGql))

                        balanceInquiry.attributesEmoneyInquiry?.let { attributes ->
                            if (attributes.pendingBalance == 0) {
                                logDebugEmoney()
                                emoneyInquiry.postValue(balanceInquiry)
                            } else {
                                writeBalanceToCard(intent, rawLogBrizzi, brizziInstance)
                            }
                        }
                    }
                }
            })
        }) {
            ServerLogger.log(Priority.P2, BRIZZI_TAG, mapOf("err" to "ERROR_FAILED_REFRESH_TOKEN"))
            errorCommonBrizzi.postValue(it)
        }
    }

    private fun logBrizzi(inquiryId: Int, cardNumber: String, rawString: String, status: String, lastBalance: Double) {
        var mapParam = HashMap<String, Any>()
        mapParam.put(ISSUER_ID, ISSUER_ID_BRIZZI)
        mapParam.put(INQUIRY_ID, inquiryId)
        mapParam.put(CARD_NUMBER, cardNumber)
        mapParam.put(RC, status)
        mapParam.put(LAST_BALANCE, lastBalance)

        launchCatchError(block = {
            val mapParamLog = HashMap<String, kotlin.Any>()
            mapParamLog.put(LOG_BRIZZI, mapParam)

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawString, BrizziInquiryLogResponse::class.java, mapParamLog)
                graphqlRepository.response(listOf(graphqlRequest))
            }.getSuccessData<BrizziInquiryLogResponse>()

            data.brizziInquiryLog?.let {
                inquiryIdBrizzi = it.inquiryId.toIntOrZero()
            }
        }) {
            inquiryIdBrizzi = -1
        }
    }

    private fun handleError(brizziException: BrizziException) {
        when (brizziException.errorCode) {
            BRIZZI_TOKEN_EXPIRED -> {
                ServerLogger.log(Priority.P2, BRIZZI_TAG, mapOf("err" to "ERROR_TOKEN_NEED_REFRESH"))
                tokenNeedRefresh.postValue(true)
            }
            BRIZZI_CARD_NOT_FOUND -> {
                ServerLogger.log(Priority.P2, BRIZZI_TAG, mapOf("err" to "ERROR_CARD_IS_NOT_BRIZZI"))
                cardIsNotBrizzi.postValue(true)
            }
            else -> {
                ServerLogger.log(Priority.P2, BRIZZI_TAG, mapOf("err" to (brizziException.message ?: "")))
                errorCardMessage.postValue(MessageErrorException(NfcCardErrorTypeDef.FAILED_READ_CARD))
            }
        }
    }

    private fun writeBalanceToCard(intent: Intent, logRawQuery: String, brizziInstance: Brizzi) {
        val startWriteCard = System.currentTimeMillis()
        brizziInstance.doUpdateBalance(intent, System.currentTimeMillis().toString(), object : Callback {
            override fun OnFailure(brizziException: BrizziException?) {
                brizziException?.let {
                    handleError(it)
                }
            }

            override fun OnSuccess(brizziCardObject: BrizziCardObject) {
                val balanceInquiry = brizziCardObjectMapper.mapperBrizzi(brizziCardObject, EmoneyInquiryError(title = BRIZZI_SUCCESS_UPDATE_BALANCE))

                if (inquiryIdBrizzi > -1) {
                    balanceInquiry.attributesEmoneyInquiry?.let {
                        logBrizzi(inquiryIdBrizzi, it.cardNumber, logRawQuery, "success", it.lastBalance.toDouble())
                    }
                }
                val endWriteCard = System.currentTimeMillis()
                mapLoggerDebugData.put(EMONEY_BRI_TIME_WRITE_TAG, getTimeDifferences(startWriteCard, endWriteCard))
                logDebugEmoney()

                emoneyInquiry.postValue(balanceInquiry)
            }
        })
    }

    private fun logDebugEmoney() {
        mapLoggerDebugData.forEach {
            Log.d(EMONEY_DEBUG_TAG, "${it.key} ${it.value}")
        }
        ServerLogger.log(Priority.P2, EMONEY_DEBUG_TAG, mapLoggerDebugData)
    }

    private fun getTimeDifferences(startTime: Long, endTime: Long): String {
        return "${endTime - startTime} ms"
    }

    companion object {
        const val REFRESH_TOKEN = "refresh"
        const val ISSUER_ID = "issuer_id"
        const val INQUIRY_ID = "inquiry_id"
        const val CARD_NUMBER = "card_number"
        const val RC = "rc"
        const val LAST_BALANCE = "last_balance"
        const val LOG_BRIZZI = "log"
        const val BRIZZI_TAG = "BRIZZI"

        const val ISSUER_ID_BRIZZI = 2
        const val ETOLL_BRIZZI_OPERATOR_ID = "1015"
        const val BRIZZI_TOKEN_EXPIRED = "61"
        const val BRIZZI_CARD_NOT_FOUND = "21"

        const val BRIZZI_SUCCESS_UPDATE_BALANCE = "Oke, saldo kamu berhasil di-update!"
        const val BRIZZI_SUCCESS_LAST_BALANCE = "Ini saldo kamu yang paling baru, ya."

        private const val EMONEY_DEBUG_TAG = "EMONEY_DEBUG"
        private const val EMONEY_TIME_CHECK_LOGIC_TAG = "EMONEY_TIME_CHECK_LOGIC"
        private const val EMONEY_BRI_TIME_WRITE_TAG = "EMONEY_BRI_TIME_WRITE"
        private const val EMONEY_BRI_TIME_CALL_TAG = "EMONEY_BRI_TIME_CALL"
        private const val EMONEY_BRI_BEFORE_CALL_TOKEN_TAG = "EMONEY_BRI_TIME_BEFORE_TOKEN_CALL"
        private const val EMONEY_BRI_BEFORE_CALL_TAG = "EMONEY_BRI_TIME_BEFORE_CALL"
        private const val EMONEY_BRI_BEFORE_TOKEN_TAG = "EMONEY_BRI_TIME_TOKEN_CALL"
    }
}