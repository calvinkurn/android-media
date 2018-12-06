package com.tokopedia.groupchat.chatroom.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by yfsx on 6/12/18.
 */

public class InteruptViewModel implements Parcelable {

    String bubbleTitle;
    String title;
    String imageUrl;
    String description;
    String ctaButton;
    String ctaUrl;
    boolean hasCloseButton;
    boolean hasHeader;

    public InteruptViewModel(String bubbleTitle, String title, String imageUrl, String description, String ctaButton, String ctaUrl, boolean hasCloseButton, boolean hasHeader) {
        this.bubbleTitle = bubbleTitle;
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.ctaButton = ctaButton;
        this.ctaUrl = ctaUrl;
        this.hasCloseButton = hasCloseButton;
        this.hasHeader = hasHeader;
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public String getBubbleTitle() {
        return bubbleTitle;
    }

    public void setBubbleTitle(String bubbleTitle) {
        this.bubbleTitle = bubbleTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCtaUrl() {
        return ctaUrl;
    }

    public void setCtaUrl(String ctaUrl) {
        this.ctaUrl = ctaUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCtaButton() {
        return ctaButton;
    }

    public void setCtaButton(String ctaButton) {
        this.ctaButton = ctaButton;
    }

    public boolean isHasCloseButton() {
        return hasCloseButton;
    }

    public void setHasCloseButton(boolean hasCloseButton) {
        this.hasCloseButton = hasCloseButton;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bubbleTitle);
        dest.writeString(this.title);
        dest.writeString(this.imageUrl);
        dest.writeString(this.description);
        dest.writeString(this.ctaButton);
        dest.writeString(this.ctaUrl);
        dest.writeByte(this.hasCloseButton ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasHeader ? (byte) 1 : (byte) 0);
    }

    protected InteruptViewModel(Parcel in) {
        this.bubbleTitle = in.readString();
        this.title = in.readString();
        this.imageUrl = in.readString();
        this.description = in.readString();
        this.ctaButton = in.readString();
        this.ctaUrl = in.readString();
        this.hasCloseButton = in.readByte() != 0;
        this.hasHeader = in.readByte() != 0;
    }

    public static final Creator<InteruptViewModel> CREATOR = new Creator<InteruptViewModel>() {
        @Override
        public InteruptViewModel createFromParcel(Parcel source) {
            return new InteruptViewModel(source);
        }

        @Override
        public InteruptViewModel[] newArray(int size) {
            return new InteruptViewModel[size];
        }
    };
}

