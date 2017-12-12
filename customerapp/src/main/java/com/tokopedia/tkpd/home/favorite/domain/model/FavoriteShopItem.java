package com.tokopedia.tkpd.home.favorite.domain.model;

/**
 * @author Kulomady on 1/23/17.
 */
public class FavoriteShopItem {

    private String name;
    private String iconUri;
    private String coverUri;
    private String location;
    private boolean isFav;
    private String id;
    private String adKey;
    private String adR;
    private String shopClickUrl;
    private String badgeUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }

    public String getCoverUri() {
        return coverUri;
    }

    public void setCoverUri(String coverUri) {
        this.coverUri = coverUri;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdKey() {
        return adKey;
    }

    public void setAdKey(String adKey) {
        this.adKey = adKey;
    }

    public String getAdR() {
        return adR;
    }

    public void setAdR(String adR) {
        this.adR = adR;
    }

    public String getShopClickUrl() {
        return shopClickUrl;
    }

    public void setShopClickUrl(String shopClickUrl) {
        this.shopClickUrl = shopClickUrl;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }

    public void setBadgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
    }
}
