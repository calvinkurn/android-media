package com.tokopedia.withdraw.domain.model.premiumAccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data  {

    @SerializedName("isPowerWD")
    @Expose
    private boolean isPowerWD;
    @SerializedName("isPowerMerchant")
    @Expose
    private boolean isPowerMerchant;
    @SerializedName("shopID")
    @Expose
    private long shopID;
    @SerializedName("accNum")
    @Expose
    private String accNum;
    @SerializedName("bankID")
    @Expose
    private long bankID;
    @SerializedName("userID")
    @Expose
    private long userID;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("program")
    @Expose
    private String program;
    @SerializedName("wdPoints")
    @Expose
    private long wdPoints;
    @SerializedName("statusInt")
    @Expose
    private int statusInt;

    public boolean isIsPowerWD() {
        return isPowerWD;
    }

    public void setIsPowerWD(boolean isPowerWD) {
        this.isPowerWD = isPowerWD;
    }

    public boolean isIsPowerMerchant() {
        return isPowerMerchant;
    }

    public void setIsPowerMerchant(boolean isPowerMerchant) {
        this.isPowerMerchant = isPowerMerchant;
    }

    public long getShopID() {
        return shopID;
    }

    public void setShopID(long shopID) {
        this.shopID = shopID;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(String accNum) {
        this.accNum = accNum;
    }

    public long getBankID() {
        return bankID;
    }

    public void setBankID(long bankID) {
        this.bankID = bankID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public long getWdPoints() {
        return wdPoints;
    }

    public void setWdPoints(long wdPoints) {
        this.wdPoints = wdPoints;
    }

    public int getStatusInt() {
        return statusInt;
    }

    public void setStatusInt(int statusInt) {
        this.statusInt = statusInt;
    }

}
