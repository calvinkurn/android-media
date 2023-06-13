package com.tokopedia.entertainment.pdp.viewmodel

import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.entertainment.pdp.common.util.EventRedeemMapper
import com.tokopedia.entertainment.pdp.data.redeem.ErrorRedeem
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeem
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeemedData
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.Participant
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.RedeemRequest
import com.tokopedia.entertainment.pdp.network_api.GetEventRedeemUseCase
import com.tokopedia.entertainment.pdp.network_api.GetEventRedeemedUseCase
import com.tokopedia.entertainment.pdp.network_api.RedeemTicketEventUseCase
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
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

/**
 * Author firmanda on 17,Nov,2022
 */

class EventRedeemRevampViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getEventRedeemUseCase: GetEventRedeemUseCase,
    private val getEventRedeemedUseCase: GetEventRedeemedUseCase,
    private val getEventOldRedeemUseCase: RedeemTicketEventUseCase
) : BaseViewModel(dispatcher.main) {

    private val _inputRedeemUrl = MutableSharedFlow<String>(Int.ONE)
    private val _inputRedeemedUrl = MutableSharedFlow<String>(Int.ONE)
    private val _inputOldRedeemedUrl = MutableSharedFlow<String>(Int.ONE)
    var listRedemptions: List<Participant> = mutableListOf()
    var oldFlowQuantity: Int = Int.ZERO

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

    val flowRedeem: SharedFlow<Result<EventRedeemedData>> =
        _inputRedeemedUrl.flatMapConcat {
            flow {
                emit(redeemIds(it))
            }.catch {
                emit(Fail(it))
            }
        }.shareIn(
            scope = this,
            started = SharingStarted.WhileSubscribed(SHARED_FLOW_STOP_TIMEOUT_MILLIS),
            replay = Int.ONE
        )

    val flowOldRedeem: SharedFlow<Result<EventRedeemedData>> =
        _inputOldRedeemedUrl.flatMapConcat {
            flow {
                emit(oldRedeem(it))
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

    fun setInputRedeemedUrl(redeemUrl: String) {
        _inputRedeemedUrl.tryEmit(redeemUrl)
    }

    fun setInputOldRedeemedUrl(redeemUrl: String) {
        _inputOldRedeemedUrl.tryEmit(redeemUrl)
    }

    fun updateCheckedIds(listCheckedIds: List<Pair<String, Boolean>>) {
        EventRedeemMapper.updateCheckedIds(listRedemptions, listCheckedIds)
    }

    fun getCheckedIdsSize(): Int {
        return EventRedeemMapper.getCheckedIdsSize(listRedemptions)
    }

    private suspend fun getRedeemData(redeemUrl: String): Result<EventRedeem> {
        getEventRedeemUseCase.setUrlRedeem(redeemUrl)
        val response = withContext(dispatcher.io) {
            getEventRedeemUseCase.executeOnBackground()
        }
        val value = response[EventRedeem::class.java]

        return if (value?.code == SUCCESS_CODE && !value.isError) {
            val convertedResponse = convertToRedeemResponse(value)
            updateListRedemptionAndQuantity(convertedResponse)
            Success(convertedResponse)
        } else {
            val error = convertToErrorResponse(response)
            Fail(MessageErrorException(error))
        }
    }

    private suspend fun redeemIds(redeemUrl: String): Result<EventRedeemedData> {
        getEventRedeemedUseCase.setUrlRedeem(redeemUrl)
        getEventRedeemedUseCase.setRedeemIds(RedeemRequest(getCheckedIds()))
        val response = withContext(dispatcher.io) {
            getEventRedeemedUseCase.executeOnBackground()
        }
        val value = response[EventRedeemedData::class.java]

        return if (value?.code == SUCCESS_CODE && !value.isError) {
            Success(convertToRedeemedResponse(value))
        } else {
            val error = convertToErrorResponseRedeemed(response)
            Fail(MessageErrorException(error))
        }
    }

    private suspend fun oldRedeem(redeemUrl: String): Result<EventRedeemedData> {
        getEventOldRedeemUseCase.setUrlRedeem(redeemUrl)
        val response = withContext(dispatcher.io) {
            getEventOldRedeemUseCase.executeOnBackground()
        }
        val value = response[EventRedeemedData::class.java]

        return if (value?.code == SUCCESS_CODE && !value.isError) {
            Success(convertToRedeemedResponse(value))
        } else {
            val error = convertToErrorResponseRedeemed(response)
            Fail(MessageErrorException(error))
        }
    }

    private fun updateListRedemptionAndQuantity(redeemData: EventRedeem) {
        listRedemptions = redeemData.data.redemptions ?: emptyList()
        oldFlowQuantity = redeemData.data.quantity
    }

    private fun getCheckedIds(): List<Int> {
        return EventRedeemMapper.getCheckedIds(listRedemptions)
    }

    private fun convertToRedeemResponse(restResponse: RestResponse): EventRedeem {
        return restResponse.getData() as EventRedeem
    }

    private fun convertToRedeemedResponse(restResponse: RestResponse): EventRedeemedData {
        return restResponse.getData() as EventRedeemedData
    }

    private fun convertToErrorResponse(typeRestResponseMap: Map<Type, RestResponse?>): String? {
        val errorBody = typeRestResponseMap[EventRedeem::class.java]?.errorBody ?: ""
        val errorRedeem = Gson().fromJson(errorBody, ErrorRedeem::class.java)
        return errorRedeem.messageError.firstOrNull()
    }

    private fun convertToErrorResponseRedeemed(typeRestResponseMap: Map<Type, RestResponse?>): String? {
        val errorBody = typeRestResponseMap[EventRedeemedData::class.java]?.errorBody ?: ""
        val errorRedeem = Gson().fromJson(errorBody, ErrorRedeem::class.java)
        return errorRedeem.messageError.firstOrNull()
    }

    companion object{
        private const val SUCCESS_CODE = 200
        private const val SHARED_FLOW_STOP_TIMEOUT_MILLIS = 5000L
    }
}
