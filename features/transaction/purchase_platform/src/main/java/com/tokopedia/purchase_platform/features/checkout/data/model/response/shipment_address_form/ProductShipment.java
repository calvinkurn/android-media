package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ProductShipment {
    @SerializedName("shipment_id")
    @Expose
    private String shipmentId;
    @SerializedName("service_id")
    @Expose
    private List<String> serviceId = new ArrayList<>();

    public String getShipmentId() {
        return shipmentId;
    }

    public List<String> getServiceId() {
        return serviceId;
    }
}
