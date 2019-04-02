package com.tokopedia.transactiondata.entity.response.cod;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by fajarnuha on 19/12/18.
 */
public class Message implements Parcelable {

    @SerializedName("message_info")
    @Expose
    private String messageInfo;
    @SerializedName("message_link")
    @Expose
    private String messageLink;
    @SerializedName("message_logo")
    @Expose
    private String messageLogo;

    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    public String getMessageLink() {
        return messageLink;
    }

    public void setMessageLink(String messageLink) {
        this.messageLink = messageLink;
    }

    public String getMessageLogo() {
        return messageLogo;
    }

    public void setMessageLogo(String messageLogo) {
        this.messageLogo = messageLogo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.messageInfo);
        dest.writeString(this.messageLink);
        dest.writeString(this.messageLogo);
    }

    public Message() {
    }

    protected Message(Parcel in) {
        this.messageInfo = in.readString();
        this.messageLink = in.readString();
        this.messageLogo = in.readString();
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
