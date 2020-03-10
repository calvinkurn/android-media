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
import com.tokopedia.variant_common.model.ProductDetailVariantCommonResponse
import com.tokopedia.variant_common.use_case.GetProductVariantUseCase
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Created by mzennis on 2020-03-06.
 */
class PlayVariantViewModel @Inject constructor(
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val postAddtoCartUseCase: PostAddtoCartUseCase,
        private val dispatchers: CoroutineDispatcherProvider
) : BaseViewModel(dispatchers.main) {

    private val job = SupervisorJob()

    private val _observableAddtoCart = MutableLiveData<CartFeedbackUiModel>()
    private val _observableProductVariant = MutableLiveData<ProductDetailVariantCommonResponse>()

    val observableAddtoCart: LiveData<CartFeedbackUiModel> = _observableAddtoCart
    val observableProductVariant: LiveData<ProductDetailVariantCommonResponse> = _observableProductVariant

    fun getProductVariant(productId: String) {
        launchCatchError(block = {
            val productVariant = withContext(dispatchers.io) {
                getProductVariantUseCase.params = getProductVariantUseCase.createParams(productId)
                getProductVariantUseCase.executeOnBackground()
            }
            _observableProductVariant.value = productVariant
        }){}
    }

    fun addtoCart(productId: String, shopId: String, quantity: Int = 1, notes: String = "", isAtcOnly: Boolean = true) {
        launchCatchError(block = {
            val responseCart = withContext(dispatchers.io) {
                postAddtoCartUseCase.parameters = AddToCartUseCase.getMinimumParams(productId, shopId, quantity, notes)
                postAddtoCartUseCase.executeOnBackground()
            }

            _observableAddtoCart.value = mappingResponseCart(responseCart)
        }) { }
    }

    private fun mappingResponseCart(addToCartDataModel: AddToCartDataModel) =
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