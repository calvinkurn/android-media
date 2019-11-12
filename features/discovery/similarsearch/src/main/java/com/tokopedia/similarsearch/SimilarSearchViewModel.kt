package com.tokopedia.similarsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.discovery.common.Event
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.State.*
import com.tokopedia.discovery.common.model.SimilarSearchSelectedProduct
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlin.math.min

internal class SimilarSearchViewModel(
        dispatcherProvider: DispatcherProvider,
        val similarSearchSelectedProduct: SimilarSearchSelectedProduct,
        private val getSimilarProductsUseCase: UseCase<SimilarProductModel>,
        private val addWishlistUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val userSession: UserSessionInterface
): BaseViewModel(dispatcherProvider.ui()) {

    private var hasLoadData = false
    private val similarSearchLiveData = MutableLiveData<State<List<Any>>>()
    private val similarSearchViewModelList = mutableListOf<Any>()
    private val similarProductModelList = mutableListOf<Product>()
    private val loadingMoreModel = LoadingMoreModel()
    private val routeToLoginPageEventLiveData = MutableLiveData<Event<Boolean>>()

    fun onViewCreated() {
        if (!hasLoadData) {
            hasLoadData = true
            getSimilarProducts()
        }
    }

    private fun getSimilarProducts() {
        launchCatchError(block = {
            tryGetSimilarProducts()
        }, onError = {
            catchGetSimilarProductsError(it)
        })
    }

    private suspend fun tryGetSimilarProducts() {
        similarSearchLiveData.postValue(Loading())

        val similarProductModel = getSimilarProductsUseCase.executeOnBackground()

        processGetSimilarSearchSuccess(similarProductModel)
    }

    private fun processGetSimilarSearchSuccess(similarProductModel: SimilarProductModel?) {
        if (similarProductModel == null) {
            catchGetSimilarProductsError(null)
            return
        }

        similarProductModelList.addAll(similarProductModel.getProductList())

        initFirstPageSimilarSearchViewModelList()

        if (similarProductModelList.isEmpty()) {
            addEmptyResultView()
        }
        else {
            addTitleView()
            processSimilarProductListForOnePage()
        }

        postSuccessData()
    }

    private fun initFirstPageSimilarSearchViewModelList() {
        similarSearchViewModelList.clear()
        similarSearchViewModelList.add(DividerViewModel())
    }

    private fun addEmptyResultView() {
        similarSearchViewModelList.add(EmptyResultViewModel())
    }

    private fun addTitleView() {
        similarSearchViewModelList.add(TitleViewModel())
    }

    private fun processSimilarProductListForOnePage() {
        val productList = getProductListForOnePage()

        if (productList.isNotEmpty()) {
            appendSimilarProductList(productList)
            appendLoadingMoreView()
        }
    }

    private fun getProductListForOnePage(): List<Product> {
        val itemCount = min(similarProductModelList.size, SIMILAR_PRODUCT_ITEM_SIZE_PER_PAGE)

        val productListForOnePage = similarProductModelList.subList(0, itemCount).toList()

        for (i in 0 until itemCount) {
            similarProductModelList.removeAt(0)
        }

        return productListForOnePage
    }

    private fun appendSimilarProductList(productList: List<Product>) {
        similarSearchViewModelList.addAll(productList)
    }

    private fun appendLoadingMoreView() {
        if (getHasNextPage()) {
            similarSearchViewModelList.add(loadingMoreModel)
        }
    }

    fun getHasNextPage(): Boolean {
        return similarProductModelList.size > 0
    }

    private fun postSuccessData() {
        similarSearchLiveData.postValue(Success(similarSearchViewModelList))
    }

    private fun catchGetSimilarProductsError(throwable: Throwable?) {
        throwable?.printStackTrace()

        similarSearchLiveData.postValue(Error(""))
    }

    fun onViewLoadMore() {
        if (!getHasNextPage()) return

        removeLoadingMoreModel()
        processSimilarProductListForOnePage()

        postSuccessData()
    }

    private fun removeLoadingMoreModel() {
        similarSearchViewModelList.remove(loadingMoreModel)
    }

    fun getSimilarSearchLiveData(): LiveData<State<List<Any>>> {
        return similarSearchLiveData
    }

    fun onViewToggleWishlistSelectedProduct() {
        if (!userSession.isLoggedIn) {
            routeToLoginPageEventLiveData.postValue(Event(true))
            return
        }

        addWishlistUseCase.createObservable(
                similarSearchSelectedProduct.id,
                userSession.userId,
                createSelectedProductWishlistActionListener()
        )
    }

    private fun createSelectedProductWishlistActionListener(): WishListActionListener {
        return object : WishListActionListener {
            override fun onSuccessRemoveWishlist(productId: String?) {

            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {

            }

            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {

            }

            override fun onSuccessAddWishlist(productId: String?) {

            }
        }
    }

    fun getRouteToLoginPageEventLiveData(): LiveData<Event<Boolean>> {
        return routeToLoginPageEventLiveData
    }
}