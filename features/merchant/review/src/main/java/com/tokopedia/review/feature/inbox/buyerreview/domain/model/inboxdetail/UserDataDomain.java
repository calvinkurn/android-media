package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail;

/**
 * @author by nisie on 8/23/17.
 */

public class UserDataDomain {

    private int userId;
    private String fullName;
    private String userEmail;
    private int userStatus;
    private String userUrl;
    private String userLabel;
    private String userProfilePict;
    private UserReputationDomain userReputation;

    public UserDataDomain(int userId, String fullName, String userEmail,
                          int userStatus, String userUrl, String userLabel,
                          String userProfilePict, UserReputationDomain userReputation) {
        this.userId = userId;
        this.fullName = fullName;
        this.userEmail = userEmail;
        this.userStatus = userStatus;
        this.userUrl = userUrl;
        this.userLabel = userLabel;
        this.userProfilePict = userProfilePict;
        this.userReputation = userReputation;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getUserStatus() {
        return userStatus;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public String getUserProfilePict() {
        return userProfilePict;
    }

    public UserReputationDomain getUserReputation() {
        return userReputation;
    }
}
