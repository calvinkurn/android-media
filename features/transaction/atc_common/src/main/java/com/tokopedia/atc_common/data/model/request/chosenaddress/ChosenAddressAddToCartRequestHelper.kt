package com.tokopedia.atc_common.data.model.request.chosenaddress

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ChosenAddressAddToCartRequestHelper @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val PARAM_KEY_CHOSEN_ADDRESS = "chosen_address"
    }

    fun getContext(): Context {
        return context
    }

    fun addChosenAddressParam(requestParams: RequestParams): RequestParams {
        requestParams.putObject(PARAM_KEY_CHOSEN_ADDRESS, getChosenAddress())
        return requestParams
    }

    fun getChosenAddress(): ChosenAddressAddToCart? {
        ChooseAddressUtils.getLocalizingAddressData(getContext())?.let {
            if (it.address_id.isBlank()) {
                it.address_id = "0"
            }
            if (it.district_id.isBlank()) {
                it.district_id = "0"
            }
            return ChosenAddressAddToCart(
                    mode = if (it.address_id.toLongOrZero() != 0L) ChosenAddressAddToCart.MODE_ADDRESS else ChosenAddressAddToCart.MODE_SNIPPET,
                    addressId = it.address_id,
                    districtId = it.district_id,
                    postalCode = it.postal_code,
                    geolocation = if (it.lat.isNotBlank() && it.long.isNotBlank()) it.lat + "," + it.long else ""
            )
        }

        return null
    }
}