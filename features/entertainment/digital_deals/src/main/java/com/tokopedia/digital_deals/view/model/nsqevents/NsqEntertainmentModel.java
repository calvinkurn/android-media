package com.tokopedia.digital_deals.view.model.nsqevents;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NsqEntertainmentModel implements Parcelable {

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("price_prefix")
    @Expose
    private String pricePrefix;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("app_url")
    @Expose
    private String appUrl;

    protected NsqEntertainmentModel(Parcel in) {
        value = in.readString();
        price = in.readString();
        imageUrl = in.readString();
        id = in.readString();
        pricePrefix = in.readString();
        url = in.readString();
        appUrl = in.readString();
    }

    public NsqEntertainmentModel() {}

    public static final Creator<NsqEntertainmentModel> CREATOR = new Creator<NsqEntertainmentModel>() {
        @Override
        public NsqEntertainmentModel createFromParcel(Parcel in) {
            return new NsqEntertainmentModel(in);
        }

        @Override
        public NsqEntertainmentModel[] newArray(int size) {
            return new NsqEntertainmentModel[size];
        }
    };

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPricePrefix() {
        return pricePrefix;
    }

    public void setPricePrefix(String pricePrefix) {
        this.pricePrefix = pricePrefix;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(value);
        dest.writeString(price);
        dest.writeString(imageUrl);
        dest.writeString(id);
        dest.writeString(pricePrefix);
        dest.writeString(url);
        dest.writeString(appUrl);
    }
}
