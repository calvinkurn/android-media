package com.tokopedia.feedplus.view.viewmodel.kol;


/**
 * @author by milhamj on 08/05/18.
 */

public class ProductCommunicationItemViewModel {
    private int activityId;
    private String imageUrl;
    private String redirectUrl;

    public ProductCommunicationItemViewModel(int activityId, String imageUrl, String redirectUrl) {
        this.activityId = activityId;
        this.imageUrl = imageUrl;
        this.redirectUrl = redirectUrl;
    }

    public int getActivityId() {
        return activityId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
