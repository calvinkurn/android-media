package com.tokopedia.product.detail.view.viewmodel.product_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.affiliatecommon.domain.TrackAffiliateUseCase
import com.tokopedia.analytics.byteio.ProductType
import com.tokopedia.analytics.byteio.TrackConfirmCart
import com.tokopedia.analytics.byteio.TrackConfirmCartResult
import com.tokopedia.analytics.byteio.TrackConfirmSku
import com.tokopedia.analytics.byteio.TrackProductDetail
import com.tokopedia.analytics.byteio.TrackStayProductDetail
import com.tokopedia.analytics.byteio.pdp.AppLogPdp
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
import com.tokopedia.common_sdk_affiliate_toko.model.AdditionalParam
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper.Companion.PARAM_START_SUBID
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.library.subviewmodel.ParentSubViewModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.mapProductsWithProductId
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.ProductDetailPrefetch
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkirImage
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.media.Media
import com.tokopedia.product.detail.common.data.model.pdplayout.ProductInfoP1
import com.tokopedia.product.detail.common.data.model.product.ProductParams
import com.tokopedia.product.detail.common.data.model.rates.ErrorBottomSheet
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimateData
import com.tokopedia.product.detail.common.data.model.rates.ShipmentPlus
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product.detail.common.usecase.ToggleFavoriteUseCase
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.bottom_sheet_edu.BottomSheetEduUiModel
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaRecomBottomSheetData
import com.tokopedia.product.detail.data.model.datamodel.ProductMediaRecomBottomSheetState
import com.tokopedia.product.detail.data.model.datamodel.ProductSingleVariantDataModel
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.product.detail.data.model.ui.OneTimeMethodEvent
import com.tokopedia.product.detail.data.model.ui.OneTimeMethodState
import com.tokopedia.product.detail.data.model.upcoming.NotifyMeUiData
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant.ADS_COUNT
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DEFAULT_PAGE_NUMBER
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DEFAULT_PRICE_MINIMUM_SHIPPING
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DIMEN_ID
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PAGE_SOURCE
import com.tokopedia.product.detail.data.util.ProductDetailMapper
import com.tokopedia.product.detail.data.util.ProductDetailMapper.generateTokoNowRequest
import com.tokopedia.product.detail.data.util.ProductDetailMapper.generateUserLocationRequest
import com.tokopedia.product.detail.data.util.ProductDetailTalkLastAction
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
import com.tokopedia.product.detail.usecase.ToggleNotifyMeUseCase
import com.tokopedia.product.detail.view.util.ProductDetailLogger
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.product.detail.view.viewmodel.product_detail.mediator.GetProductDetailDataMediator
import com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel.PlayWidgetSubViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel.ProductRecommSubViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel.ThumbnailVariantSubViewModel
import com.tokopedia.recommendation_widget_common.affiliate.RecommendationNowAffiliate
import com.tokopedia.recommendation_widget_common.affiliate.RecommendationNowAffiliateData
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.extension.DEFAULT_QTY_1
import com.tokopedia.recommendation_widget_common.extension.PAGENAME_IDENTIFIER_RECOM_ATC
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.topads.sdk.domain.model.TopAdsGetDynamicSlottingDataProduct
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.domain.usecase.GetTopadsIsAdsUseCase
import com.tokopedia.topads.sdk.domain.usecase.GetTopadsIsAdsUseCase.Companion.TIMEOUT_REMOTE_CONFIG_KEY
import com.tokopedia.topads.sdk.domain.usecase.TopAdsImageViewUseCase
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import dagger.Lazy
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ProductDetailViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getPdpLayoutUseCase: Lazy<GetPdpLayoutUseCase>,
    private val getProductInfoP2LoginUseCase: Lazy<GetProductInfoP2LoginUseCase>,
    private val getProductInfoP2OtherUseCase: Lazy<GetProductInfoP2OtherUseCase>,
    private val getP2DataAndMiniCartUseCase: Lazy<GetP2DataAndMiniCartUseCase>,
    private val toggleFavoriteUseCase: Lazy<ToggleFavoriteUseCase>,
    private val deleteWishlistV2UseCase: Lazy<DeleteWishlistV2UseCase>,
    private val addToWishlistV2UseCase: Lazy<AddToWishlistV2UseCase>,
    private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>,
    private val recommendationNowAffiliate: Lazy<RecommendationNowAffiliate>,
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
    private val affiliateEligibilityUseCase: Lazy<AffiliateEligibilityCheckUseCase>,
    private val remoteConfig: RemoteConfig,
    val userSessionInterface: UserSessionInterface,
    private val affiliateCookieHelper: Lazy<AffiliateCookieHelper>,
    private val productRecommSubViewModel: ProductRecommSubViewModel,
    playWidgetSubViewModel: PlayWidgetSubViewModel,
    thumbnailVariantSubViewModel: ThumbnailVariantSubViewModel
) : ParentSubViewModel(
    dispatcher.main,
    productRecommSubViewModel,
    playWidgetSubViewModel,
    thumbnailVariantSubViewModel
),
    IProductRecommSubViewModel by productRecommSubViewModel,
    IPlayWidgetSubViewModel by playWidgetSubViewModel,
    IThumbnailVariantSubViewModel by thumbnailVariantSubViewModel,
    GetProductDetailDataMediator,
    IPdpCartRedirectionButtonsByteIOTrackerDataProvider by PdpCartRedirectionButtonsByteIOTrackerDataProvider() {

    companion object {
        private const val TEXT_ERROR = "ERROR"
        private const val ATC_ERROR_TYPE = "error_atc"
        private const val P2_LOGIN_ERROR_TYPE = "error_p2_login"
        private const val P2_DATA_ERROR_TYPE = "error_p2_data"
        private const val TIMEOUT_QUANTITY_FLOW = 500L
        private const val PARAM_JOB_TIMEOUT = 5000L
        private const val PARAM_TXSC = "txsc"
        private const val CODE_200 = 200
        private const val CODE_300 = 300
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

    private val _toggleFavoriteResult = MutableLiveData<Result<Pair<Boolean, Boolean>>>()
    val toggleFavoriteResult: LiveData<Result<Pair<Boolean, Boolean>>>
        get() = _toggleFavoriteResult

    private val _toggleTeaserNotifyMe = MutableLiveData<Result<NotifyMeUiData>>()
    val toggleTeaserNotifyMe: LiveData<Result<NotifyMeUiData>>
        get() = _toggleTeaserNotifyMe

    private val _discussionMostHelpful =
        MutableLiveData<Result<DiscussionMostHelpfulResponseWrapper>>()
    val discussionMostHelpful: LiveData<Result<DiscussionMostHelpfulResponseWrapper>>
        get() = _discussionMostHelpful

    private val _topAdsImageView: MutableLiveData<Result<ArrayList<TopAdsImageUiModel>>> =
        MutableLiveData()
    val topAdsImageView: LiveData<Result<ArrayList<TopAdsImageUiModel>>>
        get() = _topAdsImageView

    private val _topAdsRecomChargeData =
        MutableLiveData<Result<TopAdsGetDynamicSlottingDataProduct>>()
    val topAdsRecomChargeData: LiveData<Result<TopAdsGetDynamicSlottingDataProduct>>
        get() = _topAdsRecomChargeData

    private val _atcRecom = MutableLiveData<Result<String>>()
    val atcRecom: LiveData<Result<String>> get() = _atcRecom

    private val _atcRecomTracker = MutableLiveData<Result<RecommendationItem>>()
    val atcRecomTracker: LiveData<Result<RecommendationItem>> get() = _atcRecomTracker

    private val _atcRecomTokonowResetCard = SingleLiveEvent<RecommendationItem>()
    val atcRecomTokonowResetCard: LiveData<RecommendationItem> get() = _atcRecomTokonowResetCard

    private val _atcRecomTokonowNonLogin = SingleLiveEvent<RecommendationItem>()
    val atcRecomTokonowNonLogin: LiveData<RecommendationItem> get() = _atcRecomTokonowNonLogin

    private val _oneTimeMethod = MutableStateFlow(OneTimeMethodState())
    val oneTimeMethodState: StateFlow<OneTimeMethodState> = _oneTimeMethod

    private val _finishAnimationAtc = MutableStateFlow(false)
    private val _addToCartState = MutableStateFlow<Result<AddToCartDataModel>?>(null)

    val addToCartResultState
        get() = _finishAnimationAtc.combine(_addToCartState) { finishAtcAnimation, addToCartState ->
            finishAtcAnimation to addToCartState
        }.filter {
            it.first && it.second != null
        }.map {
            _finishAnimationAtc.emit(false)
            _addToCartState.emit(null)
            it.second
        }.filterNotNull().shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    val showBottomSheetEdu: LiveData<BottomSheetEduUiModel?> = p2Data.map {
        val edu = it.bottomSheetEdu
        val showEdu = edu.isShow && edu.appLink.isNotBlank()

        if (showEdu) {
            edu
        } else {
            null
        }
    }

    var videoTrackerData: Pair<Long, Long>? = null
    var getProductInfoP1: ProductInfoP1? = null
    var variantData: ProductVariant? = null
    var listOfParentMedia: MutableList<Media>? = null
    var buttonActionText: String = ""
    var tradeinDeviceId: String = ""
    val impressionHolders = mutableListOf<String>()

    /**
     * These variable are for storing appLog stay-analytics data
     * */
    private var isLoadData: Boolean = false
    private var hasDoneAddToCart: Boolean = false
    val mainPhotoViewed: MutableSet<Int> = mutableSetOf()
    val skuPhotoViewed: MutableSet<Int> = mutableSetOf()
    private val isSingleSku: Boolean
        get() = if (getProductInfoP1?.isProductVariant() == false) {
            true
        } else {
            variantData?.children?.size == 1
        }

    // used only for bringing product id to edit product
    var parentProductId: String? = null
    var shippingMinimumPrice: Double = getProductInfoP1?.basic?.getDefaultOngkirDouble()
        ?: DEFAULT_PRICE_MINIMUM_SHIPPING
    var talkLastAction: ProductDetailTalkLastAction? = null
    private var userLocationCache: LocalCacheModel = LocalCacheModel()
    private var forceRefresh: Boolean = false
    private var shopDomain: String? = null
    private var updateCartCounterSubscription: Subscription? = null

    fun hasShopAuthority(): Boolean = isShopOwner() || getShopInfo().allowManage
    fun isShopOwner(): Boolean =
        isUserSessionActive && userSessionInterface.shopId == getProductInfoP1?.basic?.shopID

    val isUserSessionActive: Boolean
        get() = userSessionInterface.isLoggedIn

    private var _productMediaRecomBottomSheetData: ProductMediaRecomBottomSheetData? = null
    private val _productMediaRecomBottomSheetState =
        MutableLiveData<ProductMediaRecomBottomSheetState>()
    val productMediaRecomBottomSheetState: LiveData<ProductMediaRecomBottomSheetState>
        get() = _productMediaRecomBottomSheetState

    private val _resultAffiliate = MutableLiveData<Result<GenerateAffiliateLinkEligibility>>()

    val resultAffiliate: LiveData<Result<GenerateAffiliateLinkEligibility>>
        get() = _resultAffiliate

    val userId: String
        get() = userSessionInterface.userId

    var deviceId: String = userSessionInterface.deviceId ?: ""

    private var aPlusContentExpanded: Boolean =
        ProductDetailConstant.A_PLUS_CONTENT_DEFAULT_EXPANDED_STATE

    override fun getP1(): ProductInfoP1? = getProductInfoP1

    override fun getP2(): ProductInfoP2UiData? = p2Data.value

    override fun getP2Login(): ProductInfoP2Login? = p2Login.value

    override fun getVariant(): ProductVariant? = variantData

    init {
        iniQuantityFlow()
        registerPdpCartRedirectionButtonsByteIOTrackerDataProvider(mediator = this)
    }

    fun onFinishAnimation() {
        _finishAnimationAtc.tryEmit(true)
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
                getProductInfoP1?.basic?.productID
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
        if (getProductInfoP1?.basic?.isTokoNow == false) return

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
        val p2 = p2Data.value ?: return ShopInfo()
        return p2.shopInfo
    }

    fun getCartTypeByProductId(): CartTypeData? {
        val p2 = p2Data.value ?: return null
        val p1 = getProductInfoP1 ?: return null
        return p2.cartRedirection[p1.basic.productID]
    }

    fun updateLastAction(talkLastAction: ProductDetailTalkLastAction) {
        this.talkLastAction = talkLastAction
    }

    fun getMiniCartItem(): MiniCartItem.MiniCartItemProduct? {
        val p2 = p2Data.value ?: return null
        val miniCart = p2.miniCart ?: return null
        val p1 = getProductInfoP1 ?: return null
        return miniCart[p1.basic.productID]
    }

    fun updateDynamicProductInfoData(data: ProductInfoP1?) {
        data?.let {
            getProductInfoP1 = it
        }
    }

    fun getP2RatesEstimateByProductId(): P2RatesEstimate? {
        val p1 = getProductInfoP1 ?: return null
        val p2 = p2Data.value ?: return null
        val productId = p1.basic.productID

        p2.ratesEstimate.forEach {
            if (productId in it.listfProductId) {
                return it
            }
        }

        return null
    }

    fun getP2RatesEstimateDataByProductId(): P2RatesEstimateData? {
        return getP2RatesEstimateByProductId()?.p2RatesData
    }

    fun getP2ShipmentPlusByProductId(): ShipmentPlus? {
        return getP2RatesEstimateByProductId()?.shipmentPlus
    }

    fun getP2RatesBottomSheetData(): ErrorBottomSheet? {
        return getP2RatesEstimateByProductId()?.errorBottomSheet
    }

    fun getBebasOngkirDataByProductId(): BebasOngkirImage {
        val p1 = getProductInfoP1 ?: return BebasOngkirImage()
        val p2 = p2Data.value ?: return BebasOngkirImage()

        val productId = p1.basic.productID
        val boType = p2.bebasOngkir.boProduct.firstOrNull { it.productId == productId }?.boType ?: 0
        return p2.bebasOngkir.boImages.firstOrNull { it.boType == boType } ?: BebasOngkirImage()
    }

    fun getProductDetailTrack(): TrackProductDetail? {
        val p1 = getProductInfoP1 ?: return null
        val p2 = p2Data.value ?: return null
        Timber.d("Is single sku ${p1.isProductVariant()} ${p1.isProductVariant()}")
        return TrackProductDetail(
            productId = p1.parentProductId,
            productCategory = p1.basic.category.detail.firstOrNull()?.name.orEmpty(),
            productType = p1.productType,
            originalPrice = p1.originalPriceFmt,
            salePrice = p1.data.campaign.priceFmt,
            isSingleSku = isSingleSku
        )
    }

    fun getStayAnalyticsData(): TrackStayProductDetail {
        val p1 = getProductInfoP1
        val mainCount = mainPhotoViewed.count()
        mainPhotoViewed.clear()
        val skuCount = skuPhotoViewed.count()
        skuPhotoViewed.clear()
        return TrackStayProductDetail(
            productId = p1?.parentProductId.orEmpty(),
            productCategory = p1?.basic?.category?.detail?.firstOrNull()?.name.orEmpty(),
            productType = p1?.productType ?: ProductType.NOT_AVAILABLE,
            originalPrice = p1?.originalPriceFmt.orEmpty(),
            salePrice = p1?.data?.campaign?.priceFmt.orEmpty(),
            isLoadData = isLoadData,
            isSingleSku = isSingleSku,
            mainPhotoViewCount = mainCount,
            skuPhotoViewCount = skuCount,
            isAddCartSelected = hasDoneAddToCart,
            isSkuSelected = p1?.isProductVariant() == false
        )
    }

    fun getConfirmCartResultData(): TrackConfirmCartResult {
        val data = getProductInfoP1
        return TrackConfirmCartResult(
            productId = data?.parentProductId.orEmpty(),
            productCategory = data?.basic?.category?.detail?.firstOrNull()?.name.orEmpty(),
            productType = data?.productType ?: ProductType.NOT_AVAILABLE,
            originalPrice = data?.originalPrice.orZero(),
            salePrice = data?.finalPrice.orZero(),
            skuId = data?.basic?.productID.orEmpty(),
            addSkuNum = data?.basic?.minOrder.orZero()
        )
    }

    /**
     * If variant change, make sure this function is called after update product Id
     */
    fun getMultiOriginByProductId(): WarehouseInfo {
        val p1Data = getProductInfoP1 ?: return WarehouseInfo()
        val p2Data = p2Data.value ?: return WarehouseInfo()
        return p2Data.nearestWarehouseInfo[p1Data.basic.productID] ?: WarehouseInfo()
    }

    fun getProductP1(
        productParams: ProductParams,
        refreshPage: Boolean = false,
        layoutId: String = "",
        userLocationLocal: LocalCacheModel,
        urlQuery: String = "",
        extParam: String = "",
        prefetchData: ProductDetailPrefetch.Data? = null
    ) = launch(context = coroutineContext) {
        runCatching {
            processPrefetch(
                prefetchData,
                productParams.productId ?: "",
                refreshPage
            )
            resetVariables(
                shopDomain = productParams.shopDomain.orEmpty(),
                forceRefresh = refreshPage,
                userLocationCache = userLocationLocal
            )

            GetPdpLayoutUseCase.createParams(
                productId = productParams.productId.orEmpty(),
                shopDomain = productParams.shopDomain.orEmpty(),
                productKey = productParams.productName.orEmpty(),
                whId = productParams.warehouseId.orEmpty(),
                layoutId = layoutId,
                userLocationRequest = generateUserLocationRequest(userLocationCache),
                extParam = extParam,
                tokonow = generateTokoNowRequest(userLocationCache),
                refreshPage = refreshPage
            )
        }.onSuccess {
            getPdpLayoutUseCase.get()
                .apply { requestParams = it }
                .executeOnBackground()
                .collect {
                    pdpLayoutCollector(urlQuery = urlQuery, data = it)
                }
        }.onFailure {
            _productLayout.value = it.asFail()
        }
    }

    private fun processPrefetch(
        data: ProductDetailPrefetch.Data?,
        productId: String,
        refreshPage: Boolean
    ) {
        if (data != null && !refreshPage) {
            val prefetch = PDPPrefetch.toProductDetailDataModel(productId, data)
            processPdpLayout(prefetch)
        }
    }

    private fun resetVariables(
        shopDomain: String,
        forceRefresh: Boolean,
        userLocationCache: LocalCacheModel
    ) {
        impressionHolders.clear()
        aPlusContentExpanded = ProductDetailConstant.A_PLUS_CONTENT_DEFAULT_EXPANDED_STATE
        productRecommSubViewModel.onResetAlreadyRecomHit()
        this.shopDomain = shopDomain
        this.forceRefresh = forceRefresh
        this.userLocationCache = userLocationCache
    }

    private fun pdpLayoutCollector(
        urlQuery: String,
        data: kotlin.Result<ProductDetailDataModel>
    ) {
        data.onSuccess {
            val p1 = processPdpLayout(pdpLayout = it)
            getProductP2(p1 = p1, urlQuery = urlQuery)
        }.onFailure {
            _productLayout.value = it.asFail()
        }
    }

    private fun processPdpLayout(pdpLayout: ProductDetailDataModel): ProductInfoP1 {
        /**
         * When wishlist clicked, so viewModel should hit addWishlist api and refresh page.
         * refresh page in p1 the isWishlist field value doesn't updated, should updated after hit p2Login.
         * so then, for keep wishlist value didn't replace from p1, so using previous value
         */
        var p1 = getProductInfoP1 ?: ProductInfoP1()
        val isWishlist = p1.data.isWishlist.orFalse()

        parentProductId = pdpLayout.layoutData.parentProductId
        getProductInfoP1 = pdpLayout.layoutData.let {
            listOfParentMedia = it.data.media.toMutableList()
            it.copy(data = it.data.copy(isWishlist = isWishlist))
        }.also { p1 = it }

        // process variant
        processInitialVariant(pdpLayout = pdpLayout)

        // Remove all component that can be remove by using p1 data
        // So we don't have to inflate to UI
        val processedList = ProductDetailMapper.removeUnusedComponent(
            getProductInfoP1,
            variantData,
            isShopOwner(),
            pdpLayout.listOfLayout
        )

        // Render initial data
        _productLayout.value = processedList.asSuccess()

        return p1
    }

    private fun processInitialVariant(pdpLayout: ProductDetailDataModel) {
        variantData = pdpLayout.variantData.takeIf {
            pdpLayout.layoutData.isProductVariant()
        } ?: return
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
            _addToCartState.emit(it.cause?.asFail() ?: it.asFail())
        }
    }

    private fun sendConfirmCartBytIoTracker() {
        val data = getProductInfoP1 ?: return
        AppLogPdp.sendConfirmCart(
            TrackConfirmCart(
                productId = data.parentProductId,
                productCategory = data.basic.category.detail.firstOrNull()?.name.orEmpty(),
                productType = data.productType,
                originalPrice = data.originalPrice,
                salePrice = data.finalPrice,
                skuId = data.basic.productID,
                addSkuNum = data.basic.minOrder
            )
        )
    }

    private suspend fun getAddToCartUseCase(requestParams: RequestParams) {
        sendConfirmCartBytIoTracker()
        val result = withContext(dispatcher.io) {
            addToCartUseCase.get().createObservable(requestParams).toBlocking().single()
        }

        hasDoneAddToCart = true
        if (result.isStatusError()) {
            val errorMessage = result.getAtcErrorMessage() ?: ""
            if (errorMessage.isNotBlank()) {
                ProductDetailLogger.logMessage(
                    errorMessage,
                    ATC_ERROR_TYPE,
                    getProductInfoP1?.basic?.productID
                        ?: "",
                    deviceId
                )
            }
            _addToCartState.emit(MessageErrorException(errorMessage).asFail())
        } else {
            val isTokoNow = getProductInfoP1?.basic?.isTokoNow ?: false
            if (isTokoNow) {
                updateMiniCartData(
                    result.data.productId,
                    result.data.cartId,
                    result.data.quantity,
                    result.data.notes
                )
            }
            _addToCartState.emit(result.asSuccess())
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
                    getProductInfoP1?.basic?.productID
                        ?: "",
                    deviceId
                )
            }
            _addToCartState.emit(MessageErrorException(errorMessage).asFail())
        } else {
            _addToCartState.emit(result.asSuccess())
        }
    }

    private suspend fun getAddToCartOccUseCase(atcParams: AddToCartOccMultiRequestParams) {
        AppLogPdp.sendConfirmSku(
            TrackConfirmSku(
                productId = getProductInfoP1?.parentProductId.orEmpty(),
                productCategory = getProductInfoP1?.basic?.category?.detail?.firstOrNull()?.name.orEmpty(),
                productType = getProductInfoP1?.productType ?: ProductType.NOT_AVAILABLE,
                originalPrice = getProductInfoP1?.originalPrice.orZero(),
                salePrice = getProductInfoP1?.finalPrice.orZero(),
                skuId = getProductInfoP1?.basic?.productID.orEmpty(),
                isSingleSku = isSingleSku,
                qty = getProductInfoP1?.basic?.minOrder.orZero().toString(),
                isHaveAddress = false
            )
        )
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
                    getProductInfoP1?.basic?.productID
                        ?: "",
                    deviceId
                )
            }
            _addToCartState.emit(MessageErrorException(errorMessage).asFail())
        } else {
            _addToCartState.emit(result.asSuccess())
        }
    }

    private fun getProductP2(p1: ProductInfoP1, urlQuery: String) {
        launch(context = coroutineContext) {
            runCatching {
                if (p1.cacheState.isFromCache) {
                    doBasicProductP2(p1 = p1)
                } else {
                    getProductP2WhenCloud(p1 = p1, urlQuery = urlQuery)
                }
            }.onFailure {
                _productLayout.postValue(it.asFail())
            }
        }
    }

    private suspend fun getProductP2WhenCloud(p1: ProductInfoP1, urlQuery: String) {
        doBasicProductP2(p1 = p1)
        getTopAdsImageViewData(p1.basic.productID)
        getProductTopadsStatus(p1.basic.productID, urlQuery)
    }

    private suspend fun doBasicProductP2(p1: ProductInfoP1) {
        val productLayout = (_productLayout.value as? Success)?.data.orEmpty()
        val hasQuantityEditor = productLayout.any {
            it.name().contains(PAGENAME_IDENTIFIER_RECOM_ATC)
        }
        val p2LoginDeferred: Deferred<ProductInfoP2Login>? = if (isUserSessionActive) {
            getProductInfoP2LoginAsync(
                shopId = p1.basic.shopID,
                productId = p1.basic.productID,
                isFromCache = p1.cacheState.isFromCache
            )
        } else {
            null
        }
        val p2DataDeferred: Deferred<ProductInfoP2UiData> = getProductInfoP2DataAsync(
            productId = p1.basic.productID,
            pdpSession = p1.pdpSession,
            shopId = p1.basic.shopID,
            hasQuantityEditor = p1.basic.isTokoNow || hasQuantityEditor
        )
        val p2OtherDeferred: Deferred<ProductInfoP2Other> =
            getProductInfoP2OtherAsync(p1.basic.productID, p1.basic.shopID)

        p2DataDeferred.await().let { p2 ->
            this@ProductDetailViewModel._p2Data.postValue(p2)
        }

        p2LoginDeferred?.let {
            this@ProductDetailViewModel._p2Login.postValue(it.await())
        }

        this@ProductDetailViewModel._p2Other.postValue(p2OtherDeferred.await())

        isLoadData = true
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
                getP2()?.updateWishlistStatus(productId, false)
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
                getProductInfoP1?.let {
                    getProductInfoP1 = it.copy(data = it.data.copy(isWishlist = true))
                }
                getP2()?.updateWishlistStatus(productId, true)
                listener.onSuccessAddWishlist(result.data, productId)
            } else if (result is Fail) {
                listener.onErrorAddWishList(result.throwable, productId)
            }
        }
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
        launch(context = coroutineContext) {
            runCatching {
                val action = if (isNotifyMeActive) {
                    ProductDetailCommonConstant.VALUE_TEASER_ACTION_UNREGISTER
                } else {
                    ProductDetailCommonConstant.VALUE_TEASER_ACTION_REGISTER
                }

                action to toggleNotifyMeUseCase.get().executeOnBackground(
                    ToggleNotifyMeUseCase.createParams(
                        campaignId,
                        productId,
                        action,
                        ProductDetailCommonConstant.VALUE_TEASER_SOURCE
                    )
                ).result
            }.onSuccess { pair ->
                updateNotifyMeData(productId.toString())
                _toggleTeaserNotifyMe.value = NotifyMeUiData(
                    pair.first,
                    pair.second.isSuccess,
                    pair.second.message
                ).asSuccess()
            }.onFailure {
                _toggleTeaserNotifyMe.value = it.asFail()
            }
        }
    }

    fun getDiscussionMostHelpful(productId: String, shopId: String) {
        launch(context = coroutineContext) {
            runCatching {
                withContext(dispatcher.io) {
                    discussionMostHelpfulUseCase.get().createRequestParams(productId, shopId)
                    discussionMostHelpfulUseCase.get().executeOnBackground()
                }
            }.onSuccess {
                _discussionMostHelpful.postValue(it.asSuccess())
            }.onFailure {
                _discussionMostHelpful.postValue(it.asFail())
            }
        }
    }

    fun shouldHideFloatingButton(): Boolean {
        val cardRedirection = p2Data.value?.cartRedirection ?: return false
        val p1 = getProductInfoP1 ?: return false
        val pid = p1.basic.productID
        val hideFloatingButton = cardRedirection[pid]?.shouldHideFloatingButtonInPdp.orFalse()
        return hideFloatingButton && !isShopOwner()
    }

    fun onAtcRecomNonVariantQuantityChanged(
        recomItem: RecommendationItem,
        quantity: Int,
        recommendationNowAffiliateData: RecommendationNowAffiliateData
    ) {
        if (!userSessionInterface.isLoggedIn) {
            _atcRecomTokonowNonLogin.value = recomItem
        } else {
            if (recomItem.quantity == quantity) return
            val miniCartItem = p2Data.value?.miniCart?.get(recomItem.productId.toString())
            if (quantity == 0) {
                deleteRecomItemFromCart(recomItem, miniCartItem)
            } else if (recomItem.quantity == 0) {
                atcRecomNonVariant(recomItem, quantity, recommendationNowAffiliateData)
            } else {
                updateRecomCartNonVariant(
                    recomItem,
                    quantity,
                    miniCartItem,
                    recommendationNowAffiliateData
                )
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
                    updateRecomAtcStatusAndMiniCart(
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

    fun atcRecomNonVariant(
        recomItem: RecommendationItem,
        quantity: Int,
        recommendationNowAffiliateData: RecommendationNowAffiliateData? = null
    ) {
        launchCatchError(block = {
            val param = AddToCartUseCase.getMinimumParams(
                recomItem.productId.toString(),
                recomItem.shopId.toString(),
                quantity.coerceAtLeast(DEFAULT_QTY_1)
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
                recommendationNowAffiliateData?.let {
                    recommendationNowAffiliate.get()?.initCookieDirectATC(
                        it,
                        recomItem
                    )
                }
                recomItem.cartId = result.data.cartId
                updateRecomAtcStatusAndMiniCart(result.data.message.first(), true, recomItem)
            }
        }) {
            onFailedATCRecomTokonow(it, recomItem)
        }
    }

    fun updateRecomCartNonVariant(
        recomItem: RecommendationItem,
        quantity: Int,
        miniCartItem: MiniCartItem.MiniCartItemProduct?,
        recommendationNowAffiliateData: RecommendationNowAffiliateData
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
                    recommendationNowAffiliate.get()?.initCookieDirectATC(
                        recommendationNowAffiliateData,
                        recomItem
                    )
                    updateRecomAtcStatusAndMiniCart(result.data.message, false, recomItem)
                }
            }
        }) {
            onFailedATCRecomTokonow(it, recomItem)
        }
    }

    fun hitAffiliateCookie(
        productInfo: ProductInfoP1,
        affiliateUuid: String,
        uuid: String,
        affiliateChannel: String,
        affiliateSubIds: Map<String, String>?,
        affiliateSource: String?
    ) {
        launchCatchError(block = {
            val affiliatePageDetail =
                ProductDetailMapper.getAffiliatePageDetail(productInfo)

            val subIds = affiliateSubIds?.mapNotNull {
                val key = it.key.toIntOrNull()
                    ?: if (it.key.length > PARAM_START_SUBID.length) {
                        it.key.substring(
                            PARAM_START_SUBID.length
                        ).toIntOrNull()
                    } else {
                        return@mapNotNull null
                    }

                AdditionalParam(
                    key = key.toString(),
                    value = it.value.replace(" ", "+")
                )
            } ?: emptyList()

            affiliateCookieHelper.get().initCookie(
                affiliateUUID = affiliateUuid,
                affiliateChannel = affiliateChannel,
                affiliatePageDetail = affiliatePageDetail,
                uuid = uuid,
                subIds = subIds,
                source = affiliateSource ?: ""
            )
        }, onError = {
                // no op, expect to be handled by Affiliate SDK
            })
    }

    private fun updateRecomAtcStatusAndMiniCart(
        message: String,
        isAtc: Boolean,
        recomItem: RecommendationItem
    ) {
        _atcRecom.value = message.asSuccess()
        if (isAtc) {
            _atcRecomTracker.value = recomItem.asSuccess()
        }
        getMiniCart(getProductInfoP1?.basic?.shopID ?: "")
    }

    private fun onFailedATCRecomTokonow(throwable: Throwable, recomItem: RecommendationItem) {
        recomItem.onFailedUpdateCart()
        _atcRecom.value = throwable.asFail()
        _atcRecomTokonowResetCard.value = recomItem
    }

    private fun getProductInfoP2OtherAsync(
        productId: String,
        shopId: String
    ): Deferred<ProductInfoP2Other> {
        return async(dispatcher.io) {
            getProductInfoP2OtherUseCase.get().executeOnBackground(
                GetProductInfoP2OtherUseCase.createParams(productId, shopId),
                forceRefresh
            )
        }
    }

    private fun getProductInfoP2LoginAsync(
        shopId: String,
        productId: String,
        isFromCache: Boolean
    ): Deferred<ProductInfoP2Login> {
        return async(dispatcher.io) {
            getProductInfoP2LoginUseCase.get().requestParams =
                GetProductInfoP2LoginUseCase.createParams(
                    shopId,
                    productId,
                    isShopOwner(),
                    isFromCache
                )
            getProductInfoP2LoginUseCase.get().setErrorLogListener { logP2Login(it, productId) }
            getProductInfoP2LoginUseCase.get().executeOnBackground()
        }
    }

    private fun getProductInfoP2DataAsync(
        productId: String,
        pdpSession: String,
        shopId: String,
        hasQuantityEditor: Boolean
    ): Deferred<ProductInfoP2UiData> {
        return async(dispatcher.io) {
            getP2DataAndMiniCartUseCase.get().executeOnBackground(
                requestParams = GetProductInfoP2DataUseCase.createParams(
                    productId,
                    pdpSession,
                    deviceId,
                    generateUserLocationRequest(userLocationCache),
                    generateTokoNowRequest(userLocationCache)
                ),
                hasQuantityEditor = hasQuantityEditor,
                shopId = shopId,
                forceRefresh = forceRefresh,
                isLoggedIn = isUserSessionActive,
                setErrorLogListener = {
                    logP2Data(it, productId, pdpSession)
                }
            )
        }
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

    fun getChildOfVariantSelected(singleVariant: ProductSingleVariantDataModel?): VariantChild? {
        val singleVariant = singleVariant ?: return null
        val mapOfSelectedVariants = singleVariant.mapOfSelectedVariant
        val selectedOptionIds = mapOfSelectedVariants.values.toList()
        val variantDataNonNull = variantData ?: ProductVariant()

        return variantDataNonNull.children.firstOrNull {
            it.optionIds == selectedOptionIds
        }
    }

    fun changeOneTimeMethod(event: OneTimeMethodEvent) {
        when (event) {
            is OneTimeMethodEvent.ImpressRestriction -> {
                if (_oneTimeMethod.value.impressRestriction) return
                _oneTimeMethod.update {
                    it.copy(event = event, impressRestriction = true)
                }
            }

            is OneTimeMethodEvent.ImpressGeneralEduBs -> {
                if (_oneTimeMethod.value.impressGeneralEduBS) return
                _oneTimeMethod.update {
                    it.copy(event = event, impressGeneralEduBS = true)
                }
            }

            else -> {
                // noop
            }
        }
    }

    fun showProductMediaRecomBottomSheet(
        title: String,
        pageName: String,
        productId: String,
        isTokoNow: Boolean
    ) {
        launch(context = dispatcher.main) {
            runCatching {
                val data =
                    _productMediaRecomBottomSheetData.let { productMediaRecomBottomSheetData ->
                        if (
                            productMediaRecomBottomSheetData?.pageName == pageName &&
                            productMediaRecomBottomSheetData.recommendationWidget.recommendationItemList.isNotEmpty()
                        ) {
                            productMediaRecomBottomSheetData
                        } else {
                            setProductMediaRecomBottomSheetLoading(title)
                            loadProductMediaRecomBottomSheetData(pageName, productId, isTokoNow)
                        }
                    }
                setProductMediaRecomBottomSheetData(title, data)
            }.onFailure {
                setProductMediaRecomBottomSheetError(title = title, error = it)
            }
        }
    }

    fun dismissProductMediaRecomBottomSheet() {
        _productMediaRecomBottomSheetState.value = ProductMediaRecomBottomSheetState.Dismissed
    }

    fun checkAffiliateEligibility(affiliatePDPInput: AffiliateInput) {
        launch {
            try {
                val result = affiliateEligibilityUseCase.get().apply {
                    params = AffiliateEligibilityCheckUseCase.createParam(affiliatePDPInput)
                }.executeOnBackground()
                _resultAffiliate.value = Success(result)
            } catch (e: Exception) {
                _resultAffiliate.value = Fail(e)
            }
        }
    }

    private suspend fun loadProductMediaRecomBottomSheetData(
        pageName: String,
        productId: String,
        isTokoNow: Boolean
    ): ProductMediaRecomBottomSheetData {
        val requestParams = GetRecommendationRequestParam(
            pageNumber = DEFAULT_PAGE_NUMBER,
            pageName = pageName,
            productIds = arrayListOf(productId),
            isTokonow = isTokoNow,
            hasNewProductCardEnabled = true
        )
        val response = getRecommendationUseCase
            .get()
            .getData(requestParams)
            .first()
            .copy(title = String.EMPTY)
        return ProductMediaRecomBottomSheetData(
            pageName = pageName,
            recommendationWidget = response
        ).also { _productMediaRecomBottomSheetData = it }
    }

    private fun setProductMediaRecomBottomSheetLoading(title: String) {
        _productMediaRecomBottomSheetState.value = ProductMediaRecomBottomSheetState.Loading(
            title = title
        )
    }

    private fun setProductMediaRecomBottomSheetData(
        title: String,
        data: ProductMediaRecomBottomSheetData
    ) {
        _productMediaRecomBottomSheetState.value = if (
            data.recommendationWidget.recommendationItemList.isEmpty()
        ) {
            ProductMediaRecomBottomSheetState.Dismissed
        } else {
            ProductMediaRecomBottomSheetState.ShowingData(
                title = title,
                recomWidgetData = data.recommendationWidget
            )
        }
    }

    private fun setProductMediaRecomBottomSheetError(
        title: String,
        error: Throwable
    ) {
        _productMediaRecomBottomSheetState.value = ProductMediaRecomBottomSheetState.ShowingError(
            title = title,
            error = error
        )
    }

    fun setAPlusContentExpandedState(expanded: Boolean) {
        aPlusContentExpanded = expanded
    }

    fun isAPlusContentExpanded() = aPlusContentExpanded
}
