package com.tokopedia.digital.product.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 4/25/17.
 */

public class ProductDigitalData implements Parcelable {
    private CategoryData categoryData;
    private List<BannerData> bannerDataList = new ArrayList<>();
    private List<OrderClientNumber> recentClientNumberList = new ArrayList<>();
    private OrderClientNumber lastOrderClientNumber;

    public ProductDigitalData() {
    }

    private ProductDigitalData(Builder builder) {
        setCategoryData(builder.categoryData);
        setBannerDataList(builder.bannerDataList);
        setRecentClientNumberList(builder.recentClientNumberList);
        setLastOrderClientNumber(builder.recentClientNumber);
    }


    public CategoryData getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(CategoryData categoryData) {
        this.categoryData = categoryData;
    }

    public List<BannerData> getBannerDataList() {
        return bannerDataList;
    }

    public void setBannerDataList(List<BannerData> bannerDataList) {
        this.bannerDataList = bannerDataList;
    }

    public List<OrderClientNumber> getRecentClientNumberList() {
        return recentClientNumberList;
    }

    public void setRecentClientNumberList(List<OrderClientNumber> recentClientNumberList) {
        this.recentClientNumberList = recentClientNumberList;
    }

    public OrderClientNumber getLastOrderClientNumber() {
        return lastOrderClientNumber;
    }

    public void setLastOrderClientNumber(OrderClientNumber lastOrderClientNumber) {
        this.lastOrderClientNumber = lastOrderClientNumber;
    }

    public static final class Builder {
        private CategoryData categoryData;
        private List<BannerData> bannerDataList;
        private List<OrderClientNumber> recentClientNumberList;
        private OrderClientNumber recentClientNumber;

        public Builder() {
        }

        public Builder categoryData(CategoryData val) {
            categoryData = val;
            return this;
        }

        public Builder bannerDataList(List<BannerData> val) {
            bannerDataList = val;
            return this;
        }

        public Builder recentClientNumberList(List<OrderClientNumber> val) {
            recentClientNumberList = val;
            return this;
        }

        public Builder recentClientNumber(OrderClientNumber val) {
            recentClientNumber = val;
            return this;
        }

        public ProductDigitalData build() {
            return new ProductDigitalData(this);
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.categoryData, flags);
        dest.writeTypedList(this.bannerDataList);
        dest.writeTypedList(this.recentClientNumberList);
        dest.writeParcelable(this.lastOrderClientNumber, flags);
    }

    protected ProductDigitalData(Parcel in) {
        this.categoryData = in.readParcelable(CategoryData.class.getClassLoader());
        this.bannerDataList = in.createTypedArrayList(BannerData.CREATOR);
        this.recentClientNumberList = in.createTypedArrayList(OrderClientNumber.CREATOR);
        this.lastOrderClientNumber = in.readParcelable(OrderClientNumber.class.getClassLoader());
    }

    public static final Creator<ProductDigitalData> CREATOR = new Creator<ProductDigitalData>() {
        @Override
        public ProductDigitalData createFromParcel(Parcel source) {
            return new ProductDigitalData(source);
        }

        @Override
        public ProductDigitalData[] newArray(int size) {
            return new ProductDigitalData[size];
        }
    };


}
