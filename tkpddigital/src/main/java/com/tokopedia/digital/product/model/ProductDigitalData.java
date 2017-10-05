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
    private List<BannerData> otherBannerDataList = new ArrayList<>();
    private HistoryClientNumber historyClientNumber;

    private ProductDigitalData(Builder builder) {
        setCategoryData(builder.categoryData);
        setBannerDataList(builder.bannerDataList);
        setOtherBannerDataList(builder.otherBannerDataList);
        setHistoryClientNumber(builder.historyClientNumber);
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

    public List<BannerData> getOtherBannerDataList() {
        return otherBannerDataList;
    }

    public void setOtherBannerDataList(List<BannerData> otherBannerDataList) {
        this.otherBannerDataList = otherBannerDataList;
    }

    public HistoryClientNumber getHistoryClientNumber() {
        return historyClientNumber;
    }

    public void setHistoryClientNumber(HistoryClientNumber historyClientNumber) {
        this.historyClientNumber = historyClientNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.categoryData, flags);
        dest.writeTypedList(this.bannerDataList);
        dest.writeTypedList(this.otherBannerDataList);
        dest.writeParcelable(this.historyClientNumber, flags);
    }

    public ProductDigitalData() {
    }

    protected ProductDigitalData(Parcel in) {
        this.categoryData = in.readParcelable(CategoryData.class.getClassLoader());
        this.bannerDataList = in.createTypedArrayList(BannerData.CREATOR);
        this.otherBannerDataList = in.createTypedArrayList(BannerData.CREATOR);
        this.historyClientNumber = in.readParcelable(HistoryClientNumber.class.getClassLoader());
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


    public static final class Builder {
        private CategoryData categoryData;
        private List<BannerData> bannerDataList;
        private List<BannerData> otherBannerDataList;
        private HistoryClientNumber historyClientNumber;

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

        public Builder otherBannerDataList(List<BannerData> val) {
            otherBannerDataList = val;
            return this;
        }

        public Builder historyClientNumber(HistoryClientNumber val) {
            historyClientNumber = val;
            return this;
        }

        public ProductDigitalData build() {
            return new ProductDigitalData(this);
        }
    }
}
