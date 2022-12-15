package com.tokopedia.play.analytic.tokonow

import com.tokopedia.play.ui.productsheet.adapter.ProductSheetAdapter
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel

/**
 * @author by astidhiyaa on 17/06/22
 */
interface PlayTokoNowAnalytic {
    fun sendDataNow(channelId: String, channelType: PlayChannelType, channelName: String)
    fun impressAddressWidgetNow()
    fun impressChooseAddressNow()
    fun clickChooseAddressNow()
    fun clickInfoAddressWidgetNow()
    fun impressInfoNow()
    fun clickInfoNow()
    fun impressNowToaster()
    fun clickLihatNowToaster()
    fun impressGlobalToaster()
    fun clickProductBottomSheetNow(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        position: Int,
    )

    fun clickFeaturedProductNow(featuredProduct: PlayProductUiModel.Product, position: Int)

    fun impressProductBottomSheetNow(
        products: Map<ProductSheetAdapter.Item.Product, Int>
    )

    fun impressFeaturedProductNow(
        products: List<Pair<PlayProductUiModel.Product, Int>>,
    )

    fun clickBeliNowProduct(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        shopInfo: PlayPartnerInfo,
    )

    fun clickAtcNowProduct(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        shopInfo: PlayPartnerInfo,
    )
}
