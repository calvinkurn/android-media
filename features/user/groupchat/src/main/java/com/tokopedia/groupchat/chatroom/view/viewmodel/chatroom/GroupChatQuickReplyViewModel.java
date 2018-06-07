package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.QuickReplyTypeFactory;

/**
 * @author by StevenFredian on 15/05/18.
 */

public class GroupChatQuickReplyViewModel implements Visitable<QuickReplyTypeFactory>, Parcelable {
    public static final String TYPE = "group chat";

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("text")
    @Expose
    private String text;

    public GroupChatQuickReplyViewModel(String id, String text) {
        this.id = id;
        this.text = text;
    }

    protected GroupChatQuickReplyViewModel(Parcel in) {
        id = in.readString();
        text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupChatQuickReplyViewModel> CREATOR = new Creator<GroupChatQuickReplyViewModel>() {
        @Override
        public GroupChatQuickReplyViewModel createFromParcel(Parcel in) {
            return new GroupChatQuickReplyViewModel(in);
        }

        @Override
        public GroupChatQuickReplyViewModel[] newArray(int size) {
            return new GroupChatQuickReplyViewModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int type(QuickReplyTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
