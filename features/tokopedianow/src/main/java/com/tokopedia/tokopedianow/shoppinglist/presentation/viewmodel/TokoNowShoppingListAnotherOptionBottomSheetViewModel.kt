package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_SOURCE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.helper.ResourceProvider
import com.tokopedia.tokopedianow.common.model.UiState
import com.tokopedia.tokopedianow.common.model.UiState.Success
import com.tokopedia.tokopedianow.common.model.UiState.Loading
import com.tokopedia.tokopedianow.common.model.UiState.Error
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.AnotherOptionBottomSheetVisitableExtension.addEmptyState
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.AnotherOptionBottomSheetVisitableExtension.addLoadingState
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.AnotherOptionBottomSheetVisitableExtension.switchToProductRecommendationAdded
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.addErrorState
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.CommonVisitableExtension.modifyProduct
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.ProductRecommendationMapper.mapRecommendedProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addProduct
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.addProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.doIf
import com.tokopedia.tokopedianow.shoppinglist.domain.extension.MainVisitableExtension.resetIndices
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class TokoNowShoppingListAnotherOptionBottomSheetViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val addToWishlistUseCase: AddToWishlistV2UseCase,
    private val productRecommendationUseCase: GetSingleRecommendationUseCase,
    private val resourceProvider: ResourceProvider,
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.io)  {
    private companion object {
        const val RECOMMENDATION_PAGE_NAME = "tokonow_similar"
    }

    private val mutableLayout: MutableList<Visitable<*>> = mutableListOf()

    private val _layoutState: MutableStateFlow<UiState<List<Visitable<*>>>> = MutableStateFlow(Loading(data = mutableLayout.addLoadingState()))
    private val _toasterData: MutableStateFlow<ToasterModel?> = MutableStateFlow(null)

    private val recommendedProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()
    private val filteredRecommendedProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()

    val availableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()

    val layoutState
        get() = _layoutState.asStateFlow()
    val toasterData
        get() = _toasterData.asStateFlow()

    private fun showProductShimmering(
        productId: String
    ) {
        mutableLayout
            .modifyProduct(
                productId = productId,
                state = TokoNowLayoutState.LOADING
            )

        _layoutState.value = Success(getUpdatedLayout())
    }

    private fun filterProductRecommendationWithAvailableProduct() {
        val newRecommendedProducts = recommendedProducts.switchToProductRecommendationAdded(
            availableProducts = availableProducts
        )

        filteredRecommendedProducts.clear()
        filteredRecommendedProducts.addAll(newRecommendedProducts.resetIndices())
    }

    private fun getUpdatedLayout(): List<Visitable<*>> = mutableLayout.toList()

    private fun onSuccessAddingWishlist(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        availableProducts
            .addProduct(
                product.copy(
                    productLayoutType = AVAILABLE_SHOPPING_LIST,
                    isSelected = true,
                    state = TokoNowLayoutState.SHOW
                )
            )

        filterProductRecommendationWithAvailableProduct()

        mutableLayout.clear()
        mutableLayout.addProducts(filteredRecommendedProducts)

        _layoutState.value = Success(getUpdatedLayout())

        _toasterData.value = ToasterModel(
            text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_to_add_product_to_shopping_list),
            actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_success_for_cta),
            type = Toaster.TYPE_NORMAL,
            event = ToasterModel.Event.ADD_WISHLIST,
            any = product
        )
    }

    private fun onErrorAddingWishlist(
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        mutableLayout
            .modifyProduct(
                productId = product.id,
                state = TokoNowLayoutState.SHOW
            )

        _layoutState.value = Success(getUpdatedLayout())

        _toasterData.value = ToasterModel(
            text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_to_add_product_to_shopping_list),
            actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
            type = Toaster.TYPE_ERROR,
            event = ToasterModel.Event.ADD_WISHLIST,
            any = product
        )
    }

    fun loadLayout(
        productId: String
    ) {
        launchCatchError(
            block = {
                val param = GetRecommendationRequestParam(
                    userId = userSession.userId.toIntSafely(),
                    productIds = listOf(productId),
                    pageName = RECOMMENDATION_PAGE_NAME,
                    xDevice = X_DEVICE_RECOMMENDATION_PARAM,
                    xSource = X_SOURCE_RECOMMENDATION_PARAM,
                    isTokonow = true
                )
                val productRecommendation = productRecommendationUseCase.getData(param)

                mutableLayout
                    .doIf(
                        predicate = productRecommendation.recommendationItemList.isNotEmpty(),
                        then = {
                            recommendedProducts.clear()
                            recommendedProducts.addAll(mapRecommendedProducts(productRecommendation))

                            filterProductRecommendationWithAvailableProduct()

                            clear()
                            addProducts(filteredRecommendedProducts)
                        },
                        ifNot = {
                            clear()
                            addEmptyState()
                        }
                    )

                _layoutState.value = Success(getUpdatedLayout())
            },
            onError = { throwable ->
                mutableLayout.clear()

                mutableLayout
                    .addErrorState(
                        isFullPage = false,
                        throwable = throwable
                    )

                _layoutState.value = Error(
                    data = getUpdatedLayout(),
                    throwable = throwable
                )
            }
        )
    }

    fun loadLoadingState() {
        mutableLayout.clear()

        mutableLayout.addLoadingState()

        _layoutState.value = Loading(getUpdatedLayout())
    }

    fun addToWishlist(
        onTrackAddToWishlist: () -> Unit,
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        launchCatchError(
            block = {
                onTrackAddToWishlist.invoke()

                _toasterData.value = null

                showProductShimmering(product.id)

                addToWishlistUseCase.setParams(
                    productId = product.id,
                    userId = userSession.userId
                )
                val response = addToWishlistUseCase.executeOnBackground()

                if (response is com.tokopedia.usecase.coroutines.Success && response.data.success) {
                    onSuccessAddingWishlist(product)
                } else {
                    onErrorAddingWishlist(product)
                }
            },
            onError = {
                onErrorAddingWishlist(product)
            }
        )
    }
}
