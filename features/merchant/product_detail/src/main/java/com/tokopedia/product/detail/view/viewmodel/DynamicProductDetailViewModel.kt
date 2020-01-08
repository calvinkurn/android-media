package com.tokopedia.product.detail.view.viewmodel

import android.content.Intent
import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.affiliatecommon.domain.TrackAffiliateUseCase
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.debugTrace
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product.detail.data.model.*
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductLastSeenDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductOpenShopDataModel
import com.tokopedia.product.detail.data.model.financing.FinancingDataResponse
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.getCurrencyFormatted
import com.tokopedia.product.detail.data.util.origin
import com.tokopedia.product.detail.updatecartcounter.interactor.UpdateCartCounterUseCase
import com.tokopedia.product.detail.usecase.*
import com.tokopedia.product.detail.view.util.DynamicProductDetailDispatcherProvider
import com.tokopedia.purchase_platform.common.data.model.request.helpticket.SubmitHelpTicketRequest
import com.tokopedia.purchase_platform.common.sharedata.helpticket.SubmitTicketResult
import com.tokopedia.purchase_platform.common.usecase.SubmitHelpTicketUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.Observer
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

open class DynamicProductDetailViewModel @Inject constructor(private val dispatcher: DynamicProductDetailDispatcherProvider,
                                                             private val stickyLoginUseCase: StickyLoginUseCase,
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
                                                             private val userSessionInterface: UserSessionInterface) : BaseViewModel(dispatcher.ui()) {

    private val _productLayout = MutableLiveData<Result<List<DynamicPdpDataModel>>>()
    val productLayout: LiveData<Result<List<DynamicPdpDataModel>>>
        get() = _productLayout

    private val _p2ShopDataResp = MutableLiveData<ProductInfoP2ShopData>()
    val p2ShopDataResp: LiveData<ProductInfoP2ShopData>
        get() = _p2ShopDataResp

    private val _p2Login = MutableLiveData<ProductInfoP2Login>()
    val p2Login: LiveData<ProductInfoP2Login>
        get() = _p2Login

    private val _p2General = MutableLiveData<ProductInfoP2General>()
    val p2General: LiveData<ProductInfoP2General>
        get() = _p2General

    private val _productInfoP3resp = MutableLiveData<ProductInfoP3>()
    val productInfoP3resp: LiveData<ProductInfoP3>
        get() = _productInfoP3resp

    private val _loadTopAdsProduct = MutableLiveData<Result<List<RecommendationWidget>>>()
    val loadTopAdsProduct: LiveData<Result<List<RecommendationWidget>>>
        get() = _loadTopAdsProduct

    private val _moveToWarehouseResult = MutableLiveData<Result<Boolean>>()
    val moveToWarehouseResult: LiveData<Result<Boolean>>
        get() = _moveToWarehouseResult

    private val _moveToEtalaseResult = MutableLiveData<Result<Boolean>>()
    val moveToEtalaseResult: LiveData<Result<Boolean>>
        get() = _moveToEtalaseResult

    private val _toggleFavoriteResult = MutableLiveData<Result<Boolean>>()
    val toggleFavoriteResult: LiveData<Result<Boolean>>
        get() = _toggleFavoriteResult

    var multiOrigin: WarehouseInfo = WarehouseInfo()
    var getDynamicProductInfoP1: DynamicProductInfoP1? = null
    var shopInfo: ShopInfo? = null
    var installmentData: FinancingDataResponse? = null
    var tradeInParams: TradeInParams = TradeInParams()

    private var submitTicketSubscription: Subscription? = null
    private var updateCartCounterSubscription: Subscription? = null

    fun isShopOwner(shopId: Int): Boolean = userSessionInterface.shopId.toIntOrNull() == shopId
    val isUserSessionActive: Boolean
        get() = userSessionInterface.isLoggedIn
    val userId: String
        get() = userSessionInterface.userId
    val isUserHasShop: Boolean
        get() = userSessionInterface.hasShop()
    val deviceId: String
        get() = userSessionInterface.deviceId

    override fun clear() {
        super.clear()
        stickyLoginUseCase.cancelJobs()
        getPdpLayoutUseCase.cancelJobs()
        getProductInfoP2ShopUseCase.cancelJobs()
        getProductInfoP2LoginUseCase.cancelJobs()
        getProductInfoP2GeneralUseCase.cancelJobs()
        getProductInfoP3UseCase.cancelJobs()
        toggleFavoriteUseCase.cancelJobs()
        trackAffiliateUseCase.cancelJobs()
        moveProductToWarehouseUseCase.cancelJobs()
        moveProductToEtalaseUseCase.cancelJobs()
        getRecommendationUseCase.unsubscribe()
        removeWishlistUseCase.unsubscribe()
        submitTicketSubscription?.unsubscribe()
        updateCartCounterSubscription?.unsubscribe()
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
            getPdpLayout(productParams.productId ?: "", productParams.shopDomain
                    ?: "", productParams.productName ?: "", forceRefresh).also {
                addStaticComponent(it)
                getDynamicProductInfoP1 = it.layoutData
                //Remove any unused component based on P1 / PdpLayout
                removeDynamicComponent(it.listOfLayout)
                //Render initial data first
                _productLayout.value = Success(it.listOfLayout)
            }
            // Then update the following, it will not throw anything when error
            getProductP2(forceRefresh, productParams.warehouseId)
            getProductP3(productParams.shopDomain, forceRefresh)
        }) {
            _productLayout.value = Fail(it)
        }
    }

    private fun addStaticComponent(it: ProductDetailDataModel) {
        if (isUserSessionActive && !isUserHasShop) {
            it.listOfLayout.add(ProductOpenShopDataModel())
        }
        it.listOfLayout.add(ProductLastSeenDataModel())
    }

    private fun createTradeinParam(productInfoP1: DynamicProductInfoP1?, deviceId: String?): TradeInParams {
        productInfoP1?.let {
            tradeInParams.categoryId = it.basic.category.id.toIntOrZero()
            tradeInParams.deviceId = deviceId ?: ""
            tradeInParams.userId = if (userId.isNotEmpty())
                userId.toIntOrZero()
            else
                0
            tradeInParams.setPrice(it.data.price.value)
            tradeInParams.productId = it.basic.getProductId()
            tradeInParams.shopId = it.basic.getShopId()
            tradeInParams.productName = it.getProductName

            tradeInParams.isPreorder = it.data.preOrder.isPreOrderActive()
            tradeInParams.isOnCampaign = it.data.campaign.isActive
        }
        return tradeInParams
    }


    private suspend fun getProductP2(forceRefresh: Boolean, warehouseId: String?) {
        getDynamicProductInfoP1?.let {
            val p2ShopDeferred = getProductInfoP2ShopAsync(it.basic.getShopId(),
                    it.basic.productID, warehouseId ?: "", forceRefresh)

            val p2LoginDeferred: Deferred<ProductInfoP2Login>? = if (isUserSessionActive) {
                getProductInfoP2LoginAsync(it.basic.getShopId(),
                        it.basic.getProductId())
            } else null

            val userIdInt = userId.toIntOrNull() ?: 0
            val categoryId = it.basic.category.id.toIntOrNull() ?: 0

            val p2GeneralAsync: Deferred<ProductInfoP2General> = getProductInfoP2GeneralAsync(it.basic.getShopId(),
                    it.basic.getProductId(), it.data.price.value,
                    it.basic.condition,
                    it.getProductName,
                    categoryId, it.basic.catalogID, userIdInt, it.basic.minOrder, forceRefresh)

            _p2ShopDataResp.value = p2ShopDeferred.await().also {
                shopInfo = it.shopInfo
                val tradeInResponse = it.tradeinResponse?.validateTradeInPDP ?: ValidateTradeInPDP()
                tradeInParams.isEligible = if (tradeInResponse.isEligible) 1 else 0
                tradeInParams.usedPrice = tradeInResponse.usedPrice
                tradeInParams.remainingPrice = tradeInResponse.remainingPrice
                tradeInParams.isUseKyc = if (tradeInResponse.useKyc) 1 else 0
            }

            _p2General.value = p2GeneralAsync.await()
            p2LoginDeferred?.let {
                _p2Login.value = it.await()
            }
        }
    }

    private suspend fun getProductP3(shopDomain: String?, forceRefresh: Boolean) {
        getDynamicProductInfoP1?.run {
            _p2ShopDataResp.value?.let { p2Shop ->
                multiOrigin = p2Shop.nearestWarehouse.warehouseInfo
                val domain = shopDomain ?: p2Shop.shopInfo?.shopCore?.domain
                ?: return@run

                if (isUserSessionActive) {
                    val origin = if (multiOrigin.isFulfillment) multiOrigin.origin else null
                    _productInfoP3resp.value = getProductInfoP3(basic.getWeightUnit(), domain, forceRefresh,
                            shouldShowCod, origin)
                }
            }
        }
    }

    private fun removeDynamicComponent(initialLayoutData: MutableList<DynamicPdpDataModel>) {
        val isTradein = getDynamicProductInfoP1?.data?.isTradeIn == true
        val hasWholesale = getDynamicProductInfoP1?.data?.hasWholesale == true
        val isOfficialStore = getDynamicProductInfoP1?.data?.isOS == true

        val removedData = initialLayoutData.map {
            if (!isTradein && it.name() == ProductDetailConstant.TRADE_IN) {
                it
            } else if (!hasWholesale && it.name() == ProductDetailConstant.PRODUCT_WHOLESALE_INFO) {
                it
            } else if (!isOfficialStore && it.name() == ProductDetailConstant.VALUE_PROP) {
                it
            } else if (it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO && !isUserSessionActive) {
                it
            } else {
                null
            }
        }

        if (removedData.isNotEmpty())
            initialLayoutData.removeAll(removedData)
    }

    fun toggleFavorite(shopID: String) {
        launchCatchError(block = {
            toggleFavoriteUseCase.createRequestParam(shopID)
            _toggleFavoriteResult.value = Success(toggleFavoriteUseCase.executeOnBackground().followShop.isSuccess)
        }) {
            _toggleFavoriteResult.value = Fail(it)
        }
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
        if (intent == null || productId == null) return
        val variants = mapSelectedProductVariants(userInputVariant)
        val productImageUrl = productInfo?.data?.getProductImageUrl() ?: ""
        val productName = productInfo?.getProductName ?: ""
        val productPrice = productInfo?.data?.price?.value?.getCurrencyFormatted() ?: ""
        val productUrl = productInfo?.basic?.url ?: ""
        val productFsIsActive = productInfo?.data?.getFsProductIsActive() ?: false
        val productFsImageUrl = productInfo?.data?.getFsProductImageUrl() ?: ""
        val productColorVariant = variants?.get("colour")?.get("value") ?: ""
        val productColorHexVariant = variants?.get("colour")?.get("hex") ?: ""
        val productSizeVariant = variants?.get("size")?.get("value") ?: ""
        val productColorVariantId = variants?.get("colour")?.get("id") ?: ""
        val productSizeVariantId = variants?.get("size")?.get("id") ?: ""

        val productPreviews = listOf(
                ProductPreview(
                        productId,
                        productImageUrl,
                        productName,
                        productPrice,
                        productColorVariantId,
                        productColorVariant,
                        productColorHexVariant,
                        productSizeVariantId,
                        productSizeVariant,
                        productUrl,
                        productFsIsActive,
                        productFsImageUrl
                )
        )
        val stringProductPreviews = CommonUtil.toJson(productPreviews)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }

    fun loadRecommendation() {
        launch {
            if (GlobalConfig.isCustomerApp()) {
                try {
                    withContext(dispatcher.io()) {
                        val recomData = getRecommendationUseCase.createObservable(getRecommendationUseCase.getRecomParams(
                                pageNumber = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
                                pageName = ProductDetailConstant.DEFAULT_PAGE_NAME,
                                productIds = arrayListOf(getDynamicProductInfoP1?.basic?.productID
                                        ?: "")
                        )).toBlocking()
                        _loadTopAdsProduct.postValue(Success(recomData.first() ?: emptyList()))
                    }
                } catch (e: Throwable) {
                    _loadTopAdsProduct.value = Fail(e)
                }
            }
        }
    }

    fun moveProductToWareHouse(productId: String) {
        launchCatchError(block = {
            moveProductToWarehouseUseCase.createParams(productId, userId, deviceId)
            _moveToWarehouseResult.value = Success(moveProductToWarehouseUseCase.executeOnBackground().getIsSuccess())
        }) {
            _moveToWarehouseResult.value = Fail(it)
        }
    }

    fun moveProductToEtalase(productId: String, selectedEtalaseId: String, selectedEtalaseName: String) {
        launchCatchError(block = {
            moveProductToEtalaseUseCase.createParams(productId, selectedEtalaseId, selectedEtalaseName, userId, deviceId)
            _moveToEtalaseResult.value = Success(moveProductToEtalaseUseCase.executeOnBackground().getIsSuccess())
        }) {
            _moveToEtalaseResult.value = Fail(it)
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

            }
        })
    }

    fun updateCartCounerUseCase(onSuccessRequest: (count: Int) -> Unit) {
        updateCartCounterSubscription = updateCartCounterUseCase.createObservable(RequestParams.EMPTY)
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
            getProductInfoP2ShopUseCase.requestParams = GetProductInfoP2ShopUseCase.createParams(shopId, productId, warehouseId, forceRefresh, createTradeinParam(getDynamicProductInfoP1, deviceId))
            getProductInfoP2ShopUseCase.executeOnBackground()
        }
    }

    private fun getProductInfoP2LoginAsync(shopId: Int, productId: Int): Deferred<ProductInfoP2Login> {
        return async {
            getProductInfoP2LoginUseCase.requestParams = GetProductInfoP2LoginUseCase.createParams(shopId, productId)
            getProductInfoP2LoginUseCase.executeOnBackground()
        }
    }

    private fun getProductInfoP2GeneralAsync(shopId: Int, productId: Int, productPrice: Int,
                                             condition: String, productTitle: String, categoryId: Int, catalogId: String,
                                             userId: Int, minOrder: Int,
                                             forceRefresh: Boolean = false): Deferred<ProductInfoP2General> {
        return async {
            getProductInfoP2GeneralUseCase.requestParams = GetProductInfoP2GeneralUseCase.createParams(shopId, productId, productPrice, condition, productTitle, categoryId, catalogId, userId, forceRefresh, minOrder)
            getProductInfoP2GeneralUseCase.executeOnBackground()
        }
    }

    private suspend fun getProductInfoP3(weight: Float, shopDomain: String,
                                         forceRefresh: Boolean, needRequestCod: Boolean, origin: String?): ProductInfoP3 {
        getProductInfoP3UseCase.createRequestParams(weight, shopDomain, needRequestCod, forceRefresh, origin)
        return getProductInfoP3UseCase.executeOnBackground()
    }

    private suspend fun getPdpLayout(productId: String, shopDomain: String, productKey: String, forceRefresh: Boolean): ProductDetailDataModel {
        getPdpLayoutUseCase.requestParams = GetPdpLayoutUseCase.createParams(productId, shopDomain, productKey)
        getPdpLayoutUseCase.isUserActive = isUserSessionActive
        getPdpLayoutUseCase.isUserHasShop = isUserHasShop
        getPdpLayoutUseCase.forceRefresh = forceRefresh
        return getPdpLayoutUseCase.executeOnBackground()
    }
}