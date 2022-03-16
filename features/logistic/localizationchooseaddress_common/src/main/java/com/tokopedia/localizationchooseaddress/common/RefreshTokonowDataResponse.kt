package com.tokopedia.localizationchooseaddress.common

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
                @SerializedName("shop_id")
                val shopId: String = "",

                @Expose
                @SerializedName("service_type")
                val serviceType: String = "",

                @Expose
                @SerializedName("last_update")
                val lastUpdate: String = "",


                @Expose
                @SerializedName("warehouses")
                val warehouses: List<WarehouseItem> = emptyList(),

                @Expose
                @SerializedName("warehouse_id")
                val warehouseId: String = ""
            ) {
                data class WarehouseItem(

                    @Expose
                    @SerializedName("service_type")
                    val serviceType: String = "",

                    @Expose
                    @SerializedName("warehouse_id")
                    val warehouseId: String = ""
                )
            }
        }
    }
}






