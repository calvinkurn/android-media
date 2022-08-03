package com.tokopedia.kol.feature.postdetail.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.kol.feature.postdetail.domain.ContentDetailRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by meyta.taliti on 02/08/22.
 */
class ContentDetailRepositoryImpl @Inject constructor(
    private  val dispatcher: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val addToCartUseCase: AddToCartUseCase,
) : ContentDetailRepository {

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
}