package com.tokopedia.tokofood.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelBanner
import com.tokopedia.home_component.model.ChannelBenefit
import com.tokopedia.home_component.model.ChannelConfig
import com.tokopedia.home_component.model.ChannelCtaData
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelHeader
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.LabelGroup
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.CategoryWidgetV2DataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.HomeComponentVisitable
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutItemState
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutType.Companion.BANNER_CAROUSEL
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutType.Companion.CATEGORY_WIDGET
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutType.Companion.ICON_TOKOFOOD
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutType.Companion.LEGO_6_IMAGE
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutType.Companion.TABS_TOKOFOOD
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutType.Companion.USP_TOKOFOOD
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.CHOOSE_ADDRESS_WIDGET_ID
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_NO_ADDRESS
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_NO_PIN_POINT
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.EMPTY_STATE_OUT_OF_COVERAGE
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeStaticLayoutId.Companion.LOADING_STATE
import com.tokopedia.tokofood.home.domain.data.HomeLayoutResponse
import com.tokopedia.tokofood.home.domain.data.Merchant
import com.tokopedia.tokofood.home.domain.data.TokoFoodHomeDynamicIconsResponse
import com.tokopedia.tokofood.home.domain.data.TokoFoodHomeUSPResponse
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodCategoryLoadingStateUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeChooseAddressWidgetUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeIconsUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodItemUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeLayoutUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeLoadingStateUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeEmptyStateLocationUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeUSPUiModel
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodMerchantListUiModel

object TokoFoodHomeMapper {

    val SUPPORTED_LAYOUT_TYPE = listOf(
        BANNER_CAROUSEL,
        LEGO_6_IMAGE,
        CATEGORY_WIDGET,
        TABS_TOKOFOOD,
        USP_TOKOFOOD,
        ICON_TOKOFOOD
    )

    fun MutableList<TokoFoodItemUiModel>.addLoadingIntoList() {
        val loadingLayout = TokoFoodHomeLoadingStateUiModel(id = LOADING_STATE)
        add(TokoFoodItemUiModel(loadingLayout, TokoFoodLayoutItemState.LOADED))
    }

    fun MutableList<TokoFoodItemUiModel>.addNoPinPointState() {
        addChooseAddressWidget()
        add(TokoFoodItemUiModel(TokoFoodHomeEmptyStateLocationUiModel(EMPTY_STATE_NO_PIN_POINT), TokoFoodLayoutItemState.LOADED))
    }

    fun MutableList<TokoFoodItemUiModel>.addNoAddressState() {
        addChooseAddressWidget()
        add(TokoFoodItemUiModel(TokoFoodHomeEmptyStateLocationUiModel(EMPTY_STATE_NO_ADDRESS), TokoFoodLayoutItemState.LOADED))
    }

    fun MutableList<TokoFoodItemUiModel>.addOutOfCoverageState() {
        addChooseAddressWidget()
        add(TokoFoodItemUiModel(TokoFoodHomeEmptyStateLocationUiModel(EMPTY_STATE_OUT_OF_COVERAGE), TokoFoodLayoutItemState.LOADED))
    }


    fun MutableList<TokoFoodItemUiModel>.mapHomeLayoutList(
        responses: List<HomeLayoutResponse>
    ){
        if(isOutOfCoverage(responses)) {
            addOutOfCoverageState()
        } else {
            addChooseAddressWidget()
            responses.filter { SUPPORTED_LAYOUT_TYPE.contains(it.layout) }
                .forEach { homeLayoutResponse ->
                    mapToHomeUiModel(homeLayoutResponse)?.let { item ->
                        add(item)
                    }
                }
        }
    }

    fun MutableList<TokoFoodItemUiModel>.mapUSPData(
        item: TokoFoodHomeUSPUiModel,
        uspResponse: TokoFoodHomeUSPResponse
    ){
        updateItemById(item.visitableId) {
            val usp = TokoFoodHomeUSPUiModel(
                item.visitableId,
                uspResponse,
                TokoFoodLayoutState.SHOW
            )
            TokoFoodItemUiModel(usp, TokoFoodLayoutItemState.LOADED)
        }
    }

    fun MutableList<TokoFoodItemUiModel>.mapDynamicIcons(
        item: TokoFoodHomeIconsUiModel,
        result: TokoFoodHomeDynamicIconsResponse
    ){
        updateItemById(item.visitableId) {
            val usp = TokoFoodHomeIconsUiModel(
                id = item.visitableId,
                widgetParam = item.widgetParam,
                listIcons = result.dynamicIcon.listDynamicIcon,
                state = TokoFoodLayoutState.SHOW
            )
            TokoFoodItemUiModel(usp, TokoFoodLayoutItemState.LOADED)
        }
    }

    fun MutableList<TokoFoodItemUiModel>.addChooseAddressWidget() {
        val chooseAddressUiModel =
            TokoFoodHomeChooseAddressWidgetUiModel(id = CHOOSE_ADDRESS_WIDGET_ID)
        add(TokoFoodItemUiModel(chooseAddressUiModel, TokoFoodLayoutItemState.LOADED))

    }

    fun MutableList<TokoFoodItemUiModel>.setStateToLoading(item: TokoFoodItemUiModel) {
        item.layout?.let { layout ->
            updateItemById(layout.getVisitableId()) {
                TokoFoodItemUiModel(layout, TokoFoodLayoutItemState.LOADING)
            }
        }
    }

    fun MutableList<TokoFoodItemUiModel>.removeItem(id: String?) {
        getItemIndex(id)?.let { removeAt(it) }
    }

    fun mapToChannelModel(response: HomeLayoutResponse): ChannelModel {
        return ChannelModel(
            id = response.id,
            groupId = response.groupId,
            type = response.type,
            layout = response.layout,
            pageName = response.pageName,
            channelHeader = ChannelHeader(
                response.header.id,
                response.header.name,
                response.header.subtitle,
                response.header.expiredTime,
                response.header.serverTimeUnix,
                response.header.applink,
                response.header.url,
                response.header.backColor,
                response.header.backImage,
                response.header.textColor
            ),
            channelBanner = ChannelBanner(
                id = response.banner.id,
                title = response.banner.title,
                description = response.banner.description,
                backColor = response.banner.backColor,
                url = response.banner.url,
                applink = response.banner.applink,
                textColor = response.banner.textColor,
                imageUrl = response.banner.imageUrl,
                attribution = response.banner.attribution,
                cta = ChannelCtaData(
                    response.banner.cta.type,
                    response.banner.cta.mode,
                    response.banner.cta.text,
                    response.banner.cta.couponCode
                ),
                gradientColor = response.banner.gradientColor
            ),
            channelConfig = ChannelConfig(
                response.layout,
                response.showPromoBadge,
                response.hasCloseButton,
                if (response.header.serverTimeUnix != 0L) ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(response.header.serverTimeUnix) else 0,
                response.timestamp,
                response.isAutoRefreshAfterExpired
            ),
            trackingAttributionModel = TrackingAttributionModel(
                galaxyAttribution = response.galaxyAttribution,
                persona = response.persona,
                brandId = response.brandId,
                categoryPersona = response.categoryPersona,
                categoryId = response.categoryID,
                persoType = response.persoType,
                campaignCode = response.campaignCode,
                homeAttribution = response.homeAttribution
            ),
            channelGrids = response.grids.map {
                ChannelGrid(
                    id = it.id,
                    warehouseId = it.warehouseId,
                    parentProductId = it.parentProductId,
                    recommendationType = it.recommendationType,
                    minOrder = it.minOrder,
                    stock = it.maxOrder,
                    price = it.price,
                    imageUrl = it.imageUrl,
                    name = it.name,
                    applink = it.applink,
                    url = it.url,
                    discount = it.discount,
                    slashedPrice = it.slashedPrice,
                    label = it.label,
                    soldPercentage = it.soldPercentage,
                    attribution = it.attribution,
                    impression = it.impression,
                    cashback = it.cashback,
                    productClickUrl = it.productClickUrl,
                    isTopads = it.isTopads,
                    productViewCountFormatted = it.productViewCountFormatted,
                    isOutOfStock = it.isOutOfStock,
                    isFreeOngkirActive = it.freeOngkir.isActive,
                    freeOngkirImageUrl = it.freeOngkir.imageUrl,
                    shopId = it.shop.shopId,
                    hasBuyButton = it.hasBuyButton,
                    labelGroup = it.labelGroup.map { label ->
                        LabelGroup(
                            title = label.title,
                            position = label.position,
                            type = label.type
                        )
                    },
                    rating = it.rating,
                    ratingFloat = it.ratingFloat,
                    countReview = it.countReview,
                    backColor = it.backColor,
                    benefit = ChannelBenefit(
                        it.benefit.type,
                        it.benefit.value
                    ),
                    textColor = it.textColor,
                    param = it.param
                )
            }
        )
    }

    fun MutableList<TokoFoodItemUiModel>.updateItemById(id: String?, block: () -> TokoFoodItemUiModel?) {
        getItemIndex(id)?.let { index ->
            block.invoke()?.let { item ->
                removeAt(index)
                add(index, item)
            }
        }
    }

    fun MutableList<TokoFoodItemUiModel>.getItemIndex(visitableId: String?): Int? {
        return firstOrNull { it.layout?.getVisitableId() == visitableId }?.let { indexOf(it) }
    }

    fun Visitable<*>.getVisitableId(): String? {
        return when (this) {
            is TokoFoodHomeLayoutUiModel -> visitableId
            is HomeComponentVisitable -> visitableId()
            else -> null
        }
    }

    private fun mapToHomeUiModel(
        response: HomeLayoutResponse
    ): TokoFoodItemUiModel? {
        val loadedState = TokoFoodLayoutItemState.LOADED
        val notLoadedState = TokoFoodLayoutItemState.NOT_LOADED

        return when (response.layout) {
            // region data got from dynamic channel direcly
            LEGO_6_IMAGE -> mapLego6DataModel(response, loadedState)
            BANNER_CAROUSEL -> mapBannerCarouselModel(response, loadedState)
            CATEGORY_WIDGET -> mapCategoryWidgetModel(response, loadedState)
            // endregion

            // region data got from other gql
            USP_TOKOFOOD -> mapUSPWidgetModel(response, notLoadedState)
            ICON_TOKOFOOD -> mapDynamicIconModel(response, notLoadedState)
            // endregion

            else -> null
        }
    }

    private fun mapLego6DataModel(response: HomeLayoutResponse, state: TokoFoodLayoutItemState): TokoFoodItemUiModel {
        val channelModel = mapToChannelModel(response)
        val dynamicLego6Data = DynamicLegoBannerDataModel(channelModel)
        return TokoFoodItemUiModel(dynamicLego6Data, state)
    }

    private fun mapBannerCarouselModel(response: HomeLayoutResponse, state: TokoFoodLayoutItemState): TokoFoodItemUiModel {
        val channelModel = mapToChannelModel(response)
        val bannerDataModel = BannerDataModel(channelModel)
        return TokoFoodItemUiModel(bannerDataModel, state)
    }

    private fun mapCategoryWidgetModel(response: HomeLayoutResponse, state: TokoFoodLayoutItemState): TokoFoodItemUiModel {
        val channelModel = mapToChannelModel(response)
        val categoryWidgetV2DataModel = CategoryWidgetV2DataModel(channelModel)
        return TokoFoodItemUiModel(categoryWidgetV2DataModel, state)
    }

    private fun mapUSPWidgetModel(response: HomeLayoutResponse, state: TokoFoodLayoutItemState): TokoFoodItemUiModel {
        val uspWidgetDataModel = TokoFoodHomeUSPUiModel(
            id = response.id,
            uspModel = null,
            TokoFoodLayoutState.LOADING
        )
        return TokoFoodItemUiModel(uspWidgetDataModel, state)
    }

    private fun mapDynamicIconModel(response: HomeLayoutResponse, state: TokoFoodLayoutItemState): TokoFoodItemUiModel {
        val dynamicIconModel = TokoFoodHomeIconsUiModel(
            id = response.id,
            widgetParam = response.widgetParam,
            listIcons = null,
            state = TokoFoodLayoutState.LOADING
        )
        return TokoFoodItemUiModel(dynamicIconModel, state)
    }

    private fun isOutOfCoverage(responses: List<HomeLayoutResponse>): Boolean {
        return responses.isNullOrEmpty()
    }

}