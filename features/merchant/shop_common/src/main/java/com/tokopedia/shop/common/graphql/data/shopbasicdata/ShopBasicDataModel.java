package com.tokopedia.shop.common.graphql.data.shopbasicdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.shop.common.constant.ShopStatusDef;
import com.tokopedia.shop.common.constant.ShopStatusLevelDef;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopBasicDataModel {

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

    public boolean isOpen() { return status == ShopStatusDef.OPEN; }
    public boolean isClosed() { return status == ShopStatusDef.CLOSED; }
    public boolean isRegular() { return level == ShopStatusLevelDef.LEVEL_REGULAR; }
    public boolean isGold() { return level == ShopStatusLevelDef.LEVEL_GOLD; }
    public boolean isOfficialStore() { return level == ShopStatusLevelDef.LEVEL_OFFICIAL_STORE; }
}

