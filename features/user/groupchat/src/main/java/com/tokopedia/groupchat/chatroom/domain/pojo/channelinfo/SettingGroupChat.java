package com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author : Steven 23/10/18
 */
public class SettingGroupChat implements Parcelable{

    public static final int DEFAULT_PING = 10000;
    public static final int DEFAULT_MAX_RETRIES = 3;
    public static final int DEFAULT_DELAY = 5000;

    @SerializedName("ping_interval")
    @Expose
    private int pingInterval;
    @SerializedName("max_chars")
    @Expose
    private int maxChar;
    @SerializedName("max_retries")
    @Expose
    private int maxRetries;
    @SerializedName("min_reconnect_delay")
    @Expose
    private int delay;

    public SettingGroupChat() {
        pingInterval = DEFAULT_PING;
        maxRetries = DEFAULT_MAX_RETRIES;
        delay = DEFAULT_DELAY;
    }

    public int getPingInterval() {
        return pingInterval;
    }

    public int getMaxChar() {
        return maxChar;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public int getDelay() {
        return delay;
    }

    public SettingGroupChat(Parcel in) {
        pingInterval = in.readInt();
        maxChar = in.readInt();
        maxRetries = in.readInt();
        delay = in.readInt();
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
        dest.writeInt(maxRetries);
        dest.writeInt(delay);
    }
}
