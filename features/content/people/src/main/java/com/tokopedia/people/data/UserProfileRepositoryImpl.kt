package com.tokopedia.people.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.people.domains.*
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase.Companion.WHITELIST_ENTRY_POINT
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
class UserProfileRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val userDetailsUseCase: UserDetailsUseCase,
    private val playVodUseCase: PlayPostContentUseCase,
    private val useCaseDoFollow: ProfileFollowUseCase,
    private val useCaseDoUnFollow: ProfileUnfollowedUseCase,
    private val profileIsFollowing: ProfileTheyFollowedUseCase,
    private val videoPostReminderUseCase: VideoPostReminderUseCase,
    private val getWhitelistUseCase: GetWhitelistUseCase,
) : UserProfileRepository {

}