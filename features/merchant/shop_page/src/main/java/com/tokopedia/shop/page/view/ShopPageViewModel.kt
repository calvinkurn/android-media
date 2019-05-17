package com.tokopedia.shop.page.view

import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.UserNotLoginException
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kolcommon.data.pojo.Whitelist
import com.tokopedia.kolcommon.data.pojo.WhitelistQuery
import com.tokopedia.kolcommon.domain.usecase.GetWhitelistUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.domain.interactor.model.favoriteshop.DataFollowShop
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.shop.page.domain.interactor.GetModerateShopUseCase
import com.tokopedia.shop.page.domain.interactor.RequestModerateShopUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject

class ShopPageViewModel @Inject constructor(private val userSessionInterface: UserSessionInterface,
                                            private val getShopInfoUseCase: GQLGetShopInfoUseCase,
                                            private val getWhitelistUseCase: GetWhitelistUseCase,
                                            private val getShopReputationUseCase: GetShopReputationUseCase,
                                            private val toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase,
                                            private val getModerateShopUseCase: GetModerateShopUseCase,
                                            private val requestModerateShopUseCase: RequestModerateShopUseCase,
                                            dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    val isUserSessionActive: Boolean
        get() = userSessionInterface.isLoggedIn

    val shopInfoResp = MutableLiveData<Result<ShopInfo>>()
    val whiteListResp =  MutableLiveData<Pair<Boolean, String>>()
    val shopBadgeResp = MutableLiveData<Pair<Boolean, ShopBadge>>()
    val shopModerateResp = MutableLiveData<Result<ShopModerateRequestData>>()

    fun getShop(shopId: String? = null, shopDomain: String? = null, isRefresh: Boolean = false){
        val id = shopId?.toIntOrNull() ?: 0
        if (id == 0 && shopDomain == null) return
        launchCatchError(block = {
            getShopInfoUseCase.params = GQLGetShopInfoUseCase
                    .createParams(if (id == 0)listOf() else listOf(id), shopDomain)
            getShopInfoUseCase.isFromCacheFirst = !isRefresh
            val shopInfo = withContext(Dispatchers.IO){getShopInfoUseCase.executeOnBackground()}
            shopInfoResp.value = Success(shopInfo)
            withContext(Dispatchers.IO){getShopReputation(shopInfo.shopCore.shopID, isRefresh)}?.let {
                badge -> shopBadgeResp.value = (shopInfo.goldOS.isOfficial != 1) to badge
            }
        }){
            shopInfoResp.value = Fail(it)
        }
    }

    fun getFeedWhiteList(shopId: String){
        getWhitelistUseCase.execute(
                GetWhitelistUseCase.createRequestParams(GetWhitelistUseCase.WHITELIST_SHOP, shopId),
                object : Subscriber<GraphqlResponse>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) {}

                    override fun onNext(graphqlResponse: GraphqlResponse?) {

                        graphqlResponse?.let {
                            val error = it.getError(WhitelistQuery::class.java)
                            if (error == null || error.isEmpty()){
                                val whitelist = it.getData<WhitelistQuery>(WhitelistQuery::class.java)?.whitelist ?: return
                                if (TextUtils.isEmpty(whitelist.error))
                                    whiteListResp.value = whitelist.isWhitelist to whitelist.url
                            }
                        }
                    }
                }
        )
    }

    fun toggleFavorite(shopID: String, onSuccess: (Boolean) -> Unit, onError: (Throwable) -> Unit) {
        if (!userSessionInterface.isLoggedIn) {
            onError(UserNotLoginException())
            return
        }

        toggleFavouriteShopUseCase.execute(ToggleFavouriteShopUseCase.createRequestParam(shopID),
                object : Subscriber<Boolean>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) { onError(e) }

                    override fun onNext(success: Boolean) { onSuccess(success) }
        })
    }

    private suspend fun getShopReputation(shopId: String, isRefresh: Boolean): ShopBadge?{
        getShopReputationUseCase.params = GetShopReputationUseCase.createParams(shopId.toInt())
        getShopReputationUseCase.isFromCacheFirst = !isRefresh
        return try {
             getShopReputationUseCase.executeOnBackground()
        } catch (t: Throwable){
            null
        }
    }

    fun getModerateShopInfo(){
        getModerateShopUseCase.execute(object : Subscriber<ShopModerateRequestData>() {
            override fun onNext(t: ShopModerateRequestData?) {
                t?.let { shopModerateResp.value = Success(it) }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) { e?.let { shopModerateResp.value = Fail(it) }}

        })
    }

    fun moderateShopRequest(shopId: Int, moderateNotes:String, onSuccess: ()-> Unit, onError: (Throwable?) -> Unit){
        requestModerateShopUseCase.execute(
                RequestModerateShopUseCase.createRequestParams(shopId,moderateNotes),
                object : Subscriber<Boolean>() {
                    override fun onNext(t: Boolean?) {
                        if (t == true) {
                            onSuccess()
                        } else {
                            onError(null)
                        }
                    }

                    override fun onCompleted() {}

                    override fun onError(e: Throwable?) { onError(e)}
                })
    }

    override fun clear() {
        super.clear()
        getWhitelistUseCase.unsubscribe()
        toggleFavouriteShopUseCase.unsubscribe()
        getModerateShopUseCase.unsubscribe()
        requestModerateShopUseCase.unsubscribe()
    }
}