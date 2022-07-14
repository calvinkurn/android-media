package com.tokopedia.play.analytic.tokonow

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel

/**
 * @author by astidhiyaa on 17/06/22
 */
interface PlayTokonowAnalytic {
    fun impressAddressWidget(channelId: String, channelType: PlayChannelType)
    fun impressChooseAddress(channelId: String, channelType: PlayChannelType)
    fun clickChooseAddress(channelId: String, channelType: PlayChannelType)
    fun clickInfoAddressWidget(channelId: String, channelType: PlayChannelType)
    fun impressInfoNow(channelId: String, channelType: PlayChannelType)
    fun clickInfoNow(channelId: String, channelType: PlayChannelType)

    fun impressNowToaster(channelId: String, channelType: PlayChannelType)
    fun clickLihatNowToaster(channelId: String, channelType: PlayChannelType)

    fun impressGlobalToaster(channelId: String, channelType: PlayChannelType)
    fun clickGlobalToaster(channelId: String, channelType: PlayChannelType)

    fun clickProductBottomSheet(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        position: Int,
        channelId: String,
        channelType: PlayChannelType,
    )

    fun clickFeaturedProduct(featuredProduct: PlayProductUiModel.Product, position: Int, channelId: String, channelType: PlayChannelType, channelName: String)

    fun impressProductBottomSheet(
        products: List<Pair<PlayProductUiModel.Product, Int>>,
        sectionInfo: ProductSectionUiModel.Section,
        channelId: String,
        channelType: PlayChannelType,
    )

    fun impressFeaturedProduct(
        products: List<Pair<PlayProductUiModel.Product, Int>>,
        isGeneral: Boolean = true,
        channelId: String,
        channelType: PlayChannelType,
    )

    fun clickBeli(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        shopInfo: PlayPartnerInfo,
        channelId: String,
        channelType: PlayChannelType,
    )

    fun clickAtc(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        shopInfo: PlayPartnerInfo,
        channelId: String,
        channelType: PlayChannelType,
    )
}