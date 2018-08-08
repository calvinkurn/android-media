package com.tokopedia.tkpd.home.favorite.domain.model;

/**
 * @author Kulomady on 1/23/17.
 */
public class WishListBadge {

    private String title;
    private String imgUrl;

    public WishListBadge(String title, String imgUrl) {
        this.title = title;
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
