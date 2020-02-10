package com.tokopedia.profilecommon.domain.usecase

import android.text.TextUtils
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.profilecommon.di.ProfileCommonQueryConstant
import com.tokopedia.profilecommon.domain.pojo.GetUserProfileCompletionPojo
import com.tokopedia.profilecommon.domain.pojo.UserProfileCompletionData
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

class GetUserProfileCompletionUseCase @Inject constructor(
        @Named(ProfileCommonQueryConstant.QUERY_USER_PROFILE_COMPLETION)
        private val query: String,
        private val graphqlRepository: GraphqlRepository,
        private val dispatcher: CoroutineDispatcher
) : BaseUseCase<Result<UserProfileCompletionData>>() {

    override suspend fun getData(): Result<UserProfileCompletionData> {
        val response = withContext(dispatcher) {
            val cacheStrategy =
                    GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

            val request = GraphqlRequest(
                    query,
                    GetUserProfileCompletionPojo::class.java
            )

            return@withContext graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        }

        response.getError(GetUserProfileCompletionPojo::class.java)?.let {
            if (it.isNotEmpty()) {
                if (!TextUtils.isEmpty(it[0].message)) {
                    return onFailedGetUserProfile(Throwable(it[0].message))
                }
            }
        }

        return onSuccessGetUserProfile(response.getSuccessData())
    }

    private fun onSuccessGetUserProfile(getUserProfileCompletionPojo: GetUserProfileCompletionPojo)
            : Result<UserProfileCompletionData> = Success(getUserProfileCompletionPojo.data)

    private fun onFailedGetUserProfile(throwable: Throwable): Result<UserProfileCompletionData> =
            Fail(throwable)

}