package com.tokopedia.pushnotif.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackPushNotificationEntity {

    @SerializedName("notifier_sendWebhookPushNotification")
    @Expose
    private NotifierSendWebhookPushNotification notifierSendWebhookPushNotification;

}
