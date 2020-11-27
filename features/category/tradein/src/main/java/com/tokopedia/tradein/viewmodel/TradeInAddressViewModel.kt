package com.tokopedia.tradein.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.tradein.mapper.TradeInMapper
import com.tokopedia.tradein.model.AddressResult
import com.tokopedia.tradein.model.MoneyInKeroGetAddressResponse
import com.tokopedia.tradein.usecase.GetAddressUseCase
import com.tokopedia.tradein.usecase.RatesV3UseCase
import com.tokopedia.tradein.usecase.ShopInfoUseCase
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class TradeInAddressViewModel @Inject constructor(
        private var getAddressUseCase: GetAddressUseCase,
        private var shopInfoUseCase: ShopInfoUseCase,
        private var ratesV3UseCase: RatesV3UseCase
) : BaseTradeInViewModel(), CoroutineScope {

    private val addressLiveData = MutableLiveData<AddressResult>()
    private var token: Token? = null
    private val isEligible: MutableLiveData<Boolean?> = MutableLiveData()
    var recipientAddressModel = RecipientAddressModel()

    fun getAddressLiveData(): LiveData<AddressResult> = addressLiveData
    fun getTradeInEligibleLiveData(): LiveData<Boolean?> = isEligible

    fun getAddress(origin: String, weight: Int, shopId: Int) {
        launchCatchError(block = {
            val address = getAddressUseCase.getAddress()
            recipientAddressModel = TradeInMapper.mapKeroAddressToRecipientAddress(address.defaultAddress)
            addressLiveData.value = address
            token = address.token
            progBarVisibility.value = false
            isJabodetabekArea(origin, weight, shopId)
        }, onError = {
            it.printStackTrace()
            progBarVisibility.value = false
            errorMessage.value = it.localizedMessage
        })
    }

    private fun getShopInfo(origin: String, weight: Int, shopId: Int) {
        progBarVisibility.value = true
        launchCatchError(block = {
            val shopInfoData = shopInfoUseCase.getShopInfo(shopId)
            for (shop in shopInfoData.shopInfoByID.result[0].shipmentInfo) {
                if (shop.shipmentID == VALID_SHIPMENT_ID) {
                    isEligible.value = true
                    progBarVisibility.value = false
                    break
                }
            }
            if (isEligible.value == null) {
                getRatesV3(origin, weight)
            }
        }, onError = {
            it.printStackTrace()
            progBarVisibility.value = false
            errorMessage.value = it.localizedMessage
        })
    }

    private fun getRatesV3(origin: String, weight: Int) {
        progBarVisibility.value = true
        launchCatchError(block = {
            var destination = ""
            addressLiveData.value?.defaultAddress?.apply {
                destination = "${(district)}|${(postalCode)}|${(latitude)},${(longitude)}"
            }
            val ratesV3Data = ratesV3UseCase.getRatesV3(origin = origin, destination = destination, weight = weight.toString())
            ratesV3Data.ratesV3Api.ratesv3.services[0].apply {
                isEligible.value = (status == 200
                        && products[0].price != null
                        && products[0].price?.price ?: 0 > 0)
            }
            progBarVisibility.value = false
        }, onError = {
            it.printStackTrace()
            progBarVisibility.value = false
            errorMessage.value = it.localizedMessage
        })

    }

    private fun isJabodetabekArea(origin: String, weight: Int, shopId: Int) {
        if (addressLiveData.value?.defaultAddress?.city in array) {
            isEligible.value = true
        } else
            getShopInfo(origin, weight, shopId)
    }

    fun setAddress(address: MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress.Data, origin: String, weight: Int, shopId: Int) {
        isEligible.value = null
        addressLiveData.value = AddressResult(address, token)
        isJabodetabekArea(origin, weight, shopId)
    }

    companion object {
        val array: List<Int> = arrayListOf(174, 175, 176, 177, 178, 179, 151, 168, 171, 144, 146, 463, 150, 167)
        const val VALID_SHIPMENT_ID = "25"
    }
}