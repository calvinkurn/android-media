package com.tokopedia.play.widget.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetResponse
import com.tokopedia.play.widget.domain.query.PlayWidgetQueryParamBuilder
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject


/**
 * Created by mzennis on 05/10/20.
 */
class PlayWidgetUseCase @Inject constructor(private val repository: GraphqlRepository) : UseCase<PlayWidget>() {

    var params: Map<String, Any> = emptyMap()
    var widgetType: WidgetType? = null

    override suspend fun executeOnBackground(): PlayWidget {
        val query = PlayWidgetQueryParamBuilder
            .setWidgetType(widgetType)
            .setBody(BODY)
            .build()

        val gqlRequest = GraphqlRequest(query, PlayWidgetResponse::class.java, params)
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
        const val BODY = """
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
        """

        @JvmStatic
        fun createParams(
                widgetType: WidgetType,
                isWifi: Boolean,
        ): Map<String, Any> {
            return mapOf(
                PlayWidgetQueryParamBuilder.PARAM_AUTHOR_ID to widgetType.authorId,
                PlayWidgetQueryParamBuilder.PARAM_AUTHOR_TYPE to widgetType.authorType,
                PlayWidgetQueryParamBuilder.PARAM_WIDGET_TYPE to widgetType.typeKey,
                PlayWidgetQueryParamBuilder.PARAM_IS_WIFI to isWifi,
                PlayWidgetQueryParamBuilder.PARAM_PRODUCT_ID to widgetType.productIds,
                PlayWidgetQueryParamBuilder.PARAM_CATEGORY_ID to widgetType.categoryIds
            )
        }
    }

    sealed class WidgetType {

        abstract val typeKey: String
        abstract val authorId: String
        abstract val authorType: String
        abstract val productIds: String
        abstract val categoryIds: String

        data class ShopPage(val shopId: String) : WidgetType() {
            override val typeKey: String
                get() = "SHOP_PAGE"

            override val authorId: String
                get() = shopId

            override val authorType: String
                get() = "shop"

            override val productIds: String
                get() = ""

            override val categoryIds: String
                get() = ""
        }
        object Home : WidgetType() {
            override val typeKey: String
                get() = "HOME"

            override val authorId: String
                get() = ""

            override val authorType: String
                get() = ""

            override val productIds: String
                get() = ""

            override val categoryIds: String
                get() = ""
        }
        object Feeds : WidgetType() {
            override val typeKey: String
                get() = "FEEDS"

            override val authorId: String
                get() = ""

            override val authorType: String
                get() = ""

            override val productIds: String
                get() = ""

            override val categoryIds: String
                get() = ""
        }
        data class SellerApp(val shopId: String) : WidgetType() {
            override val typeKey: String
                get() = "SELLER_APP"

            override val authorId: String
                get() = shopId

            override val authorType: String
                get() = "shop"

            override val productIds: String
                get() = ""

            override val categoryIds: String
                get() = ""
        }

        data class DiscoveryPage(val widgetID: String): WidgetType(){
            override val typeKey: String
                get() = "DISCO_PAGE"

            override val authorId: String
                get() = widgetID

            override val authorType: String
                get() = ""

            override val productIds: String
                get() = ""

            override val categoryIds: String
                get() = ""
        }

        data class PDPWidget(val productIdList: String, val categoryIdList: String): WidgetType(){
            override val typeKey: String
                get() = "PDP_WIDGET"

            override val authorId: String
                get() = ""

            override val authorType: String
                get() = ""

            override val productIds: String
                get() = productIdList

            override val categoryIds: String
                get() = categoryIdList
        }
    }
}