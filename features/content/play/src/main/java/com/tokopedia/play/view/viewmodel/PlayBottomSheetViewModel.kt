package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.domain.PostAddtoCartUseCase
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.Event
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.type.ProductLineUiModel
import com.tokopedia.play.view.uimodel.CartFeedbackUiModel
import com.tokopedia.play.view.uimodel.VariantSheetUiModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.use_case.GetProductVariantUseCase
import com.tokopedia.variant_common.util.VariantCommonMapper
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Created by mzennis on 2020-03-06.
 */
class PlayBottomSheetViewModel @Inject constructor(
        private val getProductVariantUseCase: GetProductVariantUseCase,
        private val postAddtoCartUseCase: PostAddtoCartUseCase,
        private val userSession: UserSessionInterface,
        private val dispatchers: CoroutineDispatcherProvider
) : BaseViewModel(dispatchers.main) {

    private val job = SupervisorJob()

    private val _observableAddtoCart = MutableLiveData<CartFeedbackUiModel>()
    private val _observableProductVariant = MutableLiveData<VariantSheetUiModel>()

    private val _observableLoggedInInteractionEvent = MutableLiveData<Event<LoginStateEvent>>()
    val observableLoggedInInteractionEvent: LiveData<Event<LoginStateEvent>> = _observableLoggedInInteractionEvent

    val observableAddtoCart: LiveData<CartFeedbackUiModel> = _observableAddtoCart
    val observableProductVariant: LiveData<VariantSheetUiModel> = _observableProductVariant

    fun getProductVariant(product: ProductLineUiModel, action: ProductAction) {
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
                       listOfVariantCategory = categoryVariants?: emptyList()
               )
            }
            _observableProductVariant.value = variantSheetUiModel
        }){}
    }

    fun doInteractionEvent(event: InteractionEvent) {
        _observableLoggedInInteractionEvent.value = Event(
                if (event.needLogin && !userSession.isLoggedIn) LoginStateEvent.NeedLoggedIn(event)
                else LoginStateEvent.InteractionAllowed(event)
        )
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