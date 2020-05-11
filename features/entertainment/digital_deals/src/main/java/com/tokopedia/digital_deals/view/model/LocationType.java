package com.tokopedia.digital_deals.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.library.baseadapter.BaseItem;

public class LocationType extends BaseItem implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("display_name")
    @Expose
    private String displayName;

    @SerializedName("search_radius")
    @Expose
    private String searchRadius;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("type_id")
    @Expose
    private int typeId;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("custom_text_1")
    @Expose
    private String customText;

    protected LocationType(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.displayName = in.readString();
        this.searchRadius = in.readString();
        this.icon = in.readString();
        this.typeId = in.readInt();
        this.status = in.readString();
        this.customText = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(String searchRadius) {
        this.searchRadius = searchRadius;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomText() {
        return customText;
    }

    public void setCustomText(String customText) {
        this.customText = customText;
    }

    public static final Creator<LocationType> CREATOR = new Creator<LocationType>() {
        @Override
        public LocationType createFromParcel(Parcel in) {
            return new LocationType(in);
        }

        @Override
        public LocationType[] newArray(int size) {
            return new LocationType[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(displayName);
        dest.writeString(searchRadius);
        dest.writeString(icon);
        dest.writeInt(typeId);
        dest.writeString(status);
        dest.writeString(customText);
    }
}
