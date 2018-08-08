package com.tokopedia.shop.common.graphql.model.shopbasicdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hendry on 08/08/18.
 */

public class ShopBasicData {
    public static final int STATUS_OPEN = 1;
    public static final int STATUS_CLOSED = 2;

    public static final int LEVEL_REGULAR = 0;
    public static final int LEVEL_GOLD = 2;
    public static final int LEVEL_OFFICIAL_STORE = 3;

    @SerializedName("domain")
    @Expose
    private String domain = "";
    @SerializedName("name")
    @Expose
    private String name = "";
    @SerializedName("status")
    @Expose
    private int status = STATUS_OPEN;
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
    private int level = LEVEL_REGULAR;
    @SerializedName("expired")
    @Expose
    private String expired = "";

    private boolean isOpen() { return status == STATUS_OPEN; }
    private boolean isClosed() { return status == STATUS_CLOSED; }
    private boolean isRegular() { return level == LEVEL_REGULAR; }
    private boolean isGold() { return level == LEVEL_GOLD; }
    private boolean isOfficialStore() { return level == LEVEL_OFFICIAL_STORE; }

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
}

