package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.people.model.UserPostModel
import javax.inject.Inject

const val PLAY_VIDEO_QUERY = """
    query playGetContentSlot(${'$'}group: String, ${'$'}cursor: String, ${'$'}source_type: String, ${'$'}source_id: String) {
                playGetContentSlot(req: {group:${'$'}group, cursor: ${'$'}cursor, source_type: ${'$'}source_type, source_id: \${'$'}source_id}) {
                  data {
                    id
                    title
                    type
                    items {
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
                          cover_url
                          autoplay
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
                        app_link
                        web_link
                        display_type
                      }
                    }
                    hash
                  }
                  meta {
                    next_cursor
                    is_autoplay
                    max_autoplay_in_cell
                  }
                }
                }
                
"""

@GqlQuery("PlayVOD", PLAY_VIDEO_QUERY)
class PlayPostContentUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun getPlayPost(group: String, cursor: String, sourceType: String, sourceId: String): UserPostModel {
        val request = GraphqlRequest(
            PlayVOD.GQL_QUERY,
            UserPostModel::class.java,
            getRequestParams(group,cursor,sourceType,sourceId)
        )

        useCase.clearRequest()
        useCase.addRequest(request)
        val response = useCase.executeOnBackground()
        return response.getData(UserPostModel::class.java)
    }

    private fun getRequestParams(group: String, cursor: String, sourceType: String, sourceId: String): MutableMap<String, Any?> {
        val requestMap = mutableMapOf<String, Any?>()
        requestMap[KEY_GROUP] = group
        requestMap[KEY_CURSOR] = cursor
        requestMap[KEY_SOURCE_TYPE] = sourceType
        requestMap[KEY_SOURCE_ID] = sourceId
        return requestMap
    }

    companion object {
        const val KEY_GROUP = "group"
        const val KEY_CURSOR = "cursor"
        const val KEY_SOURCE_TYPE = "source_type"
        const val KEY_SOURCE_ID = "source_id"
    }
}
