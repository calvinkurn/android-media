package com.tokopedia.tokofood.purchase.purchasepage.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KeroEditAddressResponse(
    @SerializedName("kero_edit_address")
    @Expose
    val keroEditAddress: KeroEditAddress = KeroEditAddress()
)

data class KeroEditAddress(
    @SerializedName("data")
    @Expose
    val data: KeroEditAddressData = KeroEditAddressData()
)

data class KeroEditAddressData(
    @SerializedName("addr_id")
    @Expose
    val addressId: String = "",
    @SerializedName("is_success")
    @Expose
    val isSuccess: Int = 0
) {

    companion object {
        private const val IS_SUCCESS = 1
    }

    fun isEditSuccess(): Boolean = isSuccess == IS_SUCCESS

}