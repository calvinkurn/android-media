package com.tokopedia.pushnotif.model;

import java.util.HashMap;
import java.util.Map;

public class ApplinkNotificationModel {

    private String desc;
    private int tkpCode;
    private String applinks;
    private String counter;
    private String toUserId;
    private String senderId;
    private String gId;
    private String thumbnail;
    private String fullName;
    private String summary;
    private Boolean loginRequired;
    private String createTime;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getTkpCode() {
        return tkpCode;
    }

    public void setTkpCode(int tkpCode) {
        this.tkpCode = tkpCode;
    }

    public String getApplinks() {
        return applinks;
    }

    public void setApplinks(String applinks) {
        this.applinks = applinks;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getGId() {
        return gId;
    }

    public void setGId(String gId) {
        this.gId = gId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Boolean getLoginRequired() {
        return loginRequired;
    }

    public void setLoginRequired(Boolean loginRequired) {
        this.loginRequired = loginRequired;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
