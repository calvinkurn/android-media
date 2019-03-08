
package model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceDataResponse {
    @SerializedName("IsEligible")
    private Boolean isEligible;
    @SerializedName("OldPrice")
    private Integer oldPrice;
    @SerializedName("RemainingPrice")
    private Integer remainingPrice;
    @SerializedName("DeviceDiagId")
    private Integer deviceDiagId;
    @SerializedName("DeviceAttr")
    private DeviceAttr deviceAttr;
    @SerializedName("DeviceReview")
    private List<String> deviceReview = null;
    @SerializedName("ExpiryTimeFmt")
    private String expiryTimeFmt;
    @SerializedName("ConvenienceFee")
    private ConvenienceFee convenienceFee;
    @SerializedName("CommissionFee")
    private CommissionFee commissionFee;
    @SerializedName("Message")
    @Expose
    private String message;

    public Boolean getIsEligible() {
        return isEligible;
    }

    public void setIsEligible(Boolean isEligible) {
        this.isEligible = isEligible;
    }

    public Integer getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Integer oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Integer getRemainingPrice() {
        return remainingPrice;
    }

    public void setRemainingPrice(Integer remainingPrice) {
        this.remainingPrice = remainingPrice;
    }

    public Integer getDeviceDiagId() {
        return deviceDiagId;
    }

    public void setDeviceDiagId(Integer deviceDiagId) {
        this.deviceDiagId = deviceDiagId;
    }

    public DeviceAttr getDeviceAttr() {
        return deviceAttr;
    }

    public void setDeviceAttr(DeviceAttr deviceAttr) {
        this.deviceAttr = deviceAttr;
    }

    public List<String> getDeviceReview() {
        return deviceReview;
    }

    public void setDeviceReview(List<String> deviceReview) {
        this.deviceReview = deviceReview;
    }

    public String getExpiryTimeFmt() {
        return expiryTimeFmt;
    }

    public void setExpiryTimeFmt(String expiryTimeFmt) {
        this.expiryTimeFmt = expiryTimeFmt;
    }

    public ConvenienceFee getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(ConvenienceFee convenienceFee) {
        this.convenienceFee = convenienceFee;
    }

    public CommissionFee getCommissionFee() {
        return commissionFee;
    }

    public void setCommissionFee(CommissionFee commissionFee) {
        this.commissionFee = commissionFee;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
