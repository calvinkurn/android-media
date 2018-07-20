package com.tokopedia.explore.view.viewmodel;

/**
 * @author by milhamj on 20/07/18.
 */

public class ExploreImageViewModel {
    private String imageUrl;

    public ExploreImageViewModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
