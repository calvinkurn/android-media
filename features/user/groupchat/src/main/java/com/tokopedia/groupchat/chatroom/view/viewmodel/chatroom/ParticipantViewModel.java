package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

/**
 * @author : Steven 06/11/18
 */
public class ParticipantViewModel implements Visitable<GroupChatTypeFactory>, Parcelable {

    public static final String TYPE = "total_view";

    public String channelId;
    public String totalView;


    public ParticipantViewModel(Parcel in) {
        channelId = in.readString();
        totalView = in.readString();
    }

    public ParticipantViewModel(String channelId, String totalView) {
        this.channelId = channelId;
        this.totalView = totalView;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(channelId);
        dest.writeString(totalView);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParticipantViewModel> CREATOR = new Creator<ParticipantViewModel>() {
        @Override
        public ParticipantViewModel createFromParcel(Parcel in) {
            return new ParticipantViewModel(in);
        }

        @Override
        public ParticipantViewModel[] newArray(int size) {
            return new ParticipantViewModel[size];
        }
    };

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return 0;
    }
}
