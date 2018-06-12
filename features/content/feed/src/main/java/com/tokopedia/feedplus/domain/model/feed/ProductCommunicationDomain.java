package com.tokopedia.feedplus.domain.model.feed;

/**
 * @author by milhamj on 11/06/18.
 */

public class ProductCommunicationDomain {
    private String imageUrl;
    private String redirectUrl;

    public ProductCommunicationDomain(String imageUrl, String redirectUrl) {
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
