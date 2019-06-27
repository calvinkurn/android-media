package model;

import com.google.gson.annotations.SerializedName;

public class ValidateTradeInResponse {
    @SerializedName("IsEligible")
    private boolean isEligible;
    @SerializedName("IsDiagnosed")
    private boolean isDiagnosed;
    @SerializedName("UseKyc")
    private boolean useKyc;
    @SerializedName("UsedPrice")
    private int usedPrice;
    @SerializedName("RemainingPrice")
    private int remainingPrice;
    @SerializedName("Message")
    private String message;

    public boolean isEligible() {
        return isEligible;
    }

    public void setEligible(boolean eligible) {
        isEligible = eligible;
    }

    public boolean isDiagnosed() {
        return isDiagnosed;
    }

    public void setDiagnosed(boolean diagnosed) {
        isDiagnosed = diagnosed;
    }

    public boolean isUseKyc() {
        return useKyc;
    }

    public void setUseKyc(boolean useKyc) {
        this.useKyc = useKyc;
    }

    public int getUsedPrice() {
        return usedPrice;
    }

    public void setUsedPrice(int usedPrice) {
        this.usedPrice = usedPrice;
    }

    public int getRemainingPrice() {
        return remainingPrice;
    }

    public void setRemainingPrice(int remainingPrice) {
        this.remainingPrice = remainingPrice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
