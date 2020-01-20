package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 8/30/18.
 */
public class MembershipSumUserCard {
    @SerializedName("sumUserCard")
    @Expose
    private int sumUserCard = 0;
    @SerializedName("sumUserCardStr")
    @Expose
    private String sumUserCardStr = "";

    public int getSumUserCard() {
        return sumUserCard;
    }

    public void setSumUserCard(int sumUserCard) {
        this.sumUserCard = sumUserCard;
    }

    public String getSumUserCardStr() {
        return sumUserCardStr;
    }

    public void setSumUserCardStr(String sumUserCardStr) {
        this.sumUserCardStr = sumUserCardStr;
    }
}
