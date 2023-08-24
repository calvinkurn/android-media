package com.tokopedia.shop.home.view.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartBundleRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.data.model.request.ProductDetail
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPageConstant.ALL_SHOWCASE_ID
import com.tokopedia.shop.common.constant.ShopPageConstant.CODE_STATUS_SUCCESS
import com.tokopedia.shop.common.constant.ShopPageConstant.RequestParamValue.PAGE_NAME_SHOP_COMPARISON_WIDGET
import com.tokopedia.shop.common.data.mapper.ShopPageWidgetMapper
import com.tokopedia.shop.common.data.model.*
import com.tokopedia.shop.common.domain.GetShopFilterBottomSheetDataUseCase
import com.tokopedia.shop.common.domain.GetShopFilterProductCountUseCase
import com.tokopedia.shop.common.domain.interactor.GQLCheckWishlistUseCase
import com.tokopedia.shop.common.domain.interactor.GqlShopPageGetDynamicTabUseCase
import com.tokopedia.shop.common.domain.interactor.GqlShopPageGetHomeType
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.graphql.domain.usecase.shopsort.GqlGetShopSortUseCase
import com.tokopedia.shop.common.util.ShopAsyncErrorException
import com.tokopedia.shop.common.util.ShopPageExceptionHandler
import com.tokopedia.shop.common.util.ShopPageExceptionHandler.logExceptionToCrashlytics
import com.tokopedia.shop.common.util.ShopPageMapper
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.ShopUtil.setElement
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.home.WidgetName
import com.tokopedia.shop.home.WidgetType
import com.tokopedia.shop.home.data.model.CheckCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.ShopLayoutWidgetParamsModel
import com.tokopedia.shop.home.domain.CheckCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutV2UseCase
import com.tokopedia.shop.home.util.CheckCampaignNplException
import com.tokopedia.shop.home.util.Event
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.shop_widget.thematicwidget.uimodel.ThematicWidgetUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Provider

class ShopHomeViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val getShopProductUseCase: GqlGetShopProductUseCase,
    private val dispatcherProvider: CoroutineDispatchers,
    private val addToCartUseCaseRx: AddToCartUseCase,
    private val addToCartUseCase: com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val addToCartOccUseCase: AddToCartOccMultiUseCase,
    private val addToCartBundleUseCase: AddToCartBundleUseCase,
    private val gqlCheckWishlistUseCase: Provider<GQLCheckWishlistUseCase>,
    private val getYoutubeVideoUseCase: GetYoutubeVideoDetailUseCase,
    private val getCampaignNotifyMeUseCase: Provider<GetCampaignNotifyMeUseCase>,
    private val checkCampaignNotifyMeUseCase: Provider<CheckCampaignNotifyMeUseCase>,
    private val getShopFilterBottomSheetDataUseCase: GetShopFilterBottomSheetDataUseCase,
    private val getShopFilterProductCountUseCase: GetShopFilterProductCountUseCase,
    private val gqlGetShopSortUseCase: GqlGetShopSortUseCase,
    private val shopProductSortMapper: ShopProductSortMapper,
    private val mvcSummaryUseCase: MVCSummaryUseCase,
    private val playWidgetTools: PlayWidgetTools,
    private val gqlShopPageGetHomeType: GqlShopPageGetHomeType,
    private val getShopPageHomeLayoutV2UseCase: Provider<GetShopPageHomeLayoutV2UseCase>,
    private val getShopDynamicTabUseCase: Provider<GqlShopPageGetDynamicTabUseCase>,
    private val getComparisonProductUseCase: Provider<GetRecommendationUseCase>
    ) : BaseViewModel(dispatcherProvider.main) {

    val productListData: LiveData<Result<GetShopHomeProductUiModel>>
        get() = _productListData
    private val _productListData = MutableLiveData<Result<GetShopHomeProductUiModel>>()

    val shopHomeWidgetContentData: Flow<Result<Map<Pair<String, String>, Visitable<*>?>>>
        get() = _shopHomeWidgetContentData
    private val _shopHomeWidgetContentData = MutableSharedFlow<Result<Map<Pair<String, String>, Visitable<*>?>>>()

    val shopHomeWidgetContentDataError: Flow<List<ShopPageWidgetUiModel>>
        get() = _shopHomeWidgetContentDataError
    private val _shopHomeWidgetContentDataError = MutableSharedFlow<List<ShopPageWidgetUiModel>>()

    val shopHomeMerchantVoucherLayoutData: LiveData<Result<ShopHomeVoucherUiModel>>
        get() = _shopHomeMerchantVoucherLayoutData
    private val _shopHomeMerchantVoucherLayoutData = MutableLiveData<Result<ShopHomeVoucherUiModel>>()

    val checkWishlistData: LiveData<Result<List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>?>>>
        get() = _checkWishlistData
    private val _checkWishlistData = MutableLiveData<Result<List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>?>>>()

    val videoYoutube: LiveData<Pair<String, Result<YoutubeVideoDetailModel>>>
        get() = _videoYoutube
    private val _videoYoutube = MutableLiveData<Pair<String, Result<YoutubeVideoDetailModel>>>()

    val campaignNplRemindMeStatusData: LiveData<Result<GetCampaignNotifyMeUiModel>>
        get() = _campaignNplRemindMeStatusData
    private val _campaignNplRemindMeStatusData = MutableLiveData<Result<GetCampaignNotifyMeUiModel>>()

    val campaignFlashSaleStatusData: LiveData<Result<GetCampaignNotifyMeUiModel>>
        get() = _campaignFlashSaleRemindMeStatusData
    private val _campaignFlashSaleRemindMeStatusData = MutableLiveData<Result<GetCampaignNotifyMeUiModel>>()

    val checkCampaignNplRemindMeStatusData: LiveData<Result<CheckCampaignNotifyMeUiModel>>
        get() = _checkCampaignNplRemindMeStatusData
    private val _checkCampaignNplRemindMeStatusData = MutableLiveData<Result<CheckCampaignNotifyMeUiModel>>()

    val checkCampaignFlashSaleRemindMeStatusData: LiveData<Result<CheckCampaignNotifyMeUiModel>>
        get() = _checkCampaignFlashSaleRemindMeStatusData
    private val _checkCampaignFlashSaleRemindMeStatusData = MutableLiveData<Result<CheckCampaignNotifyMeUiModel>>()

    val bottomSheetFilterLiveData: LiveData<Result<DynamicFilterModel>>
        get() = _bottomSheetFilterLiveData
    private val _bottomSheetFilterLiveData = MutableLiveData<Result<DynamicFilterModel>>()

    val shopProductFilterCountLiveData: LiveData<Result<Int>>
        get() = _shopProductFilterCountLiveData
    private val _shopProductFilterCountLiveData = MutableLiveData<Result<Int>>()

    private var sortListData: List<ShopProductSortModel> = listOf()

    val playWidgetObservable: LiveData<CarouselPlayWidgetUiModel?>
        get() = _playWidgetObservable
    private val _playWidgetObservable = MutableLiveData<CarouselPlayWidgetUiModel?>()

    val playWidgetReminderEvent: LiveData<Pair<String, PlayWidgetReminderType>>
        get() = _playWidgetReminderEvent
    private val _playWidgetReminderEvent = MutableLiveData<Pair<String, PlayWidgetReminderType>>()

    val playWidgetReminderObservable: LiveData<Result<PlayWidgetReminderType>>
        get() = _playWidgetReminderObservable
    private val _playWidgetReminderObservable = MutableLiveData<Result<PlayWidgetReminderType>>()

    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()

    val miniCartUpdate: LiveData<Result<UpdateCartV2Data>>
        get() = _miniCartUpdate
    private val _miniCartUpdate = MutableLiveData<Result<UpdateCartV2Data>>()

    val miniCartRemove: LiveData<Result<Pair<String, String>>>
        get() = _miniCartRemove
    private val _miniCartRemove = MutableLiveData<Result<Pair<String, String>>>()

    val updatedShopHomeWidgetQuantityData: LiveData<MutableList<Visitable<*>>>
        get() = _updatedShopHomeWidgetQuantityData
    private val _updatedShopHomeWidgetQuantityData = MutableLiveData<MutableList<Visitable<*>>>()

    val shopPageAtcTracker: LiveData<ShopPageAtcTracker>
        get() = _shopPageAtcTracker
    private val _shopPageAtcTracker = MutableLiveData<ShopPageAtcTracker>()

    val createAffiliateCookieAtcProduct: LiveData<AffiliateAtcProductModel>
        get() = _createAffiliateCookieAtcProduct
    private val _createAffiliateCookieAtcProduct = MutableLiveData<AffiliateAtcProductModel>()

    val isShowHomeTabConfettiLiveData: LiveData<Boolean>
        get() = _isShowHomeTabConfettiLiveData
    private val _isShowHomeTabConfettiLiveData = MutableLiveData<Boolean>()

    val productComparisonLiveData: LiveData<Result<ShopHomePersoProductComparisonUiModel>>
        get() = _productComparisonLiveData
    private val _productComparisonLiveData = MutableLiveData<Result<ShopHomePersoProductComparisonUiModel>>()

    val userSessionShopId: String
        get() = userSession.shopId ?: ""
    val isLogin: Boolean
        get() = userSession.isLoggedIn
    val userId: String
        get() = userSession.userId

    private var miniCartData: MiniCartSimplifiedData? = null

    val latestShopHomeWidgetLayoutData: LiveData<Result<ShopPageLayoutUiModel>>
        get() = _latestShopHomeWidgetLayoutData
    private val _latestShopHomeWidgetLayoutData = MutableLiveData<Result<ShopPageLayoutUiModel>>()

    val bannerTimerRemindMeStatusData: LiveData<Result<GetCampaignNotifyMeUiModel>>
        get() = _bannerTimerRemindMeStatusData
    private val _bannerTimerRemindMeStatusData = MutableLiveData<Result<GetCampaignNotifyMeUiModel>>()

    //TODO need to check if we can combine other CheckCampaignNotifyMeUiModel live data since it is the same
    val checkBannerTimerRemindMeStatusData: LiveData<Result<CheckCampaignNotifyMeUiModel>>
        get() = _checkBannerTimerRemindMeStatusData
    private val _checkBannerTimerRemindMeStatusData = MutableLiveData<Result<CheckCampaignNotifyMeUiModel>>()

    private val _homeWidgetListVisitable = MutableLiveData<Result<List<Visitable<*>>>>()
    val homeWidgetListVisitable: LiveData<Result<List<Visitable<*>>>>
        get() = _homeWidgetListVisitable

    private val _updatedBannerTimerUiModelData = MutableLiveData<ShopWidgetDisplayBannerTimerUiModel?>()
    val updatedBannerTimerUiModelData: LiveData<ShopWidgetDisplayBannerTimerUiModel?>
        get() = _updatedBannerTimerUiModelData

    private val _productCarouselWidgetData = MutableLiveData<Result<ShopHomeProductCarouselUiModel>>()
    val productCarouselWidgetData: LiveData<Result<ShopHomeProductCarouselUiModel>>
        get() = _productCarouselWidgetData

    fun getNewProductList(
        shopId: String,
        page: Int,
        productPerPage: Int,
        shopProductFilterParameter: ShopProductFilterParameter,
        widgetUserAddressLocalData: LocalCacheModel,
        isEnableDirectPurchase: Boolean
    ) {
        launchCatchError(block = {
            val listProductData = withContext(dispatcherProvider.io) {
                getProductListData(
                    shopId,
                    page,
                    productPerPage,
                    shopProductFilterParameter,
                    widgetUserAddressLocalData,
                    isEnableDirectPurchase
                )
            }
            _productListData.postValue(Success(listProductData))
        }) {
            _productListData.postValue(Fail(it))
        }
    }

    fun getMerchantVoucherCoupon(
        shopId: String,
        context: Context?,
        shopHomeVoucherUiModel: ShopHomeVoucherUiModel
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            var uiModel = shopHomeVoucherUiModel
            val response = mvcSummaryUseCase.getResponse(mvcSummaryUseCase.getQueryParams(shopId))
            val widgetMasterId = uiModel.widgetMasterId
            uiModel = uiModel.copy(
                data = ShopPageMapper.mapToVoucherCouponUiModel(response.data, shopId),
                isError = false
            )
            uiModel.widgetMasterId = widgetMasterId
            val code = response.data?.resultStatus?.code
            if (code != CODE_STATUS_SUCCESS) {
                val errorMessage = ErrorHandler.getErrorMessage(context, MessageErrorException(response.data?.resultStatus?.message.toString()))
                logExceptionToCrashlytics(
                    ShopPageExceptionHandler.ERROR_WHEN_GET_MERCHANT_VOUCHER_DATA,
                    Throwable(errorMessage)
                )
            }
            _shopHomeMerchantVoucherLayoutData.postValue(Success(uiModel))
        }) {
            _shopHomeMerchantVoucherLayoutData.postValue(Fail(it))
        }
    }

    fun addProductToCart(
        product: ShopHomeProductUiModel,
        shopId: String,
        onSuccessAddToCart: (dataModelAtc: DataModel) -> Unit,
        onErrorAddToCart: (exception: Throwable) -> Unit
    ) {
        launchCatchError(block = {
            val addToCartSubmitData = withContext(dispatcherProvider.io) {
                submitAddProductToCart(shopId, product)
            }
            if (addToCartSubmitData.data.success == ShopPageConstant.ATC_SUCCESS_VALUE) {
                onSuccessAddToCart(addToCartSubmitData.data)
                checkShouldCreateAffiliateCookieAtcProduct(ShopPageAtcTracker.AtcType.ADD, product)
            } else {
                onErrorAddToCart(MessageErrorException(addToCartSubmitData.data.message.first()))
            }
        }) {
            onErrorAddToCart(it)
        }
    }

    fun addProductToCartOcc(
        product: ShopHomeProductUiModel,
        shopId: String,
        onSuccessAddToCartOcc: (dataModelAtc: DataModel) -> Unit,
        onErrorAddToCartOcc: (exception: Throwable) -> Unit
    ) {
        launchCatchError(block = {
            val addToCartOccSubmitData = withContext(dispatcherProvider.io) {
                submitAddProductToCartOcc(shopId, product)
            }
            if (addToCartOccSubmitData.data.success == ShopPageConstant.ATC_SUCCESS_VALUE) {
                onSuccessAddToCartOcc(addToCartOccSubmitData.data)
                checkShouldCreateAffiliateCookieAtcProduct(ShopPageAtcTracker.AtcType.ADD, product)
            } else {
                onErrorAddToCartOcc(MessageErrorException(addToCartOccSubmitData.data.message.first()))
            }
        }) {
            onErrorAddToCartOcc(it)
        }
    }

    fun addBundleToCart(
        shopId: String,
        userId: String,
        bundleId: String,
        productDetails: List<ShopHomeBundleProductUiModel>,
        onFinishAddToCart: (atcBundleModel: AddToCartBundleModel) -> Unit,
        onErrorAddBundleToCart: (exception: Throwable) -> Unit,
        productQuantity: Int
    ) {
        launchCatchError(block = {
            val bundleProductDetails = productDetails.map {
                ProductDetail(
                    productId = it.productId,
                    quantity = productQuantity,
                    shopId = shopId,
                    customerId = userId
                )
            }

            val atcBundleParams = AddToCartBundleRequestParams(
                shopId = shopId,
                bundleId = bundleId,
                bundleQty = ShopHomeProductBundleItemUiModel.DEFAULT_BUNDLE_QUANTITY,
                selectedProductPdp = ShopHomeProductBundleItemUiModel.DEFAULT_BUNDLE_PRODUCT_PARENT_ID,
                productDetails = bundleProductDetails
            )

            val atcBundleResult = withContext(dispatcherProvider.io) {
                submitAddBundleToCart(atcBundleParams)
            }

            onFinishAddToCart(atcBundleResult)
        }) {
            onErrorAddBundleToCart(it)
        }
    }

    fun clearGetShopProductUseCase() {
        getShopProductUseCase.clearCache()
        gqlShopPageGetHomeType.clearCache()
    }

    fun getWishlistStatus(shopHomeCarousellProductUiModel: List<ShopHomeCarousellProductUiModel>) {
        launchCatchError(block = {
            val listResultCheckWishlist = shopHomeCarousellProductUiModel.map {
                asyncCatchError(
                    dispatcherProvider.io,
                    block = {
                        val resultCheckWishlist = checkListProductWishlist(it)
                        Pair(it, resultCheckWishlist)
                    },
                    onError = { null }
                )
            }.awaitAll()
            _checkWishlistData.postValue(Success(listResultCheckWishlist))
        }) {}
    }

    private suspend fun getProductListData(
        shopId: String,
        page: Int,
        productPerPage: Int,
        shopProductFilterParameter: ShopProductFilterParameter,
        widgetUserAddressLocalData: LocalCacheModel,
        isEnableDirectPurchase: Boolean
    ): GetShopHomeProductUiModel {
        getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(
            shopId,
            ShopProductFilterInput().apply {
                etalaseMenu = ALL_SHOWCASE_ID
                this.page = page
                sort = shopProductFilterParameter.getSortId().toIntOrZero()
                rating = shopProductFilterParameter.getRating()
                pmax = shopProductFilterParameter.getPmax()
                pmin = shopProductFilterParameter.getPmin()
                fcategory = shopProductFilterParameter.getCategory()
                userDistrictId = widgetUserAddressLocalData.district_id
                userCityId = widgetUserAddressLocalData.city_id
                userLat = widgetUserAddressLocalData.lat
                userLong = widgetUserAddressLocalData.long
                extraParam = shopProductFilterParameter.getExtraParam()
            }
        )
        val productListResponse = getShopProductUseCase.executeOnBackground()
        return mapToShopHomeProductUiModel(
            shopId,
            page,
            productPerPage,
            productListResponse,
            isEnableDirectPurchase
        )
    }

    private fun mapToShopHomeProductUiModel(
        shopId: String,
        page: Int,
        productPerPage: Int,
        productListResponse: ShopProduct.GetShopProduct,
        isEnableDirectPurchase: Boolean
    ): GetShopHomeProductUiModel {
        val isHasNextPage = ShopUtil.isHasNextPage(page, productPerPage, productListResponse.totalData)
        val productListUiModelData = productListResponse.data.map {
            ShopPageHomeMapper.mapToHomeProductViewModelForAllProduct(
                it,
                ShopUtil.isMyShop(shopId, userSessionShopId),
                isEnableDirectPurchase
            )
        }
        val totalProductListData = productListResponse.totalData
        return GetShopHomeProductUiModel(
            isHasNextPage,
            productListUiModelData,
            totalProductListData,
            page
        ).apply {
            updateProductCardQuantity(listShopProductUiModel.toMutableList())
        }
    }

    private fun getMatchedMiniCartItem(
        shopHomeProductUiModel: ShopHomeProductUiModel
    ): List<MiniCartItem.MiniCartItemProduct> {
        return miniCartData?.let { miniCartSimplifiedData ->
            val isVariant = shopHomeProductUiModel.isVariant
            val listMatchedMiniCartItemProduct = if (isVariant) {
                miniCartSimplifiedData.miniCartItems.values.filterIsInstance<MiniCartItem.MiniCartItemProduct>()
                    .filter { it.productParentId == shopHomeProductUiModel.parentId }
            } else {
                val childProductId = shopHomeProductUiModel.id
                miniCartSimplifiedData.miniCartItems.getMiniCartItemProduct(childProductId)?.let {
                    listOf(it)
                }.orEmpty()
            }
            listMatchedMiniCartItemProduct.filter { !it.isError }
        }.orEmpty()
    }

    private suspend fun getSortListData(): List<ShopProductSortModel> {
        val listSort = gqlGetShopSortUseCase.executeOnBackground()
        return shopProductSortMapper.convertSort(listSort)
    }

    fun getVideoYoutube(videoUrl: String, widgetId: String) {
        launchCatchError(block = {
            getYoutubeVideoUseCase.setVideoUrl(videoUrl)
            val result = withContext(dispatcherProvider.io) {
                convertToYoutubeResponse(getYoutubeVideoUseCase.executeOnBackground())
            }
            _videoYoutube.postValue(Pair(widgetId, Success(result)))
        }, onError = {
                _videoYoutube.postValue(Pair(widgetId, Fail(it)))
            })
    }

    private fun convertToYoutubeResponse(typeRestResponseMap: Map<Type, RestResponse>): YoutubeVideoDetailModel {
        return typeRestResponseMap[YoutubeVideoDetailModel::class.java]?.getData() as YoutubeVideoDetailModel
    }

    private fun submitAddProductToCart(shopId: String, product: ShopHomeProductUiModel): AddToCartDataModel {
        val requestParams = AddToCartUseCase.getMinimumParams(
            product.id,
            shopId,
            productName = product.name,
            price = product.displayedPrice,
            userId = userId,
            atcExternalSource = AtcFromExternalSource.ATC_FROM_SHOP
        )
        return addToCartUseCaseRx.createObservable(requestParams).toBlocking().first()
    }

    private suspend fun submitAddProductToCartOcc(shopId: String, product: ShopHomeProductUiModel): AddToCartDataModel {
        return addToCartOccUseCase.setParams(
            AddToCartOccMultiRequestParams(
                carts = listOf(
                    AddToCartOccMultiCartParam(
                        productId = product.id,
                        shopId = shopId,
                        quantity = product.minimumOrder.toString(),
                        productName = product.name,
                        price = product.displayedPrice
                    )
                ),
                userId = userId
            )
        ).executeOnBackground().mapToAddToCartDataModel()
    }

    private suspend fun submitAddBundleToCart(atcBundleParams: AddToCartBundleRequestParams): AddToCartBundleModel {
        addToCartBundleUseCase.setParams(atcBundleParams)
        return addToCartBundleUseCase.executeOnBackground()
    }

    private suspend fun checkListProductWishlist(
        shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel
    ): List<CheckWishlistResult> {
        val useCase = gqlCheckWishlistUseCase.get()
        val listProductIdString = mutableListOf<String>().apply {
            shopHomeCarousellProductUiModel.productList.onEach {
                add(it.id)
            }
        }.joinToString(separator = ",")
        useCase.params = GQLCheckWishlistUseCase.createParams(listProductIdString)
        return useCase.executeOnBackground()
    }

    fun clearCache() {
        clearGetShopProductUseCase()
    }

    fun getCampaignNplRemindMeStatus(model: ShopHomeNewProductLaunchCampaignUiModel.NewProductLaunchCampaignItem) {
        launchCatchError(block = {
            val getCampaignNotifyMeModel = withContext(dispatcherProvider.io) {
                val campaignId = model.campaignId
                getCampaignNotifyMe(campaignId)
            }
            val getCampaignNotifyMeUiModel = ShopPageHomeMapper.mapToGetCampaignNotifyMeUiModel(
                getCampaignNotifyMeModel
            )
            _campaignNplRemindMeStatusData.value = Success(getCampaignNotifyMeUiModel)
        }) {}
    }

    fun getCampaignFlashSaleRemindMeStatus(campaignId: String) {
        launchCatchError(block = {
            val getCampaignNotifyMeModel = withContext(dispatcherProvider.io) {
                getCampaignNotifyMe(campaignId)
            }
            val getCampaignNotifyMeUiModel = ShopPageHomeMapper.mapToGetCampaignNotifyMeUiModel(
                getCampaignNotifyMeModel
            )
            _campaignFlashSaleRemindMeStatusData.value = Success(getCampaignNotifyMeUiModel)
        }) {}
    }

    private suspend fun getCampaignNotifyMe(campaignId: String): GetCampaignNotifyMeModel {
        val useCase = getCampaignNotifyMeUseCase.get()
        useCase.params = GetCampaignNotifyMeUseCase.createParams(campaignId)
        return useCase.executeOnBackground()
    }

    fun clickRemindMe(campaignId: String, action: String) {
        launchCatchError(block = {
            val checkCampaignNotifyMeModel = withContext(dispatcherProvider.io) {
                checkCampaignNotifyMe(campaignId, action)
            }
            val checkCampaignNotifyMeUiModel = CheckCampaignNotifyMeUiModel(
                checkCampaignNotifyMeModel.campaignId,
                checkCampaignNotifyMeModel.success,
                checkCampaignNotifyMeModel.message,
                checkCampaignNotifyMeModel.errorMessage,
                action
            )
            _checkCampaignNplRemindMeStatusData.postValue(Success(checkCampaignNotifyMeUiModel))
        }) {
            _checkCampaignNplRemindMeStatusData.postValue(
                Fail(
                    CheckCampaignNplException(
                        it.cause,
                        it.message,
                        campaignId
                    )
                )
            )
        }
    }

    fun clickFlashSaleReminder(campaignId: String, action: String) {
        launchCatchError(block = {
            val checkCampaignNotifyMeModel = withContext(dispatcherProvider.io) {
                checkCampaignNotifyMe(campaignId, action)
            }
            val checkCampaignNotifyMeUiModel = CheckCampaignNotifyMeUiModel(
                checkCampaignNotifyMeModel.campaignId,
                checkCampaignNotifyMeModel.success,
                checkCampaignNotifyMeModel.message,
                checkCampaignNotifyMeModel.errorMessage,
                action
            )
            _checkCampaignFlashSaleRemindMeStatusData.postValue(Success(checkCampaignNotifyMeUiModel))
        }) {
            _checkCampaignFlashSaleRemindMeStatusData.postValue(
                Fail(
                    CheckCampaignNplException(
                        it.cause,
                        it.message,
                        campaignId
                    )
                )
            )
        }
    }

    fun clickBannerTimerReminder(campaignId: String, action: String) {
        launchCatchError(block = {
            val checkCampaignNotifyMeModel = withContext(dispatcherProvider.io) {
                checkCampaignNotifyMe(campaignId, action)
            }
            val checkCampaignNotifyMeUiModel = CheckCampaignNotifyMeUiModel(
                checkCampaignNotifyMeModel.campaignId,
                checkCampaignNotifyMeModel.success,
                checkCampaignNotifyMeModel.message,
                checkCampaignNotifyMeModel.errorMessage,
                action
            )
            _checkBannerTimerRemindMeStatusData.postValue(Success(checkCampaignNotifyMeUiModel))
        }) {
            _checkBannerTimerRemindMeStatusData.postValue(
                Fail(
                    CheckCampaignNplException(
                        it.cause,
                        it.message,
                        campaignId
                    )
                )
            )
        }
    }

    private suspend fun checkCampaignNotifyMe(campaignId: String, action: String): CheckCampaignNotifyMeModel {
        return checkCampaignNotifyMeUseCase.get().run {
            params = CheckCampaignNotifyMeUseCase.createParams(
                campaignId,
                action
            )
            executeOnBackground()
        }
    }

    fun getBottomSheetFilterData(shopId: String = "") {
        launchCatchError(coroutineContext, block = {
            val filterBottomSheetData = withContext(dispatcherProvider.io) {
                getShopFilterBottomSheetDataUseCase.params = GetShopFilterBottomSheetDataUseCase.createParams(shopId)
                getShopFilterBottomSheetDataUseCase.executeOnBackground()
            }
            filterBottomSheetData.data.let {
                it.filter = it.filter.filter { filterItem ->
                    ShopUtil.isFilterNotIgnored(filterItem.title)
                }
            }
            _bottomSheetFilterLiveData.postValue(Success(filterBottomSheetData))
        }) {
        }
    }

    fun getFilterResultCount(
        shopId: String,
        productPerPage: Int,
        tempShopProductFilterParameter: ShopProductFilterParameter,
        widgetUserAddressLocalData: LocalCacheModel
    ) {
        launchCatchError(block = {
            val filterResultProductCount = withContext(dispatcherProvider.io) {
                getFilterResultCountData(shopId, productPerPage, tempShopProductFilterParameter, widgetUserAddressLocalData)
            }
            _shopProductFilterCountLiveData.postValue(Success(filterResultProductCount))
        }) {}
    }

    private suspend fun getFilterResultCountData(
        shopId: String,
        productPerPage: Int,
        tempShopProductFilterParameter: ShopProductFilterParameter,
        widgetUserAddressLocalData: LocalCacheModel
    ): Int {
        val filter = ShopProductFilterInput(
            ShopPageConstant.START_PAGE,
            productPerPage,
            "",
            ALL_SHOWCASE_ID,
            tempShopProductFilterParameter.getSortId().toIntOrZero(),
            tempShopProductFilterParameter.getRating(),
            tempShopProductFilterParameter.getPmax(),
            tempShopProductFilterParameter.getPmin(),
            tempShopProductFilterParameter.getCategory(),
            widgetUserAddressLocalData.district_id,
            widgetUserAddressLocalData.city_id,
            widgetUserAddressLocalData.lat,
            widgetUserAddressLocalData.long,
            tempShopProductFilterParameter.getExtraParam()
        )
        getShopFilterProductCountUseCase.params = GetShopFilterProductCountUseCase.createParams(
            shopId,
            filter
        )
        return getShopFilterProductCountUseCase.executeOnBackground()
    }

    fun getSortNameById(sortId: String): String {
        return sortListData.firstOrNull {
            it.value == sortId
        }?.name.orEmpty()
    }

    /**
     * Play widget
     */
    fun getPlayWidget(
        carouselPlayWidgetUiModel: CarouselPlayWidgetUiModel,
        playWidgetType: PlayWidgetUseCase.WidgetType
    ) {
        launchCatchError(block = {
            val response = playWidgetTools.getWidgetFromNetwork(playWidgetType, dispatcherProvider.io)
            val widgetUiModel = playWidgetTools.mapWidgetToModel(widgetResponse = response, prevState = _playWidgetObservable.value?.playWidgetState)
            _playWidgetObservable.postValue(
                carouselPlayWidgetUiModel.copy(
                    actionEvent = Event(CarouselPlayWidgetUiModel.Action.Refresh),
                    playWidgetState = widgetUiModel
                )
            )
        }) {
            _playWidgetObservable.postValue(null)
        }
    }

    fun updatePlayWidgetTotalView(channelId: String?, totalView: String?) {
        if (channelId == null || totalView == null) return

        updateWidget {
            it.copy(playWidgetState = playWidgetTools.updateTotalView(it.playWidgetState, channelId, totalView))
        }
    }

    fun updatePlayWidgetReminder(channelId: String?, isReminder: Boolean) {
        if (channelId == null) return

        updateWidget {
            val reminderType = if (isReminder) PlayWidgetReminderType.Reminded else PlayWidgetReminderType.NotReminded
            it.copy(playWidgetState = playWidgetTools.updateActionReminder(it.playWidgetState, channelId, reminderType))
        }
    }

    fun shouldUpdatePlayWidgetToggleReminder(channelId: String, reminderType: PlayWidgetReminderType) {
        if (!isLogin) {
            _playWidgetReminderEvent.value = Pair(channelId, reminderType)
        } else {
            updatePlayWidgetToggleReminder(channelId, reminderType)
        }
    }

    private fun updatePlayWidgetToggleReminder(channelId: String, reminderType: PlayWidgetReminderType) {
        updateWidget {
            it.copy(playWidgetState = playWidgetTools.updateActionReminder(it.playWidgetState, channelId, reminderType))
        }

        launchCatchError(block = {
            val response = playWidgetTools.updateToggleReminder(
                channelId,
                reminderType,
                dispatcherProvider.io
            )

            when (playWidgetTools.mapWidgetToggleReminder(response)) {
                true -> {
                    _playWidgetReminderObservable.postValue(Success(reminderType))
                }
                else -> {
                    updateWidget {
                        it.copy(playWidgetState = playWidgetTools.updateActionReminder(it.playWidgetState, channelId, reminderType.switch()))
                    }
                    _playWidgetReminderObservable.postValue(Fail(Throwable()))
                }
            }
        }) { throwable ->
            updateWidget {
                it.copy(playWidgetState = playWidgetTools.updateActionReminder(it.playWidgetState, channelId, reminderType.switch()))
            }
            _playWidgetReminderObservable.postValue(Fail(throwable))
        }
    }

    fun deleteChannel(channelId: String) {
        updateWidget {
            it.copy(playWidgetState = playWidgetTools.updateDeletingChannel(it.playWidgetState, channelId))
        }

        launchCatchError(block = {
            playWidgetTools.deleteChannel(
                channelId,
                userSessionShopId
            )

            updateWidget {
                it.copy(
                    playWidgetState = playWidgetTools.updateDeletedChannel(it.playWidgetState, channelId),
                    actionEvent = Event(CarouselPlayWidgetUiModel.Action.Delete(channelId))
                )
            }
        }, onError = { err ->
                updateWidget {
                    it.copy(
                        playWidgetState = playWidgetTools.updateFailedDeletingChannel(it.playWidgetState, channelId),
                        actionEvent = Event(CarouselPlayWidgetUiModel.Action.DeleteFailed(channelId, err))
                    )
                }
            })
    }

    private fun updateWidget(onUpdate: (oldVal: CarouselPlayWidgetUiModel) -> CarouselPlayWidgetUiModel) {
        val currentValue = _playWidgetObservable.value
        if (currentValue != null) {
            _playWidgetObservable.value = onUpdate(currentValue)
        }
    }

    fun getWidgetContentData(
        listWidgetLayout: List<ShopPageWidgetUiModel>,
        shopId: String,
        widgetUserAddressLocalData: LocalCacheModel,
        isThematicWidgetShown: Boolean,
        isEnableDirectPurchase: Boolean,
        isOverrideTheme: Boolean,
        colorSchema: ShopPageColorSchema
    ) {
        println(listWidgetLayout)

        launchCatchError(block = {
            val responseWidgetContent = withContext(dispatcherProvider.io) {
                val useCase = getShopPageHomeLayoutV2UseCase.get()
                useCase.params = GetShopPageHomeLayoutV2UseCase.createParams(
                    ShopLayoutWidgetParamsModel(
                        shopId = shopId,
                        districtId = widgetUserAddressLocalData.district_id,
                        cityId = widgetUserAddressLocalData.city_id,
                        latitude = widgetUserAddressLocalData.lat,
                        longitude = widgetUserAddressLocalData.long,
                        listWidgetRequest = ShopPageWidgetMapper.mapToShopPageWidgetRequest(listWidgetLayout)
                    )
                )
                useCase.executeOnBackground()
            }
            val listShopHomeWidget = ShopPageHomeMapper.mapToListShopHomeWidget(
                responseWidgetContent.listWidget,
                ShopUtil.isMyShop(shopId, userSessionShopId),
                isLogin,
                isThematicWidgetShown,
                isEnableDirectPurchase,
                shopId,
                listWidgetLayout,
                isOverrideTheme,
                colorSchema
            )
            updateProductCardQuantity(listShopHomeWidget.toMutableList())
            val mapShopHomeWidgetData = mutableMapOf<Pair<String, String>, Visitable<*>?>().apply {
                listWidgetLayout.onEach {
                    val widgetLayoutId = it.widgetId
                    val matchedWidget = listShopHomeWidget.firstOrNull { shopHomeWidget ->
                        when (shopHomeWidget) {
                            is BaseShopHomeWidgetUiModel -> {
                                shopHomeWidget.widgetId == widgetLayoutId
                            }
                            is ThematicWidgetUiModel -> {
                                shopHomeWidget.widgetId == widgetLayoutId
                            }
                            else -> {
                                false
                            }
                        }
                    }
                    if (matchedWidget != null) {
                        put(Pair(it.widgetId, it.widgetMasterId), matchedWidget)
                    } else {
                        put(Pair(it.widgetId, it.widgetMasterId), null)
                    }
                }
            }
            _shopHomeWidgetContentData.emit(Success(mapShopHomeWidgetData))
        }) {
            _shopHomeWidgetContentDataError.emit(listWidgetLayout)
            _shopHomeWidgetContentData.emit(
                Fail(
                    ShopAsyncErrorException(
                        ShopAsyncErrorException.AsyncQueryType.SHOP_PAGE_GET_LAYOUT_V2,
                        it
                    )
                )
            )
        }
    }

    fun getProductGridListWidgetData(
        shopId: String,
        productPerPage: Int,
        shopProductFilterParameter: ShopProductFilterParameter,
        initialProductListData: ShopProduct.GetShopProduct?,
        widgetUserAddressLocalData: LocalCacheModel,
        isEnableDirectPurchase: Boolean
    ) {
        launchCatchError(block = {
            val productList = asyncCatchError(
                dispatcherProvider.io,
                block = {
                    if (initialProductListData == null) {
                        getProductListData(
                            shopId,
                            ShopPageConstant.START_PAGE,
                            productPerPage,
                            shopProductFilterParameter,
                            widgetUserAddressLocalData,
                            isEnableDirectPurchase
                        )
                    } else {
                        null
                    }
                },
                onError = { null }
            )

            val sortResponse = asyncCatchError(
                dispatcherProvider.io,
                block = {
                    getSortListData()
                },
                onError = {
                    null
                }
            )
            sortResponse.await()?.let {
                if (initialProductListData == null) {
                    productList.await()?.let { productListData ->
                        _productListData.postValue(Success(productListData))
                    }
                } else {
                    _productListData.postValue(
                        Success(
                            mapToShopHomeProductUiModel(
                                shopId,
                                productPerPage,
                                ShopPageConstant.START_PAGE,
                                initialProductListData,
                                isEnableDirectPurchase
                            )
                        )
                    )
                }
                it.let { sortResponse ->
                    sortListData = sortResponse
                }
            }
        }) {
        }
    }

    fun handleAtcFlow(
        quantity: Int,
        shopId: String,
        componentName: String,
        shopHomeProductUiModel: ShopHomeProductUiModel
    ) {
        miniCartData?.let {
            val miniCartItem = getMiniCartItem(it, shopHomeProductUiModel.id)
            when {
                miniCartItem == null -> addItemToCart(
                    shopId,
                    quantity,
                    componentName,
                    shopHomeProductUiModel
                )
                quantity.isZero() -> removeItemCart(miniCartItem, componentName, shopHomeProductUiModel)
                else -> updateItemCart(miniCartItem, quantity, componentName, shopHomeProductUiModel)
            }
        }
    }

    private fun addItemToCart(
        shopId: String,
        quantity: Int,
        componentName: String,
        shopHomeProductUiModel: ShopHomeProductUiModel
    ) {
        val addToCartRequestParams = com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase.getMinimumParams(
            productId = shopHomeProductUiModel.id,
            shopId = shopId,
            quantity = quantity,
            atcExternalSource = AtcFromExternalSource.ATC_FROM_SHOP
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            val atcType = ShopPageAtcTracker.AtcType.ADD
            trackAddToCart(
                it.data.cartId,
                it.data.productId.toString(),
                shopHomeProductUiModel.name,
                shopHomeProductUiModel.displayedPrice,
                shopHomeProductUiModel.isVariant,
                it.data.quantity,
                atcType,
                componentName
            )
            checkShouldCreateAffiliateCookieAtcProduct(atcType, shopHomeProductUiModel)
            _miniCartAdd.postValue(Success(it))
        }, {
            _miniCartAdd.postValue(Fail(it))
        })
    }

    private fun updateItemCart(
        miniCartItem: MiniCartItem.MiniCartItemProduct,
        quantity: Int,
        componentName: String,
        shopHomeProductUiModel: ShopHomeProductUiModel
    ) {
        val existingQuantity = miniCartItem.quantity
        miniCartItem.quantity = quantity
        val cartId = miniCartItem.cartId
        val updateCartRequest = UpdateCartRequest(
            cartId = cartId,
            quantity = miniCartItem.quantity,
            notes = miniCartItem.notes
        )
        updateCartUseCase.setParams(
            updateCartRequestList = listOf(updateCartRequest),
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES
        )
        updateCartUseCase.execute({
            val atcType = if (quantity < existingQuantity) {
                ShopPageAtcTracker.AtcType.UPDATE_REMOVE
            } else {
                ShopPageAtcTracker.AtcType.UPDATE_ADD
            }
            trackAddToCart(
                miniCartItem.cartId,
                miniCartItem.productId,
                shopHomeProductUiModel.name,
                shopHomeProductUiModel.displayedPrice,
                shopHomeProductUiModel.isVariant,
                miniCartItem.quantity,
                atcType,
                componentName
            )
            checkShouldCreateAffiliateCookieAtcProduct(atcType, shopHomeProductUiModel)
            _miniCartUpdate.value = Success(it)
        }, {
            _miniCartUpdate.postValue(Fail(it))
        })
    }

    private fun checkShouldCreateAffiliateCookieAtcProduct(
        atcType: ShopPageAtcTracker.AtcType,
        shopHomeProductUiModel: ShopHomeProductUiModel
    ) {
        when (atcType) {
            ShopPageAtcTracker.AtcType.ADD, ShopPageAtcTracker.AtcType.UPDATE_ADD -> {
                _createAffiliateCookieAtcProduct.postValue(
                    AffiliateAtcProductModel(
                        shopHomeProductUiModel.id,
                        shopHomeProductUiModel.isVariant,
                        shopHomeProductUiModel.stock
                    )
                )
            }
            else -> {}
        }
    }

    private fun removeItemCart(
        miniCartItem: MiniCartItem.MiniCartItemProduct,
        componentName: String,
        shopHomeProductUiModel: ShopHomeProductUiModel
    ) {
        deleteCartUseCase.setParams(
            cartIdList = listOf(miniCartItem.cartId)
        )
        deleteCartUseCase.execute({
            val productId = miniCartItem.productId
            val data = Pair(productId, it.data.message.joinToString(separator = ", "))
            trackAddToCart(
                miniCartItem.cartId,
                miniCartItem.productId,
                shopHomeProductUiModel.name,
                shopHomeProductUiModel.displayedPrice,
                shopHomeProductUiModel.isVariant,
                miniCartItem.quantity,
                ShopPageAtcTracker.AtcType.REMOVE,
                componentName
            )
            _miniCartRemove.postValue(Success(data))
        }, {
            _miniCartRemove.postValue(Fail(it))
        })
    }

    private fun getMiniCartItem(
        miniCartSimplifiedData: MiniCartSimplifiedData,
        productId: String
    ): MiniCartItem.MiniCartItemProduct? {
        val items = miniCartSimplifiedData.miniCartItems
        return items.getMiniCartItemProduct(productId)
    }

    fun setMiniCartData(miniCartSimplifiedData: MiniCartSimplifiedData?) {
        miniCartData = miniCartSimplifiedData
    }

    private fun updateProductCardQuantity(shopHomeWidgetData: MutableList<Visitable<*>>) {
        shopHomeWidgetData.forEachIndexed { index, widgetModel ->
            when (widgetModel) {
                is ShopHomeCarousellProductUiModel -> {
                    updateShopHomeCarouselProductUiModelProductQuantity(widgetModel)?.let {
                        shopHomeWidgetData.setElement(index, it)
                    }
                }
                is ShopHomeFlashSaleUiModel -> {
                    updateShopHomeFlashSaleWidgetProductQuantity(widgetModel)?.let {
                        shopHomeWidgetData.setElement(index, it)
                    }
                }
                is ShopHomeProductUiModel -> {
                    updateShopHomeProductUiModelProductQuantity(widgetModel).let {
                        shopHomeWidgetData.setElement(index, it)
                    }
                }
            }
        }
    }

    private fun updateShopHomeFlashSaleWidgetProductQuantity(
        widgetModel: ShopHomeFlashSaleUiModel
    ): ShopHomeFlashSaleUiModel? {
        widgetModel.data?.onEach { flashSaleItem ->
            flashSaleItem.productList.onEach { shopHomeProductUiModel ->
                updateShopHomeProductUiModelProductQuantity(shopHomeProductUiModel)
            }.let { listUpdatedShopHomeProductUiModel ->
                widgetModel.isNewData = listUpdatedShopHomeProductUiModel.any { it.isNewData }
            }
        }
        return if (widgetModel.isNewData) {
            widgetModel.copy()
        } else {
            null
        }
    }

    fun getShopWidgetDataWithUpdatedQuantity(shopHomeWidgetData: MutableList<Visitable<*>>) {
        updateProductCardQuantity(shopHomeWidgetData)
        _updatedShopHomeWidgetQuantityData.postValue(shopHomeWidgetData)
    }

    private fun updateShopHomeCarouselProductUiModelProductQuantity(
        widgetModel: ShopHomeCarousellProductUiModel
    ): ShopHomeCarousellProductUiModel? {
        val isProductWidgetWithDirectPurchase = when (widgetModel.type) {
            WidgetType.PRODUCT -> {
                true
            }
            WidgetType.PERSONALIZATION -> {
                when (widgetModel.name) {
                    WidgetName.RECENT_ACTIVITY, WidgetName.REMINDER -> {
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            else -> {
                false
            }
        }
        return if (isProductWidgetWithDirectPurchase) {
            widgetModel.productList.onEach {
                updateShopHomeProductUiModelProductQuantity(it)
            }.let {
                if (it.any { shopHomeProductUiModel -> shopHomeProductUiModel.isNewData }) {
                    widgetModel.isNewData = true
                    widgetModel.copy()
                } else {
                    null
                }
            }
        } else {
            null
        }
    }

    private fun updateShopHomeProductUiModelProductQuantity(
        productModel: ShopHomeProductUiModel
    ): ShopHomeProductUiModel {
        val matchedMiniCartItem = getMatchedMiniCartItem(productModel)
        if (matchedMiniCartItem.isNotEmpty()) {
            val cartQuantity = matchedMiniCartItem.sumOf {
                it.quantity.orZero()
            }
            if (cartQuantity != productModel.productInCart) {
                productModel.productInCart = cartQuantity
                productModel.isNewData = true
            } else {
                productModel.isNewData = false
            }
        } else {
            if (!productModel.productInCart.isZero()) {
                productModel.productInCart = 0
                productModel.isNewData = true
            } else {
                productModel.productInCart = 0
                productModel.isNewData = false
            }
        }
        return productModel
    }

    private fun trackAddToCart(
        cartId: String,
        productId: String,
        productName: String,
        productPrice: String,
        isVariant: Boolean,
        quantity: Int,
        atcType: ShopPageAtcTracker.AtcType,
        componentName: String
    ) {
        val shopPageAtcTracker = ShopPageAtcTracker(
            cartId,
            productId,
            productName,
            productPrice,
            isVariant,
            quantity,
            atcType,
            componentName
        )
        _shopPageAtcTracker.postValue(shopPageAtcTracker)
    }

    fun isWidgetBundle(data: ShopPageWidgetUiModel): Boolean {
        return data.widgetType == WidgetType.BUNDLE
    }

    fun getLatestShopHomeWidgetLayoutData(
        shopId: String,
        extParam: String,
        locData: LocalCacheModel,
        tabName: String
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            val shopHomeWidgetData = getShopDynamicHomeTabWidgetData(
                shopId,
                extParam,
                locData,
                tabName
            )
            _latestShopHomeWidgetLayoutData.postValue(Success(shopHomeWidgetData))
        }) {
            _latestShopHomeWidgetLayoutData.postValue(Fail(it))
        }
    }

    // need to surpress it.name, since name is not related to PII
    @SuppressLint("PII Data Exposure")
    private suspend fun getShopDynamicHomeTabWidgetData(
        shopId: String,
        extParam: String,
        locData: LocalCacheModel,
        tabName: String
    ): ShopPageLayoutUiModel {
        val useCase = getShopDynamicTabUseCase.get()
        useCase.isFromCacheFirst = false
        useCase.setRequestParams(
            GqlShopPageGetDynamicTabUseCase.createParams(
                shopId.toIntOrZero(),
                extParam,
                locData.district_id,
                locData.city_id,
                locData.lat,
                locData.long,
                tabName
            ).parameters
        )
        val layoutData = useCase.executeOnBackground().shopPageGetDynamicTab.tabData.firstOrNull {
            it.name == ShopPageHeaderTabName.HOME
        } ?: ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData()
        return ShopPageHomeMapper.mapToShopHomeWidgetLayoutData(layoutData.data.homeLayoutData)
    }

    fun checkShowConfetti(
        listShopHomeWidgetData: List<BaseShopHomeWidgetUiModel>,
        showConfetti: Boolean
    ) {
        val anyFestivityWidget = listShopHomeWidgetData.any {
            it.isFestivity
        }
        if(anyFestivityWidget && showConfetti) {
            _isShowHomeTabConfettiLiveData.postValue(true)
        } else {
            _isShowHomeTabConfettiLiveData.postValue(false)
        }
    }

    fun getProductComparisonData(
        shopId: String,
        shopHomePersoProductComparisonUiModel: ShopHomePersoProductComparisonUiModel
    ) {
        launchCatchError(block = {
            val productComparisonData = getProductComparisonResponse(shopId)
            val uiModel = shopHomePersoProductComparisonUiModel.copy(
                recommendationWidget = productComparisonData.firstOrNull()
            )
            _productComparisonLiveData.postValue(Success(uiModel))
        }) {
            _productComparisonLiveData.postValue(Fail(it))
        }
    }

    private suspend fun getProductComparisonResponse(shopId: String): List<RecommendationWidget> {
        return getComparisonProductUseCase.get().getData(
            GetRecommendationRequestParam(
                pageName = PAGE_NAME_SHOP_COMPARISON_WIDGET,
                shopIds = listOf(shopId),
            )
        )
    }

    fun getBannerTimerRemindMeStatus(campaignId: String) {
        launchCatchError(block = {
            val getCampaignNotifyMeModel = withContext(dispatcherProvider.io) {
                getCampaignNotifyMe(campaignId)
            }
            val getCampaignNotifyMeUiModel = ShopPageHomeMapper.mapToGetCampaignNotifyMeUiModel(
                getCampaignNotifyMeModel
            )
            _bannerTimerRemindMeStatusData.value = Success(getCampaignNotifyMeUiModel)
        }) {}
    }

    fun toggleBannerTimerRemindMe(
        newList: MutableList<Visitable<*>>,
        isRemindMe: Boolean,
        isClickRemindMe: Boolean
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            newList.filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>()
                .onEach { bannerTimerUiModel ->
                    bannerTimerUiModel.data?.let {
                        it.isRemindMe = isRemindMe
                        if (isClickRemindMe) {
                            if (isRemindMe)
                                ++it.totalNotify
                            else
                                --it.totalNotify
                        }
                        it.showRemindMeLoading = false
                        bannerTimerUiModel.isNewData = true
                    }
                }
            _updatedBannerTimerUiModelData.postValue(newList.filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>().firstOrNull())
            _homeWidgetListVisitable.postValue(Success(newList))
        }) { throwable ->
            _homeWidgetListVisitable.postValue(Fail(throwable))
        }
    }

    fun updateBannerTimerWidgetUiModel(
        newList: MutableList<Visitable<*>>,
        bannerTimerUiModel: ShopWidgetDisplayBannerTimerUiModel
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            val position = newList.indexOfFirst{ it is ShopWidgetDisplayBannerTimerUiModel }
            if(position != -1){
                newList.setElement(position, bannerTimerUiModel.copy().apply {
                    isNewData = true
                })
            }
            _homeWidgetListVisitable.postValue(Success(newList))
        }) { throwable ->
            _homeWidgetListVisitable.postValue(Fail(throwable))
        }
    }
}
