package com.tokopedia.recharge_credit_card.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recharge_credit_card.datamodel.CCRedirectUrl
import com.tokopedia.recharge_credit_card.datamodel.CCRedirectUrlResponse
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCSignatureReponse
import com.tokopedia.recharge_credit_card.usecase.RechargeSubmitCcUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.internal.http2.ConnectionShutdownException
import java.io.InterruptedIOException
import java.lang.reflect.Type
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject

class RechargeSubmitCCViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                    private val dispatcher: CoroutineDispatcher,
                                                    private val submitCCUseCase: RechargeSubmitCcUseCase)
    : BaseViewModel(dispatcher) {

    private val _errorSubmitCreditCard = MutableLiveData<Throwable>()
    private val _redirectUrl = MutableLiveData<CCRedirectUrl>()
    private val _errorSignature = MutableLiveData<Throwable>()

    val errorSubmitCreditCard: LiveData<Throwable> = _errorSubmitCreditCard
    val redirectUrl: LiveData<CCRedirectUrl> = _redirectUrl
    val errorSignature: LiveData<Throwable> = _errorSignature

    fun postCreditCard(rawQuery: String, categoryId: String, paramSubmitCC: HashMap<String, String>) {
        launchCatchError(block = {
            val mapParam = mutableMapOf<String, Any>()
            mapParam[CATEGORY_ID] = categoryId.toInt()

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeCCSignatureReponse::class.java, mapParam)
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<RechargeCCSignatureReponse>()

            if (data.rechargeSignature.messageError.isEmpty()) {
                paramSubmitCC[PARAM_PCIDSS] = data.rechargeSignature.signature
                submitCreditCard(paramSubmitCC)
            } else {
                _errorSignature.postValue(MessageErrorException(data.rechargeSignature.messageError))
            }
        }) {
            _errorSignature.postValue(it)
        }
    }

    private fun submitCreditCard(mapParam: HashMap<String, String>) {
        launchCatchError(block = {
            val data = withContext(dispatcher) {
                submitCCUseCase.setMapParam(mapParam)
                convertCCResponse(submitCCUseCase.executeOnBackground())
            }

            val ccRedirectUrl = data?.data ?: CCRedirectUrl()
            if (ccRedirectUrl.redirectUrl != "") {
                ccRedirectUrl.clientNumber = mapParam[PARAM_CLIENT_NUMBER] ?: ""
                ccRedirectUrl.operatorId = mapParam[PARAM_OPERATOR_ID] ?: ""
                ccRedirectUrl.productId = mapParam[PARAM_PRODUCT_ID] ?: ""
                _redirectUrl.postValue(ccRedirectUrl)
            } else {
                _errorSubmitCreditCard.postValue(MessageErrorException(ccRedirectUrl.messageError))
            }
        }) {
            _errorSubmitCreditCard.postValue(it)
        }
    }

    fun createMapParam(clientNumber: String, operatorId: String,
                       productId: String, userId: String): HashMap<String, String> {
        val mapParam = HashMap<String, String>()
        mapParam[PARAM_ACTION] = VALUE_ACTION
        mapParam[PARAM_CLIENT_NUMBER] = clientNumber
        mapParam[PARAM_OPERATOR_ID] = operatorId
        mapParam[PARAM_PRODUCT_ID] = productId
        mapParam[PARAM_USER_ID] = userId
        return mapParam
    }

    companion object {
        private const val CATEGORY_ID = "categoryId"
        const val PARAM_CLIENT_NUMBER = "client_number"
        const val PARAM_OPERATOR_ID = "operator_id"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_USER_ID = "user_id"
        const val PARAM_ACTION = "action"
        const val VALUE_ACTION = "init_data"
        const val PARAM_PCIDSS = "pcidss_signature"

        const val ERROR_DEFAULT = "Terjadi kesalahan, silakan ulangi beberapa saat lagi"

        fun convertCCResponse(typeRestResponseMap: Map<Type, RestResponse?>): CCRedirectUrlResponse? {
            return typeRestResponseMap[CCRedirectUrlResponse::class.java]?.getData() as CCRedirectUrlResponse?
        }
    }
}