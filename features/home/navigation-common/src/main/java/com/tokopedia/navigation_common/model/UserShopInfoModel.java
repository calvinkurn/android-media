package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class UserShopInfoModel {
    @SerializedName("info")
    @Expose
    private ShopInfoModel info = new ShopInfoModel();
    @SerializedName("owner")
    @Expose
    private ShopOwnerModel owner = new ShopOwnerModel();

    public ShopInfoModel getInfo() {
        return info;
    }

    public void setInfo(ShopInfoModel info) {
        this.info = info;
    }

    public ShopOwnerModel getOwner() {
        return owner;
    }

    public void setOwner(ShopOwnerModel owner) {
        this.owner = owner;
    }
}
