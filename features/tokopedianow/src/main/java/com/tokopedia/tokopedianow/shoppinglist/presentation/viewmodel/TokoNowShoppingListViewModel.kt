package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_SOURCE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState.Companion.SHOW
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.UiState
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.CommonVisitableMapper.addRecommendedProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addDivider
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addEmptyStockProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addHeader
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addProductInCartWidget
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addShimmeringPage
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addTitle
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addWishlistProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.removeLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LoadMoreDataModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class TokoNowShoppingListViewModel @Inject constructor(
    private val addressData: TokoNowLocalAddress,
    private val productRecommendationUseCase: GetSingleRecommendationUseCase,
    private val userSession: UserSessionInterface,
    getMiniCartUseCase: GetMiniCartListSimplifiedUseCase,
    addToCartUseCase: AddToCartUseCase,
    updateCartUseCase: UpdateCartUseCase,
    deleteCartUseCase: DeleteCartUseCase,
    affiliateService: NowAffiliateService,
    getTargetedTickerUseCase: GetTargetedTickerUseCase,
    dispatchers: CoroutineDispatchers
) : BaseTokoNowViewModel(
    addToCartUseCase,
    updateCartUseCase,
    deleteCartUseCase,
    getMiniCartUseCase,
    affiliateService,
    getTargetedTickerUseCase,
    addressData,
    userSession,
    dispatchers
) {
    private companion object {
        const val PRODUCT_RECOMMENDATION_PAGE_NAME = "tokonow_shopping_list"
        const val PRODUCT_RECOMMENDATION_PAGE_NUMBER_COUNTER = 1
    }

    private val layout: MutableList<Visitable<*>> = arrayListOf()

    private val _isOnScrollNotNeeded: SingleLiveEvent<Unit> = SingleLiveEvent()
    private val _uiState: MutableStateFlow<UiState<List<Visitable<*>>>> = MutableStateFlow(
        UiState.Loading(
            data = layout.addShimmeringPage()
        )
    )

    private var needToLoadMoreData: LoadMoreDataModel = LoadMoreDataModel(
        isNeededToLoadMore = true
    )
    private var job: Job? = null

    val uiState
        get() = _uiState.asStateFlow()
    val isOnScrollNotNeeded
        get() = _isOnScrollNotNeeded

    var headerModel: HeaderModel = HeaderModel()

    fun loadLayout() {
        job = launchCatchError(
            block = {
                layout.clear()

                layout.addHeader(
                    headerModel = headerModel,
                    state = SHOW
                )

                addWishlistSection()
                addEmptyStockSection()
                addProductInCartSection()
                addProductRecommendationSection()

                _uiState.value = UiState.Success(
                    layout.toMutableList()
                )
            },
            onError = {

            }
        )
    }

    private fun addWishlistSection() {
        // do some logic
        layout.addWishlistProducts()
    }

    private fun addEmptyStockSection() {
        // do some logic
        layout.addDivider()
        layout.addTitle(
            title = "Stok habis"
        )
        layout.addEmptyStockProducts()
    }

    private fun addProductInCartSection() {
        // do some logic
        layout.addDivider()
        layout.addTitle(
            title = "5 produk ada di keranjang"
        )
        layout.addProductInCartWidget()
    }

    private suspend fun getProductRecommendationDeferred(
        pageNumber: Int
    ): Deferred<RecommendationWidget> = async {
        val param = GetRecommendationRequestParam(
            pageNumber = pageNumber,
            userId = userSession.userId.toIntSafely(),
            pageName = PRODUCT_RECOMMENDATION_PAGE_NAME,
            xDevice = X_DEVICE_RECOMMENDATION_PARAM,
            xSource = X_SOURCE_RECOMMENDATION_PARAM,
            isTokonow = true
        )
        productRecommendationUseCase.getData(param)
    }

    private suspend fun addProductRecommendationSection() {
        val productRecommendation = getProductRecommendationDeferred(
            pageNumber = PRODUCT_RECOMMENDATION_PAGE_NUMBER_COUNTER
        ).await()

        if (productRecommendation.recommendationItemList.isNotEmpty()) {
            layout.addDivider()
            layout.addTitle(title = productRecommendation.title)
            layout.addRecommendedProducts(productRecommendation)

            if (productRecommendation.hasNext) layout.addLoadMore()
        }

        needToLoadMoreData = needToLoadMoreData.copy(
            isNeededToLoadMore = productRecommendation.hasNext,
            counter = PRODUCT_RECOMMENDATION_PAGE_NUMBER_COUNTER.inc()
        )
    }

    fun loadMoreProductRecommendation(
        isLastVisibleLoadingMore: Boolean
    ) {
        when {
            !needToLoadMoreData.isNeededToLoadMore -> _isOnScrollNotNeeded.value = Unit
            isLastVisibleLoadingMore && job?.isCompleted.orFalse() -> {
                job = launchCatchError(
                    block = {
                        val productRecommendation = getProductRecommendationDeferred(
                            pageNumber = needToLoadMoreData.counter
                        ).await()

                        layout.removeLoadMore()

                        if (productRecommendation.recommendationItemList.isNotEmpty()) {
                            layout.addRecommendedProducts(productRecommendation)

                            if (productRecommendation.hasNext) layout.addLoadMore()
                        }

                        _uiState.value = UiState.Success(layout.toMutableList())

                        needToLoadMoreData = needToLoadMoreData.copy(
                            isNeededToLoadMore = productRecommendation.hasNext,
                            counter = needToLoadMoreData.counter.inc()
                        )
                    },
                    onError = {
                        layout.removeLoadMore()

                        _uiState.value = UiState.Success(layout.toMutableList())

                        needToLoadMoreData = needToLoadMoreData.copy(
                            isNeededToLoadMore = false
                        )
                    }
                )
            }
        }
    }
}
