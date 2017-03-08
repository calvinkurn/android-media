package com.tokopedia.core.gcm.notification.applink;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.gcm.BuildAndShowNotification;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.domain.model.MessagePushNotification;
import com.tokopedia.core.gcm.model.ApplinkNotificationPass;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 2/23/17.
 */

public class MessagePushNotificationBuildAndShow extends AbstractApplinkBuildAndShowNotification<List<MessagePushNotification>> {
    private static final String NOTIFICATION_TITLE = "Tokopedia - Pesan Baru";
    private static final String NOTIFICATION_GROUP = "personalized_group";
    private static final String NOTIFICATION_CATEGORY = "msg";

    private List<MessagePushNotification> discussionPushNotifications;
    BuildAndShowNotification buildAndShowNotification;

    public MessagePushNotificationBuildAndShow(List<MessagePushNotification> discussionPushNotifications) {
        this.discussionPushNotifications = discussionPushNotifications;
    }

    @Override
    public void process(Context context, Intent handlerIntent) {
        buildAndShowNotification = new BuildAndShowNotification(context);
        if (discussionPushNotifications.size() > 0) {
            boolean isSingle = true;
            int senderCount = 0;
            String username = null;
            String uri = null;
            String image = null;
            Boolean multipleSender = false;
            List<String> contents = new ArrayList<>();
            for (MessagePushNotification messagePushNotification : discussionPushNotifications) {
                contents.add(messagePushNotification.getUsername() + " : " + messagePushNotification.getDescription());
                if (uri == null) {
                    uri = messagePushNotification.getApplink();
                    image = messagePushNotification.getThumbnail();
                } else if (!messagePushNotification.getApplink().equalsIgnoreCase(uri)) {
                    if (isSingle) {
                        Uri url = Uri.parse(uri);
                        uri = url.getScheme() + "://" + url.getHost();
                    }
                    isSingle = false;
                }

                if (!messagePushNotification.getUsername().equalsIgnoreCase(username)) {
                    senderCount++;
                    username = messagePushNotification.getUsername();
                }
            }

            if (!isSingle) {
                multipleSender = true;
            }
            String description;
            if (!isSingle){
                description = String.format("%d pesan dari %d pengirim", discussionPushNotifications.size(), senderCount);
            }else{
                description = String.format("%d pesan dari %d", discussionPushNotifications.size(), username);
            }

            Uri url = Uri.parse(uri);
            handlerIntent.setData(url);
            Bundle bundle = new Bundle();
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
            buildAndShowNotification.buildAndShowNotification(applinkNotificationPass);
        } else {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(Constants.ARG_NOTIFICATION_APPLINK_MESSAGE_ID);
        }

    }
}
