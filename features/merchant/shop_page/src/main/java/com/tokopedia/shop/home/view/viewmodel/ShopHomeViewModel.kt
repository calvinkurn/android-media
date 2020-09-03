package com.tokopedia.shop.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play_common.domain.model.PlayToggleChannelReminder
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase.Companion.SHOP_AUTHOR_TYPE
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase.Companion.SHOP_WIDGET_TYPE
import com.tokopedia.play_common.domain.usecases.PlayToggleChannelReminderUseCase
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselItemDataModel
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.domain.interactor.GQLCheckWishlistUseCase
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.home.data.model.CheckCampaignNotifyMeModel
import com.tokopedia.shop.home.data.model.GetCampaignNotifyMeModel
import com.tokopedia.shop.home.domain.CheckCampaignNotifyMeUseCase
import com.tokopedia.shop.home.domain.GetCampaignNotifyMeUseCase
import com.tokopedia.shop.home.util.CoroutineDispatcherProvider
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomePlayCarouselUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.home.view.model.ShopPageHomeLayoutUiModel
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutUseCase
import com.tokopedia.shop.home.util.CheckCampaignNplException
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.youtube_common.data.model.YoutubeVideoDetailModel
import com.tokopedia.youtube_common.domain.usecase.GetYoutubeVideoDetailUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Provider

class ShopHomeViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getShopPageHomeLayoutUseCase: GetShopPageHomeLayoutUseCase,
        private val getShopProductUseCase: GqlGetShopProductUseCase,
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val addToCartUseCase: AddToCartUseCase,
        private val getPlayWidgetUseCase: GetPlayWidgetUseCase,
        private val playToggleChannelReminderUseCase: PlayToggleChannelReminderUseCase,
        private val gqlCheckWishlistUseCase: Provider<GQLCheckWishlistUseCase>,
        private val getYoutubeVideoUseCase: GetYoutubeVideoDetailUseCase,
        private val getCampaignNotifyMeUseCase: Provider<GetCampaignNotifyMeUseCase>,
        private val checkCampaignNotifyMeUseCase: Provider<CheckCampaignNotifyMeUseCase>
) : BaseViewModel(dispatcherProvider.main()) {

    companion object {
        const val ALL_SHOWCASE_ID = "etalase"
    }

    val initialProductListData: LiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>
        get() = _initialProductListData
    private val _initialProductListData = MutableLiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>()

    val newProductListData: LiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>
        get() = _newProductListData
    private val _newProductListData = MutableLiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>()

    val shopHomeLayoutData: LiveData<Result<ShopPageHomeLayoutUiModel>>
        get() = _shopHomeLayoutData
    private val _shopHomeLayoutData = MutableLiveData<Result<ShopPageHomeLayoutUiModel>>()

    val checkWishlistData: LiveData<Result<List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>?>>>
        get() = _checkWishlistData
    private val _checkWishlistData = MutableLiveData<Result<List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>?>>>()

    val reminderPlayLiveData: LiveData<Pair<Int, Result<Boolean>>> get() = _reminderPlayLiveData
    private val _reminderPlayLiveData = MutableLiveData<Pair<Int, Result<Boolean>>>()

    val updatePlayWidgetData: LiveData<ShopHomePlayCarouselUiModel> get() = _updatePlayWidgetData
    private val _updatePlayWidgetData = MutableLiveData<ShopHomePlayCarouselUiModel>()

    val videoYoutube: LiveData<Pair<String, Result<YoutubeVideoDetailModel>>>
        get() = _videoYoutube
    private val _videoYoutube = MutableLiveData<Pair<String, Result<YoutubeVideoDetailModel>>>()

    val campaignNplRemindMeStatusData: LiveData<Result<GetCampaignNotifyMeUiModel>>
        get() = _campaignNplRemindMeStatusData
    private val _campaignNplRemindMeStatusData = MutableLiveData<Result<GetCampaignNotifyMeUiModel>>()

    val checkCampaignNplRemindMeStatusData: LiveData<Result<CheckCampaignNotifyMeUiModel>>
        get() = _checkCampaignNplRemindMeStatusData
    private val _checkCampaignNplRemindMeStatusData = MutableLiveData<Result<CheckCampaignNotifyMeUiModel>>()

    val userSessionShopId: String
        get() = userSession.shopId ?: ""
    val isLogin: Boolean
        get() = userSession.isLoggedIn
    val userId: String
        get() = userSession.userId

    fun getShopPageHomeData(
            shopId: String,
            sortId: Int,
            isRefreshShopLayout: Boolean = false
    ) {
        launchCatchError(block = {
            val shopLayoutWidget = asyncCatchError(
                    dispatcherProvider.io(),
                    block = {
                        getShopPageHomeLayout(shopId)
                    },
                    onError = {
                        _shopHomeLayoutData.postValue(Fail(it))
                        null
                    }
            )
            val productList = asyncCatchError(
                    dispatcherProvider.io(),
                    block = {
                        if (!isRefreshShopLayout)
                            getProductListData(shopId, sortId, 1)
                        else null
                    },
                    onError = { null }
            )

            shopLayoutWidget.await()?.let {

                val newShopPageHomeLayoutUiModel = asyncCatchError(
                        dispatcherProvider.io(),
                        block = { getPlayWidgetCarousel(shopId, it) },
                        onError = {null}
                )

                newShopPageHomeLayoutUiModel.await()?.let { newShopPageHomeLayoutUiModelData ->
                    _shopHomeLayoutData.postValue(Success(newShopPageHomeLayoutUiModelData))
                }
                productList.await()?.let { productListData ->
                    _initialProductListData.postValue(Success(productListData))
                }
            }
        }) {
            it.printStackTrace()
        }
    }

    fun getNewProductList(
            shopId: String,
            sortId: Int,
            page: Int
    ) {
        launchCatchError(block = {
            val listProductData = withContext(dispatcherProvider.io()) {
                getProductListData(shopId, sortId, page)
            }
            _newProductListData.postValue(Success(listProductData))
        }) {
            _newProductListData.postValue(Fail(it))
        }
    }

    fun onRefreshPlayBanner(shopId: String){
        val result = _shopHomeLayoutData.value
        if(result is Success){
            launchCatchError(dispatcherProvider.io(), block = {
                result.data.listWidget.find { data -> data is ShopHomePlayCarouselUiModel }?.let { uiModel ->
                    getPlayWidgetUseCase.setParams(SHOP_WIDGET_TYPE, shopId, SHOP_AUTHOR_TYPE)
                    val playWidgetEntity = getPlayWidgetUseCase.executeOnBackground()
                    _updatePlayWidgetData.postValue((uiModel as ShopHomePlayCarouselUiModel).copy(playBannerCarouselDataModel = playWidgetEntity))
                }
            }){
                it.printStackTrace()
            }
        }
    }

    fun setToggleReminderPlayBanner(channelId: String, isSet: Boolean, position: Int){
        launchCatchError(dispatcherProvider.io(), block = {
            playToggleChannelReminderUseCase.setParams(channelId, isSet)
            val reminder = playToggleChannelReminderUseCase.executeOnBackground()
            if(reminder.playToggleChannelReminder != null && reminder.playToggleChannelReminder?.header?.status == PlayToggleChannelReminder.SUCCESS_STATUS){
                _reminderPlayLiveData.postValue(Pair(position, Success(isSet)))
            } else {
                _reminderPlayLiveData.postValue(Pair(position, Fail(Throwable())))
            }
        }){
            _reminderPlayLiveData.postValue(Pair(position, Fail(it)))
        }
    }

    private suspend fun getPlayWidgetCarousel(shopId: String, shopPageHomeLayoutUiModel: ShopPageHomeLayoutUiModel): ShopPageHomeLayoutUiModel?{
        val index = shopPageHomeLayoutUiModel.listWidget.indexOfFirst { it is ShopHomePlayCarouselUiModel }
        if(index != -1){
            val data = shopPageHomeLayoutUiModel.listWidget[index] as ShopHomePlayCarouselUiModel
            getPlayWidgetUseCase.setParams(SHOP_WIDGET_TYPE, shopId, SHOP_AUTHOR_TYPE)
            val playBannerDataModel = getPlayWidgetUseCase.executeOnBackground()
            val newHomeLayout = shopPageHomeLayoutUiModel.listWidget.toMutableList()
            if(playBannerDataModel.channelList.isNotEmpty()){
                newHomeLayout[index] = data.copy(playBannerCarouselDataModel = playBannerDataModel)
            }else{
                newHomeLayout.remove(data)
            }
            return shopPageHomeLayoutUiModel.copy(
                    listWidget = newHomeLayout
            )
        }
        return shopPageHomeLayoutUiModel
    }

    fun addProductToCart(
            product: ShopHomeProductViewModel,
            shopId: String,
            onSuccessAddToCart: (dataModelAtc: DataModel) -> Unit,
            onErrorAddToCart: (exception: Throwable) -> Unit
    ) {
        launchCatchError(block = {
            val addToCartSubmitData = withContext(dispatcherProvider.io()) {
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
                        dispatcherProvider.io(),
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

    fun updatePlayWidgetData(channelId: String, totalView: String){
        val result = _shopHomeLayoutData.value
        if(result is Success){
            launchCatchError(block = {
                result.data.listWidget.withIndex().find { (_, data) -> data is ShopHomePlayCarouselUiModel }?.let { (index, uiModel) ->
                    val newList = (uiModel as ShopHomePlayCarouselUiModel).playBannerCarouselDataModel.channelList.toMutableList()
                    val indexOf = newList.indexOfFirst { it is PlayBannerCarouselItemDataModel && it.channelId == channelId }
                    if(indexOf != -1){
                        newList[index] = (newList[index] as PlayBannerCarouselItemDataModel).copy(countView = totalView)
                        _updatePlayWidgetData.postValue(uiModel.copy(playBannerCarouselDataModel = uiModel.playBannerCarouselDataModel.copy(channelList = newList)))
                    }
                }
            }){
            }
        }
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
            sortId: Int,
            page: Int
    ): Pair<Boolean, List<ShopHomeProductViewModel>> {
        getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(
                shopId,
                ShopProductFilterInput().apply {
                    etalaseMenu = ALL_SHOWCASE_ID
                    this.page = page
                    sort = sortId
                }
        )
        val productListResponse = getShopProductUseCase.executeOnBackground()
        val isHasNextPage = ShopUtil.isHasNextPage(page, ShopPageConstant.DEFAULT_PER_PAGE, productListResponse.totalData)
        return Pair(
                isHasNextPage,
                productListResponse.data.map {
                    ShopPageHomeMapper.mapToHomeProductViewModelForAllProduct(
                            it,
                            ShopUtil.isMyShop(shopId, userSessionShopId)
                    )
                }
        )
    }

    fun getVideoYoutube(videoUrl: String, widgetId: String) {
        launchCatchError(block = {
            getYoutubeVideoUseCase.setVideoUrl(videoUrl)
            val result = withContext(Dispatchers.IO) {
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

    private fun submitAddProductToCart(shopId: String, product: ShopHomeProductViewModel): AddToCartDataModel {
        val requestParams = AddToCartUseCase.getMinimumParams(product.id
                ?: "", shopId, productName = product.name ?: "", price = product.displayedPrice
                ?: "")
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
            val getCampaignNotifyMeModel = withContext(dispatcherProvider.io()) {
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
            val checkCampaignNotifyMeModel = withContext(dispatcherProvider.io()) {
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
}