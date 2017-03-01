package com.tokopedia.core.gcm.notification.applink;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.gcm.BuildAndShowNotification;
import com.tokopedia.core.gcm.domain.model.MessagePushNotification;
import com.tokopedia.core.gcm.model.ApplinkNotificationPass;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 2/23/17.
 */

public class MessagePushNotificationBuildAndShow extends AbstractApplinkBuildAndShowNotification<List<MessagePushNotification>> {

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
            List<String> contents = new ArrayList<>();
            for (MessagePushNotification messagePushNotification : discussionPushNotifications) {
                contents.add(messagePushNotification.getUsername() + " : " + messagePushNotification.getDescription());
                if (uri == null) {
                    uri = messagePushNotification.getApplink();
                    image = messagePushNotification.getThumbnail();
                } else if (!messagePushNotification.getApplink().equalsIgnoreCase(uri)) {
                    if (isSingle) {
                        try {
                            URL url = new URL(uri);
                            uri = url.getProtocol() + "://" + url.getHost();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                    isSingle = false;
                }

                if (!messagePushNotification.getUsername().equalsIgnoreCase(username)){
                    senderCount++;
                    username = messagePushNotification.getUsername();
                }
            }

            if (!isSingle){
                image = "https://ecs7.tokopedia.net/img/microsite-brand-resource/mascot-toped-new.png";
            }
            String description = null;
            if (discussionPushNotifications.size() >1){
                description = String.format("%d pesan dari %d pengirim", discussionPushNotifications.size(), senderCount);
            }else {
                description = String.format("%d pesan", discussionPushNotifications.size());
            }

            ApplinkNotificationPass.ApplinkNotificationPassBuilder builder =
                    ApplinkNotificationPass.ApplinkNotificationPassBuilder.builder();
            ApplinkNotificationPass applinkNotificationPass = builder.contents(contents)
                    .description(description)
                    .icon("https://ecs7.tokopedia.net/img/microsite-brand-resource/mascot-toped-new.png")
                    .image(image)
                    .id(1001)
                    .title("Tokopedia - Pesan Baru")
                    .intent(handlerIntent)
                    .applink(uri)
                    .build();
            buildAndShowNotification.buildAndShowNotification(applinkNotificationPass);
        }

    }
}
