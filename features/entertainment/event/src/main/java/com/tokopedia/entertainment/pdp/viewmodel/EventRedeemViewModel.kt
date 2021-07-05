package com.tokopedia.entertainment.pdp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.entertainment.pdp.data.redeem.ErrorRedeem
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeem
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeemedData
import com.tokopedia.entertainment.pdp.network_api.GetEventRedeemUseCase
import com.tokopedia.entertainment.pdp.network_api.RedeemTicketEventUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

class EventRedeemViewModel @Inject constructor(private val dispatcher: CoroutineDispatcher,
                                               private val usecase: GetEventRedeemUseCase,
                                               private val useCaseRedeem: RedeemTicketEventUseCase
) : BaseViewModel(dispatcher) {

    private val eventRedeemMutable = MutableLiveData<EventRedeem>()
    val eventRedeem: LiveData<EventRedeem>
        get() = eventRedeemMutable

    private val eventRedeemedMutable = MutableLiveData<EventRedeemedData>()
    val eventRedeemed: LiveData<EventRedeemedData>
        get() = eventRedeemedMutable

    private val isErrorMutable = MutableLiveData<Throwable>()
    val isError: LiveData<Throwable>
        get() = isErrorMutable


    fun getDataRedeem(urlRedeem : String) {
        launchCatchError(block = {
            usecase.setUrlRedeem(urlRedeem)
           withContext(dispatcher){
                val data = usecase.executeOnBackground()
                val value = data[EventRedeem::class.java]
                if (value?.code == 200 && !value.isError) {
                    val result = convertToRedeemResponse(data)
                    eventRedeemMutable.value = result
                } else {
                    val errorMessage = convertToErrorResponse(data)
                    isErrorMutable.value = MessageErrorException(errorMessage)
                }
            }
        }, onError = {
            isErrorMutable.value = it
        })
    }

    fun redeemData(urlRedeem: String){
        launchCatchError(block = {
            useCaseRedeem.setUrlRedeem(urlRedeem)
            withContext(dispatcher){
                val data = useCaseRedeem.executeOnBackground()
                val value = data[EventRedeemedData::class.java]
                if (value?.code == 200 && !value.isError) {
                    val result = convertToRedeemedResponse(data)
                    eventRedeemedMutable.value = result
                } else {
                    val errorMessage = convertToErrorResponse(data)
                    isErrorMutable.value = MessageErrorException(errorMessage)
                }
            }
        }, onError = {
            isErrorMutable.value = it
        })
    }

    private fun convertToRedeemResponse(typeRestResponseMap: Map<Type, RestResponse?>): EventRedeem {
        return typeRestResponseMap[EventRedeem::class.java]?.getData() as EventRedeem
    }

    private fun convertToErrorResponse(typeRestResponseMap: Map<Type, RestResponse?>): String? {
        val errorBody = typeRestResponseMap[EventRedeem::class.java]?.errorBody ?: ""
        val errorRedeem = Gson().fromJson(errorBody, ErrorRedeem::class.java)
        return errorRedeem.messageError.firstOrNull()
    }

    private fun convertToRedeemedResponse(typeRestResponseMap: Map<Type, RestResponse?>): EventRedeemedData {
        return typeRestResponseMap[EventRedeemedData::class.java]?.getData() as EventRedeemedData
    }
}