package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PaymentType {

    @SerializedName("metadata")
    @Expose
    private String metadata;
    @SerializedName("types")
    @Expose
    private List<String> types = new ArrayList<>();

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
