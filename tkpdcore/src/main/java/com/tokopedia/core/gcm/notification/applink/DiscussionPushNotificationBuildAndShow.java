package com.tokopedia.core.gcm.notification.applink;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.gcm.BuildAndShowNotification;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.gcm.NotificationConfiguration;
import com.tokopedia.core.gcm.domain.model.DiscussionPushNotification;
import com.tokopedia.core.gcm.domain.model.MessagePushNotification;
import com.tokopedia.core.gcm.model.ApplinkNotificationPass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 2/24/17.
 */

public class DiscussionPushNotificationBuildAndShow extends AbstractApplinkBuildAndShowNotification<DiscussionPushNotificationWrapper> {
    private List<DiscussionPushNotification> discussionPushNotifications;
    private static final String NOTIFICATION_TITLE = "Tokopedia - Diskusi";
    private static final String NOTIFICATION_GROUP = "personalized_group";
    private static final String NOTIFICATION_CATEGORY = "msg";

    public DiscussionPushNotificationBuildAndShow(DiscussionPushNotificationWrapper discussionPushNotificationWrapper) {
        discussionPushNotifications = discussionPushNotificationWrapper.getDiscussionPushNotifications();
    }

    @Override
    public void process(Context context, Intent handlerIntent) {
        process(context, handlerIntent, true);
    }

    @Override
    public void process(Context context, Intent handlerIntent, boolean isNew) {
        BuildAndShowNotification buildAndShowNotification = new BuildAndShowNotification(context);
        if (discussionPushNotifications.size() > 0) {
            int senderCount = 0;
            String senderId = null;
            String uri = null;
            String image = null;
            Boolean multipleSender = false;
            List<String> contents = new ArrayList<>();
            for (DiscussionPushNotification discussionPushNotification : discussionPushNotifications) {
                contents.add(String.format("%s : %s", discussionPushNotification.getUsername(), discussionPushNotification.getDescription()));
                if (uri == null) {
                    uri = discussionPushNotification.getApplink();
                    image = discussionPushNotification.getThumbnail();
                } else if (!discussionPushNotification.getApplink().equalsIgnoreCase(uri)) {
                    Uri url = Uri.parse(uri);
                    uri = String.format("%s://%s", url.getScheme(), url.getHost());
                }

                if (!discussionPushNotification.getSenderId().equalsIgnoreCase(senderId)) {
                    senderCount++;
                    senderId = discussionPushNotification.getSenderId();
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

            String description = String.format("%d diskusi produk", discussionPushNotifications.size());

            Uri url = Uri.parse(uri);
            handlerIntent.setData(url);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.EXTRA_PUSH_PERSONALIZATION, true);
            bundle.putString(Constants.EXTRA_APPLINK_CATEGORY, Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION);
            handlerIntent.putExtras(bundle);

            ApplinkNotificationPass.ApplinkNotificationPassBuilder builder =
                    ApplinkNotificationPass.ApplinkNotificationPassBuilder.builder();
            ApplinkNotificationPass applinkNotificationPass = builder.contents(contents)
                    .description(description)
                    .image(image)
                    .id(Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION_ID)
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
            notificationManager.cancel(Constants.ARG_NOTIFICATION_APPLINK_DISCUSSION_ID);
        }
    }
}
