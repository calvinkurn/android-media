package com.tokopedia.navigation_common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class ProfileModel {
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("completion")
    @Expose
    private Integer completion;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getCompletion() {
        return completion;
    }

    public void setCompletion(Integer completion) {
        this.completion = completion;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
