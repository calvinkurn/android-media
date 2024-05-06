package com.tokopedia.content.common.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.model.WidgetSlot
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class GetPlayWidgetSlotUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetPlayWidgetSlotUseCase.Param, WidgetSlot>(dispatchers.io) {

    private val queryObject = GetPlayWidgetSlotUseCaseQuery()

    @GqlQuery(QUERY_NAME, QUERY)
    override fun graphqlQuery(): String {
        return queryObject.getQuery()
    }

    override suspend fun execute(params: Param): WidgetSlot {
        return graphqlRepository.request(queryObject, params)
    }

    companion object {
        private const val KEY_REQ = "req"

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
                          badge_url
                          thumbnail_url
                          app_link
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
                    status_code
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

    data class Param(
        @SerializedName("req")
        val req: Request
    ) : GqlParam {

        data class Request(
            @SerializedName("group")
            val group: String,

            @SerializedName("cursor")
            val cursor: String,

            @SerializedName("source_type")
            val sourceType: String,

            @SerializedName("source_id")
            val sourceId: String,

            @SerializedName("is_wifi")
            val isWifi: Boolean,

            @SerializedName("search_keyword")
            val searchKeyword: String,
        )
    }
}
