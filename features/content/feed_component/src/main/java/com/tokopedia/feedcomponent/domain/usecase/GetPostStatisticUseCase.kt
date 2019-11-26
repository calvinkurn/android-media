package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.domain.SUSPEND_GRAPHQL_REPOSITORY
import com.tokopedia.feedcomponent.domain.model.statistic.FeedGetStatsPosts
import com.tokopedia.feedcomponent.domain.model.statistic.GetPostStatisticResponse
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by jegul on 2019-11-22
 */
class GetPostStatisticUseCase @Inject constructor(
        @param:Named(SUSPEND_GRAPHQL_REPOSITORY) private val graphqlRepository: GraphqlRepository
) : UseCase<FeedGetStatsPosts>() {

    companion object {

        private const val PARAM_ACTIVITY_IDS = "activityIDs"

        fun getParam(activityIDs: List<String>): RequestParams {
            return RequestParams.create().apply {
                putObject(PARAM_ACTIVITY_IDS, activityIDs)
            }
        }
    }

    private val params: MutableMap<String, Any> = mutableMapOf()

    private val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()

    //region query
    private val query by lazy {
        val activityIDs = "\$activityIDs"

        """
            query GetPostStatistic($activityIDs: [String!]!) {
                feedGetStatsPosts(activityIDs: $activityIDs) {
                    stats {
                        activityID
                        like {
                            fmt
                            value
                            checked
                        }
                        click {
                            fmt
                            value
                        }
                        comment {
                            fmt
                            value
                        }
                        view {
                            fmt
                            value
                        }
                    }
                }
            }
        """.trimIndent()
    }
    //endregion

    override suspend fun executeOnBackground(): FeedGetStatsPosts {
        val response = graphqlRepository.getReseponse(
                listOf(
                        GraphqlRequest(query, GetPostStatisticResponse::class.java, params)
                ),
                cacheStrategy
        )
        return response.getData<GetPostStatisticResponse>(GetPostStatisticResponse::class.java).feedGetStatsPosts
    }

    fun setParams(params: RequestParams) {
        this.params.run {
            clear()
            putAll(params.parameters)
        }
    }
}