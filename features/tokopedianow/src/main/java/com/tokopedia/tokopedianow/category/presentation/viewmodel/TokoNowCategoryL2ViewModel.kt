package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.filter.newdynamicfilter.controller.FilterController
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper.addCategoryMenu
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper.addChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper.addEmptyStateDivider
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper.addEmptyState
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper.addFeedbackWidget
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper.addProductRecommendation
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2Mapper.mapToCategoryUiModel
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.removeItem
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryDetailUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryLayoutUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.model.CategoryEmptyStateModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2ShimmerUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.common.domain.mapper.AceSearchParamMapper
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetProductAdsUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowEmptyStateNoResultUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.repurchase.domain.mapper.RepurchaseLayoutMapper.mapCategoryMenuData
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetFeedbackFieldToggleUseCase
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class TokoNowCategoryL2ViewModel @Inject constructor(
    private val getCategoryLayout: GetCategoryLayoutUseCase,
    private val getCategoryDetailUseCase: GetCategoryDetailUseCase,
    private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getFeedbackToggleUseCase: GetFeedbackFieldToggleUseCase,
    private val addressData: TokoNowLocalAddress,
    getCategoryProductUseCase: GetCategoryProductUseCase,
    getProductAdsUseCase: GetProductAdsUseCase,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    getShopAndWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    aceSearchParamMapper: AceSearchParamMapper,
    userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseCategoryViewModel(
    getCategoryProductUseCase = getCategoryProductUseCase,
    getProductAdsUseCase = getProductAdsUseCase,
    getTargetedTickerUseCase = getTargetedTickerUseCase,
    getShopAndWarehouseUseCase = getShopAndWarehouseUseCase,
    getMiniCartUseCase = getMiniCartUseCase,
    addToCartUseCase = addToCartUseCase,
    updateCartUseCase = updateCartUseCase,
    deleteCartUseCase = deleteCartUseCase,
    affiliateService = affiliateService,
    aceSearchParamMapper = aceSearchParamMapper,
    addressData = addressData,
    userSession = userSession,
    dispatchers = dispatchers
) {

    companion object {
        private const val CATEGORY_LEVEL_DEPTH = 1
    }

    private val _showEmptyState = MutableLiveData<Boolean>()
    private val _categoryTab = MutableLiveData<CategoryL2TabUiModel>()

    val showEmptyState: LiveData<Boolean> = _showEmptyState
    val categoryTab: LiveData<CategoryL2TabUiModel> = _categoryTab

    private var lastVisitableList: List<Visitable<*>> = emptyList()

    init {
        miniCartSource = MiniCartSource.TokonowCategoryPage
    }

    override suspend fun loadFirstPage(tickerList: List<TickerData>) {
        val warehouses = addressData.getWarehousesData()
        val getCategoryLayoutResponse = getCategoryLayout.execute(categoryIdL2)
        val getCategoryDetailResponse = getCategoryDetailUseCase.execute(warehouses, categoryIdL1)

        visitableList.clear()
        visitableList.addChooseAddress()
        visitableList.mapToCategoryUiModel(
            getCategoryLayoutResponse,
            getCategoryDetailResponse
        )

        val categoryTab = CategoryL2Mapper.mapToCategoryTab(
            categoryIdL1 = categoryIdL1,
            categoryIdL2 = categoryIdL2,
            getCategoryLayoutResponse = getCategoryLayoutResponse,
            categoryDetailResponse = getCategoryDetailResponse
        )

        hidePageLoading()
        updateCategoryTab(categoryTab)
        updateVisitableListLiveData()
        trackOpenScreen(getCategoryDetailResponse)
    }

    fun onCartQuantityChanged(
        product: ProductCardCompactUiModel,
        shopId: String,
        quantity: Int
    ) {
        val productId = product.productId
        val isVariant = product.isVariant
        val stock = product.availableStock

        onCartQuantityChanged(
            productId = productId,
            shopId = shopId,
            quantity = quantity,
            stock = stock,
            isVariant = isVariant,
            onSuccessAddToCart = {

            },
            onSuccessUpdateCart = { _, _ ->

            },
            onSuccessDeleteCart = { _, _ ->

            },
            onError = {

            }
        )
    }

    fun getCategoryMenuData() {
        launchCatchError(block = {
            getCategoryMenuDataAsync().await()
        }) {

        }
    }

    fun showEmptyState(model: CategoryEmptyStateModel, filterController: FilterController) {
        launchCatchError(block = {
            lastVisitableList = visitableList.toMutableList()

            visitableList.clear()
            visitableList.addChooseAddress()
            visitableList.addEmptyState(model, filterController)
            visitableList.addCategoryMenu()
            visitableList.addEmptyStateDivider()
            visitableList.addProductRecommendation()

            getCategoryMenuDataAsync().await()
            getFeedbackToggleAsync().await()
            getMiniCartAsync().await()

            updateVisitableListLiveData()
            updateEmptyState(show = true)
        }) {

        }
    }

    fun hideEmptyState() {
        visitableList.clear()
        visitableList.addAll(lastVisitableList)
        updateVisitableListLiveData()
        updateEmptyState(show = false)
    }

    fun showPageLoading() {
        visitableList.clear()
        visitableList.addChooseAddress()
        visitableList.add(CategoryL2ShimmerUiModel)
        updateVisitableListLiveData()
    }

    fun isEmptyState(): Boolean {
        return visitableList.find { it is TokoNowEmptyStateNoResultUiModel } != null
    }

    fun removeProductRecommendationWidget() {
        visitableList.removeItem<TokoNowProductRecommendationUiModel>()
        updateVisitableListLiveData()
    }

    fun removeChooseAddressWidget() {
        visitableList.removeItem<TokoNowChooseAddressWidgetUiModel>()
        updateVisitableListLiveData()
    }

    fun getCategoryIdForTracking() =
        if (categoryIdL2.isNotEmpty()) {
            "$categoryIdL1/$categoryIdL2"
        } else {
            categoryIdL1
        }

    private fun getCategoryMenuDataAsync(): Deferred<Unit?> {
        return asyncCatchError(block = {
            val warehouses = AddressMapper.mapToWarehousesData(addressData.getAddressData())
            val response = getCategoryListUseCase.execute(warehouses, CATEGORY_LEVEL_DEPTH)
            visitableList.mapCategoryMenuData(response.data)
            updateVisitableListLiveData()
        }) {
            visitableList.mapCategoryMenuData(emptyList())
            updateVisitableListLiveData()
        }
    }

    private fun getFeedbackToggleAsync(): Deferred<Unit?> {
        return asyncCatchError(block = {
            val toggle = getFeedbackToggleUseCase.execute()
            if(!toggle.isActive) return@asyncCatchError
            visitableList.addFeedbackWidget()
            updateVisitableListLiveData()
        }) {

        }
    }

    private fun getMiniCartAsync(): Deferred<Unit?> {
        return asyncCatchError(block = {
            getMiniCart()
        }) {

        }
    }

    private fun trackOpenScreen(getCategoryDetailResponse: CategoryDetailResponse) {
        sendOpenScreenTracker(
            id = getCategoryDetailResponse.categoryDetail.data.id,
            name = getCategoryDetailResponse.categoryDetail.data.name,
            url = getCategoryDetailResponse.categoryDetail.data.url
        )
    }

    private fun updateCategoryTab(categoryTab: CategoryL2TabUiModel) {
        _categoryTab.postValue(categoryTab)
    }

    private fun updateEmptyState(show: Boolean) {
        _showEmptyState.postValue(show)
    }
}
