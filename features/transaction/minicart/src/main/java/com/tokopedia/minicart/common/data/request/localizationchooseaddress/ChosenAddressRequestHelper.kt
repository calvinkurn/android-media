package com.tokopedia.minicart.common.data.request.localizationchooseaddress

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ChosenAddressRequestHelper @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val KEY_CHOSEN_ADDRESS = "chosen_address"
    }

    fun getContext(): Context {
        return context
    }

    fun addChosenAddressParam(requestParams: RequestParams): RequestParams {
        requestParams.putObject(KEY_CHOSEN_ADDRESS, getChosenAddress())
        return requestParams
    }

    fun getChosenAddress(): ChosenAddress? {
        ChooseAddressUtils.getLocalizingAddressData(getContext())?.let {
            val addressId = it.address_id.toZeroStringIfNullOrBlank()
            val districtId = it.district_id.toZeroStringIfNullOrBlank()
            return ChosenAddress(
                    mode = if (addressId.toLongOrZero() != 0L) ChosenAddress.MODE_ADDRESS else if (districtId.toLongOrZero() != 0L) ChosenAddress.MODE_SNIPPET else ChosenAddress.MODE_EMPTY,
                    addressId = addressId,
                    districtId = districtId,
                    postalCode = it.postal_code,
                    geolocation = if (it.lat.isNotBlank() && it.long.isNotBlank()) it.lat + "," + it.long else ""
            )
        }

        return null
    }
}