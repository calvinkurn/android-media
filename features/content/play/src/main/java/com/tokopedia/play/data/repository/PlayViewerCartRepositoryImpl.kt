package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.play.domain.GetCartCountUseCase
import com.tokopedia.play.domain.PostAddToCartUseCase
import com.tokopedia.play.domain.repository.PlayViewerCartRepository
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by jegul on 29/07/21
 */
class PlayViewerCartRepositoryImpl @Inject constructor(
        private val getCartCountUseCase: GetCartCountUseCase,
        private val postAddToCartUseCase: PostAddToCartUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers,
) : PlayViewerCartRepository {

    override suspend fun getItemCountInCart(): Int = withContext(dispatchers.io) {
        return@withContext getCartCountUseCase.executeOnBackground()
    }

    override suspend fun addItemToCart(productId: String, productName: String, productShopId: String, price: String, qty: Int): Boolean = withContext(dispatchers.io) {
        val response = postAddToCartUseCase.apply {
            parameters = AddToCartUseCase.getMinimumParams(
                    productId = productId,
                    productName = productName,
                    shopId = productShopId,
                    quantity = qty,
                    atcExternalSource = AtcFromExternalSource.ATC_FROM_PLAY,
                    price = price,
                    userId = userSession.userId,
            )
        }.executeOnBackground()
        return@withContext response.data.success == 1
    }
}