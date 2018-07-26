package com.tokopedia.feedplus.view.viewmodel.product;

/**
 * @author by nisie on 5/19/17.
 */

public class ProductCardHeaderViewModel {

    private String url;
    private Integer shopId;
    private String shopName;
    private String shopAvatar;
    private boolean isGoldMerchant;
    private String time;
    private boolean isOfficialStore;

    public ProductCardHeaderViewModel(Integer shopId,
                                      String url,
                                      String shopName,
                                      String shopAvatar,
                                      boolean isGoldMerchant,
                                      String postTime,
                                      boolean isOfficialStore) {
        this.shopId = shopId;
        this.url = url;
        this.shopName = shopName;
        this.shopAvatar = shopAvatar;
        this.isGoldMerchant = isGoldMerchant;
        this.time = postTime;
        this.isOfficialStore = isOfficialStore;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isOfficialStore() {
        return isOfficialStore;
    }

    public void setOfficialStore(boolean isOfficialStore) {
        this.isOfficialStore = isOfficialStore;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
