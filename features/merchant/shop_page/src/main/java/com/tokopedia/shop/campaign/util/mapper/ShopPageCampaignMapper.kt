package com.tokopedia.shop.campaign.util.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import com.tokopedia.shop.campaign.domain.entity.ShopCampaignRedeemPromoVoucherResult
import com.tokopedia.shop.common.data.mapper.ShopPageWidgetMapper
import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.home.WidgetNameEnum
import com.tokopedia.shop.home.WidgetTypeEnum
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.BaseShopHomeWidgetUiModel

object ShopPageCampaignMapper {

    fun mapToListShopCampaignWidget(
        responseWidgetData: List<ShopLayoutWidget.Widget>,
        shopId: String,
        listWidgetLayout: List<ShopPageWidgetUiModel>
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
        return layoutWidget.data.isNotEmpty() || isSpecialWidget(layoutWidget)
    }

    private fun isSpecialWidget(layoutWidget: ShopLayoutWidget.Widget): Boolean {
        return layoutWidget.type.equals(WidgetTypeEnum.DYNAMIC.value, ignoreCase = true) ||
            layoutWidget.name.equals(WidgetNameEnum.SLIDER_BANNER_HIGHLIGHT.value, ignoreCase = true)
    }

    private fun mapToWidgetUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        shopId: String,
        widgetLayout: ShopPageWidgetUiModel?
    ): Visitable<*>? {
        return when (widgetResponse.type.lowercase()) {
            WidgetTypeEnum.VOUCHER_SLIDER.value.lowercase() -> mapToVoucherWidget(widgetResponse, widgetLayout)

            WidgetTypeEnum.DISPLAY.value.lowercase() -> mapDisplayWidget(widgetResponse, widgetLayout)

            WidgetTypeEnum.CAMPAIGN.value.lowercase() -> mapCampaignWidget(widgetResponse, widgetLayout)

            WidgetTypeEnum.DYNAMIC.value.lowercase() -> mapDynamicWidget(widgetResponse, widgetLayout)

            WidgetTypeEnum.BUNDLE.value.lowercase() -> mapBundleWidget(
                widgetResponse,
                shopId,
                widgetLayout
            )

            else -> {
                null
            }
        }
    }

    private fun mapToVoucherWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?
    ): Visitable<*>? {
        return when (widgetResponse.name) {
            WidgetNameEnum.VOUCHER.value -> {
                ShopPageWidgetMapper.mapToCampaignVoucherSliderUiModel(
                    widgetResponse,
                    widgetLayout
                )
            }

            else -> null
        }
    }

    private fun mapBundleWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        shopId: String,
        widgetLayout: ShopPageWidgetUiModel?
    ): Visitable<*> {
        return ShopPageHomeMapper.mapToProductBundleListUiModel(
            widgetResponse,
            shopId,
            widgetLayout,
            false,
            ShopPageColorSchema()
        )
    }

    private fun mapDynamicWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?
    ): Visitable<*> {
        return ShopPageHomeMapper.mapCarouselPlayWidget(
            widgetResponse,
            widgetLayout,
            false,
            ShopPageColorSchema()
        )
    }

    private fun mapCampaignWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?
    ): Visitable<*>? {
        return when (widgetResponse.name) {
            WidgetNameEnum.PRODUCT_HIGHLIGHT.value -> {
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
        widgetLayout: ShopPageWidgetUiModel?
    ): Visitable<*>? {
        return when (widgetResponse.name) {
            WidgetNameEnum.DISPLAY_SINGLE_COLUMN.value,
            WidgetNameEnum.DISPLAY_DOUBLE_COLUMN.value,
            WidgetNameEnum.DISPLAY_TRIPLE_COLUMN.value,
            WidgetNameEnum.SLIDER_BANNER.value,
            WidgetNameEnum.SLIDER_SQUARE_BANNER.value,
            WidgetNameEnum.VIDEO.value -> {
                ShopPageHomeMapper.mapToDisplayImageWidget(widgetResponse, widgetLayout, false, ShopPageColorSchema())
            }

            WidgetNameEnum.BANNER_TIMER.value -> {
                ShopPageWidgetMapper.mapToBannerTimerWidget(widgetResponse, widgetLayout,false, ShopPageColorSchema())
            }

            WidgetNameEnum.SLIDER_BANNER_HIGHLIGHT.value -> {
                ShopPageWidgetMapper.mapToSliderBannerHighlightWidget(widgetResponse, widgetLayout)
            }

            else -> null
        }
    }


    fun mapShopCampaignWidgetLayoutToListShopCampaignWidget(
        listWidgetLayout: List<ShopPageWidgetUiModel>,
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

    fun mapToShopCampaignRedeemPromoVoucherResult(
        slug: String,
        couponCode: String,
        campaignId: String,
        widgetId: String,
        redeemResult: RedeemPromoVoucherResult,
    ) = ShopCampaignRedeemPromoVoucherResult(
        slug,
        couponCode,
        campaignId,
        widgetId,
        redeemResult
    )

}
