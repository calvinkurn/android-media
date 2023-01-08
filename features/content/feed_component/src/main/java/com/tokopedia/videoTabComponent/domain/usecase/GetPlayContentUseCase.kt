package com.tokopedia.videoTabComponent.domain.usecase

import android.content.Context
import android.net.wifi.WifiManager
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.videoTabComponent.domain.model.data.ContentSlotResponse
import com.tokopedia.videoTabComponent.domain.model.data.VideoPageParams
import javax.inject.Inject

/**
 * Duplicate with: com.tokopedia.people.domains.PlayPostContentUseCase
 */
const val GQL_QUERY : String = """
    query playGetContentSlot(${'$'}req:  PlayGetContentSlotRequest!) {
    playGetContentSlot(req:${'$'}req) {
    data{
      id
      title
      type
      items{
        ... on PlayBanner{
          id
          title
          created_time
          updated_time
          slug
          description
          app_link
          web_link
          broadcaster_name
          image_url
          start_time
          end_time
        }
        ... on PlaySlotTabMenu{
          id
          slug_id
          label
          icon_url
          group
          source_type
          source_id
        }
        
        ... on PlayChannelDetails{
          id
          title
          description
          start_time
          end_time
          is_live
          air_time
          cover_url
          partner{
            id
            type
            name
            thumbnail_url
            badge_url
            app_link
            web_link
          }
          video{
            id
            orientation
            type
            cover_url
            stream_source
            autoplay
          }
          stats{
            view{
              value
              formatted
            }
          }
          configurations{
            has_promo
            reminder{
              is_set
            }
            promo_labels {
              text
              type
            }
          }
          app_link
          web_link
          share{
            text
            redirect_url
            use_short_url
            meta_title
            meta_description
          }
          display_type
        }
      }
      hash
      lihat_semua{
        show
        label
        web_link
      }
      inplace_pager{
        group
        cursor
        source_type
        source_id
      }
    }
    meta{
      next_cursor
      is_autoplay
      max_autoplay_in_cell
    }
    }
                 
}

"""
private const val CURSOR: String = "cursor"
private const val LIMIT = "limit"
private const val GROUP = "group"
private const val SOURCE_TYPE = "source_type"
private const val SOURCE_ID = "source_id"
private const val IS_WIFI = "is_wifi"
private const val GROUP_VALUE = "feeds_channels"

@GqlQuery("GetPlayContentUseCaseQuery", GQL_QUERY)
class GetPlayContentUseCase @Inject constructor(@ApplicationContext context: Context, graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<ContentSlotResponse>(graphqlRepository) {

    var context: Context? = null

    init {
        this.context = context
        setTypeClass(ContentSlotResponse::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetPlayContentUseCaseQuery.GQL_QUERY)
    }

    fun setParams(videoPageParams: VideoPageParams) {
        val isWifi = context?.let { isWifiEnabled(it) } ?: false
        val queryMap = mutableMapOf(
                CURSOR to videoPageParams.cursor,
                SOURCE_TYPE to videoPageParams.sourceType,
                GROUP to videoPageParams.group,
                SOURCE_ID to videoPageParams.sourceId,
                IS_WIFI to isWifi
        )
        val map = mutableMapOf("req" to queryMap)
        setRequestParams(map)
    }

    suspend fun execute(videoPageParams: VideoPageParams):
            ContentSlotResponse {
        this.setParams(videoPageParams)
        return executeOnBackground()
    }
    private fun isWifiEnabled(context: Context) : Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }

}