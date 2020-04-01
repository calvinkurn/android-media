
package com.tokopedia.flight.review.domain.verifybooking.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fare {

    @SerializedName("adult")
    @Expose
    private String adult;
    @SerializedName("child")
    @Expose
    private String child;
    @SerializedName("infant")
    @Expose
    private String infant;
    @SerializedName("adult_numeric")
    @Expose
    private int adultNumeric;
    @SerializedName("child_numeric")
    @Expose
    private int childNumeric;
    @SerializedName("infant_numeric")
    @Expose
    private int infantNumeric;

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getInfant() {
        return infant;
    }

    public void setInfant(String infant) {
        this.infant = infant;
    }

    public int getAdultNumeric() {
        return adultNumeric;
    }

    public void setAdultNumeric(int adultNumeric) {
        this.adultNumeric = adultNumeric;
    }

    public int getChildNumeric() {
        return childNumeric;
    }

    public void setChildNumeric(int childNumeric) {
        this.childNumeric = childNumeric;
    }

    public int getInfantNumeric() {
        return infantNumeric;
    }

    public void setInfantNumeric(int infantNumeric) {
        this.infantNumeric = infantNumeric;
    }

}
