package com.tokopedia.content.common.usecase

import com.tokopedia.content.common.model.WidgetSlot
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

@GqlQuery(GetPlayWidgetSlotUseCase.QUERY_NAME, GetPlayWidgetSlotUseCase.QUERY)
class GetPlayWidgetSlotUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<WidgetSlot>(graphqlRepository) {

    init {
        setGraphqlQuery(GetPlayWidgetSlotUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(WidgetSlot::class.java)
    }

    suspend fun executeOnBackground(
        group: String,
        cursor: String,
        sourceType: String,
        sourceId: String,
        isWifi: Boolean
    ): WidgetSlot {
        val request = mapOf(
            KEY_REQ to mapOf(
                KEY_GROUP to group,
                KEY_CURSOR to cursor,
                KEY_SOURCE_TYPE to sourceType,
                KEY_SOURCE_ID to sourceId,
                KEY_WIFI to isWifi
            )
        )
        setRequestParams(request)
        return executeOnBackground()
    }

    companion object {
        const val KEY_REQ = "req"
        private const val KEY_GROUP = "group"
        private const val KEY_CURSOR = "cursor"
        private const val KEY_SOURCE_TYPE = "source_type"
        private const val KEY_SOURCE_ID = "source_id"
        const val KEY_WIFI = "is_wifi"

        const val QUERY_NAME = "GetPlayWidgetSlotUseCaseQuery"
        const val QUERY = """
            query playGetWidgetSlot(
                ${"$$KEY_REQ"}: PlayGetContentSlotRequest!
            ) {
                playGetContentSlot(
                 $KEY_REQ: ${"$$KEY_REQ"}
                ) {
                  data {
                    id
                    title
                    type
                    items {
                    ... on PlayBanner {
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
                    ... on PlaySlotTabMenu {
                          id
                          slug_id
                          label
                          icon_url
                          group
                          source_type
                          source_id
                      }
                      ... on PlayChannelDetails {
                        id
                        title
                        start_time
                        description
                        air_time
                        is_live
                        cover_url
                        video {
                          id
                          orientation
                          type
                          stream_source
                        }
                        stats {
                          view {
                            value
                            formatted
                          }
                        }
                        configurations {
                          has_promo
                          reminder {
                            is_set
                          }
                          promo_labels {
                            text
                            type
                          }
                        }
                        partner {
                          id
                          name
                          type
                          thumbnail_url
                        }
                        app_link
                        web_link
                        display_type
                        recommendationType
                      }
                    }
                    hash
                  }
                  meta {
                    next_cursor
                    is_autoplay
                    max_autoplay_in_cell
                    is_autorefresh
                    autorefresh_timer
                  }
                }
            }
        """
    }
}
