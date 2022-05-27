package com.tokopedia.tokofood.common.address

import android.content.Context
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class TokoFoodChosenAddress(
    @Expose
    @SerializedName("mode")
    val mode: Int = 0,
    @Expose
    @SerializedName("address_id")
    val addressId: Long = 0,
    @Expose
    @SerializedName("district_id")
    val districtId: Long = 0,
    @Expose
    @SerializedName("postal_code")
    val postalCode: String = "",
    @Expose
    @SerializedName("geolocation")
    val geolocation: String = ""
) : Parcelable {
    companion object {
        const val MODE_EMPTY = 0
        const val MODE_ADDRESS = 1
        const val MODE_SNIPPET = 2
    }

    fun generateString(): String = Gson().toJson(this)
}

class TokoFoodChosenAddressRequestHelper @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val KEY_CHOSEN_ADDRESS = "chosen_address"
    }

    private fun getContext(): Context {
        return context
    }

    fun getChosenAddress(): TokoFoodChosenAddress {
        ChooseAddressUtils.getLocalizingAddressData(getContext()).let {
            val addressId = it.address_id.toLongOrZero()
            val districtId = it.district_id.toLongOrZero()
            return TokoFoodChosenAddress(
                mode = if (addressId != 0L) TokoFoodChosenAddress.MODE_ADDRESS else if (districtId != 0L) TokoFoodChosenAddress.MODE_SNIPPET else TokoFoodChosenAddress.MODE_EMPTY,
                addressId = addressId,
                districtId = districtId,
                postalCode = it.postal_code,
                geolocation = it.latLong
            )
        }
    }
}