package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
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
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.updateProductQuantity
import com.tokopedia.tokopedianow.category.domain.mapper.VisitableMapper.updateWishlistStatus
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryDetailUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2Model
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType.CATEGORY_SHOWCASE
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType.MORE_PROGRESS_BAR
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.domain.mapper.AceSearchParamMapper
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper.addProductAdsCarousel
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper.findAdsProductCarousel
import com.tokopedia.tokopedianow.common.domain.usecase.GetProductAdsUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.searchcategory.domain.model.AceSearchProductModel
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class TokoNowCategoryViewModel @Inject constructor(
    private val getCategoryDetailUseCase: GetCategoryDetailUseCase,
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
        const val BATCH_SHOWCASE_TOTAL = 3
        const val NO_WAREHOUSE_ID = "0"
    }

    init {
        miniCartSource = MiniCartSource.TokonowCategoryPage
    }

    /**
     * -- private immutable variable section --
     */

    private val categoryL2Models: MutableList<CategoryL2Model> = mutableListOf()
    private val _atcDataTracker: MutableLiveData<CategoryAtcTrackerModel> = MutableLiveData()
    private val _categoryFirstPage = MutableLiveData<Result<List<Visitable<*>>>>()
    private val _scrollNotNeeded = MutableLiveData<Unit>()

    /**
     * -- private mutable variable section --
     */

    private var categoryRecommendation: TokoNowCategoryMenuUiModel? = null

    /**
     * -- public immutable variable section --
     */

    val atcDataTracker: LiveData<CategoryAtcTrackerModel> = _atcDataTracker
    val categoryFirstPage: LiveData<Result<List<Visitable<*>>>> = _categoryFirstPage
    val scrollNotNeeded: LiveData<Unit> = _scrollNotNeeded

    /**
     * -- override function section --
     */

    override suspend fun loadFirstPage(tickerList: List<TickerData>) {
        val warehouses = addressData.getWarehousesData()
        val detailResponse = getCategoryDetailUseCase.execute(
            categoryIdL1 = categoryIdL1,
            warehouses = warehouses
        )

        val categoryNavigationUiModel = detailResponse.mapToCategoryNavigation()
        categoryRecommendation = detailResponse.mapToCategoryRecommendation()

        visitableList.clear()

        visitableList.addHeaderSpace(
            space = navToolbarHeight,
            detailResponse = detailResponse
        )
        visitableList.addChooseAddress(
            detailResponse = detailResponse
        )
        visitableList.addTicker(
            detailResponse = detailResponse,
            tickerList = tickerList
        )
        visitableList.addCategoryTitle(
            detailResponse = detailResponse
        )
        visitableList.addCategoryNavigation(
            categoryNavigationUiModel = categoryNavigationUiModel
        )
        visitableList.addProductRecommendation(
            categoryId = listOf(categoryIdL1)
        )
        visitableList.addProductAdsCarousel()

        addCategoryShowcases(
            categoryNavigationUiModel = categoryNavigationUiModel
        )

        updateVisitableListLiveData()
        hidePageLoading()
        getFirstPage()

        sendOpenScreenL1Tracker(detailResponse)
    }

    override suspend fun loadNextPage() {
        getBatchShowcase()
    }

    override fun onSuccessGetMiniCartData(miniCartData: MiniCartSimplifiedData) {
        super.onSuccessGetMiniCartData(miniCartData)
        visitableList.updateProductQuantity(miniCartData)
        updateVisitableListLiveData()
    }

    override fun onSuccessGetCategoryProduct(
        response: AceSearchProductModel,
        categoryL2Model: CategoryL2Model
    ) {
        categoryL2Models.remove(categoryL2Model)

        val searchProduct = response.searchProduct
        val header = searchProduct.header
        val data = searchProduct.data
        val productList = data.productList.filter { !it.isOos() }

        if (productList.isEmpty()) {
            removeVisitableItem(categoryL2Model.id)
            return
        }

        visitableList.mapCategoryShowcase(
            totalData = header.totalData,
            productList = productList,
            categoryIdL2 = categoryL2Model.id,
            title = categoryL2Model.title,
            seeAllAppLink = categoryL2Model.appLink,
            miniCartData = miniCartData,
            hasBlockedAddToCart = hasBlockedAddToCart
        )
    }

    override fun onErrorGetCategoryProduct(
        error: Throwable,
        categoryL2Model: CategoryL2Model
    ) {
        categoryL2Models.remove(categoryL2Model)
        removeVisitableItem(categoryL2Model.id)
    }

    /**
     * -- private suspend function section --
     */

    private suspend fun getBatchShowcase() {
        visitableList.addProgressBar()
        updateVisitableListLiveData()

        categoryL2Models.take(BATCH_SHOWCASE_TOTAL).map { categoryL2Model ->
            getCategoryProductAsync(categoryL2Model = categoryL2Model).await()
        }

        removeVisitableItem(MORE_PROGRESS_BAR.name)
        addCategoryRecommendation()
        updateVisitableListLiveData()
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
            visitableList.addCategoryShowcase(
                categoryIdL2 = categoryL2Model.id,
                state = TokoNowLayoutState.LOADING,
                miniCartData = miniCartData,
                hasBlockedAddToCart = hasBlockedAddToCart
            )
        }
    }

    private fun addCategoryRecommendation() {
        if (categoryL2Models.isEmpty()) {
            _scrollNotNeeded.postValue(Unit)

            categoryRecommendation?.let { categoryMenu ->
                visitableList.addCategoryMenu(categoryMenu)
                categoryRecommendation = null
            }
        }
    }

    private fun updateProductCartQuantity(
        productId: String,
        quantity: Int,
        layoutType: String
    ) {
        visitableList.updateProductQuantity(
            productId = productId,
            quantity = quantity,
            layoutType = layoutType
        )
        updateVisitableListLiveData()
    }

    /**
     * -- public function section --
     */

    fun getFirstPage() {
        launchCatchError(
            block = {
                getBatchShowcase()
                getProductAds(categoryIdL1)
            },
            onError = { /* nothing to do */ }
        )
    }

    fun removeProductRecommendation() {
        launchCatchError(
            block = {
                removeVisitableItem(PRODUCT_RECOMMENDATION.name)
                updateVisitableListLiveData()
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
                visitableList.updateWishlistStatus(
                    productId = productId,
                    hasBeenWishlist = hasBeenWishlist
                )
                updateVisitableListLiveData()
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
            },
            onSuccessUpdateCart = { _, _ ->
                updateProductCartQuantity(productId, quantity, layoutType)
            },
            onSuccessDeleteCart = { _, _ ->
                updateProductCartQuantity(productId, DEFAULT_PRODUCT_QUANTITY, layoutType)
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
        visitableList.findCategoryShowcaseItem(product.productId)?.let { item ->
            val trackerModel = CategoryAtcTrackerModel(
                categoryIdL1 = categoryIdL1,
                index = item.index,
                warehouseId = getWarehouseId(),
                headerName = item.headerName,
                quantity = quantity,
                product = product,
                layoutType = CATEGORY_SHOWCASE.name
            )
            _atcDataTracker.postValue(trackerModel)
        }
    }

    private fun trackProductAdsAddToCart(product: ProductCardCompactUiModel, quantity: Int) {
        visitableList.findAdsProductCarousel(product.productId)?.let { item ->
            val trackerModel = CategoryAtcTrackerModel(
                index = item.position,
                quantity = quantity,
                shopId = item.shopId,
                shopName = item.shopName,
                shopType = item.shopType,
                categoryBreadcrumbs = item.categoryBreadcrumbs,
                product = item.productCardModel,
                layoutType = PRODUCT_ADS_CAROUSEL
            )
            _atcDataTracker.postValue(trackerModel)
        }
    }

    private fun sendOpenScreenL1Tracker(detailResponse: CategoryDetailResponse) {
        sendOpenScreenTracker(
            id = detailResponse.categoryDetail.data.id,
            name = detailResponse.categoryDetail.data.name,
            url = detailResponse.categoryDetail.data.url
        )
    }
}
