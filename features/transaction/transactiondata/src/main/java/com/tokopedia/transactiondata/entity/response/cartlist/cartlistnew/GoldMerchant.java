package com.tokopedia.transactiondata.entity.response.cartlist.cartlistnew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 16/08/18.
 */

public class GoldMerchant {

    @SerializedName("is_gold")
    @Expose
    private int isGold;
    @SerializedName("is_gold_badge")
    @Expose
    private boolean isGoldBadge;
    @SerializedName("gold_merchant_logo_url")
    @Expose
    private boolean goldMerchantLogoUrl;

    public int getIsGold() {
        return isGold;
    }

    public boolean isGoldBadge() {
        return isGoldBadge;
    }

    public boolean isGoldMerchantLogoUrl() {
        return goldMerchantLogoUrl;
    }
}
