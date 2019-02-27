package com.tokopedia.transactiondata.entity.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 11/02/19.
 */
public class TokopediaCornerData {

    @SerializedName("is_tokopedia_corner")
    @Expose
    private boolean isTokopediaCorner;
    @SerializedName("user_corner_id")
    @Expose
    private String userCornerId;
    @SerializedName("corner_id")
    @Expose
    private int cornerId;

    public TokopediaCornerData() {
    }

    public TokopediaCornerData(String userCornerId, int cornerId) {
        this.isTokopediaCorner = true;
        this.userCornerId = userCornerId;
        this.cornerId = cornerId;
    }

    public boolean isTokopediaCorner() {
        return isTokopediaCorner;
    }

    public void setTokopediaCorner(boolean tokopediaCorner) {
        isTokopediaCorner = tokopediaCorner;
    }

    public String getUserCornerId() {
        return userCornerId;
    }

    public void setUserCornerId(String userCornerId) {
        this.userCornerId = userCornerId;
    }

    public int getCornerId() {
        return cornerId;
    }

    public void setCornerId(int cornerId) {
        this.cornerId = cornerId;
    }
}
