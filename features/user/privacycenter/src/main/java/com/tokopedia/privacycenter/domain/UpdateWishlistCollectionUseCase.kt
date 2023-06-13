package com.tokopedia.privacycenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.*
import com.tokopedia.privacycenter.ui.sharingwishlist.SharingWishlistConst
import javax.inject.Inject

class UpdateWishlistCollectionUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<WishlistCollectionByIdDataModel, PrivacyCenterStateResult<UpdateWishlistDataModel>>(
    dispatchers.io
) {

    override suspend fun execute(params: WishlistCollectionByIdDataModel): PrivacyCenterStateResult<UpdateWishlistDataModel> {
        val param = UpdateWishlistParam(UpdateWishlistCollectionParam(params))

        val response = repository.request<UpdateWishlistParam, UpdateWishlistCollectionResponse>(
            graphqlQuery(),
            param
        ).updateWishlistCollection

        return if (response.status == SharingWishlistConst.STATUS_OK) {
            if (response.data.success) {
                PrivacyCenterStateResult.Success(response.data)
            } else {
                PrivacyCenterStateResult.Fail(Throwable(response.data.message))
            }
        } else {
            PrivacyCenterStateResult.Fail(Throwable(response.errorMessage.first()))
        }
    }

    override fun graphqlQuery(): String {
        return """
            mutation updateWishlistCollection(${'$'}params: UpdateWishlistCollectionParams) {
                update_wishlist_collection(params: ${'$'}params) {
                    status
                    error_message
                    data {
                        success
                        message
                    }
                }
            }
        """.trimIndent()
    }
}
