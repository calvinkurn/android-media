package com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toLongOrZero
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
            if (it.address_id.isBlank()) {
                it.address_id = "0"
            }
            if (it.district_id.isBlank()) {
                it.district_id = "0"
            }
            return ChosenAddress(
                    mode = if (it.address_id.toLongOrZero() != 0L) ChosenAddress.MODE_ADDRESS else if (it.district_id.toLongOrZero() != 0L) ChosenAddress.MODE_SNIPPET else ChosenAddress.MODE_EMPTY,
                    addressId = it.address_id,
                    districtId = it.district_id,
                    postalCode = it.postal_code,
                    geolocation = if (it.lat.isNotBlank() && it.long.isNotBlank()) it.lat + "," + it.long else ""
            )
        }

        return null
    }
}