package com.tokopedia.logisticCommon.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class KeroAddrGetDistrictCenterResponse(

    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(

        @SerializedName("kero_addr_get_district_center")
        val keroAddrGetDistrictCenter: KeroAddrGetDistrictCenter = KeroAddrGetDistrictCenter()
    ) {
        data class KeroAddrGetDistrictCenter(

            @SerializedName("district")
            val district: District = District()
        ) {
            data class District(

                @SerializedName("latitude")
                val latitude: Double = 0.0,

                @SuppressLint("Invalid Data Type")
                @SerializedName("district_id")
                val districtId: Long = 0,

                @SerializedName("longitude")
                val longitude: Double = 0.0
            )
        }
    }
}
