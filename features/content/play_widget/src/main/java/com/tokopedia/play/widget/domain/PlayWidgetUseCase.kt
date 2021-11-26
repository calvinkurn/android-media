package com.tokopedia.play.widget.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 05/10/20.
 */
@GqlQuery(PlayWidgetUseCase.QUERY_NAME, PlayWidgetUseCase.QUERY)
class PlayWidgetUseCase @Inject constructor(private val repository: GraphqlRepository) : UseCase<PlayWidget>() {

    var params: Map<String, Any> = emptyMap()

    override suspend fun executeOnBackground(): PlayWidget {
        val gqlRequest = GraphqlRequest(GetPlayWidgetV2Query.GQL_QUERY, PlayWidgetResponse::class.java, params)
        val gqlResponse = repository.response(
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

        private const val PARAM_WIDGET_TYPE = "widgetType"
        private const val PARAM_AUTHOR_ID = "authorID"
        private const val PARAM_AUTHOR_TYPE = "authorType"
        private const val PARAM_IS_WIFI = "isWifi"

        const val QUERY_NAME = "GetPlayWidgetV2Query"

        const val QUERY = """
        query playGetWidgetV2(
            ${'$'}$PARAM_WIDGET_TYPE: String!, 
            ${'$'}$PARAM_AUTHOR_ID: String, 
            ${'$'}$PARAM_AUTHOR_TYPE: String,
            ${'$'}$PARAM_IS_WIFI: Boolean
        ){
          playGetWidgetV2(
            req: {
              $PARAM_WIDGET_TYPE:${'$'}$PARAM_WIDGET_TYPE,
              $PARAM_AUTHOR_ID: ${'$'}$PARAM_AUTHOR_ID,
              $PARAM_AUTHOR_TYPE: ${'$'}$PARAM_AUTHOR_TYPE,
              $PARAM_IS_WIFI: ${'$'}$PARAM_IS_WIFI
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
                widgetSortingMethod
                config{
                  hasPromo
                  isReminderSet
                  promo_labels {
                    text
                    type
                  }
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
                share {
                  text
                  redirect_url
                  use_short_url
                  meta_title
                  meta_description
                  is_show_button
                }
                performanceSummaryPageLink
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
              maxAutoplayWifi
              template
              isButtonVisible
              businessWidgetPosition
            }
          }
        }
        """

        @JvmStatic
        fun createParams(
                widgetType: WidgetType,
                isWifi: Boolean,
        ): Map<String, Any> = mapOf(
                PARAM_AUTHOR_ID to widgetType.authorId,
                PARAM_AUTHOR_TYPE to widgetType.authorType,
                PARAM_WIDGET_TYPE to widgetType.typeKey,
                PARAM_IS_WIFI to isWifi,
        )
    }

    sealed class WidgetType {

        abstract val typeKey: String
        abstract val authorId: String
        abstract val authorType: String

        data class ShopPage(val shopId: String) : WidgetType() {
            override val typeKey: String
                get() = "SHOP_PAGE"

            override val authorId: String
                get() = shopId

            override val authorType: String
                get() = "shop"
        }
        object Home : WidgetType() {
            override val typeKey: String
                get() = "HOME"

            override val authorId: String
                get() = ""

            override val authorType: String
                get() = ""
        }
        object Feeds : WidgetType() {
            override val typeKey: String
                get() = "FEEDS"

            override val authorId: String
                get() = ""

            override val authorType: String
                get() = ""
        }
        data class SellerApp(val shopId: String) : WidgetType() {
            override val typeKey: String
                get() = "SELLER_APP"

            override val authorId: String
                get() = shopId

            override val authorType: String
                get() = "shop"
        }

        data class DiscoveryPage(val widgetID: String): WidgetType(){
            override val typeKey: String
                get() = "DISCO_PAGE"

            override val authorId: String
                get() = widgetID

            override val authorType: String
                get() = ""
        }
    }
}