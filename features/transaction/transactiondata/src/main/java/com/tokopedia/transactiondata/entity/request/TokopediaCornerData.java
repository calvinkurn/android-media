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
    private int userCornerId;
    @SerializedName("corner_id")
    @Expose
    private int cornerId;

    public TokopediaCornerData() {
    }

    public boolean isTokopediaCorner() {
        return isTokopediaCorner;
    }

    public void setTokopediaCorner(boolean tokopediaCorner) {
        isTokopediaCorner = tokopediaCorner;
    }

    public int getUserCornerId() {
        return userCornerId;
    }

    public void setUserCornerId(int userCornerId) {
        this.userCornerId = userCornerId;
    }

    public int getCornerId() {
        return cornerId;
    }

    public void setCornerId(int cornerId) {
        this.cornerId = cornerId;
    }
}
