package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.QuickReplyTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by StevenFredian on 15/05/18.
 */

public class GroupChatQuickReplyViewModel implements Visitable<QuickReplyTypeFactory>, Parcelable {
    public static final String TYPE = "group chat";
    
    private List<Visitable> list;

    public List<Visitable> getList() {
        return list;
    }

    public void setList(List<Visitable> list) {
        this.list = list;
    }

    @Override
    public int type(QuickReplyTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.list);
    }

    public GroupChatQuickReplyViewModel() {
    }

    protected GroupChatQuickReplyViewModel(Parcel in) {
        this.list = new ArrayList<>();
        in.readList(this.list, Visitable.class.getClassLoader());
    }

    public static final Creator<GroupChatQuickReplyViewModel> CREATOR = new Creator<GroupChatQuickReplyViewModel>() {
        @Override
        public GroupChatQuickReplyViewModel createFromParcel(Parcel source) {
            return new GroupChatQuickReplyViewModel(source);
        }

        @Override
        public GroupChatQuickReplyViewModel[] newArray(int size) {
            return new GroupChatQuickReplyViewModel[size];
        }
    };
}
