package com.tokopedia.shop.campaign.view.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import com.tokopedia.shop.campaign.domain.entity.ShopCampaignRedeemPromoVoucherResult
import com.tokopedia.shop.campaign.domain.usecase.GetPromoVoucherListUseCase
import com.tokopedia.shop.campaign.domain.usecase.RedeemPromoVoucherUseCase
import com.tokopedia.shop.campaign.util.mapper.ShopPageCampaignMapper
import com.tokopedia.shop.common.data.mapper.ShopPageWidgetMapper
import com.tokopedia.shop.common.data.model.*
import com.tokopedia.shop.common.domain.interactor.GqlShopPageGetDynamicTabUseCase
import com.tokopedia.shop.common.util.ShopAsyncErrorException
import com.tokopedia.shop.common.util.ShopUtil.setElement
import com.tokopedia.shop.home.data.model.ShopLayoutWidgetParamsModel
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutV2UseCase
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.shop.pageheader.util.ShopPageHeaderTabName
import com.tokopedia.shop_widget.common.util.WidgetState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopCampaignViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getShopPageHomeLayoutV2UseCase: GetShopPageHomeLayoutV2UseCase,
    private val getPromoVoucherListUseCase: GetPromoVoucherListUseCase,
    private val redeemPromoVoucherUseCase: RedeemPromoVoucherUseCase,
    private val getShopDynamicTabUseCase: GqlShopPageGetDynamicTabUseCase
) : BaseViewModel(dispatcherProvider.main) {

    val shopCampaignWidgetContentData: Flow<Result<Map<Pair<String, String>, Visitable<*>?>>>
        get() = _shopCampaignWidgetContentData
    private val _shopCampaignWidgetContentData =
        MutableSharedFlow<Result<Map<Pair<String, String>, Visitable<*>?>>>()

    val shopCampaignHomeWidgetContentDataError: Flow<List<ShopPageWidgetUiModel>>
        get() = _shopCampaignWidgetContentDataError
    private val _shopCampaignWidgetContentDataError =
        MutableSharedFlow<List<ShopPageWidgetUiModel>>()

    private val _redeemResult = MutableLiveData<Result<ShopCampaignRedeemPromoVoucherResult>>()
    val redeemResult: LiveData<Result<ShopCampaignRedeemPromoVoucherResult>>
        get() = _redeemResult

    private val _voucherSliderWidgetData = MutableLiveData<Result<ShopWidgetVoucherSliderUiModel>>()
    val voucherSliderWidgetData: LiveData<Result<ShopWidgetVoucherSliderUiModel>>
        get() = _voucherSliderWidgetData

    private val _campaignWidgetListVisitable = MutableLiveData<Result<List<Visitable<*>>>>()
    val campaignWidgetListVisitable: LiveData<Result<List<Visitable<*>>>>
        get() = _campaignWidgetListVisitable

    val latestShopCampaignWidgetLayoutData: LiveData<Result<ShopPageLayoutUiModel>>
        get() = _latestShopCampaignWidgetLayoutData
    private val _latestShopCampaignWidgetLayoutData = MutableLiveData<Result<ShopPageLayoutUiModel>>()

    private val _updatedBannerTimerUiModelData = MutableLiveData<ShopWidgetDisplayBannerTimerUiModel?>()
    val updatedBannerTimerUiModelData: LiveData<ShopWidgetDisplayBannerTimerUiModel?>
        get() = _updatedBannerTimerUiModelData

    private var isLoadingRedeemVoucher = false

    fun getWidgetContentData(
        listWidgetLayout: List<ShopPageWidgetUiModel>,
        shopId: String,
        widgetUserAddressLocalData: LocalCacheModel,
    ) {
        launchCatchError(block = {
            val responseWidgetContent = withContext(dispatcherProvider.io) {
                getShopPageHomeLayoutV2UseCase.params = GetShopPageHomeLayoutV2UseCase.createParams(
                    ShopLayoutWidgetParamsModel(
                        shopId = shopId,
                        districtId = widgetUserAddressLocalData.district_id,
                        cityId = widgetUserAddressLocalData.city_id,
                        latitude = widgetUserAddressLocalData.lat,
                        longitude = widgetUserAddressLocalData.long,
                        listWidgetRequest = ShopPageWidgetMapper.mapToShopPageWidgetRequest(listWidgetLayout)
                    )
                )
                getShopPageHomeLayoutV2UseCase.executeOnBackground()
            }
            val listShopCampaignWidget = ShopPageCampaignMapper.mapToListShopCampaignWidget(
                responseWidgetContent.listWidget,
                shopId,
                listWidgetLayout
            )
            val mapShopCampaignWidgetData =
                mutableMapOf<Pair<String, String>, Visitable<*>?>().apply {
                    listWidgetLayout.onEach {
                        val widgetLayoutId = it.widgetId
                        val matchedWidget =
                            listShopCampaignWidget.filterIsInstance<BaseShopHomeWidgetUiModel>()
                                .firstOrNull { shopCampaignWidget ->
                                    shopCampaignWidget.widgetId == widgetLayoutId
                                }
                        if (matchedWidget != null) {
                            put(Pair(it.widgetId, it.widgetMasterId), matchedWidget)
                        } else {
                            put(Pair(it.widgetId, it.widgetMasterId), null)
                        }
                    }
                }
            _shopCampaignWidgetContentData.emit(Success(mapShopCampaignWidgetData))
        }) {
            _shopCampaignWidgetContentDataError.emit(listWidgetLayout)
            _shopCampaignWidgetContentData.emit(
                Fail(
                    ShopAsyncErrorException(
                        ShopAsyncErrorException.AsyncQueryType.SHOP_PAGE_GET_LAYOUT_V2,
                        it
                    )
                )
            )
        }
    }

    fun getVoucherSliderData(uiModel: ShopWidgetVoucherSliderUiModel) {
        launchCatchError(
            dispatcherProvider.io,
            block = {
                val listVoucherPromo = getPromoVoucherData(uiModel)
                val updatedWidgetVoucherSliderUiModel = uiModel.copy(
                    listVoucher = listVoucherPromo
                )
                _voucherSliderWidgetData.postValue(Success(updatedWidgetVoucherSliderUiModel))
            },
            onError = { throwable ->
                _voucherSliderWidgetData.postValue(Fail(throwable))
            }
        )
    }

    private suspend fun getPromoVoucherData(uiModel: ShopWidgetVoucherSliderUiModel): List<ExclusiveLaunchVoucher> {
        return getPromoVoucherListUseCase.execute(uiModel.getListCategorySlug())
    }

    fun redeemCampaignVoucherSlider(
        parentVoucherUiModel: ShopWidgetVoucherSliderUiModel,
        voucherModel: ExclusiveLaunchVoucher
    ) {
        launchCatchError(
            dispatcherProvider.io,
            block = {
                isLoadingRedeemVoucher = true
                val redeemResult = doRedeemPromoVoucher(voucherModel)
                isLoadingRedeemVoucher = false
                _redeemResult.postValue(Success(
                    ShopPageCampaignMapper.mapToShopCampaignRedeemPromoVoucherResult(
                        voucherModel.slug,
                        voucherModel.couponCode,
                        "",
                        parentVoucherUiModel.widgetId,
                        redeemResult
                    )
                ))
            },
            onError = { throwable ->
                isLoadingRedeemVoucher = false
                _redeemResult.postValue(Fail(throwable))
            }
        )
    }

    private suspend fun doRedeemPromoVoucher(model: ExclusiveLaunchVoucher): RedeemPromoVoucherResult {
        val param = RedeemPromoVoucherUseCase.Param(model.id, 0)
        return redeemPromoVoucherUseCase.execute(param)
    }

    fun updateVoucherSliderWidgetData(
        newList: MutableList<Visitable<*>>,
        voucherSliderUiModel: ShopWidgetVoucherSliderUiModel
    ) {
        launchCatchError(
            dispatcherProvider.io,
            block = {
                newList.indexOfFirst { it is ShopWidgetVoucherSliderUiModel }.let { index ->
                    if (index >= Int.ZERO) {
                        if ((voucherSliderUiModel.listVoucher.isEmpty() || voucherSliderUiModel.isError)) {
                            newList.removeAt(index)
                        } else {
                            voucherSliderUiModel.widgetState = WidgetState.FINISH
                            voucherSliderUiModel.isNewData = true
                            newList.setElement(index, voucherSliderUiModel)
                        }
                    }
                }
                _campaignWidgetListVisitable.postValue(Success(newList))
            },
            onError = { throwable ->
                _campaignWidgetListVisitable.postValue(Fail(throwable))
            }
        )

    }

    fun getLatestShopCampaignWidgetLayoutData(
        shopId: String,
        extParam: String,
        locData: LocalCacheModel,
        tabName: String
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            val shopHomeWidgetData = getShopDynamicHomeTabWidgetData(
                shopId,
                extParam,
                locData,
                tabName
            )
            _latestShopCampaignWidgetLayoutData.postValue(Success(shopHomeWidgetData))
        }) {
            _latestShopCampaignWidgetLayoutData.postValue(Fail(it))
        }
    }

    // need to surpress it.name, since name is not related to PII
    @SuppressLint("PII Data Exposure")
    private suspend fun getShopDynamicHomeTabWidgetData(
        shopId: String,
        extParam: String,
        locData: LocalCacheModel,
        tabName: String
    ): ShopPageLayoutUiModel {
        getShopDynamicTabUseCase.isFromCacheFirst = false
        getShopDynamicTabUseCase.setRequestParams(
            GqlShopPageGetDynamicTabUseCase.createParams(
                shopId.toIntOrZero(),
                extParam,
                locData.district_id,
                locData.city_id,
                locData.lat,
                locData.long,
                tabName
            ).parameters
        )
        val layoutData = getShopDynamicTabUseCase.executeOnBackground().shopPageGetDynamicTab.tabData.firstOrNull {
            it.name == ShopPageHeaderTabName.CAMPAIGN
        } ?: ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData()
        return ShopPageHomeMapper.mapToShopHomeWidgetLayoutData(layoutData.data.homeLayoutData)
    }

    fun showBannerTimerRemindMeLoading(newList: MutableList<Visitable<*>>) {
        launchCatchError(dispatcherProvider.io, block = {
            newList.filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>().onEach { bannerTimerUiModel ->
                bannerTimerUiModel.let {
                    it.data?.showRemindMeLoading = true
                    it.isNewData = true
                }
            }
            _campaignWidgetListVisitable.postValue(Success(newList))
        }) {throwable ->
            _campaignWidgetListVisitable.postValue(Fail(throwable))
        }
    }

    fun toggleBannerTimerRemindMe(
        newList: MutableList<Visitable<*>>,
        isRemindMe: Boolean,
        isClickRemindMe: Boolean
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            newList.filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>()
                .onEach { bannerTimerUiModel ->
                    bannerTimerUiModel.data?.let {
                        it.isRemindMe = isRemindMe
                        if (isClickRemindMe) {
                            if (isRemindMe)
                                ++it.totalNotify
                            else
                                --it.totalNotify
                        }
                        it.showRemindMeLoading = false
                        bannerTimerUiModel.isNewData = true
                    }
                }
            _updatedBannerTimerUiModelData.postValue(newList.filterIsInstance<ShopWidgetDisplayBannerTimerUiModel>().firstOrNull())
            _campaignWidgetListVisitable.postValue(Success(newList))
        }) { throwable ->
            _campaignWidgetListVisitable.postValue(Fail(throwable))
        }
    }


    fun updateBannerTimerWidgetUiModel(
        newList: MutableList<Visitable<*>>,
        bannerTimerUiModel: ShopWidgetDisplayBannerTimerUiModel
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            val position = newList.indexOfFirst{ it is ShopWidgetDisplayBannerTimerUiModel }
            if(position != -1){
                newList.setElement(position, bannerTimerUiModel.copy().apply {
                    isNewData = true
                })
            }
            _campaignWidgetListVisitable.postValue(Success(newList))
        }) { throwable ->
            _campaignWidgetListVisitable.postValue(Fail(throwable))
        }
    }

    fun isLoadingRedeemVoucher(): Boolean {
        return isLoadingRedeemVoucher
    }
}
