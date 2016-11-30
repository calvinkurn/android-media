/*
 * Created By Kulomady on 11/25/16 11:06 PM
 * Copyright (c) 2016. All rights reserved
 *
 * Last Modified 11/25/16 11:06 PM
 */

package com.tokopedia.core.network.entity.topads;


/**
 * Created by ricoharisin on 6/9/16.
 */
public class TopAds {

    private String id;
    private String adRefKey;
    private String redirect;
    private String stickerId;
    private String stickerImage;
    private String productClickUrl;
    private String shopClickUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdRefKey() {
        return adRefKey;
    }

    public void setAdRefKey(String adRefKey) {
        this.adRefKey = adRefKey;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getStickerId() {
        return stickerId;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }

    public String getStickerImage() {
        return stickerImage;
    }

    public void setStickerImage(String stickerImage) {
        this.stickerImage = stickerImage;
    }

    public String getProductClickUrl() {
        return productClickUrl;
    }

    public void setProductClickUrl(String productClickUrl) {
        this.productClickUrl = productClickUrl;
    }

    public String getShopClickUrl() {
        return shopClickUrl;
    }

    public void setShopClickUrl(String shopClickUrl) {
        this.shopClickUrl = shopClickUrl;
    }

    public static TopAds from(TopAdsResponse.Data data){
        TopAds topAds = new TopAds();
        topAds.adRefKey = data.adRefKey;
        topAds.setId(data.id);
        topAds.setRedirect(data.redirect);
        topAds.setStickerId(data.stickerId);
        topAds.setStickerImage(data.stickerImage);
        topAds.setProductClickUrl(data.productClickUrl);
        topAds.setShopClickUrl(data.shopClickUrl);
        return topAds;
    }
}
