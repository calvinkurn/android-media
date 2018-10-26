package com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Steven 23/10/18
 */
public class SettingGroupChat implements Parcelable{

    @SerializedName("ping_interval")
    @Expose
    private int pingInterval;
    @SerializedName("max_char")
    @Expose
    private int maxChar;

    public int getPingInterval() {
        return pingInterval;
    }

    public int getMaxChar() {
        return maxChar;
    }

    protected SettingGroupChat(Parcel in) {
        pingInterval = in.readInt();
        maxChar = in.readInt();
    }

    public static final Creator<SettingGroupChat> CREATOR = new Creator<SettingGroupChat>() {
        @Override
        public SettingGroupChat createFromParcel(Parcel in) {
            return new SettingGroupChat(in);
        }

        @Override
        public SettingGroupChat[] newArray(int size) {
            return new SettingGroupChat[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pingInterval);
        dest.writeInt(maxChar);
    }
}
