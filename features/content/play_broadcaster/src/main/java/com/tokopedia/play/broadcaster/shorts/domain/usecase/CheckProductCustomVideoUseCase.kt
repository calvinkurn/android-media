package com.tokopedia.play.broadcaster.shorts.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.play.broadcaster.shorts.domain.model.CheckProductCustomVideoResponse
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on December 14, 2023
 */
@GqlQuery(CheckProductCustomVideoUseCase.QUERY_NAME, CheckProductCustomVideoUseCase.QUERY)
class CheckProductCustomVideoUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
) : CoroutineUseCase<String, CheckProductCustomVideoResponse>(dispatchers.io) {

    private val gqlQuery: GqlQueryInterface = CheckProductCustomVideoUseCaseQuery()

    override fun graphqlQuery(): String = gqlQuery.getQuery()

    override suspend fun execute(params: String): CheckProductCustomVideoResponse {
        val requestParam = mapOf(
            PARAM_CHANNEL_ID to params
        )

        return repository.request(gqlQuery, requestParam)
    }

    companion object {
        private const val PARAM_CHANNEL_ID = "channelID"

        const val QUERY_NAME = "CheckProductCustomVideoUseCaseQuery"
        const val QUERY = """
            query broadcasterCheckPDPCustomVideo(
                ${"$$PARAM_CHANNEL_ID"}: String!
            ) {
                broadcasterCheckPDPCustomVideo(
                    $PARAM_CHANNEL_ID: ${"$$PARAM_CHANNEL_ID"}
                ) {
                    hasVideo
                    videoURL
                    coverURL
                }
            }
        """
    }
}
