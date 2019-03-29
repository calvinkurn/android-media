package com.tokopedia.groupchat.chatroom.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 2/14/18.
 */

public class GroupChatViewModel implements Parcelable {

    private String channelUuid;
    private ChannelInfoViewModel channelInfoViewModel;
    private long timeStampAfterPause = 0;
    private long timeStampAfterResume = 0;
    private int channelPosition;

    public GroupChatViewModel(String channelUuid, int channelPosition) {
        this.channelUuid = channelUuid;
        this.channelInfoViewModel = null;
        this.timeStampAfterPause = 0;
        this.timeStampAfterResume = 0;
        this.channelPosition = channelPosition;
    }

    protected GroupChatViewModel(Parcel in) {
        channelUuid = in.readString();
        channelInfoViewModel = in.readParcelable(ChannelInfoViewModel.class.getClassLoader());
        timeStampAfterPause = in.readLong();
        timeStampAfterResume = in.readLong();
        channelPosition = in.readInt();
    }

    public static final Creator<GroupChatViewModel> CREATOR = new Creator<GroupChatViewModel>() {
        @Override
        public GroupChatViewModel createFromParcel(Parcel in) {
            return new GroupChatViewModel(in);
        }

        @Override
        public GroupChatViewModel[] newArray(int size) {
            return new GroupChatViewModel[size];
        }
    };

    public String getChannelUuid() {
        return channelUuid;
    }

    public void setTotalView(String totalView) {
        if (channelInfoViewModel != null) {
            this.channelInfoViewModel.setTotalView(totalView);
        }
    }

    public String getTotalView() {
        return channelInfoViewModel != null ? channelInfoViewModel.getTotalView() : "0";
    }

    public String getChannelName() {
        return channelInfoViewModel != null ? channelInfoViewModel.getTitle() : "";
    }


    public String getChannelUrl() {
        return channelInfoViewModel != null ? channelInfoViewModel.getChannelUrl() : "";
    }

    public void setChannelInfo(ChannelInfoViewModel channelInfoViewModel) {
        this.channelInfoViewModel = channelInfoViewModel;
    }

    public String getPollId() {
        if (channelInfoViewModel != null
                && channelInfoViewModel.getVoteInfoViewModel() != null) {
            return this.channelInfoViewModel.getVoteInfoViewModel().getPollId();
        } else {
            return "";
        }
    }

    public ChannelInfoViewModel getChannelInfoViewModel() {
        return channelInfoViewModel;
    }

    public long getTimeStampAfterPause() {
        return timeStampAfterPause;
    }

    public void setTimeStampAfterPause(long timeStampBeforePause) {
        this.timeStampAfterPause = timeStampBeforePause;
    }

    public int getChannelPosition() {
        return channelPosition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(channelUuid);
        dest.writeParcelable(channelInfoViewModel, flags);
        dest.writeLong(timeStampAfterPause);
        dest.writeLong(timeStampAfterResume);
        dest.writeInt(channelPosition);
    }

    public void setTimeStampAfterResume(long timeStampAfterResume) {
        this.timeStampAfterResume = timeStampAfterResume;
    }

    public long getTimeStampAfterResume() {
        return timeStampAfterResume;
    }
}
