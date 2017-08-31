
package com.tokopedia.core.product.model.productdetail.mosthelpful;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("user_label")
    @Expose
    private String userLabel;
    @SerializedName("user_url")
    @Expose
    private String userUrl;
    @SerializedName("user_reputation")
    @Expose
    private UserReputation userReputation;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public UserReputation getUserReputation() {
        return userReputation;
    }

    public void setUserReputation(UserReputation userReputation) {
        this.userReputation = userReputation;
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.userId);
        dest.writeString(this.fullName);
        dest.writeString(this.userImage);
        dest.writeString(this.userLabel);
        dest.writeString(this.userUrl);
        dest.writeParcelable(this.userReputation, flags);
    }

    protected User(Parcel in) {
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fullName = in.readString();
        this.userImage = in.readString();
        this.userLabel = in.readString();
        this.userUrl = in.readString();
        this.userReputation = in.readParcelable(UserReputation.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
