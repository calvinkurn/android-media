package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AddToWishlistV2UseCaseStub @Inject constructor(
    graphqlRepository: GraphqlRepository
): AddToWishlistV2UseCase(graphqlRepository) {

    var isFail = false

    override suspend fun executeOnBackground(): Result<AddToWishlistV2Response.Data.WishlistAddV2> {
        val result = AddToWishlistV2Response(
            AddToWishlistV2Response.Data(
                AddToWishlistV2Response.Data.WishlistAddV2(
                    success = !isFail
                )
            )
        )
        return Success(result.data.wishlistAdd)
    }
}
