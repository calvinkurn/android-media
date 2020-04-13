package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.domain.PostAddToCartUseCase
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.Event
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.CartFeedbackUiModel
import com.tokopedia.play.view.uimodel.ProductLineUiModel
import com.tokopedia.play.view.uimodel.VariantSheetUiModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.use_case.GetProductVariantUseCase
import com.tokopedia.variant_common.util.VariantCommonMapper
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by mzennis on 2020-03-06.
 */
class PlayBottomSheetViewModel @Inject constructor(
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val postAddToCartUseCase: PostAddToCartUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatcherProvider
) : BaseViewModel(dispatchers.main) {

    private val job = SupervisorJob()

    private val _observableAddToCart = MutableLiveData<CartFeedbackUiModel>()
    private val _observableProductVariant = MutableLiveData<PlayResult<VariantSheetUiModel>>()

    private val _observableLoggedInInteractionEvent = MutableLiveData<Event<LoginStateEvent>>()
    val observableLoggedInInteractionEvent: LiveData<Event<LoginStateEvent>> = _observableLoggedInInteractionEvent

    val observableAddToCart: LiveData<CartFeedbackUiModel> = _observableAddToCart
    val observableProductVariant: LiveData<PlayResult<VariantSheetUiModel>> = _observableProductVariant

    override fun flush() {
        clearJob()
    }

    fun getProductVariant(product: ProductLineUiModel, action: ProductAction) {
        _observableProductVariant.value = PlayResult.Loading(true)

        launchCatchError(block = {
            val variantSheetUiModel = withContext(dispatchers.io) {
                getProductVariantUseCase.params = getProductVariantUseCase.createParams(product.id)
                val response = getProductVariantUseCase.executeOnBackground()
                val mapOfSelectedVariants = VariantCommonMapper.mapVariantIdentifierToHashMap(response.data)
                val categoryVariants = VariantCommonMapper.processVariant(response.data,
                        mapOfSelectedVariant = mapOfSelectedVariants)
                VariantSheetUiModel(
                        product = product,
                        action = action,
                        parentVariant = response.data,
                        mapOfSelectedVariants = mapOfSelectedVariants,
                        listOfVariantCategory = categoryVariants ?: emptyList()
                )
            }
            _observableProductVariant.value = PlayResult.Success(variantSheetUiModel)
        }){
            _observableProductVariant.value = PlayResult.Failure(it) {
                getProductVariant(product, action)
            }
        }
    }

    fun doInteractionEvent(event: InteractionEvent) {
        _observableLoggedInInteractionEvent.value = Event(
                if (event.needLogin && !userSession.isLoggedIn) LoginStateEvent.NeedLoggedIn(event)
                else LoginStateEvent.InteractionAllowed(event)
        )
    }

    fun addToCart(product: ProductLineUiModel, notes: String = "", action: ProductAction, type: BottomInsetsType) {
        launchCatchError(block = {
            val responseCart = withContext(dispatchers.io) {
                postAddToCartUseCase.parameters = AddToCartUseCase.getMinimumParams(
                        product.id,
                        product.shopId,
                        product.minQty,
                        notes
                )
                postAddToCartUseCase.executeOnBackground()
            }

            _observableAddToCart.value = mappingResponseCart(responseCart, product, action, type)
        }) { }
    }

    fun onFreezeBan() {
        clearJob()
    }

    private fun clearJob() {
        if (isActive && !masterJob.isCancelled) {
            masterJob.cancelChildren()
        }
    }

    private fun mappingResponseCart(addToCartDataModel: AddToCartDataModel,
                                    product: ProductLineUiModel,
                                    action: ProductAction,
                                    bottomInsetsType: BottomInsetsType) =
            CartFeedbackUiModel(
                    isSuccess = addToCartDataModel.data.success == 1,
                    errorMessage = if (addToCartDataModel.errorMessage.size > 0)
                        addToCartDataModel.errorMessage.joinToString { "$it " } else "",
                    cartId = addToCartDataModel.data.cartId,
                    product = product,
                    action = action,
                    bottomInsetsType = bottomInsetsType
            )

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}