package com.tokopedia.people.domains

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.people.model.UserPostModel
import javax.inject.Inject

/**
 * Duplicate with: com.tokopedia.videoTabComponent.domain.usecase.GetPlayContentUseCase
 */
@GqlQuery(PlayPostContentUseCase.QUERY_NAME, PlayPostContentUseCase.QUERY)
class PlayPostContentUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<UserPostModel>(graphqlRepository) {

    init {
        setGraphqlQuery(PlayPostContentUseCaseQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(UserPostModel::class.java)
    }

    suspend fun executeOnBackground(
        group: String,
        cursor: String,
        sourceType: String,
        sourceId: String,
    ): UserPostModel {
        val request = mapOf(
            KEY_GROUP to group,
            KEY_CURSOR to cursor,
            KEY_SOURCE_TYPE to sourceType,
            KEY_SOURCE_ID to sourceId,
        )
        setRequestParams(request)

        return executeOnBackground()
    }

    companion object {
        private const val KEY_GROUP = "group"
        private const val KEY_CURSOR = "cursor"
        private const val KEY_SOURCE_TYPE = "source_type"
        private const val KEY_SOURCE_ID = "source_id"

        const val QUERY_NAME = "PlayPostContentUseCaseQuery"
        const val QUERY = """
            query playGetContentSlot(
                ${"$$KEY_GROUP"}: String, 
                ${"$$KEY_CURSOR"}: String, 
                ${"$$KEY_SOURCE_TYPE"}: String, 
                ${"$$KEY_SOURCE_ID"}: String
            ) {
                playGetContentSlot(req: {
                    $KEY_GROUP: ${"$$KEY_GROUP"}, 
                    $KEY_CURSOR: ${"$$KEY_CURSOR"}, 
                    $KEY_SOURCE_TYPE: ${"$$KEY_SOURCE_TYPE"}, 
                    $KEY_SOURCE_ID: ${"$$KEY_SOURCE_ID"}
                }) {
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
                        partner {
                          id
                          name
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
    }
}
