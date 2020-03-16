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
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OutOfStock
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.ProductLineUiModel
import com.tokopedia.play.view.uimodel.CartFeedbackUiModel
import com.tokopedia.play.view.uimodel.VariantPlaceholderUiModel
import com.tokopedia.play.view.uimodel.VariantSheetUiModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.use_case.GetProductVariantUseCase
import com.tokopedia.variant_common.util.VariantCommonMapper
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
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

    fun getProductVariant(product: ProductLineUiModel, action: ProductAction) {
        showVariantSheetPlaceholder()

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
        }){}
    }

    fun doInteractionEvent(event: InteractionEvent) {
        _observableLoggedInInteractionEvent.value = Event(
                if (event.needLogin && !userSession.isLoggedIn) LoginStateEvent.NeedLoggedIn(event)
                else LoginStateEvent.InteractionAllowed(event)
        )
    }

    fun addToCart(productId: String, shopId: String, quantity: Int, notes: String = "", action: ProductAction) {
        launchCatchError(block = {
            val responseCart = withContext(dispatchers.io) {
                postAddToCartUseCase.parameters = AddToCartUseCase.getMinimumParams(productId, shopId, quantity, notes)
                postAddToCartUseCase.executeOnBackground()
            }

            _observableAddToCart.value = mappingResponseCart(responseCart, action)
        }) { }
    }

    private fun mappingResponseCart(addToCartDataModel: AddToCartDataModel, action: ProductAction) =
            CartFeedbackUiModel(
                    isSuccess = addToCartDataModel.data.success == 1,
                    errorMessage = if (addToCartDataModel.errorMessage.size > 0)
                        addToCartDataModel.errorMessage.joinToString { "$it " } else "",
                    action = action
            )

    private fun showVariantSheetPlaceholder() {
        _observableProductVariant.value = PlayResult.Loading(
                true,
                List(PLACEHOLDER_VARIANT_CATEGORY_COUNT) { VariantPlaceholderUiModel.Category(
                        List(PLACEHOLDER_VARIANT_OPTION_COUNT) { VariantPlaceholderUiModel.Option }
                ) }
        )
    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }

    companion object {
        private const val PLACEHOLDER_VARIANT_CATEGORY_COUNT = 2
        private const val PLACEHOLDER_VARIANT_OPTION_COUNT = 7
    }

    // TODO("testing")
//    private fun setMockVariantSheetContent(action: ProductAction) {
//        _observableProductVariant.value = PlayResult.Success(VariantSheetUiModel(
//                product = ProductLineUiModel(
//                        id = "123",
//                        shopId = "123",
//                        imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/5/8/52943980/52943980_908dc570-338d-46d5-aed2-4871f2840d0d_1664_1664",
//                        title = "Product Value",
//                        isVariantAvailable = true,
//                        price = DiscountedPrice(
//                                originalPrice = "Rp20.000",
//                                discountPercent = 10,
//                                discountedPrice = "Rp20.000"
//                        ),
//                        stock = OutOfStock,
//                        minQty = 1,
//                        applink = null
//                ),
//                action = action
//        ))
//    }
}