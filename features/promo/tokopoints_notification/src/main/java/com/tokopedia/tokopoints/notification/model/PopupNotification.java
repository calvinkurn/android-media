package com.tokopedia.tokopoints.notification.model;

import com.google.gson.annotations.SerializedName;

public class PopupNotification {

    @SerializedName("title")
    private String title;

    @SerializedName("text")
    private String text;

    @SerializedName("imageURL")
    private String imageURL;

    @SerializedName("buttonText")
    private String buttonText;

    @SerializedName("buttonURL")
    private String buttonURL;

    @SerializedName("appLink")
    private String appLink;

    @SerializedName("notes")
    private String notes;

    @SerializedName("sender")
    private String sender;

    @SerializedName("catalog")
    private CouponValueEntity catalog = new CouponValueEntity();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public String getButtonURL() {
        return buttonURL;
    }

    public void setButtonURL(String buttonURL) {
        this.buttonURL = buttonURL;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public CouponValueEntity getCatalog() {
        return catalog;
    }

    public void setCatalog(CouponValueEntity catalog) {
        this.catalog = catalog;
    }

    public boolean isEmpty() {
        if (catalog == null || catalog.isEmpty()) {
            return title.isEmpty();
        }

        return catalog.isEmpty();
    }

    @Override
    public String toString() {
        return "PopupNotification{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", buttonText='" + buttonText + '\'' +
                ", buttonURL='" + buttonURL + '\'' +
                ", appLink='" + appLink + '\'' +
                ", notes='" + notes + '\'' +
                ", sender='" + sender + '\'' +
                ", catalog=" + catalog +
                '}';
    }
}
