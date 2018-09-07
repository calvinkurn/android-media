package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class RevieweeDataDomain {

    private int revieweeRoleId;
    private String revieweeName;
    private String revieweeUri;
    private String revieweeRole;
    private String revieweePicture;
    private RevieweeBadgeSellerDomain revieweeBadgeSeller;
    private RevieweeBadgeCustomerDomain revieweeBadgeCustomer;

    public RevieweeDataDomain(String revieweeName, String revieweeUri,
                              String revieweeRole, int revieweeRoleId, String revieweePicture,
                              RevieweeBadgeCustomerDomain revieweeBadgeCustomer,
                              RevieweeBadgeSellerDomain revieweeBadgeSeller) {
        this.revieweeName = revieweeName;
        this.revieweeUri = revieweeUri;
        this.revieweeRole = revieweeRole;
        this.revieweePicture = revieweePicture;
        this.revieweeBadgeCustomer = revieweeBadgeCustomer;
        this.revieweeBadgeSeller = revieweeBadgeSeller;
        this.revieweeRoleId = revieweeRoleId;
    }

    public String getRevieweeName() {
        return revieweeName;
    }

    public String getRevieweeUri() {
        return revieweeUri;
    }

    public String getRevieweeRole() {
        return revieweeRole;
    }

    public String getRevieweePicture() {
        return revieweePicture;
    }

    public RevieweeBadgeSellerDomain getRevieweeBadgeSeller() {
        return revieweeBadgeSeller;
    }

    public RevieweeBadgeCustomerDomain getRevieweeBadgeCustomer() {
        return revieweeBadgeCustomer;
    }

    public int getRevieweeRoleId() {
        return revieweeRoleId;
    }
}
