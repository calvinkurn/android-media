package com.tokopedia.train.station.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainStationEntity {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("popularityRank")
    @Expose
    private int popularityOrder;

    public int getId() {
        return id;
    }

    public int getPopularityOrder() {
        return popularityOrder;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
