package com.tokopedia.product.detail.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.affiliatecommon.domain.TrackAffiliateUseCase
import com.tokopedia.analytics.performance.util.EmbraceKey
import com.tokopedia.analytics.performance.util.EmbraceMonitoring
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartOcsUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.UpdateCartCounterUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.mapProductsWithProductId
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirImage
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.rates.ErrorBottomSheet
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimateData
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product.detail.common.usecase.ToggleFavoriteUseCase
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.product.detail.data.model.upcoming.NotifyMeUiData
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper.generateTokoNowRequest
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper.generateUserLocationRequest
import com.tokopedia.product.detail.data.util.DynamicProductDetailTalkLastAction
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant.ADS_COUNT
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DEFAULT_PAGE_NUMBER
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DEFAULT_PRICE_MINIMUM_SHIPPING
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DIMEN_ID
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PAGE_SOURCE
import com.tokopedia.product.detail.tracking.ProductDetailServerLogger
import com.tokopedia.product.detail.tracking.ProductTopAdsLogger
import com.tokopedia.product.detail.tracking.ProductTopAdsLogger.TOPADS_PDP_BE_ERROR
import com.tokopedia.product.detail.tracking.ProductTopAdsLogger.TOPADS_PDP_GENERAL_ERROR
import com.tokopedia.product.detail.tracking.ProductTopAdsLogger.TOPADS_PDP_HIT_DYNAMIC_SLOTTING
import com.tokopedia.product.detail.tracking.ProductTopAdsLogger.TOPADS_PDP_TIMEOUT_EXCEEDED
import com.tokopedia.product.detail.usecase.DiscussionMostHelpfulUseCase
import com.tokopedia.product.detail.usecase.GetP2DataAndMiniCartUseCase
import com.tokopedia.product.detail.usecase.GetPdpLayoutUseCase
import com.tokopedia.product.detail.usecase.GetProductInfoP2DataUseCase
import com.tokopedia.product.detail.usecase.GetProductInfoP2LoginUseCase
import com.tokopedia.product.detail.usecase.GetProductInfoP2OtherUseCase
import com.tokopedia.product.detail.usecase.GetProductRecommendationUseCase
import com.tokopedia.product.detail.usecase.ToggleNotifyMeUseCase
import com.tokopedia.product.detail.view.util.ProductDetailLogger
import com.tokopedia.product.detail.view.util.ProductDetailVariantLogic
import com.tokopedia.product.detail.view.util.ProductRecommendationMapper
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.AnnotationChip
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.topads.sdk.domain.interactor.GetTopadsIsAdsUseCase
import com.tokopedia.topads.sdk.domain.interactor.GetTopadsIsAdsUseCase.Companion.TIMEOUT_REMOTE_CONFIG_KEY
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsGetDynamicSlottingDataProduct
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.variant_common.util.VariantCommonMapper
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import dagger.Lazy
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

open class DynamicProductDetailViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getPdpLayoutUseCase: Lazy<GetPdpLayoutUseCase>,
    private val getProductInfoP2LoginUseCase: Lazy<GetProductInfoP2LoginUseCase>,
    private val getProductInfoP2OtherUseCase: Lazy<GetProductInfoP2OtherUseCase>,
    private val getP2DataAndMiniCartUseCase: Lazy<GetP2DataAndMiniCartUseCase>,
    private val toggleFavoriteUseCase: Lazy<ToggleFavoriteUseCase>,
    private val deleteWishlistV2UseCase: Lazy<DeleteWishlistV2UseCase>,
    private val addToWishlistV2UseCase: Lazy<AddToWishlistV2UseCase>,
    private val getProductRecommendationUseCase: Lazy<GetProductRecommendationUseCase>,
    private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>,
    private val trackAffiliateUseCase: Lazy<TrackAffiliateUseCase>,
    private val updateCartCounterUseCase: Lazy<UpdateCartCounterUseCase>,
    private val addToCartUseCase: Lazy<AddToCartUseCase>,
    private val addToCartOcsUseCase: Lazy<AddToCartOcsUseCase>,
    private val addToCartOccUseCase: Lazy<AddToCartOccMultiUseCase>,
    private val toggleNotifyMeUseCase: Lazy<ToggleNotifyMeUseCase>,
    private val discussionMostHelpfulUseCase: Lazy<DiscussionMostHelpfulUseCase>,
    private val topAdsImageViewUseCase: Lazy<TopAdsImageViewUseCase>,
    private val miniCartListSimplifiedUseCase: Lazy<GetMiniCartListSimplifiedUseCase>,
    private val updateCartUseCase: Lazy<UpdateCartUseCase>,
    private val deleteCartUseCase: Lazy<DeleteCartUseCase>,
    private val getTopadsIsAdsUseCase: Lazy<GetTopadsIsAdsUseCase>,
    private val playWidgetTools: PlayWidgetTools,
    private val remoteConfig: RemoteConfig,
    val userSessionInterface: UserSessionInterface,
    private val affiliateCookieHelper: Lazy<AffiliateCookieHelper>
) : BaseViewModel(dispatcher.main) {

    companion object {
        private const val TEXT_ERROR = "ERROR"
        private const val ATC_ERROR_TYPE = "error_atc"
        private const val REMOVE_WISHLIST = "false"
        private const val P2_LOGIN_ERROR_TYPE = "error_p2_login"
        private const val P2_DATA_ERROR_TYPE = "error_p2_data"
        private const val TIMEOUT_QUANTITY_FLOW = 500L
        private const val PARAM_JOB_TIMEOUT = 5000L
        private const val PARAM_TXSC = "txsc"
        private const val CODE_200 = 200
        private const val CODE_300 = 300
        private const val VARIANT_LEVEL_TWO_INDEX = 1
        private const val MAX_VARIANT_LEVEL = 2
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

    private val _loadTopAdsProduct = MutableLiveData<Result<RecommendationWidget>>()
    val loadTopAdsProduct: LiveData<Result<RecommendationWidget>>
        get() = _loadTopAdsProduct

    private val _miniCartData = MutableLiveData<Boolean>()
    val miniCartData: LiveData<Boolean>
        get() = _miniCartData

    private val _quantityUpdated = MutableLiveData<Pair<Int, MiniCartItem.MiniCartItemProduct>>()

    private val _updateCartLiveData = MutableLiveData<Result<String>>()
    val updateCartLiveData: LiveData<Result<String>>
        get() = _updateCartLiveData

    private val _deleteCartLiveData = MutableLiveData<Result<String>>()
    val deleteCartLiveData: LiveData<Result<String>>
        get() = _deleteCartLiveData

    private val _filterTopAdsProduct = MutableLiveData<ProductRecommendationDataModel>()
    val filterTopAdsProduct: LiveData<ProductRecommendationDataModel>
        get() = _filterTopAdsProduct

    private val _statusFilterTopAdsProduct = MutableLiveData<Result<Boolean>>()
    val statusFilterTopAdsProduct: LiveData<Result<Boolean>>
        get() = _statusFilterTopAdsProduct

    private val _toggleFavoriteResult = MutableLiveData<Result<Pair<Boolean, Boolean>>>()
    val toggleFavoriteResult: LiveData<Result<Pair<Boolean, Boolean>>>
        get() = _toggleFavoriteResult

    private val _updatedImageVariant = MutableLiveData<Pair<List<VariantCategory>?, String>>()
    val updatedImageVariant: LiveData<Pair<List<VariantCategory>?, String>>
        get() = _updatedImageVariant

    private val _addToCartLiveData = MutableLiveData<Result<AddToCartDataModel>>()
    val addToCartLiveData: LiveData<Result<AddToCartDataModel>>
        get() = _addToCartLiveData

    private val _singleVariantData = MutableLiveData<VariantCategory?>()
    val singleVariantData: LiveData<VariantCategory?>
        get() = _singleVariantData

    private val _onVariantClickedData = MutableLiveData<List<VariantCategory>?>()
    val onVariantClickedData: LiveData<List<VariantCategory>?>
        get() = _onVariantClickedData

    // slicing from _onVariantClickedData, because thumbnail variant feature using vbs for refresh pdp info
    private val _onThumbnailVariantSelectedData = MutableLiveData<ProductSingleVariantDataModel?>()
    val onThumbnailVariantSelectedData: LiveData<ProductSingleVariantDataModel?>
        get() = _onThumbnailVariantSelectedData

    private val _toggleTeaserNotifyMe = MutableLiveData<Result<NotifyMeUiData>>()
    val toggleTeaserNotifyMe: LiveData<Result<NotifyMeUiData>>
        get() = _toggleTeaserNotifyMe

    private val _discussionMostHelpful =
        MutableLiveData<Result<DiscussionMostHelpfulResponseWrapper>>()
    val discussionMostHelpful: LiveData<Result<DiscussionMostHelpfulResponseWrapper>>
        get() = _discussionMostHelpful

    private val _topAdsImageView: MutableLiveData<Result<ArrayList<TopAdsImageViewModel>>> =
        MutableLiveData()
    val topAdsImageView: LiveData<Result<ArrayList<TopAdsImageViewModel>>>
        get() = _topAdsImageView

    private val _topAdsRecomChargeData =
        MutableLiveData<Result<TopAdsGetDynamicSlottingDataProduct>>()
    val topAdsRecomChargeData: LiveData<Result<TopAdsGetDynamicSlottingDataProduct>>
        get() = _topAdsRecomChargeData

    private val _atcRecomTokonow = MutableLiveData<Result<String>>()
    val atcRecomTokonow: LiveData<Result<String>> get() = _atcRecomTokonow

    private val _atcRecomTokonowSendTracker = MutableLiveData<Result<RecommendationItem>>()
    val atcRecomTokonowSendTracker: LiveData<Result<RecommendationItem>> get() = _atcRecomTokonowSendTracker

    private val _atcRecomTokonowResetCard = SingleLiveEvent<RecommendationItem>()
    val atcRecomTokonowResetCard: LiveData<RecommendationItem> get() = _atcRecomTokonowResetCard

    private val _atcRecomTokonowNonLogin = SingleLiveEvent<RecommendationItem>()
    val atcRecomTokonowNonLogin: LiveData<RecommendationItem> get() = _atcRecomTokonowNonLogin

    private val _playWidgetModel = MutableLiveData<Result<PlayWidgetState>>()
    val playWidgetModel: LiveData<Result<PlayWidgetState>> = _playWidgetModel

    private val _playWidgetReminderSwitch = MutableLiveData<Result<PlayWidgetReminderType>>()
    val playWidgetReminderSwitch: LiveData<Result<PlayWidgetReminderType>> =
        _playWidgetReminderSwitch

    private val _verticalRecommendation = MutableLiveData<Result<RecommendationWidget>>()
    val verticalRecommendation: LiveData<Result<RecommendationWidget>> = _verticalRecommendation

    private val _loadViewToView = MutableLiveData<Result<RecommendationWidget>>()
    val loadViewToView: LiveData<Result<RecommendationWidget>>
        get() = _loadViewToView

    var videoTrackerData: Pair<Long, Long>? = null

    var getDynamicProductInfoP1: DynamicProductInfoP1? = null
    var variantData: ProductVariant? = null
    var listOfParentMedia: MutableList<Media>? = null
    var buttonActionText: String = ""
    var tradeinDeviceId: String = ""

    // used only for bringing product id to edit product
    var parentProductId: String? = null
    var shippingMinimumPrice: Double = getDynamicProductInfoP1?.basic?.getDefaultOngkirDouble()
        ?: DEFAULT_PRICE_MINIMUM_SHIPPING
    var talkLastAction: DynamicProductDetailTalkLastAction? = null
    private var userLocationCache: LocalCacheModel = LocalCacheModel()
    private var forceRefresh: Boolean = false
    private var shopDomain: String? = null
    private var alreadyHitRecom: MutableList<String> = mutableListOf()

    private var updateCartCounterSubscription: Subscription? = null

    fun hasShopAuthority(): Boolean = isShopOwner() || getShopInfo().allowManage
    fun isShopOwner(): Boolean =
        isUserSessionActive && userSessionInterface.shopId.toIntOrNull() == getDynamicProductInfoP1?.basic?.getShopId()

    val isUserSessionActive: Boolean
        get() = userSessionInterface.isLoggedIn

    val userId: String
        get() = userSessionInterface.userId

    var deviceId: String = userSessionInterface.deviceId ?: ""

    init {
        iniQuantityFlow()
    }

    fun updateQuantity(quantity: Int, miniCartItem: MiniCartItem.MiniCartItemProduct) {
        _quantityUpdated.value = quantity to miniCartItem
    }

    private fun iniQuantityFlow() {
        launch {
            _quantityUpdated.asFlow()
                .debounce(TIMEOUT_QUANTITY_FLOW)
                .flatMapLatest { request ->
                    hitUpdateCart(request.first, request.second)
                        .catch {
                            emit(it.asFail())
                        }
                }
                .flowOn(dispatcher.io)
                .collect {
                    _updateCartLiveData.value = it
                }
        }
    }

    fun deleteProductInCart(productId: String) {
        launchCatchError(dispatcher.io, block = {
            val selectedMiniCart = p2Data.value?.miniCart?.get(
                getDynamicProductInfoP1?.basic?.productID
                    ?: ""
            ) ?: return@launchCatchError

            deleteCartUseCase.get().setParams(listOf(selectedMiniCart.cartId))
            val data = deleteCartUseCase.get().executeOnBackground()

            _p2Data.value?.miniCart?.remove(productId)
            _deleteCartLiveData.postValue((data.data.message.firstOrNull() ?: "").asSuccess())
        }) {
            _deleteCartLiveData.postValue(it.asFail())
        }
    }

    private fun updateMiniCartData(
        productId: String,
        cartId: String,
        quantity: Int,
        notes: String
    ) {
        if (getDynamicProductInfoP1?.basic?.isTokoNow == false) return

        val miniCartData = _p2Data.value?.miniCart?.get(productId)
        if (miniCartData == null) {
            _p2Data.value?.miniCart?.set(
                productId,
                MiniCartItem.MiniCartItemProduct(
                    cartId = cartId,
                    productId = productId,
                    quantity = quantity,
                    notes = notes
                )
            )
        } else {
            miniCartData.quantity = quantity
        }
    }

    private fun hitUpdateCart(
        quantity: Int,
        request: MiniCartItem.MiniCartItemProduct
    ): Flow<Result<String>> {
        return flow {
            val copyOfMiniCartItem = request.copy(quantity = quantity)
            val updateCartRequest = UpdateCartRequest(
                cartId = copyOfMiniCartItem.cartId,
                quantity = copyOfMiniCartItem.quantity,
                notes = copyOfMiniCartItem.notes
            )
            updateCartUseCase.get().setParams(
                updateCartRequestList = listOf(updateCartRequest),
                source = UpdateCartUseCase.VALUE_SOURCE_PDP_UPDATE_QTY_NOTES
            )
            val result = updateCartUseCase.get().executeOnBackground()

            if (result.error.isNotEmpty()) {
                emit(Throwable(result.error.firstOrNull() ?: "").asFail())
            } else {
                updateMiniCartData(
                    copyOfMiniCartItem.productId,
                    copyOfMiniCartItem.cartId,
                    copyOfMiniCartItem.quantity,
                    copyOfMiniCartItem.notes
                )
                emit((result.data.message).asSuccess())
            }
        }
    }

    fun getUserLocationCache(): LocalCacheModel {
        return userLocationCache
    }

    fun updateVideoTrackerData(stopDuration: Long, videoDuration: Long) {
        videoTrackerData = stopDuration to videoDuration
    }

    fun clearCacheP2Data() {
        getP2DataAndMiniCartUseCase.get().clearCacheP2Data()
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

    fun getMiniCartItem(): MiniCartItem.MiniCartItemProduct? {
        return p2Data.value?.miniCart?.get(getDynamicProductInfoP1?.basic?.productID ?: "")
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
        val boType =
            p2Data.value?.bebasOngkir?.boProduct?.firstOrNull { it.productId == productId }?.boType
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

    fun processVariant(
        data: ProductVariant,
        mapOfSelectedVariant: MutableMap<String, String>?
    ) {
        launchCatchError(dispatcher.io, block = {
            _singleVariantData.postValue(
                ProductDetailVariantLogic.determineVariant(
                    mapOfSelectedOptionIds = mapOfSelectedVariant.orEmpty(),
                    productVariant = data
                )
            )
        }) {}
    }

    fun onVariantClicked(
        data: ProductVariant?,
        mapOfSelectedVariant: MutableMap<String, String>?,
        isPartialySelected: Boolean,
        variantLevel: Int,
        variantId: String
    ) {
        launchCatchError(block = {
            withContext(dispatcher.io) {
                val processedVariant = VariantCommonMapper.processVariant(
                    data,
                    mapOfSelectedVariant,
                    variantLevel,
                    isPartialySelected
                )

                if (isPartialySelected) {
                    _updatedImageVariant.postValue(processedVariant to variantId)
                    return@withContext
                } else {
                    _onVariantClickedData.postValue(processedVariant)
                }
            }
        }) {}
    }

    fun getProductP1(
        productParams: ProductParams,
        refreshPage: Boolean = false,
        layoutId: String = "",
        userLocationLocal: LocalCacheModel,
        urlQuery: String = "",
        extParam: String = ""
    ) {
        launchCatchError(dispatcher.io, block = {
            alreadyHitRecom = mutableListOf()
            shopDomain = productParams.shopDomain
            forceRefresh = refreshPage
            userLocationCache = userLocationLocal
            getPdpLayout(
                productParams.productId ?: "",
                productParams.shopDomain
                    ?: "",
                productParams.productName ?: "",
                productParams.warehouseId
                    ?: "",
                layoutId,
                extParam
            ).also {
                getDynamicProductInfoP1 = it.layoutData.also {
                    listOfParentMedia = it.data.media.toMutableList()
                }

                variantData =
                    if (getDynamicProductInfoP1?.isProductVariant() == false) null else it.variantData
                parentProductId = it.layoutData.parentProductId

                // Remove all component that can be remove by using p1 data
                // So we don't have to inflate to UI
                val processedList = DynamicProductDetailMapper.removeUnusedComponent(
                    getDynamicProductInfoP1,
                    variantData,
                    isShopOwner(),
                    it.listOfLayout
                )

                // Render initial data
                _productLayout.postValue(processedList.asSuccess())
            }
            // Then update the following, it will not throw anything when error
            getProductP2(urlQuery)
        }) {
            _productLayout.postValue(it.asFail())
        }
    }

    fun addToCart(atcParams: Any) {
        launchCatchError(block = {
            val requestParams = RequestParams.create()
            requestParams.putObject(
                AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST,
                atcParams
            )

            when (atcParams) {
                is AddToCartRequestParams -> {
                    getAddToCartUseCase(requestParams)
                }
                is AddToCartOcsRequestParams -> {
                    getAddToCartOcsUseCase(requestParams)
                }
                is AddToCartOccMultiRequestParams -> {
                    getAddToCartOccUseCase(atcParams)
                }
            }
        }) {
            _addToCartLiveData.value = it.cause?.asFail() ?: it.asFail()
        }
    }

    private suspend fun getAddToCartUseCase(requestParams: RequestParams) {
        val result = withContext(dispatcher.io) {
            addToCartUseCase.get().createObservable(requestParams).toBlocking().single()
        }

        EmbraceMonitoring.stopMoments(EmbraceKey.KEY_ACT_ADD_TO_CART)
        if (result.isStatusError()) {
            val errorMessage = result.getAtcErrorMessage() ?: ""
            if (errorMessage.isNotBlank()) {
                ProductDetailLogger.logMessage(
                    errorMessage,
                    ATC_ERROR_TYPE,
                    getDynamicProductInfoP1?.basic?.productID
                        ?: "",
                    deviceId
                )
            }
            _addToCartLiveData.value = MessageErrorException(errorMessage).asFail()
        } else {
            val isTokoNow = getDynamicProductInfoP1?.basic?.isTokoNow ?: false
            if (isTokoNow) {
                updateMiniCartData(
                    result.data.productId.toString(),
                    result.data.cartId,
                    result.data.quantity,
                    result.data.notes
                )
            }

            _addToCartLiveData.value = result.asSuccess()
        }
    }

    private suspend fun getAddToCartOcsUseCase(requestParams: RequestParams) {
        val result = withContext(dispatcher.io) {
            addToCartOcsUseCase.get().createObservable(requestParams).toBlocking().single()
        }
        if (result.isDataError()) {
            val errorMessage = result.errorMessage.firstOrNull() ?: ""
            if (errorMessage.isNotBlank()) {
                ProductDetailLogger.logMessage(
                    errorMessage,
                    ATC_ERROR_TYPE,
                    getDynamicProductInfoP1?.basic?.productID
                        ?: "",
                    deviceId
                )
            }
            _addToCartLiveData.value = MessageErrorException(errorMessage).asFail()
        } else {
            _addToCartLiveData.value = result.asSuccess()
        }
    }

    private suspend fun getAddToCartOccUseCase(atcParams: AddToCartOccMultiRequestParams) {
        val result = withContext(dispatcher.io) {
            addToCartOccUseCase.get().setParams(atcParams).executeOnBackground()
                .mapToAddToCartDataModel()
        }
        if (result.isStatusError()) {
            val errorMessage = result.getAtcErrorMessage() ?: ""
            if (errorMessage.isNotBlank()) {
                ProductDetailLogger.logMessage(
                    errorMessage,
                    ATC_ERROR_TYPE,
                    getDynamicProductInfoP1?.basic?.productID
                        ?: "",
                    deviceId
                )
            }
            _addToCartLiveData.value = MessageErrorException(errorMessage).asFail()
        } else {
            _addToCartLiveData.value = result.asSuccess()
        }
    }

    private suspend fun getProductP2(urlQuery: String = "") {
        getDynamicProductInfoP1?.let {
            val p2LoginDeferred: Deferred<ProductInfoP2Login>? = if (isUserSessionActive) {
                getProductInfoP2LoginAsync(
                    it.basic.getShopId(),
                    it.basic.productID
                )
            } else {
                null
            }
            val p2DataDeffered: Deferred<ProductInfoP2UiData> = getProductInfoP2DataAsync(
                productId = it.basic.productID,
                pdpSession = it.pdpSession,
                shopId = it.basic.shopID,
                isTokoNow = it.basic.isTokoNow
            )
            val p2OtherDeffered: Deferred<ProductInfoP2Other> =
                getProductInfoP2OtherAsync(it.basic.productID, it.basic.getShopId())

            p2DataDeffered.await().let { p2 ->
                _p2Data.postValue(p2)
            }

            p2LoginDeferred?.let {
                _p2Login.postValue(it.await())
            }

            _p2Other.postValue(p2OtherDeffered.await())

            getTopAdsImageViewData(it.basic.productID)
            getProductTopadsStatus(it.basic.productID, urlQuery)
        }
    }

    private fun getTopAdsImageViewData(productID: String) {
        launchCatchError(block = {
            val result = topAdsImageViewUseCase.get().getImageData(
                topAdsImageViewUseCase.get().getQueryMap(
                    "",
                    PAGE_SOURCE,
                    "",
                    ADS_COUNT,
                    DIMEN_ID,
                    "",
                    productID
                )
            )
            _topAdsImageView.postValue(result.asSuccess())
        }) {
            _topAdsImageView.postValue(it.asFail())
        }
    }

    fun toggleFavorite(shopID: String, isNplFollowerType: Boolean = false) {
        launchCatchError(dispatcher.io, block = {
            val requestParams = ToggleFavoriteUseCase.createParams(
                shopID,
                if (isNplFollowerType) ToggleFavoriteUseCase.FOLLOW_ACTION else null
            )
            val favoriteData =
                toggleFavoriteUseCase.get().executeOnBackground(requestParams).followShop
            if (favoriteData?.isSuccess == true) {
                _toggleFavoriteResult.postValue((favoriteData.isSuccess to isNplFollowerType).asSuccess())
            } else {
                _toggleFavoriteResult.postValue(Throwable(favoriteData?.message.orEmpty()).asFail())
            }
        }) {
            _toggleFavoriteResult.postValue(it.asFail())
        }
    }

    fun removeWishListV2(productId: String, listener: WishlistV2ActionListener) {
        launch(dispatcher.main) {
            deleteWishlistV2UseCase.get().setParams(productId, userSessionInterface.userId)
            val result =
                withContext(dispatcher.io) { deleteWishlistV2UseCase.get().executeOnBackground() }
            if (result is Success) {
                listener.onSuccessRemoveWishlist(result.data, productId)
            } else if (result is Fail) {
                listener.onErrorRemoveWishlist(result.throwable, productId)
            }
        }
    }

    fun addWishListV2(productId: String, listener: WishlistV2ActionListener) {
        launch(dispatcher.main) {
            addToWishlistV2UseCase.get().setParams(productId, userSessionInterface.userId)
            val result =
                withContext(dispatcher.io) { addToWishlistV2UseCase.get().executeOnBackground() }
            if (result is Success) {
                listener.onSuccessAddWishlist(result.data, productId)
            } else if (result is Fail) {
                listener.onErrorAddWishList(result.throwable, productId)
            }
        }
    }

    fun loadRecommendation(
        pageName: String,
        productId: String,
        isTokoNow: Boolean,
        miniCart: MutableMap<String, MiniCartItem.MiniCartItemProduct>?
    ) {
        if (GlobalConfig.isSellerApp()) {
            return
        }

        if (!alreadyHitRecom.contains(pageName)) {
            alreadyHitRecom.add(pageName)
        } else {
            return
        }

        launchCatchError(dispatcher.main, block = {
            val response = getProductRecommendationUseCase.get().executeOnBackground(
                GetProductRecommendationUseCase.createParams(
                    productId = productId,
                    pageName = pageName,
                    isTokoNow = isTokoNow,
                    miniCartData = miniCart
                )
            )

            _loadTopAdsProduct.value = response.asSuccess()
        }) {
            _loadTopAdsProduct.value = Throwable(pageName).asFail()
        }
    }

    fun loadViewToView(
        pageName: String,
        productId: String,
        isTokoNow: Boolean
    ) {
        if (GlobalConfig.isSellerApp()) return

        if (!alreadyHitRecom.contains(pageName)) {
            alreadyHitRecom.add(pageName)
        } else {
            return
        }

        launchCatchError(dispatcher.main, block = {
            val response = getRecommendationUseCase.get().getData(
                GetRecommendationRequestParam(
                    pageNumber = DEFAULT_PAGE_NUMBER,
                    pageName = pageName,
                    productIds = arrayListOf(productId),
                    isTokonow = isTokoNow
                )
            )

            _loadViewToView.value = if (response.isNotEmpty()) {
                Success(response.first())
            } else {
                Fail(MessageErrorException())
            }
        }) {
            alreadyHitRecom.remove(pageName)
            _loadViewToView.value = Throwable(pageName, it).asFail()
        }
    }

    fun recommendationChipClicked(
        recommendationDataModel: ProductRecommendationDataModel,
        annotationChip: AnnotationChip,
        productId: String
    ) {
        launchCatchError(dispatcher.io, block = {
            if (!GlobalConfig.isSellerApp()) {
                val requestParams = GetRecommendationRequestParam(
                    pageNumber = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
                    pageName = recommendationDataModel.recomWidgetData?.pageName ?: "",
                    queryParam = if (annotationChip.recommendationFilterChip.isActivated) annotationChip.recommendationFilterChip.value else "",
                    productIds = arrayListOf(productId)
                )
                val recommendationResponse =
                    getRecommendationUseCase.get().getData(requestParams)
                val updatedData = if (recommendationResponse.isNotEmpty() &&
                    recommendationResponse.first().recommendationItemList.isNotEmpty()
                ) {
                    recommendationResponse.first()
                } else {
                    null
                }

                updateFilterTopadsProduct(
                    updatedData,
                    recommendationDataModel,
                    annotationChip
                )
            }
        }) { throwable ->
            updateFilterTopadsProduct(
                null,
                recommendationDataModel,
                annotationChip
            )
            _statusFilterTopAdsProduct.postValue(throwable.asFail())
        }
    }

    private fun updateFilterTopadsProduct(
        updatedData: RecommendationWidget?,
        recommendationDataModel: ProductRecommendationDataModel,
        annotationChip: AnnotationChip
    ) {
        _filterTopAdsProduct.postValue(
            recommendationDataModel.copy(
                recomWidgetData = updatedData ?: recommendationDataModel.recomWidgetData,
                filterData = ProductRecommendationMapper.selectOrDeselectAnnotationChip(
                    filterData = recommendationDataModel.filterData,
                    name = annotationChip.recommendationFilterChip.name,
                    isActivated = annotationChip.recommendationFilterChip.isActivated
                )
            )
        )
    }

    fun getProductTopadsStatus(
        productId: String,
        queryParams: String = ""
    ) {
        if (queryParams.contains(PARAM_TXSC)) {
            launchCatchError(coroutineContext, block = {
                val timeOut = remoteConfig.getLong(TIMEOUT_REMOTE_CONFIG_KEY, PARAM_JOB_TIMEOUT)
                ProductTopAdsLogger.logServer(
                    tag = TOPADS_PDP_HIT_DYNAMIC_SLOTTING,
                    productId = productId,
                    queryParam = queryParams
                )
                val job = withTimeoutOrNull(timeOut) {
                    getTopadsIsAdsUseCase.get().setParams(
                        productId = productId,
                        urlParam = queryParams,
                        pageName = "im_pdp"
                    )
                    val adsStatus = getTopadsIsAdsUseCase.get().executeOnBackground()
                    val errorCode = adsStatus.data.status.error_code
                    val isTopAds = adsStatus.data.productList[0].isCharge
                    if (errorCode in CODE_200..CODE_300 && isTopAds) {
                        _topAdsRecomChargeData.postValue(adsStatus.data.productList[0].asSuccess())
                    } else {
                        ProductTopAdsLogger.logServer(
                            tag = TOPADS_PDP_BE_ERROR,
                            reason = "Error code $errorCode",
                            productId = productId,
                            queryParam = queryParams
                        )
                    }
                    ProductDetailServerLogger.logBreadCrumbTopAdsIsAds(
                        isSuccess = true,
                        errorCode = errorCode,
                        isTopAds = isTopAds
                    )
                }
                if (job == null) {
                    ProductTopAdsLogger.logServer(
                        tag = TOPADS_PDP_TIMEOUT_EXCEEDED,
                        productId = productId,
                        queryParam = queryParams
                    )
                }
            }) {
                it.printStackTrace()
                _topAdsRecomChargeData.postValue(it.asFail())
                ProductDetailServerLogger.logBreadCrumbTopAdsIsAds(
                    isSuccess = false,
                    errorMessage = it.message
                )
                ProductTopAdsLogger.logServer(
                    tag = TOPADS_PDP_GENERAL_ERROR,
                    throwable = it,
                    productId = productId
                )
                // nothing to do since fire and forget
            }
        }
    }

    fun getMiniCart(shopId: String) {
        launchCatchError(dispatcher.io, block = {
            miniCartListSimplifiedUseCase.get()
                .setParams(listOf(shopId), MiniCartSource.PDPRecommendationWidget)
            val result = miniCartListSimplifiedUseCase.get().executeOnBackground()
            val data =
                result.miniCartItems.mapProductsWithProductId().values.associateBy({ it.productId }) {
                    it
                }
            _p2Data.value?.miniCart = data.toMutableMap()
            _miniCartData.postValue(true)
        }) {
        }
    }

    fun hitAffiliateTracker(affiliateUniqueString: String, deviceId: String) {
        trackAffiliateUseCase.get().params =
            TrackAffiliateUseCase.createParams(affiliateUniqueString, deviceId)
        trackAffiliateUseCase.get().execute({
            // no op
        }) {
            Timber.d(it)
        }
    }

    fun updateCartCounerUseCase(onSuccessRequest: (count: Int) -> Unit) {
        updateCartCounterSubscription =
            updateCartCounterUseCase.get().createObservable(RequestParams.EMPTY)
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

    fun toggleTeaserNotifyMe(isNotifyMeActive: Boolean, campaignId: Long, productId: Long) {
        launchCatchError(block = {
            val action = if (isNotifyMeActive) {
                ProductDetailCommonConstant.VALUE_TEASER_ACTION_UNREGISTER
            } else {
                ProductDetailCommonConstant.VALUE_TEASER_ACTION_REGISTER
            }

            val result = toggleNotifyMeUseCase.get().executeOnBackground(
                ToggleNotifyMeUseCase.createParams(
                    campaignId,
                    productId,
                    action,
                    ProductDetailCommonConstant.VALUE_TEASER_SOURCE
                )
            ).result

            updateNotifyMeData(productId.toString())
            _toggleTeaserNotifyMe.value = NotifyMeUiData(
                action,
                result.isSuccess,
                result.message
            ).asSuccess()
        }) {
            _toggleTeaserNotifyMe.value = it.asFail()
        }
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
        return p2Data.value?.cartRedirection?.get(getDynamicProductInfoP1?.basic?.productID)
            ?.hideFloatingButton.orFalse()
    }

    fun onAtcRecomNonVariantQuantityChanged(recomItem: RecommendationItem, quantity: Int) {
        if (!userSessionInterface.isLoggedIn) {
            _atcRecomTokonowNonLogin.value = recomItem
        } else {
            if (recomItem.quantity == quantity) return
            val miniCartItem = p2Data.value?.miniCart?.get(recomItem.productId.toString())
            if (quantity == 0) {
                deleteRecomItemFromCart(recomItem, miniCartItem)
            } else if (recomItem.quantity == 0) {
                atcRecomNonVariant(recomItem, quantity)
            } else {
                updateRecomCartNonVariant(recomItem, quantity, miniCartItem)
            }
        }
    }

    fun deleteRecomItemFromCart(
        recomItem: RecommendationItem,
        miniCartItem: MiniCartItem.MiniCartItemProduct?
    ) {
        launchCatchError(block = {
            miniCartItem?.let {
                deleteCartUseCase.get().setParams(listOf(miniCartItem.cartId))
                val result = deleteCartUseCase.get().executeOnBackground()
                val isFailed =
                    result.data.success == 0 || result.status.equals(TEXT_ERROR, true)
                if (isFailed) {
                    val error = result.errorMessage.firstOrNull()
                        ?: result.data.message.firstOrNull()
                    onFailedATCRecomTokonow(Throwable(error ?: ""), recomItem)
                } else {
                    updateMiniCartAfterATCRecomTokonow(
                        result.data.message.first(),
                        false,
                        recomItem
                    )
                }
            }
        }) {
            onFailedATCRecomTokonow(it, recomItem)
        }
    }

    fun atcRecomNonVariant(recomItem: RecommendationItem, quantity: Int) {
        launchCatchError(block = {
            val param = AddToCartUseCase.getMinimumParams(
                recomItem.productId.toString(),
                recomItem.shopId.toString(),
                quantity
            )
            val result = withContext(dispatcher.io) {
                addToCartUseCase.get().createObservable(param).toBlocking().single()
            }
            if (result.isStatusError()) {
                onFailedATCRecomTokonow(
                    Throwable(
                        result.errorMessage.firstOrNull()
                            ?: result.status
                    ),
                    recomItem
                )
            } else {
                recomItem.cartId = result.data.cartId
                updateMiniCartAfterATCRecomTokonow(result.data.message.first(), true, recomItem)
            }
        }) {
            onFailedATCRecomTokonow(it, recomItem)
        }
    }

    fun updateRecomCartNonVariant(
        recomItem: RecommendationItem,
        quantity: Int,
        miniCartItem: MiniCartItem.MiniCartItemProduct?
    ) {
        launchCatchError(block = {
            miniCartItem?.let {
                val copyOfMiniCartItem =
                    UpdateCartRequest(cartId = it.cartId, quantity = quantity, notes = it.notes)
                updateCartUseCase.get().setParams(
                    updateCartRequestList = listOf(copyOfMiniCartItem),
                    source = UpdateCartUseCase.VALUE_SOURCE_PDP_UPDATE_QTY_NOTES
                )
                val result = updateCartUseCase.get().executeOnBackground()

                if (result.error.isNotEmpty()) {
                    onFailedATCRecomTokonow(
                        Throwable(result.error.firstOrNull() ?: ""),
                        recomItem
                    )
                } else {
                    updateMiniCartAfterATCRecomTokonow(result.data.message, false, recomItem)
                }
            }
        }) {
            onFailedATCRecomTokonow(it, recomItem)
        }
    }

    fun hitAffiliateCookie(
        productInfo: DynamicProductInfoP1,
        affiliateUuid: String,
        uuid: String,
        affiliateChannel: String
    ) {
        launchCatchError(block = {
            val affiliatePageDetail =
                DynamicProductDetailMapper.getAffiliatePageDetail(productInfo)

            affiliateCookieHelper.get().initCookie(
                affiliateUUID = affiliateUuid,
                affiliateChannel = affiliateChannel,
                affiliatePageDetail = affiliatePageDetail,
                uuid = uuid
            )
        }, onError = {
                // no op, expect to be handled by Affiliate SDK
            })
    }

    private fun updateMiniCartAfterATCRecomTokonow(
        message: String,
        isAtc: Boolean,
        recomItem: RecommendationItem
    ) {
        _atcRecomTokonow.value = message.asSuccess()
        if (isAtc) {
            _atcRecomTokonowSendTracker.value = recomItem.asSuccess()
        }
        getMiniCart(getDynamicProductInfoP1?.basic?.shopID ?: "")
    }

    private fun onFailedATCRecomTokonow(throwable: Throwable, recomItem: RecommendationItem) {
        recomItem.onFailedUpdateCart()
        _atcRecomTokonow.value = throwable.asFail()
        _atcRecomTokonowResetCard.value = recomItem
    }

    private fun getProductInfoP2OtherAsync(
        productId: String,
        shopId: Int
    ): Deferred<ProductInfoP2Other> {
        return async(dispatcher.io) {
            getProductInfoP2OtherUseCase.get().executeOnBackground(
                GetProductInfoP2OtherUseCase.createParams(productId, shopId),
                forceRefresh
            )
        }
    }

    private fun getProductInfoP2LoginAsync(
        shopId: Int,
        productId: String
    ): Deferred<ProductInfoP2Login> {
        return async(dispatcher.io) {
            getProductInfoP2LoginUseCase.get().requestParams =
                GetProductInfoP2LoginUseCase.createParams(shopId, productId, isShopOwner())
            getProductInfoP2LoginUseCase.get().setErrorLogListener { logP2Login(it, productId) }
            getProductInfoP2LoginUseCase.get().executeOnBackground()
        }
    }

    private fun getProductInfoP2DataAsync(
        productId: String,
        pdpSession: String,
        shopId: String,
        isTokoNow: Boolean
    ): Deferred<ProductInfoP2UiData> {
        return async(dispatcher.io) {
            getP2DataAndMiniCartUseCase.get().executeOnBackground(
                requestParams = GetProductInfoP2DataUseCase.createParams(
                    productId,
                    pdpSession,
                    generatePdpSessionWithDeviceId(),
                    generateUserLocationRequest(userLocationCache),
                    generateTokoNowRequest(userLocationCache)
                ),
                isTokoNow = isTokoNow,
                shopId = shopId,
                forceRefresh = forceRefresh,
                isLoggedIn = isUserSessionActive,
                setErrorLogListener = {
                    logP2Data(it, productId, pdpSession)
                }
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

    private suspend fun getPdpLayout(
        productId: String,
        shopDomain: String,
        productKey: String,
        whId: String,
        layoutId: String,
        extParam: String
    ): ProductDetailDataModel {
        getPdpLayoutUseCase.get().requestParams = GetPdpLayoutUseCase.createParams(
            productId,
            shopDomain,
            productKey,
            whId,
            layoutId,
            generateUserLocationRequest(userLocationCache),
            extParam,
            generateTokoNowRequest(userLocationCache)
        )
        return getPdpLayoutUseCase.get().executeOnBackground()
    }

    private fun logP2Login(throwable: Throwable, productId: String) {
        ProductDetailLogger.logThrowable(throwable, P2_LOGIN_ERROR_TYPE, productId, deviceId)
    }

    private fun logP2Data(throwable: Throwable, productId: String, pdpSession: String) {
        val extras = mapOf(ProductDetailConstant.SESSION_KEY to pdpSession).toString()
        ProductDetailLogger.logThrowable(
            throwable,
            P2_DATA_ERROR_TYPE,
            productId,
            deviceId,
            extras
        )
    }

    private fun updateNotifyMeData(productId: String) {
        val selectedUpcoming = p2Data.value?.upcomingCampaigns?.get(productId)
        p2Data.value?.upcomingCampaigns?.get(productId)?.notifyMe =
            selectedUpcoming?.notifyMe != true
    }

    fun getPlayWidgetData() {
        launchCatchError(
            block = {
                val productIds = variantData?.let { variant ->
                    listOf(variant.parentId) + variant.children.map { it.productId }
                } ?: emptyList()
                val categoryIds = getDynamicProductInfoP1?.basic?.category?.detail?.map {
                    it.id
                } ?: emptyList()

                val widgetType = PlayWidgetUseCase.WidgetType.PDPWidget(
                    productIds,
                    categoryIds
                )
                val response = playWidgetTools.getWidgetFromNetwork(widgetType)
                val uiModel = playWidgetTools.mapWidgetToModel(response)
                _playWidgetModel.value = Success(uiModel)
            },
            onError = {
                _playWidgetModel.value = Fail(it)
            }
        )
    }

    fun updatePlayWidgetToggleReminder(
        playWidgetState: PlayWidgetState,
        channelId: String,
        reminderType: PlayWidgetReminderType
    ) {
        launchCatchError(block = {
            val updatedUi = playWidgetTools.updateActionReminder(
                playWidgetState,
                channelId,
                reminderType
            )
            _playWidgetModel.value = Success(updatedUi)

            val response = playWidgetTools.updateToggleReminder(channelId, reminderType)
            if (playWidgetTools.mapWidgetToggleReminder(response)) {
                _playWidgetReminderSwitch.value = Success(reminderType)
            } else {
                val reversedToggleUi = playWidgetTools.updateActionReminder(
                    playWidgetState,
                    channelId,
                    reminderType.switch()
                )
                _playWidgetModel.value = Success(reversedToggleUi)
                _playWidgetReminderSwitch.value = Fail(Throwable())
            }
        }, onError = {
                val reversedToggleUi = playWidgetTools.updateActionReminder(
                    playWidgetState,
                    channelId,
                    reminderType.switch()
                )
                _playWidgetModel.value = Success(reversedToggleUi)
                _playWidgetReminderSwitch.value = Fail(it)
            })
    }

    fun getVerticalRecommendationData(
        pageName: String,
        page: Int? = DEFAULT_PAGE_NUMBER,
        productId: String?
    ) {
        val nonNullPage = page ?: DEFAULT_PAGE_NUMBER
        val nonNullProductId = productId.orEmpty()
        launchCatchError(block = {
            val requestParams = GetRecommendationRequestParam(
                pageNumber = nonNullPage,
                pageName = pageName,
                productIds = arrayListOf(nonNullProductId)
            )
            val recommendationResponse = getRecommendationUseCase.get().getData(requestParams)
            val dataResponse = recommendationResponse.firstOrNull()
            if (dataResponse == null) {
                _verticalRecommendation.value = Fail(Throwable())
            } else {
                _verticalRecommendation.value = dataResponse.asSuccess()
            }
        }, onError = {
                _verticalRecommendation.value = Fail(it)
            })
    }

    fun getChildOfVariantSelected(singleVariant: ProductSingleVariantDataModel?): VariantChild? {
        val mapOfSelectedVariants = singleVariant?.mapOfSelectedVariant ?: mutableMapOf()
        val selectedOptionIds = mapOfSelectedVariants.values.toList()
        val variantDataNonNull = variantData ?: ProductVariant()

        return VariantCommonMapper.selectedProductData(
            variantData = variantDataNonNull,
            selectedOptionIds = selectedOptionIds
        )
    }

    /**
     * Thumbnail variant selected is variant level one only
     */
    fun onThumbnailVariantSelected(
        uiData: ProductSingleVariantDataModel?,
        variantId: String,
        categoryKey: String
    ) {
        val singleVariant = uiData ?: return
        val variantSelected = singleVariant.mapOfSelectedVariant
        val variantDataNonNull = variantData ?: ProductVariant()
        val variantSelectUpdated = selectVariantTwoOnThumbnailVariantSelected(
            productVariant = variantDataNonNull,
            variantsSelected = variantSelected,
            newVariantId = variantId,
            newVariantCategoryKey = categoryKey
        )
        val variantLevelOneUpdated = ProductDetailVariantLogic.determineVariant(
            variantSelectUpdated,
            variantDataNonNull
        )

        if (variantLevelOneUpdated != null) {
            _onThumbnailVariantSelectedData.postValue(
                singleVariant.copy(
                    mapOfSelectedVariant = variantSelectUpdated,
                    variantLevelOne = variantLevelOneUpdated
                )
            )
        }
    }

    private fun selectVariantTwoOnThumbnailVariantSelected(
        productVariant: ProductVariant,
        variantsSelected: Map<String, String>,
        newVariantId: String,
        newVariantCategoryKey: String
    ): MutableMap<String, String> {
        val variants = variantsSelected.toMutableMap()
        val variantLevelTwo = productVariant.variants.getOrNull(VARIANT_LEVEL_TWO_INDEX)

        // in case, when swipe media but media is not variant
        if (newVariantId.isEmpty()) {
            // set empty to variant level 1, and keep variant level2 if available
            val variantLevelOne = productVariant.variants.getOrNull(Int.ZERO)

            if (variantLevelOne != null) {
                variants[variantLevelOne.pv.orEmpty()] = ""
            }

            return variants
        }

        // don't move this order, because level 1 always on top and level 2 always below lvl1 in map
        variants[newVariantCategoryKey] = newVariantId

        // if mapOfSelected still don't select yet with variant lvl two, so set default lvl2 with fist lvl2 item
        if (variantLevelTwo != null && variantsSelected.size < MAX_VARIANT_LEVEL) {
            // in case, thumb variant selected but variant two never select from vbs
            val variantLevelTwoId = variantLevelTwo.options.firstOrNull()?.id
            variants[variantLevelTwo.pv.orEmpty()] = variantLevelTwoId.orEmpty()
        }

        return variants
    }
}
