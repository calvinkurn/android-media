package com.tokopedia.shop.campaign.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.campaign.util.mapper.ShopPageCampaignMapper
import com.tokopedia.shop.common.data.model.*
import com.tokopedia.shop.common.util.ShopAsyncErrorException
import com.tokopedia.shop.home.data.model.ShopLayoutWidgetParamsModel
import com.tokopedia.shop.home.data.model.ShopPageWidgetRequestModel
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutV2UseCase
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

class ShopCampaignViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getShopPageHomeLayoutV2UseCase: Provider<GetShopPageHomeLayoutV2UseCase>,
) : BaseViewModel(dispatcherProvider.main) {

    val shopCampaignWidgetContentData: Flow<Result<Map<Pair<String, String>, Visitable<*>?>>>
        get() = _shopCampaignWidgetContentData
    private val _shopCampaignWidgetContentData = MutableSharedFlow<Result<Map<Pair<String, String>, Visitable<*>?>>>()

    val shopCampaignHomeWidgetContentDataError: Flow<List<ShopPageWidgetLayoutUiModel>>
        get() = _shopCampaignWidgetContentDataError
    private val _shopCampaignWidgetContentDataError = MutableSharedFlow<List<ShopPageWidgetLayoutUiModel>>()

    fun getWidgetContentData(
        listWidgetLayout: List<ShopPageWidgetLayoutUiModel>,
        shopId: String,
        widgetUserAddressLocalData: LocalCacheModel,
    ) {
        launchCatchError(block = {
            val responseWidgetContent = withContext(dispatcherProvider.io) {
                val useCase = getShopPageHomeLayoutV2UseCase.get()
                useCase.params = GetShopPageHomeLayoutV2UseCase.createParams(
                    ShopLayoutWidgetParamsModel(
                        shopId = shopId,
                        districtId = widgetUserAddressLocalData.district_id,
                        cityId = widgetUserAddressLocalData.city_id,
                        latitude = widgetUserAddressLocalData.lat,
                        longitude = widgetUserAddressLocalData.long,
                        listWidgetRequest = listWidgetLayout.map {
                            ShopPageWidgetRequestModel(
                                it.widgetId,
                                it.widgetMasterId,
                                it.widgetType,
                                it.widgetName
                            )
                        }
                    )
                )
                useCase.executeOnBackground()
            }
            val listShopCampaignWidget = ShopPageCampaignMapper.mapToListShopCampaignWidget(
                responseWidgetContent.listWidget,
                shopId,
                listWidgetLayout
            )
            val mapShopCampaignWidgetData = mutableMapOf<Pair<String, String>, Visitable<*>?>().apply {
                listWidgetLayout.onEach {
                    val widgetLayoutId = it.widgetId
                    val matchedWidget = listShopCampaignWidget.firstOrNull { shopCampaignWidget ->
                        when (shopCampaignWidget) {
                            is BaseShopHomeWidgetUiModel -> {
                                shopCampaignWidget.widgetId == widgetLayoutId
                            }
                            else -> {
                                false
                            }
                        }
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
}
