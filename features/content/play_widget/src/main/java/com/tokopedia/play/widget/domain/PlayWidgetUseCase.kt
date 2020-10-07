package com.tokopedia.play.widget.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetResponse
import com.tokopedia.usecase.coroutines.UseCase


/**
 * Created by mzennis on 05/10/20.
 */
class PlayWidgetUseCase(private val repository: GraphqlRepository) : UseCase<PlayWidget>() {

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): PlayWidget {
        val gqlRequest = GraphqlRequest(query, PlayWidgetResponse::class.java, params)
        val gqlResponse = repository.getReseponse(
                listOf(gqlRequest),
                GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        )
        val errors = gqlResponse.getError(PlayWidgetResponse::class.java)
        if (!errors.isNullOrEmpty()) {
            throw Throwable(errors.firstOrNull()?.message)
        }
        val response = gqlResponse.getData<PlayWidgetResponse>(PlayWidgetResponse::class.java)
        return response.playWidget
    }

    companion object {

        private val query = """
        query playGetWidgetV2(${'$'}widgetType: String!, ${'$'}authorId: String, ${'$'}authorType: String){
          playGetWidgetV2(
            req: {
              widgetType:${'$'}widgetType,
              authorID: ${'$'}authorId,
              authorType: ${'$'}authorType
            })
          {
            data {
              __typename... on PlayWidgetChannel{
                ID
                title
                widgetType
                appLink
                webLink
                startTime
                config{
                  hasPromo
                  isReminderSet
                }
                partner {
                  ID
                  name
                }
                video {
                  ID
                  type
                  coverUrl
                  streamSource
                  isShowTotalView,
                  isLive
                }
                stats {
                  view {
                    formatted
                  }
                }
              }
              __typename ... on PlayWidgetBanner {
                backgroundURL
                appLink
                webLink      
              }
            }
            meta {
              isAutoRefresh
              autoRefreshTimer
              widgetTitle
              buttonText
              widgetBackground
            autoplayAmount
              autoplay
              buttonApplink
              buttonWeblink
              overlayImage
              overlayImageAppLink
              overlayImageWebLink
              gradient
              serverTimeOffset
              maxAutoplayCell
            }
          }
        }
        """

        private const val PARAM_WIDGET_TYPE = "shopId"
        private const val PARAM_AUTHOR_ID = "shopId"
        private const val PARAM_AUTHOR_TYPE = "shopId"

        const val VALUE_WIDGET_TYPE = "SHOP_PAGE"
        const val VALUE_AUTHOR_TYPE = "shop"

        @JvmStatic
        fun createParams(
                authorId: String,
                authorType: String,
                widgetType: String
        ): Map<String, Any> = mapOf(
                PARAM_AUTHOR_ID to authorId,
                PARAM_AUTHOR_TYPE to authorType,
                PARAM_WIDGET_TYPE to widgetType
        )
    }
}