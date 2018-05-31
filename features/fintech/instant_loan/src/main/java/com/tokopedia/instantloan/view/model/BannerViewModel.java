package com.tokopedia.instantloan.view.model;

/**
 * Created by lavekush on 22/03/18.
 */

public class BannerViewModel {

    private String image;
    private String link;

    public BannerViewModel(String image, String link) {
        this.image = image;
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
