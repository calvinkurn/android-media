package com.tokopedia.people.domains

import android.annotation.SuppressLint
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.people.model.response.PostBlockUserResponse
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 28/11/22
 */
@SuppressLint("PII Data Exposure")
@GqlQuery(PostBlockUserUseCase.QUERY_NAME, PostBlockUserUseCase.QUERY)
class PostBlockUserUseCase @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
) : GraphqlUseCase<PostBlockUserResponse>() {

    init {
        setGraphqlQuery(PostBlockUserUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD)
                .build()
        )
        setTypeClass(PostBlockUserResponse::class.java)
    }

    suspend fun execute(
        userId: String,
        status: Boolean
    ): PostBlockUserResponse = withContext(dispatchers.io) {
        setRequestParams(
            mapOf(
                PARAM_USER_ID to userId,
                PARAM_STATUS to status,
            )
        )

        executeOnBackground()
    }

    companion object {
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_STATUS = "status"

        const val QUERY_NAME = "PostBlockUserUseCaseQuery"
        const val QUERY = """
            mutation blockUser(
                ${"$$PARAM_USER_ID"}: String!, 
                ${"$$PARAM_STATUS"}: Boolean,
            ) {
                feedXProfileBlockUser(req:{
                    blockUserID:${"$$PARAM_USER_ID"},
                    blockStatus:${"$$PARAM_STATUS"}
                }) {
                    success
                }
            }
        """
    }
}
