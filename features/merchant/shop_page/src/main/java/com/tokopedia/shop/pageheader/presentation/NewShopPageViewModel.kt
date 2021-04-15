package com.tokopedia.shop.pageheader.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPageConstant.DISABLE_SHOP_PAGE_CACHE_INITIAL_PRODUCT_LIST
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTracker
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTrackerInput
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShopResponse
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatusResponse
import com.tokopedia.shop.common.di.GqlGetShopInfoForHeaderUseCaseQualifier
import com.tokopedia.shop.common.di.GqlGetShopInfoUseCaseCoreAndAssetsQualifier
import com.tokopedia.shop.common.domain.interactor.*
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_ALLOW_MANAGE
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_ASSETS
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_CLOSED_INFO
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_CORE
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_CREATE_INFO
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_IS_OPEN
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_IS_OWNER
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_LAST_ACTIVE
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_LOCATION
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_SHOP_SNIPPET
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_STATUS
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.SHOP_PAGE_SOURCE
import com.tokopedia.shop.common.domain.interactor.GetFollowStatusUseCase.Companion.SOURCE_SHOP_PAGE
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderP1
import com.tokopedia.shop.pageheader.data.model.ShopRequestUnmoderateSuccessResponse
import com.tokopedia.shop.pageheader.domain.interactor.*
import com.tokopedia.shop.pageheader.presentation.uimodel.NewShopPageP1HeaderData
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageP1HeaderData
import com.tokopedia.shop.pageheader.util.NewShopPageHeaderMapper
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageProcessingUtil
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import rx.Subscriber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NewShopPageViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        @GqlGetShopInfoForHeaderUseCaseQualifier
        private val gqlGetShopInfoForHeaderUseCase: Lazy<GQLGetShopInfoUseCase>,
        private val getBroadcasterShopConfigUseCase: Lazy<GetBroadcasterShopConfigUseCase>,
        @GqlGetShopInfoUseCaseCoreAndAssetsQualifier
        private val gqlGetShopInfobUseCaseCoreAndAssets: Lazy<GQLGetShopInfoUseCase>,
        private val shopQuestGeneralTrackerUseCase: Lazy<ShopQuestGeneralTrackerUseCase>,
        private val getShopPageP1DataUseCase: Lazy<GetShopPageP1DataUseCase>,
        private val getShopProductListUseCase: Lazy<GqlGetShopProductUseCase>,
        private val shopModerateRequestStatusUseCase: Lazy<ShopModerateRequestStatusUseCase>,
        private val shopRequestUnmoderateUseCase: Lazy<ShopRequestUnmoderateUseCase>,
        private val getShopPageHeaderLayoutUseCase: Lazy<GetShopPageHeaderLayoutUseCase>,
        private val getFollowStatusUseCase: Lazy<GetFollowStatusUseCase>,
        private val updateFollowStatusUseCase: Lazy<UpdateFollowStatusUseCase>,
        private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.main) {

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    val isUserSessionActive: Boolean
        get() = userSessionInterface.isLoggedIn

    val ownerShopName: String
        get() = userSessionInterface.shopName

    val userShopId: String
        get() = userSessionInterface.shopId

    val userId: String
        get() = userSessionInterface.userId

    private val _shopShareTracker = MutableLiveData<Result<ShopQuestGeneralTracker>>()
    val shopShareTracker : LiveData<Result<ShopQuestGeneralTracker>>
        get() = _shopShareTracker

    val shopPageP1Data = MutableLiveData<Result<NewShopPageP1HeaderData>>()
    val shopIdFromDomainData = MutableLiveData<Result<String>>()
    var productListData: ShopProduct.GetShopProduct = ShopProduct.GetShopProduct()
    val shopImagePath = MutableLiveData<String>()

    private val _shopUnmoderateData = MutableLiveData<Result<ShopRequestUnmoderateSuccessResponse>>()
    val shopUnmoderateData : LiveData<Result<ShopRequestUnmoderateSuccessResponse>>
        get() = _shopUnmoderateData

    private val _shopModerateRequestStatus = MutableLiveData<Result<ShopModerateRequestData>>()
    val shopModerateRequestStatus : LiveData<Result<ShopModerateRequestData>>
        get() = _shopModerateRequestStatus

    private val _followStatusData = MutableLiveData<Result<FollowStatusResponse>>()
    val followStatusData: LiveData<Result<FollowStatusResponse>>
        get() = _followStatusData

    private val _followShopData = MutableLiveData<Result<FollowShopResponse>>()
    val followShopData: LiveData<Result<FollowShopResponse>>
        get() = _followShopData

    private val _shopSellerPLayWidgetData = MutableLiveData<Result<Broadcaster.Config>>()
    val shopSellerPLayWidgetData : LiveData<Result<Broadcaster.Config>>
        get() = _shopSellerPLayWidgetData

    private val _shopPageTickerData = MutableLiveData<Result<ShopInfo.StatusInfo>>()
    val shopPageTickerData : LiveData<Result<ShopInfo.StatusInfo>>
        get() = _shopPageTickerData

    private val _shopPageShopShareData = MutableLiveData<Result<ShopInfo>>()
    val shopPageShopShareData: LiveData<Result<ShopInfo>>
        get() = _shopPageShopShareData

    fun getShopPageTabData(
            shopId: Int,
            shopDomain: String,
            page: Int,
            itemPerPage: Int,
            shopProductFilterParameter: ShopProductFilterParameter,
            keyword: String,
            etalaseId: String,
            isRefresh: Boolean,
            widgetUserAddressLocalData: LocalCacheModel
    ) {
        launchCatchError(block = {
            val shopP1DataAsync = asyncCatchError(
                    dispatcherProvider.io,
                    block = {
                        getShopP1Data(
                                shopId,
                                shopDomain,
                                isRefresh
                        )
                    },
                    onError = {
                        shopPageP1Data.postValue(Fail(it))
                        null
                    })

            val shopHeaderWidgetDataAsync = asyncCatchError(
                    dispatcherProvider.io,
                    block = {
                        getShopPageHeaderData(
                                shopId,
                                isRefresh
                        )
                    },
                    onError = {
                        shopPageP1Data.postValue(Fail(it))
                        null
                    })

            val productListDataAsync = asyncCatchError(
                    dispatcherProvider.io,
                    block = {
                        getProductListData(
                                shopId.toString(),
                                page,
                                itemPerPage,
                                shopProductFilterParameter,
                                keyword,
                                etalaseId,
                                widgetUserAddressLocalData
                        )
                    },
                    onError = {
                        shopPageP1Data.postValue(Fail(it))
                        null
                    }
            )
            shopP1DataAsync.await()?.let { shopPageHeaderP1Data ->
                productListDataAsync.await()?.let { shopProductData ->
                    productListData = shopProductData
                }
                shopHeaderWidgetDataAsync.await()?.let{ shopPageHeaderWidgetData ->
                    shopPageP1Data.postValue(Success(NewShopPageHeaderMapper.mapToShopPageP1HeaderData(
                            shopPageHeaderP1Data.isShopOfficialStore,
                            shopPageHeaderP1Data.isShopPowerMerchant,
                            shopPageHeaderP1Data.shopInfoHomeTypeData,
                            shopPageHeaderP1Data.feedWhitelist,
                            shopPageHeaderWidgetData
                    )))
                }
            }
        }) { exception ->
            shopPageP1Data.postValue(Fail(exception))
        }
    }

    private suspend fun getShopPageHeaderData(shopId: Int, isRefresh: Boolean): ShopPageHeaderLayoutResponse {
        val useCase = getShopPageHeaderLayoutUseCase.get()
        useCase.params = GetShopPageHeaderLayoutUseCase.createParams(shopId.toString())
        useCase.isFromCloud = isRefresh
        return useCase.executeOnBackground()
    }

    private suspend fun getProductListData(
            shopId: String,
            page: Int,
            itemPerPage: Int,
            shopProductFilterParameter: ShopProductFilterParameter,
            keyword: String,
            etalaseId: String,
            widgetUserAddressLocalData: LocalCacheModel
    ): ShopProduct.GetShopProduct {
        val useCase = getShopProductListUseCase.get()
        useCase.params = GqlGetShopProductUseCase.createParams(
                shopId,
                ShopProductFilterInput().apply {
                    etalaseMenu = etalaseId
                    this.page = page
                    perPage = itemPerPage
                    searchKeyword = keyword
                    sort = shopProductFilterParameter.getSortId().toIntOrZero()
                    rating = shopProductFilterParameter.getRating()
                    pmax = shopProductFilterParameter.getPmax()
                    pmin = shopProductFilterParameter.getPmin()
                    userDistrictId = widgetUserAddressLocalData.district_id
                    userCityId = widgetUserAddressLocalData.city_id
                    userLat = widgetUserAddressLocalData.lat
                    userLong = widgetUserAddressLocalData.long
                }
        )
        return useCase.executeOnBackground()
    }

    private suspend fun getShopP1Data(
            shopId: Int,
            shopDomain: String,
            isRefresh: Boolean
    ): ShopPageHeaderP1 {
        val useCase = getShopPageP1DataUseCase.get()
        useCase.isFromCacheFirst = !isRefresh
        useCase.params = GetShopPageP1DataUseCase.createParams(shopId, shopDomain)
        return useCase.executeOnBackground()
    }

    fun checkShopRequestModerateStatus() {
        launchCatchError(dispatcherProvider.io, {
            val shopModerateRequestStatusUseCase = shopModerateRequestStatusUseCase.get()
            val shopModerateRequestStatusResponse = shopModerateRequestStatusUseCase.executeOnBackground()
            _shopModerateRequestStatus.postValue(Success(shopModerateRequestStatusResponse))
        }) {
            _shopModerateRequestStatus.postValue(Fail(it))
        }
    }

    fun sendRequestUnmoderateShop(shopId : Double, optionValue : String) {
        launchCatchError(dispatcherProvider.io, {
            val shopUnmoderateUseCase = shopRequestUnmoderateUseCase.get().apply {
                params = ShopRequestUnmoderateUseCase.createRequestParams(shopId, optionValue)
            }
            val shopUnmoderateResponse = shopUnmoderateUseCase.executeOnBackground()
            _shopUnmoderateData.postValue(Success(shopUnmoderateResponse))

        }) {
            _shopUnmoderateData.postValue(Fail(it))
        }
    }

    fun saveShopImageToPhoneStorage(context: Context?, shopSnippetUrl: String) {
        launchCatchError(dispatcherProvider.io, {
            ImageHandler.loadImageWithTarget(context, shopSnippetUrl, object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val savedFile = ImageProcessingUtil.writeImageToTkpdPath(
                            resource,
                            Bitmap.CompressFormat.PNG
                    )
                    if (savedFile!= null) {
                        shopImagePath.postValue(savedFile.absolutePath)
                    }
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                    // no op
                }
            })
        }, onError = {
            it.printStackTrace()
        })
    }

    fun sendShopShareTracker(shopId : String, channel : String) {
        launchCatchError(dispatcherProvider.io, {
            val useCase = shopQuestGeneralTrackerUseCase.get()
            useCase.params = ShopQuestGeneralTrackerUseCase.createRequestParams(
                    actionName = ShopPageConstant.SHOP_SHARE_GQL_TRACKER_ACTION,
                    source = ShopPageConstant.SHOP_SHARE_GQL_TRACKER_SOURCE,
                    channel = channel,
                    input = ShopQuestGeneralTrackerInput(shopId)
            )
            val shopShareTrackerResponse = useCase.executeOnBackground()
            _shopShareTracker.postValue(Success(shopShareTrackerResponse))

        }) {
            _shopShareTracker.postValue(Fail(it))
        }
    }

    private suspend fun getShopBroadcasterConfig(shopId: String): Broadcaster.Config {
        var broadcasterConfig = Broadcaster.Config()
        try {
            getBroadcasterShopConfigUseCase.get().params = GetBroadcasterShopConfigUseCase.createParams(shopId)
            broadcasterConfig = getBroadcasterShopConfigUseCase.get().executeOnBackground()
        } catch (t: Throwable) {
        }
        return broadcasterConfig
    }

    fun getFollowStatusData(shopId: String) {
        launchCatchError(dispatcherProvider.io, block = {
            getFollowStatusUseCase.get().params = GetFollowStatusUseCase.createParams(shopId, SOURCE_SHOP_PAGE)
            _followStatusData.postValue(Success(getFollowStatusUseCase.get().executeOnBackground()))
        }, onError = {
            _followStatusData.postValue(Fail(it))
        })
    }

    fun updateFollowStatus(shopId: String, action: String) {
        if (!userSessionInterface.isLoggedIn) {
            _followShopData.value = Fail(UserNotLoginException())
            return
        }

        launchCatchError(dispatcherProvider.io, block = {
            updateFollowStatusUseCase.get().params = UpdateFollowStatusUseCase.createParams(shopId, action)
            _followShopData.postValue(Success(updateFollowStatusUseCase.get().executeOnBackground()))
        }, onError = {
            _followShopData.postValue(Fail(it))
        })
    }

    fun getShopInfoData(shopId: String, shopDomain: String, isRefresh: Boolean){
        launchCatchError(dispatcherProvider.io ,block = {
            val shopInfoForHeaderResponse = getShopInfoHeader(
                    shopId.toIntOrZero(),
                    shopDomain,
                    isRefresh
            )
            _shopPageTickerData.postValue(Success(shopInfoForHeaderResponse.statusInfo))
            _shopPageShopShareData.postValue(Success(shopInfoForHeaderResponse))
        }) {}
    }

    fun getSellerPlayWidgetData(shopId: String){
        launchCatchError(dispatcherProvider.io ,block = {
            var broadcasterConfig: Broadcaster.Config = Broadcaster.Config()
            if(isMyShop(shopId = shopId)) {
                broadcasterConfig = getShopBroadcasterConfig(shopId)
            }
            _shopSellerPLayWidgetData.postValue(Success(broadcasterConfig))
        }) {
            _shopSellerPLayWidgetData.postValue(Fail(it))
        }
    }

    private suspend fun getShopInfoHeader(shopId: Int, shopDomain: String, refresh: Boolean): ShopInfo {
        gqlGetShopInfoForHeaderUseCase.get().isFromCacheFirst = !refresh
        gqlGetShopInfoForHeaderUseCase.get().params = GQLGetShopInfoUseCase.createParams(
                if (shopId == 0) listOf() else listOf(shopId),
                shopDomain,
                source = SHOP_PAGE_SOURCE,
                fields = listOf(
                        FIELD_CORE,
                        FIELD_ASSETS,
                        FIELD_LAST_ACTIVE,
                        FIELD_LOCATION,
                        FIELD_ALLOW_MANAGE,
                        FIELD_IS_OWNER,
                        FIELD_STATUS,
                        FIELD_IS_OPEN,
                        FIELD_CLOSED_INFO,
                        FIELD_CREATE_INFO,
                        FIELD_SHOP_SNIPPET
                )
        )
        return gqlGetShopInfoForHeaderUseCase.get().executeOnBackground()
    }

    fun getShopIdFromDomain(shopDomain: String) {
        launchCatchError(block = {
            flow{
                emit(getShopInfoCoreFromDomain(shopDomain))
            }.flowOn(dispatcherProvider.io).collect{
                shopIdFromDomainData.postValue(Success(it.shopCore.shopID))
            }
        }){
            shopIdFromDomainData.postValue(Fail(it))
        }
    }

    private suspend fun getShopInfoCoreFromDomain(shopDomain: String): ShopInfo {
        val useCase = gqlGetShopInfobUseCaseCoreAndAssets.get()
        useCase.params = GQLGetShopInfoUseCase.createParams(
                listOf(),
                shopDomain,
                listOf(
                        FIELD_CORE
                )
        )
        return useCase.executeOnBackground()
    }
}