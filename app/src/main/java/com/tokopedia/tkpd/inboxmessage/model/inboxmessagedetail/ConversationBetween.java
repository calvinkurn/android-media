
package com.tokopedia.tkpd.inboxmessage.model.inboxmessagedetail;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConversationBetween implements Parcelable {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;

    protected ConversationBetween(Parcel in) {
        userId = in.readString();
        userName = in.readString();
    }

    public static final Creator<ConversationBetween> CREATOR = new Creator<ConversationBetween>() {
        @Override
        public ConversationBetween createFromParcel(Parcel in) {
            return new ConversationBetween(in);
        }

        @Override
        public ConversationBetween[] newArray(int size) {
            return new ConversationBetween[size];
        }
    };

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The userName
     */
    public String getUserName() {
        return Html.fromHtml(userName).toString();
    }

    /**
     * @param userName The user_name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
    }

    public String getUserLabel() {
        if (!this.userId.equals("0")) {
            return "Pengguna";
        } else {
            return "Admin";
        }
    }
}
