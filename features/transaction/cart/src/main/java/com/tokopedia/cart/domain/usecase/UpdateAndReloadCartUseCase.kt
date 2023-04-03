package com.tokopedia.cart.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cart.data.model.request.UpdateCartWrapperRequest
import com.tokopedia.cart.domain.model.updatecart.UpdateAndReloadCartListData
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class UpdateAndReloadCartUseCase @Inject constructor(
    private val updateCartUseCase: UpdateCartUseCase,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<UpdateCartWrapperRequest, UpdateAndReloadCartListData>(dispatcher.io) {
    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: UpdateCartWrapperRequest): UpdateAndReloadCartListData {
        val updateAndReloadCartListData = UpdateAndReloadCartListData()
        val updateCartData = updateCartUseCase.setParams(params).executeOnBackground()
        updateAndReloadCartListData.updateCartData = updateCartData
        updateAndReloadCartListData.cartId = params.cartId
        updateAndReloadCartListData.getCartState = params.getCartState
        return updateAndReloadCartListData
    }
}
