package com.tokopedia.privacycenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.data.GetWishlistCollectionResponse
import com.tokopedia.privacycenter.data.GetWishlistCollectionsParams
import com.tokopedia.privacycenter.data.WishlistCollectionsParams
import com.tokopedia.privacycenter.data.WishlistDataModel
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistStateResult
import javax.inject.Inject

class GetWishlistCollectionUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Int, SharingWishlistStateResult<WishlistDataModel>>(
    dispatchers.io
) {

    override suspend fun execute(params: Int): SharingWishlistStateResult<WishlistDataModel> {
        val param = WishlistCollectionsParams(
            GetWishlistCollectionsParams(
                collectionAccess = params
            )
        )

        val response = graphqlRepository.request<WishlistCollectionsParams, GetWishlistCollectionResponse>(
            graphqlQuery(),
            param
        ).getWishlistCollections

        return if (response.status == SharingWishlistConst.STATUS_OK && response.errorMessage.isEmpty()) {
            if (response.data.collections.isEmpty()) {
                SharingWishlistStateResult.CollectionEmpty(
                    response.data.emptyWishlistImageUrl
                )
            } else {
                SharingWishlistStateResult.RenderCollection(response.data)
            }
        } else {
            SharingWishlistStateResult.Fail(Throwable(response.errorMessage.first()))
        }
    }

    override fun graphqlQuery(): String {
        return """
            query getWishlistCollection(${'$'}params: GetWishlistCollectionsParams) {
                get_wishlist_collections(params: ${'$'}params) {
                    error_message
                    status
                    data {
                        empty_wishlist_image_url
                        collections {
                            id
                            name
                            total_item
                            item_text
                            access
                        }
                        total_collection
                        is_empty_state
                    }
                }
            }
        """.trimIndent()
    }
}
