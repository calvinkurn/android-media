package com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatBlockResponse implements Parcelable {

    @SerializedName("success")
    @Expose
    private boolean isSuccess;

    @SerializedName(value = "block_status", alternate = {"block"})
    @Expose
    private ChatBlockStatus chatBlockStatus;


    protected ChatBlockResponse(Parcel in) {
        isSuccess = in.readByte() != 0;
        chatBlockStatus = in.readParcelable(ChatBlockStatus.class.getClassLoader());
    }

    public static final Creator<ChatBlockResponse> CREATOR = new Creator<ChatBlockResponse>() {
        @Override
        public ChatBlockResponse createFromParcel(Parcel in) {
            return new ChatBlockResponse(in);
        }

        @Override
        public ChatBlockResponse[] newArray(int size) {
            return new ChatBlockResponse[size];
        }
    };

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public ChatBlockStatus getChatBlockStatus() {
        return chatBlockStatus;
    }

    public void setChatBlockStatus(ChatBlockStatus chatBlockStatus) {
        this.chatBlockStatus = chatBlockStatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isSuccess ? 1 : 0));
        parcel.writeParcelable(chatBlockStatus, i);
    }
}
