package com.tokopedia.tokofood.common.domain.response

import com.google.gson.annotations.SerializedName

data class KeroEditAddressResponse(
    @SerializedName("kero_edit_address")
    val keroEditAddress: KeroEditAddress = KeroEditAddress()
)

data class KeroEditAddress(
    @SerializedName("data")
    val data: KeroEditAddressData = KeroEditAddressData()
)

data class KeroEditAddressData(
    @SerializedName("addr_id")
    val addressId: String = "",
    @SerializedName("is_success")
    val isSuccess: Int = 0
) {

    companion object {
        private const val IS_SUCCESS = 1
    }

    fun isEditSuccess(): Boolean = isSuccess == IS_SUCCESS

}