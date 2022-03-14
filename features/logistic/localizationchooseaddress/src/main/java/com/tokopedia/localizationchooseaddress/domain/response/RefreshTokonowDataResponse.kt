package com.tokopedia.localizationchooseaddress.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefreshTokonowDataResponse(

    @Expose
    @SerializedName("data")
    val data: Data
) {
    data class Data(

        @Expose
        @SerializedName("RefreshTokonowData")
        val refreshTokonowData: RefreshTokonowData
    ) {
        data class RefreshTokonowData(

            @Expose
            @SerializedName("data")
            val data: RefreshTokonowDataSuccess,

            @Expose
            @SerializedName("header")
            val header: Header
        ) {
            data class Header(

                @Expose
                @SerializedName("reason")
                val reason: String,

                @Expose
                @SerializedName("error_code")
                val errorCode: String,

                @Expose
                @SerializedName("process_time")
                val processTime: Double
            )

            data class RefreshTokonowDataSuccess(

                @Expose
                @SerializedName("shop_id")
                val shopId: String,

                @Expose
                @SerializedName("service_type")
                val serviceType: String,

                @Expose
                @SerializedName("last_update")
                val lastUpdate: String,


                @Expose
                @SerializedName("warehouses")
                val warehouses: List<WarehousesItem>,

                @Expose
                @SerializedName("warehouse_id")
                val warehouseId: String
            ) {
                data class WarehousesItem(

                    @Expose
                    @SerializedName("service_type")
                    val serviceType: String,

                    @Expose
                    @SerializedName("warehouse_id")
                    val warehouseId: String
                )
            }
        }
    }
}






