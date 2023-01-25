package com.tokopedia.product.detail.postatc.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.postatc.base.PostAtcUiModel
import com.tokopedia.product.detail.postatc.usecase.GetPostAtcLayoutUseCase
import com.tokopedia.product.detail.postatc.util.mapToUiModel
import javax.inject.Inject

class PostAtcViewModel @Inject constructor(
    private val getPostAtcLayoutUseCase: GetPostAtcLayoutUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val _layouts = MutableLiveData<List<PostAtcUiModel>>()
    val layouts: LiveData<List<PostAtcUiModel>> = _layouts

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

            val uiModels = result.components.mapToUiModel()
            _layouts.postValue(uiModels)

        }, onError = {
            it
        })
    }

}
