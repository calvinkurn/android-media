package com.tokopedia.core.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 10/17/16.
 */

public class CatalogInfo implements Parcelable {
    @SerializedName("catalog_price")
    @Expose
    private CatalogPrice catalogPrice;
    @SerializedName("catalog_key")
    @Expose
    private String catalogKey;
    @SerializedName("catalog_url")
    @Expose
    private String catalogUrl;
    @SerializedName("catalog_id")
    @Expose
    private String catalogId;
    @SerializedName("catalog_department_id")
    @Expose
    private String catalogDepartmentId;
    @SerializedName("catalog_images")
    @Expose
    private List<CatalogImage> catalogImageList = new ArrayList<CatalogImage>();
    @SerializedName("catalog_description")
    @Expose
    private String catalogDescription;
    @SerializedName("catalog_name")
    @Expose
    private String catalogName;

    public CatalogPrice getCatalogPrice() {
        return catalogPrice;
    }

    public String getCatalogKey() {
        return catalogKey;
    }

    public String getCatalogUrl() {
        return catalogUrl;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public String getCatalogDepartmentId() {
        return catalogDepartmentId;
    }

    public List<CatalogImage> getCatalogImageList() {
        return catalogImageList;
    }

    public String getCatalogDescription() {
        return catalogDescription;
    }

    public String getCatalogName() {
        return catalogName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.catalogPrice, flags);
        dest.writeString(this.catalogKey);
        dest.writeString(this.catalogUrl);
        dest.writeString(this.catalogId);
        dest.writeString(this.catalogDepartmentId);
        dest.writeTypedList(this.catalogImageList);
        dest.writeString(this.catalogDescription);
        dest.writeString(this.catalogName);
    }

    public CatalogInfo() {
    }

    protected CatalogInfo(Parcel in) {
        this.catalogPrice = in.readParcelable(CatalogPrice.class.getClassLoader());
        this.catalogKey = in.readString();
        this.catalogUrl = in.readString();
        this.catalogId = in.readString();
        this.catalogDepartmentId = in.readString();
        this.catalogImageList = in.createTypedArrayList(CatalogImage.CREATOR);
        this.catalogDescription = in.readString();
        this.catalogName = in.readString();
    }

    public static final Parcelable.Creator<CatalogInfo> CREATOR = new Parcelable.Creator<CatalogInfo>() {
        @Override
        public CatalogInfo createFromParcel(Parcel source) {
            return new CatalogInfo(source);
        }

        @Override
        public CatalogInfo[] newArray(int size) {
            return new CatalogInfo[size];
        }
    };
}
