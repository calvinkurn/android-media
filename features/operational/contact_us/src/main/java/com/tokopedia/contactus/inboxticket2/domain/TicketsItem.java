package com.tokopedia.contactus.inboxticket2.domain;

import com.google.gson.annotations.SerializedName;

public class TicketsItem {

    @SerializedName("url_detail")
    private String urlDetail;

    @SerializedName("last_message_plaintext")
    private String lastMessagePlaintext;

    @SerializedName("subject")
    private String subject;

    @SerializedName("read_status_id")
    private int readStatusId;

    @SerializedName("read_status")
    private String readStatus;

    @SerializedName("last_message")
    private String lastMessage;

    @SerializedName("message")
    private String message;

    @SerializedName("status_id")
    private int statusId;

    @SerializedName("create_time_fmt2")
    private String createTimeFmt2;

    @SerializedName("update_time_fmt2")
    private String updateTimeFmt2;

    @SerializedName("last_update")
    private String lastUpdate;

    @SerializedName("id")
    private String id;

    @SerializedName("status")
    private String status;

    @SerializedName("need_rating")
    private int needRating;

    @SerializedName("is_official_store")
    private String isOfficialStore;

    private boolean isSelectableMode;

    private boolean isSelected;

    public boolean isSelectableMode() {
        return isSelectableMode;
    }

    public void setSelectableMode(boolean selectableMode) {
        isSelectableMode = selectableMode;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setUrlDetail(String urlDetail) {
        this.urlDetail = urlDetail;
    }

    public String getUrlDetail() {
        return urlDetail;
    }

    public void setLastMessagePlaintext(String lastMessagePlaintext) {
        this.lastMessagePlaintext = lastMessagePlaintext;
    }

    public String getLastMessagePlaintext() {
        return lastMessagePlaintext;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setReadStatusId(int readStatusId) {
        this.readStatusId = readStatusId;
    }

    public int getReadStatusId() {
        return readStatusId;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setCreateTimeFmt2(String createTimeFmt2) {
        this.createTimeFmt2 = createTimeFmt2;
    }

    public String getCreateTimeFmt2() {
        return createTimeFmt2;
    }

    public void setUpdateTimeFmt2(String updateTimeFmt2) {
        this.updateTimeFmt2 = updateTimeFmt2;
    }

    public String getUpdateTimeFmt2() {
        return updateTimeFmt2;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setNeedRating(int needRating) {
        this.needRating = needRating;
    }

    public int getNeedRating() {
        return needRating;
    }

    public String getIsOfficialStore() {
        return isOfficialStore;
    }

    public void setIsOfficialStore(String isOfficialStore) {
        this.isOfficialStore = isOfficialStore;
    }
}