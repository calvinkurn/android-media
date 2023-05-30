package com.tokopedia.shop.campaign.util.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.campaign.view.model.ShopCampaignWidgetCarouselProductUiModel
import com.tokopedia.shop.common.data.mapper.ShopPageWidgetMapper
import com.tokopedia.shop.common.data.model.ShopPageWidgetLayoutUiModel
import com.tokopedia.shop.home.WidgetName.BANNER_TIMER
import com.tokopedia.shop.home.WidgetName.DISPLAY_DOUBLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_SINGLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_TRIPLE_COLUMN
import com.tokopedia.shop.home.WidgetName.PRODUCT_HIGHLIGHT
import com.tokopedia.shop.home.WidgetName.SLIDER_BANNER
import com.tokopedia.shop.home.WidgetName.SLIDER_BANNER_HIGHLIGHT
import com.tokopedia.shop.home.WidgetName.SLIDER_SQUARE_BANNER
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.WidgetType.BUNDLE
import com.tokopedia.shop.home.WidgetType.CAMPAIGN
import com.tokopedia.shop.home.WidgetType.DISPLAY
import com.tokopedia.shop.home.WidgetType.DYNAMIC
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel

object ShopPageCampaignMapper {

    fun mapToListShopCampaignWidget(
        responseWidgetData: List<ShopLayoutWidget.Widget>,
        shopId: String,
        listWidgetLayout: List<ShopPageWidgetLayoutUiModel>
    ): List<Visitable<*>> {
        return mutableListOf<Visitable<*>>().apply {
            responseWidgetData.filter { checkShouldMapWidget(it) }.onEach {
                when (val widgetUiModel = mapToWidgetUiModel(
                    it,
                    shopId,
                    listWidgetLayout.firstOrNull { widgetLayout -> it.widgetID == widgetLayout.widgetId })) {
                    is BaseShopHomeWidgetUiModel -> {
                        widgetUiModel.let { model ->
                            model.widgetMasterId = it.widgetMasterID
                            add(model)
                        }
                    }

                }
            }
        }
    }

    private fun checkShouldMapWidget(layoutWidget: ShopLayoutWidget.Widget): Boolean {
        return layoutWidget.data.isNotEmpty() ||
            layoutWidget.type.equals(DYNAMIC, ignoreCase = true)
    }

    private fun mapToWidgetUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        shopId: String,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ): Visitable<*>? {
        return when (widgetResponse.type.lowercase()) {
            DISPLAY.lowercase() -> mapDisplayWidget(widgetResponse, widgetLayout)

            CAMPAIGN.lowercase() -> mapCampaignWidget(widgetResponse, widgetLayout)

            DYNAMIC.lowercase() -> mapDynamicWidget(widgetResponse, widgetLayout)

            BUNDLE.lowercase() -> mapBundleWidget(
                widgetResponse,
                shopId,
                widgetLayout
            )

            else -> {
                null
            }
        }
    }

    private fun mapBundleWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        shopId: String,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ): Visitable<*> {
        return ShopPageHomeMapper.mapToProductBundleListUiModel(
            widgetResponse,
            shopId,
            widgetLayout
        )
    }

    private fun mapDynamicWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ): Visitable<*> {
        return ShopPageHomeMapper.mapCarouselPlayWidget(
            widgetResponse,
            widgetLayout
        )
    }

    private fun mapCampaignWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ): Visitable<*>? {
        return when (widgetResponse.name) {
            PRODUCT_HIGHLIGHT -> {
                ShopPageWidgetMapper.mapToCampaignProductCarouselWidgetUiModel(
                    widgetResponse,
                    widgetLayout
                )
            }

            else -> null
        }
    }

    private fun mapDisplayWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetLayoutUiModel?
    ): Visitable<*>? {
        return when (widgetResponse.name) {
            DISPLAY_SINGLE_COLUMN, DISPLAY_DOUBLE_COLUMN, DISPLAY_TRIPLE_COLUMN, SLIDER_BANNER, SLIDER_SQUARE_BANNER, VIDEO -> {
                ShopPageHomeMapper.mapToDisplayImageWidget(widgetResponse, widgetLayout)
            }

            BANNER_TIMER -> {
                ShopPageWidgetMapper.mapToBannerTimerWidget(widgetResponse, widgetLayout)
            }

            SLIDER_BANNER_HIGHLIGHT -> {
                ShopPageWidgetMapper.mapToSliderBannerHighlightWidget(widgetResponse, widgetLayout)
            }

            else -> null
        }
    }


    fun mapShopCampaignWidgetLayoutToListShopCampaignWidget(
        listWidgetLayout: List<ShopPageWidgetLayoutUiModel>,
        shopId: String
    ): List<Visitable<*>> {
        return mutableListOf<Visitable<*>>().apply {
            listWidgetLayout.onEach {
                mapToWidgetUiModel(
                    ShopLayoutWidget.Widget(
                        widgetID = it.widgetId,
                        type = it.widgetType,
                        name = it.widgetName,
                        header = ShopLayoutWidget.Widget.Header(
                            title = it.widgetTitle
                        ),
                        data = it.header.data.map { headerDataUiModel ->
                            ShopLayoutWidget.Widget.Data(bundleGroupId = headerDataUiModel.linkID.toString())
                        }
                    ),
                    shopId,
                    it
                )?.let { widgetModel ->
                    when (widgetModel) {
                        is BaseShopHomeWidgetUiModel -> {
                            widgetModel.widgetMasterId = it.widgetMasterId
                            add(widgetModel)
                        }
                    }
                }
            }
        }
    }

}
