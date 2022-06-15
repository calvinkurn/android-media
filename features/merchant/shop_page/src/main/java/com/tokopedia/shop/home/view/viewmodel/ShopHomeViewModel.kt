package com.tokopedia.shop.home.view.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
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
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.domain.GetShopFilterBottomSheetDataUseCase
import com.tokopedia.shop.common.domain.GetShopFilterProductCountUseCase
import com.tokopedia.shop.common.domain.GqlGetShopSortUseCase
import com.tokopedia.shop.common.domain.interactor.GQLCheckWishlistUseCase
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.util.ShopAsyncErrorException
import com.tokopedia.shop.common.util.ShopPageExceptionHandler
import com.tokopedia.shop.common.util.ShopPageExceptionHandler.logExceptionToCrashlytics
import com.tokopedia.shop.common.util.ShopPageMapper
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.home.data.model.CheckCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.ShopLayoutWidgetParamsModel
import com.tokopedia.shop.home.data.model.ShopPageWidgetRequestModel
import com.tokopedia.shop.home.domain.CheckCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutV2UseCase
import com.tokopedia.shop.home.util.CheckCampaignNplException
import com.tokopedia.shop.home.util.Event
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.common.domain.interactor.GqlShopPageGetHomeType
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
    private val getShopPageHomeLayoutV2UseCase: Provider<GetShopPageHomeLayoutV2UseCase>
    ) : BaseViewModel(dispatcherProvider.main) {

    companion object {
        const val ALL_SHOWCASE_ID = "etalase"
        const val CODE_STATUS_SUCCESS = "200"
    }

    val productListData: LiveData<Result<GetShopHomeProductUiModel>>
        get() = _productListData
    private val _productListData = MutableLiveData<Result<GetShopHomeProductUiModel>>()

    val shopHomeWidgetLayoutData: LiveData<Result<ShopPageHomeWidgetLayoutUiModel>>
        get() = _shopHomeWidgetLayoutData
    private val _shopHomeWidgetLayoutData = MutableLiveData<Result<ShopPageHomeWidgetLayoutUiModel>>()

    val shopHomeWidgetContentData : Flow<Result<Map<Pair<String,String>, Visitable<*>?>>>
        get() = _shopHomeWidgetContentData
    private val _shopHomeWidgetContentData = MutableSharedFlow<Result<Map<Pair<String,String>, Visitable<*>?>>>()

    val shopHomeWidgetContentDataError : Flow<List<ShopPageHomeWidgetLayoutUiModel.WidgetLayout>>
        get() = _shopHomeWidgetContentDataError
    private val _shopHomeWidgetContentDataError = MutableSharedFlow<List<ShopPageHomeWidgetLayoutUiModel.WidgetLayout>>()

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

    val bottomSheetFilterLiveData : LiveData<Result<DynamicFilterModel>>
        get() = _bottomSheetFilterLiveData
    private val _bottomSheetFilterLiveData = MutableLiveData<Result<DynamicFilterModel>>()

    val shopProductFilterCountLiveData : LiveData<Result<Int>>
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

    val userSessionShopId: String
        get() = userSession.shopId ?: ""
    val isLogin: Boolean
        get() = userSession.isLoggedIn
    val userId: String
        get() = userSession.userId

    private var miniCartData : MiniCartSimplifiedData? = null

    fun getShopPageHomeWidgetLayoutData(
            shopId: String,
            extParam: String,
    ) {
        launchCatchError(block = {
            val shopHomeLayoutResponse = withContext(dispatcherProvider.io) {
                gqlShopPageGetHomeType.isFromCacheFirst = false
                gqlShopPageGetHomeType.params = GqlShopPageGetHomeType.createParams(
                        shopId,
                        extParam
                )
                gqlShopPageGetHomeType.executeOnBackground()
            }
            val shopHomeLayoutUiModelPlaceHolder = ShopPageHomeMapper.mapToShopHomeWidgetLayoutData(
                    shopHomeLayoutResponse.homeLayoutData
            )
            _shopHomeWidgetLayoutData.postValue(Success(shopHomeLayoutUiModelPlaceHolder))
        }) {
            _shopHomeWidgetLayoutData.postValue(Fail(it))
        }
    }

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

    fun getMerchantVoucherCoupon(shopId: String, context: Context?, shopHomeVoucherUiModel: ShopHomeVoucherUiModel) {
        launchCatchError(dispatcherProvider.io, block = {
            var uiModel = shopHomeVoucherUiModel
            val response = mvcSummaryUseCase.getResponse(mvcSummaryUseCase.getQueryParams(shopId))
            uiModel = uiModel.copy(
                    data = ShopPageMapper.mapToVoucherCouponUiModel(response.data, shopId),
                    isError = false
            )
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
            if (addToCartSubmitData.data.success == ShopPageConstant.ATC_SUCCESS_VALUE)
                onSuccessAddToCart(addToCartSubmitData.data)
            else
                onErrorAddToCart(MessageErrorException(addToCartSubmitData.data.message.first()))
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
            if (addToCartOccSubmitData.data.success == ShopPageConstant.ATC_SUCCESS_VALUE)
                onSuccessAddToCartOcc(addToCartOccSubmitData.data)
            else
                onErrorAddToCartOcc(MessageErrorException(addToCartOccSubmitData.data.message.first()))
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
    ) : GetShopHomeProductUiModel {
        val isHasNextPage = ShopUtil.isHasNextPage(page, productPerPage, productListResponse.totalData)
        val productListUiModelData = productListResponse.data.map {
            val productInCart = getProductInMiniCart(it.productId, miniCartData)
            ShopPageHomeMapper.mapToHomeProductViewModelForAllProduct(
                    it,
                    ShopUtil.isMyShop(shopId, userSessionShopId),
                    isEnableDirectPurchase,
                    productInCart
            )
        }
        val totalProductListData = productListResponse.totalData
        return GetShopHomeProductUiModel(
                isHasNextPage,
                productListUiModelData,
                totalProductListData
        )
    }

    private fun getProductInMiniCart(
        productId: String,
        miniCartSimplifiedData: MiniCartSimplifiedData?
    ): Int {
        val matchedMiniCartItem = getMatchedMiniCartItem(productId, miniCartSimplifiedData)
        return if (matchedMiniCartItem != null && !matchedMiniCartItem.isError) {
            matchedMiniCartItem.quantity
        } else {
            0
        }
    }

    private fun getMatchedMiniCartItem(
        productId: String,
        miniCartData: MiniCartSimplifiedData?
    ): MiniCartItem.MiniCartItemProduct? {
//        val isVariant = productUiModel.isVariant
//        if (isVariant) {
//            return miniCartData.miniCartItems.values.firstOrNull {
//                it is MiniCartItem.MiniCartItemProduct && (it.productId == productUiModel.productID || productUiModel.childIDs.contains(it.productId))
//            } as? MiniCartItem.MiniCartItemProduct
//        }
        return miniCartData?.miniCartItems?.getMiniCartItemProduct(productId)
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
        val requestParams = AddToCartUseCase.getMinimumParams(product.id
                ?: "", shopId, productName = product.name ?: "", price = product.displayedPrice
                ?: "", userId = userId)
        return addToCartUseCaseRx.createObservable(requestParams).toBlocking().first()
    }

    private suspend fun submitAddProductToCartOcc(shopId: String, product: ShopHomeProductUiModel): AddToCartDataModel {
        return addToCartOccUseCase.setParams(AddToCartOccMultiRequestParams(
                carts = listOf(
                        AddToCartOccMultiCartParam(
                                productId = product.id ?: "",
                                shopId = shopId,
                                quantity = product.minimumOrder.toString(),
                                productName = product.name ?: "",
                                price = product.displayedPrice ?: ""
                        )
                ),
                userId = userId
        )).executeOnBackground().mapToAddToCartDataModel()
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
                add(it.id ?: "")
            }
        }.joinToString(separator = ",")
        useCase.params = GQLCheckWishlistUseCase.createParams(listProductIdString)
        return useCase.executeOnBackground()
    }

    fun clearCache() {
        clearGetShopProductUseCase()
        gqlShopPageGetHomeType.clearCache()
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
            _checkCampaignNplRemindMeStatusData.postValue(Fail(CheckCampaignNplException(
                    it.cause,
                    it.message,
                    campaignId
            )))
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
            _checkCampaignFlashSaleRemindMeStatusData.postValue(Fail(CheckCampaignNplException(
                it.cause,
                it.message,
                campaignId
            )))
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
                widgetUserAddressLocalData.long
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
    fun getPlayWidget(shopId: String, carouselPlayWidgetUiModel: CarouselPlayWidgetUiModel) {
        launchCatchError(block = {
            val response = playWidgetTools.getWidgetFromNetwork(
                    if (shopId == userSessionShopId) PlayWidgetUseCase.WidgetType.SellerApp(shopId) else PlayWidgetUseCase.WidgetType.ShopPage(shopId),
                    dispatcherProvider.io
            )
            val widgetUiModel = playWidgetTools.mapWidgetToModel(widgetResponse = response, prevState = _playWidgetObservable.value?.playWidgetState)
            _playWidgetObservable.postValue(carouselPlayWidgetUiModel.copy(
                    actionEvent = Event(CarouselPlayWidgetUiModel.Action.Refresh),
                    playWidgetState = widgetUiModel
            ))
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
            val reminderType = if(isReminder) PlayWidgetReminderType.Reminded else PlayWidgetReminderType.NotReminded
            it.copy(playWidgetState = playWidgetTools.updateActionReminder(it.playWidgetState, channelId, reminderType))
        }
    }

    fun shouldUpdatePlayWidgetToggleReminder(channelId: String, reminderType: PlayWidgetReminderType) {
        if (!isLogin) _playWidgetReminderEvent.value = Pair(channelId, reminderType)
        else updatePlayWidgetToggleReminder(channelId, reminderType)
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
            listWidgetLayout: List<ShopPageHomeWidgetLayoutUiModel.WidgetLayout>,
            shopId: String,
            widgetUserAddressLocalData: LocalCacheModel,
            isThematicWidgetShown: Boolean,
            isEnableDirectPurchase: Boolean
    ) {
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
                                listWidgetRequest = listWidgetLayout.map {
                                    ShopPageWidgetRequestModel(
                                            it.widgetId,
                                            it.widgetMasterId,
                                            it.widgetType,
                                            it.widgetName
                                    )
                                }
                        )
                )
                useCase.executeOnBackground()
            }
            val listShopHomeWidget = ShopPageHomeMapper.mapToListShopHomeWidget(
                    responseWidgetContent.listWidget,
                    ShopUtil.isMyShop(shopId, userSessionShopId),
                    isLogin,
                    isThematicWidgetShown,
                    isEnableDirectPurchase
            )
            val mapShopHomeWidgetData = mutableMapOf<Pair<String, String>, Visitable<*>?>().apply{
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
                        put(Pair(it.widgetId, it.widgetMasterId) , null)
                    }
                }
            }
            _shopHomeWidgetContentData.emit(Success(mapShopHomeWidgetData))
        }) {
            _shopHomeWidgetContentDataError.emit(listWidgetLayout)
            _shopHomeWidgetContentData.emit(Fail(ShopAsyncErrorException(
                ShopAsyncErrorException.AsyncQueryType.SHOP_PAGE_GET_LAYOUT_V2,
                it
            )))
        }
    }

    fun getProductGridListWidgetData(
            shopId: String,
            productPerPage: Int,
            shopProductFilterParameter: ShopProductFilterParameter,
            initialProductListData: ShopProduct.GetShopProduct?,
            widgetUserAddressLocalData: LocalCacheModel,
            isEnableDirectPurchase: Boolean
    ){
        launchCatchError(block = {
            val productList = asyncCatchError(
                    dispatcherProvider.io,
                    block = {
                        if (initialProductListData == null)
                            getProductListData(
                                shopId,
                                ShopPageConstant.START_PAGE,
                                productPerPage,
                                shopProductFilterParameter,
                                widgetUserAddressLocalData,
                                isEnableDirectPurchase
                            )
                        else
                            null
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
                    _productListData.postValue(Success(mapToShopHomeProductUiModel(
                            shopId,
                            productPerPage,
                            ShopPageConstant.START_PAGE,
                            initialProductListData,
                            isEnableDirectPurchase
                    )))
                }
                it.let { sortResponse ->
                    sortListData = sortResponse
                }
            }
        }) {

        }
    }

    fun handleAtcFlow(
        productId: String,
        quantity: Int,
        shopId: String,
        miniCartSimplifiedData: MiniCartSimplifiedData?
    ) {
        val miniCartItem = getMiniCartItem(miniCartSimplifiedData, productId)
        when {
            miniCartItem == null -> addItemToCart(productId, shopId, quantity)
//            quantity.isZero() -> removeItemCart(miniCartItem)
//            else -> updateItemCart(miniCartItem, quantity)
        }
    }

    private fun addItemToCart(
        productId: String,
        shopId: String,
        quantity: Int
    ) {
        val addToCartRequestParams = com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase.getMinimumParams(
            productId = productId,
            shopId = shopId,
            quantity = quantity
        )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
//            trackAddToCart(
//                it.data.cartId,
//                it.data.productId.toString(),
//                it.data.quantity,
//                MvcLockedToProductAddToCartTracker.AtcType.ADD
//            )
            _miniCartAdd.postValue(Success(it))
        }, {
            _miniCartAdd.postValue(Fail(it))
        })
    }

    private fun getMiniCartItem(
        miniCartSimplifiedData: MiniCartSimplifiedData?,
        productId: String
    ): MiniCartItem.MiniCartItemProduct? {
        val items = miniCartSimplifiedData?.miniCartItems.orEmpty()
        return items.getMiniCartItemProduct(productId)
    }

    fun setMiniCartData(miniCartSimplifiedData: MiniCartSimplifiedData?) {
        miniCartData = miniCartSimplifiedData
    }

}