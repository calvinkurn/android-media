package com.tokopedia.tradein.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.model.MoneyInCheckoutMutationResponse.ResponseData.CheckoutGeneral.CheckoutData
import com.tokopedia.tradein.model.MoneyInCourierResponse.ResponseData.RatesV4
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress
import com.tokopedia.tradein.model.MoneyInScheduleOptionResponse.ResponseData.GetPickupScheduleOption
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import rx.Subscriber
import kotlin.coroutines.CoroutineContext


class MoneyInCheckoutViewModel : BaseViewModel(), CoroutineScope {

    private val addressLiveData = MutableLiveData<Result<KeroGetAddress>>()
    private val pickupScheduleOptionLiveData = MutableLiveData<Result<GetPickupScheduleOption>>()
    private val courierRatesLiveData = MutableLiveData<Result<RatesV4.Data>>()
    private val checkoutDataLiveData = MutableLiveData<Result<CheckoutData.Data>>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun getAddress(query: String) {
        launchCatchError(block = {
            val request = HashMap<String, String>()

            val response = repository?.getGQLData(query, MoneyInKeroGetAddressResponse.ResponseData::class.java, request) as MoneyInKeroGetAddressResponse.ResponseData
            addressLiveData.value = Success(response.keroGetAddress)
        }, onError = {
            it.printStackTrace()
            warningMessage.value = it.localizedMessage
        })
    }

    fun getPickupScheduleOption() {
//        getMoneyInScheduleOptionUseCase.execute(RequestParams(), object : Subscriber<GetPickupScheduleOption>() {
//            override fun onNext(t: GetPickupScheduleOption?) {
//                pickupScheduleOptionLiveData.value = Success((t as GetPickupScheduleOption))
//            }
//
//            override fun onCompleted() {
//
//            }
//
//            override fun onError(e: Throwable) {
//                pickupScheduleOptionLiveData.value = Fail(e)
//            }
//        })
    }

    fun getMoneyInAddressLiveData(): MutableLiveData<Result<KeroGetAddress>> = addressLiveData
    fun getPickupScheduleOptionLiveData(): MutableLiveData<Result<GetPickupScheduleOption>> = pickupScheduleOptionLiveData
    fun getCourierRatesLiveData(): MutableLiveData<Result<RatesV4.Data>> = courierRatesLiveData
    fun getCheckoutDataLiveData(): MutableLiveData<Result<CheckoutData.Data>> = checkoutDataLiveData
}