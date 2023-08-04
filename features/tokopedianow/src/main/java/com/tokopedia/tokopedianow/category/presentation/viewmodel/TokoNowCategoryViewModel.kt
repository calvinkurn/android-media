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
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.category.di.module.CategoryParamModule.Companion.NOW_CATEGORY_L1
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryNavigationMapper.mapToCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryRecommendationMapper.mapToCategoryRecommendation
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.DEFAULT_PRODUCT_QUANTITY
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryMenu
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryNavigation
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryShowcase
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addCategoryTitle
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addChooseAddress
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addHeaderSpace
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addProductRecommendation
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addProgressBar
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.addTicker
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.findCategoryShowcaseItem
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.mapCategoryShowcase
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.mapProductAdsCarousel
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.removeItem
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.updateProductQuantity
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.updateWishlistStatus
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryDetailUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2Model
import com.tokopedia.tokopedianow.category.presentation.model.CategoryOpenScreenTrackerModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType.CATEGORY_SHOWCASE
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper.addProductAdsCarousel
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper.findAdsProductCarousel
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam.Companion.SRC_DIRECTORY_TOKONOW
import com.tokopedia.tokopedianow.common.domain.usecase.GetProductAdsUseCase
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
import javax.inject.Inject
import javax.inject.Named

class TokoNowCategoryViewModel @Inject constructor(
    private val getCategoryDetailUseCase: GetCategoryDetailUseCase,
    private val getCategoryProductUseCase: GetCategoryProductUseCase,
    private val getProductAdsUseCase: GetProductAdsUseCase,
    private val addressData: TokoNowLocalAddress,
    @Named(NOW_CATEGORY_L1)
    val categoryIdL1: String,
    userSession: UserSessionInterface,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    dispatchers: CoroutineDispatchers
) : BaseTokoNowViewModel(
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

    init {
        miniCartSource = MiniCartSource.TokonowCategoryPage
    }

    /**
     * -- private immutable variable section --
     */

    private val layout: MutableList<Visitable<*>> = mutableListOf()
    private val categoryL2Models: MutableList<CategoryL2Model> = mutableListOf()

    private val _updateToolbarNotification: MutableLiveData<Boolean> = MutableLiveData()
    private val _openScreenTracker: MutableLiveData<CategoryOpenScreenTrackerModel> = MutableLiveData()
    private val _atcDataTracker: MutableLiveData<CategoryAtcTrackerModel> = MutableLiveData()
    private val _categoryFirstPage = MutableLiveData<Result<List<Visitable<*>>>>()
    private val _categoryPage = MutableLiveData<List<Visitable<*>>>()
    private val _scrollNotNeeded = MutableLiveData<Unit>()
    private val _refreshState = MutableLiveData<Unit>()
    private val _oosState = MutableLiveData<Unit>()

    /**
     * -- private mutable variable section --
     */

    private var categoryRecommendation: TokoNowCategoryMenuUiModel? = null
    private var moreShowcaseJob: Job? = null

    /**
     * -- public immutable variable section --
     */

    val updateToolbarNotification: LiveData<Boolean> = _updateToolbarNotification
    val openScreenTracker: LiveData<CategoryOpenScreenTrackerModel> = _openScreenTracker
    val atcDataTracker: LiveData<CategoryAtcTrackerModel> = _atcDataTracker
    val categoryFirstPage: LiveData<Result<List<Visitable<*>>>> = _categoryFirstPage
    val categoryPage: LiveData<List<Visitable<*>>> = _categoryPage
    val scrollNotNeeded: LiveData<Unit> = _scrollNotNeeded
    val refreshState: LiveData<Unit> = _refreshState
    val oosState: LiveData<Unit> = _oosState

    /**
     * -- override function section --
     */

    override fun onSuccessGetMiniCartData(miniCartData: MiniCartSimplifiedData) {
        super.onSuccessGetMiniCartData(miniCartData)
        layout.updateProductQuantity(miniCartData)
        _categoryPage.postValue(layout)
    }

    /**
     * -- private suspend function section --
     */

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

        val productList = categoryPage.searchProduct.data.productList.filter { !it.isOos() }
        if (productList.isEmpty()) {
            layout.removeItem(
                id = categoryL2Model.id
            )
            return@asyncCatchError
        }

        if (hasAdded) {
            layout.mapCategoryShowcase(
                totalData = categoryPage.searchProduct.header.totalData,
                productList = productList,
                categoryIdL2 = categoryL2Model.id,
                title = categoryL2Model.title,
                seeAllAppLink = categoryL2Model.appLink,
                miniCartData = miniCartData,
                hasBlockedAddToCart = hasBlockedAddToCart
            )
        } else {
            layout.addCategoryShowcase(
                totalData = categoryPage.searchProduct.header.totalData,
                productList = productList,
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

        addCategoryRecommendation()

        _categoryPage.postValue(layout)
    }

    private fun getProductAds() {
        launchCatchError(block = {
            val params = GetProductAdsParam(
                categoryId = categoryIdL1,
                src = SRC_DIRECTORY_TOKONOW,
                userId = getUserId(),
                addressData = getAddressData()
            )

            val response = getProductAdsUseCase.execute(params)

            if (response.productList.isNotEmpty()) {
                layout.mapProductAdsCarousel(response, miniCartData, hasBlockedAddToCart)
            } else {
                layout.removeItem(PRODUCT_ADS_CAROUSEL)
            }

            _categoryPage.postValue(layout)
        }) {
            layout.removeItem(PRODUCT_ADS_CAROUSEL)
            _categoryPage.postValue(layout)
        }
    }

    /**
     * -- private function section --
     */

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

    private fun updateToolbarNotification() {
        _updateToolbarNotification.postValue(true)
    }

    private fun getMoreShowcases() {
        moreShowcaseJob = launchCatchError(
            block = {
                getBatchShowcase(
                    hasAdded = false
                )
            },
            onError = { /* nothing to do */ }
        )
    }

    private fun addCategoryRecommendation() {
        if (categoryL2Models.isEmpty()) {
            _scrollNotNeeded.postValue(Unit)

            categoryRecommendation?.let { categoryMenu ->
                layout.addCategoryMenu(categoryMenu)
                categoryRecommendation = null
            }
        }
    }

    private fun sendOpenScreenTracker(detailResponse: CategoryDetailResponse) {
        _openScreenTracker.postValue(
            CategoryOpenScreenTrackerModel(
                id = detailResponse.categoryDetail.data.id,
                name = detailResponse.categoryDetail.data.name,
                url = detailResponse.categoryDetail.data.url
            )
        )
    }

    private fun getUniqueId() = if (isLoggedIn()) AuthHelper.getMD5Hash(getUserId()) else AuthHelper.getMD5Hash(getDeviceId())

    private fun updateProductCartQuantity(
        productId: String,
        quantity: Int,
        layoutType: String
    ) {
        layout.updateProductQuantity(
            productId = productId,
            quantity = quantity,
            layoutType = layoutType
        )
        _categoryPage.postValue(layout)
    }

    /**
     * -- public function section --
     */

    fun onViewCreated(
        navToolbarHeight: Int
    ) {
        initAffiliateCookie()
        loadCategoryPage(navToolbarHeight)
    }

    fun loadCategoryPage(
        navToolbarHeight: Int
    ) {
        launchCatchError(
            block = {
                if (getWarehouseId() == NO_WAREHOUSE_ID) {
                    _oosState.postValue(Unit)
                    return@launchCatchError
                }

                val detailResponse = getCategoryDetailUseCase.execute(
                    warehouses = addressData.getWarehousesData(),
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
                layout.addProductAdsCarousel()

                addCategoryShowcases(
                    categoryNavigationUiModel = categoryNavigationUiModel
                )

                _categoryFirstPage.postValue(Success(layout))

                sendOpenScreenTracker(detailResponse)
            },
            onError = {
                _categoryFirstPage.postValue(Fail(it))
            }
        )
    }

    fun getFirstPage() {
        launchCatchError(
            block = {
                getBatchShowcase(
                    hasAdded = true
                )
                getProductAds()
            },
            onError = { /* nothing to do */ }
        )
    }

    fun loadMore(
        isAtTheBottomOfThePage: Boolean
    ) {
        if (isAtTheBottomOfThePage && (moreShowcaseJob == null || moreShowcaseJob?.isCompleted == true)) {
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
        launchCatchError(
            block = {
                layout.removeItem(CategoryLayoutType.PRODUCT_RECOMMENDATION.name)
                _categoryPage.postValue(layout)
            },
            onError = { /* nothing to do */ }
        )
    }

    fun updateWishlistStatus(
        productId: String,
        hasBeenWishlist: Boolean
    ) {
        launchCatchError(
            block = {
                layout.updateWishlistStatus(
                    productId = productId,
                    hasBeenWishlist = hasBeenWishlist
                )
                _categoryPage.postValue(layout)
            },
            onError = { /* nothing to do */ }
        )
    }

    fun onCartQuantityChanged(
        product: ProductCardCompactUiModel,
        shopId: String,
        quantity: Int,
        layoutType: String
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
                updateProductCartQuantity(productId, quantity, layoutType)
                trackAddToCart(product, quantity, layoutType)
                updateToolbarNotification()
            },
            onSuccessUpdateCart = { _, _ ->
                updateProductCartQuantity(productId, quantity, layoutType)
                updateToolbarNotification()
            },
            onSuccessDeleteCart = { _, _ ->
                updateProductCartQuantity(productId, DEFAULT_PRODUCT_QUANTITY, layoutType)
                updateToolbarNotification()
            },
            onError = {
                updateProductCartQuantity(productId, quantity, layoutType)
            }
        )
    }

    private fun trackAddToCart(
        product: ProductCardCompactUiModel,
        quantity: Int,
        layoutType: String
    ) {

        when (layoutType) {
            CATEGORY_SHOWCASE.name -> trackCategoryShowCase(product, quantity)
            PRODUCT_ADS_CAROUSEL -> trackProductAdsAddToCart(product, quantity)
        }
    }

    private fun trackCategoryShowCase(product: ProductCardCompactUiModel, quantity: Int) {
        layout.findCategoryShowcaseItem(product.productId)?.let { item ->
            _atcDataTracker.postValue(CategoryAtcTrackerModel(
                categoryIdL1 = categoryIdL1,
                index = item.index,
                headerName = item.headerName,
                quantity = quantity,
                product = product,
                layoutType = CATEGORY_SHOWCASE.name
            ))
        }
    }

    private fun trackProductAdsAddToCart(product: ProductCardCompactUiModel, quantity: Int) {
        layout.findAdsProductCarousel(product.productId)?.let { item ->
            _atcDataTracker.postValue(CategoryAtcTrackerModel(
                index = item.position,
                quantity = quantity,
                shopId = item.shopId,
                shopName = item.shopName,
                shopType = item.shopType,
                categoryBreadcrumbs = item.categoryBreadcrumbs,
                product = item.productCardModel,
                layoutType = PRODUCT_ADS_CAROUSEL
            ))
        }
    }
}
