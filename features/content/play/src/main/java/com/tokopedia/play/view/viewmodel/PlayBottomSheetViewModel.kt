package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.data.CartFeedbackResponseModel
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.DiscountedPrice
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.CartFeedbackUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayUserReportUiModel
import com.tokopedia.play.view.uimodel.VariantSheetUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
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
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    private val repo: PlayViewerRepository,
) : ViewModel() {

    private val _observableAddToCart = MutableLiveData<PlayResult<Pair<Event<CartFeedbackUiModel>, ProductSectionUiModel.Section>>>()
    private val _observableProductVariant = MutableLiveData<PlayResult<VariantSheetUiModel>>()
    private val _observableUserReportReasoning = MutableLiveData<PlayResult<PlayUserReportUiModel.Loaded>>()
    private val _observableUserReportSubmission = MutableLiveData<PlayResult<Event<Unit>>>()

    private val _observableLoggedInInteractionEvent = MutableLiveData<Event<LoginStateEvent>>()
    val observableLoggedInInteractionEvent: LiveData<Event<LoginStateEvent>> = _observableLoggedInInteractionEvent

    val observableAddToCart: LiveData<PlayResult<Pair<Event<CartFeedbackUiModel>, ProductSectionUiModel.Section>>> = _observableAddToCart
    val observableProductVariant: LiveData<PlayResult<VariantSheetUiModel>> = _observableProductVariant
    val observableUserReportReasoning : LiveData<PlayResult<PlayUserReportUiModel.Loaded>> = _observableUserReportReasoning
    val observableUserReportSubmission : LiveData<PlayResult<Event<Unit>>> = _observableUserReportSubmission

    fun getProductVariant(product: PlayProductUiModel.Product, action: ProductAction, sectionInfo: ProductSectionUiModel.Section) {
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
                        listOfVariantCategory = categoryVariants.orEmpty(),
                        section = sectionInfo
                )
            }
            _observableProductVariant.value = PlayResult.Success(variantSheetUiModel)
        }){
            _observableProductVariant.value = PlayResult.Failure(it) {
                getProductVariant(product, action, sectionInfo)
            }
        }
    }

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

    fun getUserReportList(){
        _observableUserReportReasoning.value = PlayResult.Loading(true)

        viewModelScope.launchCatchError(block = {
            val userReportUiModel = withContext(dispatchers.io){
                repo.getReasoningList()
            }
            val data = PlayUserReportUiModel.Loaded(
                reasoningList = userReportUiModel
            )
            _observableUserReportReasoning.value = PlayResult.Success(data = data)

        }){
            _observableUserReportReasoning.value = PlayResult.Failure(it){
                getUserReportList()
            }
        }
    }

    fun submitUserReport(channelId: Long,
               mediaUrl: String,
               shopId: Long,
               reasonId: Int,
               timestamp: Long,
               reportDesc: String){
        viewModelScope.launchCatchError(block = {
           val isSuccess = withContext(dispatchers.io) {
                repo.submitReport(
                    channelId = channelId,
                    mediaUrl = mediaUrl,
                    shopId = shopId,
                    reasonId = reasonId,
                    timestamp = timestamp,
                    reportDesc = reportDesc
                )
           }
            if(isSuccess){
                _observableUserReportSubmission.value = PlayResult.Success(Event(Unit))
            }else{
                throw Throwable()
            }
        }){
            _observableUserReportSubmission.value = PlayResult.Failure(it)
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