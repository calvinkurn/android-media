
package com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserOwner {

    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("user_url")
    @Expose
    private String userUrl;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("user_full_name")
    @Expose
    private String userFullName;
    @SerializedName("user_label")
    @Expose
    private String userLabel;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

}
