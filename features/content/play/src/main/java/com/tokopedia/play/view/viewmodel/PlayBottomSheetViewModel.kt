package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.data.CartFeedbackResponseModel
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.CartFeedbackUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.VariantSheetUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.util.event.Event
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by mzennis on 2020-03-06.
 */
class PlayBottomSheetViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    private val repo: PlayViewerRepository,
) : ViewModel() {

    private val _observableAddToCart = MutableLiveData<PlayResult<Pair<Event<CartFeedbackUiModel>, ProductSectionUiModel.Section>>>()
    private val _observableProductVariant = MutableLiveData<PlayResult<VariantSheetUiModel>>()

    private val _observableLoggedInInteractionEvent = MutableLiveData<Event<LoginStateEvent>>()
    val observableLoggedInInteractionEvent: LiveData<Event<LoginStateEvent>> = _observableLoggedInInteractionEvent

    val observableAddToCart: LiveData<PlayResult<Pair<Event<CartFeedbackUiModel>, ProductSectionUiModel.Section>>> = _observableAddToCart
    val observableProductVariant: LiveData<PlayResult<VariantSheetUiModel>> = _observableProductVariant

    fun doInteractionEvent(event: InteractionEvent) {
        _observableLoggedInInteractionEvent.value = Event(
                if (event.needLogin && !userSession.isLoggedIn) LoginStateEvent.NeedLoggedIn(event)
                else LoginStateEvent.InteractionAllowed(event)
        )
    }

    fun addToCart(product: PlayProductUiModel.Product, sectionInfo: ProductSectionUiModel.Section, notes: String = "", action: ProductAction, type: BottomInsetsType) {
        _observableAddToCart.value = PlayResult.Loading(false)

        //TODO(If isSuccess = false, treat that as Failure instead of Success(isSuccess=true))
        viewModelScope.launchCatchError(block = {
            val responseCart = withContext(dispatchers.io) {
                repo.addItemToCart(
                    productId = product.id,
                    productName = product.title,
                    productShopId = product.shopId,
                    price = when (product.price) {
                        is OriginalPrice -> product.price.priceNumber.toString()
                        is DiscountedPrice -> product.price.discountedPriceNumber.toString()
                    },
                    qty = product.minQty,
                )
            }

            _observableAddToCart.value = PlayResult.Success(Pair(
                Event(mappingResponseCart(responseCart, product, action, type)), sectionInfo)
            )
        }) {
            _observableAddToCart.value = PlayResult.Success(
                Pair(Event(
                    CartFeedbackUiModel(
                        isSuccess = false,
                        errorMessage = it,
                        cartId = "",
                        product = product,
                        action = action,
                        bottomInsetsType = type
                    )
                ), sectionInfo)
            )
        }
    }

    fun onFreezeBan() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    private fun mappingResponseCart(response: CartFeedbackResponseModel,
                                    product: PlayProductUiModel.Product,
                                    action: ProductAction,
                                    bottomInsetsType: BottomInsetsType) =
            CartFeedbackUiModel(
                    isSuccess = response.isSuccess,
                    errorMessage = response.errorMessage,
                    cartId = response.cartId,
                    product = product,
                    action = action,
                    bottomInsetsType = bottomInsetsType
            )
}