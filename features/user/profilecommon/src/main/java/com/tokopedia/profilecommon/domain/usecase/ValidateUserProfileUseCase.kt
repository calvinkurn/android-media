package com.tokopedia.profilecommon.domain.usecase

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecommon.di.ProfileCommonQueryConstant
import com.tokopedia.profilecommon.domain.param.ValidateUserProfileParam
import com.tokopedia.profilecommon.domain.pojo.UserProfileValidate
import com.tokopedia.profilecommon.domain.pojo.UserProfileValidatePojo
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

class ValidateUserProfileUseCase @Inject constructor(
        @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_VALIDATE)
        private val query: String,
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatcher
) : BaseUseCaseWithParam<ValidateUserProfileParam, Result<UserProfileValidate>>() {

    override suspend fun getData(parameter: ValidateUserProfileParam): Result<UserProfileValidate> {
        val response = withContext(dispatcher) {
            val cacheStrategy =
                    GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

            val request = GraphqlRequest(
                    query,
                    UserProfileValidatePojo::class.java,
                    parameter.toMap()
            )

            return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        }

        response.getError(UserProfileValidatePojo::class.java)?.let {
            if (it.isNotEmpty()) {
                if (!TextUtils.isEmpty(it[0].message)) {
                    return onFailedValidateUserProfile(Throwable(it[0].message))
                }
            }
        }

        return onSuccessValidateUserProfile(response.getSuccessData())
    }

    private fun onSuccessValidateUserProfile(userProfileValidatePojo: UserProfileValidatePojo)
            : Result<UserProfileValidate> {
        userProfileValidatePojo.data.let {
            return when {
                it.isValid -> {
                    Success(it)
                }
                it.message.isNotEmpty() -> {
                    Fail(MessageErrorException(it.message))
                }
                else -> {
                    Fail(RuntimeException())
                }
            }
        }
    }

    private fun onFailedValidateUserProfile(throwable: Throwable): Result<UserProfileValidate> =
            Fail(throwable)
}