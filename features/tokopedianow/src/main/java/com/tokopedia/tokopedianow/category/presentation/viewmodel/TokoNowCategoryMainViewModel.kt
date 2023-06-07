package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.category.di.module.CategoryParamModule.Companion.NOW_CATEGORY_L1
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryRecommendationMapper.mapToCategoryRecommendation
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryMenu
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryShowcase
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addProductRecommendation
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addProgressBar
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addTicker
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.mapCategoryShowcase
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.removeItem
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.updateProductQuantity
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.updateWishlistStatus
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryDetailUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2Model
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase.Companion.CATEGORY_PAGE
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class TokoNowCategoryMainViewModel @Inject constructor(
    private val getCategoryDetailUseCase: GetCategoryDetailUseCase,
    private val getCategoryProductUseCase: GetCategoryProductUseCase,
    @Named(NOW_CATEGORY_L1)
    val categoryIdL1: String,
    addressData: TokoNowLocalAddress,
    userSession: UserSessionInterface,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    dispatchers: CoroutineDispatchers
): TokoNowCategoryBaseViewModel(
    userSession = userSession,
    getMiniCartUseCase = getMiniCartUseCase,
    addToCartUseCase = addToCartUseCase,
    updateCartUseCase = updateCartUseCase,
    deleteCartUseCase = deleteCartUseCase,
    affiliateService = affiliateService,
    getTargetedTickerUseCase = getTargetedTickerUseCase,
    addressData = addressData,
    dispatchers = dispatchers
) {
    companion object {
        const val BATCH_SHOWCASE_TOTAL = 3
        const val NO_WAREHOUSE_ID = "0"
    }

    private val layout: MutableList<Visitable<*>> = mutableListOf()
    private val categoryL2Models: MutableList<CategoryL2Model> = mutableListOf()
    private var categoryRecommendation: TokoNowCategoryMenuUiModel? = null
    private var moreShowcaseJob: Job? = null

    private val _categoryHeader = MutableLiveData<Result<List<Visitable<*>>>>()
    private val _categoryPage = MutableLiveData<List<Visitable<*>>>()
    private val _scrollNotNeeded = MutableLiveData<Unit>()
    private val _refreshState = MutableLiveData<Unit>()
    private val _oosState = MutableLiveData<Unit>()

    val categoryHeader: LiveData<Result<List<Visitable<*>>>> = _categoryHeader
    val categoryPage: LiveData<List<Visitable<*>>> = _categoryPage
    val scrollNotNeeded: LiveData<Unit> = _scrollNotNeeded
    val refreshState: LiveData<Unit> = _refreshState
    val oosState: LiveData<Unit> = _oosState

    override fun updateProductCartQuantity(
        productId: String,
        quantity: Int,
        layoutType: CategoryLayoutType
    ) {
        layout.updateProductQuantity(
            productId = productId,
            quantity = quantity,
            layoutType = layoutType
        )
        _categoryPage.postValue(layout)
    }

    override fun onSuccessGetMiniCartData(
        miniCartData: MiniCartSimplifiedData
    ) {
        super.onSuccessGetMiniCartData(miniCartData)
        layout.updateProductQuantity(
            miniCartData = miniCartData,
            layoutType = CategoryLayoutType.CATEGORY_SHOWCASE
        )
    }

    private suspend fun getCategoryShowcaseAsync(
        categoryL2Model: CategoryL2Model,
        hasAdded: Boolean
    ): Deferred<Unit?> = asyncCatchError(block = {
        val categoryPage = getCategoryProductUseCase.execute(
            chooseAddressData = getAddressData(),
            categoryIdL2 = categoryL2Model.id,
            uniqueId = getUniqueId()
        )

        categoryL2Models.remove(categoryL2Model)

        if (hasAdded) {
            layout.mapCategoryShowcase(
                model = categoryPage,
                categoryIdL2 = categoryL2Model.id,
                title = categoryL2Model.title,
                seeAllAppLink = categoryL2Model.appLink,
                miniCartData = miniCartData,
                hasBlockedAddToCart = hasBlockedAddToCart
            )
        } else {
            layout.addCategoryShowcase(
                model = categoryPage,
                categoryIdL2 = categoryL2Model.id,
                title = categoryL2Model.title,
                state = TokoNowLayoutState.SHOW,
                seeAllAppLink = categoryL2Model.appLink,
                miniCartData = miniCartData,
                hasBlockedAddToCart = hasBlockedAddToCart
            )
        }
    }) {
        categoryL2Models.remove(categoryL2Model)

        layout.removeItem(
            id = categoryL2Model.id
        )
    }

    private suspend fun getBatchShowcase(
        hasAdded: Boolean
    ) {
        layout.addProgressBar()
        _categoryPage.postValue(layout)

        categoryL2Models.take(BATCH_SHOWCASE_TOTAL).map { categoryL2Model ->
            getCategoryShowcaseAsync(
                categoryL2Model = categoryL2Model,
                hasAdded = hasAdded
            ).await()
        }

        layout.removeItem(CategoryLayoutType.MORE_PROGRESS_BAR.name)
        _categoryPage.postValue(layout)
    }

    private fun addCategoryShowcases(
        categoryNavigationUiModel: CategoryNavigationUiModel
    ) {
        categoryL2Models.clear()

        categoryL2Models.addAll(
            categoryNavigationUiModel.categoryListUiModel.map {
                CategoryL2Model(
                    id = it.id,
                    title = it.title,
                    appLink = it.appLink
                )
            }
        )

        categoryL2Models.take(BATCH_SHOWCASE_TOTAL).forEach { categoryL2Model ->
            layout.addCategoryShowcase(
                categoryIdL2 = categoryL2Model.id,
                state = TokoNowLayoutState.LOADING,
                miniCartData = miniCartData,
                hasBlockedAddToCart = hasBlockedAddToCart
            )
        }
    }

    private fun getMoreShowcases() {
        moreShowcaseJob = launch {
            getBatchShowcase(
                hasAdded = false
            )
        }
    }

    fun getCategoryHeader(
        navToolbarHeight: Int
    ) {
        launchCatchError(
            block = {
                if (getWarehouseId() == NO_WAREHOUSE_ID) {
                    _oosState.postValue(Unit)
                    return@launchCatchError
                }

                val detailResponse = getCategoryDetailUseCase.execute(
                    warehouseId = getWarehouseId(),
                    categoryIdL1 = categoryIdL1
                )

                val tickerData = getTickerDataAsync(
                    warehouseId = getWarehouseId(),
                    page = CATEGORY_PAGE
                ).await()

                val categoryNavigationUiModel = detailResponse.mapToCategoryNavigation()
                categoryRecommendation = detailResponse.mapToCategoryRecommendation()

                layout.clear()

                layout.addHeaderSpace(
                    space = navToolbarHeight,
                    detailResponse = detailResponse
                )
                layout.addChooseAddress(
                    detailResponse = detailResponse
                )
                hasBlockedAddToCart = layout.addTicker(
                    detailResponse = detailResponse,
                    tickerData = tickerData
                )
                layout.addCategoryTitle(
                    detailResponse = detailResponse
                )
                layout.addCategoryNavigation(
                    categoryNavigationUiModel = categoryNavigationUiModel
                )
                layout.addProductRecommendation(
                    categoryId = listOf(categoryIdL1)
                )

                addCategoryShowcases(
                    categoryNavigationUiModel = categoryNavigationUiModel
                )

                _categoryHeader.postValue(Success(layout))

                sendOpenScreenTracker(detailResponse)
            },
            onError = {
                _categoryHeader.postValue(Fail(it))
            }
        )
    }

    fun getFirstPage() {
        launch {
            getBatchShowcase(
                hasAdded = true
            )
        }
    }

    fun loadMore(
        isAtTheBottomOfThePage: Boolean
    ) {
        if (isAtTheBottomOfThePage && categoryL2Models.isEmpty()) {
            _scrollNotNeeded.value = Unit

            categoryRecommendation?.let { categoryMenu ->
                layout.addCategoryMenu(categoryMenu)
                categoryRecommendation = null

                _categoryPage.value = layout
            }
        } else if (isAtTheBottomOfThePage && (moreShowcaseJob == null || moreShowcaseJob?.isCompleted == true)) {
            getMoreShowcases()
        }
    }

    fun refreshLayout() {
        getMiniCart()
        updateAddressData()
        moreShowcaseJob = null
        _refreshState.value = Unit
    }

    fun removeProductRecommendation() {
        launch {
            layout.removeItem(CategoryLayoutType.PRODUCT_RECOMMENDATION.name)
            _categoryPage.postValue(layout)
        }
    }

    fun updateWishlistStatus(
        productId: String,
        hasBeenWishlist: Boolean
    ) {
        launch {
            layout.updateWishlistStatus(
                productId = productId,
                hasBeenWishlist = hasBeenWishlist
            )
            _categoryPage.postValue(layout)
        }
    }
}
