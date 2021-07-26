package com.tokopedia.similarsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase.Companion.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.Event
import com.tokopedia.discovery.common.State
import com.tokopedia.discovery.common.State.*
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.similarsearch.divider.DividerViewModel
import com.tokopedia.similarsearch.emptyresult.EmptyResultViewModel
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.getsimilarproducts.model.SimilarProductModel
import com.tokopedia.similarsearch.title.TitleViewModel
import com.tokopedia.similarsearch.utils.asObjectDataLayerAddToCart
import com.tokopedia.similarsearch.utils.asObjectDataLayerImpressionAndClick
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import rx.Subscriber
import kotlin.math.min
import com.tokopedia.usecase.UseCase as RxUseCase

internal class SimilarSearchViewModel(
        dispatcherProvider: CoroutineDispatchers,
        private val similarSearchQuery: String,
        private val getSimilarProductsUseCase: UseCase<SimilarProductModel>,
        private val addWishlistUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val addToCartUseCase: RxUseCase<AddToCartDataModel>,
        private val userSession: UserSessionInterface
): BaseViewModel(dispatcherProvider.main) {

    private var hasLoadData = false
    private var originalProduct = Product()
    private val originalProductLiveData = MutableLiveData<Product>()
    private val similarSearchLiveData = MutableLiveData<State<List<Any>>>()
    private val similarSearchViewModelList = mutableListOf<Any>()
    private val similarProductModelList = mutableListOf<Product>()
    private val loadingMoreModel = LoadingMoreModel()
    private val routeToLoginPageEventLiveData = MutableLiveData<Event<Boolean>>()
    private val updateWishlistOriginalProductEventLiveData = MutableLiveData<Event<Boolean>>()
    private val addWishlistEventLiveData = MutableLiveData<Event<Boolean>>()
    private val removeWishlistEventLiveData = MutableLiveData<Event<Boolean>>()
    private val trackingImpressionSimilarProductEventLiveData = MutableLiveData<Event<List<Any>>>()
    private val trackingEmptyResultEventLiveData = MutableLiveData<Event<Boolean>>()
    private val trackingWishlistEventLiveData = MutableLiveData<Event<WishlistTrackingModel>>()
    private val trackingAddToCartEventLiveData = MutableLiveData<Event<Any>>()
    private val trackingBuyEventLiveData = MutableLiveData<Event<Any>>()
    private val addToCartEventLiveData = MutableLiveData<Event<Boolean>>()
    private val routeToCartPageEventLiveData = MutableLiveData<Event<Boolean>>()
    private var addToCartFailedMessage = ""

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
            trackingImpressionSimilarProductList.add(productItem.asObjectDataLayerImpressionAndClick())
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

    fun onViewClickAddToCart() {
        executeAddToCart {
            onClickAddToCartSuccess(it)
        }
    }

    private fun executeAddToCart(onAddToCartUseCaseSuccess: (addToCartDataModel: AddToCartDataModel?) -> Unit) {
        if (!userSession.isLoggedIn) {
            routeToLoginPageEventLiveData.postValue(Event(true))
        }
        else {
            val requestParams = createAddToCartUseCaseRequestParams()
            val addToCartUseCaseSubscriber = createAddToCartUseCaseSubscriber(onAddToCartUseCaseSuccess)

            addToCartUseCase.execute(requestParams, addToCartUseCaseSubscriber)
        }
    }

    private fun createAddToCartUseCaseRequestParams(): RequestParams {
        return RequestParams.create().also {
            it.putObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, createAddToCartRequestParams())
        }
    }

    private fun createAddToCartRequestParams(): AddToCartRequestParams {
        return AddToCartRequestParams(
                productId = originalProduct.id.toLong(),
                shopId = originalProduct.shop.id,
                quantity = originalProduct.minOrder,
                productName = originalProduct.name,
                category = originalProduct.categoryName,
                price = originalProduct.price,
                userId = userSession.userId
        )
    }

    private fun createAddToCartUseCaseSubscriber(onAddToCartUseCaseSuccess: (addToCartDataModel: AddToCartDataModel?) -> Unit): Subscriber<AddToCartDataModel> {
        return object : Subscriber<AddToCartDataModel>() {
            override fun onNext(addToCartDataModel: AddToCartDataModel?) {
                onAddToCartUseCaseSuccess(addToCartDataModel)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                onAddToCartUseCaseFailed()
            }
        }
    }

    private fun onClickAddToCartSuccess(addToCartDataModel: AddToCartDataModel?) {
        if (isAddToCartStatusOK(addToCartDataModel)) {
            onClickAddToCartStatusOK(addToCartDataModel)
        }
        else {
            onAddToCartUseCaseFailed(addToCartDataModel)
        }
    }

    private fun isAddToCartStatusOK(addToCartDataModel: AddToCartDataModel?): Boolean {
        return addToCartDataModel?.status == AddToCartDataModel.STATUS_OK
                && addToCartDataModel.data.success == 1
    }

    private fun onClickAddToCartStatusOK(addToCartDataModel: AddToCartDataModel?) {
        trackAddToCartStatusOK(addToCartDataModel)

        addToCartEventLiveData.postValue(Event(true))
    }

    private fun trackAddToCartStatusOK(addToCartDataModel: AddToCartDataModel?) {
        val cartId = addToCartDataModel?.data?.cartId ?: ""
        val originalProductAsObjectDataLayerAddToCart = originalProduct.asObjectDataLayerAddToCart(cartId)

        trackingAddToCartEventLiveData.postValue(Event(originalProductAsObjectDataLayerAddToCart))
    }

    private fun onAddToCartUseCaseFailed(addToCartDataModel: AddToCartDataModel? = null) {
        addToCartFailedMessage = addToCartDataModel?.errorMessage?.get(0) ?: ""
        addToCartEventLiveData.postValue(Event(false))
    }

    fun onViewClickBuy() {
        executeAddToCart {
            onClickBuySuccess(it)
        }
    }

    private fun onClickBuySuccess(addToCartDataModel: AddToCartDataModel?) {
        if (isAddToCartStatusOK(addToCartDataModel)) {
            onClickBuyStatusOK(addToCartDataModel)
        }
        else {
            onAddToCartUseCaseFailed(addToCartDataModel)
        }
    }

    private fun onClickBuyStatusOK(addToCartDataModel: AddToCartDataModel?) {
        trackBuyStatusOK(addToCartDataModel)

        routeToCartPageEventLiveData.postValue(Event(true))
    }

    private fun trackBuyStatusOK(addToCartDataModel: AddToCartDataModel?) {
        val cartId = addToCartDataModel?.data?.cartId ?: ""
        val originalProductAsObjectDataLayerAddToCart = originalProduct.asObjectDataLayerAddToCart(cartId)

        trackingBuyEventLiveData.postValue(Event(originalProductAsObjectDataLayerAddToCart))
    }

    fun onReceiveProductCardOptionsWishlistResult(productCardOptionsModel: ProductCardOptionsModel) {
        if (!productCardOptionsModel.wishlistResult.isUserLoggedIn) {
            handleReceiveWishlistResultNonLogin(productCardOptionsModel)
            return
        }

        if (productCardOptionsModel.wishlistResult.isSuccess)
            handleReceiveWishlistResultSuccess(productCardOptionsModel)
        else
            handleReceiveWishlistResultFailed(productCardOptionsModel)
    }

    private fun handleReceiveWishlistResultNonLogin(productCardOptionsModel: ProductCardOptionsModel) {
        trackingWishlistEventLiveData.postValue(Event(productCardOptionsModel.toWishlistTrackingModel()))
        routeToLoginPageEventLiveData.postValue(Event(true))
    }

    private fun ProductCardOptionsModel.toWishlistTrackingModel(): WishlistTrackingModel {
        return WishlistTrackingModel(
                isAddWishlist = !isWishlisted,
                productId = productId,
                isTopAds = isTopAds,
                keyword = similarSearchQuery,
                isUserLoggedIn = wishlistResult.isUserLoggedIn
        )
    }

    private fun handleReceiveWishlistResultSuccess(productCardOptionsModel: ProductCardOptionsModel) {
        postWishlistEvent(true, productCardOptionsModel.wishlistResult.isAddWishlist)
        updateSimilarProductItemWishlistStatus(productCardOptionsModel)
        trackingWishlistEventLiveData.postValue(Event(productCardOptionsModel.toWishlistTrackingModel()))
    }

    private fun postWishlistEvent(isSuccess: Boolean, isAddWishlist: Boolean) {
        if (isAddWishlist) {
            addWishlistEventLiveData.postValue(Event(isSuccess))
        }
        else {
            removeWishlistEventLiveData.postValue(Event(isSuccess))
        }
    }

    private fun updateSimilarProductItemWishlistStatus(productCardOptionsModel: ProductCardOptionsModel) {
        val similarProductItem = getSimilarProductItem(productCardOptionsModel.productId)
        val isWishlistedFromProductCardOptions = productCardOptionsModel.wishlistResult.isAddWishlist
        similarProductItem.isWishlisted = isWishlistedFromProductCardOptions
    }

    private fun getSimilarProductItem(productId: String): Product {
        return similarSearchViewModelList.find { it is Product && it.id == productId } as Product
    }

    private fun handleReceiveWishlistResultFailed(productCardOptionsModel: ProductCardOptionsModel) {
        postWishlistEvent(false, productCardOptionsModel.wishlistResult.isAddWishlist)
    }

    fun getUpdateWishlistOriginalProductEventLiveData(): LiveData<Event<Boolean>> {
        return updateWishlistOriginalProductEventLiveData
    }

    fun getAddWishlistEventLiveData(): LiveData<Event<Boolean>> {
        return addWishlistEventLiveData
    }

    fun getRemoveWishlistEventLiveData(): LiveData<Event<Boolean>> {
        return removeWishlistEventLiveData
    }

    fun onViewUpdateProductWishlistStatus(productId: String?, isWishlisted: Boolean) {
        postUpdateWishlistOriginalProductEvent(productId, isWishlisted)
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

    fun getTrackingAddToCartEventLiveData(): LiveData<Event<Any>> {
        return trackingAddToCartEventLiveData
    }

    fun getTrackingBuyEventLiveData(): LiveData<Event<Any>> {
        return trackingBuyEventLiveData
    }

    fun getAddToCartEventLiveData(): LiveData<Event<Boolean>> {
        return addToCartEventLiveData
    }

    fun getRouteToCartPageEventLiveData(): LiveData<Event<Boolean>> {
        return routeToCartPageEventLiveData
    }

    fun getAddToCartFailedMessage(): String {
        return addToCartFailedMessage
    }
}