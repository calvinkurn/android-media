package com.tokopedia.gamification.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenDataEntity {

    @SerializedName("offFlag")
    @Expose
    private Boolean offFlag;

    @SerializedName("sumToken")
    @Expose
    private Integer sumToken;

    @SerializedName("sumTokenStr")
    @Expose
    private String sumTokenStr;

    @SerializedName("tokenUnit")
    @Expose
    private String tokenUnit;

    @SerializedName("floating")
    @Expose
    private TokenFloatingEntity floating;

    @SerializedName("home")
    @Expose
    private TokenHomeEntity home;

    public Boolean getOffFlag() {
        return offFlag;
    }

    public Integer getSumToken() {
        return sumToken;
    }

    public String getSumTokenStr() {
        return sumTokenStr;
    }

    public String getTokenUnit() {
        return tokenUnit;
    }

    public TokenFloatingEntity getFloating() {
        return floating;
    }

    public TokenHomeEntity getHome() {
        return home;
    }
}
