package com.tokopedia.tokofood.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.localizationchooseaddress.domain.response.ChosenAddressDataResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.logisticCommon.data.response.EligibleForAddressFeature
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureData
import com.tokopedia.logisticCommon.data.response.KeroAddrIsEligibleForAddressFeatureResponse
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_NO_PIN_POINT
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.LOADING_STATE
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.PROGRESS_BAR
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.TICKER_WIDGET_ID
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.domain.data.DynamicHomeChannelResponse
import com.tokopedia.tokofood.feature.home.domain.data.DynamicHomeIcon
import com.tokopedia.tokofood.feature.home.domain.data.DynamicIcon
import com.tokopedia.tokofood.feature.home.domain.data.Header
import com.tokopedia.tokofood.feature.home.domain.data.HomeLayoutResponse
import com.tokopedia.tokofood.feature.home.domain.data.Merchant
import com.tokopedia.tokofood.feature.home.domain.data.TickerItem
import com.tokopedia.tokofood.feature.home.domain.data.Tickers
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodHomeDynamicIconsResponse
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodHomeLayoutResponse
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodHomeTickerResponse
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodHomeUSPResponse
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodMerchantList
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodMerchantListResponse
import com.tokopedia.tokofood.feature.home.domain.data.USP
import com.tokopedia.tokofood.feature.home.domain.data.USPResponse
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeChooseAddressWidgetUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeEmptyStateLocationUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeLoadingStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeTickerUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeUSPUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodMerchantListUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodProgressBarUiModel
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodTickerDetail
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.KeroEditAddress
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.KeroEditAddressData
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response.KeroEditAddressResponse
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData

fun createLoadingState(): TokoFoodListUiModel {
    val mutableList = mutableListOf<Visitable<*>>()
    val loadingStateUiModel = TokoFoodHomeLoadingStateUiModel(id = LOADING_STATE)
    mutableList.add(loadingStateUiModel)
    return TokoFoodListUiModel(
        items = mutableList,
        state = TokoFoodLayoutState.LOADING
    )
}

fun createNoPinPoinState(): TokoFoodListUiModel {
    val mutableList = mutableListOf<Visitable<*>>()
    val chooseAddressModel = TokoFoodHomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
    val noPinPoinUiModel = TokoFoodHomeEmptyStateLocationUiModel(id = EMPTY_STATE_NO_PIN_POINT)
    mutableList.add(chooseAddressModel)
    mutableList.add(noPinPoinUiModel)
    return TokoFoodListUiModel(
        items = mutableList,
        state = TokoFoodLayoutState.HIDE
    )
}

fun createNoAddressState(): TokoFoodListUiModel {
    val mutableList = mutableListOf<Visitable<*>>()
    val chooseAddressModel = TokoFoodHomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
    val noAddressModel = TokoFoodHomeEmptyStateLocationUiModel(id = EMPTY_STATE_NO_ADDRESS)
    mutableList.add(chooseAddressModel)
    mutableList.add(noAddressModel)
    return TokoFoodListUiModel(
        items = mutableList,
        state = TokoFoodLayoutState.HIDE
    )
}

fun createChooseAddress(): GetStateChosenAddressQglResponse {
    return GetStateChosenAddressQglResponse(
        response = GetStateChosenAddressResponse(
            data = ChosenAddressDataResponse(
                districtId = 1,
                cityId = 1,
                addressId = 1
            )
        )
    )
}

fun createLoadMoreState(): TokoFoodListUiModel {
    val mutableList = mutableListOf<Visitable<*>>()
    val progressBarUiModel = TokoFoodProgressBarUiModel(id = PROGRESS_BAR)
    mutableList.add(progressBarUiModel)
    return TokoFoodListUiModel(
        items = mutableList,
        state = TokoFoodLayoutState.UPDATE
    )
}

fun createKeroAddrIsEligibleForAddressFeature(): KeroAddrIsEligibleForAddressFeatureResponse {
    return KeroAddrIsEligibleForAddressFeatureResponse(
        data = KeroAddrIsEligibleForAddressFeatureData (
            eligibleForRevampAna = EligibleForAddressFeature (
                eligible = true
                    )
                )
    )
}

fun createKeroEditAddressResponse(): KeroEditAddressResponse {
    return KeroEditAddressResponse(
        keroEditAddress = KeroEditAddress(
            data = KeroEditAddressData(
                addressId = "123",
                isSuccess = 1
            )
        )
    )
}

fun createTickerStateRemoved(): TokoFoodListUiModel {
    val mutableList = mutableListOf<Visitable<*>>()
    val chooseAddressModel = TokoFoodHomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
    mutableList.add(chooseAddressModel)
    return TokoFoodListUiModel(
        items = mutableList,
        state = TokoFoodLayoutState.UPDATE
    )
}

fun createTickerState(): TokoFoodListUiModel {
    val mutableList = mutableListOf<Visitable<*>>()
    val chooseAddressModel = TokoFoodHomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
    val tickerModel = createHomeTickerDataModel()
    mutableList.add(chooseAddressModel)
    mutableList.add(tickerModel)
    return TokoFoodListUiModel(
        items = mutableList,
        state = TokoFoodLayoutState.UPDATE
    )
}

fun createHomeTickerDataModel(tickers: List<TickerData> = listOf(createTickerData())): TokoFoodHomeTickerUiModel {
    return TokoFoodHomeTickerUiModel(id = TICKER_WIDGET_ID, tickers = tickers, TokoFoodLayoutState.SHOW)
}

fun createTickerData(
    title: String = "TokoFood Title",
    description: String = "TokoFood Description",
    type: Int = Ticker.TYPE_ANNOUNCEMENT
): TickerData {
    return TickerData(title = title, description = description, type = type)
}

fun createTicker(): TokoFoodHomeTickerResponse {
    return  TokoFoodHomeTickerResponse(
        ticker = Tickers(
            tickerList = listOf(
                TickerItem(
                    id = "10",
                    title = "TokoFood Title",
                    message = "TokoFood Description",
                    color = "#FFF",
                    layout = "default"
                )

            )
        )
    )
}

fun createHomeLayoutList(): TokoFoodHomeLayoutResponse {
    return TokoFoodHomeLayoutResponse(
        response = DynamicHomeChannelResponse(
            data = listOf(
                HomeLayoutResponse(
                    id = "11111",
                    layout = "tokofood_usp",
                    header = Header(
                        name = "",
                        serverTimeUnix = 0
                    ),
                ),
                HomeLayoutResponse(
                    id = "22222",
                    layout = "home_icon",
                    header = Header(
                        name = "",
                        serverTimeUnix = 0
                    )
                ),
                HomeLayoutResponse(
                    id = "33333",
                    layout = "banner_carousel_v2",
                    header = Header(
                        name = "Banner TokoFood",
                        serverTimeUnix = 0
                    )
                ),
                HomeLayoutResponse(
                    id = "44444",
                    layout = "6_image",
                    header = Header(
                        name = "6 Image",
                        serverTimeUnix = 0
                    )
                )
            )
        )
    )
}

fun createUSPResponse(): TokoFoodHomeUSPResponse {
    return TokoFoodHomeUSPResponse(
        response = USPResponse(
            list = listOf(USP(
                iconUrl = "lalala.jpg",
                title = "Title USP",
                description = "Desc USP",
                formatted = "Title USP & Desc USP"
            )),
            footer = "Footer",
            imageUrl = "lalala.jpg"
        )
    )
}

fun createDynamicIconsResponse(): TokoFoodHomeDynamicIconsResponse {
    return TokoFoodHomeDynamicIconsResponse(
        dynamicIcon = DynamicHomeIcon(
            listDynamicIcon = listOf(DynamicIcon(
                applinks = "tokopedia://food/home",
                imageUrl = "lalala.jpg"
            ))
        )
    )
}

fun createDynamicLegoBannerDataModel(
    id: String,
    groupId: String,
    headerName: String,
    headerServerTimeUnix: Long = 0,
    layout: String = "6_image"
): DynamicLegoBannerDataModel {
    val channelHeader = ChannelHeader(name = headerName, serverTimeUnix = headerServerTimeUnix)
    val channelConfig = ChannelConfig(layout = layout)
    val channelModel = ChannelModel(
        id = id,
        groupId = groupId,
        layout = layout,
        channelHeader = channelHeader,
        channelConfig = channelConfig,
        verticalPosition = 3
    )
    return DynamicLegoBannerDataModel(channelModel = channelModel)
}

fun createSliderBannerDataModel(
    id: String,
    groupId: String,
    headerName: String,
    headerServerTimeUnix: Long = 0
): BannerDataModel {
    val channelHeader = ChannelHeader(name = headerName, serverTimeUnix = headerServerTimeUnix)
    val channelConfig = ChannelConfig(layout = "banner_carousel_v2")
    val channelModel = ChannelModel(
        id = id,
        groupId = groupId,
        layout = "banner_carousel_v2",
        channelHeader = channelHeader,
        channelConfig = channelConfig,
        verticalPosition = 2
    )
    return BannerDataModel(channelModel = channelModel)
}

fun createUSPModel(): TokoFoodHomeUSPUiModel{
    return TokoFoodHomeUSPUiModel(
        id = "11111",
        uspModel = createUSPResponse(),
        TokoFoodLayoutState.SHOW,
        verticalPosition = 0
    )
}

fun createIconsModel(): TokoFoodHomeIconsUiModel {
    return TokoFoodHomeIconsUiModel(
        id = "22222",
        widgetParam = "",
        listIcons = createDynamicIconsResponse().dynamicIcon.listDynamicIcon,
        state = TokoFoodLayoutState.SHOW,
        verticalPosition = 1
    )
}

fun createMerchantListResponse(): TokoFoodMerchantListResponse {
    return TokoFoodMerchantListResponse(
        data = TokoFoodMerchantList(
            merchants = listOf(
                Merchant(
                   id = "abcdef",
                   name = "Toko 1",
                   isClosed = false
                ),
                Merchant(
                    id = "abcdeg",
                    name = "Toko 2",
                    isClosed = true
                )
            ),
            nextPageKey = "2"
        )
    )
}

fun createMerchantListModel1(): TokoFoodMerchantListUiModel {
    return TokoFoodMerchantListUiModel(
        id = "abcdef",
        createMerchantListResponse().data.merchants.first()
    )
}

fun createMerchantListModel2(): TokoFoodMerchantListUiModel {
    return TokoFoodMerchantListUiModel(
        id = "abcdeg",
        createMerchantListResponse().data.merchants.get(1)
    )
}