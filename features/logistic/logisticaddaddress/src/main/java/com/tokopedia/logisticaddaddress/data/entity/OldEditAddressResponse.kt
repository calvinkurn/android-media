package com.tokopedia.logisticaddaddress.data.entity

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OldEditAddressResponse(

    @Expose
    @SerializedName("data")
    val data: OldEditAddressResponseData = OldEditAddressResponseData()
)

data class OldEditAddressResponseData(

    @Expose
    @SerializedName("is_success")
    val isSuccess: Int = 0,

    @Expose
    @SerializedName("is_state_chosen_address_changed")
    val isStateChosenAddressChanged: Boolean = false,

    @SuppressLint("Invalid Data Type")
    @Expose
    @SerializedName("addr_id")
    val addrId: Long = 0
)