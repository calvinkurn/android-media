package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.request.UpdateCartWrapperRequest
import com.tokopedia.cart.domain.model.updatecart.UpdateAndReloadCartListData
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class UpdateAndReloadCartUseCase @Inject constructor(
    private val updateCartUseCase: UpdateCartUseCase,
) : UseCase<UpdateAndReloadCartListData>() {

    private var params: UpdateCartWrapperRequest = UpdateCartWrapperRequest()
    
    fun setParams(request: UpdateCartWrapperRequest): UpdateAndReloadCartUseCase {
        this.params = request
        return this
    }
    
    override suspend fun executeOnBackground(): UpdateAndReloadCartListData {
        val updateAndReloadCartListData = UpdateAndReloadCartListData()
        val updateCartData = updateCartUseCase.setParams(params).executeOnBackground()
        updateAndReloadCartListData.updateCartData = updateCartData
        updateAndReloadCartListData.cartId = params.cartId
        updateAndReloadCartListData.getCartState = params.getCartState
        return updateAndReloadCartListData
    }
}
