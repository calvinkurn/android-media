package com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatSettingsResponse implements Parcelable {

    @SerializedName(value ="chatToggleBlockChat", alternate = {"chatReplies"})
    @Expose
    ChatBlockResponse chatBlockResponse;

    protected ChatSettingsResponse(Parcel in) {
        chatBlockResponse = in.readParcelable(ChatBlockResponse.class.getClassLoader());
    }

    public static final Creator<ChatSettingsResponse> CREATOR = new Creator<ChatSettingsResponse>() {
        @Override
        public ChatSettingsResponse createFromParcel(Parcel in) {
            return new ChatSettingsResponse(in);
        }

        @Override
        public ChatSettingsResponse[] newArray(int size) {
            return new ChatSettingsResponse[size];
        }
    };

    public ChatBlockResponse getChatBlockResponse() {
        return chatBlockResponse;
    }

    public void setChatBlockResponse(ChatBlockResponse chatBlockResponse) {
        this.chatBlockResponse = chatBlockResponse;
    }

    @Override
    public String toString() {
        return "ChatBlockResponse:{" + chatBlockResponse + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(chatBlockResponse, i);
    }
}
