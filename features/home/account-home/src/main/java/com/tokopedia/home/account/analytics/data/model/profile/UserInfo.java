
package com.tokopedia.home.account.analytics.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("user_messenger")
    @Expose
    private String userMessenger = "";
    @SerializedName("user_birth")
    @Expose
    private String userBirth = "";
    @SerializedName("user_name")
    @Expose
    private String userName = "";
    @SerializedName("user_reputation")
    @Expose
    private UserReputation userReputation = new UserReputation();
    @SerializedName("user_email")
    @Expose
    private String userEmail = "";
    @SerializedName("user_id")
    @Expose
    private String userId = "";
    @SerializedName("user_phone")
    @Expose
    private String userPhone = "";
    @SerializedName("user_hobbies")
    @Expose
    private String userHobbies = "";
    @SerializedName("user_image")
    @Expose
    private String userImage = "";

    public String getUserMessenger() {
        return userMessenger;
    }

    public void setUserMessenger(String userMessenger) {
        this.userMessenger = userMessenger;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserReputation getUserReputation() {
        return userReputation;
    }

    public void setUserReputation(UserReputation userReputation) {
        this.userReputation = userReputation;
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserHobbies() {
        return userHobbies;
    }

    public void setUserHobbies(String userHobbies) {
        this.userHobbies = userHobbies;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
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
