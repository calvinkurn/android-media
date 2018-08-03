package com.tokopedia.topads.common.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hadi.putra on 17/05/18.
 */

public class DataCheckPromo {
    @SerializedName("ad_id")
    @Expose
    private String id;

    @SerializedName("ad_type")
    @Expose
    private int type;

    @SerializedName("shop_id")
    @Expose
    private String shopId;

    @SerializedName("dep_id")
    @Expose
    private String depId;

    @SerializedName("item_name")
    @Expose
    private String itemName;

    @SerializedName("item_image")
    @Expose
    private String itemImage;

    @SerializedName("is_enable_ad")
    @Expose
    private int isEnabled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }
}
