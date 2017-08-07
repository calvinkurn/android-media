package com.tokopedia.core.gcm.notification.applink;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.gcm.BuildAndShowNotification;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationConfiguration;
import com.tokopedia.core.gcm.domain.model.MessagePushNotification;
import com.tokopedia.core.gcm.model.ApplinkNotificationPass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 2/23/17.
 */

public class MessagePushNotificationBuildAndShow extends AbstractApplinkBuildAndShowNotification<List<MessagePushNotification>> {
    private static final String NOTIFICATION_TITLE = "Tokopedia - Pesan Baru";
    private static final String NOTIFICATION_GROUP = "personalized_group";
    private static final String NOTIFICATION_CATEGORY = "msg";

    private List<MessagePushNotification> messagePushNotifications;

    public MessagePushNotificationBuildAndShow(List<MessagePushNotification> messagePushNotifications) {
        this.messagePushNotifications = messagePushNotifications;
    }

    @Override
    public void process(Context context, Intent handlerIntent, boolean isNew) {
        BuildAndShowNotification buildAndShowNotification = new BuildAndShowNotification(context);
        if (messagePushNotifications.size() > 0) {
            int senderCount = 0;
            String username = null;
            String senderId = null;
            String uri = null;
            String image = null;
            Boolean multipleSender = false;
            List<String> contents = new ArrayList<>();
            for (MessagePushNotification messagePushNotification : messagePushNotifications) {
                contents.add(String.format("%s : %s", messagePushNotification.getUsername(), messagePushNotification.getDescription()));
                if (uri == null) {
                    uri = messagePushNotification.getApplink();
                    image = messagePushNotification.getThumbnail();
                } else if (!messagePushNotification.getApplink().equalsIgnoreCase(uri)) {
                    Uri url = Uri.parse(uri);
                    uri = String.format("%s://%s", url.getScheme(), url.getHost());
                }

                if (!messagePushNotification.getSenderId().equalsIgnoreCase(senderId)) {
                    senderCount++;
                    senderId = messagePushNotification.getSenderId();
                    username = messagePushNotification.getUsername();
                }
            }


            NotificationConfiguration configuration = buildDefaultConfiguration(context);
            if (!isNew) {
                configuration.setBell(false);
                configuration.setVibrate(false);
            }

            if (senderCount > 1) {
                multipleSender = true;
                configuration.setNetworkIcon(false);
            } else {
                configuration.setNetworkIcon(true);
            }

            String description;
            if (senderCount > 1) {
                description = String.format("%d pesan dari %d pengirim", messagePushNotifications.size(), senderCount);
            } else {
                description = String.format("%d pesan dari %s", messagePushNotifications.size(), username);
            }

            Uri url = Uri.parse(uri);
            handlerIntent.setData(url);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.EXTRA_PUSH_PERSONALIZATION, true);
            bundle.putString(Constants.EXTRA_APPLINK_CATEGORY, Constants.ARG_NOTIFICATION_APPLINK_MESSAGE);
            handlerIntent.putExtras(bundle);

            ApplinkNotificationPass.ApplinkNotificationPassBuilder builder =
                    ApplinkNotificationPass.ApplinkNotificationPassBuilder.builder();

            ApplinkNotificationPass applinkNotificationPass = builder.contents(contents)
                    .description(description)
                    .image(image)
                    .id(Constants.ARG_NOTIFICATION_APPLINK_MESSAGE_ID)
                    .title(NOTIFICATION_TITLE)
                    .group(NOTIFICATION_GROUP)
                    .category(NOTIFICATION_CATEGORY)
                    .intent(handlerIntent)
                    .applink(uri)
                    .multipleSender(multipleSender)
                    .build();
            buildAndShowNotification.buildAndShowNotification(applinkNotificationPass, configuration);
        } else {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Constants.ARG_NOTIFICATION_APPLINK_MESSAGE_ID);
        }
    }

    @Override
    public void process(Context context, Intent handlerIntent) {
        process(context, handlerIntent, true);
    }
}
