package com.tokopedia.tokopoints.view.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LuckyEggEntity {
    @Expose
    @SerializedName("resultStatus")
    ResultStatusEntity resultStatus;

    @Expose
    @SerializedName("offFlag")
    private boolean offFlag;

    @Expose
    @SerializedName("sumToken")
    private int sumToken;

    @Expose
    @SerializedName("sumTokenStr")
    private String sumTokenStr;

    @Expose
    @SerializedName("floating")
    private LuckyEggFloatingEntity floating;

    public ResultStatusEntity getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatusEntity resultStatus) {
        this.resultStatus = resultStatus;
    }

    public boolean isOffFlag() {
        return offFlag;
    }

    public void setOffFlag(boolean offFlag) {
        this.offFlag = offFlag;
    }

    public int getSumToken() {
        return sumToken;
    }

    public void setSumToken(int sumToken) {
        this.sumToken = sumToken;
    }

    public String getSumTokenStr() {
        return sumTokenStr;
    }

    public void setSumTokenStr(String sumTokenStr) {
        this.sumTokenStr = sumTokenStr;
    }

    public LuckyEggFloatingEntity getFloating() {
        return floating;
    }

    public void setFloating(LuckyEggFloatingEntity floating) {
        this.floating = floating;
    }

    @Override
    public String toString() {
        return "LuckyEggEntity{" +
                "resultStatus=" + resultStatus +
                ", offFlag=" + offFlag +
                ", sumToken='" + sumToken + '\'' +
                ", sumTokenStr='" + sumTokenStr + '\'' +
                ", floating=" + floating +
                '}';
    }
}
