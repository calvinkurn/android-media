package com.tokopedia.shop.oldpage.view

import androidx.lifecycle.MutableLiveData
import android.text.TextUtils
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData
import com.tokopedia.shop.common.domain.interactor.GQLGetShopFavoriteStatusUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.oldpage.data.model.ShopInfoShopBadgeFeedWhitelist
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.shop.oldpage.domain.interactor.GetModerateShopUseCase
import com.tokopedia.shop.oldpage.domain.interactor.RequestModerateShopUseCase
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import rx.Subscriber
import javax.inject.Inject

class ShopPageViewModel @Inject constructor(private val gqlRepository: GraphqlRepository,
                                            private val gqlGetShopFavoriteStatusUseCase: GQLGetShopFavoriteStatusUseCase,
                                            private val userSessionInterface: UserSessionInterface,
                                            private val getShopInfoUseCase: GQLGetShopInfoUseCase,
                                            private val getWhitelistUseCase: GetWhitelistUseCase,
                                            private val getShopReputationUseCase: GetShopReputationUseCase,
                                            private val toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase,
                                            private val getModerateShopUseCase: GetModerateShopUseCase,
                                            private val requestModerateShopUseCase: RequestModerateShopUseCase,
                                            private val stickyLoginUseCase: StickyLoginUseCase,
                                            dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    val isUserSessionActive: Boolean
        get() = userSessionInterface.isLoggedIn

    val shopInfoResp = MutableLiveData<Result<ShopInfo>>()
    val whiteListResp = MutableLiveData<Result<Pair<Boolean, String>>>()
    val shopBadgeResp = MutableLiveData<Pair<Boolean, ShopBadge>>()
    val shopModerateResp = MutableLiveData<Result<ShopModerateRequestData>>()
    val shopFavouriteResp = MutableLiveData<ShopInfo.FavoriteData>()

    fun getShop(shopId: String? = null, shopDomain: String? = null, isRefresh: Boolean = false) {
        val id = shopId?.toIntOrNull() ?: 0
        if (id == 0 && shopDomain == null) return
        launchCatchError(block = {
            coroutineScope {
                launch(Dispatchers.IO) {
                    val shopInfoShopBadgeFeedWhitelist = getShopInfoShopReputationDataFeedWhitelist(id, shopDomain, isRefresh)
                    shopInfoShopBadgeFeedWhitelist.feedWhitelist?.let {
                        if (TextUtils.isEmpty(it.error)) {
                            whiteListResp.postValue(Success(it.isWhitelist to it.url))
                        } else {
                            whiteListResp.postValue(Fail(RuntimeException()))
                        }
                    }
                    shopInfoShopBadgeFeedWhitelist.shopInfo?.let {
                        shopInfoResp.postValue(Success(it))
                    }
                    shopInfoShopBadgeFeedWhitelist.shopBadge?.let {
                        shopBadgeResp.postValue((shopInfoShopBadgeFeedWhitelist.shopInfo?.goldOS?.isOfficial != 1) to it)
                    }
                }
                launch(Dispatchers.IO) {
                    val shopFavourite = getShopFavoriteStatus(shopId, shopDomain)
                    shopFavouriteResp.postValue(shopFavourite)
                }
            }
        }) {
            shopInfoResp.value = Fail(it)
        }
    }

    private suspend fun getShopInfoShopReputationDataFeedWhitelist(shopId: Int, shopDomain: String?, isRefresh: Boolean): ShopInfoShopBadgeFeedWhitelist {
        val shopInfoShopBadgeFeedWhitelist = ShopInfoShopBadgeFeedWhitelist()

        getShopInfoUseCase.params = GQLGetShopInfoUseCase
                .createParams(if (shopId == 0) listOf() else listOf(shopId), shopDomain)
        val shopInfoRequest = getShopInfoUseCase.request

        getShopReputationUseCase.params = GetShopReputationUseCase.createParams(shopId)
        val shopReputationRequest = getShopReputationUseCase.request

        val feedWhitelistRequest = getWhitelistUseCase.getRequest(GetWhitelistUseCase.createRequestParams(
                GetWhitelistUseCase.WHITELIST_SHOP,
                shopId.toString()
        ))
        val requests = mutableListOf(shopInfoRequest, shopReputationRequest, feedWhitelistRequest)
        val cacheStrategy = GraphqlCacheStrategy.Builder(if (isRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST).build()
        val gqlResponse = gqlRepository.getReseponse(requests, cacheStrategy)
        val shopInfoError = gqlResponse.getError(ShopInfo.Response::class.java)
        val shopInfo = gqlResponse.getData<ShopInfo.Response>(ShopInfo.Response::class.java).result.data.firstOrNull()
        if (shopInfoError == null || shopInfoError.isEmpty()) {
            shopInfo?.let {
                shopInfoShopBadgeFeedWhitelist.shopInfo = it
            }
        } else {
            throw MessageErrorException(shopInfoError.mapNotNull { it.message }.joinToString(separator = ", "))
        }
        val shopBadgeError = gqlResponse.getError(ShopBadge.Response::class.java)
        val shopBadge = gqlResponse.getData<ShopBadge.Response>(ShopBadge.Response::class.java).result.firstOrNull()
        if (shopBadgeError == null || shopBadgeError.isEmpty()) {
            shopBadge?.let {
                shopInfoShopBadgeFeedWhitelist.shopBadge = it
            }
        } else {
            throw MessageErrorException(shopBadgeError.mapNotNull { it.message }.joinToString(separator = ", "))
        }
        val feedWhitelistError = gqlResponse.getError(WhitelistQuery::class.java)
        val feedWhitelist = gqlResponse.getData<WhitelistQuery>(WhitelistQuery::class.java)?.whitelist
        if (feedWhitelistError == null || feedWhitelistError.isEmpty()) {
            feedWhitelist?.let{
                shopInfoShopBadgeFeedWhitelist.feedWhitelist = it
            }
        } else {
            throw MessageErrorException(feedWhitelistError.mapNotNull { it.message }.joinToString(separator = ", "))
        }
        return shopInfoShopBadgeFeedWhitelist
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

    fun getModerateShopInfo() {
        getModerateShopUseCase.execute(object : Subscriber<ShopModerateRequestData>() {
            override fun onNext(t: ShopModerateRequestData?) {
                t?.let { shopModerateResp.value = Success(it) }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                e?.let { shopModerateResp.value = Fail(it) }
            }

        })
    }

    fun moderateShopRequest(shopId: Int, moderateNotes: String, onSuccess: () -> Unit, onError: (Throwable?) -> Unit) {
        requestModerateShopUseCase.execute(
                RequestModerateShopUseCase.createRequestParams(shopId, moderateNotes),
                object : Subscriber<Boolean>() {
                    override fun onNext(t: Boolean?) {
                        if (t == true) {
                            onSuccess()
                        } else {
                            onError(null)
                        }
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {
                        onError(e)
                    }
                })
    }

    fun getStickyLoginContent(onSuccess: (StickyLoginTickerPojo.TickerDetail) -> Unit, onError: ((Throwable) -> Unit)?) {
        stickyLoginUseCase.setParams(StickyLoginConstant.Page.SHOP)
        stickyLoginUseCase.execute(
            onSuccess = {
                if (it.response.tickers.isNotEmpty()) {
                    for(tickerDetail in it.response.tickers) {
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

    override fun clear() {
        super.clear()
        getWhitelistUseCase.unsubscribe()
        toggleFavouriteShopUseCase.unsubscribe()
        getModerateShopUseCase.unsubscribe()
        requestModerateShopUseCase.unsubscribe()
        stickyLoginUseCase.cancelJobs()
    }

    companion object {
        private const val DATA_NOT_FOUND = "Data not found"
    }
}