package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class ProductData {

    @SerializedName("shipper_name")
    @Expose
    private String shipperName;
    @SerializedName("shipper_id")
    @Expose
    private int shipperId;
    @SerializedName("shipper_product_id")
    @Expose
    private int shipperProductId;
    @SerializedName("shipper_product_name")
    @Expose
    private String shipperProductName;
    @SerializedName("shipper_product_desc")
    @Expose
    private String shipperProductDesc;
    @SerializedName("shipper_weight")
    @Expose
    private int shipperWeight;
    @SerializedName("is_show_map")
    @Expose
    private int isShowMap;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("recommend")
    @Expose
    private boolean recommend;
    @SerializedName("check_sum")
    @Expose
    private String checkSum;
    @SerializedName("ut")
    @Expose
    private String ut;
    @SerializedName("price")
    @Expose
    private PriceData price;
    @SerializedName("etd")
    @Expose
    private EtdData etd;
    @SerializedName("insurance")
    @Expose
    private InsuranceData insurance;
    @SerializedName("texts")
    @Expose
    private ProductTextData texts;
    @SerializedName("error")
    @Expose
    private ErrorData error;

    public ProductData() {
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    public int getShipperProductId() {
        return shipperProductId;
    }

    public void setShipperProductId(int shipperProductId) {
        this.shipperProductId = shipperProductId;
    }

    public String getShipperProductName() {
        return shipperProductName;
    }

    public void setShipperProductName(String shipperProductName) {
        this.shipperProductName = shipperProductName;
    }

    public String getShipperProductDesc() {
        return shipperProductDesc;
    }

    public void setShipperProductDesc(String shipperProductDesc) {
        this.shipperProductDesc = shipperProductDesc;
    }

    public int getShipperWeight() {
        return shipperWeight;
    }

    public void setShipperWeight(int shipperWeight) {
        this.shipperWeight = shipperWeight;
    }

    public int getIsShowMap() {
        return isShowMap;
    }

    public void setIsShowMap(int isShowMap) {
        this.isShowMap = isShowMap;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getUt() {
        return ut;
    }

    public void setUt(String ut) {
        this.ut = ut;
    }

    public PriceData getPrice() {
        return price;
    }

    public void setPrice(PriceData price) {
        this.price = price;
    }

    public EtdData getEtd() {
        return etd;
    }

    public void setEtd(EtdData etd) {
        this.etd = etd;
    }

    public InsuranceData getInsurance() {
        return insurance;
    }

    public void setInsurance(InsuranceData insurance) {
        this.insurance = insurance;
    }

    public ProductTextData getTexts() {
        return texts;
    }

    public void setTexts(ProductTextData texts) {
        this.texts = texts;
    }

    public ErrorData getError() {
        return error;
    }

    public void setError(ErrorData error) {
        this.error = error;
    }
}
