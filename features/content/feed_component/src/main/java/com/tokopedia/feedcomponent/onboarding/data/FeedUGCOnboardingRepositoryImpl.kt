package com.tokopedia.feedcomponent.onboarding.data

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedcomponent.onboarding.domain.repository.FeedUGCOnboardingRepository
import com.tokopedia.feedcomponent.onboarding.domain.usecase.FeedProfileAcceptTncUseCase
import com.tokopedia.feedcomponent.onboarding.domain.usecase.FeedProfileSubmitUseCase
import com.tokopedia.feedcomponent.onboarding.domain.usecase.FeedProfileValidateUsernameUseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class FeedUGCOnboardingRepositoryImpl @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val feedProfileAcceptTncUseCase: FeedProfileAcceptTncUseCase,
    private val feedProfileSubmitUseCase: FeedProfileSubmitUseCase,
    private val feedProfileValidateUsernameUseCase: FeedProfileValidateUsernameUseCase,
) : FeedUGCOnboardingRepository {

    override suspend fun acceptTnc(): Boolean {
        return withContext(dispatcher.io) {
            val result = feedProfileAcceptTncUseCase.executeOnBackground()

            result.wrapper.hasAcceptTnC
        }
    }

    override suspend fun insertUsername(username: String): Boolean {
        return withContext(dispatcher.io) {
            val result = feedProfileSubmitUseCase.apply {
                setRequestParams(FeedProfileSubmitUseCase.createInsertNewUsernameParam(username))
            }.executeOnBackground()

            result.wrapper.status
        }
    }

    override suspend fun validateUsername(username: String): Pair<Boolean, String> {
        return withContext(dispatcher.io) {
            val result = feedProfileValidateUsernameUseCase.apply {
                setRequestParams(FeedProfileValidateUsernameUseCase.createParam(username))
            }.executeOnBackground()

            Pair(result.wrapper.isValid == SUCCESS_CODE, result.wrapper.notValidInformation)
        }
    }

    companion object {
        private const val SUCCESS_CODE = 1L
    }
}