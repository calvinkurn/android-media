package com.tokopedia.gamification.floatingtoken.model;

import java.util.List;

/**
 * Created by nabillasabbaha on 3/28/18.
 */

public class TokenAsset {

    private String smallImgUrl;
    private List<String> imageUrls;
    private String spriteUrl;

    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public String getSpriteUrl() {
        return spriteUrl;
    }

    public void setSmallImgUrl(String smallImgUrl) {
        this.smallImgUrl = smallImgUrl;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void setSpriteUrl(String spriteUrl) {
        this.spriteUrl = spriteUrl;
    }
}
