package com.tokopedia.tradein.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.model.MoneyInCheckoutMutationResponse
import com.tokopedia.tradein.model.MoneyInCheckoutMutationResponse.ResponseData.CheckoutGeneral.CheckoutData
import com.tokopedia.tradein.model.MoneyInCourierResponse
import com.tokopedia.tradein.model.MoneyInCourierResponse.ResponseData.RatesV4
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress
import com.tokopedia.tradein.model.MoneyInScheduleOptionResponse
import com.tokopedia.tradein.model.MoneyInScheduleOptionResponse.ResponseData.GetPickupScheduleOption
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

    fun getPickupScheduleOption(query: String) {
        launchCatchError(block = {
            val request = HashMap<String, String>()

            val response = repository?.getGQLData(query, MoneyInScheduleOptionResponse.ResponseData::class.java, request) as MoneyInScheduleOptionResponse.ResponseData
            pickupScheduleOptionLiveData.value = Success(response.getPickupScheduleOption)
        }, onError = {
            it.printStackTrace()
            warningMessage.value = it.localizedMessage
        })
    }

    fun getCourierRates(query: String, destination: String, orderValue: String) {
        launchCatchError(block = {
            val request = HashMap<String, Any>()
            val input = HashMap<String, String>()
            input["features"] = "money_in"
            input["weight"] = "1"
            input["destination"] = destination
            input["from"] = "client"
            input["type"] = "android"
            input["order_value"] = orderValue
            input["lang"] = "en"
            request["input"] = input

            val response = repository?.getGQLData(query, MoneyInCourierResponse.ResponseData::class.java, request) as MoneyInCourierResponse.ResponseData
            courierRatesLiveData.value = Success(response.ratesV4.data)
        }, onError = {
            it.printStackTrace()
            warningMessage.value = it.localizedMessage
        })
    }

    fun makeCheckoutMutation(query: String) {
        launchCatchError(block = {
            val request = HashMap<String, Any>()

            val response = repository?.getGQLData(query, MoneyInCheckoutMutationResponse.ResponseData::class.java, request) as MoneyInCheckoutMutationResponse.ResponseData
            checkoutDataLiveData.value = Success(response.checkoutGeneral.data.data)
        }, onError = {
            it.printStackTrace()
            warningMessage.value = it.localizedMessage
        })
    }

    fun getMoneyInAddressLiveData(): MutableLiveData<Result<KeroGetAddress>> = addressLiveData
    fun getPickupScheduleOptionLiveData(): MutableLiveData<Result<GetPickupScheduleOption>> = pickupScheduleOptionLiveData
    fun getCourierRatesLiveData(): MutableLiveData<Result<RatesV4.Data>> = courierRatesLiveData
    fun getCheckoutDataLiveData(): MutableLiveData<Result<CheckoutData.Data>> = checkoutDataLiveData
}