package com.tokopedia.kol.feature.postdetail.data.repository

import android.text.TextUtils
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.feedcomponent.util.CustomUiMessageThrowable
import com.tokopedia.kol.R
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.kol.feature.postdetail.view.datamodel.type.ShopFollowAction
import com.tokopedia.kolcommon.domain.interactor.SubmitActionContentUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.shop.common.domain.interactor.UpdateFollowStatusUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by meyta.taliti on 02/08/22.
 */
class ContentDetailRepositoryImpl @Inject constructor(
    private  val dispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val followShopUseCase: UpdateFollowStatusUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val addToWishlistUseCase: AddToWishlistV2UseCase,
    private val submitActionContentUseCase: SubmitActionContentUseCase,
) : ContentDetailRepository {

    override suspend fun followShop(shopId: String, action: ShopFollowAction) {
        return withContext(dispatcher.io) {
            followShopUseCase.params = UpdateFollowStatusUseCase.createParams(
                shopId,
                action.value
            )
            val response = followShopUseCase.executeOnBackground()
            if (response.followShop?.success == false) {
                throw CustomUiMessageThrowable(
                    if (action.isFollowing) R.string.feed_follow_error_message
                    else R.string.feed_unfollow_error_message
                )
            }
        }

    }

    override suspend fun addToCart(
        productId: String,
        productName: String,
        price: String,
        shopId: String,
    ): Boolean = withContext(dispatcher.io) {
        val params = AddToCartUseCase.getMinimumParams(
            productId,
            shopId,
            productName = productName,
            price = price,
            userId = userSession.userId
        )
        try {
            addToCartUseCase.setParams(params)
            val response = addToCartUseCase.executeOnBackground()
            if (response.isDataError()) throw MessageErrorException(response.getAtcErrorMessage())
            return@withContext !response.isStatusError()
        } catch (e: Throwable) {
            if (e is ResponseErrorException) throw MessageErrorException(e.localizedMessage)
            else throw e
        }
    }

    override suspend fun addToWishlist(productId: String): Result<AddToWishlistV2Response.Data.WishlistAddV2> {
        return withContext(dispatcher.io) {
            addToWishlistUseCase.setParams(productId, userSession.userId)
            addToWishlistUseCase.executeOnBackground()
        }
    }

    override suspend fun deleteContent(contentId: String) = withContext(dispatcher.io) {
        submitActionContentUseCase.setRequestParams(SubmitActionContentUseCase.paramToDeleteContent(contentId))
        val response = submitActionContentUseCase.executeOnBackground()
        if (TextUtils.isEmpty(response.content.error).not()) {
            throw MessageErrorException(response.content.error)
        }
    }
}