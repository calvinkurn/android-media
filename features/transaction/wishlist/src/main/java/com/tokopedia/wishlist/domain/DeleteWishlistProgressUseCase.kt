package com.tokopedia.wishlist.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.wishlist.data.model.response.DeleteWishlistProgressResponse
import javax.inject.Inject

@GqlQuery("CountDeletionProgressQuery", DeleteWishlistProgressUseCase.query)
class DeleteWishlistProgressUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, DeleteWishlistProgressResponse>(dispatcher.io) {

    override suspend fun execute(params: Unit): DeleteWishlistProgressResponse {
        return repository.request(CountDeletionProgressQuery(), params)
    }

    override fun graphqlQuery(): String = ""

    companion object {
        const val query = """
            query DeleteWishlistProgress() {
              delete_wishlist_progress() {
                error_message
                status
                data {
                  total_items
                  successfully_removed_items
                  message
                  ticker_color
                  success
                  toaster_message
                }
              }
            }"""
    }
}
