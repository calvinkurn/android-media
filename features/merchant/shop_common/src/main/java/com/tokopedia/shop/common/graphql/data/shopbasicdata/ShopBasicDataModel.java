package com.tokopedia.shop.common.graphql.data.shopbasicdata;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.shop.common.constant.ShopStatusDef;
import com.tokopedia.shop.common.constant.ShopStatusLevelDef;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopBasicDataModel implements Parcelable {

    @SerializedName("domain")
    @Expose
    private String domain = "";
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("status")
    @Expose
    private int status = ShopStatusDef.OPEN;
    @SerializedName("closeSchedule")
    @Expose
    private String closeSchedule = "";
    @SerializedName("closeNote")
    @Expose
    private String closeNote = "";
    @SerializedName("openSchedule")
    @Expose
    private String openSchedule = "";
    @SerializedName("tagline")
    @Expose
    private String tagline = "";
    @SerializedName("description")
    @Expose
    private String description = "";
    @SerializedName("logo")
    @Expose
    private String logo = "";
    @SerializedName("level")
    @Expose
    private int level = ShopStatusLevelDef.LEVEL_REGULAR;
    @SerializedName("expired")
    @Expose
    private String expired = "";

    public String getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public String getCloseSchedule() {
        return closeSchedule.startsWith("-") ? "" : closeSchedule;
    }

    public String getOpenSchedule() {
        return openSchedule.startsWith("-") ? "" : openSchedule;
    }

    public String getTagline() {
        return tagline;
    }

    public String getDescription() {
        return description;
    }

    public String getLogo() {
        return logo;
    }

    public int getLevel() {
        return level;
    }

    public String getCloseNote() {
        return closeNote;
    }

    public String getExpired() {
        return expired;
    }

    public boolean isOpen() {
        return status == ShopStatusDef.OPEN;
    }

    public boolean isClosed() {
        return status == ShopStatusDef.CLOSED;
    }

    public boolean isRegular() {
        return level == ShopStatusLevelDef.LEVEL_REGULAR;
    }

    public boolean isGold() {
        return level == ShopStatusLevelDef.LEVEL_GOLD;
    }

    public boolean isOfficialStore() {
        return level == ShopStatusLevelDef.LEVEL_OFFICIAL_STORE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.domain);
        dest.writeString(this.name);
        dest.writeInt(this.status);
        dest.writeString(this.closeSchedule);
        dest.writeString(this.closeNote);
        dest.writeString(this.openSchedule);
        dest.writeString(this.tagline);
        dest.writeString(this.description);
        dest.writeString(this.logo);
        dest.writeInt(this.level);
        dest.writeString(this.expired);
    }

    public ShopBasicDataModel() {
    }

    protected ShopBasicDataModel(Parcel in) {
        this.domain = in.readString();
        this.name = in.readString();
        this.status = in.readInt();
        this.closeSchedule = in.readString();
        this.closeNote = in.readString();
        this.openSchedule = in.readString();
        this.tagline = in.readString();
        this.description = in.readString();
        this.logo = in.readString();
        this.level = in.readInt();
        this.expired = in.readString();
    }

    public static final Creator<ShopBasicDataModel> CREATOR = new Creator<ShopBasicDataModel>() {
        @Override
        public ShopBasicDataModel createFromParcel(Parcel source) {
            return new ShopBasicDataModel(source);
        }

        @Override
        public ShopBasicDataModel[] newArray(int size) {
            return new ShopBasicDataModel[size];
        }
    };
}

