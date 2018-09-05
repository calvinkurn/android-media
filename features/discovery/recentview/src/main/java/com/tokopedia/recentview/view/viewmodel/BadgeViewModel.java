package com.tokopedia.recentview.view.viewmodel;

/**
 * @author by nisie on 7/4/17.
 */

public class BadgeViewModel {

    String title;

    String imgUrl;

    public BadgeViewModel(String title, String imgUrl) {
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
