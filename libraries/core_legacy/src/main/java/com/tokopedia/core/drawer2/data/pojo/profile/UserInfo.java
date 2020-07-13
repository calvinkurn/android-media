
package com.tokopedia.core.drawer2.data.pojo.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("user_messenger")
    @Expose
    private String userMessenger;
    @SerializedName("user_birth")
    @Expose
    private String userBirth;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_reputation")
    @Expose
    private UserReputation userReputation;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("user_hobbies")
    @Expose
    private String userHobbies;
    @SerializedName("user_image")
    @Expose
    private String userImage;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userMessenger='" + userMessenger + '\'' +
                ", userBirth='" + userBirth + '\'' +
                ", userName='" + userName + '\'' +
                ", userReputation=" + userReputation +
                ", userEmail='" + userEmail + '\'' +
                ", userId='" + userId + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userHobbies='" + userHobbies + '\'' +
                ", userImage='" + userImage + '\'' +
                '}';
    }
}
