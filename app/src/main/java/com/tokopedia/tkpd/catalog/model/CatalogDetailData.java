package com.tokopedia.tkpd.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogDetailData implements Parcelable {

    @SerializedName("catalog_review")
    @Expose
    private CatalogReview catalogReview;
    @SerializedName("catalog_info")
    @Expose
    private CatalogInfo catalogInfo;
    @SerializedName("catalog_market_price")
    @Expose
    private CatalogMarketPrice catalogMarketPrice;
    @SerializedName("catalog_shops")
    @Expose
    private List<CatalogShop> catalogShopList = new ArrayList<>();
    @SerializedName("catalog_image")
    @Expose
    private String catalogImage;
    @SerializedName("catalog_location")
    @Expose
    private List<CatalogLocation> catalogLocationList = new ArrayList<>();
    @SerializedName("catalog_specs")
    @Expose
    private List<CatalogSpec> catalogSpecList = new ArrayList<>();

    public CatalogReview getCatalogReview() {
        return catalogReview;
    }

    public CatalogInfo getCatalogInfo() {
        return catalogInfo;
    }

    public CatalogMarketPrice getCatalogMarketPrice() {
        return catalogMarketPrice;
    }

    public List<CatalogShop> getCatalogShopList() {
        return catalogShopList;
    }

    public String getCatalogImage() {
        return catalogImage;
    }

    public List<CatalogLocation> getCatalogLocationList() {
        return catalogLocationList;
    }

    public List<CatalogSpec> getCatalogSpecList() {
        return catalogSpecList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.catalogReview, flags);
        dest.writeParcelable(this.catalogInfo, flags);
        dest.writeParcelable(this.catalogMarketPrice, flags);
        dest.writeList(this.catalogShopList);
        dest.writeString(this.catalogImage);
        dest.writeList(this.catalogLocationList);
        dest.writeList(this.catalogSpecList);
    }

    public CatalogDetailData() {
    }

    protected CatalogDetailData(Parcel in) {
        this.catalogReview = in.readParcelable(CatalogReview.class.getClassLoader());
        this.catalogInfo = in.readParcelable(CatalogInfo.class.getClassLoader());
        this.catalogMarketPrice = in.readParcelable(CatalogMarketPrice.class.getClassLoader());
        this.catalogShopList = new ArrayList<CatalogShop>();
        in.readList(this.catalogShopList, CatalogShop.class.getClassLoader());
        this.catalogImage = in.readString();
        this.catalogLocationList = new ArrayList<CatalogLocation>();
        in.readList(this.catalogLocationList, CatalogLocation.class.getClassLoader());
        this.catalogSpecList = new ArrayList<CatalogSpec>();
        in.readList(this.catalogSpecList, CatalogSpec.class.getClassLoader());
    }

    public static final Parcelable.Creator<CatalogDetailData> CREATOR = new Parcelable.Creator<CatalogDetailData>() {
        @Override
        public CatalogDetailData createFromParcel(Parcel source) {
            return new CatalogDetailData(source);
        }

        @Override
        public CatalogDetailData[] newArray(int size) {
            return new CatalogDetailData[size];
        }
    };
}
