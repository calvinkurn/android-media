package com.tokopedia.entertainment.pdp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeem
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeemedData
import com.tokopedia.entertainment.pdp.network_api.GetEventRedeemUseCase
import com.tokopedia.entertainment.pdp.network_api.RedeemTicketEventUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
            val result = withContext(dispatcher){
                convertToRedeemResponse(usecase.executeOnBackground())
            }
            eventRedeemMutable.value = result
        }, onError = {
            isErrorMutable.value = it
        })
    }

    fun redeemData(urlRedeem: String){
        launchCatchError(block = {
            useCaseRedeem.setUrlRedeem(urlRedeem)
            val result = withContext(dispatcher){
                convertToRedeemedResponse(useCaseRedeem.executeOnBackground())
            }
            eventRedeemedMutable.value = result
        }, onError = {
            isErrorMutable.value = it
        })
    }

    private fun convertToRedeemResponse(typeRestResponseMap: Map<Type, RestResponse?>): EventRedeem {
        return typeRestResponseMap[EventRedeem::class.java]?.getData() as EventRedeem
    }

    private fun convertToRedeemedResponse(typeRestResponseMap: Map<Type, RestResponse?>): EventRedeemedData {
        return typeRestResponseMap[EventRedeemedData::class.java]?.getData() as EventRedeemedData
    }
}