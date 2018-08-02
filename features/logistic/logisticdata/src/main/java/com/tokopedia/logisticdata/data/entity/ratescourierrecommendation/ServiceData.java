package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class ServiceData {

    @SerializedName("service_name")
    @Expose
    private String serviceName;
    @SerializedName("service_id")
    @Expose
    private int serviceId;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("range_price")
    @Expose
    private RangePriceData rangePrice;
    @SerializedName("texts")
    @Expose
    private ServiceTextData texts;
    @SerializedName("products")
    @Expose
    private List<ProductData> products;

    public ServiceData() {
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public RangePriceData getRangePrice() {
        return rangePrice;
    }

    public void setRangePrice(RangePriceData rangePrice) {
        this.rangePrice = rangePrice;
    }

    public ServiceTextData getTexts() {
        return texts;
    }

    public void setTexts(ServiceTextData texts) {
        this.texts = texts;
    }

    public List<ProductData> getProducts() {
        return products;
    }

    public void setProducts(List<ProductData> products) {
        this.products = products;
    }
}
