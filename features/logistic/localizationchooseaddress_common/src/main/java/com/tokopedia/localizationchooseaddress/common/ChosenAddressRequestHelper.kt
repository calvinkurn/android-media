package com.tokopedia.localizationchooseaddress.common

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.toZeroStringIfNullOrBlank
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class ChosenAddress(
    @SerializedName("mode")
    val mode: Int = 0,
    @SerializedName("address_id")
    val addressId: String = "",
    @SerializedName("city_id")
    var cityId: String = "",
    @SerializedName("district_id")
    val districtId: String = "",
    @SerializedName("postal_code")
    val postalCode: String = "",
    @SerializedName("geolocation")
    val geolocation: String = "",
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = "",
    @SerializedName("tokonow")
    val tokonow: ChosenAddressTokonow = ChosenAddressTokonow()
) : Parcelable {
    companion object {
        const val MODE_EMPTY = 0
        const val MODE_ADDRESS = 1
        const val MODE_SNIPPET = 2
    }
}

@Parcelize
data class ChosenAddressTokonow(
    @SerializedName("shop_id")
    val shopId: String = "",
    @SerializedName("warehouse_id")
    val warehouseId: String = "",
    @SerializedName("warehouses")
    val warehouses: List<LocalWarehouseModel> = emptyList(),
    @SerializedName("service_type")
    val serviceType: String = "",
    @SerializedName("warehouse_ids")
    val warehouseIds: List<String> = emptyList()
) : Parcelable

class ChosenAddressRequestHelper @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        const val KEY_CHOSEN_ADDRESS = "chosen_address"
    }

    private fun getContext(): Context {
        return context
    }

    fun getChosenAddress(): ChosenAddress {
        ChooseAddressUtils.getLocalizingAddressData(getContext()).let {
            val addressId = it.address_id.toZeroStringIfNullOrBlank()
            val districtId = it.district_id.toZeroStringIfNullOrBlank()
            val cityId = it.city_id.toZeroStringIfNullOrBlank()
            return ChosenAddress(
                mode = if (addressId.toLongOrZero() != 0L) ChosenAddress.MODE_ADDRESS else if (districtId.toLongOrZero() != 0L) ChosenAddress.MODE_SNIPPET else ChosenAddress.MODE_EMPTY,
                addressId = addressId,
                cityId = cityId,
                districtId = districtId,
                postalCode = it.postal_code,
                geolocation = it.latLong,
                latitude = it.lat,
                longitude = it.long,
                tokonow = ChosenAddressTokonow(
                    shopId = it.shop_id.toZeroStringIfNullOrBlank(),
                    warehouseId = it.warehouse_id.toZeroStringIfNullOrBlank(),
                    warehouses = it.warehouses,
                    serviceType = it.service_type,
                    warehouseIds = it.warehouse_ids
                )
            )
        }
    }
}
