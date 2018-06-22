package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.QuickReplyTypeFactory;

/**
 * @author by StevenFredian on 15/05/18.
 */

public class GroupChatQuickReplyItemViewModel implements Visitable<QuickReplyTypeFactory>, Parcelable {
    public static final String TYPE = "group chat";

    private String text;

    public GroupChatQuickReplyItemViewModel(String id, String text) {
        this.id = id;
        this.text = text;
    }

    protected GroupChatQuickReplyItemViewModel(Parcel in) {
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

    public static final Creator<GroupChatQuickReplyItemViewModel> CREATOR = new Creator<GroupChatQuickReplyItemViewModel>() {
        @Override
        public GroupChatQuickReplyItemViewModel createFromParcel(Parcel in) {
            return new GroupChatQuickReplyItemViewModel(in);
        }

        @Override
        public GroupChatQuickReplyItemViewModel[] newArray(int size) {
            return new GroupChatQuickReplyItemViewModel[size];
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
