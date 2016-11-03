
package com.tokopedia.tkpd.inboxreputation.model.inboxreputationdetail;

import android.text.Html;
import android.text.Spanned;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@org.parceler.Parcel
public class ReviewResponse {

    @SerializedName("is_response_read")
    @Expose
    int isResponseRead;
    @SerializedName("response_create_time")
    @Expose
    String responseTime;
    @SerializedName("response_message")
    @Expose
    String responseMessage;

    /**
     * 
     * @return
     *     The isResponseRead
     */
    public boolean getIsResponseRead() {
        return isResponseRead == 1;
    }

    /**
     * 
     * @param isResponseRead
     *     The is_response_read
     */
    public void setIsResponseRead(int isResponseRead) {
        this.isResponseRead = isResponseRead;
    }

    /**
     * 
     * @return
     *     The responseTime
     */
    public String getResponseTime() {
        return responseTime;
    }

    /**
     * 
     * @param responseTime
     *     The response_time
     */
    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    /**
     * 
     * @return
     *     The responseMessage
     */
    public Spanned getResponseMessage() {
        return Html.fromHtml(responseMessage);
    }

    /**
     * 
     * @param responseMessage
     *     The response_message
     */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}
