package com.tokopedia.tokopedianow.category.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.addLoadMoreLoading
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.addProductCardItems
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.filterNotLoadedLayout
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.mapProductAdsCarousel
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.mapToCategoryTabLayout
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.mapToQuickFilter
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.removeLoadMoreLoading
import com.tokopedia.tokopedianow.category.domain.mapper.CategoryL2TabMapper.updateAllProductQuantity
import com.tokopedia.tokopedianow.category.domain.response.GetCategoryLayoutResponse.Component
import com.tokopedia.tokopedianow.category.domain.usecase.GetCategoryProductUseCase
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryProductListUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam
import com.tokopedia.tokopedianow.common.domain.param.GetProductAdsParam.Companion.SRC_DIRECTORY_TOKONOW
import com.tokopedia.tokopedianow.common.domain.usecase.GetProductAdsUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.searchcategory.domain.mapper.VisitableMapper.updateProductItem
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetSortFilterUseCase
import com.tokopedia.tokopedianow.searchcategory.utils.QUICK_FILTER_TOKONOW_DIRECTORY
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoNowCategoryL2TabViewModel @Inject constructor(
    private val getSortFilterUseCase: GetSortFilterUseCase,
    private val getCategoryProductUseCase: GetCategoryProductUseCase,
    private val getProductAdsUseCase: GetProductAdsUseCase,
    private val addressData: TokoNowLocalAddress,
    private val userSession: UserSessionInterface,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    dispatchers: CoroutineDispatchers
) : BaseTokoNowViewModel(
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
        private const val FIRST_PAGE = 1
        private const val PRODUCT_ROWS = 9
    }

    private val _visitableListLiveData = MutableLiveData<List<Visitable<*>>>()
    private val _routeAppLink = MutableLiveData<String>()

    val visitableListLiveData: LiveData<List<Visitable<*>>> = _visitableListLiveData
    val routeAppLink: LiveData<String> = _routeAppLink

    private val visitableList = mutableListOf<Visitable<*>>()

    private var page = FIRST_PAGE
    private var getProductJob: Job? = null

    private var categoryIdL2: String = ""

    init {
        miniCartSource = MiniCartSource.TokonowCategoryPage
    }

    override fun onSuccessGetMiniCartData(miniCartData: MiniCartSimplifiedData) {
        super.onSuccessGetMiniCartData(miniCartData)
        visitableList.updateAllProductQuantity(
            miniCartData = miniCartData,
            hasBlockedAddToCart = hasBlockedAddToCart
        )
        updateVisitableListLiveData()
    }

    fun onViewCreated(categoryIdL2: String, components: List<Component>) {
        initAffiliateCookie()
        setCategoryIdL2(categoryIdL2)
        loadFirstPage(components)
    }

    fun loadMore() {
        if (getProductJob?.isCompleted != false) {
            launchCatchError(block = {
                showLoadMoreLoading()
                getProductList()
                hideLoadMoreLoading()
                page++
            }) {
            }.let {
                getProductJob = it
            }
        }
    }

    fun refreshPage(components: List<Component>) {
        loadFirstPage(components)
        getMiniCart()
    }

    fun updateWishlistStatus(productId: String, hasBeenWishlist: Boolean) {
        launch {
            visitableList.updateProductItem(
                productId = productId,
                hasBeenWishlist = hasBeenWishlist
            )
            updateVisitableListLiveData()
        }
    }

    fun createProductDetailAppLink(productId: String, appLink: String = "") {
        launch {
            val uri = appLink.ifEmpty {
                UriUtil.buildUri(
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    productId
                )
            }
            val affiliateLink = createAffiliateLink(uri)
            _routeAppLink.postValue(affiliateLink)
        }
    }

    fun onResume() {
        getMiniCart()
    }

    private fun loadFirstPage(components: List<Component>) {
        launchCatchError(block = {
            page = FIRST_PAGE

            visitableList.clear()
            visitableList.mapToCategoryTabLayout(components)
            updateVisitableListLiveData()

            visitableList.filterNotLoadedLayout().forEach {
                when (it) {
                    is CategoryQuickFilterUiModel -> getQuickFilterAsync(it).await()
                    is CategoryProductListUiModel -> getProductListAsync(it).await()
                    is TokoNowAdsCarouselUiModel -> getAdsProductListAsync(it).await()
                }
            }
            page++
        }) {
        }.let {
            getProductJob = it
        }
    }

    private fun getQuickFilterAsync(item: CategoryQuickFilterUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val response = getSortFilterUseCase.execute(
                source = QUICK_FILTER_TOKONOW_DIRECTORY
            )
            visitableList.mapToQuickFilter(item, response)
            updateVisitableListLiveData()
        }) {
        }
    }

    private fun getProductListAsync(item: CategoryProductListUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            visitableList.remove(item)
            getProductList()
        }) {
        }
    }

    private fun getAdsProductListAsync(item: TokoNowAdsCarouselUiModel): Deferred<Unit?> {
        return asyncCatchError(block = {
            val warehouseIds = addressData.getWarehouseIds()

            val params = GetProductAdsParam(
                categoryId = categoryIdL2,
                warehouseIds = warehouseIds,
                src = SRC_DIRECTORY_TOKONOW,
                userId = userSession.userId
            )

            val response = getProductAdsUseCase.execute(params)

            if(response.productList.isNotEmpty()) {
                visitableList.mapProductAdsCarousel(
                    item = item,
                    response = response,
                    miniCartData = miniCartData,
                    hasBlockedAddToCart = hasBlockedAddToCart
                )
            } else {
                visitableList.remove(item)
            }

            updateVisitableListLiveData()
        }) {

        }
    }

    private suspend fun getProductList() {
        val response = getCategoryProductUseCase.execute(
            categoryIdL2 = categoryIdL2,
            page = page,
            rows = PRODUCT_ROWS
        )
        visitableList.addProductCardItems(
            response = response,
            miniCartData = miniCartData,
            hasBlockedAddToCart = hasBlockedAddToCart
        )
        updateVisitableListLiveData()
    }

    private fun setCategoryIdL2(categoryIdL2: String) {
        this.categoryIdL2 = categoryIdL2
    }

    private fun showLoadMoreLoading() {
        visitableList.addLoadMoreLoading()
        updateVisitableListLiveData()
    }

    private fun hideLoadMoreLoading() {
        visitableList.removeLoadMoreLoading()
        updateVisitableListLiveData()
    }

    private fun updateVisitableListLiveData() {
        _visitableListLiveData.postValue(visitableList)
    }
}
