
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RevieweeData {

    @SerializedName("reviewee_name")
    @Expose
    private String revieweeName;
    @SerializedName("reviewee_uri")
    @Expose
    private String revieweeUri;
    @SerializedName("reviewee_role")
    @Expose
    private String revieweeRole;
    @SerializedName("reviewee_role_id")
    @Expose
    private int revieweeRoleId;
    @SerializedName("reviewee_picture")
    @Expose
    private String revieweePicture;
    @SerializedName("reviewee_buyer_badge")
    @Expose
    private RevieweeBuyerBadge revieweeBuyerBadge;
    @SerializedName("reviewee_shop_badge")
    @Expose
    private RevieweeShopBadge revieweeShopBadge;

    public String getRevieweeName() {
        return revieweeName;
    }

    public void setRevieweeName(String revieweeName) {
        this.revieweeName = revieweeName;
    }

    public String getRevieweeUri() {
        return revieweeUri;
    }

    public void setRevieweeUri(String revieweeUri) {
        this.revieweeUri = revieweeUri;
    }

    public String getRevieweeRole() {
        return revieweeRole;
    }

    public void setRevieweeRole(String revieweeRole) {
        this.revieweeRole = revieweeRole;
    }

    public String getRevieweePicture() {
        return revieweePicture;
    }

    public void setRevieweePicture(String revieweePicture) {
        this.revieweePicture = revieweePicture;
    }

    public RevieweeBuyerBadge getRevieweeBuyerBadge() {
        return revieweeBuyerBadge;
    }

    public void setRevieweeBuyerBadge(RevieweeBuyerBadge revieweeBuyerBadge) {
        this.revieweeBuyerBadge = revieweeBuyerBadge;
    }

    public RevieweeShopBadge getRevieweeShopBadge() {
        return revieweeShopBadge;
    }

    public void setRevieweeShopBadge(RevieweeShopBadge revieweeShopBadge) {
        this.revieweeShopBadge = revieweeShopBadge;
    }

    public int getRevieweeRoleId() {
        return revieweeRoleId;
    }
}
