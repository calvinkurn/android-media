package com.tokopedia.discovery.catalog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by alvarisi on 10/18/16.
 */

public class CatalogDetailListLocation implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("total_data")
    @Expose
    private Integer totalData;

    protected CatalogDetailListLocation(Parcel in) {
        id = in.readString();
        name = in.readString();
        totalData = in.readByte() == 0x00 ? null : in.readInt();
    }

    public CatalogDetailListLocation() {
    }

    public static CatalogDetailListLocation createSelectionInfo(String title) {
        CatalogDetailListLocation location = new CatalogDetailListLocation();
        location.setName(title);
        location.setId("");
        location.setTotalData(0);
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        if (totalData == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalData);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CatalogDetailListLocation> CREATOR = new Parcelable.Creator<CatalogDetailListLocation>() {
        @Override
        public CatalogDetailListLocation createFromParcel(Parcel in) {
            return new CatalogDetailListLocation(in);
        }

        @Override
        public CatalogDetailListLocation[] newArray(int size) {
            return new CatalogDetailListLocation[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalData() {
        return totalData;
    }

    public void setTotalData(Integer totalData) {
        this.totalData = totalData;
    }
}
