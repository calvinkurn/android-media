package com.tokopedia.pushnotif.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotifierSendWebhookPushNotification {

    @SerializedName("is_success")
    @Expose
    private Integer isSuccess;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    
}
