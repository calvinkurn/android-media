package com.tokopedia.notifications.model;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashwani Tyagi on 18/10/18.
 */
public class BaseNotificationModel {

    private int notificationId;
    private String title;
    private String detailMessage;
    private String message;
    private String icon;
    private String soundFileName;

    private String tribeKey;

    private Media media;

    private String appLink;
    private List<ActionButton> actionButton = new ArrayList<>();
    private JSONObject customValues;
    private String type;

    private String channelName;

    private List<PersistentButton> persistentButtonList;

    private JSONObject videoPushModel;

    private List<Carousal> carousalList = new ArrayList<>();

    private List<Grid> gridList = new ArrayList<>();
    private String subText;

    private String visualCollapsedImageUrl;
    private String visualExpandedImageUrl;

    private int carousalIndex = 0;
    private boolean vibration = true;
    private boolean sound = true;
    private boolean updateExisting;


    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
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

    public List<PersistentButton> getPersistentButtonList() {
        return persistentButtonList;
    }

    public void setPersistentButtonList(List<PersistentButton> persistentButtonList) {
        this.persistentButtonList = persistentButtonList;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSoundFileName() {
        return soundFileName;
    }

    public void setSoundFileName(String soundFileName) {
        this.soundFileName = soundFileName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getTribeKey() {
        return tribeKey;
    }

    public void setTribeKey(String tribeKey) {
        this.tribeKey = tribeKey;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public JSONObject getVideoPushModel() {
        return videoPushModel;
    }

    public void setVideoPushModel(JSONObject videoPushModel) {
        this.videoPushModel = videoPushModel;
    }

    public List<Carousal> getCarousalList() {
        return carousalList;
    }

    public void setCarousalList(List<Carousal> carousalList) {
        this.carousalList = carousalList;
    }

    public int getCarousalIndex() {
        return carousalIndex;
    }

    public void setCarousalIndex(int carousalIndex) {
        this.carousalIndex = carousalIndex;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean isUpdateExisting() {
        return updateExisting;
    }

    public void setUpdateExisting(boolean updateExisting) {
        this.updateExisting = updateExisting;
    }

    public List<Grid> getGridList() {
        return gridList;
    }

    public void setGridList(List<Grid> gridList) {
        this.gridList = gridList;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getVisualCollapsedImageUrl() {
        return visualCollapsedImageUrl;
    }

    public void setVisualCollapsedImageUrl(String visualCollapsedImageUrl) {
        this.visualCollapsedImageUrl = visualCollapsedImageUrl;
    }

    public String getVisualExpandedImageUrl() {
        return visualExpandedImageUrl;
    }

    public void setVisualExpandedImageUrl(String visualExpandedImageUrl) {
        this.visualExpandedImageUrl = visualExpandedImageUrl;
    }
}
