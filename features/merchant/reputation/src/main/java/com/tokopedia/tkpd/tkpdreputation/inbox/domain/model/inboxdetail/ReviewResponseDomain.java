package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail;

/**
 * @author by nisie on 8/23/17.
 */

public class ReviewResponseDomain {
    private String responseMessage;
    private ResponseCreateTimeDomain responseCreateTime;
    private String responseBy;

    public ReviewResponseDomain(String responseMessage,
                                ResponseCreateTimeDomain responseCreateTime,
                                String responseBy) {
        this.responseMessage = responseMessage;
        this.responseCreateTime = responseCreateTime;
        this.responseBy = responseBy;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public ResponseCreateTimeDomain getResponseCreateTime() {
        return responseCreateTime;
    }

    public String getResponseBy() {
        return responseBy;
    }
}
