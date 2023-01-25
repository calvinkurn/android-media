package com.tokopedia.product.detail.postatc.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.postatc.usecase.GetPostAtcLayoutUseCase
import javax.inject.Inject

class PostAtcViewModel @Inject constructor(
    private val getPostAtcLayoutUseCase: GetPostAtcLayoutUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    fun fetchLayout(
        productId: String,
        cartId: String,
        layoutId: String
    ) {
        launchCatchError(block = {
            val result = getPostAtcLayoutUseCase.execute(
                productId,
                cartId,
                layoutId
            )
            result
        }, onError = {
            it
        })
    }
}
