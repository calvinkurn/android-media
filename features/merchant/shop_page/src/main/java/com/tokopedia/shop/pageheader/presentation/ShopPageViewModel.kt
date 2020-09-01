package com.tokopedia.shop.pageheader.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.di.GqlGetShopInfoForHeaderUseCaseQualifier
import com.tokopedia.shop.common.di.*
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
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.isshoppowermerchant.GetIsShopPowerMerchant
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.shop.pageheader.data.model.ShopPageGetHomeType
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderContentData
import com.tokopedia.shop.pageheader.domain.interactor.GetBroadcasterShopConfigUseCase
import com.tokopedia.shop.pageheader.data.model.ShopPageP1Data
import com.tokopedia.shop.pageheader.domain.interactor.GetModerateShopUseCase
import com.tokopedia.shop.pageheader.domain.interactor.GqlShopPageGetHomeType
import com.tokopedia.shop.pageheader.domain.interactor.RequestModerateShopUseCase
import com.tokopedia.shop.pageheader.util.ShopPageHeaderMapper
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.view.datamodel.ShopProductViewModel
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject

class ShopPageViewModel @Inject constructor(
        private val gqlGetShopFavoriteStatusUseCase: Lazy<GQLGetShopFavoriteStatusUseCase>,
        private val userSessionInterface: UserSessionInterface,
        @GqlGetShopInfoForHeaderUseCaseQualifier
        private val gqlGetShopInfoForHeaderUseCase: Lazy<GQLGetShopInfoUseCase>,
        private val getBroadcasterShopConfigUseCase: Lazy<GetBroadcasterShopConfigUseCase>,
        private val gqlGetIsOfficialUseCase: Lazy<GqlGetIsShopOsUseCase>,
        private val gqlGetIsPowerMerchantUseCase: Lazy<GqlGetIsShopPmUseCase>,
        private val gqlGetShopPageHomeType: Lazy<GqlShopPageGetHomeType>,
        @GqlGetShopInfoUseCaseTopContentQualifier
        private val gqlGetShopInfobUseCaseTopContent: Lazy<GQLGetShopInfoUseCase>,
        @GqlGetShopInfoUseCaseCoreAndAssetsQualifier
        private val gqlGetShopInfobUseCaseCoreAndAssets: Lazy<GQLGetShopInfoUseCase>,
        private val getWhitelistUseCase: Lazy<GetWhitelistUseCase>,
        private val getShopReputationUseCase: Lazy<GetShopReputationUseCase>,
        private val toggleFavouriteShopUseCase: Lazy<ToggleFavouriteShopUseCase>,
        private val getModerateShopUseCase: Lazy<GetModerateShopUseCase>,
        private val requestModerateShopUseCase: Lazy<RequestModerateShopUseCase>,
        private val stickyLoginUseCase: Lazy<StickyLoginUseCase>,
        private val gqlGetShopOperationalHourStatusUseCase: Lazy<GQLGetShopOperationalHourStatusUseCase>,
        private val getShopProductUseCase: Lazy<GqlGetShopProductUseCase>,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    companion object {
        private const val DATA_NOT_FOUND = "Data not found"
        private const val START_PAGE = 1
    }

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    val isUserSessionActive: Boolean
        get() = userSessionInterface.isLoggedIn

    val shopPageP1Data = MutableLiveData<Result<ShopPageP1Data>>()
    val shopIdFromDomainData = MutableLiveData<Result<String>>()
    val shopPageHeaderContentData = MutableLiveData<Result<ShopPageHeaderContentData>>()
    var productListData: Pair<Boolean, List<ShopProductViewModel>> = Pair(false, listOf())
    val shopImagePath = MutableLiveData<String>()

    fun getShopPageTabData(
            shopId: String? = null,
            shopDomain: String? = null,
            isRefresh: Boolean = false,
            initialProductListSortId: String
    ) {
        val id = shopId?.toIntOrNull() ?: 0
        if (id == 0 && shopDomain == null) return
        launchCatchError(block = {
            val getIsShopOfficialDataAsync = asyncCatchError(
                    Dispatchers.IO,
                    block = {
                        getIsShopOfficial(id, isRefresh)
                    },
                    onError = {
                        shopPageP1Data.postValue(Fail(it))
                        null
                    })

            val getIsShopPowerMerchantDataAsync = asyncCatchError(
                    Dispatchers.IO,
                    block = {
                        getIsShopPowerMerchant(id, isRefresh)
                    },
                    onError = {
                        shopPageP1Data.postValue(Fail(it))
                        null
                    })

            val shopInfoTopContentDataAsync = asyncCatchError(
                    Dispatchers.IO,
                    block = {
                        getShopInfoTopContentData(id, shopDomain, isRefresh)
                    },
                    onError = {
                        shopPageP1Data.postValue(Fail(it))
                        null
                    })

            val shopPageHomeTypeDataAsync = asyncCatchError(
                    Dispatchers.IO,
                    block = {
                        getShopInfoHomeTypeData(id, shopDomain, isRefresh)
                    },
                    onError = {
                        shopPageP1Data.postValue(Fail(it))
                        null
                    })

            val shopInfoShopCoreShopAssetsDataAsync = asyncCatchError(
                    Dispatchers.IO,
                    block = {
                        getShopInfoCoreAndAssetsData(id, shopDomain, isRefresh)
                    },
                    onError = {
                        shopPageP1Data.postValue(Fail(it))
                        null
                    })

            val feedWhitelistAsync = asyncCatchError(
                    Dispatchers.IO,
                    block = {
                        getFeedWhitelist(shopId.toIntOrZero(), isRefresh)
                    },
                    onError = {
                        shopPageP1Data.postValue(Fail(it))
                        null
                    }
            )

            val productListDataAsync = asyncCatchError(
                    Dispatchers.IO,
                    block = {
                        getProductList(
                                getShopProductUseCase.get(),
                                shopId.orEmpty(),
                                START_PAGE,
                                ShopPageConstant.DEFAULT_PER_PAGE,
                                "",
                                "",
                                initialProductListSortId.toIntOrZero(),
                                isRefresh
                        )
                    },
                    onError = {
                        shopPageP1Data.postValue(Fail(it))
                        null
                    })
            val shopInfoOsData = getIsShopOfficialDataAsync.await()
            val shopInfoGoldData = getIsShopPowerMerchantDataAsync.await()
            val shopInfoTopContentData = shopInfoTopContentDataAsync.await()
            val shopPageHomeTypeData = shopPageHomeTypeDataAsync.await()
            val shopInfoShopCoreShopAssetsData = shopInfoShopCoreShopAssetsDataAsync.await()
            val feedWhitelistData = feedWhitelistAsync.await()
            productListDataAsync.await()?.let { productList ->
                productListData = productList
            }
            if (null != shopInfoOsData && null != shopInfoGoldData && null != shopInfoTopContentData
                    && null != shopPageHomeTypeData && null != shopInfoShopCoreShopAssetsData
                    && null != feedWhitelistData
            ) {
                shopPageP1Data.postValue(Success(ShopPageHeaderMapper.mapToShopPageP1Data(
                        shopInfoOsData,
                        shopInfoGoldData,
                        shopInfoTopContentData,
                        shopPageHomeTypeData,
                        shopInfoShopCoreShopAssetsData,
                        feedWhitelistData
                )))
            }
        }) {
            shopPageP1Data.postValue(Fail(it))
        }
    }

    fun saveShopImageToPhoneStorage(context: Context?, shopSnippetUrl: String) {
        launchCatchError(Dispatchers.IO, {
            ImageHandler.loadImageWithTarget(context, shopSnippetUrl, object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val savedFile = ImageUtils.writeImageToTkpdPath(
                            ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE,
                            resource,
                            true
                    )
                    if(savedFile != null) {
                        shopImagePath.value = savedFile.absolutePath
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

    private suspend fun getIsShopOfficial(shopId: Int, isRefresh: Boolean): GetIsShopOfficialStore {
        val useCase = gqlGetIsOfficialUseCase.get()
        useCase.params = GqlGetIsShopOsUseCase.createParams(shopId)
        useCase.isFromCacheFirst = !isRefresh
        return useCase.executeOnBackground()
    }

    private suspend fun getIsShopPowerMerchant(shopId: Int, isRefresh: Boolean): GetIsShopPowerMerchant {
        val useCase = gqlGetIsPowerMerchantUseCase.get()
        useCase.params = GqlGetIsShopPmUseCase.createParams(shopId)
        useCase.isFromCacheFirst = !isRefresh
        return useCase.executeOnBackground()
    }

    private suspend fun getShopInfoTopContentData(shopId: Int, shopDomain: String?, isRefresh: Boolean): ShopInfo {
        gqlGetShopInfobUseCaseTopContent.get().params = GQLGetShopInfoUseCase.createParams(
                if (shopId == 0) listOf() else listOf(shopId),
                shopDomain,
                source = SHOP_PAGE_SOURCE,
                fields = listOf(GQLGetShopInfoUseCase.FIELD_TOP_CONTENT)
        )
        gqlGetShopInfobUseCaseTopContent.get().isFromCacheFirst = !isRefresh
        return gqlGetShopInfobUseCaseTopContent.get().executeOnBackground()
    }

    private suspend fun getShopInfoHomeTypeData(shopId: Int, shopDomain: String?, isRefresh: Boolean): ShopPageGetHomeType {
        val useCase = gqlGetShopPageHomeType.get()
        useCase.params = GqlShopPageGetHomeType.createParams(shopId)
        useCase.isFromCacheFirst = !isRefresh
        return useCase.executeOnBackground()
    }

    private suspend fun getShopInfoCoreAndAssetsData(shopId: Int, shopDomain: String?, isRefresh: Boolean): ShopInfo {
        gqlGetShopInfobUseCaseCoreAndAssets.get().params = GQLGetShopInfoUseCase.createParams(
                if (shopId == 0) listOf() else listOf(shopId),
                shopDomain,
                source = SHOP_PAGE_SOURCE,
                fields = listOf(GQLGetShopInfoUseCase.FIELD_CORE, GQLGetShopInfoUseCase.FIELD_ASSETS)
        )
        gqlGetShopInfobUseCaseCoreAndAssets.get().isFromCacheFirst = !isRefresh
        return gqlGetShopInfobUseCaseCoreAndAssets.get().executeOnBackground()
    }

    private suspend fun getProductList(
            useCase: GqlGetShopProductUseCase,
            shopId: String,
            page: Int,
            perPage: Int,
            etalaseId: String,
            keyword: String,
            sortId: Int,
            isRefresh: Boolean
    ): Pair<Boolean, List<ShopProductViewModel>> {
        useCase.isFromCacheFirst = !isRefresh
        useCase.params = GqlGetShopProductUseCase.createParams(shopId, ShopProductFilterInput(
                page, perPage, keyword, etalaseId, sortId
        ))
        val productListResponse = useCase.executeOnBackground()
        val isHasNextPage = isHasNextPage(page, ShopPageConstant.DEFAULT_PER_PAGE, productListResponse.totalData)
        return Pair(
                isHasNextPage,
                productListResponse.data.map { ShopPageProductListMapper.mapShopProductToProductViewModel(it, isMyShop(shopId), etalaseId) }
        )
    }

    private fun isHasNextPage(page: Int, perPage: Int, totalData: Int): Boolean = page * perPage < totalData

    private suspend fun getShopOperationalHourStatus(shopId: Int): ShopOperationalHourStatus {
        gqlGetShopOperationalHourStatusUseCase.get().params = GQLGetShopOperationalHourStatusUseCase.createParams(shopId.toString())
        return gqlGetShopOperationalHourStatusUseCase.get().executeOnBackground()
    }

    private suspend fun getFeedWhitelist(shopId: Int, isRefresh: Boolean): Whitelist? {
        val feedWhitelistRequest = getWhitelistUseCase.get().getRequest(GetWhitelistUseCase.createRequestParams(
                GetWhitelistUseCase.WHITELIST_SHOP,
                shopId.toString()
        ))
        getWhitelistUseCase.get().clearRequest()
        getWhitelistUseCase.get().addRequest(feedWhitelistRequest)
        getWhitelistUseCase.get().setCacheStrategy(GraphqlCacheStrategy.Builder(if (isRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build())
        val gqlResponse = getWhitelistUseCase.get().createObservable(RequestParams.EMPTY).toBlocking().first()
        val feedWhitelistError = gqlResponse.getError(WhitelistQuery::class.java)
        val feedWhitelist = gqlResponse.getData<WhitelistQuery>(WhitelistQuery::class.java)?.whitelist
        if (feedWhitelistError == null || feedWhitelistError.isEmpty()) {
            return feedWhitelist
        } else {
            throw MessageErrorException(feedWhitelistError.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    private suspend fun getShopFavoriteStatus(shopId: String? = null, shopDomain: String? = null): ShopInfo.FavoriteData {
        val id = shopId?.toIntOrNull() ?: 0
        var favoritInfo = ShopInfo.FavoriteData()
        try {
            gqlGetShopFavoriteStatusUseCase.get().params = GQLGetShopFavoriteStatusUseCase.createParams(if (id == 0) listOf() else listOf(id), shopDomain)
            favoritInfo = gqlGetShopFavoriteStatusUseCase.get().executeOnBackground().favoriteData
        } catch (t: Throwable) {
        }
        return favoritInfo
    }

    private suspend fun getShopBroadcasterConfig(shopId: String? = null): Broadcaster.Config {
        var broadcasterConfig = Broadcaster.Config()
        try {
            getBroadcasterShopConfigUseCase.get().params = GetBroadcasterShopConfigUseCase.createParams(shopId ?: "")
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

    fun getStickyLoginContent(onSuccess: (StickyLoginTickerPojo.TickerDetail) -> Unit, onError: ((Throwable) -> Unit)?) {
        stickyLoginUseCase.get().setParams(StickyLoginConstant.Page.SHOP)
        stickyLoginUseCase.get().execute(
                onSuccess = {
                    if (it.response.tickers.isNotEmpty()) {
                        for (tickerDetail in it.response.tickers) {
                            if (tickerDetail.layout == StickyLoginConstant.LAYOUT_FLOATING) {
                                onSuccess.invoke(tickerDetail)
                                return@execute
                            }
                        }
                        onError?.invoke(Throwable(""))
                    } else {
                        onError?.invoke(Throwable(DATA_NOT_FOUND))
                    }
                },
                onError = {
                    onError?.invoke(it)
                }
        )
    }

    override fun flush() {
        super.flush()
        getWhitelistUseCase.get().unsubscribe()
        toggleFavouriteShopUseCase.get().unsubscribe()
        getModerateShopUseCase.get().unsubscribe()
        requestModerateShopUseCase.get().unsubscribe()
        stickyLoginUseCase.get().cancelJobs()
    }

    fun getShopPageHeaderContentData(shopId: String, shopDomain: String, isRefresh: Boolean) {
        val id = shopId.toIntOrNull() ?: 0
        if (id == 0 && shopDomain.isEmpty()) return
        launchCatchError(block = {
            val shopInfoForHeaderResponse = asyncCatchError(
                    Dispatchers.IO,
                    block = {
                        getShopInfoHeader(id, shopDomain, isRefresh)
                    },
                    onError = {
                        shopPageHeaderContentData.postValue(Fail(it))
                        null
                    }
            )

            val shopBadgeDataResponse = asyncCatchError(
                    Dispatchers.IO,
                    block = {
                        getShopBadgeData(id, isRefresh)
                    },
                    onError = {
                        shopPageHeaderContentData.postValue(Fail(it))
                        null
                    }
            )

            val shopOperationalHourStatus = asyncCatchError(
                    Dispatchers.IO,
                    block = { getShopOperationalHourStatus(id) },
                    onError = {
                        shopPageHeaderContentData.postValue(Fail(it))
                        null
                    }
            )

            val shopFavourite = asyncCatchError(
                    Dispatchers.IO,
                    block = { getShopFavoriteStatus(shopId, shopDomain) },
                    onError = {
                        shopPageHeaderContentData.postValue(Fail(it))
                        null
                    }
            )
            var broadcasterConfig: Broadcaster.Config = Broadcaster.Config()
            if(isMyShop(shopId = shopId)) {
                broadcasterConfig = asyncCatchError(
                        Dispatchers.IO,
                        block = { getShopBroadcasterConfig(shopId) },
                        onError = { null }
                ).await() ?: Broadcaster.Config()
            }

            val shopInfoForHeaderData = shopInfoForHeaderResponse.await()
            val shopBadgeData = shopBadgeDataResponse.await()
            val shopOperationalHourStatusResponse = shopOperationalHourStatus.await()
            val shopFavouriteResponse = shopFavourite.await()
            if (null != shopInfoForHeaderData && null != shopBadgeData && null != shopOperationalHourStatusResponse && null != shopFavouriteResponse) {
                shopPageHeaderContentData.postValue(Success(ShopPageHeaderContentData(
                        shopInfoForHeaderData,
                        shopBadgeData,
                        shopOperationalHourStatusResponse,
                        broadcasterConfig,
                        shopFavouriteResponse
                )))
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
            val shopInfoCoreData = withContext(Dispatchers.IO){
                getShopInfoCoreFromDomain(shopDomain)
            }
            shopIdFromDomainData.postValue(Success(shopInfoCoreData.shopCore.shopID))
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