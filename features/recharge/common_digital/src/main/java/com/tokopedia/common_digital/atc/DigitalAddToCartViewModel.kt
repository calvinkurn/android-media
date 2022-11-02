package com.tokopedia.common_digital.atc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.atc.data.response.ErrorAtc
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.DigitalAtcErrorException
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by jessica on 04/03/21
 */

class DigitalAddToCartViewModel @Inject constructor(
    private val digitalAddToCartUseCase: DigitalAddToCartUseCase,
    private val userSession: UserSessionInterface,
    private val dispatcher: CoroutineDispatcher,
    private val rechargeAnalytics: RechargeAnalytics
) : BaseViewModel(dispatcher) {

    private val _addToCartResult = MutableLiveData<Result<String>>()
    val addToCartResult: LiveData<Result<String>>
        get() = _addToCartResult

    private val _errorAtc = MutableLiveData<ErrorAtc>()
    val errorAtc: LiveData<ErrorAtc>
        get() = _errorAtc


    fun addToCart(
        digitalCheckoutPassData: DigitalCheckoutPassData,
        digitalIdentifierParam: RequestBodyIdentifier,
        digitalSubscriptionParams: DigitalSubscriptionParams,
        isUseGql: Boolean
    ) {
        if (!userSession.isLoggedIn) {
            _addToCartResult.postValue(Fail(MessageErrorException(MESSAGE_ERROR_NON_LOGIN)))
        } else {
            launchCatchError(block = {
                val data = withContext(dispatcher) {
                    digitalAddToCartUseCase.execute(
                        digitalCheckoutPassData,
                        userSession.userId,
                        digitalIdentifierParam,
                        digitalSubscriptionParams,
                        digitalCheckoutPassData.idemPotencyKey,
                        isUseGql
                    )
                }

                data?.let {
                    rechargeAnalytics.eventAddToCart(it)
                    if (it.atcError != null) {
                        _errorAtc.postValue(it.atcError)
                    } else {
                        if (it.categoryId.isNotEmpty()) {
                            _addToCartResult.postValue(Success(it.categoryId))
                        } else {
                            _addToCartResult.postValue(Success(digitalCheckoutPassData.categoryId ?: ""))
                        }
                    }
                    return@launchCatchError
                }
                _addToCartResult.postValue(Fail(Throwable(DigitalFailGetCartId())))
            }) {
                if (it is ResponseErrorException && !it.message.isNullOrEmpty()) {
                    _addToCartResult.postValue(Fail(MessageErrorException(it.message)))
                } else if (it is DigitalAtcErrorException) {
                    _errorAtc.postValue(it.getError())
                } else {
                    _addToCartResult.postValue(Fail(it))
                }
            }
        }
    }

    class DigitalFailGetCartId : Exception()

    companion object {
        const val MESSAGE_ERROR_NON_LOGIN = "Silakan login terlebih dahulu"
    }
}