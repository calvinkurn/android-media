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

public class ShopBasicDataViewModel implements Parcelable {
    private String domain = "";
    private String name = "";
    private int status = ShopStatusDef.OPEN;
    private String closeSchedule = "";
    private String openSchedule = "";
    private String tagline = "";
    private String description = "";
    private String logo = "";
    private int level = ShopStatusLevelDef.LEVEL_REGULAR;
    private String expired = "";

    public ShopBasicDataViewModel(ShopBasicDataModel shopBasicDataModel) {
        this.domain = shopBasicDataModel.getDomain();
        this.name = shopBasicDataModel.getName();
        this.status = shopBasicDataModel.getStatus();
        this.closeSchedule = shopBasicDataModel.getCloseSchedule();
        this.openSchedule = shopBasicDataModel.getOpenSchedule();
        this.tagline = shopBasicDataModel.getTagline();
        this.description = shopBasicDataModel.getDescription();
        this.logo = shopBasicDataModel.getLogo();
        this.level = shopBasicDataModel.getLevel();
        this.expired = shopBasicDataModel.getExpired();
    }

    private boolean isOpen() { return status == ShopStatusDef.OPEN; }
    private boolean isClosed() { return status == ShopStatusDef.CLOSED; }
    private boolean isRegular() { return level == ShopStatusLevelDef.LEVEL_REGULAR; }
    private boolean isGold() { return level == ShopStatusLevelDef.LEVEL_GOLD; }
    private boolean isOfficialStore() { return level == ShopStatusLevelDef.LEVEL_OFFICIAL_STORE; }

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
        return closeSchedule;
    }

    public String getOpenSchedule() {
        return openSchedule;
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

    public String getExpired() {
        return expired;
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
        dest.writeString(this.openSchedule);
        dest.writeString(this.tagline);
        dest.writeString(this.description);
        dest.writeString(this.logo);
        dest.writeInt(this.level);
        dest.writeString(this.expired);
    }

    protected ShopBasicDataViewModel(Parcel in) {
        this.domain = in.readString();
        this.name = in.readString();
        this.status = in.readInt();
        this.closeSchedule = in.readString();
        this.openSchedule = in.readString();
        this.tagline = in.readString();
        this.description = in.readString();
        this.logo = in.readString();
        this.level = in.readInt();
        this.expired = in.readString();
    }

    public static final Creator<ShopBasicDataViewModel> CREATOR = new Creator<ShopBasicDataViewModel>() {
        @Override
        public ShopBasicDataViewModel createFromParcel(Parcel source) {
            return new ShopBasicDataViewModel(source);
        }

        @Override
        public ShopBasicDataViewModel[] newArray(int size) {
            return new ShopBasicDataViewModel[size];
        }
    };
}

