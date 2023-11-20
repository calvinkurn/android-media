package com.tokopedia.feedplus.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedplus.data.FeedXRecomWidgetEntity
import com.tokopedia.feedplus.domain.mapper.MapperFeedXRecomWidget
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 26, 2023
 */
class FeedXRecomWidgetUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers,
    private val uiMapper: MapperFeedXRecomWidget,
) : CoroutineUseCase<Map<String, Any>, FeedFollowRecommendationModel>(dispatcher.io){

    override suspend fun execute(params: Map<String, Any>): FeedFollowRecommendationModel {
        val widgetId = params[PARAM_WIDGET_ID].toString()
        val finalParam = params.toMutableMap().apply {
            remove(PARAM_WIDGET_ID)
        }

        val response = graphqlRepository.request<Map<String, Any>, FeedXRecomWidgetEntity>(
            graphqlQuery(),
            finalParam
        )

        return uiMapper.transform(response, widgetId)
    }

    override fun graphqlQuery(): String = """
        query feedXRecomWidget(
            ${'$'}$PARAM_REQ: FeedXRecomWidgetRequest!
        ) {
            feedXRecomWidget(
                $PARAM_REQ: ${'$'}$PARAM_REQ
            ) {
                isShown
                title
                subtitle
                seeAll {
                    isShown
                    text
                    weblink
                    applink
                }
                items {
                    type
                    id
                    encryptedID
                    name
                    nickname
                    badgeImageURL
                    logoImageURL
                    weblink
                    applink
                    coverURL
                    mediaURL
                }
                nextCursor
            }
        }
    """.trimIndent()

    private fun createParams(
        screenName: String,
        limit: Int,
        cursor: String,
        widgetId: String,
    ) = mapOf(
        PARAM_REQ to mapOf(
            PARAM_SCREEN_NAME to screenName,
            PARAM_LIMIT to limit,
            PARAM_CURSOR to cursor,
        ),

        /** PARAM_WIDGET_ID only for mapper, will be removed when calling real gql */
        PARAM_WIDGET_ID to widgetId
    )

    fun createFeedFollowRecomParams(
        cursor: String,
        widgetId: String,
    ) = createParams(
        screenName = SCREEN_FOLLOWING_TAB,
        limit = FEED_FOLLOW_RECOM_LIMIT,
        cursor = cursor,
        widgetId = widgetId,
    )

    companion object {
        private const val PARAM_REQ = "req"
        private const val PARAM_SCREEN_NAME = "screenName"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_CURSOR = "cursor"
        private const val PARAM_WIDGET_ID = "widgetId"

        private const val SCREEN_FOLLOWING_TAB = "following_tab"
        private const val FEED_FOLLOW_RECOM_LIMIT = 10
    }
}
