package com.tokopedia.shop.pageheader.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.common.constant.ShopPageConstant.DISABLE_SHOP_PAGE_CACHE_INITIAL_PRODUCT_LIST
import com.tokopedia.shop.common.di.GqlGetShopInfoForHeaderUseCaseQualifier
import com.tokopedia.shop.common.di.GqlGetShopInfoUseCaseCoreAndAssetsQualifier
import com.tokopedia.shop.common.domain.interactor.GQLGetShopFavoriteStatusUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
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
import com.tokopedia.shop.common.domain.interactor.GQLGetShopOperationalHourStatusUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderContentData
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderP1
import com.tokopedia.shop.pageheader.domain.interactor.GetBroadcasterShopConfigUseCase
import com.tokopedia.shop.pageheader.domain.interactor.GetShopPageP1DataUseCase
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageP1HeaderData
import com.tokopedia.shop.pageheader.util.ShopPageHeaderMapper
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import rx.Subscriber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ShopPageViewModel @Inject constructor(
        private val gqlGetShopFavoriteStatusUseCase: Lazy<GQLGetShopFavoriteStatusUseCase>,
        private val userSessionInterface: UserSessionInterface,
        @GqlGetShopInfoForHeaderUseCaseQualifier
        private val gqlGetShopInfoForHeaderUseCase: Lazy<GQLGetShopInfoUseCase>,
        private val getBroadcasterShopConfigUseCase: Lazy<GetBroadcasterShopConfigUseCase>,
        @GqlGetShopInfoUseCaseCoreAndAssetsQualifier
        private val gqlGetShopInfobUseCaseCoreAndAssets: Lazy<GQLGetShopInfoUseCase>,
        private val getShopReputationUseCase: Lazy<GetShopReputationUseCase>,
        private val toggleFavouriteShopUseCase: Lazy<ToggleFavouriteShopUseCase>,
        private val gqlGetShopOperationalHourStatusUseCase: Lazy<GQLGetShopOperationalHourStatusUseCase>,
        private val getShopPageP1DataUseCase: Lazy<GetShopPageP1DataUseCase>,
        private val getShopProductListUseCase: Lazy<GqlGetShopProductUseCase>,
        private val firebaseRemoteConfig: RemoteConfig,
        private val dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.main) {

    companion object {
        private const val DATA_NOT_FOUND = "Data not found"
    }

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    val isUserSessionActive: Boolean
        get() = userSessionInterface.isLoggedIn

    val ownerShopName: String
        get() = userSessionInterface.shopName

    val userShopId: String
        get() = userSessionInterface.shopId

    val shopPageP1Data = MutableLiveData<Result<ShopPageP1HeaderData>>()
    val shopIdFromDomainData = MutableLiveData<Result<String>>()
    val shopPageHeaderContentData = MutableLiveData<Result<ShopPageHeaderContentData>>()
    var productListData: ShopProduct.GetShopProduct = ShopProduct.GetShopProduct()
    val shopImagePath = MutableLiveData<String>()
    private val TEN_MINUTE_MS: Long = 1000 * 60 * 10.toLong()

    fun getShopPageTabData(
            shopId: Int,
            shopDomain: String,
            page: Int,
            itemPerPage: Int,
            shopProductFilterParameter: ShopProductFilterParameter,
            keyword: String,
            etalaseId: String,
            isRefresh: Boolean
    ) {
        launchCatchError(block = {
            val flowShopP1Data = flow {
                emit(getShopP1Data(shopId, shopDomain, isRefresh))
            }
            val flowProductListData = flow {
                emit(getProductListData(
                        shopId.toString(),
                        page,
                        itemPerPage,
                        shopProductFilterParameter,
                        keyword,
                        etalaseId,
                        isRefresh
                ))
            }
            combine(
                    flowShopP1Data,
                    flowProductListData
            ){ shopPageHeaderP1Data, productListData ->
                this@ShopPageViewModel.productListData = productListData
                ShopPageHeaderMapper.mapToShopPageP1HeaderData(
                        shopPageHeaderP1Data.isShopOfficialStore,
                        shopPageHeaderP1Data.isShopPowerMerchant,
                        shopPageHeaderP1Data.shopInfoTopContentData,
                        shopPageHeaderP1Data.shopInfoHomeTypeData,
                        shopPageHeaderP1Data.shopInfoCoreAndAssetsData,
                        shopPageHeaderP1Data.feedWhitelist
                )
            }.flowOn(dispatcherProvider.io).collect{
                shopPageP1Data.postValue(Success(it))
            }
        }) { exception ->
            shopPageP1Data.postValue(Fail(exception))
        }
    }

    private suspend fun getProductListData(
            shopId: String,
            page: Int,
            itemPerPage: Int,
            shopProductFilterParameter: ShopProductFilterParameter,
            keyword: String,
            etalaseId: String,
            isRefresh: Boolean
    ): ShopProduct.GetShopProduct {
        val useCase = getShopProductListUseCase.get()
        val isDisableProductListCache = firebaseRemoteConfig.getBoolean(
                DISABLE_SHOP_PAGE_CACHE_INITIAL_PRODUCT_LIST,
                false
        )
        useCase.isFromCacheFirst = (!isRefresh).takeIf { !isDisableProductListCache } ?: false
        useCase.cacheTime = TEN_MINUTE_MS
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

    fun saveShopImageToPhoneStorage(context: Context?, shopSnippetUrl: String) {
        launchCatchError(dispatcherProvider.io, {
            ImageHandler.loadImageWithTarget(context, shopSnippetUrl, object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val savedFile = ImageUtils.writeImageToTkpdPath(
                            ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE,
                            resource,
                            true
                    )
                    if(savedFile != null) {
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

    private suspend fun getShopOperationalHourStatus(shopId: Int): ShopOperationalHourStatus {
        gqlGetShopOperationalHourStatusUseCase.get().params = GQLGetShopOperationalHourStatusUseCase.createParams(shopId.toString())
        return gqlGetShopOperationalHourStatusUseCase.get().executeOnBackground()
    }

    private suspend fun getShopFavoriteStatus(shopId: String, shopDomain: String): ShopInfo.FavoriteData {
        val id = shopId.toIntOrZero()
        var favoritInfo = ShopInfo.FavoriteData()
        try {
            gqlGetShopFavoriteStatusUseCase.get().params = GQLGetShopFavoriteStatusUseCase.createParams(if (id == 0) listOf() else listOf(id), shopDomain)
            favoritInfo = gqlGetShopFavoriteStatusUseCase.get().executeOnBackground().favoriteData
        } catch (t: Throwable) {
        }
        return favoritInfo
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

    fun toggleFavorite(shopID: String, onSuccess: (Boolean) -> Unit, onError: (Throwable) -> Unit) {
        if (!userSessionInterface.isLoggedIn) {
            onError(UserNotLoginException())
            return
        }

        toggleFavouriteShopUseCase.get().execute(ToggleFavouriteShopUseCase.createRequestParam(shopID),
                object : Subscriber<Boolean>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        onError(e)
                    }

                    override fun onNext(success: Boolean) {
                        onSuccess(success)
                    }
                })
    }

    override fun flush() {
        super.flush()
        toggleFavouriteShopUseCase.get().unsubscribe()
    }

    fun getShopPageHeaderContentData(shopId: String, shopDomain: String, isRefresh: Boolean) {
        launchCatchError(block = {
            val flowShopInfoHeader = flow {
                emit(getShopInfoHeader(shopId.toIntOrZero(), shopDomain, isRefresh))
            }
            val flowShopBadgeData = flow {
                emit(getShopBadgeData(shopId.toIntOrZero(), isRefresh))
            }
            val flowShopOperationalHourStatus = flow {
                emit(getShopOperationalHourStatus(shopId.toIntOrZero()))
            }
            val flowShopFavoriteStatus = flow {
                emit(getShopFavoriteStatus(shopId, shopDomain))
            }
            val flowBroadcasterConfig = flow {
                var broadcasterConfig: Broadcaster.Config = Broadcaster.Config()
                if (isMyShop(shopId = shopId)) {
                    broadcasterConfig = getShopBroadcasterConfig(shopId)
                }
                emit(broadcasterConfig)
            }.catch {
                emit(Broadcaster.Config())
            }.flowOn(dispatcherProvider.io)
            combine(
                    flowShopInfoHeader,
                    flowShopBadgeData,
                    flowShopOperationalHourStatus,
                    flowBroadcasterConfig,
                    flowShopFavoriteStatus
            ) { shopInfoHeader, shopBadge, shopOperationalHourStatus, broadcasterConfigData, shopInfoFavoriteData ->
                ShopPageHeaderContentData(
                        shopInfoHeader,
                        shopBadge,
                        shopOperationalHourStatus,
                        broadcasterConfigData,
                        shopInfoFavoriteData
                )
            }.flowOn(dispatcherProvider.io).collect {
                shopPageHeaderContentData.postValue(Success(it))
            }
        }) {
            shopPageHeaderContentData.postValue(Fail(it))
        }

    }

    private suspend fun getShopBadgeData(shopId: Int, refresh: Boolean): ShopBadge {
        getShopReputationUseCase.get().isFromCacheFirst = !refresh
        getShopReputationUseCase.get().params = GetShopReputationUseCase.createParams(shopId)
        return getShopReputationUseCase.get().executeOnBackground()
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