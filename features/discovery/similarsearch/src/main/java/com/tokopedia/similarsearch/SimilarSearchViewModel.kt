package com.tokopedia.similarsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.discovery.common.Event
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.State.*
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.similarsearch.divider.DividerViewModel
import com.tokopedia.similarsearch.emptyresult.EmptyResultViewModel
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.similarsearch.title.TitleViewModel
import com.tokopedia.similarsearch.utils.asObjectDataLayer
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlin.math.min

internal class SimilarSearchViewModel(
        dispatcherProvider: DispatcherProvider,
        private val similarSearchQuery: String,
        private val getSimilarProductsUseCase: UseCase<SimilarProductModel>,
        private val addWishlistUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val userSession: UserSessionInterface
): BaseViewModel(dispatcherProvider.ui()) {

    private var hasLoadData = false
    private var originalProduct = Product()
    private val originalProductLiveData = MutableLiveData<Product>()
    private val similarSearchLiveData = MutableLiveData<State<List<Any>>>()
    private val similarSearchViewModelList = mutableListOf<Any>()
    private val similarProductModelList = mutableListOf<Product>()
    private val loadingMoreModel = LoadingMoreModel()
    private val routeToLoginPageEventLiveData = MutableLiveData<Event<Boolean>>()
    private val updateWishlistOriginalProductEventLiveData = MutableLiveData<Event<Boolean>>()
    private val updateWishlistSimilarProductEventLiveData = MutableLiveData<Event<Product>>()
    private val addWishlistEventLiveData = MutableLiveData<Event<Boolean>>()
    private val removeWishlistEventLiveData = MutableLiveData<Event<Boolean>>()
    private val trackingImpressionSimilarProductEventLiveData = MutableLiveData<Event<List<Any>>>()
    private val trackingEmptyResultEventLiveData = MutableLiveData<Event<Boolean>>()
    private val trackingWishlistEventLiveData = MutableLiveData<Event<WishlistTrackingModel>>()

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

        similarProductModelList.addAll(similarProductModel.getSimilarProductList())
        similarProductModelList.forEachIndexed { index, product -> product.position = index + 1 }

        processSimilarSearchViewModelList(similarProductModel)
        postSimilarSearchLiveDataSuccess()
    }

    private fun processSimilarSearchViewModelList(similarProductModel: SimilarProductModel) {
        similarSearchViewModelList.clear()

        if (similarProductModelList.isEmpty()) {
            processSimilarSearchViewModelEmptyResult()
        }
        else {
            processSimilarSearchViewModelData(similarProductModel)
        }
    }

    private fun processSimilarSearchViewModelEmptyResult() {
        addEmptyResultView()
        postTrackingEmptyResult()
    }

    private fun addEmptyResultView() {
        similarSearchViewModelList.add(EmptyResultViewModel())
    }

    private fun postTrackingEmptyResult() {
        trackingEmptyResultEventLiveData.postValue(Event(true))
    }

    private fun processSimilarSearchViewModelData(similarProductModel: SimilarProductModel) {
        addDividerModel()
        addTitleView()
        addSimilarProductListForOnePage()

        postOriginalProductLiveData(similarProductModel.getOriginalProduct())
        postTrackingImpressionSimilarProduct(similarProductModel)
    }

    private fun addDividerModel() {
        similarSearchViewModelList.add(DividerViewModel())
    }

    private fun addTitleView() {
        similarSearchViewModelList.add(TitleViewModel())
    }

    private fun addSimilarProductListForOnePage() {
        val productList = getProductListForOnePage()

        if (productList.isNotEmpty()) {
            addSimilarProductList(productList)
            addLoadingMoreView()
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

    private fun addSimilarProductList(productList: List<Product>) {
        similarSearchViewModelList.addAll(productList)
    }

    private fun addLoadingMoreView() {
        if (getHasNextPage()) {
            similarSearchViewModelList.add(loadingMoreModel)
        }
    }

    fun getHasNextPage(): Boolean {
        return similarProductModelList.size > 0
    }

    private fun postOriginalProductLiveData(originalProduct: Product) {
        this.originalProduct = originalProduct
        this.originalProductLiveData.postValue(originalProduct)
    }

    private fun postTrackingImpressionSimilarProduct(similarProductModel: SimilarProductModel) {
        val trackingImpressionSimilarProductList = mutableListOf<Any>()

        similarProductModel.getSimilarProductList().forEach { productItem ->
            trackingImpressionSimilarProductList.add(productItem.asObjectDataLayer())
        }

        trackingImpressionSimilarProductEventLiveData.postValue(Event(trackingImpressionSimilarProductList))
    }

    private fun postSimilarSearchLiveDataSuccess() {
        similarSearchLiveData.postValue(Success(similarSearchViewModelList))
    }

    private fun catchGetSimilarProductsError(throwable: Throwable?) {
        throwable?.printStackTrace()

        addEmptyResultView()
        postTrackingEmptyResult()

        postSimilarSearchLiveDataError()
    }

    private fun postSimilarSearchLiveDataError() {
        similarSearchLiveData.postValue(Error("", similarSearchViewModelList))
    }

    fun getOriginalProductLiveData(): LiveData<Product> {
        return originalProductLiveData
    }

    fun onViewLoadMore() {
        if (!getHasNextPage()) return

        removeLoadingMoreModel()
        addSimilarProductListForOnePage()

        postSimilarSearchLiveDataSuccess()
    }

    private fun removeLoadingMoreModel() {
        similarSearchViewModelList.remove(loadingMoreModel)
    }

    fun getSimilarSearchLiveData(): LiveData<State<List<Any>>> {
        return similarSearchLiveData
    }

    fun onViewToggleWishlistOriginalProduct() {
        if (!userSession.isLoggedIn) {
            routeToLoginPageEventLiveData.postValue(Event(true))
            return
        }

        val originalProductWishListActionListener = createOriginalProductWishlistActionListener()
        toggleWishlistForProduct(
                originalProduct.id,
                originalProduct.isWishlisted,
                originalProductWishListActionListener
        )
    }

    private fun createOriginalProductWishlistActionListener() = object : WishListActionListener {
        override fun onSuccessRemoveWishlist(productId: String?) {
            removeWishlistEventLiveData.postValue(Event(true))

            postUpdateWishlistOriginalProductEvent(productId,false)
        }

        override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
            removeWishlistEventLiveData.postValue(Event(false))
        }

        override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
            addWishlistEventLiveData.postValue(Event(false))
        }

        override fun onSuccessAddWishlist(productId: String?) {
            addWishlistEventLiveData.postValue(Event(true))

            postUpdateWishlistOriginalProductEvent(productId,true)
        }
    }

    fun postUpdateWishlistOriginalProductEvent(productId: String?, isWishlisted: Boolean) {
        if (originalProduct.id == productId && originalProduct.isWishlisted != isWishlisted) {
            originalProduct.isWishlisted = isWishlisted
            updateWishlistOriginalProductEventLiveData.postValue(Event(isWishlisted))
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
            trackingWishlistEventLiveData.postValue(Event(createWishlistTrackingModel(!isWishlisted, productId)))
            routeToLoginPageEventLiveData.postValue(Event(true))
            return
        }

        if (similarSearchViewModelList.find { it is Product && it.id == productId } == null) {
            return
        }

        val similarProductWishlistActionListener = createSimilarProductWishlistActionListener()
        toggleWishlistForProduct(productId, isWishlisted, similarProductWishlistActionListener)
    }

    private fun createWishlistTrackingModel(isAddWishlist: Boolean, productId: String?): WishlistTrackingModel {
        return WishlistTrackingModel(
                isAddWishlist = isAddWishlist,
                productId = productId ?: "",
                isTopAds = false,
                keyword = similarSearchQuery,
                isUserLoggedIn = userSession.isLoggedIn
        )
    }

    private fun createSimilarProductWishlistActionListener() = object : WishListActionListener {
        override fun onSuccessRemoveWishlist(productId: String?) {
            removeWishlistEventLiveData.postValue(Event(true))

            postUpdateWishlistInSimilarSearchLiveData(productId, false)

            trackingWishlistEventLiveData.postValue(Event(createWishlistTrackingModel(false, productId)))
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

            trackingWishlistEventLiveData.postValue(Event(createWishlistTrackingModel(true, productId)))
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

    fun getUpdateWishlistOriginalProductEventLiveData(): LiveData<Event<Boolean>> {
        return updateWishlistOriginalProductEventLiveData
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
        postUpdateWishlistOriginalProductEvent(productId, isWishlisted)

        postUpdateWishlistInSimilarSearchLiveData(productId, isWishlisted)
    }

    fun getOriginalProductId(): String {
        return originalProduct.id
    }

    fun getTrackingImpressionSimilarProductEventLiveData(): LiveData<Event<List<Any>>> {
        return trackingImpressionSimilarProductEventLiveData
    }

    fun getTrackingEmptyResultEventLiveData(): LiveData<Event<Boolean>> {
        return trackingEmptyResultEventLiveData
    }

    fun getTrackingWishlistEventLiveData(): LiveData<Event<WishlistTrackingModel>> {
        return trackingWishlistEventLiveData
    }
}