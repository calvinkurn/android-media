package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.SerializedName;

public class VccUserStatus {
    @SerializedName("status")
    private String status;

    @SerializedName("redirection_url")
    private String redirectionUrl;

    @SerializedName("message_header")
    private String messageHeader;

    @SerializedName("message_body")
    private String messageBody;

    @SerializedName("message_button_name")
    private String messageButtonName;

    @SerializedName("message_url")
    private String messageUrl;

    @SerializedName("icon")
    private String icon;

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRedirectionUrl() {
        return redirectionUrl;
    }

    public void setRedirectionUrl(String redirectionUrl) {
        this.redirectionUrl = redirectionUrl;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageButtonName() {
        return messageButtonName;
    }

    public void setMessageButtonName(String messageButtonName) {
        this.messageButtonName = messageButtonName;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "VccUserStatus{" +
                "status='" + status + '\'' +
                ", redirectionUrl='" + redirectionUrl + '\'' +
                ", messageHeader='" + messageHeader + '\'' +
                ", messageBody='" + messageBody + '\'' +
                ", messageButtonName='" + messageButtonName + '\'' +
                ", messageUrl='" + messageUrl + '\'' +
                ", icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
