package com.tokopedia.profilecommon.domain.usecase

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecommon.di.ProfileCommonQueryConstant
import com.tokopedia.profilecommon.domain.param.UpdateUserProfileCompletionParam
import com.tokopedia.profilecommon.domain.pojo.UserProfileCompletionUpdate
import com.tokopedia.profilecommon.domain.pojo.UserProfileCompletionUpdatePojo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

class UpdateUserProfileCompletionUseCase @Inject constructor(
        @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_COMPLETION_UPDATE)
        private val query: String,
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatcher
) : BaseUseCaseWithParam<UpdateUserProfileCompletionParam, Result<UserProfileCompletionUpdate>>() {

    override suspend fun getData(parameter: UpdateUserProfileCompletionParam): Result<UserProfileCompletionUpdate> {
        val response = withContext(dispatcher) {
            val cacheStrategy = GraphqlCacheStrategy
                    .Builder(CacheType.ALWAYS_CLOUD)
                    .build()

            val request = GraphqlRequest(
                    query,
                    UserProfileCompletionUpdatePojo::class.java,
                    parameter.toMap()
            )

            return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        }

        response.getError(UserProfileCompletionUpdatePojo::class.java)?.let {
            if (it.isNotEmpty()) {
                if (!TextUtils.isEmpty(it[0].message)) {
                    return onFailedUpdateUserProfileCompletion(Throwable(it[0].message))
                }
            }
        }

        return onSuccessUpdateUserProfileCompletion(response.getSuccessData())
    }

    private fun onSuccessUpdateUserProfileCompletion(userProfileCompletionUpdatePojo: UserProfileCompletionUpdatePojo)
            : Result<UserProfileCompletionUpdate> {
        userProfileCompletionUpdatePojo.data.let {
            return when {
                it.isSuccess -> {
                    Success(it)
                }
                it.fullNameMessage.isNotEmpty() -> {
                    Fail(MessageErrorException(it.fullNameMessage))
                }
                it.genderMessage.isNotEmpty() -> {
                    Fail(MessageErrorException(it.genderMessage))
                }
                it.birthDateMessage.isNotEmpty() -> {
                    Fail(MessageErrorException(it.birthDateMessage))
                }
                it.passwordMessage.isNotEmpty() -> {
                    Fail(MessageErrorException(it.passwordMessage))
                }
                it.passwordConfirmMessage.isNotEmpty() -> {
                    Fail(MessageErrorException(it.passwordConfirmMessage))
                }
                it.emailMessage.isNotEmpty() -> {
                    Fail(MessageErrorException(it.emailMessage))
                }
                it.msisdnMessage.isNotEmpty() -> {
                    Fail(MessageErrorException(it.msisdnMessage))
                }
                else -> {
                    Fail(RuntimeException())
                }
            }
        }
    }

    private fun onFailedUpdateUserProfileCompletion(throwable: Throwable): Result<UserProfileCompletionUpdate> =
            Fail(throwable)
}