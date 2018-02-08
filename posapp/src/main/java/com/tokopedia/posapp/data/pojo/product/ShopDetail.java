package com.tokopedia.posapp.data.pojo.product;

/**
 * Created by okasurya on 10/17/17.
 */

public class ShopDetail {
    private int shopId;
    private String name;
    private String url;
    private boolean isGold;
    private String location;
    private String city;
    private String reputation;
    private String clover;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
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

    public boolean isGold() {
        return isGold;
    }

    public void setGold(boolean gold) {
        isGold = gold;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getReputation() {
        return reputation;
    }

    public void setReputation(String reputation) {
        this.reputation = reputation;
    }

    public String getClover() {
        return clover;
    }

    public void setClover(String clover) {
        this.clover = clover;
    }
}
