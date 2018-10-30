package com.tokopedia.flight.searchV2.data.api.combined.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 19/09/18.
 */
public class ComboResponse {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("adult_price")
    @Expose
    private String adultPrice;

    @SerializedName("child_price")
    @Expose
    private String childPrice;

    @SerializedName("infant_price")
    @Expose
    private String infantPrice;

    @SerializedName("adult_price_numeric")
    @Expose
    private int adultPriceNumeric;

    @SerializedName("child_price_numeric")
    @Expose
    private int childPriceNumeric;

    @SerializedName("infant_price_numeric")
    @Expose
    private int infantPriceNumeric;

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

    public int getAdultPriceNumeric() {
        return adultPriceNumeric;
    }

    public int getChildPriceNumeric() {
        return childPriceNumeric;
    }

    public int getInfantPriceNumeric() {
        return infantPriceNumeric;
    }

}
