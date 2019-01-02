package com.tokopedia.common_digital.cart.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PostPaidPopupAttribute implements Parcelable {
    private String title;
    private String content;
    private String imageUrl;
    private String confirmButtonTitle;

    protected PostPaidPopupAttribute(Parcel in) {
        title = in.readString();
        content = in.readString();
        imageUrl = in.readString();
        confirmButtonTitle = in.readString();
    }

    public static final Creator<PostPaidPopupAttribute> CREATOR = new Creator<PostPaidPopupAttribute>() {
        @Override
        public PostPaidPopupAttribute createFromParcel(Parcel in) {
            return new PostPaidPopupAttribute(in);
        }

        @Override
        public PostPaidPopupAttribute[] newArray(int size) {
            return new PostPaidPopupAttribute[size];
        }
    };

    public PostPaidPopupAttribute() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getConfirmButtonTitle() {
        return confirmButtonTitle;
    }

    public void setConfirmButtonTitle(String confirmButtonTitle) {
        this.confirmButtonTitle = confirmButtonTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(imageUrl);
        dest.writeString(confirmButtonTitle);
    }
}
