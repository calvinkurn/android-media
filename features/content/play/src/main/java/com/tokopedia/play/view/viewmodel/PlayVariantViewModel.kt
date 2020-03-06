package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.domain.PostAddtoCartUseCase
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.uimodel.CartFeedbackUiModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext


/**
 * Created by mzennis on 2020-03-06.
 */
class PlayVariantViewModel(
        private val postAddtoCartUseCase: PostAddtoCartUseCase,
        private val dispatchers: CoroutineDispatcherProvider
) : BaseViewModel(dispatchers.main) {

    private val job = SupervisorJob()

    private val _observableAddtoCart = MutableLiveData<CartFeedbackUiModel>()
    val observableAddtoCart: LiveData<CartFeedbackUiModel> = _observableAddtoCart

    fun addtoCart(productId: String, shopId: String, quantity: Int = 1, notes: String = "", isAtcOnly: Boolean = true) {
        launchCatchError(block = {
            val cartFeedback = withContext(dispatchers.io) {
                postAddtoCartUseCase.parameters = AddToCartUseCase.getMinimumParams(productId, shopId, quantity, notes)
                postAddtoCartUseCase.executeOnBackground()
            }

            _observableAddtoCart.value = mapCartFeedback(cartFeedback)
        }) { }
    }

    private fun mapCartFeedback(addToCartDataModel: AddToCartDataModel) =
            CartFeedbackUiModel(
                    isSuccess = addToCartDataModel.data.success == 1,
                    errorMessage = if (addToCartDataModel.errorMessage.size > 0)
                        addToCartDataModel.errorMessage.joinToString { "$it\n" } else ""
            )

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}