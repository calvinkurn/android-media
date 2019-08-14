package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 01/08/18.
 */
public class ProductTrackerData {

    @SerializedName("attribution")
    @Expose
    private String attribution;
    @SerializedName("tracker_list_name")
    @Expose
    private String trackerListName;

    public String getAttribution() {
        return attribution;
    }

    public String getTrackerListName() {
        return trackerListName;
    }
}
