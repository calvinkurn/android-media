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

    private var params: Map<String, Any> = emptyMap()
    private var query = ""

    override suspend fun executeOnBackground(): PlayWidget {
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

    fun setQuery(widgetType: WidgetType, isWifi: Boolean) {
        val param = hashMapOf(
            PlayWidgetQueryParamBuilder.PARAM_AUTHOR_ID to widgetType.authorId,
            PlayWidgetQueryParamBuilder.PARAM_AUTHOR_TYPE to widgetType.authorType,
            PlayWidgetQueryParamBuilder.PARAM_WIDGET_TYPE to widgetType.typeKey,
            PlayWidgetQueryParamBuilder.PARAM_IS_WIFI to isWifi,
        )

        if(widgetType is WidgetType.PDPWidget) {
            param[PlayWidgetQueryParamBuilder.PARAM_PRODUCT_ID] = widgetType.productIdList.joinToString(",")
            param[PlayWidgetQueryParamBuilder.PARAM_CATEGORY_ID] = widgetType.categoryIdList.joinToString(",")
        }

        this.params = param
        this.query = PlayWidgetQueryParamBuilder()
            .setWidgetType(widgetType)
            .setBody(BODY)
            .build()
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
                    recommendationType
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

        data class PDPWidget(val productIdList: List<String>, val categoryIdList: List<String>): WidgetType(){
            override val typeKey: String
                get() = "PDP_WIDGET"

            override val authorId: String
                get() = ""

            override val authorType: String
                get() = ""
        }
    }
}