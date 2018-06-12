package com.tokopedia.feedplus.view.viewmodel.kol;


/**
 * @author by milhamj on 08/05/18.
 */

public class ProductCommunicationItemViewModel {
    private String imageUrl;
    private String redirectUrl;

    public ProductCommunicationItemViewModel(String imageUrl, String redirectUrl) {
        this.imageUrl = imageUrl;
        this.redirectUrl = redirectUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
