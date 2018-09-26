package com.tokopedia.logisticdata.data.entity.ratescourierrecommendation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Irfan Khoirul on 02/08/18.
 */

public class ServiceData implements Parcelable {

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

    protected ServiceData(Parcel in) {
        serviceName = in.readString();
        serviceId = in.readInt();
        status = in.readInt();
        rangePrice = in.readParcelable(RangePriceData.class.getClassLoader());
        texts = in.readParcelable(ServiceTextData.class.getClassLoader());
        products = in.createTypedArrayList(ProductData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serviceName);
        dest.writeInt(serviceId);
        dest.writeInt(status);
        dest.writeParcelable(rangePrice, flags);
        dest.writeParcelable(texts, flags);
        dest.writeTypedList(products);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceData> CREATOR = new Creator<ServiceData>() {
        @Override
        public ServiceData createFromParcel(Parcel in) {
            return new ServiceData(in);
        }

        @Override
        public ServiceData[] newArray(int size) {
            return new ServiceData[size];
        }
    };

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
