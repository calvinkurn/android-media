package com.tokopedia.logisticCommon.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KeroAddrGetDistrictCenterResponse(

    @Expose
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(

        @Expose
        @SerializedName("kero_addr_get_district_center")
        val keroAddrGetDistrictCenter: KeroAddrGetDistrictCenter = KeroAddrGetDistrictCenter()
    ) {
        data class KeroAddrGetDistrictCenter(

            @Expose
            @SerializedName("district")
            val district: District = District()
        ) {
            data class District(

                @Expose
                @SerializedName("latitude")
                val latitude: Double = 0.0,

                @SuppressLint("Invalid Data Type")
                @Expose
                @SerializedName("district_id")
                val districtId: Long = 0,

                @Expose
                @SerializedName("longitude")
                val longitude: Double = 0.0
            )
        }
    }
}