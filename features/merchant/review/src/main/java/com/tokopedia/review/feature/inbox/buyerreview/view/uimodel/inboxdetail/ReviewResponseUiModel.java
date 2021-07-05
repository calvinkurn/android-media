package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail;

/**
 * @author by nisie on 9/27/17.
 */

public class ReviewResponseUiModel {
    private String responseMessage;
    private String responseCreateTime;
    private String responseBy;

    public ReviewResponseUiModel(String responseMessage, String responseCreateTime, String responseBy) {
        this.responseMessage = responseMessage;
        this.responseCreateTime = responseCreateTime;
        this.responseBy = responseBy;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getResponseCreateTime() {
        return responseCreateTime;
    }

    public String getResponseBy() {
        return responseBy;
    }
}
