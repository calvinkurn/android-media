package com.tokopedia.localizationchooseaddress.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefreshTokonowDataResponse(

    @Expose
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(

        @Expose
        @SerializedName("RefreshTokonowData")
        val refreshTokonowData: RefreshTokonowData = RefreshTokonowData()
    ) {
        data class RefreshTokonowData(

            @Expose
            @SerializedName("data")
            val data: RefreshTokonowDataSuccess = RefreshTokonowDataSuccess(),

            @Expose
            @SerializedName("header")
            val header: Header = Header()
        ) {
            data class Header(

                @Expose
                @SerializedName("reason")
                val reason: String = "",

                @Expose
                @SerializedName("error_code")
                val errorCode: String = "",

                @Expose
                @SerializedName("process_time")
                val processTime: Double = 0.0
            )

            data class RefreshTokonowDataSuccess(

                @Expose
                @SerializedName("shopID")
                val shopId: String = "",

                @Expose
                @SerializedName("serviceType")
                val serviceType: String = "",

                @Expose
                @SerializedName("tokonowLastUpdate")
                val lastUpdate: String = "",


                @Expose
                @SerializedName("warehouses")
                val warehouses: List<WarehouseItem> = emptyList(),

                @Expose
                @SerializedName("warehouseID")
                val warehouseId: String = ""
            ) {
                data class WarehouseItem(

                    @Expose
                    @SerializedName("serviceType")
                    val serviceType: String = "",

                    @Expose
                    @SerializedName("warehouseID")
                    val warehouseId: String = ""
                )
            }
        }
    }
}






