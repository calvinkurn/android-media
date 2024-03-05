package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.tokopedianow.annotation.domain.mapper.BrandWidgetMapper.addBrandWidget
import com.tokopedia.tokopedianow.annotation.domain.mapper.BrandWidgetMapper.mapBrandWidget
import com.tokopedia.tokopedianow.annotation.domain.mapper.BrandWidgetMapper.mapBrandWidgetError
import com.tokopedia.tokopedianow.annotation.domain.mapper.BrandWidgetMapper.mapBrandWidgetLoading
import com.tokopedia.tokopedianow.annotation.domain.mapper.BrandWidgetMapper.removeBrandWidget
import com.tokopedia.tokopedianow.annotation.domain.model.TokoNowGetAnnotationListResponse.GetAnnotationListResponse
import com.tokopedia.tokopedianow.annotation.domain.param.AnnotationPageSource
import com.tokopedia.tokopedianow.annotation.domain.param.AnnotationType
import com.tokopedia.tokopedianow.annotation.domain.usecase.GetAnnotationWidgetUseCase
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
import com.tokopedia.tokopedianow.category.domain.response.CategoryDetailResponse
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryDetailUseCase
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.model.CategoryAtcTrackerModel
import com.tokopedia.tokopedianow.category.presentation.model.CategoryL2Model
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryNavigationUiModel
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType.CATEGORY_SHOWCASE
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType.PRODUCT_RECOMMENDATION
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.constant.TokoNowStaticLayoutType.Companion.PRODUCT_ADS_CAROUSEL
import com.tokopedia.tokopedianow.common.domain.mapper.AceSearchParamMapper
import com.tokopedia.tokopedianow.common.domain.mapper.AddressMapper
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper.addProductAdsCarousel
import com.tokopedia.tokopedianow.common.domain.mapper.ProductAdsMapper.findAdsProductCarousel
import com.tokopedia.tokopedianow.common.domain.model.GetTickerData
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam
import com.tokopedia.tokopedianow.common.domain.usecase.GetProductAdsUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuUiModel
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.category.domain.model.CategorySharingModel
import com.tokopedia.tokopedianow.category.constant.TOKONOW_CATEGORY_L1
import com.tokopedia.tokopedianow.searchcategory.utils.CATEGORY_TOKONOW_DIRECTORY
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class TokoNowCategoryViewModel @Inject constructor(
    private val getCategoryProductUseCase: GetCategoryProductUseCase,
    private val getCategoryDetailUseCase: GetCategoryDetailUseCase,
    private val getProductAdsUseCase: GetProductAdsUseCase,
    private val getAnnotationWidgetUseCase: GetAnnotationWidgetUseCase,
    private val aceSearchParamMapper: AceSearchParamMapper,
    private val addressData: TokoNowLocalAddress,
    getShopAndWarehouseUseCase: GetChosenAddressWarehouseLocUseCase,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : BaseCategoryViewModel(
    getShopAndWarehouseUseCase = getShopAndWarehouseUseCase,
    getTargetedTickerUseCase = getTargetedTickerUseCase,
    getMiniCartUseCase = getMiniCartUseCase,
    addToCartUseCase = addToCartUseCase,
    updateCartUseCase = updateCartUseCase,
    deleteCartUseCase = deleteCartUseCase,
    affiliateService = affiliateService,
    addressData = addressData,
    userSession = userSession,
    dispatchers = dispatchers
) {
    companion object {
        const val BATCH_SHOWCASE_TOTAL = 3
        const val NO_WAREHOUSE_ID = "0"
        const val PRODUCT_ROWS = 7
        const val DEFAULT_DEEPLINK_PARAM = "category/l1"
        const val PAGE_TYPE_CATEGORY = "cat%s"
        const val CATEGORY_LVL_1 = 1
    }

    init {
        miniCartSource = MiniCartSource.TokonowCategoryPage
    }

    /**
     * -- private immutable variable section --
     */

    private val categoryL2Models: MutableList<CategoryL2Model> = mutableListOf()
    private val _atcDataTracker: MutableLiveData<CategoryAtcTrackerModel> = MutableLiveData()
    private val _scrollNotNeeded = MutableLiveData<Unit>()
    private val _shareLiveData = SingleLiveEvent<CategorySharingModel>()

    /**
     * -- private mutable variable section --
     */

    private var categoryRecommendation: TokoNowCategoryMenuUiModel? = null

    /**
     * -- public immutable variable section --
     */

    val atcDataTracker: LiveData<CategoryAtcTrackerModel> = _atcDataTracker
    val scrollNotNeeded: LiveData<Unit> = _scrollNotNeeded
    val shareLiveData: LiveData<CategorySharingModel> = _shareLiveData

    /**
     * -- override function section --
     */

    override val tickerPage: String
        get() = GetTargetedTickerUseCase.CATEGORY_PAGE

    override suspend fun loadFirstPage(tickerData: GetTickerData) {
        val warehouses = addressData.getWarehousesData()
        val localCacheModel = addressData.getAddressData()
        val detailResponse = getCategoryDetailUseCase.execute(
            categoryIdL1 = categoryIdL1,
            warehouses = warehouses
        )

        val categoryNavigationUiModel = detailResponse.mapToCategoryNavigation()
        categoryRecommendation = detailResponse.mapToCategoryRecommendation(
            source = TOKONOW_CATEGORY_L1
        )

        visitableList.clear()

        visitableList.addHeaderSpace(
            space = navToolbarHeight,
            detailResponse = detailResponse
        )
        visitableList.addChooseAddress(
            detailResponse = detailResponse,
            localCacheModel = localCacheModel
        )
        visitableList.addTicker(
            detailResponse = detailResponse,
            tickerList = tickerData.tickerList
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
        visitableList.addBrandWidget()

        addCategoryShowcases(
            categoryNavigationUiModel = categoryNavigationUiModel
        )

        hidePageLoading()
        updateVisitableListLiveData()
        sendOpenScreenL1Tracker(detailResponse)
        setSharingModel(detailResponse)

        getFirstPage()
    }

    override suspend fun loadNextPage() {
        getBatchShowcase(hasAdded = false)
    }

    override fun onSuccessGetMiniCartData(miniCartData: MiniCartSimplifiedData) {
        super.onSuccessGetMiniCartData(miniCartData)
        visitableList.updateProductQuantity(miniCartData)
        updateVisitableListLiveData()
    }

    /**
     * -- private suspend function section --
     */

    private suspend fun getCategoryShowcaseAsync(
        categoryL2Model: CategoryL2Model,
        hasAdded: Boolean
    ): Deferred<Unit?> = asyncCatchError(block = {
        val requestParams = createRequestQueryParams(categoryL2Model.id)
        val categoryPage = getCategoryProductUseCase.execute(requestParams)

        categoryL2Models.remove(categoryL2Model)

        val productList = categoryPage.searchProduct.data.productList.filter { !it.isOos() }
        if (productList.isEmpty()) {
            visitableList.removeItem(
                id = categoryL2Model.id
            )
            return@asyncCatchError
        }

        if (hasAdded) {
            visitableList.mapCategoryShowcase(
                totalData = categoryPage.searchProduct.header.totalData,
                productList = productList,
                categoryIdL2 = categoryL2Model.id,
                title = categoryL2Model.title,
                seeAllAppLink = categoryL2Model.appLink,
                miniCartData = miniCartData,
                hasBlockedAddToCart = hasBlockedAddToCart
            )
        } else {
            visitableList.addCategoryShowcase(
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
        removeVisitableItem(categoryL2Model.id)
    }

    private suspend fun getBatchShowcase(hasAdded: Boolean) {
        visitableList.addProgressBar()
        updateVisitableListLiveData()

        categoryL2Models.take(BATCH_SHOWCASE_TOTAL).map { categoryL2Model ->
            getCategoryShowcaseAsync(
                categoryL2Model = categoryL2Model,
                hasAdded = hasAdded
            ).await()
        }

        visitableList.removeItem(CategoryLayoutType.MORE_PROGRESS_BAR.name)

        addCategoryRecommendation()
        updateVisitableListLiveData()
    }

    private fun getProductAds(categoryId: String) {
        launchCatchError(block = {
            val params = GetProductAdsParam(
                categoryId = categoryId,
                addressData = addressData.getAddressData(),
                src = GetProductAdsParam.SRC_DIRECTORY_TOKONOW,
                userId = getUserId()
            ).generateQueryParams()

            val response = getProductAdsUseCase.execute(params)

            if (response.productList.isNotEmpty()) {
                visitableList.mapProductAdsCarousel(
                    response = response,
                    miniCartData = miniCartData,
                    hasBlockedAddToCart = hasBlockedAddToCart
                )
            } else {
                removeVisitableItem(PRODUCT_ADS_CAROUSEL)
            }

            updateVisitableListLiveData()
        }) {
            removeVisitableItem(PRODUCT_ADS_CAROUSEL)
            updateVisitableListLiveData()
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

    private fun getFirstPage() {
        launchCatchError(
            block = {
                getBatchShowcase(hasAdded = true)
                getProductAds(categoryIdL1)
                getBrandWidget()
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

    fun retryGetBrandWidget(id: String) {
        visitableList.mapBrandWidgetLoading(id)
        updateVisitableListLiveData()
        getBrandWidget()
    }

    private fun getBrandWidget() {
        launchCatchError(block = {
            val response = getAnnotationWidget(AnnotationType.BRAND)

            if (response.showWidget()) {
                visitableList.mapBrandWidget(response)
            } else {
                visitableList.removeBrandWidget()
            }

            updateVisitableListLiveData()
        }) {
            visitableList.mapBrandWidgetError()
            updateVisitableListLiveData()
        }
    }

    private suspend fun getAnnotationWidget(
        annotationType: AnnotationType
    ): GetAnnotationListResponse {
        return getAnnotationWidgetUseCase.execute(
            categoryId = categoryIdL1,
            warehouses = getWarehouses(),
            annotationType = annotationType,
            pageSource = AnnotationPageSource.CLP_L1
        )
    }

    private fun createRequestQueryParams(categoryId: String): Map<String?, Any?> {
        return aceSearchParamMapper.createRequestParams(
            source = CATEGORY_TOKONOW_DIRECTORY,
            srpPageId = categoryId,
            rows = PRODUCT_ROWS
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
            _atcDataTracker.postValue(
                CategoryAtcTrackerModel(
                    categoryIdL1 = categoryIdL1,
                    index = item.index,
                    headerName = item.headerName,
                    quantity = quantity,
                    product = product,
                    layoutType = CATEGORY_SHOWCASE.name
                )
            )
        }
    }

    private fun trackProductAdsAddToCart(product: ProductCardCompactUiModel, quantity: Int) {
        visitableList.findAdsProductCarousel(product.productId).let { item ->
            _atcDataTracker.postValue(
                CategoryAtcTrackerModel(
                    index = item.position,
                    quantity = quantity,
                    shopId = item.shopId,
                    shopName = item.shopName,
                    shopType = item.shopType,
                    categoryBreadcrumbs = item.categoryBreadcrumbs,
                    product = item.productCardModel,
                    layoutType = PRODUCT_ADS_CAROUSEL
                )
            )
        }
    }

    private fun setSharingModel(getCategoryDetailResponse: CategoryDetailResponse) {
        val categoryDetail = getCategoryDetailResponse.categoryDetail

        val title = categoryDetail.data.name
        val url = categoryDetail.data.url
        val deepLinkParam = "$DEFAULT_DEEPLINK_PARAM/$categoryIdL1"
        val utmCampaignList = getUtmCampaignList()

        _shareLiveData.postValue(
            CategorySharingModel(
                categoryIdLvl2 = "",
                categoryIdLvl3 = "",
                title = title,
                deeplinkParam = deepLinkParam,
                url = url,
                utmCampaignList = utmCampaignList
            )
        )
    }

    private fun getUtmCampaignList(): List<String> {
        return listOf(String.format(PAGE_TYPE_CATEGORY, CATEGORY_LVL_1), categoryIdL1)
    }

    private fun sendOpenScreenL1Tracker(detailResponse: CategoryDetailResponse) {
        sendOpenScreenTracker(
            id = detailResponse.categoryDetail.data.id,
            name = detailResponse.categoryDetail.data.name,
            url = detailResponse.categoryDetail.data.url
        )
    }

    private fun getWarehouses(): String {
        val addressLocalCacheModel = addressData.getAddressData()
        return AddressMapper.mapToWarehouses(addressLocalCacheModel)
    }
}
