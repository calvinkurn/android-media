package com.tokopedia.pushnotif.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackPushNotificationEntity {

    @SerializedName("notifier_sendWebhookPushNotification")
    @Expose
    private NotifierSendWebhookPushNotification notifierSendWebhookPushNotification;

}
