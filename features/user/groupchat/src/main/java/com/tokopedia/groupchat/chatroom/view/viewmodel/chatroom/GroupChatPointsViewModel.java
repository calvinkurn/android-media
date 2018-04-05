package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

/**
 * @author by StevenFredian on 28/03/18.
 */

public class GroupChatPointsViewModel implements Parcelable,
        Visitable<GroupChatTypeFactory> {

    public static final String TYPE_POINTS = "1401";
    public static final String TYPE_LOYALTY = "1003";
    public static final String TYPE_COUPON = "1403";


    String image;
    String text;
    String span;
    String url;
    String type;

    public GroupChatPointsViewModel(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public GroupChatPointsViewModel(String text, String url, String type) {
        this.text = text;
        this.url = url;
        this.type = type;
    }

    protected GroupChatPointsViewModel(Parcel in) {
        image = in.readString();
        text = in.readString();
        span = in.readString();
        url = in.readString();
        type = in.readString();
    }

    public static final Creator<GroupChatPointsViewModel> CREATOR = new Creator<GroupChatPointsViewModel>() {
        @Override
        public GroupChatPointsViewModel createFromParcel(Parcel in) {
            return new GroupChatPointsViewModel(in);
        }

        @Override
        public GroupChatPointsViewModel[] newArray(int size) {
            return new GroupChatPointsViewModel[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSpan() {
        return span;
    }

    public void setSpan(String span) {
        this.span = span;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(text);
        dest.writeString(span);
        dest.writeString(url);
        dest.writeString(type);
    }
}
