package com.tokopedia.profilecommon.domain.usecase

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecommon.di.ProfileCommonQueryConstant
import com.tokopedia.profilecommon.domain.param.UpdateUserProfileParam
import com.tokopedia.profilecommon.domain.pojo.UserProfileUpdate
import com.tokopedia.profilecommon.domain.pojo.UserProfileUpdatePojo
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

class UpdateUserProfileUseCase @Inject constructor(
        @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_UPDATE)
        private val query: String,
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatcher
) : BaseUseCaseWithParam<UpdateUserProfileParam, Result<UserProfileUpdate>>() {

    override suspend fun getData(parameter: UpdateUserProfileParam): Result<UserProfileUpdate> {
        val response = withContext(dispatcher) {
            val cacheStrategy = GraphqlCacheStrategy
                    .Builder(CacheType.ALWAYS_CLOUD)
                    .build()

            val request = GraphqlRequest(
                    query,
                    UserProfileUpdatePojo::class.java,
                    parameter.toMap()
            )

            return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        }

        response.getError(UserProfileUpdatePojo::class.java)?.let {
            if (it.isNotEmpty()) {
                if (!TextUtils.isEmpty(it.first().message)) {
                    return onFailedUpdateUserProfile(Throwable(it.first().message))
                }
            }
        }

        return onSuccessUpdateUserProfile(response.getSuccessData())
    }

    private fun onSuccessUpdateUserProfile(userProfileUpdatePojo: UserProfileUpdatePojo)
            : Result<UserProfileUpdate> {
        userProfileUpdatePojo.data.let {
            return when {
                it.isSuccess == 1 && it.errors.isEmpty() -> {
                    Success(it)
                }
                it.errors.isNotEmpty() && it.errors[0].isNotEmpty() -> {
                    Fail(MessageErrorException(it.errors[0]))
                }
                else -> {
                    Fail(RuntimeException())
                }
            }
        }
    }

    private fun onFailedUpdateUserProfile(throwable: Throwable): Result<UserProfileUpdate> =
            Fail(throwable)
}