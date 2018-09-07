package com.tokopedia.topads.dashboard.data.model.data;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalAd {

    @SerializedName("total_shop_ad")
    @Expose
    private int totalShopAd;
    @SerializedName("total_product_ad")
    @Expose
    private int totalProductAd;
    @SerializedName("total_product_group_ad")
    @Expose
    private int totalProductGroupAd;
    @SerializedName("total_keyword")
    @Expose
    private int totalKeyword;

    public int getTotalShopAd() {
        return totalShopAd;
    }

    public void setTotalShopAd(int totalShopAd) {
        this.totalShopAd = totalShopAd;
    }

    public int getTotalProductAd() {
        return totalProductAd;
    }

    public void setTotalProductAd(int totalProductAd) {
        this.totalProductAd = totalProductAd;
    }

    public int getTotalProductGroupAd() {
        return totalProductGroupAd;
    }

    public void setTotalProductGroupAd(int totalProductGroupAd) {
        this.totalProductGroupAd = totalProductGroupAd;
    }

    public int getTotalKeyword() {
        return totalKeyword;
    }

    public void setTotalKeyword(int totalKeyword) {
        this.totalKeyword = totalKeyword;
    }
}
