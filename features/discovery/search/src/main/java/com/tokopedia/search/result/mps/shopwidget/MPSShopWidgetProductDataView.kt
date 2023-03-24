package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.mps.domain.model.MPSModel
import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS.Shop
import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS.Shop.Product

data class MPSShopWidgetProductDataView(
    val id: String = "",
    val imageUrl: String = "",
    val name: String = "",
    val applink: String = "",
    val price: Int = 0,
    val originalPrice: String = "",
    val discountPercentage: Int = 0,
    val priceFormat: String = "",
    val ratingAverage: String = "",
    val parentId: String = "",
    val stock: Int = 0,
    val minOrder: Int = 0,
    val componentId: String = "",
    val trackingOption: Int = 0,
    val buttonList: List<MPSButtonDataView> = listOf(),
    val labelGroupList: List<MPSProductLabelGroupDataView> = listOf(),
    val impressHolder: ImpressHolder = ImpressHolder(),
) {

    fun primaryButton(): MPSButtonDataView? = buttonList.find(MPSButtonDataView::isPrimary)

    fun secondaryButton(): MPSButtonDataView? = buttonList.find(MPSButtonDataView::isSecondary)

    fun asObjectDataLayer(): Any = mapOf<String, String>()

    companion object {
        fun create(
            shop: Shop,
            product: Product,
            keywords: String,
        ) = MPSShopWidgetProductDataView(
            id = product.id,
            imageUrl = product.imageUrl,
            name = product.name,
            applink = product.applink,
            price = product.price,
            originalPrice = product.originalPrice,
            discountPercentage = product.discountPercentage,
            priceFormat = product.priceFormat,
            ratingAverage = product.ratingAverage,
            parentId = product.parentId,
            stock = product.stock,
            minOrder = product.minOrder,
            componentId = product.componentId,
            trackingOption = product.trackingOption,
            buttonList = product.buttonList
                .map {
                    MPSButtonDataView.create(it, keywords, shop.id, "")
                 },
            labelGroupList = product.labelGroupList.map(MPSProductLabelGroupDataView::create),
        )
    }
}
