package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@GqlQuery("GetWishlistCollectionsQuery", GetWishlistCollectionUseCase.query)
class GetWishlistCollectionUseCase @Inject constructor(@ApplicationContext private val repository: GraphqlRepository) :
    CoroutineUseCase<Unit, GetWishlistCollectionResponse>(Dispatchers.IO) {

    override suspend fun execute(params: Unit): GetWishlistCollectionResponse {
        return repository.request(GetWishlistCollectionsQuery(), params)
    }

    override fun graphqlQuery(): String = query

    companion object {
        const val query: String = """
            query GetWishlistCollections {
              get_wishlist_collections {
                error_message
                status
                data {
                  ticker {
                    title
                    description
                  }
                  is_empty_state
                  show_delete_progress
                  empty_wishlist_image_url
                  empty_state {
                    messages {
                      image_url
                      description
                    }
                    buttons {
                      text
                      url
                      action
                      color
                    }
                  }
                  collections {
                    id
                    name
                    total_item
                    item_text
                    images
                    actions {
                      text
                      action
                      url
                    }
                    indicator {
                      title
                    }
                  }
                  placeholder {
                    text
                    image_url
                    action
                  }
                  onboarding_bottomsheet {
                    image_url
                    title
                    description
                    buttons {
                      text
                      url
                      action
                      color
                    }
                  }
                  onboarding_coachmark {
                    skip_button_text
                    details {
                      step
                      title
                      message
                      buttons {
                        text
                        action
                      }
                    }
                  }
                  total_collection
                  max_limit_collection
                  wording_max_limit_collection
                }
              }
            }
       	"""
    }
}
