package com.tokopedia.core.gcm.data.entity;

/**
 * Created by alvarisi on 1/17/17.
 */

public class NotificationEntity {
    private String title;
    private String code;

    public NotificationEntity() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
