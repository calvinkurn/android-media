package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.domain.PostAddToCartUseCase
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.CartFeedbackUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.VariantSheetUiModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.use_case.GetProductVariantUseCase
import com.tokopedia.variant_common.util.VariantCommonMapper
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by mzennis on 2020-03-06.
 */
class PlayBottomSheetViewModel @Inject constructor(
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val postAddToCartUseCase: PostAddToCartUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val _observableAddToCart = MutableLiveData<PlayResult<Event<CartFeedbackUiModel>>>()
    private val _observableProductVariant = MutableLiveData<PlayResult<VariantSheetUiModel>>()

    private val _observableLoggedInInteractionEvent = MutableLiveData<Event<LoginStateEvent>>()
    val observableLoggedInInteractionEvent: LiveData<Event<LoginStateEvent>> = _observableLoggedInInteractionEvent

    val observableAddToCart: LiveData<PlayResult<Event<CartFeedbackUiModel>>> = _observableAddToCart
    val observableProductVariant: LiveData<PlayResult<VariantSheetUiModel>> = _observableProductVariant

    fun getProductVariant(product: PlayProductUiModel.Product, action: ProductAction) {
        _observableProductVariant.value = PlayResult.Loading(true)

        viewModelScope.launchCatchError(block = {
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
                        listOfVariantCategory = categoryVariants.orEmpty()
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

    fun addToCart(product: PlayProductUiModel.Product, notes: String = "", action: ProductAction, type: BottomInsetsType) {
        _observableAddToCart.value = PlayResult.Loading(showPlaceholder = false)

        //TODO(If isSuccess = false, treat that as Failure instead of Success(isSuccess=true))
        viewModelScope.launchCatchError(block = {
            val responseCart = withContext(dispatchers.io) {
                postAddToCartUseCase.parameters = AddToCartUseCase.getMinimumParams(
                        product.id,
                        product.shopId,
                        product.minQty,
                        notes,
                        AddToCartRequestParams.ATC_FROM_PLAY,
                        product.title,
                        price = when (product.price) {
                            is OriginalPrice -> product.price.priceNumber.toString()
                            is DiscountedPrice -> product.price.discountedPriceNumber.toString()
                        },
                        userId = userSession.userId
                )
                postAddToCartUseCase.executeOnBackground()
            }

            _observableAddToCart.value = PlayResult.Success(Event(mappingResponseCart(responseCart, product, action, type)))
        }) {
            _observableAddToCart.value = PlayResult.Success(Event(
                    CartFeedbackUiModel(
                            isSuccess = false,
                            errorMessage = it.localizedMessage.orEmpty(),
                            cartId = "",
                            product = product,
                            action = action,
                            bottomInsetsType = type
                    )
            ))
        }
    }

    fun onFreezeBan() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    private fun mappingResponseCart(addToCartDataModel: AddToCartDataModel,
                                    product: PlayProductUiModel.Product,
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
}