package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

/**
 * @author by nisie on 8/23/17.
 */
class UserDataDomain constructor(
    private val userId: Int, val fullName: String?, val userEmail: String?,
    val userStatus: Int, val userUrl: String?, val userLabel: String?,
    val userProfilePict: String?, val userReputation: UserReputationDomain
) {
    fun getUserId(): Long {
        return userId.toLong()
    }
}