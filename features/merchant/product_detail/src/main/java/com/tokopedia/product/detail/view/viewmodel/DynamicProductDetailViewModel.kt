package com.tokopedia.product.detail.view.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
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
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
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
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.ratesestimate.ErrorBottomSheet
import com.tokopedia.product.detail.data.model.ratesestimate.P2RatesEstimateData
import com.tokopedia.product.detail.data.model.restrictioninfo.BebasOngkirImage
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.product.detail.data.model.tradein.ValidateTradeIn
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper.generateUserLocationRequest
import com.tokopedia.product.detail.data.util.DynamicProductDetailTalkLastAction
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant.ADS_COUNT
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DIMEN_ID
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PAGE_SOURCE
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PDP_3
import com.tokopedia.product.detail.usecase.*
import com.tokopedia.product.detail.view.util.ProductDetailLogger
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.purchase_platform.common.feature.helpticket.data.request.SubmitHelpTicketRequest
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.model.SubmitTicketResult
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
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
import dagger.Lazy
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

open class DynamicProductDetailViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                             private val getPdpLayoutUseCase: Lazy<GetPdpLayoutUseCase>,
                                                             private val getProductInfoP2LoginUseCase: Lazy<GetProductInfoP2LoginUseCase>,
                                                             private val getProductInfoP2OtherUseCase: Lazy<GetProductInfoP2OtherUseCase>,
                                                             private val getProductInfoP2DataUseCase: Lazy<GetProductInfoP2DataUseCase>,
                                                             private val getProductInfoP3UseCase: Lazy<GetProductInfoP3UseCase>,
                                                             private val toggleFavoriteUseCase: Lazy<ToggleFavoriteUseCase>,
                                                             private val removeWishlistUseCase: Lazy<RemoveWishListUseCase>,
                                                             private val addWishListUseCase: Lazy<AddWishListUseCase>,
                                                             private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>,
                                                             private val getRecommendationFilterChips: Lazy<GetRecommendationFilterChips>,
                                                             private val moveProductToWarehouseUseCase: Lazy<MoveProductToWarehouseUseCase>,
                                                             private val moveProductToEtalaseUseCase: Lazy<MoveProductToEtalaseUseCase>,
                                                             private val trackAffiliateUseCase: Lazy<TrackAffiliateUseCase>,
                                                             private val submitHelpTicketUseCase: Lazy<SubmitHelpTicketUseCase>,
                                                             private val updateCartCounterUseCase: Lazy<UpdateCartCounterUseCase>,
                                                             private val addToCartUseCase: Lazy<AddToCartUseCase>,
                                                             private val addToCartOcsUseCase: Lazy<AddToCartOcsUseCase>,
                                                             private val addToCartOccUseCase: Lazy<AddToCartOccUseCase>,
                                                             private val toggleNotifyMeUseCase: Lazy<ToggleNotifyMeUseCase>,
                                                             private val discussionMostHelpfulUseCase: Lazy<DiscussionMostHelpfulUseCase>,
                                                             private val topAdsImageViewUseCase: Lazy<TopAdsImageViewUseCase>,
                                                             val userSessionInterface: UserSessionInterface) : BaseViewModel(dispatcher.main) {

    companion object {
        private const val ATC_ERROR_TYPE = "error_atc"
        private const val WISHLIST_ERROR_TYPE = "error_wishlist"
        private const val WISHLIST_STATUS_KEY = "wishlist_status"
        private const val ADD_WISHLIST = "true"
        private const val REMOVE_WISHLIST = "false"
        private const val P2_LOGIN_ERROR_TYPE = "error_p2_login"
        private const val P2_DATA_ERROR_TYPE = "error_p2_data"
    }

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

    private val _loadTopAdsProduct = MutableLiveData<Result<RecommendationWidget>>()
    val loadTopAdsProduct: LiveData<Result<RecommendationWidget>>
        get() = _loadTopAdsProduct

    private val _filterTopAdsProduct = MutableLiveData<ProductRecommendationDataModel>()
    val filterTopAdsProduct: LiveData<ProductRecommendationDataModel>
        get() = _filterTopAdsProduct

    private val _statusFilterTopAdsProduct = MutableLiveData<Result<Boolean>>()
    val statusFilterTopAdsProduct: LiveData<Result<Boolean>>
        get() = _statusFilterTopAdsProduct

    private val _moveToWarehouseResult = MutableLiveData<Result<Boolean>>()
    val moveToWarehouseResult: LiveData<Result<Boolean>>
        get() = _moveToWarehouseResult

    private val _moveToEtalaseResult = MutableLiveData<Result<Boolean>>()
    val moveToEtalaseResult: LiveData<Result<Boolean>>
        get() = _moveToEtalaseResult

    private val _toggleFavoriteResult = MutableLiveData<Result<Pair<Boolean, Boolean>>>()
    val toggleFavoriteResult: LiveData<Result<Pair<Boolean, Boolean>>>
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

    var videoTrackerData: Pair<Long, Long>? = null

    var notifyMeAction: String = ProductDetailCommonConstant.VALUE_TEASER_ACTION_UNREGISTER
    var getDynamicProductInfoP1: DynamicProductInfoP1? = null
    var tradeInParams: TradeInParams = TradeInParams()
    var variantData: ProductVariantCommon? = null
    var listOfParentMedia: MutableList<Media>? = null
    var buttonActionText: String = ""
    var tradeinDeviceId: String = ""

    // used only for bringing product id to edit product
    var parentProductId: String? = null
    var shippingMinimumPrice: Int = getDynamicProductInfoP1?.basic?.getDefaultOngkirInt() ?: 30000
    var talkLastAction: DynamicProductDetailTalkLastAction? = null
    var isNewShipment: Boolean = false
    private var userLocationCache: LocalCacheModel = LocalCacheModel()
    private var forceRefresh: Boolean = false
    private var shopDomain: String? = null
    private var alreadyHitRecom: MutableList<String> = mutableListOf()

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
            launchCatchError(context = dispatcher.io, block = {
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
        getPdpLayoutUseCase.get().cancelJobs()
        getProductInfoP2LoginUseCase.get().cancelJobs()
        getProductInfoP2OtherUseCase.get().cancelJobs()
        getProductInfoP3UseCase.get().cancelJobs()
        toggleFavoriteUseCase.get().cancelJobs()
        trackAffiliateUseCase.get().cancelJobs()
        moveProductToWarehouseUseCase.get().cancelJobs()
        moveProductToEtalaseUseCase.get().cancelJobs()
        getRecommendationUseCase.get().unsubscribe()
        removeWishlistUseCase.get().unsubscribe()
        submitTicketSubscription?.unsubscribe()
        updateCartCounterSubscription?.unsubscribe()
        addToCartUseCase.get().unsubscribe()
        addToCartOcsUseCase.get().unsubscribe()
        toggleNotifyMeUseCase.get().cancelJobs()
        discussionMostHelpfulUseCase.get().cancelJobs()
    }

    fun getUserLocationCache(): LocalCacheModel {
        return userLocationCache
    }

    fun updateVideoTrackerData(stopDuration: Long, videoDuration: Long) {
        videoTrackerData = stopDuration to videoDuration
    }

    fun clearCacheP2Data() {
        getProductInfoP2DataUseCase.get().clearCache()
    }

    fun getShopInfo(): ShopInfo {
        return p2Data.value?.shopInfo ?: ShopInfo()
    }

    fun getCartTypeByProductId(): CartTypeData? {
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

    fun getP2RatesEstimateByProductId(): P2RatesEstimateData? {
        val productId = getDynamicProductInfoP1?.basic?.productID ?: ""
        var result: P2RatesEstimateData? = null
        p2Data.value?.ratesEstimate?.forEach {
            if (productId in it.listfProductId) result = it.p2RatesData
        }
        return result
    }

    fun getP2RatesBottomSheetData(): ErrorBottomSheet? {
        val productId = getDynamicProductInfoP1?.basic?.productID ?: ""
        var result: ErrorBottomSheet? = null
        p2Data.value?.ratesEstimate?.forEach {
            if (productId in it.listfProductId) result = it.errorBottomSheet
        }
        return result
    }

    fun getBebasOngkirDataByProductId(): BebasOngkirImage {
        val productId = getDynamicProductInfoP1?.basic?.productID ?: ""
        val boType = p2Data.value?.bebasOngkir?.boProduct?.firstOrNull { it.productId == productId }?.boType
                ?: 0
        val image = p2Data.value?.bebasOngkir?.boImages?.firstOrNull { it.boType == boType }
                ?: BebasOngkirImage()
        return image
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
        launchCatchError(dispatcher.io, block = {
            _initialVariantData.postValue(VariantCommonMapper.processVariant(data, mapOfSelectedVariant))
        }) {}
    }

    fun onVariantClicked(data: ProductVariantCommon?, mapOfSelectedVariant: MutableMap<String, Int>?,
                         isPartialySelected: Boolean, variantLevel: Int, imageVariant: String) {
        launchCatchError(block = {
            withContext(dispatcher.io) {
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

    fun getProductP1(productParams: ProductParams, refreshPage: Boolean = false, isAffiliate: Boolean = false, layoutId: String = "",
                     isUseOldNav: Boolean = false, userLocationLocal: LocalCacheModel) {
        launchCatchError(dispatcher.io, block = {
            alreadyHitRecom = mutableListOf()
            shopDomain = productParams.shopDomain
            forceRefresh = refreshPage
            userLocationCache = userLocationLocal
            getPdpLayout(productParams.productId ?: "", productParams.shopDomain
                    ?: "", productParams.productName ?: "", productParams.warehouseId
                    ?: "", layoutId).also {

                isNewShipment = ChooseAddressUtils.isRollOutUser(null)

                getDynamicProductInfoP1 = it.layoutData.also {
                    listOfParentMedia = it.data.media.toMutableList()
                }

                variantData = if (getDynamicProductInfoP1?.isProductVariant() == false) null else it.variantData
                parentProductId = it.layoutData.parentProductId

                //Create tradein params
                assignTradeinParams()

                //Remove any unused component based on P1 / PdpLayout
                removeDynamicComponent(it.listOfLayout, isAffiliate, isUseOldNav)

                //Render initial data
                _productLayout.postValue(it.listOfLayout.asSuccess())
            }
            // Then update the following, it will not throw anything when error
            getProductP2()

        }) {
            _productLayout.postValue(it.asFail())
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

    private suspend fun getAddToCartUseCase(requestParams: RequestParams) {
        withContext(dispatcher.io) {
            val result = addToCartUseCase.get().createObservable(requestParams).toBlocking().single()
            if (result.isDataError()) {
                val errorMessage = result.errorMessage.firstOrNull() ?: ""
                if (errorMessage.isNotBlank()) {
                    ProductDetailLogger.logMessage(errorMessage, ATC_ERROR_TYPE, getDynamicProductInfoP1?.basic?.productID
                            ?: "", deviceId)
                }
                _addToCartLiveData.postValue(MessageErrorException(errorMessage).asFail())
            } else {
                _addToCartLiveData.postValue(result.asSuccess())
            }
        }
    }

    private suspend fun getAddToCartOcsUseCase(requestParams: RequestParams) {
        withContext(dispatcher.io) {
            val result = addToCartOcsUseCase.get().createObservable(requestParams).toBlocking().single()
            if (result.isDataError()) {
                val errorMessage = result.errorMessage.firstOrNull() ?: ""
                if (errorMessage.isNotBlank()) {
                    ProductDetailLogger.logMessage(errorMessage, ATC_ERROR_TYPE, getDynamicProductInfoP1?.basic?.productID
                            ?: "", deviceId)
                }
                _addToCartLiveData.postValue(MessageErrorException(errorMessage).asFail())
            } else {
                _addToCartLiveData.postValue(result.asSuccess())
            }
        }
    }

    private suspend fun getAddToCartOccUseCase(requestParams: RequestParams) {
        withContext(dispatcher.io) {
            val result = addToCartOccUseCase.get().createObservable(requestParams).toBlocking().single()
            if (result.isDataError()) {
                val errorMessage = result.getAtcErrorMessage() ?: ""
                if (errorMessage.isNotBlank()) {
                    ProductDetailLogger.logMessage(errorMessage, ATC_ERROR_TYPE, getDynamicProductInfoP1?.basic?.productID
                            ?: "", deviceId)
                }
                _addToCartLiveData.postValue(MessageErrorException(errorMessage).asFail())
            } else {
                _addToCartLiveData.postValue(result.asSuccess())
            }
        }
    }

    private suspend fun getProductP2() {
        getDynamicProductInfoP1?.let {
            val p2LoginDeferred: Deferred<ProductInfoP2Login>? = if (isUserSessionActive) {
                getProductInfoP2LoginAsync(it.basic.getShopId(),
                        it.basic.productID)
            } else null
            val p2DataDeffered: Deferred<ProductInfoP2UiData> = getProductInfoP2DataAsync(it.basic.productID, it.pdpSession)
            val p2OtherDeffered: Deferred<ProductInfoP2Other> = getProductInfoP2OtherAsync(it.basic.productID, it.basic.getShopId())

            p2DataDeffered.await()?.let {
                _p2Data.postValue(it)
                updateTradeinParams(it.validateTradeIn)
            }
            p2LoginDeferred?.let {
                _p2Login.postValue(it.await())
            }

            _p2Other.postValue(p2OtherDeffered.await())

            getTopAdsImageViewData(it.basic.productID)
        }
    }

    private fun getTopAdsImageViewData(productID: String) {
        launchCatchError(block = {
            val result = topAdsImageViewUseCase.get().getImageData(topAdsImageViewUseCase.get().getQueryMap(
                    "",
                    PAGE_SOURCE,
                    "",
                    ADS_COUNT,
                    DIMEN_ID,
                    "",
                    productID
            ))
            _topAdsImageView.postValue(result.asSuccess())
        }) {
            _topAdsImageView.postValue(it.asFail())
        }
    }

    private suspend fun getProductInfoP3(shopDomain: String?, productInfo: DynamicProductInfoP1): ProductInfoP3? {
        val domain = shopDomain ?: getShopInfo().shopCore.domain
        val origin = if (getMultiOriginByProductId().isFulfillment) getMultiOriginByProductId().getOrigin() else null

        return getProductInfoP3(productInfo.basic.getWeightUnit(), domain, origin)
    }

    private fun updateShippingValue(shippingPriceValue: Int?) {
        if (isNewShipment) return
        shippingMinimumPrice = if (shippingPriceValue == null || shippingPriceValue == 0) getDynamicProductInfoP1?.basic?.getDefaultOngkirInt()
                ?: 30000 else shippingPriceValue
    }

    private fun removeDynamicComponent(initialLayoutData: MutableList<DynamicPdpDataModel>, isAffiliate: Boolean, isUseOldNav: Boolean) {
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
            } else if (it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO && (!isUserSessionActive || isNewShipment)) {
                it
            } else if (it.name() == ProductDetailConstant.PRODUCT_VARIANT_INFO) {
                it
            } else if (it.name() == ProductDetailConstant.VARIANT_OPTIONS && (!isVariant || isVariantEmpty)) {
                it
            } else if (GlobalConfig.isSellerApp() && it.type() == ProductDetailConstant.PRODUCT_LIST) {
                it
            } else if (it.name() == ProductDetailConstant.BY_ME && isAffiliate && !GlobalConfig.isSellerApp()) {
                it
            } else if (it.name() == ProductDetailConstant.REPORT && (isUseOldNav || isShopOwner())) {
                it
            } else if (it.name() == ProductDetailConstant.SHIPMENT && !isNewShipment) {
                it
            } else {
                null
            }
        }

        if (removedData.isNotEmpty())
            initialLayoutData.removeAll(removedData)
    }

    fun toggleFavorite(shopID: String, isNplFollowerType: Boolean = false) {
        launchCatchError(dispatcher.io, block = {
            val requestParams = ToggleFavoriteUseCase.createParams(shopID, if (isNplFollowerType) ToggleFavoriteUseCase.FOLLOW_ACTION else null)
            val favoriteData = toggleFavoriteUseCase.get().executeOnBackground(requestParams).followShop
            if (favoriteData?.isSuccess == true) {
                _toggleFavoriteResult.postValue((favoriteData.isSuccess to isNplFollowerType).asSuccess())
            } else {
                _toggleFavoriteResult.postValue(Throwable(favoriteData?.message).asFail())
            }
        }) {
            _toggleFavoriteResult.postValue(it.asFail())
        }
    }

    fun removeWishList(productId: String,
                       onSuccessRemoveWishlist: ((productId: String?) -> Unit)?,
                       onErrorRemoveWishList: ((errorMessage: String?) -> Unit)?) {
        removeWishlistUseCase.get().createObservable(productId,
                userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                // no op
            }

            override fun onSuccessAddWishlist(productId: String?) {
                // no op
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                if (!(errorMessage.isNullOrEmpty() || productId.isNullOrEmpty())) {
                    val extras = mapOf(WISHLIST_STATUS_KEY to REMOVE_WISHLIST).toString()
                    ProductDetailLogger.logMessage(errorMessage, WISHLIST_ERROR_TYPE, productId, deviceId, extras)
                }
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
        addWishListUseCase.get().createObservable(productId,
                userSessionInterface.userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                if (!(errorMessage.isNullOrEmpty() || productId.isNullOrEmpty())) {
                    val extras = mapOf(WISHLIST_STATUS_KEY to ADD_WISHLIST).toString()
                    ProductDetailLogger.logMessage(errorMessage, WISHLIST_ERROR_TYPE, productId, deviceId, extras)
                }
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

    fun loadRecommendation(pageName: String) {
        launch(dispatcher.main) {
            if (!GlobalConfig.isSellerApp()) {

                if (!alreadyHitRecom.contains(pageName)) {
                    alreadyHitRecom.add(pageName)
                } else {
                    return@launch
                }

                try {
                    val recomData = withContext(dispatcher.io) {
                        var recomWidget = RecommendationWidget()

                        val productIds = arrayListOf(getDynamicProductInfoP1?.basic?.productID
                                ?: "")
                        val productIdsString = TextUtils.join(",", productIds) ?: ""
                        val recomFilterList = mutableListOf<RecommendationFilterChipsEntity.RecommendationFilterChip>()
                        if (pageName == PDP_3) {
                            getRecommendationFilterChips.get().setParams(
                                    userId = if (userSessionInterface.userId.isEmpty()) 0 else userSessionInterface.userId.toInt(),
                                    pageName = pageName,
                                    productIDs = productIdsString,
                                    xSource = ProductDetailConstant.DEFAULT_X_SOURCE
                            )
                            recomFilterList.addAll(getRecommendationFilterChips.get().executeOnBackground().filterChip)
                        }

                        val recomData = getRecommendationUseCase.get().createObservable(getRecommendationUseCase.get().getRecomParams(
                                pageNumber = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
                                pageName = pageName,
                                productIds = productIds
                        )).toBlocking().first()

                        if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
                            recomWidget = recomData.first().copy(
                                    recommendationFilterChips = recomFilterList,
                                    pageName = pageName
                            )
                        }

                        recomWidget
                    }

                    if (recomData.recommendationItemList.isNotEmpty()) {
                        _loadTopAdsProduct.value = recomData.asSuccess()
                    } else {
                        _loadTopAdsProduct.value = Throwable(pageName).asFail()
                    }
                } catch (e: Throwable) {
                    _loadTopAdsProduct.value = Throwable(pageName).asFail()
                }
            }
        }
    }

    fun getRecommendation(recommendationDataModel: ProductRecommendationDataModel, annotationChip: AnnotationChip, position: Int, filterPosition: Int) {
        launchCatchError(dispatcher.io, block = {
            if (!GlobalConfig.isSellerApp()) {
                val recomData = getRecommendationUseCase.get().createObservable(getRecommendationUseCase.get().getRecomParams(
                        pageNumber = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
                        pageName = recommendationDataModel.recomWidgetData?.pageName ?: "",
                        queryParam = if (annotationChip.recommendationFilterChip.isActivated) annotationChip.recommendationFilterChip.value else "",
                        productIds = arrayListOf(getDynamicProductInfoP1?.basic?.productID ?: "")
                )).toBlocking().first()
                if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
                    val newRecommendation = recomData.first()
                    _filterTopAdsProduct.postValue(recommendationDataModel.copy(
                            recomWidgetData = newRecommendation,
                            filterData = selectOrDeselectAnnotationChip(recommendationDataModel.filterData, annotationChip.recommendationFilterChip.name, annotationChip.recommendationFilterChip.isActivated)
                    ))
                    _statusFilterTopAdsProduct.postValue(true.asSuccess())
                } else {
                    _filterTopAdsProduct.postValue(recommendationDataModel.copy(
                            filterData = selectOrDeselectAnnotationChip(recommendationDataModel.filterData, annotationChip.recommendationFilterChip.name, annotationChip.recommendationFilterChip.isActivated)
                    ))
                    _statusFilterTopAdsProduct.postValue(false.asSuccess())
                }
            }
        }) { throwable ->
            _filterTopAdsProduct.postValue(recommendationDataModel.copy(
                    filterData = selectOrDeselectAnnotationChip(recommendationDataModel.filterData, annotationChip.recommendationFilterChip.name, annotationChip.recommendationFilterChip.isActivated)
            ))
            _statusFilterTopAdsProduct.postValue(throwable.asFail())
        }
    }

    private fun selectOrDeselectAnnotationChip(filterData: List<AnnotationChip>?, name: String, isActivated: Boolean): List<AnnotationChip> {
        return filterData?.map {
            it.copy(
                    recommendationFilterChip = it.recommendationFilterChip.copy(
                            isActivated =
                            name == it.recommendationFilterChip.name
                                    && isActivated
                    )
            )
        } ?: listOf()
    }

    fun moveProductToWareHouse(productId: String) {
        launchCatchError(block = {
            moveProductToWarehouseUseCase.get().createParams(productId, userId, deviceId)
            _moveToWarehouseResult.value = moveProductToWarehouseUseCase.get().executeOnBackground().getIsSuccess().asSuccess()
        }) {
            _moveToWarehouseResult.value = it.asFail()
        }
    }

    fun moveProductToEtalase(productId: String, selectedEtalaseId: String, selectedEtalaseName: String) {
        launchCatchError(block = {
            moveProductToEtalaseUseCase.get().createParams(productId, selectedEtalaseId, selectedEtalaseName, userId, deviceId)
            _moveToEtalaseResult.value = moveProductToEtalaseUseCase.get().executeOnBackground().getIsSuccess().asSuccess()
        }) {
            _moveToEtalaseResult.value = it.asFail()
        }
    }

    fun hitAffiliateTracker(affiliateUniqueString: String, deviceId: String) {
        trackAffiliateUseCase.get().params = TrackAffiliateUseCase.createParams(affiliateUniqueString, deviceId)
        trackAffiliateUseCase.get().execute({
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
        submitTicketSubscription = submitHelpTicketUseCase.get().createObservable(requestParams).subscribe(object : Observer<SubmitTicketResult> {
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
        updateCartCounterSubscription = updateCartCounterUseCase.get().createObservable(RequestParams.EMPTY)
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

    fun toggleTeaserNotifyMe(campaignId: Long, productId: Long, source: String) {
        launchCatchError(block = {
            toggleNotifyMeUseCase.get().createParams(campaignId, productId, notifyMeAction, source)
            val isSuccess = toggleNotifyMeUseCase.get().executeOnBackground().result.isSuccess
            if (!isSuccess) _toggleTeaserNotifyMe.value = Throwable().asFail()
            _toggleTeaserNotifyMe.value = isSuccess.asSuccess()
        }) {
            _toggleTeaserNotifyMe.value = it.asFail()
        }
    }

    fun cancelWarehouseUseCase() {
        moveProductToWarehouseUseCase.get().cancelJobs()
    }

    fun cancelEtalaseUseCase() {
        moveProductToEtalaseUseCase.get().cancelJobs()
    }

    fun getDiscussionMostHelpful(productId: String, shopId: String) {
        launchCatchError(block = {
            val response = withContext(dispatcher.io) {
                discussionMostHelpfulUseCase.get().createRequestParams(productId, shopId)
                discussionMostHelpfulUseCase.get().executeOnBackground()
            }
            _discussionMostHelpful.postValue(response.asSuccess())
        }) {
            _discussionMostHelpful.postValue(it.asFail())
        }
    }

    fun shouldHideFloatingButton(): Boolean {
        return p2Data.value?.cartRedirection?.get(getDynamicProductInfoP1?.basic?.productID)?.hideFloatingButton
                ?: false
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
            tradeInParams.weight = it.basic.weight
            if (it.data.getImagePath().isNotEmpty()) {
                tradeInParams.productImage = it.data.getImagePath()[0]
            } else {
                tradeInParams.productImage = it.data.getFirstProductImage()
            }
        }
    }

    private fun updateTradeinParams(validateTradeIn: ValidateTradeIn) {
        tradeInParams.isEligible = if (validateTradeIn.isEligible) 1 else 0
        tradeInParams.usedPrice = validateTradeIn.usedPrice.toIntOrZero()
        tradeInParams.remainingPrice = validateTradeIn.remainingPrice.toIntOrZero()
        tradeInParams.isUseKyc = if (validateTradeIn.useKyc) 1 else 0
        tradeInParams.widgetString = validateTradeIn.widgetString
    }

    private fun getProductInfoP2OtherAsync(productId: String, shopId: Int): Deferred<ProductInfoP2Other> {
        return async(dispatcher.io) {
            getProductInfoP2OtherUseCase.get().executeOnBackground(GetProductInfoP2OtherUseCase.createParams(productId, shopId), forceRefresh)
        }
    }

    private fun getProductInfoP2LoginAsync(shopId: Int, productId: String): Deferred<ProductInfoP2Login> {
        return async(dispatcher.io) {
            getProductInfoP2LoginUseCase.get().requestParams = GetProductInfoP2LoginUseCase.createParams(shopId, productId, isShopOwner())
            getProductInfoP2LoginUseCase.get().setErrorLogListener { logP2Login(it, productId) }
            getProductInfoP2LoginUseCase.get().executeOnBackground()

        }
    }

    private fun getProductInfoP2DataAsync(productId: String, pdpSession: String): Deferred<ProductInfoP2UiData> {
        return async(dispatcher.io) {
            getProductInfoP2DataUseCase.get().setErrorLogListener { logP2Data(it, productId, pdpSession) }
            getProductInfoP2DataUseCase.get().executeOnBackground(
                    GetProductInfoP2DataUseCase.createParams(
                            productId,
                            pdpSession,
                            generatePdpSessionWithDeviceId(),
                            generateUserLocationRequest(userLocationCache)
                    ), forceRefresh
            )
        }
    }

    private fun generatePdpSessionWithDeviceId(): String {
        return if (getDynamicProductInfoP1?.data?.isTradeIn == false) {
            ""
        } else {
            deviceId
        }
    }

    private suspend fun getProductInfoP3(weight: Float, shopDomain: String?, origin: String?): ProductInfoP3 {
        return getProductInfoP3UseCase.get().executeOnBackground(
                GetProductInfoP3UseCase.createParams(weight, shopDomain, origin),
                forceRefresh,
                isUserSessionActive, !isNewShipment)
    }

    private suspend fun getPdpLayout(productId: String, shopDomain: String, productKey: String, whId: String, layoutId: String): ProductDetailDataModel {
        getPdpLayoutUseCase.get().requestParams = GetPdpLayoutUseCase.createParams(productId, shopDomain, productKey, whId, layoutId, generateUserLocationRequest(userLocationCache))
        return getPdpLayoutUseCase.get().executeOnBackground()
    }

    private fun logP2Login(throwable: Throwable, productId: String) {
        ProductDetailLogger.logThrowable(throwable, P2_LOGIN_ERROR_TYPE, productId, deviceId)
    }

    private fun logP2Data(throwable: Throwable, productId: String, pdpSession: String) {
        val extras = mapOf(ProductDetailConstant.SESSION_KEY to pdpSession).toString()
        ProductDetailLogger.logThrowable(throwable, P2_DATA_ERROR_TYPE, productId, deviceId, extras)
    }
}