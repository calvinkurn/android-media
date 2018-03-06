
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewResponse {

    @SerializedName("response_message")
    @Expose
    private String responseMessage;
    @SerializedName("response_create_time")
    @Expose
    private ResponseCreateTime responseCreateTime;
    @SerializedName("response_by")
    @Expose
    private String responseBy;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public ResponseCreateTime getResponseCreateTime() {
        return responseCreateTime;
    }

    public void setResponseCreateTime(ResponseCreateTime responseCreateTime) {
        this.responseCreateTime = responseCreateTime;
    }

    public String getResponseBy() {
        return responseBy;
    }

    public void setResponseBy(String responseBy) {
        this.responseBy = responseBy;
    }

}
