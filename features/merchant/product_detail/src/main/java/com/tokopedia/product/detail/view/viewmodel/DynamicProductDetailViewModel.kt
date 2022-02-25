package com.tokopedia.product.detail.view.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.affiliatecommon.domain.TrackAffiliateUseCase
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
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
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
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product.detail.common.usecase.ToggleFavoriteUseCase
import com.tokopedia.product.detail.data.model.ProductInfoP2Login
import com.tokopedia.product.detail.data.model.ProductInfoP2Other
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.datamodel.DynamicPdpDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductDetailDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductRecommendationDataModel
import com.tokopedia.product.detail.data.model.talk.DiscussionMostHelpfulResponseWrapper
import com.tokopedia.product.detail.data.model.tradein.ValidateTradeIn
import com.tokopedia.product.detail.data.model.upcoming.NotifyMeUiData
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper.generateTokoNowRequest
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper.generateUserLocationRequest
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper.getAffiliateUIID
import com.tokopedia.product.detail.data.util.DynamicProductDetailTalkLastAction
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.data.util.ProductDetailConstant.ADS_COUNT
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DEFAULT_PRICE_MINIMUM_SHIPPING
import com.tokopedia.product.detail.data.util.ProductDetailConstant.DIMEN_ID
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PAGE_SOURCE
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PDP_3
import com.tokopedia.product.detail.data.util.ProductDetailConstant.PDP_K2K
import com.tokopedia.product.detail.data.util.roundToIntOrZero
import com.tokopedia.product.detail.usecase.DiscussionMostHelpfulUseCase
import com.tokopedia.product.detail.usecase.GetP2DataAndMiniCartUseCase
import com.tokopedia.product.detail.usecase.GetPdpLayoutUseCase
import com.tokopedia.product.detail.usecase.GetProductInfoP2DataUseCase
import com.tokopedia.product.detail.usecase.GetProductInfoP2LoginUseCase
import com.tokopedia.product.detail.usecase.GetProductInfoP2OtherUseCase
import com.tokopedia.product.detail.usecase.ToggleNotifyMeUseCase
import com.tokopedia.product.detail.view.util.ProductDetailLogger
import com.tokopedia.product.detail.view.util.ProductDetailVariantLogic
import com.tokopedia.product.detail.view.util.asFail
import com.tokopedia.product.detail.view.util.asSuccess
import com.tokopedia.purchase_platform.common.constant.EmbraceConstant
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.extension.LAYOUTTYPE_HORIZONTAL_ATC
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
import com.tokopedia.topads.sdk.domain.model.TopadsIsAdsQuery
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import com.tokopedia.variant_common.util.VariantCommonMapper
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
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

open class DynamicProductDetailViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                                             private val getPdpLayoutUseCase: Lazy<GetPdpLayoutUseCase>,
                                                             private val getProductInfoP2LoginUseCase: Lazy<GetProductInfoP2LoginUseCase>,
                                                             private val getProductInfoP2OtherUseCase: Lazy<GetProductInfoP2OtherUseCase>,
                                                             private val getP2DataAndMiniCartUseCase: Lazy<GetP2DataAndMiniCartUseCase>,
                                                             private val toggleFavoriteUseCase: Lazy<ToggleFavoriteUseCase>,
                                                             private val removeWishlistUseCase: Lazy<RemoveWishListUseCase>,
                                                             private val addWishListUseCase: Lazy<AddWishListUseCase>,
                                                             private val getRecommendationUseCase: Lazy<GetRecommendationUseCase>,
                                                             private val getRecommendationFilterChips: Lazy<GetRecommendationFilterChips>,
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
                                                             val userSessionInterface: UserSessionInterface) : BaseViewModel(dispatcher.main) {

    companion object {
        private const val TEXT_ERROR = "ERROR"
        private const val ATC_ERROR_TYPE = "error_atc"
        private const val WISHLIST_ERROR_TYPE = "error_wishlist"
        private const val WISHLIST_STATUS_KEY = "wishlist_status"
        private const val ADD_WISHLIST = "true"
        private const val REMOVE_WISHLIST = "false"
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

    private val _loadTopAdsProduct = MutableLiveData<Result<RecommendationWidget>>()
    val loadTopAdsProduct: LiveData<Result<RecommendationWidget>>
        get() = _loadTopAdsProduct

    private val _miniCartData = MutableLiveData<Boolean>()
    val miniCartData: LiveData<Boolean>
        get() = _miniCartData

    private val _quantityUpdated = MutableLiveData<Pair<Int, MiniCartItem>>()

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

    private val _updatedImageVariant = MutableLiveData<Pair<List<VariantCategory>?, List<Media>>>()
    val updatedImageVariant: LiveData<Pair<List<VariantCategory>?, List<Media>>>
        get() = _updatedImageVariant

    private val _addToCartLiveData = MutableLiveData<Result<AddToCartDataModel>>()
    val addToCartLiveData: LiveData<Result<AddToCartDataModel>>
        get() = _addToCartLiveData

    private val _initialVariantData = MutableLiveData<List<VariantCategory>?>()
    val initialVariantData: LiveData<List<VariantCategory>?>
        get() = _initialVariantData

    private val _singleVariantData = MutableLiveData<VariantCategory>()
    val singleVariantData: LiveData<VariantCategory>
        get() = _singleVariantData

    private val _onVariantClickedData = MutableLiveData<List<VariantCategory>?>()
    val onVariantClickedData: LiveData<List<VariantCategory>?>
        get() = _onVariantClickedData

    private val _toggleTeaserNotifyMe = MutableLiveData<Result<NotifyMeUiData>>()
    val toggleTeaserNotifyMe: LiveData<Result<NotifyMeUiData>>
        get() = _toggleTeaserNotifyMe

    private val _discussionMostHelpful = MutableLiveData<Result<DiscussionMostHelpfulResponseWrapper>>()
    val discussionMostHelpful: LiveData<Result<DiscussionMostHelpfulResponseWrapper>>
        get() = _discussionMostHelpful

    private val _topAdsImageView: MutableLiveData<Result<ArrayList<TopAdsImageViewModel>>> = MutableLiveData()
    val topAdsImageView: LiveData<Result<ArrayList<TopAdsImageViewModel>>>
        get() = _topAdsImageView

    private val _topAdsRecomChargeData = MutableLiveData<Result<TopAdsGetDynamicSlottingDataProduct>>()
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

    private val _playWidgetModel = MutableLiveData<Result<PlayWidgetUiModel>>()
    val playWidgetModel: LiveData<Result<PlayWidgetUiModel>> = _playWidgetModel

    private val _playWidgetReminderSwitch = MutableLiveData<Result<PlayWidgetReminderType>>()
    val playWidgetReminderSwitch: LiveData<Result<PlayWidgetReminderType>> = _playWidgetReminderSwitch

    var videoTrackerData: Pair<Long, Long>? = null

    var notifyMeAction: String = ProductDetailCommonConstant.VALUE_TEASER_ACTION_UNREGISTER
    var getDynamicProductInfoP1: DynamicProductInfoP1? = null
    var tradeInParams: TradeInParams = TradeInParams()
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
    fun isShopOwner(): Boolean = isUserSessionActive && userSessionInterface.shopId.toIntOrNull() == getDynamicProductInfoP1?.basic?.getShopId()
    val isUserSessionActive: Boolean
        get() = userSessionInterface.isLoggedIn

    val userId: String
        get() = userSessionInterface.userId

    var deviceId: String = userSessionInterface.deviceId ?: ""


    init {
        iniQuantityFlow()
    }

    fun updateQuantity(quantity: Int, miniCartItem: MiniCartItem) {
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
            val selectedMiniCart = p2Data.value?.miniCart?.get(getDynamicProductInfoP1?.basic?.productID
                    ?: "") ?: return@launchCatchError

            deleteCartUseCase.get().setParams(listOf(selectedMiniCart.cartId))
            val data = deleteCartUseCase.get().executeOnBackground()

            _p2Data.value?.miniCart?.remove(productId)
            _deleteCartLiveData.postValue((data.data.message.firstOrNull() ?: "").asSuccess())
        }) {
            _deleteCartLiveData.postValue(it.asFail())
        }
    }

    private fun updateMiniCartData(productId: String, cartId: String, quantity: Int, notes: String) {
        if (getDynamicProductInfoP1?.basic?.isTokoNow == false) return

        val miniCartData = _p2Data.value?.miniCart?.get(productId)
        if (miniCartData == null) {
            _p2Data.value?.miniCart?.set(productId, MiniCartItem(
                    cartId = cartId,
                    productId = productId,
                    quantity = quantity,
                    notes = notes
            ))
        } else {
            miniCartData.quantity = quantity
        }
    }

    private fun hitUpdateCart(quantity: Int, request: MiniCartItem): Flow<Result<String>> {
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
                updateMiniCartData(copyOfMiniCartItem.productId, copyOfMiniCartItem.cartId, copyOfMiniCartItem.quantity, copyOfMiniCartItem.notes)
                emit((result.data.message).asSuccess())
            }
        }
    }

    override fun flush() {
        super.flush()
        getPdpLayoutUseCase.get().cancelJobs()
        getProductInfoP2LoginUseCase.get().cancelJobs()
        getProductInfoP2OtherUseCase.get().cancelJobs()
        toggleFavoriteUseCase.get().cancelJobs()
        trackAffiliateUseCase.get().cancelJobs()
        getRecommendationUseCase.get().unsubscribe()
        removeWishlistUseCase.get().unsubscribe()
        updateCartCounterSubscription?.unsubscribe()
        addToCartUseCase.get().unsubscribe()
        deleteCartUseCase.get().cancelJobs()
        addToCartOcsUseCase.get().unsubscribe()
        toggleNotifyMeUseCase.get().cancelJobs()
        discussionMostHelpfulUseCase.get().cancelJobs()
        getTopadsIsAdsUseCase.get().cancelJobs()
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

    fun getMiniCartItem(): MiniCartItem? {
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

    fun processVariant(data: ProductVariant, mapOfSelectedVariant: MutableMap<String, String>?, shouldRenderNewVariant: Boolean) {
        launchCatchError(dispatcher.io, block = {
            if (shouldRenderNewVariant) {
                _singleVariantData.postValue(ProductDetailVariantLogic.determineVariant(mapOfSelectedVariant
                        ?: mapOf(), data))
            } else {
                _initialVariantData.postValue(VariantCommonMapper.processVariant(data, mapOfSelectedVariant))
            }
        }) {}
    }

    fun onVariantClicked(data: ProductVariant?, mapOfSelectedVariant: MutableMap<String, String>?,
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

    fun getProductP1(productParams: ProductParams, refreshPage: Boolean = false, layoutId: String = "",
                     userLocationLocal: LocalCacheModel, affiliateUniqueString: String = "", uuid: String = "", urlQuery: String = "", extParam: String = "") {
        launchCatchError(dispatcher.io, block = {
            alreadyHitRecom = mutableListOf()
            shopDomain = productParams.shopDomain
            forceRefresh = refreshPage
            userLocationCache = userLocationLocal
            getPdpLayout(productParams.productId ?: "", productParams.shopDomain
                    ?: "", productParams.productName ?: "", productParams.warehouseId
                    ?: "", layoutId, extParam).also {

                getDynamicProductInfoP1 = it.layoutData.also {
                    listOfParentMedia = it.data.media.toMutableList()
                }

                variantData = if (getDynamicProductInfoP1?.isProductVariant() == false) null else it.variantData
                parentProductId = it.layoutData.parentProductId

                //Create tradein params
                assignTradeinParams()

                //Remove any unused component based on P1 / PdpLayout
                removeDynamicComponent(it.listOfLayout)

                //Render initial data
                _productLayout.postValue(it.listOfLayout.asSuccess())
            }
            // Then update the following, it will not throw anything when error
            getProductP2(affiliateUniqueString, uuid, urlQuery)

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

        EmbraceMonitoring.stopMoments(EmbraceConstant.KEY_EMBRACE_MOMENT_ADD_TO_CART)
        if (result.isStatusError()) {
            val errorMessage = result.getAtcErrorMessage() ?: ""
            if (errorMessage.isNotBlank()) {
                ProductDetailLogger.logMessage(errorMessage, ATC_ERROR_TYPE, getDynamicProductInfoP1?.basic?.productID
                        ?: "", deviceId)
            }
            _addToCartLiveData.value = MessageErrorException(errorMessage).asFail()
        } else {
            val isTokoNow = getDynamicProductInfoP1?.basic?.isTokoNow ?: false
            if (isTokoNow) {
                updateMiniCartData(result.data.productId.toString(), result.data.cartId, result.data.quantity, result.data.notes)
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
                ProductDetailLogger.logMessage(errorMessage, ATC_ERROR_TYPE, getDynamicProductInfoP1?.basic?.productID
                        ?: "", deviceId)
            }
            _addToCartLiveData.value = MessageErrorException(errorMessage).asFail()
        } else {
            _addToCartLiveData.value = result.asSuccess()
        }
    }

    private suspend fun getAddToCartOccUseCase(atcParams: AddToCartOccMultiRequestParams) {
        val result = withContext(dispatcher.io) {
            addToCartOccUseCase.get().setParams(atcParams).executeOnBackground().mapToAddToCartDataModel()
        }
        if (result.isStatusError()) {
            val errorMessage = result.getAtcErrorMessage() ?: ""
            if (errorMessage.isNotBlank()) {
                ProductDetailLogger.logMessage(errorMessage, ATC_ERROR_TYPE, getDynamicProductInfoP1?.basic?.productID
                        ?: "", deviceId)
            }
            _addToCartLiveData.value = MessageErrorException(errorMessage).asFail()
        } else {
            _addToCartLiveData.value = result.asSuccess()
        }
    }

    private suspend fun getProductP2(affiliateUniqueString: String, uuid: String, urlQuery: String = "") {
        getDynamicProductInfoP1?.let {
            val p2LoginDeferred: Deferred<ProductInfoP2Login>? = if (isUserSessionActive) {
                getProductInfoP2LoginAsync(it.basic.getShopId(),
                        it.basic.productID)
            } else null
            val p2DataDeffered: Deferred<ProductInfoP2UiData> = getProductInfoP2DataAsync(it.basic.productID, it.pdpSession, affiliateUniqueString, uuid, it.basic.shopID, it.basic.isTokoNow)
            val p2OtherDeffered: Deferred<ProductInfoP2Other> = getProductInfoP2OtherAsync(it.basic.productID, it.basic.getShopId())

            p2DataDeffered.await().let {
                _p2Data.postValue(it)
                updateTradeinParams(it.validateTradeIn)
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

    private fun removeDynamicComponent(initialLayoutData: MutableList<DynamicPdpDataModel>) {
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
            } else if (it.name() == ProductDetailConstant.PRODUCT_SHIPPING_INFO) {
                it
            } else if (it.name() == ProductDetailConstant.PRODUCT_VARIANT_INFO) {
                it
            } else if (it.name() == ProductDetailConstant.VARIANT_OPTIONS && (!isVariant || isVariantEmpty)) {
                it
            } else if (it.name() == ProductDetailConstant.MINI_VARIANT_OPTIONS && (!isVariant || isVariantEmpty)) {
                it
            } else if (GlobalConfig.isSellerApp() && it.type() == ProductDetailConstant.PRODUCT_LIST) {
                it
            } else if (it.name() == ProductDetailConstant.REPORT && (GlobalConfig.isSellerApp() || isShopOwner())) {
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
                _toggleFavoriteResult.postValue(Throwable(favoriteData?.message.orEmpty()).asFail())
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
                        if (pageName == PDP_3 || pageName == PDP_K2K) {
                            getRecommendationFilterChips.get().setParams(
                                    userId = if (userSessionInterface.userId.isEmpty()) 0 else userSessionInterface.userId.toInt(),
                                    pageName = pageName,
                                    productIDs = productIdsString,
                                    xSource = ProductDetailConstant.DEFAULT_X_SOURCE,
                                    isTokonow = getDynamicProductInfoP1?.basic?.isTokoNow ?: false
                            )
                            recomFilterList.addAll(getRecommendationFilterChips.get().executeOnBackground().filterChip)
                        }

                        val recomData = getRecommendationUseCase.get().createObservable(getRecommendationUseCase.get().getRecomTokonowParams(
                                pageNumber = ProductDetailConstant.DEFAULT_PAGE_NUMBER,
                                pageName = pageName,
                                productIds = productIds,
                                isTokonow = getDynamicProductInfoP1?.basic?.isTokoNow ?: false
                        )).toBlocking().first()

                        if (recomData.isNotEmpty() && recomData.first().recommendationItemList.isNotEmpty()) {
                            recomWidget = recomData.first().copy(
                                    recommendationFilterChips = recomFilterList,
                                    pageName = pageName
                            )
                            if (recomWidget.layoutType == LAYOUTTYPE_HORIZONTAL_ATC) {
                                recomWidget.recommendationItemList.forEach { item ->
                                    _p2Data.value?.miniCart?.let {
                                        if (item.isProductHasParentID()) {
                                            var variantTotalItems = 0
                                            it.values.forEach { miniCartItem ->
                                                if (miniCartItem.productParentId == item.parentID.toString()) {
                                                    variantTotalItems += miniCartItem.quantity
                                                }
                                            }
                                            item.updateItemCurrentStock(variantTotalItems)
                                        } else {
                                            item.updateItemCurrentStock(it[item.productId.toString()]?.quantity
                                                    ?: 0)
                                        }
                                    }
                                }
                            }
                        }

                        recomWidget
                    }
                    //since there is posibility gql return empty page name and recom list
                    //we append UI Page Name to be validated
                    recomData.recomUiPageName = pageName
                    _loadTopAdsProduct.value = recomData.asSuccess()
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

    fun getProductTopadsStatus(
            productId: String,
            queryParams: String = "") {
        if (queryParams.contains(PARAM_TXSC)) {
            launchCatchError(coroutineContext, block = {
                val timeOut = remoteConfig.getLong(TIMEOUT_REMOTE_CONFIG_KEY, PARAM_JOB_TIMEOUT)
                var adsStatus = TopadsIsAdsQuery()

                val job = withTimeoutOrNull(timeOut) {
                    getTopadsIsAdsUseCase.get().setParams(
                            productId = productId,
                            urlParam = queryParams,
                            pageName = "im_pdp"
                    )
                    adsStatus = getTopadsIsAdsUseCase.get().executeOnBackground()
                    val errorCode = adsStatus.data.status.error_code
                    if (errorCode in CODE_200..CODE_300 && adsStatus.data.productList[0].isCharge) {
                        _topAdsRecomChargeData.postValue(adsStatus.data.productList[0].asSuccess())
                    }
                }
            }) {
                it.printStackTrace()
                _topAdsRecomChargeData.postValue(it.asFail())
                //nothing to do since fire and forget
            }
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

    fun getMiniCart(shopId: String) {
        launchCatchError(dispatcher.io, block = {
            miniCartListSimplifiedUseCase.get().setParams(listOf(shopId))
            val result = miniCartListSimplifiedUseCase.get().executeOnBackground()
            val data = result.miniCartItems.associateBy({ it.productId }) {
                it
            }
            _p2Data.value?.miniCart = data.toMutableMap()
            _miniCartData.postValue(true)
        }) {
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

    fun toggleTeaserNotifyMe(isNotifyMeActive: Boolean, campaignId: Long, productId: Long) {
        launchCatchError(block = {
            val action = if (isNotifyMeActive) ProductDetailCommonConstant.VALUE_TEASER_ACTION_UNREGISTER
            else ProductDetailCommonConstant.VALUE_TEASER_ACTION_REGISTER

            val result = toggleNotifyMeUseCase.get().executeOnBackground(
                    ToggleNotifyMeUseCase.createParams(
                            campaignId,
                            productId,
                            action,
                            ProductDetailCommonConstant.VALUE_TEASER_SOURCE
                    )
            ).result

            updateNotifyMeData(productId.toString())
            _toggleTeaserNotifyMe.value = NotifyMeUiData(action,
                    result.isSuccess,
                    result.message).asSuccess()
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
        return p2Data.value?.cartRedirection?.get(getDynamicProductInfoP1?.basic?.productID)?.hideFloatingButton
                ?: false
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

    fun deleteRecomItemFromCart(recomItem: RecommendationItem, miniCartItem: MiniCartItem?) {
        launchCatchError(block = {
            miniCartItem?.let {
                deleteCartUseCase.get().setParams(listOf(miniCartItem.cartId))
                val result = deleteCartUseCase.get().executeOnBackground()
                val isFailed = result.data.success == 0 || result.status.equals(TEXT_ERROR, true)
                if (isFailed) {
                    val error = result.errorMessage.firstOrNull()
                            ?: result.data.message.firstOrNull()
                    onFailedATCRecomTokonow(Throwable(error ?: ""), recomItem)
                } else {
                    updateMiniCartAfterATCRecomTokonow(result.data.message.first(), false, recomItem)
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
                onFailedATCRecomTokonow(Throwable(result.errorMessage.firstOrNull()
                        ?: result.status), recomItem)
            } else {
                recomItem.cartId = result.data.cartId
                updateMiniCartAfterATCRecomTokonow(result.data.message.first(), true, recomItem)
            }
        }) {
            onFailedATCRecomTokonow(it, recomItem)
        }
    }

    fun updateRecomCartNonVariant(recomItem: RecommendationItem, quantity: Int, miniCartItem: MiniCartItem?) {
        launchCatchError(block = {
            miniCartItem?.let {
                val copyOfMiniCartItem = UpdateCartRequest(cartId = it.cartId, quantity = quantity, notes = it.notes)
                updateCartUseCase.get().setParams(
                        updateCartRequestList = listOf(copyOfMiniCartItem),
                        source = UpdateCartUseCase.VALUE_SOURCE_PDP_UPDATE_QTY_NOTES
                )
                val result = updateCartUseCase.get().executeOnBackground()

                if (result.error.isNotEmpty()) {
                    onFailedATCRecomTokonow(Throwable(result.error.firstOrNull() ?: ""), recomItem)
                } else {
                    updateMiniCartAfterATCRecomTokonow(result.data.message, false, recomItem)
                }
            }
        }) {
            onFailedATCRecomTokonow(it, recomItem)
        }

    }

    private fun updateMiniCartAfterATCRecomTokonow(message: String, isAtc: Boolean = false, recomItem: RecommendationItem = RecommendationItem()) {
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

    private fun assignTradeinParams() {
        getDynamicProductInfoP1?.let {
            tradeInParams.categoryId = it.basic.category.id.toIntOrZero()
            tradeInParams.deviceId = deviceId
            tradeInParams.userId = userId.toIntOrZero()
            tradeInParams.setPrice(it.data.price.value.roundToIntOrZero())
            tradeInParams.productId = it.basic.productID
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

    private fun getProductInfoP2DataAsync(productId: String, pdpSession: String, affiliateUniqueString: String, uuid: String, shopId: String, isTokoNow: Boolean): Deferred<ProductInfoP2UiData> {
        return async(dispatcher.io) {
            getP2DataAndMiniCartUseCase.get().executeOnBackground(
                    requestParams = GetProductInfoP2DataUseCase.createParams(
                            productId,
                            pdpSession,
                            generatePdpSessionWithDeviceId(),
                            generateUserLocationRequest(userLocationCache),
                            getAffiliateUIID(affiliateUniqueString, uuid),
                            generateTokoNowRequest(userLocationCache)),
                    isTokoNow = isTokoNow,
                    shopId = shopId,
                    forceRefresh = forceRefresh,
                    isLoggedIn = isUserSessionActive,
                    setErrorLogListener = {
                        logP2Data(it, productId, pdpSession)
                    })
        }
    }

    private fun generatePdpSessionWithDeviceId(): String {
        return if (getDynamicProductInfoP1?.data?.isTradeIn == false) {
            ""
        } else {
            deviceId
        }
    }

    private suspend fun getPdpLayout(productId: String, shopDomain: String, productKey: String, whId: String, layoutId: String, extParam: String): ProductDetailDataModel {
        getPdpLayoutUseCase.get().requestParams = GetPdpLayoutUseCase.createParams(productId, shopDomain, productKey, whId, layoutId, generateUserLocationRequest(userLocationCache), extParam, generateTokoNowRequest(userLocationCache))
        return getPdpLayoutUseCase.get().executeOnBackground()
    }

    private fun logP2Login(throwable: Throwable, productId: String) {
        ProductDetailLogger.logThrowable(throwable, P2_LOGIN_ERROR_TYPE, productId, deviceId)
    }

    private fun logP2Data(throwable: Throwable, productId: String, pdpSession: String) {
        val extras = mapOf(ProductDetailConstant.SESSION_KEY to pdpSession).toString()
        ProductDetailLogger.logThrowable(throwable, P2_DATA_ERROR_TYPE, productId, deviceId, extras)
    }

    private fun updateNotifyMeData(productId:String) {
        val selectedUpcoming = p2Data.value?.upcomingCampaigns?.get(productId)
        p2Data.value?.upcomingCampaigns?.get(productId)?.notifyMe = selectedUpcoming?.notifyMe != true
    }

    fun getPlayWidgetData() {
        launchCatchError(block = {
            val productIds = variantData?.let { variant ->
                listOf(variant.parentId) + variant.children.map { it.productId }
            } ?: emptyList()
            val categoryIds = getDynamicProductInfoP1?.basic?.category?.detail?.map {
                it.id
            } ?: emptyList()

            val widgetType = PlayWidgetUseCase.WidgetType.PDPWidget(
                productIds, categoryIds
            )
            val response = playWidgetTools.getWidgetFromNetwork(widgetType)
            val uiModel = playWidgetTools.mapWidgetToModel(response)
            _playWidgetModel.value = Success(uiModel)
        }, onError = {
            _playWidgetModel.value = Fail(it)
        })
    }

    fun updatePlayWidgetToggleReminder(
        playWidgetUiModel: PlayWidgetUiModel,
        channelId: String,
        reminderType: PlayWidgetReminderType
    ) {
        launchCatchError(block = {
            val updatedUi = playWidgetTools.updateActionReminder(
                playWidgetUiModel, channelId, reminderType
            )
            _playWidgetModel.value = Success(updatedUi)

            val response = playWidgetTools.updateToggleReminder(channelId, reminderType)
            if (playWidgetTools.mapWidgetToggleReminder(response)) {
                _playWidgetReminderSwitch.value = Success(reminderType)
            } else {
                val reversedToggleUi = playWidgetTools.updateActionReminder(
                    playWidgetUiModel, channelId, reminderType.switch()
                )
                _playWidgetModel.value = Success(reversedToggleUi)
                _playWidgetReminderSwitch.value = Fail(Throwable())
            }
        }, onError = {
            val reversedToggleUi = playWidgetTools.updateActionReminder(
                playWidgetUiModel, channelId, reminderType.switch()
            )
            _playWidgetModel.value = Success(reversedToggleUi)
            _playWidgetReminderSwitch.value = Fail(it)
        })
    }

}