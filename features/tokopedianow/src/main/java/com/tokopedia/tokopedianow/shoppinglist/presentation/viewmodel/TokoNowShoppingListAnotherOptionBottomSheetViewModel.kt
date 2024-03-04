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
import com.tokopedia.tokopedianow.common.model.UiState
import com.tokopedia.tokopedianow.common.model.UiState.Success
import com.tokopedia.tokopedianow.common.model.UiState.Loading
import com.tokopedia.tokopedianow.common.model.UiState.Error
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.AnotherOptionBottomSheetVisitableMapper.addEmptyState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.AnotherOptionBottomSheetVisitableMapper.addLoadingState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.CommonVisitableMapper.addErrorState
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.CommonVisitableMapper.mapRecommendedProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.CommonVisitableMapper.modifyProduct
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addProduct
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.doIf
import com.tokopedia.tokopedianow.shoppinglist.helper.ResourceProvider
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.ToasterModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.uimodel.common.ShoppingListHorizontalProductCardItemUiModel
import com.tokopedia.tokopedianow.shoppinglist.util.ShoppingListProductLayoutType
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
        const val RECOMMENDATION_PAGE_NAME = "tokonow_shopping_list"
    }

    private val mutableLayout: MutableList<Visitable<*>> = mutableListOf()

    private val _layoutState: MutableStateFlow<UiState<List<Visitable<*>>>> = MutableStateFlow(Loading(data = mutableLayout.addLoadingState()))
    private val _toasterData: MutableStateFlow<ToasterModel?> = MutableStateFlow(null)

    private val recommendedProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()
    private val filteredRecommendedProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()

    val availableProducts: MutableList<ShoppingListHorizontalProductCardItemUiModel> = mutableListOf()

    val layoutState
        get() = _layoutState.asStateFlow()

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
        product: ShoppingListHorizontalProductCardItemUiModel
    ) {
        launchCatchError(
            block = {
                _toasterData.value = null

                mutableLayout
                    .modifyProduct(
                        productId = product.id,
                        state = TokoNowLayoutState.LOADING
                    )

                _layoutState.value = Success(getUpdatedLayout())

                addToWishlistUseCase.setParams(
                    productId = product.id,
                    userId = userSession.userId
                )
                addToWishlistUseCase.executeOnBackground()

                availableProducts
                    .addProduct(
                        product.copy(
                            productLayoutType = ShoppingListProductLayoutType.AVAILABLE_SHOPPING_LIST,
                            isSelected = true,
                            state = TokoNowLayoutState.SHOW
                        )
                    )

                filterProductRecommendationWithAvailableProduct()

                mutableLayout.clear()

                mutableLayout.addProducts(filteredRecommendedProducts)

                _layoutState.value = Success(getUpdatedLayout())
            },
            onError = {
                mutableLayout
                    .modifyProduct(
                        productId = product.id,
                        state = TokoNowLayoutState.SHOW
                    )

                _layoutState.value = Success(getUpdatedLayout())

                _toasterData.value = ToasterModel(
                    text = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_failed_to_add_product_from_shopping_list),
                    actionText = resourceProvider.getString(R.string.tokopedianow_shopping_list_toaster_text_error_for_cta),
                    type = Toaster.TYPE_ERROR,
                    product = product,
                    event = ToasterModel.Event.ADD_WISHLIST
                )
            }
        )
    }

    private fun filterProductRecommendationWithAvailableProduct() {
        val newRecommendedProducts = recommendedProducts.toMutableList()
        newRecommendedProducts.filteredBy(
            productList = availableProducts
        )

        filteredRecommendedProducts.clear()
        filteredRecommendedProducts.addAll(newRecommendedProducts)
    }

    fun MutableList<ShoppingListHorizontalProductCardItemUiModel>.filteredBy(
        productList: MutableList<ShoppingListHorizontalProductCardItemUiModel>
    ): MutableList<ShoppingListHorizontalProductCardItemUiModel> {
        for (availableProduct in availableProducts) {
            for (recommendedProduct in recommendedProducts) {
                if (availableProduct.id == recommendedProduct.id) {
                    modifyProduct(recommendedProduct.id, productLayoutType = ShoppingListProductLayoutType.PRODUCT_RECOMMENDATION_ADDED)
                }
            }
        }
        return this
    }

    private fun getUpdatedLayout(): List<Visitable<*>> = mutableLayout.toList()
}
