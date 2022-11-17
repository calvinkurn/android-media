package com.tokopedia.entertainment.pdp.viewmodel

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.entertainment.pdp.data.redeem.ErrorRedeem
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeem
import com.tokopedia.entertainment.pdp.network_api.GetEventRedeemUseCase
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject

class EventRedeemRevampViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getEventRedeemUseCase: GetEventRedeemUseCase
) : BaseViewModel(dispatcher.main) {

    private val _inputRedeemUrl = MutableSharedFlow<String>(Int.ONE)

    val flowRedeemData: SharedFlow<Result<EventRedeem>> =
        _inputRedeemUrl.flatMapConcat {
            flow {
                emit(getRedeemData(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    fun setInputRedeemUrl(redeemUrl: String) {
        _inputRedeemUrl.tryEmit(redeemUrl)
    }

    private suspend fun getRedeemData(redeemUrl: String): Result<EventRedeem> {
        getEventRedeemUseCase.setUrlRedeem(redeemUrl)
        val response = withContext(dispatcher.io) {
            getEventRedeemUseCase.executeOnBackground()
        }
        val value = response[EventRedeem::class.java]

        return if (value?.code == SUCCESS_CODE && !value.isError) {
            Success(convertToRedeemResponse(response))
        } else {
            val error = convertToErrorResponse(response)
            Fail(MessageErrorException(error))
        }
    }

    private fun convertToRedeemResponse(typeRestResponseMap: Map<Type, RestResponse?>): EventRedeem {
        return typeRestResponseMap[EventRedeem::class.java]?.getData() as EventRedeem
    }

    private fun convertToErrorResponse(typeRestResponseMap: Map<Type, RestResponse?>): String? {
        val errorBody = typeRestResponseMap[EventRedeem::class.java]?.errorBody ?: ""
        val errorRedeem = Gson().fromJson(errorBody, ErrorRedeem::class.java)
        return errorRedeem.messageError.firstOrNull()
    }

    companion object{
        private const val SUCCESS_CODE = 200
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
    }
}
