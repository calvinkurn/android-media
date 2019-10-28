package com.tokopedia.tradein.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tradein.model.*
import com.tokopedia.tradein.model.MoneyInCheckoutMutationResponse.ResponseData.CheckoutGeneral.CheckoutData
import com.tokopedia.tradein.model.MoneyInCourierResponse.ResponseData.RatesV4
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress
import com.tokopedia.tradein.model.MoneyInScheduleOptionResponse.ResponseData.GetPickupScheduleOption
import com.tokopedia.tradein_common.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext


class MoneyInCheckoutViewModel(application: Application) : BaseViewModel(application), CoroutineScope {

    private val pickupScheduleOptionLiveData = MutableLiveData<Result<GetPickupScheduleOption>>()
    private val courierRatesLiveData = MutableLiveData<Result<RatesV4.Data>>()
    private val checkoutDataLiveData = MutableLiveData<Result<CheckoutData.Data>>()
    private var errorLiveData: MutableLiveData<MoneyInCheckoutState> = MutableLiveData()

    companion object{
        private const val SUCCESS = 1
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    fun getPickupScheduleOption(query: String) {
        launchCatchError(block = {
            val request = HashMap<String, String>()

            val response = repository?.getGQLData(query, MoneyInScheduleOptionResponse.ResponseData::class.java, request) as MoneyInScheduleOptionResponse.ResponseData
            pickupScheduleOptionLiveData.value = Success(response.getPickupScheduleOption)
        }, onError = {
            it.printStackTrace()
            errorLiveData.value = ScheduleTimeError(it.localizedMessage)
        })
    }

    fun getCourierRates(query: String, destination: String) {
        launchCatchError(block = {
            val request = HashMap<String, Any>()
            val input = HashMap<String, String>()
            input["features"] = "money_in"
            input["weight"] = "1"
            input["destination"] = destination
            input["from"] = "client"
            input["type"] = "android"
            input["lang"] = "en"
            request["input"] = input

            val response = repository?.getGQLData(query, MoneyInCourierResponse.ResponseData::class.java, request) as MoneyInCourierResponse.ResponseData
            courierRatesLiveData.value = Success(response.ratesV4.data)
        }, onError = {
            it.printStackTrace()
            errorLiveData.value = CourierPriceError(it.localizedMessage)
        })
    }

    fun makeCheckoutMutation(query: String, hardwareId: String, addressId : Int, spId: Int, pickupTimeStart: Int, pickupTimeEnd: Int) {
        launchCatchError(block = {
            val request = HashMap<String, Any>()
            val cartList: ArrayList<MoneyInMutationRequest.Cart> = arrayListOf()
            val cartInfoList: ArrayList<MoneyInMutationRequest.Cart.CartInfo> = arrayListOf()
            val metadata = HashMap<String, String>()
            val fields: ArrayList<MoneyInMutationRequest.Cart.CartInfo.Field> = arrayListOf()
            fields.add(MoneyInMutationRequest.Cart.CartInfo.Field(
                    "origin_address_id",
                    addressId
            ))
            fields.add(MoneyInMutationRequest.Cart.CartInfo.Field(
                    "sp_id",
                    spId
            ))
            fields.add(MoneyInMutationRequest.Cart.CartInfo.Field(
                    "pickup_time_start",
                    pickupTimeStart
            ))
            fields.add(MoneyInMutationRequest.Cart.CartInfo.Field(
                    "pickup_time_end",
                    pickupTimeEnd
            ))
            cartInfoList.add(MoneyInMutationRequest.Cart.CartInfo(
                    1,
                    "shipping",
                    fields,
                    1
            ))
            metadata["hardware_id"]= hardwareId
            cartList.add(MoneyInMutationRequest.Cart(
                    2,
                    cartInfoList
                    , Gson().toJson(metadata)
            ))
            val params = MoneyInMutationRequest(cartList)

            request["params"] = params

            val response = repository?.getGQLData(query, MoneyInCheckoutMutationResponse.ResponseData::class.java, request) as MoneyInCheckoutMutationResponse.ResponseData
            if (response.checkoutGeneral.data.success == SUCCESS) {
                checkoutDataLiveData.value = Success(response.checkoutGeneral.data.data)
            } else {
                errorLiveData.value = MutationCheckoutError(response.checkoutGeneral.header.messages.joinToString())
            }
        }, onError = {
            it.printStackTrace()
            errorLiveData.value = MutationCheckoutError(it.localizedMessage)
        })
    }

    fun getPickupScheduleOptionLiveData(): MutableLiveData<Result<GetPickupScheduleOption>> = pickupScheduleOptionLiveData
    fun getCourierRatesLiveData(): MutableLiveData<Result<RatesV4.Data>> = courierRatesLiveData
    fun getCheckoutDataLiveData(): MutableLiveData<Result<CheckoutData.Data>> = checkoutDataLiveData
    fun getErrorLiveData(): MutableLiveData<MoneyInCheckoutState> = errorLiveData
}
