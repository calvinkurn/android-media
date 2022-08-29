package com.tokopedia.content.common.onboarding.domain.repository

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
interface FeedUGCOnboardingRepository {

    suspend fun acceptTnc(): Boolean

    suspend fun insertUsername(username: String): Boolean

    suspend fun validateUsername(username: String): Pair<Boolean, String>
}