package com.tokopedia.buyerorder.detail.revamp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.buyerorder.detail.data.ActionButton
import com.tokopedia.buyerorder.detail.data.DetailsData
import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.detail.data.SendEventEmail
import com.tokopedia.buyerorder.detail.domain.OmsDetailUseCase
import com.tokopedia.buyerorder.detail.domain.RevampActionButtonUseCase
import com.tokopedia.buyerorder.detail.domain.SendEventNotificationUseCase
import com.tokopedia.buyerorder.detail.revamp.util.Utils.Const.KEY_REFRESH
import com.tokopedia.buyerorder.detail.revamp.viewModel.uiEvent.ActionButtonEventWrapper
import com.tokopedia.buyerorder.detail.revamp.viewModel.uiEvent.UiEvent
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import java.lang.reflect.Type
import javax.inject.Inject

/**
 * created by @bayazidnasir on 19/8/2022
 */

class OrderDetailViewModel @Inject constructor(
    private val omsDetailUseCase: Lazy<OmsDetailUseCase>,
    private val actionButtonUseCase: Lazy<RevampActionButtonUseCase>,
    private val eventNotificationUseCase: Lazy<SendEventNotificationUseCase>,
    private val gson: Lazy<Gson>,
    dispatcher: CoroutineDispatcher,
): BaseViewModel(dispatcher) {

    private val _omsDetail = MutableLiveData<UiEvent<DetailsData>>()
    val omsDetail: LiveData<UiEvent<DetailsData>>
        get() = _omsDetail

    private val _actionButton = MutableLiveData<ActionButtonEventWrapper>()
    val actionButton: LiveData<ActionButtonEventWrapper>
        get() = _actionButton

    private val _eventEmail = MutableLiveData<UiEvent<SendEventEmail>>()
    val eventEmail: LiveData<UiEvent<SendEventEmail>>
        get() = _eventEmail

    private val _actionClickable = MutableLiveData<Boolean>()
    val actionClickable: LiveData<Boolean>
        get() = _actionClickable

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var orderDetails : OrderDetails? = null

    fun requestOmsDetail(orderId: String, orderCategory: String, upstream: String?){
        _omsDetail.postValue(UiEvent.Loading)
        launchCatchError(
            block = {
                omsDetailUseCase.get().createParams(orderId, orderCategory, upstream)
                val result = omsDetailUseCase.get().executeOnBackground()
                orderDetails = result.orderDetails
                _omsDetail.postValue(UiEvent.Success(result))
            },
            onError = {
                _omsDetail.postValue(UiEvent.Fail(it))
            }
        )
    }

    fun requestActionButton(actionButton: List<ActionButton>, position: Int, flag: Boolean, isCalledFromAdapter: Boolean){
        launchCatchError(
            block = {
                actionButtonUseCase.get().setParams(actionButton)
                val result = actionButtonUseCase.get().executeOnBackground()

                if (isCalledFromAdapter){
                    if (flag){
                        _actionButton.postValue(ActionButtonEventWrapper.TapActionButton(position, result.actionButtonList))
                        result.actionButtonList.forEachIndexed { index, it ->
                            if (it.control.equals(KEY_REFRESH, true)){
                                it.body = actionButton[index].body
                            }
                        }
                    } else {
                        _actionButton.postValue(ActionButtonEventWrapper.SetActionButton(position, result.actionButtonList))
                    }
                } else {
                    _actionButton.postValue(ActionButtonEventWrapper.RenderActionButton(result.actionButtonList))
                }
            },
            onError = {
                _errorMessage.postValue(it.message)
            }
        )
    }

    fun sendEventEmail(actionButton: ActionButton, metadata: String){
        eventNotificationUseCase.get().apply {
            path = actionButton.uri
            body = metadata

            _eventEmail.postValue(UiEvent.Loading)

            execute(object : Subscriber<Map<Type, RestResponse>>(){
                override fun onCompleted() {/*no op*/}

                override fun onError(e: Throwable?) {
                    e?.let { _eventEmail.postValue(UiEvent.Fail(it)) }
                }

                override fun onNext(t: Map<Type, RestResponse>?) {
                    val token = object : TypeToken<SendEventEmail>() {}.type
                    val response = t?.get(token)
                    _actionClickable.postValue(false)
                    if (response?.code == RESPONSE_SUCCESS_CODE && response.errorBody == null) {
                        val result = response.getData<SendEventEmail>()
                        _eventEmail.postValue(UiEvent.Success(result))
                    } else {
                        val result = gson.get().fromJson(response?.errorBody, SendEventEmail::class.java)
                        _eventEmail.postValue(UiEvent.Fail(MessageErrorException(result.data.message)))
                    }
                }
            })
        }
    }

    private companion object {
        const val RESPONSE_SUCCESS_CODE = 200
    }

}