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
        trackingQueue: TrackingQueue,
        products: List<Pair<PlayProductUiModel.Product, Int>>,
        sectionInfo: ProductSectionUiModel.Section,
    )

    fun impressFeaturedProducts(
        trackingQueue: TrackingQueue,
        products: List<Pair<PlayProductUiModel.Product, Int>>,
    )

    fun clickProduct(
        trackingQueue: TrackingQueue,
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        position: Int,
    )

    fun clickFeaturedProduct(
        trackingQueue: TrackingQueue,
        featuredProduct: PlayProductUiModel.Product,
        position: Int,
    )

    fun scrollMerchantVoucher(lastPositionViewed: Int)

    fun clickActionProductWithVariant(
        productId: String,
        productAction: ProductAction,
    )

    fun clickProductAction(
        trackingQueue: TrackingQueue,
        product: PlayProductUiModel.Product,
        sectionInfo: ProductSectionUiModel.Section,
        cartId: String,
        productAction: ProductAction,
        bottomInsetsType: BottomInsetsType,
        shopInfo: PlayPartnerInfo,
    )

    fun clickSeeToasterAfterAtc()

    interface Factory {
        fun create(
            channelInfo: PlayChannelInfoUiModel
        ): PlayTagItemsAnalytic
    }
}