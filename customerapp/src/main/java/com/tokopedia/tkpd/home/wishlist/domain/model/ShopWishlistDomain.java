package com.tokopedia.tkpd.home.wishlist.domain.model;

/**
 * @author Kulomady on 2/20/17.
 */
public class ShopWishlistDomain {

    private String id;
    private String name;
    private String url;
    private boolean isGoldMerchant;
    private boolean isOfficial;
    private String location;
    private String status;
    private String luckiMerchant;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLuckiMerchant() {
        return luckiMerchant;
    }

    public void setLuckiMerchant(String luckiMerchant) {
        this.luckiMerchant = luckiMerchant;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }
}
