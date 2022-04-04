package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
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
import javax.inject.Inject

/**
 * Created by mzennis on 2020-03-06.
 */
class PlayBottomSheetViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    private val repo: PlayViewerRepository,
) : ViewModel() {

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