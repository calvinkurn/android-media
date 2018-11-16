package com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.domain.pojo.BaseGroupChatPojo;
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.GroupChatTypeFactory;

/**
 * @author by StevenFredian on 24/09/18.
 */

public class VideoViewModel extends BaseGroupChatPojo implements Visitable<GroupChatTypeFactory>, Parcelable {
    public static final String TYPE = "video";

    @SerializedName("video_id")
    @Expose
    private String videoId;

    public VideoViewModel(String videoId) {
        this.videoId = videoId;
    }

    protected VideoViewModel(Parcel in) {
        videoId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoViewModel> CREATOR = new Creator<VideoViewModel>() {
        @Override
        public VideoViewModel createFromParcel(Parcel in) {
            return new VideoViewModel(in);
        }

        @Override
        public VideoViewModel[] newArray(int size) {
            return new VideoViewModel[size];
        }
    };

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    @Override
    public int type(GroupChatTypeFactory typeFactory) {
        return 0;
    }
}
