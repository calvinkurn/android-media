package com.tokopedia.core.gcm.domain.model;

import com.tokopedia.core.gcm.notification.applink.ApplinkTypeFactory;
import com.tokopedia.core.gcm.notification.applink.ApplinkVisitor;

import java.util.List;

/**
 * Created by alvarisi on 2/24/17.
 */

public class DiscussionPushNotification{
    private String thumbnail;
    private String username;
    private String applink;
    private String description;

    public DiscussionPushNotification() {
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
