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
    private val updateWishlistSelectedProductEventLiveData = MutableLiveData<Event<Boolean>>()
    private val updateWishlistSimilarProductEventLiveData = MutableLiveData<Event<Product>>()
    private val addWishlistEventLiveData = MutableLiveData<Event<Boolean>>()
    private val removeWishlistEventLiveData = MutableLiveData<Event<Boolean>>()

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

        postSimilarSearchLiveDataSuccess()
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

    private fun postSimilarSearchLiveDataSuccess() {
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

        postSimilarSearchLiveDataSuccess()
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

        val selectedProductWishListActionListener = createSelectedProductWishlistActionListener()
        toggleWishlistForProduct(
                similarSearchSelectedProduct.id,
                similarSearchSelectedProduct.isWishlisted,
                selectedProductWishListActionListener
        )
    }

    private fun createSelectedProductWishlistActionListener() = object : WishListActionListener {
        override fun onSuccessRemoveWishlist(productId: String?) {
            removeWishlistEventLiveData.postValue(Event(true))

            postUpdateWishlistSelectedProductEvent(productId,false)
        }

        override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
            removeWishlistEventLiveData.postValue(Event(false))
        }

        override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
            addWishlistEventLiveData.postValue(Event(false))
        }

        override fun onSuccessAddWishlist(productId: String?) {
            addWishlistEventLiveData.postValue(Event(true))

            postUpdateWishlistSelectedProductEvent(productId,true)
        }
    }

    fun postUpdateWishlistSelectedProductEvent(productId: String?, isWishlisted: Boolean) {
        if (similarSearchSelectedProduct.id == productId && similarSearchSelectedProduct.isWishlisted != isWishlisted) {
            similarSearchSelectedProduct.isWishlisted = isWishlisted
            updateWishlistSelectedProductEventLiveData.postValue(Event(isWishlisted))
        }
    }

    fun getRouteToLoginPageEventLiveData(): LiveData<Event<Boolean>> {
        return routeToLoginPageEventLiveData
    }

    private fun toggleWishlistForProduct(productId: String, isWishlisted: Boolean, wishListActionListener: WishListActionListener) {
        if (!isWishlisted) {
            addWishlistUseCase.createObservable(productId, userSession.userId, wishListActionListener)
        }
        else {
            removeWishListUseCase.createObservable(productId, userSession.userId, wishListActionListener)
        }
    }

    fun onViewToggleWishlistSimilarProduct(productId: String, isWishlisted: Boolean) {
        if (!userSession.isLoggedIn) {
            routeToLoginPageEventLiveData.postValue(Event(true))
            return
        }

        if (similarSearchViewModelList.find { it is Product && it.id == productId } == null) {
            return
        }

        val similarProductWishlistActionListener = createSimilarProductWishlistActionListener()
        toggleWishlistForProduct(productId, isWishlisted, similarProductWishlistActionListener)
    }

    private fun createSimilarProductWishlistActionListener() = object : WishListActionListener {
        override fun onSuccessRemoveWishlist(productId: String?) {
            removeWishlistEventLiveData.postValue(Event(true))

            postUpdateWishlistInSimilarSearchLiveData(productId, false)
        }

        override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
            removeWishlistEventLiveData.postValue(Event(false))
        }

        override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
            addWishlistEventLiveData.postValue(Event(false))
        }

        override fun onSuccessAddWishlist(productId: String?) {
            addWishlistEventLiveData.postValue(Event(true))

            postUpdateWishlistInSimilarSearchLiveData(productId, true)
        }
    }

    private fun postUpdateWishlistInSimilarSearchLiveData(productId: String?, isWishlisted: Boolean) {
        val similarProductItemForWishlist = getSimilarProductForWishlist(productId)

        if (similarProductItemForWishlist != null && similarProductItemForWishlist.isWishlisted != isWishlisted) {
            similarProductItemForWishlist.isWishlisted = isWishlisted
            updateWishlistSimilarProductEventLiveData.postValue(Event(similarProductItemForWishlist))
        }
    }

    private fun getSimilarProductForWishlist(productId: String?): Product? {
        val similarSearchViewModelItem = similarSearchViewModelList.find { it is Product && it.id == productId }

        return if (similarSearchViewModelItem is Product) similarSearchViewModelItem else null
    }

    fun getUpdateWishlistSelectedProductEventLiveData(): LiveData<Event<Boolean>> {
        return updateWishlistSelectedProductEventLiveData
    }

    fun getUpdateWishlistSimilarProductEventLiveData(): LiveData<Event<Product>> {
        return updateWishlistSimilarProductEventLiveData
    }

    fun getAddWishlistEventLiveData(): LiveData<Event<Boolean>> {
        return addWishlistEventLiveData
    }

    fun getRemoveWishlistEventLiveData(): LiveData<Event<Boolean>> {
        return removeWishlistEventLiveData
    }

    fun onViewUpdateProductWishlistStatus(productId: String?, isWishlisted: Boolean) {
        postUpdateWishlistSelectedProductEvent(productId, isWishlisted)

        postUpdateWishlistInSimilarSearchLiveData(productId, isWishlisted)
    }
}