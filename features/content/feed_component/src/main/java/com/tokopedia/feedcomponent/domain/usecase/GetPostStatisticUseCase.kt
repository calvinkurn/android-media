package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.domain.model.FeedGetStatsPosts
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by jegul on 2019-11-22
 */
class GetPostStatisticUseCase @Inject constructor(
        private val graphqlUseCase: MultiRequestGraphqlUseCase
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
        graphqlUseCase.run {
            clearRequest()
            addRequest(
                    GraphqlRequest(query, FeedGetStatsPosts::class.java, params)
            )
        }
        val response = graphqlUseCase.executeOnBackground()
        return response.getData(FeedGetStatsPosts::class.java)
    }

    fun setParams(params: RequestParams) {
        this.params.run {
            clear()
            putAll(params.parameters)
        }
    }
}