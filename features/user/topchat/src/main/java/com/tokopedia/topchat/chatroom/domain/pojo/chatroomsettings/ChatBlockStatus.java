package com.tokopedia.topchat.chatroom.domain.pojo.chatroomsettings;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatBlockStatus implements Parcelable {

    @SerializedName(value = "is_blocked", alternate = {"isBlocked"})
    @Expose
    private boolean isBlocked;

    @SerializedName(value = "is_promo_blocked", alternate = {"isPromoBlocked"})
    @Expose
    private boolean isPromoBlocked;

    @SerializedName(value = "blocked_until", alternate = {"blockedUntil"})
    @Expose
    private String validDate;

    protected ChatBlockStatus(Parcel in) {
        isBlocked = in.readByte() != 0;
        isPromoBlocked = in.readByte() != 0;
        validDate = in.readString();
    }

    public static final Creator<ChatBlockStatus> CREATOR = new Creator<ChatBlockStatus>() {
        @Override
        public ChatBlockStatus createFromParcel(Parcel in) {
            return new ChatBlockStatus(in);
        }

        @Override
        public ChatBlockStatus[] newArray(int size) {
            return new ChatBlockStatus[size];
        }
    };

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isPromoBlocked() {
        return isPromoBlocked;
    }

    public void setPromoBlocked(boolean promoBlocked) {
        isPromoBlocked = promoBlocked;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isBlocked ? 1 : 0));
        parcel.writeByte((byte) (isPromoBlocked ? 1 : 0));
        parcel.writeString(validDate);
    }
}
