package com.tokopedia.transactiondata.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 26/12/18.
 */
public class Message {

    @SerializedName("message_info")
    @Expose
    private String messageInfo;
    @SerializedName("message_link")
    @Expose
    private String messageLink;
    @SerializedName("message_logo")
    @Expose
    private String messageLogo;

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    public String getMessageLink() {
        return messageLink;
    }

    public void setMessageLink(String messageLink) {
        this.messageLink = messageLink;
    }

    public String getMessageLogo() {
        return messageLogo;
    }

    public void setMessageLogo(String messageLogo) {
        this.messageLogo = messageLogo;
    }

}
