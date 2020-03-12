package com.tokopedia.recharge_credit_card.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recharge_credit_card.data.RechargeCCRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RechargeSubmitCCViewModel @Inject constructor(private val dispatcher: CoroutineDispatcher,
                                                    private val repository: RechargeCCRepository)
    : BaseViewModel(dispatcher) {

    val errorSubmitCreditCard = MutableLiveData<String>()
    val redirectUrl = MutableLiveData<String>()

    fun postCreditCard(clientNumber: String, operatorId: String, productId: String, userId: String, signature: String) {
        launchCatchError(block = {
            val mapParam = HashMap<String, String>()
            mapParam[PARAM_CLIENT_NUMBER] = clientNumber
            mapParam[PARAM_OPERATOR_ID] = operatorId
            mapParam[PARAM_PRODUCT_ID] = productId
            mapParam[PARAM_USER_ID] = userId
            mapParam[PARAM_PCIDSS] = signature

            val data = withContext(dispatcher) {
                repository.postCreditCardNumber(mapParam)
            }

            if (data.redirectUrl.isNotEmpty()) {
                redirectUrl.postValue(data.redirectUrl)
            } else {
                errorSubmitCreditCard.postValue(data.messageError)
            }
        }) {
            errorSubmitCreditCard.postValue(it.message)
        }
    }

    companion object {
        const val PARAM_CLIENT_NUMBER = "client_number"
        const val PARAM_OPERATOR_ID = "operator_id"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_USER_ID = "user_id"
        const val PARAM_PCIDSS = "pcidss_signature"
    }
}