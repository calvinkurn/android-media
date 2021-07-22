
package com.tokopedia.common_tradein.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceDiagnostics {

    @SerializedName("trade_in_unique_code")
    @Expose
    private String tradeInUniqueCode;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("model_id")
    @Expose
    private Integer modelId;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("storage")
    @Expose
    private String storage;
    @SerializedName("ram")
    @Expose
    private String ram;
    @SerializedName("imei")
    @Expose
    private String imei;
    @SerializedName("trade_in_price")
    @Expose
    private Integer tradeInPrice;
    @SerializedName("grade")
    @Expose
    private String grade;
    @SerializedName("timestamp")
    @Expose
    private Integer timestamp;
    @SerializedName("review_details")
    @Expose
    private List<String> reviewDetails = null;

    public String getTradeInUniqueCode() {
        return tradeInUniqueCode;
    }

    public void setTradeInUniqueCode(String tradeInUniqueCode) {
        this.tradeInUniqueCode = tradeInUniqueCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getTradeInPrice() {
        return tradeInPrice;
    }

    public void setTradeInPrice(Integer tradeInPrice) {
        this.tradeInPrice = tradeInPrice;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getReviewDetails() {
        return reviewDetails;
    }

    public void setReviewDetails(List<String> reviewDetails) {
        this.reviewDetails = reviewDetails;
    }

}
