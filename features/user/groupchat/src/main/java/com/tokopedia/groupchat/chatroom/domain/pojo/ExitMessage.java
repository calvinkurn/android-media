package com.tokopedia.groupchat.chatroom.domain.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by StevenFredian on 03/05/18.
 */

public class ExitMessage implements Parcelable{

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.body);
    }

    public ExitMessage() {
    }

    protected ExitMessage(Parcel in) {
        this.title = in.readString();
        this.body = in.readString();
    }

    public static final Creator<ExitMessage> CREATOR = new Creator<ExitMessage>() {
        @Override
        public ExitMessage createFromParcel(Parcel source) {
            return new ExitMessage(source);
        }

        @Override
        public ExitMessage[] newArray(int size) {
            return new ExitMessage[size];
        }
    };
}
