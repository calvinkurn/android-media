package com.tokopedia.digital.product.view.model;

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
    private List<GuideData> guideDataList = new ArrayList<>();
    private HistoryClientNumber historyClientNumber;

    private ProductDigitalData(Builder builder) {
        setCategoryData(builder.categoryData);
        setBannerDataList(builder.bannerDataList);
        setOtherBannerDataList(builder.otherBannerDataList);
        setHistoryClientNumber(builder.historyClientNumber);
        setGuideDataList(builder.guideDataList);
    }

    protected ProductDigitalData(Parcel in) {
        categoryData = in.readParcelable(CategoryData.class.getClassLoader());
        bannerDataList = in.createTypedArrayList(BannerData.CREATOR);
        otherBannerDataList = in.createTypedArrayList(BannerData.CREATOR);
        guideDataList = in.createTypedArrayList(GuideData.CREATOR);
        historyClientNumber = in.readParcelable(HistoryClientNumber.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(categoryData, flags);
        dest.writeTypedList(bannerDataList);
        dest.writeTypedList(otherBannerDataList);
        dest.writeTypedList(guideDataList);
        dest.writeParcelable(historyClientNumber, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductDigitalData> CREATOR = new Creator<ProductDigitalData>() {
        @Override
        public ProductDigitalData createFromParcel(Parcel in) {
            return new ProductDigitalData(in);
        }

        @Override
        public ProductDigitalData[] newArray(int size) {
            return new ProductDigitalData[size];
        }
    };

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

    public List<GuideData> getGuideDataList() {
        return guideDataList;
    }

    public void setGuideDataList(List<GuideData> guideDataList) {
        this.guideDataList = guideDataList;
    }

    public HistoryClientNumber getHistoryClientNumber() {
        return historyClientNumber;
    }

    public void setHistoryClientNumber(HistoryClientNumber historyClientNumber) {
        this.historyClientNumber = historyClientNumber;
    }

    public ProductDigitalData() {
    }

    public static final class Builder {
        private CategoryData categoryData;
        private List<BannerData> bannerDataList;
        private List<BannerData> otherBannerDataList;
        private List<GuideData> guideDataList;
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

        public Builder guideDataList(List<GuideData> val) {
            guideDataList = val;
            return this;
        }

        public ProductDigitalData build() {
            return new ProductDigitalData(this);
        }
    }

}
