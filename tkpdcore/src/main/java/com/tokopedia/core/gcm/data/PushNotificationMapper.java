package com.tokopedia.core.gcm.data;

import com.google.gson.Gson;
import com.tokopedia.core.gcm.data.source.entity.DiscussionPushNotificationResponse;
import com.tokopedia.core.gcm.data.source.entity.MessagePushNotificationResponse;
import com.tokopedia.core.gcm.domain.PushNotification;
import com.tokopedia.core.gcm.domain.model.DiscussionPushNotification;
import com.tokopedia.core.gcm.domain.model.MessagePushNotification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alvarisi on 2/23/17.
 */

public class PushNotificationMapper {
    private final Gson gson;

    public PushNotificationMapper(Gson gson) {
        this.gson = gson;
    }

    public MessagePushNotification transformMessage(PushNotification pushNotification) {
        MessagePushNotificationResponse response = transformMessageResponse(pushNotification);
        MessagePushNotification messagePushNotification = null;
        if(response != null) {
            messagePushNotification = new MessagePushNotification();
            messagePushNotification.setApplink(response.getApplink());
            messagePushNotification.setThumbnail(response.getThumbnail());
            messagePushNotification.setDescription(response.getDescription());
            messagePushNotification.setUsername(response.getFullname());
            messagePushNotification.setSenderId(response.getSenderId());
        }
        return messagePushNotification;
    }

    public MessagePushNotificationResponse transformMessageResponse(PushNotification pushNotification){
        return gson
                .fromJson(
                        pushNotification.getResponse(),
                        MessagePushNotificationResponse.class
                );
    }
    public List<MessagePushNotification> transformMessage(List<PushNotification> pushNotifications){
        List<MessagePushNotification> messagePushNotifications = new ArrayList<>();
        MessagePushNotification messagePushNotification;
        for (PushNotification pushNotification : pushNotifications){
            messagePushNotification = transformMessage(pushNotification);
            if (messagePushNotification != null)
                messagePushNotifications.add(messagePushNotification);
        }
        return messagePushNotifications;
    }

    public List<DiscussionPushNotification> transformDiscussion(List<PushNotification> pushNotifications) {
        List<DiscussionPushNotification> messagePushNotifications = new ArrayList<>();
        DiscussionPushNotification messagePushNotification;
        for (PushNotification pushNotification : pushNotifications){
            messagePushNotification = transformDiscussion(pushNotification);
            if (messagePushNotification != null)
                messagePushNotifications.add(messagePushNotification);
        }
        return messagePushNotifications;
    }

    private DiscussionPushNotification transformDiscussion(PushNotification pushNotification) {
        DiscussionPushNotificationResponse response = transformDiscussionResponse(pushNotification);
        DiscussionPushNotification discussionPushNotification = null;
        if(response != null) {
            discussionPushNotification = new DiscussionPushNotification();
            discussionPushNotification.setApplink(response.getApplink());
            discussionPushNotification.setThumbnail(response.getThumbnail());
            discussionPushNotification.setDescription(response.getDescription());
            discussionPushNotification.setUsername(response.getFullname());
            discussionPushNotification.setSenderId(response.getSenderId());
        }
        return discussionPushNotification;
    }

    public DiscussionPushNotificationResponse transformDiscussionResponse(PushNotification pushNotification) {
        return gson
                .fromJson(
                        pushNotification.getResponse(),
                        DiscussionPushNotificationResponse.class
                );
    }
}
