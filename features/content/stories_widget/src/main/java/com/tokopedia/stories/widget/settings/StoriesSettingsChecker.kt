package com.tokopedia.stories.widget.settings

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.stories.widget.settings.data.usecase.StoriesEligibilityUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by astidhiyaa on 4/19/24
 */
class StoriesSettingsChecker @Inject constructor(
    private val checkEligibilityUseCase: StoriesEligibilityUseCase,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) {
    private val authorId: String
        get() = userSession.shopId

    private val authorType: String
        get() = "shop"

    suspend fun isEligible(): Boolean = withContext(dispatchers.io) {
        val response = checkEligibilityUseCase(
            StoriesEligibilityUseCase.Param(
                req = StoriesEligibilityUseCase.Param.Author(
                    authorId = authorId,
                    authorType = authorType
                )
            )
        )
        return@withContext response.data.isEligibleForAuto
    }
}
