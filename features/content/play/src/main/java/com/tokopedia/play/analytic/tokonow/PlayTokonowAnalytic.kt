package com.tokopedia.play.analytic.tokonow

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel

/**
 * @author by astidhiyaa on 17/06/22
 */
interface PlayTokonowAnalytic {
    fun sendData(channelId: String, channelType: PlayChannelType, channelName: String)
    fun impressAddressWidget()
    fun impressChooseAddress()
    fun clickChooseAddress()
    fun clickInfoAddressWidget()
    fun impressInfoNow()
    fun clickInfoNow()
    fun impressNowToaster()
    fun clickLihatNowToaster()
    fun impressGlobalToaster()
    fun clickGlobalToaster()
    fun clickProductBottomSheet(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        position: Int,
    )

    fun clickFeaturedProduct(featuredProduct: PlayProductUiModel.Product, position: Int)

    fun impressProductBottomSheet(
        products: List<Pair<PlayProductUiModel.Product, Int>>,
        sectionInfo: ProductSectionUiModel.Section,
    )

    fun impressFeaturedProduct(
        products: List<Pair<PlayProductUiModel.Product, Int>>,
    )

    fun clickBeli(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        shopInfo: PlayPartnerInfo,
    )

    fun clickAtc(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        shopInfo: PlayPartnerInfo,
    )
}