package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 8/23/17.
 */
class UserDataDomain constructor(
    val userId: Long = 0L,
    val fullName: String = "",
    val userEmail: String = "",
    val userStatus: Int = 0,
    val userUrl: String = "",
    val userLabel: String = "",
    val userProfilePict: String = "",
    val userReputation: UserReputationDomain = UserReputationDomain()
)