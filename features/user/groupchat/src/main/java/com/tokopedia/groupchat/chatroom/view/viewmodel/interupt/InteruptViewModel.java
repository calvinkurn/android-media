package com.tokopedia.groupchat.chatroom.view.viewmodel.interupt;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 14/12/18.
 */
public class InteruptViewModel implements Parcelable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("image_link")
    @Expose
    private String imageLink;
    @SerializedName("btn_title")
    @Expose
    private String btnTitle;
    @SerializedName("btn_link")
    @Expose
    private String btnLink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getBtnTitle() {
        return btnTitle;
    }

    public void setBtnTitle(String btnTitle) {
        this.btnTitle = btnTitle;
    }

    public String getBtnLink() {
        return btnLink;
    }

    public void setBtnLink(String btnLink) {
        this.btnLink = btnLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imageLink);
        dest.writeString(this.btnTitle);
        dest.writeString(this.btnLink);
    }

    public InteruptViewModel() {
    }

    public InteruptViewModel(String title, String description, String imageUrl, String imageLink, String btnTitle, String btnLink) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.imageLink = imageLink;
        this.btnTitle = btnTitle;
        this.btnLink = btnLink;
    }

    protected InteruptViewModel(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.imageUrl = in.readString();
        this.imageLink = in.readString();
        this.btnTitle = in.readString();
        this.btnLink = in.readString();
    }

    public static final Parcelable.Creator<InteruptViewModel> CREATOR = new Parcelable.Creator<InteruptViewModel>() {
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
