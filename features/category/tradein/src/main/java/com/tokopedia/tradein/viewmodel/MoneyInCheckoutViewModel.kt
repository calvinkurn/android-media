package com.tokopedia.tradein.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.model.MoneyInCheckoutMutationResponse.ResponseData.CheckoutGeneral.CheckoutData
import com.tokopedia.tradein.model.MoneyInCourierResponse.ResponseData.RatesV4
import com.tokopedia.tradein.model.MoneyInScheduleOptionResponse.ResponseData.GetPickupScheduleOption
import com.tokopedia.tradein.usecase.MoneyInCheckoutUseCase
import com.tokopedia.tradein.usecase.MoneyInCourierRatesUseCase
import com.tokopedia.tradein.usecase.MoneyInPickupScheduleUseCase
import com.tokopedia.tradein.viewmodel.liveState.CourierPriceError
import com.tokopedia.tradein.viewmodel.liveState.MoneyInCheckoutState
import com.tokopedia.tradein.viewmodel.liveState.MutationCheckoutError
import com.tokopedia.tradein.viewmodel.liveState.ScheduleTimeError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


class MoneyInCheckoutViewModel @Inject constructor(
        private val moneyInPickupScheduleUseCase: MoneyInPickupScheduleUseCase,
        private val moneyInCourierRatesUseCase: MoneyInCourierRatesUseCase,
        private val moneyInCheckoutUseCase: MoneyInCheckoutUseCase
) : BaseTradeInViewModel(), CoroutineScope {

    private val pickupScheduleOptionLiveData = MutableLiveData<Result<GetPickupScheduleOption>>()
    private val courierRatesLiveData = MutableLiveData<Result<RatesV4.Data>>()
    private val checkoutDataLiveData = MutableLiveData<Result<CheckoutData.Data>>()
    private var errorLiveData: MutableLiveData<MoneyInCheckoutState> = MutableLiveData()

    companion object{
        private const val SUCCESS = 1
    }


    fun getPickupScheduleOption() {
        launchCatchError(block = {
            val response = moneyInPickupScheduleUseCase.getPickupScheduleOption(getResource())
            pickupScheduleOptionLiveData.value = Success(response.getPickupScheduleOption!!)
        }, onError = {
            it.printStackTrace()
            errorLiveData.value = ScheduleTimeError(it.localizedMessage ?: "")
        })
    }

    fun getCourierRates(destination: String) {
        launchCatchError(block = {
            val response = moneyInCourierRatesUseCase.getCourierRates(getResource(), destination)
            courierRatesLiveData.value = Success(response.ratesV4.data)
        }, onError = {
            it.printStackTrace()
            errorLiveData.value = CourierPriceError(it.localizedMessage ?: "")
        })
    }

    fun makeCheckoutMutation(hardwareId: String, addressId : Int, spId: Int, pickupTimeStart: Int, pickupTimeEnd: Int) {
        launchCatchError(block = {
            val response = moneyInCheckoutUseCase.makeCheckoutMutation(getResource(),
                    moneyInCheckoutUseCase.createRequestParams(hardwareId, addressId, spId, pickupTimeStart, pickupTimeEnd))
            if (response.checkoutGeneral.data.success == SUCCESS) {
                checkoutDataLiveData.value = Success(response.checkoutGeneral.data.data)
            } else {
                errorLiveData.value = MutationCheckoutError(response.checkoutGeneral.header.messages.joinToString())
            }
        }, onError = {
            it.printStackTrace()
            errorLiveData.value = MutationCheckoutError(it.localizedMessage ?: "")
        })
    }

    fun getPickupScheduleOptionLiveData(): MutableLiveData<Result<GetPickupScheduleOption>> = pickupScheduleOptionLiveData
    fun getCourierRatesLiveData(): MutableLiveData<Result<RatesV4.Data>> = courierRatesLiveData
    fun getCheckoutDataLiveData(): MutableLiveData<Result<CheckoutData.Data>> = checkoutDataLiveData
    fun getErrorLiveData(): MutableLiveData<MoneyInCheckoutState> = errorLiveData
}
