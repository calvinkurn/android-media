package com.tokopedia.product.detail.view.viewmodel

import android.content.Intent
import androidx.collection.ArrayMap
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.affiliatecommon.domain.TrackAffiliateUseCase
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.debugTrace
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductDetailLayout
import com.tokopedia.product.detail.common.data.model.product.ProductInfo
import com.tokopedia.product.detail.common.data.model.product.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product.detail.data.model.ProductInfoP2General
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.ProductInfoP2ShopData
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.datamodel.DynamicPDPDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductLastSeenDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductOpenShopDataModel
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.data.util.origin
import com.tokopedia.product.detail.updatecartcounter.interactor.UpdateCartCounterUseCase
import com.tokopedia.product.detail.usecase.*
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.transaction.common.data.ticket.SubmitHelpTicketRequest
import com.tokopedia.transaction.common.sharedata.ticket.SubmitTicketResult
import com.tokopedia.transaction.common.usecase.SubmitHelpTicketUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.*
import rx.Observer
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Named

class DynamicProductDetailViewModel @Inject constructor(@Named("Main")
                                                        private val dispatcher: CoroutineDispatcher,
                                                        private val stickyLoginUseCase: StickyLoginUseCase,
                                                        private val getProductInfoP1UseCase: GetProductInfoP1UseCase,
                                                        private val getPdpLayoutUseCase: GetPdpLayoutUseCase,
                                                        private val getProductInfoP2ShopUseCase: GetProductInfoP2ShopUseCase,
                                                        private val getProductInfoP2LoginUseCase: GetProductInfoP2LoginUseCase,
                                                        private val getProductInfoP2GeneralUseCase: GetProductInfoP2GeneralUseCase,
                                                        private val getProductInfoP3UseCase: GetProductInfoP3UseCase,
                                                        private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
                                                        private val removeWishlistUseCase: RemoveWishListUseCase,
                                                        private val addWishListUseCase: AddWishListUseCase,
                                                        private val getRecommendationUseCase: GetRecommendationUseCase,
                                                        private val moveProductToWarehouseUseCase: MoveProductToWarehouseUseCase,
                                                        private val moveProductToEtalaseUseCase: MoveProductToEtalaseUseCase,
                                                        private val trackAffiliateUseCase: TrackAffiliateUseCase,
                                                        private val submitHelpTicketUseCase: SubmitHelpTicketUseCase,
                                                        private val updateCartCounterUseCase: UpdateCartCounterUseCase,
                                                        private val userSessionInterface: UserSessionInterface) : BaseViewModel(dispatcher) {

    val productInfoP1 = MutableLiveData<Result<ProductInfoP1>>()
    val productLayout = MutableLiveData<Result<List<DynamicPDPDataModel>>>()
    val p2ShopDataResp = MutableLiveData<ProductInfoP2ShopData>()
    val p2Login = MutableLiveData<ProductInfoP2Login>()
    val p2General = MutableLiveData<ProductInfoP2General>()
    val productInfoP3resp = MutableLiveData<ProductInfoP3>()
    val loadTopAdsProduct = MutableLiveData<Result<List<RecommendationWidget>>>()
    val moveToWarehouseResult = MutableLiveData<Result<Boolean>>()
    val moveToEtalaseResult = MutableLiveData<Result<Boolean>>()

    var multiOrigin: WarehouseInfo = WarehouseInfo()
    val dynamicProductInfoP1 = MutableLiveData<Result<DynamicProductInfoP1>>()

    var getDynamicProductInfoP1: DynamicProductInfoP1? = null
    var shopInfo: ShopInfo? = null

    private var submitTicketSubscription: Subscription? = null

    fun isUserSessionActive(): Boolean = userSessionInterface.isLoggedIn
    fun isShopOwner(shopId: Int): Boolean = userSessionInterface.shopId.toIntOrNull() == shopId

    val userId: String
        get() = userSessionInterface.userId

    val isUserHasShop: Boolean
        get() = userSessionInterface.hasShop()

    val deviceId: String
        get() = userSessionInterface.deviceId

    override fun clear() {
        super.clear()
        stickyLoginUseCase.cancelJobs()
        getProductInfoP1UseCase.cancelJobs()
        getPdpLayoutUseCase.cancelJobs()
        getProductInfoP2ShopUseCase.cancelJobs()
        getProductInfoP2LoginUseCase.cancelJobs()
        getProductInfoP2GeneralUseCase.cancelJobs()
        getProductInfoP3UseCase.cancelJobs()
        toggleFavoriteUseCase.cancelJobs()
        toggleFavoriteUseCase.cancelJobs()
        trackAffiliateUseCase.cancelJobs()
        moveProductToWarehouseUseCase.cancelJobs()
        moveProductToEtalaseUseCase.cancelJobs()
        getRecommendationUseCase.unsubscribe()
        removeWishlistUseCase.unsubscribe()
        submitTicketSubscription?.unsubscribe()
        updateCartCounterUseCase.unsubscribe()
    }

    fun getStickyLoginContent(onSuccess: (StickyLoginTickerPojo.TickerDetail) -> Unit, onError: ((Throwable) -> Unit)?) {
        stickyLoginUseCase.setParams(StickyLoginConstant.Page.PDP)
        stickyLoginUseCase.execute(
                onSuccess = {
                    if (it.response.tickers.isNotEmpty()) {
                        for (tickerDetail in it.response.tickers) {
                            if (tickerDetail.layout == StickyLoginConstant.LAYOUT_FLOATING) {
                                onSuccess.invoke(tickerDetail)
                                return@execute
                            }
                        }
                        onError?.invoke(Throwable(""))
                    } else {
                        onError?.invoke(Throwable(""))
                    }
                },
                onError = {
                    onError?.invoke(it)
                }
        )
    }

    fun getProductP1(productParams: ProductParams, forceRefresh: Boolean = false) {
        launchCatchError(block = {
            var needRequestCod = false

            val pdpLayout = getPdpLayout(productParams.productId ?: "")
            val initialLayoutData = DynamicProductDetailMapper.mapIntoVisitable(pdpLayout.data.components)
            if (isUserSessionActive() || isUserHasShop) {
                initialLayoutData.add(ProductOpenShopDataModel())
            }
            initialLayoutData.add(ProductLastSeenDataModel())
            val productInfo = getPdpData(productParams.productId?.toInt() ?: 0)

            productLayout.value = Success(initialLayoutData)
            val productInfoP1 = Success(DynamicProductDetailMapper.mapToDynamicProductDetailP1(pdpLayout.data))
            getDynamicProductInfoP1 = productInfoP1.data
            dynamicProductInfoP1.value = productInfoP1

            val p2ShopDeferred = getProductInfoP2ShopAsync(productInfo.productInfo.basic.shopID,
                    productInfo.productInfo.basic.id.toString(),
                    "", false)

            val p2LoginDeferred: Deferred<ProductInfoP2Login>? = if (isUserSessionActive()) {
                getProductInfoP2LoginAsync(productInfo.productInfo.basic.shopID,
                        productInfo.productInfo.basic.id)
            } else null

            val userIdInt = userId.toIntOrNull() ?: 0
            val categoryId = productInfo.productInfo.category.id.toIntOrNull() ?: 0

            val p2GeneralAsync: Deferred<ProductInfoP2General> = getProductInfoP2GeneralAsync(productInfo.productInfo.basic.shopID,
                    productInfo.productInfo.basic.id, productInfo.productInfo.basic.price.toInt(),
                    productInfo.productInfo.basic.condition, productInfo.productInfo.basic.name,
                    categoryId, productInfo.productInfo.basic.catalogID.toString(), userIdInt)

            shopInfo = p2ShopDeferred.await().shopInfo
            p2ShopDataResp.value = p2ShopDeferred.await()
            p2General.value = p2GeneralAsync.await()
            p2LoginDeferred?.let {
                p2Login.value = it.await()
            }

            p2ShopDataResp.value?.let {
                multiOrigin = it.nearestWarehouse.warehouseInfo
                val domain = productParams.shopDomain ?: it.shopInfo?.shopCore?.domain
                ?: return@launchCatchError

                if (isUserSessionActive())
                    productInfoP3resp.value = getProductInfoP3(productInfo.productInfo, domain, true,
                            productInfo.productInfo.shouldShowCod, if (multiOrigin.isFulfillment) multiOrigin.origin else null)
            }

        }) {
            productLayout.value = Fail(it)
        }
    }

    fun toggleFavorite(shopID: String, onSuccess: (Boolean) -> Unit, onError: (Throwable) -> Unit) {
        launchCatchError(block = {
            toggleFavoriteUseCase.createRequestParam(shopID)
            val result = toggleFavoriteUseCase.executeOnBackground()

            onSuccess(result.followShop.isSuccess)
        }) { onError(it) }
    }

    fun removeWishList(productId: String,
                       onSuccessRemoveWishlist: ((productId: String?) -> Unit)?,
                       onErrorRemoveWishList: ((errorMessage: String?) -> Unit)?) {
        removeWishlistUseCase.createObservable(productId,
                userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                // no op
            }

            override fun onSuccessAddWishlist(productId: String?) {
                // no op
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                onErrorRemoveWishList?.invoke(errorMessage)
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                onSuccessRemoveWishlist?.invoke(productId)
            }
        })
    }

    fun addWishList(productId: String,
                    onErrorAddWishList: ((errorMessage: String?) -> Unit)?,
                    onSuccessAddWishlist: ((productId: String?) -> Unit)?) {
        addWishListUseCase.createObservable(productId,
                userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                onErrorAddWishList?.invoke(errorMessage)
            }

            override fun onSuccessAddWishlist(productId: String?) {
                onSuccessAddWishlist?.invoke(productId)
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                // no op
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                // no op
            }
        })
    }

    fun putChatProductInfoTo(
            intent: Intent?,
            productId: String?,
            productInfo: DynamicProductInfoP1?,
            userInputVariant: String?
    ) {
        if (intent == null) return
        val variants = mapSelectedProductVariants(userInputVariant)
        val productImageUrl = productInfo?.data?.getProductImageUrl()
        val productName = productInfo?.basic?.name
        val productPrice = productInfo?.data?.price?.value?.getCurrencyFormatted()
        val productUrl = productInfo?.basic?.url
        val productFsIsActive = productInfo?.data?.getFsProductIsActive()
        val productFsImageUrl = productInfo?.data?.getFsProductImageUrl()
        val productColorVariant = variants?.get("colour")?.get("value")
        val productColorHexVariant = variants?.get("colour")?.get("hex")
        val productSizeVariant = variants?.get("size")?.get("value")
        with(intent) {
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_ID, productId)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_IMAGE_URL, productImageUrl)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_NAME, productName)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_PRICE, productPrice)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_URL, productUrl)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_COLOR_VARIANT, productColorVariant)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_HEX_COLOR_VARIANT, productColorHexVariant)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_SIZE_VARIANT, productSizeVariant)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_FS_IS_ACTIVE, productFsIsActive)
            putExtra(ApplinkConst.Chat.PRODUCT_PREVIEW_FS_IMAGE_URL, productFsImageUrl)
        }
    }

    fun getImageUriPaths(): ArrayList<String> {
        val mediaData = (dynamicProductInfoP1.value) as? Success ?: return arrayListOf()
        return ArrayList(mediaData.data.data.media.map {
            if (it.type == "image") {
                it.uRLOriginal
            } else {
                it.uRLThumbnail
            }
        })
    }

    fun loadRecommendation() {
        val product = (productInfoP1.value ?: return) as? Success ?: return
        launch {
            if (GlobalConfig.isCustomerApp()) {
                try {
                    withContext(Dispatchers.IO) {
                        val recomData = getRecommendationUseCase.createObservable(getRecommendationUseCase.getRecomParams(
                                pageNumber = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
                                pageName = ProductDetailConstant.DEFAULT_PAGE_NAME,
                                productIds = arrayListOf(getDynamicProductInfoP1?.basic?.productID
                                        ?: "")
                        )).toBlocking()
                        loadTopAdsProduct.postValue(Success(recomData.first() ?: emptyList()))
                    }
                } catch (e: Throwable) {
                    loadTopAdsProduct.value = Fail(e)
                }
            }
        }
    }

    fun moveProductToWareHouse(productId: String) {
        launchCatchError(block = {
            moveProductToWarehouseUseCase.createParams(productId, userId, deviceId)
            moveToWarehouseResult.value = Success(moveProductToWarehouseUseCase.executeOnBackground().getIsSuccess())
        }) {
            moveToWarehouseResult.value = Fail(it)
        }
    }

    fun moveProductToEtalase(productId: String, selectedEtalaseId: String, selectedEtalaseName: String) {
        launchCatchError(block = {
            moveProductToEtalaseUseCase.createParams(productId, selectedEtalaseId, selectedEtalaseName, userId, deviceId)
            moveToEtalaseResult.value = Success(moveProductToEtalaseUseCase.executeOnBackground().getIsSuccess())
        }) {
            moveToEtalaseResult.value = Fail(it)
        }
    }

    fun hitAffiliateTracker(affiliateUniqueString: String, deviceId: String) {
        trackAffiliateUseCase.params = TrackAffiliateUseCase.createParams(affiliateUniqueString, deviceId)
        trackAffiliateUseCase.execute({
            //no op
        }) {
            it.debugTrace()
        }
    }

    fun hitSubmitTicket(addToCartDataModel: AddToCartDataModel, onErrorSubmitHelpTicket: (Throwable?) -> Unit, onNextSubmitHelpTicket: (SubmitTicketResult) -> Unit) {
        val requestParams = RequestParams.create()
        val submitHelpTicketRequest = SubmitHelpTicketRequest()
        submitHelpTicketRequest.apply {
            apiJsonResponse = addToCartDataModel.responseJson
            errorMessage = addToCartDataModel.errorReporter.texts.submitDescription
            if (addToCartDataModel.errorMessage.isNotEmpty()) {
                headerMessage = addToCartDataModel.errorMessage[0]
            }
            page = SubmitHelpTicketUseCase.PAGE_ATC
            requestUrl = SubmitHelpTicketUseCase.GQL_REQUEST_URL
        }
        requestParams.putObject(SubmitHelpTicketUseCase.PARAM, submitHelpTicketRequest)
        submitTicketSubscription = submitHelpTicketUseCase.createObservable(requestParams).subscribe(object : Observer<SubmitTicketResult> {
            override fun onError(e: Throwable?) {
                e?.printStackTrace()
                onErrorSubmitHelpTicket(e)
            }

            override fun onNext(t: SubmitTicketResult) {
                onNextSubmitHelpTicket(t)
            }

            override fun onCompleted() {
                submitTicketSubscription = null
            }
        })
    }

    fun updateCartCounerUseCase(onSuccessRequest: (count: Int) -> Unit) {
        updateCartCounterUseCase.createObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<Int>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onNext(count: Int) {
                        onSuccessRequest(count)
                    }
                })
    }


    fun generateVariantString(): String {
        return try {
            p2General.value?.variantResp?.variant?.map { it.name }?.joinToString(separator = ", ")
                    ?: ""
        } catch (e: Throwable) {
            ""
        }
    }


    fun cancelWarehouseUseCase() {
        moveProductToWarehouseUseCase.cancelJobs()
    }

    fun cancelEtalaseUseCase() {
        moveProductToEtalaseUseCase.cancelJobs()
    }

    private fun mapSelectedProductVariants(userInputVariant: String?): ArrayMap<String, ArrayMap<String, String>>? {
        return getProductVariant()?.mapSelectedProductVariants(userInputVariant)
    }

    private fun getProductVariant(): ProductVariant? {
        return p2General.value?.variantResp
    }

    private fun getProductInfoP2ShopAsync(shopId: Int, productId: String,
                                          warehouseId: String,
                                          forceRefresh: Boolean = false): Deferred<ProductInfoP2ShopData> {
        return async {
            getProductInfoP2ShopUseCase.createRequestParams(shopId, productId, warehouseId, true)
            getProductInfoP2ShopUseCase.executeOnBackground()
        }

    }

    private fun getProductInfoP2LoginAsync(shopId: Int, productId: Int): Deferred<ProductInfoP2Login> {
        return async {
            getProductInfoP2LoginUseCase.createRequestParams(shopId, productId)
            getProductInfoP2LoginUseCase.executeOnBackground()
        }

    }

    private fun getProductInfoP2GeneralAsync(shopId: Int, productId: Int, productPrice: Int,
                                             condition: String, productTitle: String, categoryId: Int, catalogId: String,
                                             userId: Int,
                                             forceRefresh: Boolean = false): Deferred<ProductInfoP2General> {
        return async {
            getProductInfoP2GeneralUseCase.createRequestParams(shopId, productId, productPrice, condition, productTitle, categoryId, catalogId, userId, true)
            getProductInfoP2GeneralUseCase.executeOnBackground()
        }
    }

    private suspend fun getPdpData(productId: Int): ProductInfoP1 {
        getProductInfoP1UseCase.params = GetProductInfoP1UseCase.createParams(productId, "", "")
        val pdpData = getProductInfoP1UseCase.executeOnBackground().data
        return ProductInfoP1(pdpData ?: ProductInfo())
    }

    private suspend fun getProductInfoP3(productInfo: ProductInfo, shopDomain: String,
                                         forceRefresh: Boolean, needRequestCod: Boolean, origin: String?): ProductInfoP3 {
        getProductInfoP3UseCase.createRequestParams(productInfo, shopDomain, needRequestCod, origin
                ?: "", true)
        return getProductInfoP3UseCase.executeOnBackground()
    }

    private suspend fun getPdpLayout(productId: String): ProductDetailLayout {
        getPdpLayoutUseCase.requestParams = GetPdpLayoutUseCase.createParams(productId)
        getPdpLayoutUseCase.isFromCacheFirst = false
        return getPdpLayoutUseCase.executeOnBackground()
    }
}