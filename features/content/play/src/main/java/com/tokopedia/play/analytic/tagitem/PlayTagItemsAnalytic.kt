package com.tokopedia.play.analytic.tagitem

import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayPartnerInfo
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.trackingoptimizer.TrackingQueue

/**
 * Created by kenny.hadisaputra on 10/03/22
 */
interface PlayTagItemsAnalytic {

    fun impressBottomSheetProducts(
        products: List<Pair<PlayProductUiModel.Product, Int>>,
        sectionInfo: ProductSectionUiModel.Section,
    )

    fun impressFeaturedProducts(
        products: List<Pair<PlayProductUiModel.Product, Int>>,
    )

    fun clickProduct(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        position: Int,
    )

    fun clickFeaturedProduct(
        featuredProduct: PlayProductUiModel.Product,
        position: Int,
    )

    fun scrollMerchantVoucher(lastPositionViewed: Int)

    fun clickActionProductWithVariant(
        productId: String,
        productAction: ProductAction,
    )

    fun clickProductAction(
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        productAction: ProductAction,
        bottomInsetsType: BottomInsetsType,
        shopInfo: PlayPartnerInfo,
    )

    fun clickSeeToasterAfterAtc()

    /**
     * Carousel
     */
    fun impressPinnedProductInCarousel(
        product: PlayProductUiModel.Product,
        position: Int,
    )

    fun clickPinnedProductInCarousel(
        product: PlayProductUiModel.Product,
        position: Int,
    )

    fun buyPinnedProductInCarousel(
        product: PlayProductUiModel.Product,
        cartId: String,
        quantity: Int,
        action: ProductAction,
    )

    fun atcPinnedProductInCarousel(
        product: PlayProductUiModel.Product,
        cartId: String,
        quantity: Int,
    )

    fun impressToasterAtcPinnedProductCarousel()

    interface Factory {
        fun create(
            trackingQueue: TrackingQueue,
            channelInfo: PlayChannelInfoUiModel
        ): PlayTagItemsAnalytic
    }
}
