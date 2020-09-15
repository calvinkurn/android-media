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
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.feedcomponent.data.pojo.whitelist.Whitelist
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.shop.common.util.ShopUtil.isHasNextPage
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderContentData
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderP1
import com.tokopedia.shop.pageheader.domain.interactor.*
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageP1HeaderData
import com.tokopedia.shop.pageheader.util.ShopPageHeaderMapper
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.view.datamodel.GetShopProductUiModel
import com.tokopedia.shop.product.view.datamodel.ShopProductViewModel
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
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
        @GqlGetShopInfoUseCaseCoreAndAssetsQualifier
        private val gqlGetShopInfobUseCaseCoreAndAssets: Lazy<GQLGetShopInfoUseCase>,
        private val getShopReputationUseCase: Lazy<GetShopReputationUseCase>,
        private val toggleFavouriteShopUseCase: Lazy<ToggleFavouriteShopUseCase>,
        private val getModerateShopUseCase: Lazy<GetModerateShopUseCase>,
        private val requestModerateShopUseCase: Lazy<RequestModerateShopUseCase>,
        private val stickyLoginUseCase: Lazy<StickyLoginUseCase>,
        private val gqlGetShopOperationalHourStatusUseCase: Lazy<GQLGetShopOperationalHourStatusUseCase>,
        private val getShopPageP1DataUseCase: Lazy<GetShopPageP1DataUseCase>,
        dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    companion object {
        private const val DATA_NOT_FOUND = "Data not found"
    }

    fun isMyShop(shopId: String) = userSessionInterface.shopId == shopId

    val isUserSessionActive: Boolean
        get() = userSessionInterface.isLoggedIn

    val ownerShopName: String
        get() = userSessionInterface.shopName

    val shopPageP1Data = MutableLiveData<Result<ShopPageP1HeaderData>>()
    val shopIdFromDomainData = MutableLiveData<Result<String>>()
    val shopPageHeaderContentData = MutableLiveData<Result<ShopPageHeaderContentData>>()
    var productListData: GetShopProductUiModel = GetShopProductUiModel()
    val shopImagePath = MutableLiveData<String>()

    fun getShopPageTabData(
            shopId: Int,
            shopDomain: String,
            page: Int,
            itemPerPage: Int,
            sortId: Int,
            keyword: String,
            etalaseId: String,
            isRefresh: Boolean
    ) {
        if (shopId == 0 && shopDomain.isEmpty()) return
        launchCatchError(block = {
            val shopP1DataAsync = asyncCatchError(
                    Dispatchers.IO,
                    block = {
                        getShopP1Data(
                                shopId,
                                shopDomain,
                                page,
                                itemPerPage,
                                sortId,
                                keyword,
                                etalaseId,
                                isRefresh
                        )
                    },
                    onError = {
                        shopPageP1Data.postValue(Fail(it))
                        null
                    })
            shopP1DataAsync.await()?.let { shopPageHeaderP1Data ->
                productListData = GetShopProductUiModel(
                        isHasNextPage(page, itemPerPage, shopPageHeaderP1Data.productList.totalData),
                        shopPageHeaderP1Data.productList.data.map {
                            ShopPageProductListMapper.mapShopProductToProductViewModel(it, isMyShop(shopId.toString()), etalaseId)
                        },
                        shopPageHeaderP1Data.productList.totalData
                )
                shopPageP1Data.postValue(Success(ShopPageHeaderMapper.mapToShopPageP1HeaderData(
                        shopPageHeaderP1Data.isShopOfficialStore,
                        shopPageHeaderP1Data.isShopPowerMerchant,
                        shopPageHeaderP1Data.shopInfoTopContentData,
                        shopPageHeaderP1Data.shopInfoHomeTypeData,
                        shopPageHeaderP1Data.shopInfoCoreAndAssetsData,
                        shopPageHeaderP1Data.feedWhitelist
                )))
            }
        }) {
            shopPageP1Data.postValue(Fail(it))
        }
    }

    private suspend fun getShopP1Data(
            shopId: Int,
            shopDomain: String,
            page: Int,
            itemPerPage: Int,
            sortId: Int,
            keyword: String,
            etalaseId: String,
            isRefresh: Boolean
    ): ShopPageHeaderP1 {
        val useCase = getShopPageP1DataUseCase.get()
        useCase.isFromCacheFirst = !isRefresh
        useCase.params = GetShopPageP1DataUseCase.createParams(
                shopId,
                shopDomain,
                page,
                itemPerPage,
                sortId,
                keyword,
                etalaseId
        )
        return useCase.executeOnBackground()
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

    private inline fun <reified T> getResponseData(response: GraphqlResponse): T {
        val error = response.getError(T::class.java)
        if (error == null || error.isEmpty()) {
            return response.getData<T>(T::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    private suspend fun getShopOperationalHourStatus(shopId: Int): ShopOperationalHourStatus {
        gqlGetShopOperationalHourStatusUseCase.get().params = GQLGetShopOperationalHourStatusUseCase.createParams(shopId.toString())
        return gqlGetShopOperationalHourStatusUseCase.get().executeOnBackground()
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