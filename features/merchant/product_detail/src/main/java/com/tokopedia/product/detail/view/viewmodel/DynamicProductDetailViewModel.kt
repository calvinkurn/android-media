package com.tokopedia.product.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.affiliatecommon.domain.TrackAffiliateUseCase
import com.tokopedia.atc_common.data.model.request.AddToCartOccRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.ProductInfoP3
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductLastSeenDataModel
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.product.detail.data.model.tradein.ValidateTradeIn
import com.tokopedia.product.detail.data.util.DynamicProductDetailTalkLastAction
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant.ADS_COUNT
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DIMEN_ID
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PAGE_SOURCE
import com.tokopedia.product.detail.usecase.*
import com.tokopedia.product.detail.view.util.DynamicProductDetailDispatcherProvider
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.product.detail.view.util.asThrowable
import com.tokopedia.purchase_platform.common.feature.helpticket.data.request.SubmitHelpTicketRequest
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.variant_common.model.ProductVariantCommon
import com.tokopedia.variant_common.model.VariantCategory
import com.tokopedia.variant_common.model.WarehouseInfo
import com.tokopedia.variant_common.util.VariantCommonMapper
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
import timber.log.Timber
import javax.inject.Inject

open class DynamicProductDetailViewModel @Inject constructor(private val dispatcher: DynamicProductDetailDispatcherProvider,
                                                             private val stickyLoginUseCase: StickyLoginUseCase,
                                                             private val getPdpLayoutUseCase: GetPdpLayoutUseCase,
                                                             private val getProductInfoP2LoginUseCase: GetProductInfoP2LoginUseCase,
                                                             private val getProductInfoP2OtherUseCase: GetProductInfoP2OtherUseCase,
                                                             private val getProductInfoP2DataUseCase: GetProductInfoP2DataUseCase,
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
                                                             private val addToCartUseCase: AddToCartUseCase,
                                                             private val addToCartOcsUseCase: AddToCartOcsUseCase,
                                                             private val addToCartOccUseCase: AddToCartOccUseCase,
                                                             private val toggleNotifyMeUseCase: ToggleNotifyMeUseCase,
                                                             private val discussionMostHelpfulUseCase: DiscussionMostHelpfulUseCase,
                                                             private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
                                                             val userSessionInterface: UserSessionInterface) : BaseViewModel(dispatcher.ui()) {

    private val _productLayout = MutableLiveData<Result<List<DynamicPdpDataModel>>>()
    val productLayout: LiveData<Result<List<DynamicPdpDataModel>>>
        get() = _productLayout

    private val _p2Login = MutableLiveData<ProductInfoP2Login>()
    val p2Login: LiveData<ProductInfoP2Login>
        get() = _p2Login

    private val _p2Data = MutableLiveData<ProductInfoP2UiData>()
    val p2Data: LiveData<ProductInfoP2UiData>
        get() = _p2Data

    private val _p2Other = MutableLiveData<ProductInfoP2Other>()
    val p2Other: LiveData<ProductInfoP2Other>
        get() = _p2Other

    private val _productInfoP3 = MediatorLiveData<ProductInfoP3>()
    val productInfoP3: LiveData<ProductInfoP3>
        get() = _productInfoP3

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

    private val _updatedImageVariant = MutableLiveData<Pair<List<VariantCategory>?, List<Media>>>()
    val updatedImageVariant: LiveData<Pair<List<VariantCategory>?, List<Media>>>
        get() = _updatedImageVariant

    private val _addToCartLiveData = MutableLiveData<Result<AddToCartDataModel>>()
    val addToCartLiveData: LiveData<Result<AddToCartDataModel>>
        get() = _addToCartLiveData

    private val _initialVariantData = MutableLiveData<List<VariantCategory>?>()
    val initialVariantData: LiveData<List<VariantCategory>?>
        get() = _initialVariantData

    private val _onVariantClickedData = MutableLiveData<List<VariantCategory>?>()
    val onVariantClickedData: LiveData<List<VariantCategory>?>
        get() = _onVariantClickedData

    private val _toggleTeaserNotifyMe = MutableLiveData<Result<Boolean>>()
    val toggleTeaserNotifyMe: LiveData<Result<Boolean>>
        get() = _toggleTeaserNotifyMe

    private val _discussionMostHelpful = MutableLiveData<Result<DiscussionMostHelpfulResponseWrapper>>()
    val discussionMostHelpful: LiveData<Result<DiscussionMostHelpfulResponseWrapper>>
        get() = _discussionMostHelpful

    private val _topAdsImageView: MutableLiveData<Result<ArrayList<TopAdsImageViewModel>>> = MutableLiveData()
    val topAdsImageView: LiveData<Result<ArrayList<TopAdsImageViewModel>>>
        get() = _topAdsImageView

    var notifyMeAction: String = ProductDetailCommonConstant.VALUE_TEASER_ACTION_UNREGISTER
    var getDynamicProductInfoP1: DynamicProductInfoP1? = null
    var tradeInParams: TradeInParams = TradeInParams()
    var enableCaching: Boolean = true
    var variantData: ProductVariantCommon? = null
    var listOfParentMedia: MutableList<Media>? = null
    var buttonActionType: Int = 0
    var buttonActionText: String = ""
    var tradeinDeviceId: String = ""
    // used only for bringing product id to edit product
    var parentProductId: String? = null
    var shippingMinimumPrice: Int = getDynamicProductInfoP1?.basic?.getDefaultOngkirInt() ?: 30000
    var talkLastAction: DynamicProductDetailTalkLastAction? = null
    private var forceRefresh: Boolean = false
    private var shopDomain: String? = null

    private var submitTicketSubscription: Subscription? = null
    private var updateCartCounterSubscription: Subscription? = null

    fun hasShopAuthority(): Boolean = isShopOwner() || getShopInfo().allowManage
    fun isShopOwner(): Boolean = isUserSessionActive && userSessionInterface.shopId.toIntOrNull() == getDynamicProductInfoP1?.basic?.getShopId()
    val isUserSessionActive: Boolean
        get() = userSessionInterface.isLoggedIn

    val userId: String
        get() = userSessionInterface.userId

    var deviceId: String = userSessionInterface.deviceId ?: ""

    init {
        _productInfoP3.addSource(_p2Data) { p2Data ->
            launchCatchError(context = dispatcher.io(), block = {
                getDynamicProductInfoP1?.let {
                    getProductInfoP3(shopDomain, it)?.let { p3Data ->
                        updateShippingValue(p3Data.ratesModel?.getMinimumShippingPrice())
                        _productInfoP3.postValue(p3Data)
                    }
                }
            }) {
                Timber.d(it)
            }
        }
    }

    override fun flush() {
        super.flush()
        stickyLoginUseCase.cancelJobs()
        getPdpLayoutUseCase.cancelJobs()
        getProductInfoP2LoginUseCase.cancelJobs()
        getProductInfoP2OtherUseCase.cancelJobs()
        getProductInfoP3UseCase.cancelJobs()
        toggleFavoriteUseCase.cancelJobs()
        trackAffiliateUseCase.cancelJobs()
        moveProductToWarehouseUseCase.cancelJobs()
        moveProductToEtalaseUseCase.cancelJobs()
        getRecommendationUseCase.unsubscribe()
        removeWishlistUseCase.unsubscribe()
        submitTicketSubscription?.unsubscribe()
        updateCartCounterSubscription?.unsubscribe()
        addToCartUseCase.unsubscribe()
        addToCartOcsUseCase.unsubscribe()
        toggleNotifyMeUseCase.cancelJobs()
        discussionMostHelpfulUseCase.cancelJobs()
    }

    fun clearCacheP2Data() {
        getProductInfoP2DataUseCase.clearCache()
    }

    fun getShopInfo(): ShopInfo {
        return p2Data.value?.shopInfo ?: ShopInfo()
    }

    fun getCartTypeByProductId() : CartTypeData? {
        return p2Data.value?.cartRedirection?.get(getDynamicProductInfoP1?.basic?.productID ?: "")
    }

    fun updateLastAction(talkLastAction: DynamicProductDetailTalkLastAction) {
        this.talkLastAction = talkLastAction
    }

    fun updateNotifyMeData() {
        val selectedUpcoming = p2Data.value?.upcomingCampaigns?.get(getDynamicProductInfoP1?.basic?.productID)
        p2Data.value?.upcomingCampaigns?.get(getDynamicProductInfoP1?.basic?.productID)?.notifyMe = selectedUpcoming?.notifyMe != true
    }

    fun updateDynamicProductInfoData(data: DynamicProductInfoP1?) {
        data?.let {
            getDynamicProductInfoP1 = it
        }
    }

    /**
     * If variant change, make sure this function is called after update product Id
     */
    fun getMultiOriginByProductId(): WarehouseInfo {
        getDynamicProductInfoP1?.let {
            return p2Data.value?.nearestWarehouseInfo?.get(it.basic.productID) ?: WarehouseInfo()
        }

        return WarehouseInfo()
    }

    fun processVariant(data: ProductVariantCommon, mapOfSelectedVariant: MutableMap<String, Int>?) {
        launchCatchError(block = {
            withContext(dispatcher.io()) {
                _initialVariantData.postValue(VariantCommonMapper.processVariant(data, mapOfSelectedVariant))
            }
        }) {}
    }

    fun onVariantClicked(data: ProductVariantCommon?, mapOfSelectedVariant: MutableMap<String, Int>?,
                         isPartialySelected: Boolean, variantLevel: Int, imageVariant: String) {
        launchCatchError(block = {
            withContext(dispatcher.io()) {
                val processedVariant = VariantCommonMapper.processVariant(data, mapOfSelectedVariant, variantLevel, isPartialySelected)

                if (isPartialySelected) {
                    //It means user only select one of two variant available
                    if (imageVariant.isNotBlank()) {
                        _updatedImageVariant.postValue(processedVariant to addPartialImage(imageVariant))
                    } else {
                        _updatedImageVariant.postValue(processedVariant to listOf())
                    }
                    return@withContext
                } else {
                    _onVariantClickedData.postValue(processedVariant)
                }
            }
        }) {}
    }

    private fun addPartialImage(imageUrl: String): List<Media> {
        listOfParentMedia?.let {
            if (imageUrl.isEmpty() && imageUrl == it.firstOrNull()?.uRLOriginal) return@let

            val listOfUpdateImage = it.toMutableList()
            listOfUpdateImage.add(0, Media(type = "image", uRL300 = imageUrl, uRLOriginal = imageUrl, uRLThumbnail = imageUrl))
            return listOfUpdateImage
        }
        return listOf()
    }

    fun getStickyLoginContent(onSuccess: (StickyLoginTickerPojo.TickerDetail) -> Unit, onError: ((Throwable) -> Unit)?) {
        if (!isUserSessionActive) {
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
    }

    fun getProductP1(productParams: ProductParams, refreshPage: Boolean = false, isAffiliate: Boolean = false, layoutId: String = "") {
        launchCatchError(block = {
            shopDomain = productParams.shopDomain
            forceRefresh = refreshPage

            getPdpLayout(productParams.productId ?: "", productParams.shopDomain
                    ?: "", productParams.productName ?: "", productParams.warehouseId ?: "", layoutId).also {
                addStaticComponent(it)
                getDynamicProductInfoP1 = it.layoutData.also {
                    listOfParentMedia = it.data.media.toMutableList()
                }

                variantData = if (getDynamicProductInfoP1?.isProductVariant() == false) null else it.variantData
                parentProductId = it.layoutData.parentProductId

                //Create tradein params
                assignTradeinParams()

                //Remove any unused component based on P1 / PdpLayout
                removeDynamicComponent(it.listOfLayout, isAffiliate)

                //Render initial data
                _productLayout.value = it.listOfLayout.asSuccess()
            }
            // Then update the following, it will not throw anything when error
            getProductP2()

        }) {
            _productLayout.value = it.asFail()
        }
    }

    fun addToCart(atcParams: Any) {
        launchCatchError(block = {
            val requestParams = RequestParams.create()
            requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, atcParams)

            when (atcParams) {
                is AddToCartRequestParams -> {
                    getAddToCartUseCase(requestParams)
                }
                is AddToCartOcsRequestParams -> {
                    getAddToCartOcsUseCase(requestParams)
                }
                is AddToCartOccRequestParams -> {
                    getAddToCartOccUseCase(requestParams)
                }
            }
        }) {
            _addToCartLiveData.value = it.cause?.asFail() ?: it.asFail()
        }
    }

    private fun addStaticComponent(it: ProductDetailDataModel) {
        it.listOfLayout.add(ProductLastSeenDataModel())
    }

    private suspend fun getAddToCartUseCase(requestParams: RequestParams) {
        withContext(dispatcher.io()) {
            val result = addToCartUseCase.createObservable(requestParams).toBlocking().single()
            if (result.isDataError()) {
                _addToCartLiveData.postValue(MessageErrorException(result.errorMessage.firstOrNull()
                        ?: "").asFail())
            } else {
                _addToCartLiveData.postValue(result.asSuccess())
            }
        }
    }

    private suspend fun getAddToCartOcsUseCase(requestParams: RequestParams) {
        withContext(dispatcher.io()) {
            val result = addToCartOcsUseCase.createObservable(requestParams).toBlocking().single()
            if (result.isDataError()) {
                _addToCartLiveData.postValue(MessageErrorException(result.errorMessage.firstOrNull()
                        ?: "").asFail())
            } else {
                _addToCartLiveData.postValue(result.asSuccess())
            }
        }
    }

    private suspend fun getAddToCartOccUseCase(requestParams: RequestParams) {
        withContext(dispatcher.io()) {
            val result = addToCartOccUseCase.createObservable(requestParams).toBlocking().single()
            if (result.isDataError()) {
                _addToCartLiveData.postValue(arrayListOf(result.getAtcErrorMessage()
                        ?: "").asThrowable().asFail())

            } else {
                _addToCartLiveData.postValue(result.asSuccess())
            }
        }
    }

    private suspend fun getProductP2() {
        getDynamicProductInfoP1?.let {
            val p2LoginDeferred: Deferred<ProductInfoP2Login>? = if (isUserSessionActive) {
                getProductInfoP2LoginAsync(it.basic.getShopId(),
                        it.basic.getProductId())
            } else null
            val p2DataDeffered: Deferred<ProductInfoP2UiData> = getProductInfoP2DataAsync(it.basic.productID, it.pdpSession)
            val p2OtherDeffered: Deferred<ProductInfoP2Other> = getProductInfoP2OtherAsync(it.basic.getProductId(), it.basic.getShopId())

            _p2Data.value = p2DataDeffered.await().also { value ->
                updateTradeinParams(value.validateTradeIn)
            }

            p2LoginDeferred?.let {
                _p2Login.value = it.await()
            }

            _p2Other.value = p2OtherDeffered.await()

            getTopAdsImageViewData(it.basic.productID)
        }
    }

    private fun getTopAdsImageViewData(productID: String) {
        launchCatchError(block = {
            val result = topAdsImageViewUseCase.getImageData(topAdsImageViewUseCase.getQueryMap(
                    "",
                    PAGE_SOURCE,
                    "",
                    ADS_COUNT,
                    DIMEN_ID,
                    "",
                    productID
            ))
            _topAdsImageView.postValue(result.asSuccess())
        }){
            _topAdsImageView.postValue(it.asFail())
        }
    }

    private suspend fun getProductInfoP3(shopDomain: String?, productInfo: DynamicProductInfoP1): ProductInfoP3? {
        val domain = shopDomain ?: getShopInfo().shopCore.domain
        val origin = if (getMultiOriginByProductId().isFulfillment) getMultiOriginByProductId().getOrigin() else null

        return getProductInfoP3(productInfo.basic.getWeightUnit(), domain, origin, productInfo.shouldShowCod)
    }

    private fun updateShippingValue(shippingPriceValue: Int?) {
        shippingMinimumPrice = if (shippingPriceValue == null || shippingPriceValue == 0) getDynamicProductInfoP1?.basic?.getDefaultOngkirInt()
                ?: 30000 else shippingPriceValue
    }

    private fun removeDynamicComponent(initialLayoutData: MutableList<DynamicPdpDataModel>, isAffiliate: Boolean) {
        val isTradein = getDynamicProductInfoP1?.data?.isTradeIn == true
        val hasWholesale = getDynamicProductInfoP1?.data?.hasWholesale == true
        val isOfficialStore = getDynamicProductInfoP1?.data?.isOS == true
        val isVariant = getDynamicProductInfoP1?.isProductVariant() ?: false
        val isVariantEmpty = variantData == null || variantData?.hasChildren == false

        val removedData = initialLayoutData.map {
            if ((!isTradein || isShopOwner()) && it.name() == ProductDetailConstant.TRADE_IN) {
                it
            } else if (!hasWholesale && it.name() == ProductDetailConstant.PRODUCT_WHOLESALE_INFO) {
                it
            } else if (!isOfficialStore && it.name() == ProductDetailConstant.VALUE_PROP) {
                it
            } else if (it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO && !isUserSessionActive) {
                it
            } else if (it.name() == ProductDetailConstant.PRODUCT_VARIANT_INFO) {
                it
            } else if (it.name() == ProductDetailConstant.VARIANT_OPTIONS && (!isVariant || isVariantEmpty)) {
                it
            } else if (GlobalConfig.isSellerApp() && it.type() == ProductDetailConstant.PRODUCT_LIST) {
                it
            } else if (it.name() == ProductDetailConstant.BY_ME && isAffiliate && !GlobalConfig.isSellerApp()) {
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
            _toggleFavoriteResult.value = toggleFavoriteUseCase.executeOnBackground().followShop.isSuccess.asSuccess()
        }) {
            _toggleFavoriteResult.value = it.asFail()
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

    fun loadRecommendation() {
        launch {
            if (!GlobalConfig.isSellerApp()) {
                try {
                    withContext(dispatcher.io()) {
                        val recomData = getRecommendationUseCase.createObservable(getRecommendationUseCase.getRecomParams(
                                pageNumber = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
                                pageName = ProductDetailConstant.DEFAULT_PAGE_NAME,
                                productIds = arrayListOf(getDynamicProductInfoP1?.basic?.productID
                                        ?: "")
                        )).toBlocking()
                        _loadTopAdsProduct.postValue((recomData.first() ?: emptyList()).asSuccess())
                    }
                } catch (e: Throwable) {
                    _loadTopAdsProduct.value = e.asFail()
                }
            }
        }
    }

    fun moveProductToWareHouse(productId: String) {
        launchCatchError(block = {
            moveProductToWarehouseUseCase.createParams(productId, userId, deviceId)
            _moveToWarehouseResult.value = moveProductToWarehouseUseCase.executeOnBackground().getIsSuccess().asSuccess()
        }) {
            _moveToWarehouseResult.value = it.asFail()
        }
    }

    fun moveProductToEtalase(productId: String, selectedEtalaseId: String, selectedEtalaseName: String) {
        launchCatchError(block = {
            moveProductToEtalaseUseCase.createParams(productId, selectedEtalaseId, selectedEtalaseName, userId, deviceId)
            _moveToEtalaseResult.value = moveProductToEtalaseUseCase.executeOnBackground().getIsSuccess().asSuccess()
        }) {
            _moveToEtalaseResult.value = it.asFail()
        }
    }

    fun hitAffiliateTracker(affiliateUniqueString: String, deviceId: String) {
        trackAffiliateUseCase.params = TrackAffiliateUseCase.createParams(affiliateUniqueString, deviceId)
        trackAffiliateUseCase.execute({
            //no op
        }) {
            Timber.d(it)
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

    fun toggleTeaserNotifyMe(campaignId: Int, productId: Int, source: String) {
        launchCatchError(block = {
            toggleNotifyMeUseCase.createParams(campaignId, productId, notifyMeAction, source)
            val isSuccess = toggleNotifyMeUseCase.executeOnBackground().result.isSuccess
            if (!isSuccess) _toggleTeaserNotifyMe.value = Throwable().asFail()
            _toggleTeaserNotifyMe.value = isSuccess.asSuccess()
        }) {
            _toggleTeaserNotifyMe.value = it.asFail()
        }
    }

    fun cancelWarehouseUseCase() {
        moveProductToWarehouseUseCase.cancelJobs()
    }

    fun cancelEtalaseUseCase() {
        moveProductToEtalaseUseCase.cancelJobs()
    }

    fun getDiscussionMostHelpful(productId: String, shopId: String) {
        launchCatchError(block = {
            val response = withContext(dispatcher.io()) {
                discussionMostHelpfulUseCase.createRequestParams(productId, shopId)
                discussionMostHelpfulUseCase.executeOnBackground()
            }
            _discussionMostHelpful.postValue(response.asSuccess())
        }) {
            _discussionMostHelpful.postValue(it.asFail())
        }
    }

    private fun assignTradeinParams() {
        getDynamicProductInfoP1?.let {
            tradeInParams.categoryId = it.basic.category.id.toIntOrZero()
            tradeInParams.deviceId = deviceId
            tradeInParams.userId = userId.toIntOrZero()
            tradeInParams.setPrice(it.data.price.value)
            tradeInParams.productId = it.basic.getProductId()
            tradeInParams.shopId = it.basic.getShopId()
            tradeInParams.productName = it.getProductName
            tradeInParams.isPreorder = it.data.preOrder.isPreOrderActive()
            tradeInParams.isOnCampaign = it.data.campaign.isActive
        }
    }

    private fun updateTradeinParams(validateTradeIn: ValidateTradeIn) {
        tradeInParams.isEligible = if (validateTradeIn.isEligible) 1 else 0
        tradeInParams.usedPrice = validateTradeIn.usedPrice.toIntOrZero()
        tradeInParams.remainingPrice = validateTradeIn.remainingPrice.toIntOrZero()
        tradeInParams.isUseKyc = if (validateTradeIn.useKyc) 1 else 0
        tradeInParams.widgetString = validateTradeIn.widgetString
    }

    private fun getProductInfoP2OtherAsync(productId: Int, shopId: Int): Deferred<ProductInfoP2Other> {
        return async {
            getProductInfoP2OtherUseCase.executeOnBackground(GetProductInfoP2OtherUseCase.createParams(productId, shopId), forceRefresh)
        }
    }

    private fun getProductInfoP2LoginAsync(shopId: Int, productId: Int): Deferred<ProductInfoP2Login> {
        return async {
            getProductInfoP2LoginUseCase.requestParams = GetProductInfoP2LoginUseCase.createParams(shopId, productId, isShopOwner())
            getProductInfoP2LoginUseCase.executeOnBackground()
        }
    }

    private fun getProductInfoP2DataAsync(productId: String, pdpSession: String): Deferred<ProductInfoP2UiData> {
        return async {
            getProductInfoP2DataUseCase.executeOnBackground(GetProductInfoP2DataUseCase.createParams(productId, generatePdpSessionWithDeviceId(pdpSession)), forceRefresh)
        }
    }

    private fun generatePdpSessionWithDeviceId(pdpSession: String): String {
        return if (getDynamicProductInfoP1?.data?.isTradeIn == false) {
            pdpSession
        } else {
            pdpSession + ProductDetailConstant.DELIMITER_DEVICE_ID + deviceId
        }
    }

    private suspend fun getProductInfoP3(weight: Float, shopDomain: String?, origin: String?, needRequestCod: Boolean): ProductInfoP3 {
        return getProductInfoP3UseCase.executeOnBackground(
                GetProductInfoP3UseCase.createParams(weight, shopDomain, origin, needRequestCod),
                forceRefresh,
                isUserSessionActive)
    }

    private suspend fun getPdpLayout(productId: String, shopDomain: String, productKey: String, whId: String, layoutId: String): ProductDetailDataModel {
        getPdpLayoutUseCase.requestParams = GetPdpLayoutUseCase.createParams(productId, shopDomain, productKey, whId, layoutId)
        getPdpLayoutUseCase.forceRefresh = forceRefresh
        getPdpLayoutUseCase.enableCaching = enableCaching
        return getPdpLayoutUseCase.executeOnBackground()
    }
}