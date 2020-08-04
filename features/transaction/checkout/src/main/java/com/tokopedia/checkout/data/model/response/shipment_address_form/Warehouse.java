package com.tokopedia.checkout.data.model.response.shipment_address_form;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 14/03/19.
 */
public class Warehouse {

    @SerializedName("warehouse_id")
    @Expose
    private Integer warehouseId;
    @SerializedName("city_name")
    @Expose
    private String cityName;

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public String getCityName() {
        return cityName;
    }
}

