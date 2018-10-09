package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

/**
 * @author by nisie on 9/27/17.
 */

public class ReviewResponseViewModel {
    private String responseMessage;
    private String responseCreateTime;
    private String responseBy;

    public ReviewResponseViewModel(String responseMessage, String responseCreateTime, String responseBy) {
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
