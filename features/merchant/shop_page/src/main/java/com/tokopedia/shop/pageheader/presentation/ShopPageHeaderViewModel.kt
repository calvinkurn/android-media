package com.tokopedia.shop.pageheader.presentation

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliatePageDetail
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkProductInfo
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateAtcSource
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.decodeToUtf8
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.network.exception.UserNotLoginException
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPageConstant.SHARED_PREF_AFFILIATE_CHANNEL
import com.tokopedia.shop.common.data.model.HomeLayoutData
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTracker
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTrackerInput
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShopResponse
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatusResponse
import com.tokopedia.shop.common.di.GqlGetShopInfoForHeaderUseCaseQualifier
import com.tokopedia.shop.common.di.GqlGetShopInfoUseCaseCoreAndAssetsQualifier
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_ALLOW_MANAGE
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_ASSETS
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.FIELD_BRANCH_LINK
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
import com.tokopedia.shop.common.domain.interactor.GetFollowStatusUseCase
import com.tokopedia.shop.common.domain.interactor.GetFollowStatusUseCase.Companion.SOURCE_SHOP_PAGE
import com.tokopedia.shop.common.domain.interactor.ShopQuestGeneralTrackerUseCase
import com.tokopedia.shop.common.domain.interactor.UpdateFollowStatusUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.util.ShopAsyncErrorException
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.pageheader.data.model.NewShopPageHeaderP1
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import com.tokopedia.shop.pageheader.data.model.ShopRequestUnmoderateSuccessResponse
import com.tokopedia.shop.pageheader.domain.interactor.GetShopPageHeaderLayoutUseCase
import com.tokopedia.shop.pageheader.domain.interactor.GetShopPageP1DataUseCase
import com.tokopedia.shop.pageheader.domain.interactor.ShopModerateRequestStatusUseCase
import com.tokopedia.shop.pageheader.domain.interactor.ShopRequestUnmoderateUseCase
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderP1HeaderData
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderTickerData
import com.tokopedia.shop.pageheader.util.ShopPageHeaderMapper
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageProcessingUtil
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
class ShopPageHeaderViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    @GqlGetShopInfoForHeaderUseCaseQualifier
    private val gqlGetShopInfoForHeaderUseCase: Lazy<GQLGetShopInfoUseCase>,
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
    private val gqlGetShopOperationalHourStatusUseCase: Lazy<GQLGetShopOperationalHourStatusUseCase>,
    private val affiliateEligibilityCheckUseCase: Lazy<AffiliateEligibilityCheckUseCase>,
    private val sharedPreferences: SharedPreferences,
    private val dispatcherProvider: CoroutineDispatchers
) :
    BaseViewModel(dispatcherProvider.main) {

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
    val shopShareTracker: LiveData<Result<ShopQuestGeneralTracker>>
        get() = _shopShareTracker

    val shopPageP1Data = MutableLiveData<Result<ShopPageHeaderP1HeaderData>>()
    val shopIdFromDomainData = MutableLiveData<Result<String>>()
    var homeWidgetLayoutData: HomeLayoutData = HomeLayoutData()
    val shopImagePath = MutableLiveData<String>()

    private val _shopUnmoderateData =
        MutableLiveData<Result<ShopRequestUnmoderateSuccessResponse>>()
    val shopUnmoderateData: LiveData<Result<ShopRequestUnmoderateSuccessResponse>>
        get() = _shopUnmoderateData

    private val _shopModerateRequestStatus = MutableLiveData<Result<ShopModerateRequestData>>()
    val shopModerateRequestStatus: LiveData<Result<ShopModerateRequestData>>
        get() = _shopModerateRequestStatus

    private val _followStatusData = MutableLiveData<Result<FollowStatusResponse>>()
    val followStatusData: LiveData<Result<FollowStatusResponse>>
        get() = _followStatusData

    private val _followShopData = MutableLiveData<Result<FollowShopResponse>>()
    val followShopData: LiveData<Result<FollowShopResponse>>
        get() = _followShopData

    private val _shopPageTickerData = MutableLiveData<Result<ShopPageHeaderTickerData>>()
    val shopPageHeaderTickerData: LiveData<Result<ShopPageHeaderTickerData>>
        get() = _shopPageTickerData

    private val _shopPageShopShareData = MutableLiveData<Result<ShopInfo>>()
    val shopPageShopShareData: LiveData<Result<ShopInfo>>
        get() = _shopPageShopShareData

    private val _resultAffiliate = MutableLiveData<Result<GenerateAffiliateLinkEligibility>>()
    val resultAffiliate: LiveData<Result<GenerateAffiliateLinkEligibility>>
        get() = _resultAffiliate

    private val _productListData = MutableLiveData<Result<ShopProduct.GetShopProduct>>()
    val productListData: LiveData<Result<ShopProduct.GetShopProduct>>
        get() = _productListData

    /*
    Function getNewShopPageTabData is expected to perform faster than
    older version due to:
    1. Remove the usages of getIsShopOfficialRequest, getIsShopPowerMerchantRequest & getShopInfoTopContentDataRequest
    2. We only use ShopInfo data to identify whether the Shop is RM, PM, or OS
     */
    fun getNewShopPageTabData(
        shopId: String,
        shopDomain: String,
        isRefresh: Boolean,
        widgetUserAddressLocalData: LocalCacheModel,
        extParam: String,
        tabName: String,
        connectionType: String,
        shopPageColorSchemaDefaultConfigColor: Map<ShopPageColorSchema.ColorSchemaName, String> = mapOf(),
        isEnableShopReimagined: Boolean
    ) {
        launchCatchError(block = {
            val shopP1DataAsync = asyncCatchError(
                dispatcherProvider.io,
                block = {
                    getNewShopP1Data(
                        shopId = shopId,
                        shopDomain = shopDomain,
                        isRefresh = isRefresh,
                        extParam = extParam,
                        widgetUserAddressLocalData = widgetUserAddressLocalData,
                        tabName = tabName,
                        connectionType = connectionType
                    )
                },
                onError = {
                    shopPageP1Data.postValue(
                        Fail(
                            ShopAsyncErrorException(
                                ShopAsyncErrorException.AsyncQueryType.SHOP_PAGE_P1,
                                it
                            )
                        )
                    )
                    null
                }
            )

            val shopHeaderWidgetDataAsync = asyncCatchError(
                dispatcherProvider.io,
                block = {
                    getShopPageHeaderData(
                        shopId = shopId,
                        isRefresh = isRefresh,
                        widgetUserAddressLocalData = widgetUserAddressLocalData
                    )
                },
                onError = {
                    shopPageP1Data.postValue(
                        Fail(
                            ShopAsyncErrorException(
                                ShopAsyncErrorException.AsyncQueryType.SHOP_HEADER_WIDGET,
                                it
                            )
                        )
                    )
                    null
                }
            )

            shopP1DataAsync.await()?.let { shopPageHeaderP1Data ->
                shopHeaderWidgetDataAsync.await()?.let { shopPageHeaderWidgetData ->
                    shopPageP1Data.postValue(
                        Success(
                            ShopPageHeaderMapper.mapToNewShopPageP1HeaderData(
                                shopInfoCoreData = shopPageHeaderP1Data.shopInfoCoreAndAssetsData,
                                shopPageGetDynamicTabResponse = shopPageHeaderP1Data.shopPageGetDynamicTabResponse,
                                shopPageHeaderLayoutData = shopPageHeaderWidgetData,
                                shopPageColorSchemaDefaultConfigColor = shopPageColorSchemaDefaultConfigColor,
                                isEnableShopReimagined = isEnableShopReimagined
                            )
                        )
                    )
                }
            }
        }) { exception ->
            shopPageP1Data.postValue(Fail(exception))
        }
    }

    private suspend fun getShopPageHeaderData(
        shopId: String,
        isRefresh: Boolean,
        widgetUserAddressLocalData: LocalCacheModel
    ): ShopPageHeaderLayoutResponse {
        val useCase = getShopPageHeaderLayoutUseCase.get()
        val districtId: String
        val cityId: String
        if (!isMyShop(shopId)) {
            districtId = widgetUserAddressLocalData.district_id
            cityId = widgetUserAddressLocalData.city_id
        } else {
            districtId = ""
            cityId = ""
        }
        useCase.params = GetShopPageHeaderLayoutUseCase.createParams(shopId, districtId, cityId)
        useCase.isFromCloud = isRefresh
        return useCase.executeOnBackground()
    }

    fun getShopShareProductListData(
        shopId: String,
        page: Int,
        itemPerPage: Int,
        shopProductFilterParameter: ShopProductFilterParameter,
        keyword: String,
        etalaseId: String,
        widgetUserAddressLocalData: LocalCacheModel
    ) {
        launchCatchError(dispatcherProvider.io, block = {
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
                    fcategory = shopProductFilterParameter.getCategory()
                    userDistrictId = widgetUserAddressLocalData.district_id
                    userCityId = widgetUserAddressLocalData.city_id
                    userLat = widgetUserAddressLocalData.lat
                    userLong = widgetUserAddressLocalData.long
                }
            )
            _productListData.postValue(Success(useCase.executeOnBackground()))
        }, onError = {
            _productListData.postValue(Fail(it))
        })
    }

    private suspend fun getNewShopP1Data(
        shopId: String,
        shopDomain: String,
        isRefresh: Boolean,
        extParam: String,
        widgetUserAddressLocalData: LocalCacheModel,
        tabName: String,
        connectionType: String
    ): NewShopPageHeaderP1 {
        val useCase = getShopPageP1DataUseCase.get()
        useCase.isFromCacheFirst = !isRefresh
        useCase.params = GetShopPageP1DataUseCase.createParams(
            shopId = shopId,
            shopDomain = shopDomain,
            extParam = extParam,
            widgetUserAddressLocalData = widgetUserAddressLocalData,
            tabName = tabName,
            connectionType = connectionType
        )
        return useCase.executeOnBackground()
    }

    fun checkShopRequestModerateStatus() {
        launchCatchError(dispatcherProvider.io, {
            val shopModerateRequestStatusUseCase = shopModerateRequestStatusUseCase.get()
            val shopModerateRequestStatusResponse =
                shopModerateRequestStatusUseCase.executeOnBackground()
            _shopModerateRequestStatus.postValue(Success(shopModerateRequestStatusResponse))
        }) {
            _shopModerateRequestStatus.postValue(Fail(it))
        }
    }

    fun sendRequestUnmoderateShop(shopId: Double, optionValue: String) {
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
            context?.let {
                ShopUtil.loadImageWithEmptyTarget(
                    it,
                    shopSnippetUrl,
                    {
                        fitCenter()
                    },
                    MediaBitmapEmptyTarget(
                        onReady = { bitmap ->
                            val savedFile = ImageProcessingUtil.writeImageToTkpdPath(
                                bitmap,
                                Bitmap.CompressFormat.PNG
                            )

                            if (savedFile != null) {
                                shopImagePath.postValue(savedFile.absolutePath)
                            }
                        }
                    )
                )
            }
        }, onError = {
                it.printStackTrace()
            })
    }

    fun sendShopShareTracker(shopId: String, channel: String) {
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

    fun getFollowStatusData(shopId: String) {
        launchCatchError(dispatcherProvider.io, block = {
            val pageSource = SOURCE_SHOP_PAGE
            getFollowStatusUseCase.get().params =
                GetFollowStatusUseCase.createParams(shopId, pageSource)
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
            updateFollowStatusUseCase.get().params =
                UpdateFollowStatusUseCase.createParams(shopId, action)
            _followShopData.postValue(
                Success(
                    updateFollowStatusUseCase.get().executeOnBackground()
                )
            )
        }, onError = {
                _followShopData.postValue(Fail(it))
            })
    }

    fun getShopShareAndOperationalHourStatusData(
        shopId: String,
        shopDomain: String,
        isRefresh: Boolean
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            val shopInfoData = asyncCatchError(
                dispatcherProvider.io,
                block = {
                    getShopInfoHeader(
                        shopId.toIntOrZero(),
                        shopDomain,
                        isRefresh
                    )
                },
                onError = {
                    null
                }
            )
            val shopOperationalHourStatusData = asyncCatchError(
                dispatcherProvider.io,
                block = {
                    getShopOperationalHourStatus(shopId.toIntOrZero())
                },
                onError = {
                    null
                }
            )
            shopInfoData.await()?.let { shopInfo ->
                _shopPageShopShareData.postValue(Success(shopInfo))
                shopOperationalHourStatusData.await()?.let { shopOperationalHourStatus ->
                    _shopPageTickerData.postValue(
                        Success(
                            ShopPageHeaderTickerData(
                                shopInfo,
                                shopOperationalHourStatus
                            )
                        )
                    )
                }
            }
        }) {}
    }

    private suspend fun getShopInfoHeader(
        shopId: Int,
        shopDomain: String,
        refresh: Boolean
    ): ShopInfo {
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
                FIELD_SHOP_SNIPPET,
                FIELD_BRANCH_LINK
            )
        )
        return gqlGetShopInfoForHeaderUseCase.get().executeOnBackground()
    }

    fun getShopIdFromDomain(shopDomain: String) {
        launchCatchError(block = {
            flow {
                emit(getShopInfoCoreFromDomain(shopDomain))
            }.flowOn(dispatcherProvider.io).collect {
                shopIdFromDomainData.postValue(Success(it.shopCore.shopID))
            }
        }) {
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

    private suspend fun getShopOperationalHourStatus(shopId: Int): ShopOperationalHourStatus {
        val useCase = gqlGetShopOperationalHourStatusUseCase.get()
        useCase.params = GQLGetShopOperationalHourStatusUseCase.createParams(shopId.toString())
        return useCase.executeOnBackground()
    }

    fun shopLandingPageInitAffiliateCookie(
        affiliateCookieHelper: AffiliateCookieHelper,
        affiliateUUId: String,
        affiliateChannel: String,
        shopId: String
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            affiliateCookieHelper.initCookie(
                affiliateUUId.decodeToUtf8(),
                affiliateChannel,
                AffiliatePageDetail(shopId, AffiliateSdkPageSource.Shop(shopId))
            )
        }) {
        }
    }

    fun createAffiliateCookieShopAtcProduct(
        uuId: String,
        affiliateCookieHelper: AffiliateCookieHelper,
        affiliateChannel: String,
        productId: String,
        isVariant: Boolean,
        stockQty: Int,
        shopId: String
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            val affiliateSdkDirectAtcSource = AffiliateSdkPageSource.DirectATC(
                AffiliateAtcSource.SHOP_PAGE,
                shopId,
                AffiliateSdkProductInfo("", isVariant, stockQty)
            )
            affiliateCookieHelper.initCookie(
                uuId,
                affiliateChannel,
                AffiliatePageDetail(productId, affiliateSdkDirectAtcSource)
            )
        }) {
        }
    }

    fun saveAffiliateChannel(affiliateChannel: String) {
        launchCatchError(dispatcherProvider.io, block = {
            sharedPreferences.edit().putString(
                SHARED_PREF_AFFILIATE_CHANNEL,
                affiliateChannel
            ).apply()
        }) {}
    }

    fun checkAffiliate(affiliateInput: AffiliateInput) {
        launch {
            try {
                val result = affiliateEligibilityCheckUseCase.get().apply {
                    params = AffiliateEligibilityCheckUseCase.createParam(affiliateInput)
                }.executeOnBackground()
                _resultAffiliate.value = Success(result)
            } catch (e: Exception) {
                _resultAffiliate.value = Fail(e)
            }
        }
    }
}
