package com.tokopedia.tokopedianow.shoppinglist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetSingleRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.base.viewmodel.BaseTokoNowViewModel
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_DEVICE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.constant.ConstantValue.X_SOURCE_RECOMMENDATION_PARAM
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.CommonVisitableMapper.addRecommendedProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addDivider
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addEmptyStockProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addHeader
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addHeaderSpace
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addProductInCartWidget
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addTitle
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.addWishlistProducts
import com.tokopedia.tokopedianow.shoppinglist.domain.mapper.MainVisitableMapper.removeLoadMore
import com.tokopedia.tokopedianow.shoppinglist.domain.model.HeaderModel
import com.tokopedia.tokopedianow.shoppinglist.presentation.model.LoadMoreDataModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
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

    private val _layout = MutableLiveData<List<Visitable<*>>>()
    private val _isOnScrollNotNeeded = MutableLiveData<Unit>()

    private val mutableLayout: MutableList<Visitable<*>> = arrayListOf()
    private var needToLoadMoreData: LoadMoreDataModel = LoadMoreDataModel(isNeededToLoadMore = true)
    private var job: Job? = null

    val layout: LiveData<List<Visitable<*>>>
        get() = _layout
    val isOnScrollNotNeeded: LiveData<Unit>
        get() = _isOnScrollNotNeeded

    var headerSpace: Int = Int.ZERO
    var headerModel: HeaderModel = HeaderModel()

    fun loadLayout() {
        job = launchCatchError(
            block = {
                /**
                 * Add wishlist products
                 */

                mutableLayout.addHeaderSpace(
                    space = headerSpace,
                    headerModel = headerModel
                )
                mutableLayout.addHeader(
                    headerModel = headerModel
                )
                mutableLayout.addWishlistProducts()

                /**
                 * Add empty stock products
                 */

                mutableLayout.addDivider()
                mutableLayout.addTitle(
                    title = "Stok habis"
                )
                mutableLayout.addEmptyStockProducts()

                /**
                 * Add product in cart widget
                 */

                mutableLayout.addDivider()
                mutableLayout.addTitle(
                    title = "5 produk ada di keranjang"
                )
                mutableLayout.addProductInCartWidget()

                addProductRecommendationSection()

                _layout.postValue(mutableLayout)
            },
            onError = {

            }
        )
    }

    private suspend fun addProductRecommendationSection() {
        val productRecommendation = getProductRecommendationDeferred(
            pageNumber = PRODUCT_RECOMMENDATION_PAGE_NUMBER_COUNTER
        ).await()

        if (productRecommendation.recommendationItemList.isNotEmpty()) {
            mutableLayout.addDivider()
            mutableLayout.addTitle(title = productRecommendation.title)
            mutableLayout.addRecommendedProducts(productRecommendation)

            if (productRecommendation.hasNext) mutableLayout.addLoadMore()

            needToLoadMoreData = needToLoadMoreData.copy(
                isNeededToLoadMore = productRecommendation.hasNext,
                counter = PRODUCT_RECOMMENDATION_PAGE_NUMBER_COUNTER.inc()
            )
        }
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

    private fun addLoadMoreProductRecommendationSection(
        productRecommendation: RecommendationWidget
    ) {
        mutableLayout.removeLoadMore()
        mutableLayout.addRecommendedProducts(productRecommendation)

        if (productRecommendation.hasNext) mutableLayout.addLoadMore()

        _layout.postValue(mutableLayout)

        needToLoadMoreData = needToLoadMoreData.copy(
            isNeededToLoadMore = productRecommendation.hasNext,
            counter = needToLoadMoreData.counter.inc()
        )
    }

    private fun removeLoadMoreSection() {
        mutableLayout.removeLoadMore()

        _layout.postValue(mutableLayout)

        needToLoadMoreData = needToLoadMoreData.copy(
            isNeededToLoadMore = false
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
                        throw Throwable()
                        val productRecommendation = getProductRecommendationDeferred(
                            pageNumber = needToLoadMoreData.counter
                        ).await()

                        if (productRecommendation.recommendationItemList.isNotEmpty()) addLoadMoreProductRecommendationSection(productRecommendation) else removeLoadMoreSection()
                    },
                    onError = {
                        removeLoadMoreSection()
                    }
                )
            }
        }
    }
}
