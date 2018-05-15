package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

/**
 * @author by steven on 5/3/18.
 */

public class PinnedMessageViewModel implements Visitable<GroupChatTypeFactory>, Parcelable {

    public static final String TYPE = "pinned_message";

    String message;
    String adminName;
    String imageUrl;
    String thumbnail;

    public PinnedMessageViewModel(String message, String adminName, String imageUrl, String thumbnail) {
        this.message = message;
        this.adminName = adminName;
        this.imageUrl = imageUrl;
        this.thumbnail = thumbnail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeString(this.adminName);
        dest.writeString(this.imageUrl);
        dest.writeString(this.thumbnail);
    }

    protected PinnedMessageViewModel(Parcel in) {
        this.message = in.readString();
        this.adminName = in.readString();
        this.imageUrl = in.readString();
        this.thumbnail = in.readString();
    }

    public static final Parcelable.Creator<PinnedMessageViewModel> CREATOR = new Parcelable.Creator<PinnedMessageViewModel>() {
        @Override
        public PinnedMessageViewModel createFromParcel(Parcel source) {
            return new PinnedMessageViewModel(source);
        }

        @Override
        public PinnedMessageViewModel[] newArray(int size) {
            return new PinnedMessageViewModel[size];
        }
    };
}
