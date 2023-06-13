package com.tokopedia.wishlistcollection.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlistcollection.data.params.GetWishlistCollectionsBottomSheetParams
import com.tokopedia.wishlistcollection.data.response.GetWishlistCollectionsBottomSheetResponse
import javax.inject.Inject

@GqlQuery("GetWishlistCollectionsBottomsheetQuery", GetWishlistCollectionsBottomSheetUseCase.query)
class GetWishlistCollectionsBottomSheetUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) :
    CoroutineUseCase<GetWishlistCollectionsBottomSheetParams, GetWishlistCollectionsBottomSheetResponse>(dispatchers.io) {

    override suspend fun execute(params: GetWishlistCollectionsBottomSheetParams): GetWishlistCollectionsBottomSheetResponse {
        return repository.request(GetWishlistCollectionsBottomsheetQuery(), params)
    }

    override fun graphqlQuery(): String = query

    companion object {
        const val query = """
            query GetWishlistCollectionsBottomsheet(${'$'}productIds: String, ${'$'}source: String){
              get_wishlist_collections_bottomsheet(params:{product_ids: ${'$'}productIds, source: ${'$'}source}){
                status
                error_message
                data{
                  title
                  title_button{
                text
                    image_url
                    url
                    action
                  }
                  notification
                  placeholder{
                text
                    image_url
                    url
                    action
                  }
                  main_section{
                    text
                    collections{
                      id
                      name
                      total_item
                      item_text
                      label
                      image_url
                      is_contain_product
                      indicator {
                        title
                      }
                    }
                  }
                  additional_section{
                    text
                    collections{
                      id
                      name
                      total_item
                      item_text
                      label
                      image_url
                      is_contain_product
                      indicator {
                        title
                      }
                    }
                  }
                  total_collection
                  max_limit_collection
                  wording_max_limit_collection
                }
              }
            }"""
    }
}
