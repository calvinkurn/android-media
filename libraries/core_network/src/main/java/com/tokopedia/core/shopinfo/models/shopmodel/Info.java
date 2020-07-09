
package com.tokopedia.core.shopinfo.models.shopmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.core.var.Badge;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class Info {

    private static final int SHOP_GOLD_MERCHANT_VALUE = 1;

    @SerializedName("shop_id")
    @Expose
    public String shopId;
    @SerializedName("shop_is_gold")
    @Expose
    public int shopIsGold;
    @SerializedName("shop_location")
    @Expose
    public String shopLocation;
    @SerializedName("shop_name")
    @Expose
    public String shopName;
    @SerializedName("shop_is_official")
    @Expose
    public int shopIsOfficial;
    @SerializedName("shop_avatar")
    @Expose
    public String shopAvatar;
    @SerializedName("shop_domain")
    @Expose
    public String shopDomain;
    @SerializedName("shop_is_free_returns")
    @Expose
    public String shopIsFreeReturns;

    @SerializedName("badges")
    @Expose
    public List<Badge> badges = new ArrayList<>();

    public String getShopId() {
        return shopId;
    }

    public boolean isGoldMerchant() {
        return shopIsGold == SHOP_GOLD_MERCHANT_VALUE;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    /**
     *
     * @return
     */
    public boolean isFreeReturns(){
        if(StringUtils.isNotBlank(shopIsFreeReturns) && Double.parseDouble(
                StringUtils.omitPunctuationAndDoubleSpace( shopIsFreeReturns)) > 0){
            return true;
        }
        return false;
    }

    public boolean isOfficialStore(){
        if(shopIsOfficial > 0){
            return true;
        }
        return false;
    }

}
