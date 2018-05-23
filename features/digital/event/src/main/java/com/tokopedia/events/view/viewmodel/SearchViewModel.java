package com.tokopedia.events.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pranaymohapatra on 25/01/18.
 */

public class SearchViewModel implements Parcelable {
    private String url;
    private String displayName;
    private String title;
    private int salesPrice;
    private String cityName;
    private int minStartDate;
    private int maxEndDate;
    private String imageApp;
    private int isTop;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(int salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public int getMinStartDate() {
        return minStartDate;
    }

    public void setMinStartDate(int minStartDate) {
        this.minStartDate = minStartDate;
    }

    public int getMaxEndDate() {
        return maxEndDate;
    }

    public void setMaxEndDate(int maxEndDate) {
        this.maxEndDate = maxEndDate;
    }

    public String getImageApp() {
        return imageApp;
    }

    public void setImageApp(String imageApp) {
        this.imageApp = imageApp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.displayName);
        dest.writeString(this.title);
        dest.writeInt(this.salesPrice);
        dest.writeString(this.cityName);
        dest.writeInt(this.minStartDate);
        dest.writeInt(this.maxEndDate);
        dest.writeString(this.imageApp);
        dest.writeInt(this.isTop);
    }

    public SearchViewModel() {
    }

    protected SearchViewModel(Parcel in) {
        this.url = in.readString();
        this.displayName = in.readString();
        this.title = in.readString();
        this.salesPrice = in.readInt();
        this.cityName = in.readString();
        this.minStartDate = in.readInt();
        this.maxEndDate = in.readInt();
        this.imageApp = in.readString();
        this.isTop = in.readInt();
    }

    public static final Parcelable.Creator<SearchViewModel> CREATOR = new Parcelable.Creator<SearchViewModel>() {
        @Override
        public SearchViewModel createFromParcel(Parcel source) {
            return new SearchViewModel(source);
        }

        @Override
        public SearchViewModel[] newArray(int size) {
            return new SearchViewModel[size];
        }
    };
}
