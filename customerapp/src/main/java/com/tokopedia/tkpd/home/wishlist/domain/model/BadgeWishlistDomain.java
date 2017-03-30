package com.tokopedia.tkpd.home.wishlist.domain.model;

/**
 * @author Kulomady on 2/20/17.
 */
public class BadgeWishlistDomain {

    private String title;
    private String imageUrl;
    private String imgUrl;
    //sometimes it's different at ws (sometimes image_url and sometimes imgurl)


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
