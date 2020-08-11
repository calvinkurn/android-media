package com.tokopedia.shop.pageheader.presentation

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.shop.common.di.GqlGetShopInfoForHeaderUseCaseQualifier
import com.tokopedia.shop.common.di.GqlGetShopInfoForTabUseCaseQualifier
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
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_STATUS
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.SHOP_PAGE_SOURCE
import com.tokopedia.shop.common.domain.interactor.GQLGetShopOperationalHourStatusUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderContentData
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderTabData
import com.tokopedia.shop.pageheader.domain.interactor.GetBroadcasterShopConfigUseCase
import com.tokopedia.shop.pageheader.domain.interactor.GetModerateShopUseCase
import com.tokopedia.shop.pageheader.domain.interactor.RequestModerateShopUseCase
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject

class ShopPageViewModel @Inject constructor(private val gqlRepository: GraphqlRepository,
                                            private val gqlGetShopFavoriteStatusUseCase: GQLGetShopFavoriteStatusUseCase,
                                            private val userSessionInterface: UserSessionInterface,
                                            @GqlGetShopInfoForHeaderUseCaseQualifier
                                            private val gqlGetShopInfoForHeaderUseCase: GQLGetShopInfoUseCase,
                                            private val getBroadcasterShopConfigUseCase: GetBroadcasterShopConfigUseCase,
                                            @GqlGetShopInfoForTabUseCaseQualifier
                                            private val gqlGetShopInfoForTabUseCase: GQLGetShopInfoUseCase,
                                            private val getWhitelistUseCase: GetWhitelistUseCase,
                                            private val getShopReputationUseCase: GetShopReputationUseCase,
                                            private val toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase,
                                            private val getModerateShopUseCase: GetModerateShopUseCase,
                                            private val requestModerateShopUseCase: RequestModerateShopUseCase,
                                            private val stickyLoginUseCase: StickyLoginUseCase,
                                            private val gqlGetShopOperationalHourStatusUseCase: GQLGetShopOperationalHourStatusUseCase,
                                            dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    companion object {
        private const val DATA_NOT_FOUND = "Data not found"
    }

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    val isUserSessionActive: Boolean
        get() = userSessionInterface.isLoggedIn

    val shopPageHeaderTabData = MutableLiveData<Result<ShopPageHeaderTabData>>()
    val shopPageHeaderContentData = MutableLiveData<Result<ShopPageHeaderContentData>>()

    fun getShopPageTabData(shopId: String? = null, shopDomain: String? = null, isRefresh: Boolean = false) {
        val id = shopId?.toIntOrNull() ?: 0
        if (id == 0 && shopDomain == null) return
        launchCatchError(block = {
            val shopPageTabDataResponse = withContext(Dispatchers.IO) {
                getShopPageTabData(id, shopDomain, isRefresh)
            }
            shopPageHeaderTabData.postValue(Success(shopPageTabDataResponse))
        }) {
            shopPageHeaderTabData.postValue(Fail(it))
        }
    }

    private suspend fun getShopOperationalHourStatus(shopId: Int): ShopOperationalHourStatus {
        gqlGetShopOperationalHourStatusUseCase.params = GQLGetShopOperationalHourStatusUseCase.createParams(shopId.toString())
        return gqlGetShopOperationalHourStatusUseCase.executeOnBackground()
    }

    private suspend fun getShopPageTabData(shopId: Int, shopDomain: String?, isRefresh: Boolean): ShopPageHeaderTabData {
        val shopPageTabData = ShopPageHeaderTabData()
        gqlGetShopInfoForTabUseCase.params = GQLGetShopInfoUseCase.createParams(
                if (shopId == 0) listOf() else listOf(shopId),
                shopDomain,
                source = SHOP_PAGE_SOURCE,
                fields = listOf()
        )
        val shopInfoForTabDataRequest = gqlGetShopInfoForTabUseCase.request
        val feedWhitelistRequest = getWhitelistUseCase.getRequest(GetWhitelistUseCase.createRequestParams(
                GetWhitelistUseCase.WHITELIST_SHOP,
                shopId.toString()
        ))
        val requests = mutableListOf(shopInfoForTabDataRequest, feedWhitelistRequest)
        val cacheStrategy = GraphqlCacheStrategy.Builder(if (isRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
        val gqlResponse = gqlRepository.getReseponse(requests, cacheStrategy)
        val shopInfoError = gqlResponse.getError(ShopInfo.Response::class.java)
        val shopInfo = gqlResponse.getData<ShopInfo.Response>(ShopInfo.Response::class.java).result.data.firstOrNull()
        if (shopInfoError == null || shopInfoError.isEmpty()) {
            shopInfo?.let {
                shopPageTabData.shopInfo = it
            }
        } else {
            throw MessageErrorException(shopInfoError.mapNotNull { it.message }.joinToString(separator = ", "))
        }
        val feedWhitelistError = gqlResponse.getError(WhitelistQuery::class.java)
        val feedWhitelist = gqlResponse.getData<WhitelistQuery>(WhitelistQuery::class.java)?.whitelist
        if (feedWhitelistError == null || feedWhitelistError.isEmpty()) {
            feedWhitelist?.let {
                shopPageTabData.feedWhitelist = it
            }
        } else {
            throw MessageErrorException(feedWhitelistError.mapNotNull { it.message }.joinToString(separator = ", "))
        }
        return shopPageTabData
    }

    private suspend fun getShopFavoriteStatus(shopId: String? = null, shopDomain: String? = null): ShopInfo.FavoriteData {
        val id = shopId?.toIntOrNull() ?: 0
        var favoritInfo = ShopInfo.FavoriteData()
        try {
            gqlGetShopFavoriteStatusUseCase.params = GQLGetShopFavoriteStatusUseCase.createParams(if (id == 0) listOf() else listOf(id), shopDomain)
            favoritInfo = gqlGetShopFavoriteStatusUseCase.executeOnBackground().favoriteData
        } catch (t: Throwable) {
        }
        return favoritInfo
    }

    private suspend fun getShopBroadcasterConfig(shopId: String? = null): Broadcaster.Config {
        var broadcasterConfig = Broadcaster.Config()
        try {
            getBroadcasterShopConfigUseCase.params = GetBroadcasterShopConfigUseCase.createParams(shopId ?: "")
            broadcasterConfig = getBroadcasterShopConfigUseCase.executeOnBackground()
        } catch (t: Throwable) {
        }
        return broadcasterConfig
    }

    fun toggleFavorite(shopID: String, onSuccess: (Boolean) -> Unit, onError: (Throwable) -> Unit) {
        if (!userSessionInterface.isLoggedIn) {
            onError(UserNotLoginException())
            return
        }

        toggleFavouriteShopUseCase.execute(ToggleFavouriteShopUseCase.createRequestParam(shopID),
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
        stickyLoginUseCase.setParams(StickyLoginConstant.Page.SHOP)
        stickyLoginUseCase.execute(
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
        getWhitelistUseCase.unsubscribe()
        toggleFavouriteShopUseCase.unsubscribe()
        getModerateShopUseCase.unsubscribe()
        requestModerateShopUseCase.unsubscribe()
        stickyLoginUseCase.cancelJobs()
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
        getShopReputationUseCase.isFromCacheFirst = !refresh
        getShopReputationUseCase.params = GetShopReputationUseCase.createParams(shopId)
        return getShopReputationUseCase.executeOnBackground()
    }

    private suspend fun getShopInfoHeader(shopId: Int, shopDomain: String, refresh: Boolean): ShopInfo {
        gqlGetShopInfoForHeaderUseCase.isFromCacheFirst = !refresh
        gqlGetShopInfoForHeaderUseCase.params = GQLGetShopInfoUseCase.createParams(
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
                        FIELD_CREATE_INFO
                )
        )
        return gqlGetShopInfoForHeaderUseCase.executeOnBackground()
    }
}