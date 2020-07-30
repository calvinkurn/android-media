package com.tokopedia.shop.setting.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.shop.pageheader.data.model.ShopInfoShopBadgeFeedWhitelist
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import javax.inject.Inject

class ShopPageSettingViewModel @Inject constructor(private val gqlRepository: GraphqlRepository,
                                                   private val userSessionInterface: UserSessionInterface,
                                                   private val getShopInfoUseCase: GQLGetShopInfoUseCase,
                                                   private val getWhitelistUseCase: GetWhitelistUseCase,
                                                   private val getShopReputationUseCase: GetShopReputationUseCase,
                                                   dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    val shopInfoResp = MutableLiveData<Result<ShopInfo>>()

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    fun getShop(shopId: String? = null, shopDomain: String? = null, isRefresh: Boolean = false) {
        val id = shopId?.toIntOrNull() ?: 0
        if (id == 0 && shopDomain == null) return
        launchCatchError(block = {
            val shopInfoShopBadgeFeedWhitelist = withContext(Dispatchers.IO) {
                getShopInfoShopReputationDataFeedWhitelist(id, shopDomain, isRefresh)
            }
            shopInfoShopBadgeFeedWhitelist.shopInfo?.let {
                shopInfoResp.postValue(Success(it))
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
            feedWhitelist?.let {
                shopInfoShopBadgeFeedWhitelist.feedWhitelist = it
            }
        } else {
            throw MessageErrorException(feedWhitelistError.mapNotNull { it.message }.joinToString(separator = ", "))
        }
        return shopInfoShopBadgeFeedWhitelist
    }
}