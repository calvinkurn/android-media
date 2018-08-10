package com.tokopedia.train.search.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 14/05/18.
 */
public class FareEntity {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("class")
    @Expose
    private String scheduleClass;
    @SerializedName("displayClass")
    @Expose
    private String displayClass;
    @SerializedName("subclass")
    @Expose
    private String subclass;
    @SerializedName("adultFare")
    @Expose
    private long adultFare;
    @SerializedName("displayAdultFare")
    @Expose
    private String displayAdultFare;
    @SerializedName("infantFare")
    @Expose
    private long infantFare;
    @SerializedName("displayInfantFare")
    @Expose
    private String displayInfantFare;

    public String getId() {
        return id;
    }

    public String getScheduleClass() {
        return scheduleClass;
    }

    public String getDisplayClass() {
        return displayClass;
    }

    public String getSubclass() {
        return subclass;
    }

    public long getAdultFare() {
        return adultFare;
    }

    public String getDisplayAdultFare() {
        return displayAdultFare;
    }

    public long getInfantFare() {
        return infantFare;
    }

    public String getDisplayInfantFare() {
        return displayInfantFare;
    }

}
