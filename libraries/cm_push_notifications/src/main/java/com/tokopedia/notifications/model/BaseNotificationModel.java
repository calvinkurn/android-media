package com.tokopedia.notifications.model;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public class BaseNotificationModel {
    private String title;
    private String desc;
    private String message;
    private String applink;
    private String bigImageURL;
    private List<ActionButton> actionButton =new ArrayList<>();
    private JSONObject customValues;
    private String type;

    private PersistentNotificationData persistentNotificationData;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
    }

    public List<ActionButton> getActionButton() {
        return actionButton;
    }

    public void setActionButton(List<ActionButton> actionButton) {
        this.actionButton = actionButton;
    }

    public JSONObject getCustomValues() {
        return customValues;
    }

    public void setCustomValues(JSONObject customValues) {
        this.customValues = customValues;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBigImageURL() {
        return bigImageURL;
    }

    public void setBigImageURL(String bigImageURL) {
        this.bigImageURL = bigImageURL;
    }

    public PersistentNotificationData getPersistentNotificationData() {
        return persistentNotificationData;
    }

    public void setPersistentNotificationData(PersistentNotificationData persistentNotificationData) {
        this.persistentNotificationData = persistentNotificationData;
    }
}
