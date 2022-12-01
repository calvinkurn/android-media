package com.tokopedia.privacycenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.GetWishlistCollectionByIdResponse
import com.tokopedia.privacycenter.data.WishlistBydIdDataModel
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst
import javax.inject.Inject

class GetWishlistCollectionByIdUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Int, PrivacyCenterStateResult<WishlistBydIdDataModel>>(dispatchers.io) {

    override suspend fun execute(params: Int): PrivacyCenterStateResult<WishlistBydIdDataModel> {
        val response = repository.request<Map<String, Int>, GetWishlistCollectionByIdResponse>(
            graphqlQuery(),
            mapOf(
                SharingWishlistConst.KEY_COLLECTION_ID to params
            )
        ).getWishlistCollectionById

        return if (response.status == SharingWishlistConst.STATUS_OK) {
            PrivacyCenterStateResult.Success(response.data)
        } else {
            PrivacyCenterStateResult.Fail(Throwable(response.errorMessage.first()))
        }
    }

    override fun graphqlQuery(): String {
        return """
            query getWishlistCollectionById(${'$'}collectionID: SuperInteger) {
                get_wishlist_collection_by_id(collectionID: ${'$'}collectionID) {
                    error_message
                    status
                    data {
                        ticker {
                            title
                            descriptions
                        }
                        collection {
                            id
                            name
                            access
                        }
                        access_options {
                            id
                            description
                            name
                        }
                    }
                }
            }
        """.trimIndent()
    }
}
