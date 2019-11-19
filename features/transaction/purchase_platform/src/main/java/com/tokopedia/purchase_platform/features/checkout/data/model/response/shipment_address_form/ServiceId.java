package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class ServiceId {

    @SerializedName("service_id")
    @Expose
    private String serviceId;
    @SerializedName("sp_ids")
    @Expose
    private String spIds;

    public String getServiceId() {
        return serviceId;
    }

    public String getSpIds() {
        return spIds;
    }
}
