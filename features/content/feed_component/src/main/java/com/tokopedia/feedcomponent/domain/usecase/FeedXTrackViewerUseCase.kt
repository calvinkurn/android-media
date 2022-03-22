package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.data.pojo.FeedXTrackViewerResponse
import com.tokopedia.feedcomponent.domain.SUSPEND_GRAPHQL_REPOSITORY
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject
import javax.inject.Named

@GqlQuery(FeedXTrackViewerUseCase.QUERY_NAME, FeedXTrackViewerUseCase.QUERY)
class FeedXTrackViewerUseCase @Inject constructor(
        @param:Named(SUSPEND_GRAPHQL_REPOSITORY) private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<FeedXTrackViewerResponse.Response>(graphqlRepository) {

    init {
        setGraphqlQuery(FeedXTrackViewerUseCaseQuery.GQL_QUERY)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(FeedXTrackViewerResponse.Response::class.java)
    }
    override suspend fun executeOnBackground(): FeedXTrackViewerResponse.Response {
        var count = 0

        while(true) {
            try {
                val response = super.executeOnBackground()
                if (response.feedXTrackViewerResponse.success) return response
                else error("Error Report Track Visit Channel")
            } catch (e: Throwable) {
                if (++count > RETRY_COUNT) error("Error Report Track Visit Channel ${RETRY_COUNT}x")
            }
        }
    }

    companion object {

        private const val RETRY_COUNT = 3

        private const val PARAMS_ACTIVITY_ID = "activityID"

        const val QUERY_NAME = "FeedXTrackViewerUseCaseQuery"
        const val QUERY = """
            mutation TrackViewer(${'$'}activityID: String!) {
               feedXTrackViewer(activityID: ${'$'}activityID) {
                success
              }
            }
        """

        fun createParams(
                activityId: String
        ): Map<String, Any> = mapOf(
                PARAMS_ACTIVITY_ID to activityId
        )
    }
}