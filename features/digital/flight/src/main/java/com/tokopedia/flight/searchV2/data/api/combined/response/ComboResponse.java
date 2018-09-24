package com.tokopedia.flight.searchV2.data.api.combined.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 19/09/18.
 */
class ComboResponse {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("adultPrice")
    @Expose
    private String adultPrice;

    @SerializedName("childPrice")
    @Expose
    private String childPrice;

    @SerializedName("infantPrice")
    @Expose
    private String infantPrice;

    @SerializedName("adultPriceNumeric")
    @Expose
    private String adultPriceNumeric;

    @SerializedName("childPriceNumeric")
    @Expose
    private String childPriceNumeric;

    @SerializedName("infantPriceNumeric")
    @Expose
    private String infantPriceNumeric;

    public String getId() {
        return id;
    }

    public String getAdultPrice() {
        return adultPrice;
    }

    public String getChildPrice() {
        return childPrice;
    }

    public String getInfantPrice() {
        return infantPrice;
    }

    public String getAdultPriceNumeric() {
        return adultPriceNumeric;
    }

    public String getChildPriceNumeric() {
        return childPriceNumeric;
    }

    public String getInfantPriceNumeric() {
        return infantPriceNumeric;
    }

}
