package com.tokopedia.recharge_credit_card.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recharge_credit_card.data.RechargeCCRepository
import com.tokopedia.recharge_credit_card.datamodel.RechargeCCSignatureReponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeSubmitCCViewModel @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                    private val dispatcher: CoroutineDispatcher,
                                                    private val repository: RechargeCCRepository)
    : BaseViewModel(dispatcher) {

    private val _errorSubmitCreditCard = MutableLiveData<String>()
    private val _redirectUrl = MutableLiveData<String>()
    private val _errorSignature = MutableLiveData<String>()

    val errorSubmitCreditCard: LiveData<String> = _errorSubmitCreditCard
    val redirectUrl: LiveData<String> = _redirectUrl
    val errorSignature: LiveData<String> = _errorSignature

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
                _errorSignature.postValue(data.rechargeSignature.messageError)
            }
        }) {
            _errorSignature.postValue(it.message)
        }
    }

    private fun submitCreditCard(mapParam: HashMap<String, String>) {
        launchCatchError(block = {
            val data = withContext(dispatcher) {
                repository.postCreditCardNumber(mapParam)
            }

            if (data.redirectUrl.isNotEmpty()) {
                _redirectUrl.postValue(data.redirectUrl)
            } else {
                _errorSubmitCreditCard.postValue(data.messageError)
            }
        }) {
            _errorSubmitCreditCard.postValue(it.message)
        }
    }

    fun createMapParam(clientNumber: String, operatorId: String,
                       productId: String, userId: String): HashMap<String, String> {
        val mapParam = HashMap<String, String>()
        mapParam[PARAM_CLIENT_NUMBER] = clientNumber
        mapParam[PARAM_OPERATOR_ID] = operatorId
        mapParam[PARAM_PRODUCT_ID] = productId
        mapParam[PARAM_USER_ID] = userId
        return mapParam
    }

    companion object {
        private const val CATEGORY_ID = "categoryId"
        private const val PARAM_CLIENT_NUMBER = "client_number"
        private const val PARAM_OPERATOR_ID = "operator_id"
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_USER_ID = "user_id"
        const val PARAM_PCIDSS = "pcidss_signature"
    }
}