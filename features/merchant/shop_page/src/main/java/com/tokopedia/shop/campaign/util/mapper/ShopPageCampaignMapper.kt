package com.tokopedia.shop.campaign.util.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.campaign.domain.entity.RedeemPromoVoucherResult
import com.tokopedia.shop.campaign.domain.entity.ShopCampaignRedeemPromoVoucherResult
import com.tokopedia.shop.common.data.mapper.ShopPageWidgetMapper
import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel
import com.tokopedia.shop.common.view.model.ShopPageColorSchema
import com.tokopedia.shop.home.WidgetName.BANNER_TIMER
import com.tokopedia.shop.home.WidgetName.DISPLAY_DOUBLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_SINGLE_COLUMN
import com.tokopedia.shop.home.WidgetName.DISPLAY_TRIPLE_COLUMN
import com.tokopedia.shop.home.WidgetName.PRODUCT_HIGHLIGHT
import com.tokopedia.shop.home.WidgetName.SLIDER_BANNER
import com.tokopedia.shop.home.WidgetName.SLIDER_BANNER_HIGHLIGHT
import com.tokopedia.shop.home.WidgetName.SLIDER_SQUARE_BANNER
import com.tokopedia.shop.home.WidgetName.VIDEO
import com.tokopedia.shop.home.WidgetName.VOUCHER
import com.tokopedia.shop.home.WidgetType.BUNDLE
import com.tokopedia.shop.home.WidgetType.CAMPAIGN
import com.tokopedia.shop.home.WidgetType.DISPLAY
import com.tokopedia.shop.home.WidgetType.DYNAMIC
import com.tokopedia.shop.home.WidgetType.VOUCHER_SLIDER
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
        return layoutWidget.type.equals(DYNAMIC, ignoreCase = true) ||
            layoutWidget.name.equals(SLIDER_BANNER_HIGHLIGHT, ignoreCase = true)
    }

    private fun mapToWidgetUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        shopId: String,
        widgetLayout: ShopPageWidgetUiModel?
    ): Visitable<*>? {
        return when (widgetResponse.type.lowercase()) {
            VOUCHER_SLIDER.lowercase() -> mapToVoucherWidget(widgetResponse, widgetLayout)

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

    private fun mapToVoucherWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?
    ): Visitable<*>? {
        return when (widgetResponse.name) {
            VOUCHER -> {
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
        widgetLayout: ShopPageWidgetUiModel?
    ): Visitable<*>? {
        return when (widgetResponse.name) {
            DISPLAY_SINGLE_COLUMN, DISPLAY_DOUBLE_COLUMN, DISPLAY_TRIPLE_COLUMN, SLIDER_BANNER, SLIDER_SQUARE_BANNER, VIDEO -> {
                ShopPageHomeMapper.mapToDisplayImageWidget(widgetResponse, widgetLayout, false, ShopPageColorSchema())
            }

            BANNER_TIMER -> {
                ShopPageWidgetMapper.mapToBannerTimerWidget(widgetResponse, widgetLayout,false, ShopPageColorSchema())
            }

            SLIDER_BANNER_HIGHLIGHT -> {
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
