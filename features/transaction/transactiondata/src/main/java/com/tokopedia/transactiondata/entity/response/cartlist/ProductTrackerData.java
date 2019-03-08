package com.tokopedia.transactiondata.entity.response.cartlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 04/06/18.
 */
public class ProductTrackerData {

    @SerializedName("attribution")
    @Expose
    public String attribution = "";
    @SerializedName("tracker_list_name")
    @Expose
    public String trackerListName = "";

    public String getAttribution() {
        return attribution;
    }

    public String getTrackerListName() {
        return trackerListName;
    }
}
