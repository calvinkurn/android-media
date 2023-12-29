package com.tokopedia.tokofood.common.address

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import javax.inject.Inject

open class TokoFoodChosenAddressRequestHelper @Inject constructor(@ApplicationContext private val context: Context) {

    private fun getContext(): Context {
        return context
    }

    fun getChosenAddress(): TokoFoodChosenAddress {
        ChooseAddressUtils.getLocalizingAddressData(getContext()).let {
            val addressId = it.address_id.toLongOrZero()
            val districtId = it.district_id.toLongOrZero()
            val mode =
                when {
                    addressId != 0L -> TokoFoodChosenAddress.MODE_ADDRESS
                    districtId != 0L -> TokoFoodChosenAddress.MODE_SNIPPET
                    else -> TokoFoodChosenAddress.MODE_EMPTY
                }
            return TokoFoodChosenAddress(
                mode = mode,
                addressId = addressId,
                districtId = districtId,
                postalCode = it.postal_code,
                geolocation = it.latLong
            )
        }
    }
}
