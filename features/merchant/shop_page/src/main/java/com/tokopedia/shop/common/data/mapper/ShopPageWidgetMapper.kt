package com.tokopedia.shop.common.data.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.campaign.view.model.ShopCampaignWidgetCarouselProductUiModel
import com.tokopedia.shop.campaign.view.model.ShopWidgetDisplaySliderBannerHighlightUiModel
import com.tokopedia.shop.common.data.model.ShopPageWidgetUiModel
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.data.model.ShopPageWidgetRequestModel
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetVoucherSliderUiModel

//TODO need to migrate all other shop widget mapper on home mapper to this mapper
object ShopPageWidgetMapper {

    fun mapToBannerTimerWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?
    )= ShopWidgetDisplayBannerTimerUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        data = mapToBannerItemWidget(widgetResponse.data.firstOrNull())
    )

    private fun mapToBannerItemWidget(
        data: ShopLayoutWidget.Widget.Data?,
    ): ShopWidgetDisplayBannerTimerUiModel.Data {
        return ShopWidgetDisplayBannerTimerUiModel.Data(
            appLink =  data?.appLink.orEmpty(),
            imageUrl =  data?.imageUrl.orEmpty(),
            linkType =  data?.linkType.orEmpty(),
            campaignId = data?.campaignId.orEmpty(),
            timeDescription = data?.timeInfo?.timeDescription.orEmpty(),
            timeCounter = data?.timeInfo?.timeCounter.orZero(),
            startDate = data?.timeInfo?.startDate.orEmpty(),
            endDate = data?.timeInfo?.endDate.orEmpty(),
            bgColor = data?.timeInfo?.bgColor.orEmpty(),
            textColor = data?.timeInfo?.textColor.orEmpty(),
            status = data?.timeInfo?.status ?: -1,
            totalNotify = data?.totalNotify.orZero(),
            totalNotifyWording = data?.totalNotifyWording.orEmpty()
        )
    }

    fun mapToSliderBannerHighlightWidget(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?
    )= ShopWidgetDisplaySliderBannerHighlightUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        listHighlightProductData = mapToListHighlightProductData(widgetResponse.data)
    )

    private fun mapToListHighlightProductData(data: List<ShopLayoutWidget.Widget.Data>): List<ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData> {
        return data.map {
            ShopWidgetDisplaySliderBannerHighlightUiModel.ProductHighlightData(
                appLink = it.appLink,
                imageUrl = it.imageUrl,
            )
        }
    }

    fun mapToCampaignProductCarouselWidgetUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?
    ) =  ShopCampaignWidgetCarouselProductUiModel(
        widgetId = widgetResponse.widgetID,
        layoutOrder = widgetResponse.layoutOrder,
        name = widgetResponse.name,
        type = widgetResponse.type,
        header = ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header),
        isFestivity = widgetLayout?.isFestivity.orFalse(),
        productList = ShopPageHomeMapper.mapCampaignListProduct(
            widgetResponse.data.firstOrNull()?.statusCampaign.orEmpty(),
            widgetResponse.data.firstOrNull()?.listProduct.orEmpty()
        )
    )

    fun mapToCampaignVoucherSliderUiModel(
        widgetResponse: ShopLayoutWidget.Widget,
        widgetLayout: ShopPageWidgetUiModel?
    ) = ShopWidgetVoucherSliderUiModel(
        widgetResponse.widgetID,
        widgetResponse.layoutOrder,
        widgetResponse.name,
        widgetResponse.type,
        ShopPageHomeMapper.mapToHeaderModel(widgetResponse.header),
        widgetLayout?.isFestivity.orFalse(),
        widgetLayout?.header?.data?.map { it.categorySlug }.orEmpty()
    )

    fun mapToShopPageWidgetRequest(listWidgetLayout: List<ShopPageWidgetUiModel>): List<ShopPageWidgetRequestModel> {
        return listWidgetLayout.map {
            ShopPageWidgetRequestModel(
                it.widgetId,
                it.widgetMasterId,
                it.widgetType,
                it.widgetName,
                ShopPageWidgetRequestModel.HeaderInput(
                    it.header.title,
                    it.header.subtitle,
                    it.header.ctaText,
                    it.header.ctaLink,
                    it.header.isAtc,
                    it.header.etalaseId,
                    it.header.isShowEtalaseName,
                    it.header.data.map { dataHeader ->
                        ShopPageWidgetRequestModel.HeaderInput.DataOnHeaderInput(
                            dataHeader.linkID.toString(),
                            dataHeader.linkType
                        )
                    }
                )
            )
        }
    }
}
