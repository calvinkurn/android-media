package com.tokopedia.shop.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.config.GlobalConfig
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderUiModel
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.domain.GetShopFilterBottomSheetDataUseCase
import com.tokopedia.shop.common.domain.GetShopFilterProductCountUseCase
import com.tokopedia.shop.common.domain.GqlGetShopSortUseCase
import com.tokopedia.shop.common.domain.interactor.GQLCheckWishlistUseCase
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.home.data.model.CheckCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeModel
import com.tokopedia.shop.home.domain.CheckCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutUseCase
import com.tokopedia.shop.home.util.CheckCampaignNplException
import com.tokopedia.shop.home.util.Event
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Provider

class ShopHomeViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val getShopPageHomeLayoutUseCase: GetShopPageHomeLayoutUseCase,
    private val getShopProductUseCase: GqlGetShopProductUseCase,
    private val dispatcherProvider: CoroutineDispatchers,
    private val addToCartUseCase: AddToCartUseCase,
    private val gqlCheckWishlistUseCase: Provider<GQLCheckWishlistUseCase>,
    private val getYoutubeVideoUseCase: GetYoutubeVideoDetailUseCase,
    private val getCampaignNotifyMeUseCase: Provider<GetCampaignNotifyMeUseCase>,
    private val checkCampaignNotifyMeUseCase: Provider<CheckCampaignNotifyMeUseCase>,
    private val getShopFilterBottomSheetDataUseCase: GetShopFilterBottomSheetDataUseCase,
    private val getShopFilterProductCountUseCase: GetShopFilterProductCountUseCase,
    private val gqlGetShopSortUseCase: GqlGetShopSortUseCase,
    private val shopProductSortMapper: ShopProductSortMapper,
    private val mvcSummeryUseCase: MVCSummaryUseCase,
    private val playWidgetTools: PlayWidgetTools
) : BaseViewModel(dispatcherProvider.main) {

    companion object {
        const val ALL_SHOWCASE_ID = "etalase"
        const val CODE_STATUS_SUCCESS = "200"
    }

    val initialProductListData: LiveData<Result<GetShopHomeProductUiModel>>
        get() = _initialProductListData
    private val _initialProductListData = MutableLiveData<Result<GetShopHomeProductUiModel>>()

    val newProductListData: LiveData<Result<GetShopHomeProductUiModel>>
        get() = _newProductListData
    private val _newProductListData = MutableLiveData<Result<GetShopHomeProductUiModel>>()

    val shopHomeLayoutData: LiveData<Result<ShopPageHomeLayoutUiModel>>
        get() = _shopHomeLayoutData
    private val _shopHomeLayoutData = MutableLiveData<Result<ShopPageHomeLayoutUiModel>>()

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

    val checkCampaignNplRemindMeStatusData: LiveData<Result<CheckCampaignNotifyMeUiModel>>
        get() = _checkCampaignNplRemindMeStatusData
    private val _checkCampaignNplRemindMeStatusData = MutableLiveData<Result<CheckCampaignNotifyMeUiModel>>()

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

    val playWidgetToggleReminderObservable: LiveData<PlayWidgetReminderUiModel>
        get() = _playWidgetToggleReminderObservable
    private val _playWidgetToggleReminderObservable = MutableLiveData<PlayWidgetReminderUiModel>()

    val userSessionShopId: String
        get() = userSession.shopId ?: ""
    val isLogin: Boolean
        get() = userSession.isLoggedIn
    val userId: String
        get() = userSession.userId

    fun getShopPageHomeData(
            shopId: String,
            shopProductFilterParameter: ShopProductFilterParameter,
            isRefreshShopLayout: Boolean = false
    ) {
        launchCatchError(block = {
            val shopLayoutWidget = asyncCatchError(
                    dispatcherProvider.io,
                    block = {
                        getShopPageHomeLayout(shopId)
                    },
                    onError = {
                        _shopHomeLayoutData.postValue(Fail(it))
                        null
                    }
            )
            val productList = asyncCatchError(
                    dispatcherProvider.io,
                    block = {
                        if (!isRefreshShopLayout)
                            getProductListData(shopId, 1, shopProductFilterParameter)
                        else null
                    },
                    onError = { null }
            )

            val sortResponse  = asyncCatchError(
                    dispatcherProvider.io,
                    block = {
                        getSortListData()
                    },
                    onError = {
                        null
                    }
            )

            shopLayoutWidget.await()?.let {

                _shopHomeLayoutData.postValue(Success(it))

                productList.await()?.let { productListData ->
                    _initialProductListData.postValue(Success(productListData))
                }
                sortResponse.await()?.let{  sortResponse ->
                    sortListData  = sortResponse
                }

                getPlayWidget(shopId, Success(it))
            }
        }) {
            it.printStackTrace()
        }
    }

    fun getNewProductList(
            shopId: String,
            page: Int,
            shopProductFilterParameter: ShopProductFilterParameter
    ) {
        launchCatchError(block = {
            val listProductData = withContext(dispatcherProvider.io) {
                getProductListData(shopId, page, shopProductFilterParameter)
            }
            _newProductListData.postValue(Success(listProductData))
        }) {
            _newProductListData.postValue(Fail(it))
        }
    }

    fun getMerchantVoucherCoupon(shopId: String) {
        val result = shopHomeLayoutData.value
        if (result is Success) {
            launchCatchError(dispatcherProvider.io, block = {
                var uiModel = result.data.listWidget.filterIsInstance<ShopHomeVoucherUiModel>().firstOrNull()
                val response =  mvcSummeryUseCase.getResponse(mvcSummeryUseCase.getQueryParams("480136"))
                uiModel = uiModel?.copy(
                        data = ShopPageHomeMapper.mapToVoucherCouponUiModel(response.data, shopId),
                        isError = false
                )
                if (response.data?.resultStatus?.code == CODE_STATUS_SUCCESS) {
                    _shopHomeMerchantVoucherLayoutData.postValue(Success(uiModel as ShopHomeVoucherUiModel))
                } else {
                    _shopHomeMerchantVoucherLayoutData.postValue(Fail(MessageErrorException(response.data?.resultStatus?.message.toString())))
                }
            }) {
                _shopHomeMerchantVoucherLayoutData.postValue(Fail(it))
            }
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
            if (addToCartSubmitData.data.success == 1)
                onSuccessAddToCart(addToCartSubmitData.data)
            else
                onErrorAddToCart(MessageErrorException(addToCartSubmitData.data.message.first()))
        }) {
            onErrorAddToCart(it)
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
            _checkWishlistData.value = Success(listResultCheckWishlist)
        }) {}
    }

    private suspend fun getShopPageHomeLayout(shopId: String): ShopPageHomeLayoutUiModel {
        getShopPageHomeLayoutUseCase.params = GetShopPageHomeLayoutUseCase.createParams(shopId)
        return ShopPageHomeMapper.mapToShopPageHomeLayoutUiModel(
                getShopPageHomeLayoutUseCase.executeOnBackground(),
                ShopUtil.isMyShop(shopId, userSessionShopId),
                isLogin
        )
    }

    private suspend fun getProductListData(
            shopId: String,
            page: Int,
            shopProductFilterParameter: ShopProductFilterParameter
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
                }
        )
        val productListResponse = getShopProductUseCase.executeOnBackground()
        val isHasNextPage = ShopUtil.isHasNextPage(page, ShopPageConstant.DEFAULT_PER_PAGE, productListResponse.totalData)
        val productListUiModelData = productListResponse.data.map {
            ShopPageHomeMapper.mapToHomeProductViewModelForAllProduct(
                    it,
                    ShopUtil.isMyShop(shopId, userSessionShopId)
            )
        }
        val totalProductListData = productListResponse.totalData
        return GetShopHomeProductUiModel(
                isHasNextPage,
                productListUiModelData,
                totalProductListData
        )
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
            _videoYoutube.value = Pair(widgetId, Success(result))
        }, onError = {
            _videoYoutube.value = Pair(widgetId, Fail(it))
        })
    }

    private fun convertToYoutubeResponse(typeRestResponseMap: Map<Type, RestResponse>): YoutubeVideoDetailModel {
        return typeRestResponseMap[YoutubeVideoDetailModel::class.java]?.getData() as YoutubeVideoDetailModel
    }

    private fun submitAddProductToCart(shopId: String, product: ShopHomeProductUiModel): AddToCartDataModel {
        val requestParams = AddToCartUseCase.getMinimumParams(product.id
                ?: "", shopId, productName = product.name ?: "", price = product.displayedPrice
                ?: "", userId = userId)
        return addToCartUseCase.createObservable(requestParams).toBlocking().first()
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
        getShopPageHomeLayoutUseCase.clearCache()
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
            _checkCampaignNplRemindMeStatusData.value = Success(checkCampaignNotifyMeUiModel)
        }) {
            _checkCampaignNplRemindMeStatusData.value = Fail(CheckCampaignNplException(
                    it.cause,
                    it.message,
                    campaignId
            ))
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


    fun getBottomSheetFilterData() {
        launchCatchError(coroutineContext, block = {
            val filterBottomSheetData = withContext(dispatcherProvider.io) {
                getShopFilterBottomSheetDataUseCase.params = GetShopFilterBottomSheetDataUseCase.createParams()
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

    fun getFilterResultCount(shopId: String, tempShopProductFilterParameter: ShopProductFilterParameter) {
        launchCatchError(block = {
            val filterResultProductCount = withContext(dispatcherProvider.io) {
                getFilterResultCountData(shopId, tempShopProductFilterParameter)
            }
            _shopProductFilterCountLiveData.postValue(Success(filterResultProductCount))
        }) {}
    }

    private suspend fun getFilterResultCountData(
            shopId: String,
            tempShopProductFilterParameter: ShopProductFilterParameter
    ): Int {
        val filter = ShopProductFilterInput(
                ShopPageConstant.START_PAGE,
                ShopPageConstant.DEFAULT_PER_PAGE,
                "",
                ALL_SHOWCASE_ID,
                tempShopProductFilterParameter.getSortId().toIntOrZero(),
                tempShopProductFilterParameter.getRating(),
                tempShopProductFilterParameter.getPmax(),
                tempShopProductFilterParameter.getPmin()
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
    fun getPlayWidget(shopId: String, shopResultLayoutUiModel: Result<ShopPageHomeLayoutUiModel>? = _shopHomeLayoutData.value) {
        if (shopResultLayoutUiModel !is Success) return
        val shopLayoutUiModel = shopResultLayoutUiModel.data
        val carouselPlayWidgetUiModel = shopLayoutUiModel.listWidget.find { it is CarouselPlayWidgetUiModel }
        if (carouselPlayWidgetUiModel !is CarouselPlayWidgetUiModel) return
        launchCatchError(block = {
            val response = playWidgetTools.getWidgetFromNetwork(
                    if (GlobalConfig.isSellerApp()) PlayWidgetUseCase.WidgetType.SellerApp(shopId) else PlayWidgetUseCase.WidgetType.ShopPage(shopId),
                    dispatcherProvider.io
            )
            val widgetUiModel = playWidgetTools.mapWidgetToModel(widgetResponse = response, prevModel = _playWidgetObservable.value?.widgetUiModel)
            _playWidgetObservable.value = carouselPlayWidgetUiModel.copy(
                    actionEvent = Event(CarouselPlayWidgetUiModel.Action.Refresh),
                    widgetUiModel = widgetUiModel
            )
        }) {
            _playWidgetObservable.value = null
        }
    }

    fun setToggleReminderPlayWidget(channelId: String, remind: Boolean, position: Int) {
        launchCatchError(block = {
            val response = playWidgetTools.setToggleReminder(
                    channelId,
                    remind,
                    dispatcherProvider.io
            )
            val reminderUiModel = playWidgetTools.mapWidgetToggleReminder(response)
            _playWidgetToggleReminderObservable.value = reminderUiModel.copy(remind = remind, position = position)
        }) {
            _playWidgetToggleReminderObservable.value = PlayWidgetReminderUiModel(
                    remind = remind,
                    success = false,
                    position = position
            )
        }
    }

    fun deleteChannel(channelId: String) {

        fun updateWidget(onUpdate: (oldVal: CarouselPlayWidgetUiModel) -> CarouselPlayWidgetUiModel) {
            val currentValue = _playWidgetObservable.value
            if (currentValue != null) _playWidgetObservable.value = onUpdate(currentValue)
        }

        updateWidget {
            it.copy(widgetUiModel = playWidgetTools.updateDeletingChannel(it.widgetUiModel, channelId))
        }

        launchCatchError(block = {
            playWidgetTools.deleteChannel(
                    channelId,
                    userSessionShopId
            )

            updateWidget {
                it.copy(
                        widgetUiModel = playWidgetTools.updateDeletedChannel(it.widgetUiModel, channelId),
                        actionEvent = Event(CarouselPlayWidgetUiModel.Action.Delete(channelId))
                )
            }
        }, onError = { err ->
            updateWidget {
                it.copy(
                        widgetUiModel = playWidgetTools.updateFailedDeletingChannel(it.widgetUiModel, channelId),
                        actionEvent = Event(CarouselPlayWidgetUiModel.Action.DeleteFailed(channelId, err))
                )
            }
        })
    }

    fun isCampaignFollower(campaignId: String): Boolean {
        val homeLayoutData = shopHomeLayoutData.value
        if (homeLayoutData !is Success) return false
        return homeLayoutData.data.listWidget.filterIsInstance<ShopHomeNewProductLaunchCampaignUiModel>().any {
            val campaignItem = it.data?.firstOrNull()
            val nplItemCampaignId = campaignItem?.campaignId.orEmpty()
            val dynamicRule = campaignItem?.dynamicRule
            val dynamicRuleDescription = dynamicRule?.descriptionHeader.orEmpty()
            nplItemCampaignId == campaignId && dynamicRuleDescription.isNotEmpty()
        }
    }
}