package com.tokopedia.atc_common.data.model.request.chosenaddress

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ChosenAddressAddToCartRequestHelper @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val KEY_CHOSEN_ADDRESS = "chosen_address"
    }

    fun addChosenAddressParam(requestParams: RequestParams): RequestParams {
        requestParams.putObject(KEY_CHOSEN_ADDRESS, getChosenAddress())
        return requestParams
    }

    fun getChosenAddress(): ChosenAddressAddToCart? {
        ChooseAddressUtils.getLocalizingAddressData(context)?.let {
            return ChosenAddressAddToCart(
                    mode = if (it.address_id.isNotBlank()) ChosenAddressAddToCart.MODE_ADDRESS else ChosenAddressAddToCart.MODE_SNIPPET,
                    addressId = it.address_id,
                    districtId = it.district_id,
                    postalCode = "",
                    geolocation = it.lat + "," + it.long
            )
        }

        return null
    }
}