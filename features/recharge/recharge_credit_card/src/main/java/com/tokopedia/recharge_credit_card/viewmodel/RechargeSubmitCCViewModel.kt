package com.tokopedia.recharge_credit_card.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recharge_credit_card.datamodel.CCRedirectUrlResponse
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCSignatureReponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject

class RechargeSubmitCCViewModel @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val dispatcher: CoroutineDispatcher
) :
    BaseViewModel(dispatcher) {

    private val _signature = MutableLiveData<String>()
    private val _errorSignature = MutableLiveData<Throwable>()

    val errorSignature: LiveData<Throwable> = _errorSignature
    val signature: LiveData<String> = _signature

    fun postCreditCard(
        rawQuery: String,
        categoryId: String
    ) {
        launchCatchError(block = {
            val mapParam = mutableMapOf<String, Any>()
            mapParam[CATEGORY_ID] = categoryId.toIntSafely()

            val data = withContext(dispatcher) {
                val graphqlRequest = GraphqlRequest(rawQuery, RechargeCCSignatureReponse::class.java, mapParam)
                graphqlRepository.response(listOf(graphqlRequest))
            }.getSuccessData<RechargeCCSignatureReponse>()

            if (data.rechargeSignature.messageError.isEmpty()) {
                _signature.postValue(data.rechargeSignature.signature)
            } else {
                _errorSignature.postValue(MessageErrorException(data.rechargeSignature.messageError))
            }
        }) {
            _errorSignature.postValue(it)
        }
    }

    companion object {
        private const val CATEGORY_ID = "categoryId"
        const val PARAM_PCIDSS = "pcidss_signature"

        fun convertCCResponse(typeRestResponseMap: Map<Type, RestResponse?>): CCRedirectUrlResponse? {
            return typeRestResponseMap[CCRedirectUrlResponse::class.java]?.getData() as CCRedirectUrlResponse?
        }
    }
}
