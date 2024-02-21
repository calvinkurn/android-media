package com.tokopedia.common_digital.atc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common_digital.atc.data.response.ErrorAtc
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData.Companion.PARAM_ATC_MULTICHECKOUT
import com.tokopedia.common_digital.common.DigitalAtcErrorException
import com.tokopedia.common_digital.common.RechargeAnalytics
import com.tokopedia.common_digital.common.presentation.model.DigitalAtcTrackingModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
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

    private var atcMultiCheckoutParam: String = ""

    private val _addToCartResult = MutableLiveData<Result<String>>()
    val addToCartResult: LiveData<Result<String>>
        get() = _addToCartResult

    private val _addToCartMultiCheckoutResult = MutableLiveData<DigitalAtcTrackingModel>()
    val addToCartMultiCheckoutResult: LiveData<DigitalAtcTrackingModel>
        get() = _addToCartMultiCheckoutResult

    private val _errorAtc = MutableLiveData<ErrorAtc>()
    val errorAtc: LiveData<ErrorAtc>
        get() = _errorAtc

    fun addToCart(
        digitalCheckoutPassData: DigitalCheckoutPassData,
        digitalIdentifierParam: RequestBodyIdentifier
    ) {
        if (!userSession.isLoggedIn) {
            _addToCartResult.postValue(Fail(MessageErrorException(MESSAGE_ERROR_NON_LOGIN)))
        } else {
            launch {
                runCatching {
                    val data = withContext(dispatcher) {
                        digitalAddToCartUseCase.execute(
                            digitalCheckoutPassData,
                            userSession.userId,
                            digitalIdentifierParam,
                            atcMultiCheckoutParam
                        )
                    }
                    data?.let {
                        if (atcMultiCheckoutParam.isEmpty()) {
                            rechargeAnalytics.eventAddToCart(it)
                        }
                        if (it.atcError != null) {
                            resetAtcMultiCheckoutParam()
                            it.atcError?.let { atcError ->
                                _errorAtc.postValue(atcError)
                            }
                        } else {
                            if (atcMultiCheckoutParam.isNotEmpty()) {
                                resetAtcMultiCheckoutParam()
                                _addToCartMultiCheckoutResult.postValue(it)
                            } else if (it.categoryId.isNotEmpty()) {
                                _addToCartResult.postValue(Success(it.categoryId))
                            } else {
                                _addToCartResult.postValue(
                                    Success(
                                        digitalCheckoutPassData.categoryId ?: ""
                                    )
                                )
                            }
                        }
                        return@launch
                    }
                    _addToCartResult.postValue(Fail(Throwable(DigitalFailGetCartId())))
                }.onFailure {
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
    }

    fun setAtcMultiCheckoutParam() {
        atcMultiCheckoutParam = PARAM_ATC_MULTICHECKOUT
    }

    private fun resetAtcMultiCheckoutParam() {
        atcMultiCheckoutParam = ""
    }

    class DigitalFailGetCartId : Exception()

    companion object {
        const val MESSAGE_ERROR_NON_LOGIN = "Silakan login terlebih dahulu"
    }
}
