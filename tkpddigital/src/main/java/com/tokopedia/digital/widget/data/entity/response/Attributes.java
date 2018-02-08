package com.tokopedia.digital.widget.data.entity.response;

import com.google.gson.annotations.SerializedName;

/**
 * @author rizkyfadillah on 10/2/2017.
 */

public class Attributes {
    @SerializedName("client_number")
    private String clientNumber;

    @SerializedName("label")
    private String label;

    @SerializedName("last_updated")
    private String lastUpdated;

    @SerializedName("last_product")
    private String lastProduct;

    public String getClientNumber() {
        return clientNumber;
    }

    public String getLabel() {
        return label;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public String getLastProduct() {
        return lastProduct;
    }
}
