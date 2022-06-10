package com.tokopedia.shop.flashsale.presentation.creation.manage.mapper

import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignValidatedProductListResponse
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel

object ReserveProductMapper {
    fun mapFromProduct(product: GetSellerCampaignValidatedProductListResponse.Product) =
        ReserveProductModel (
            productName = product.productName,
            imageUrl = product.pictures.firstOrNull {
                it.urlThumbnail.isNotBlank()
            }?.urlThumbnail.orEmpty(),
            sku = product.sku,
            price = product.price,
            variantCount = product.variantChildsIds.size,
            stock = product.stock
        )

    fun mapFromProductList(
        productList: List<GetSellerCampaignValidatedProductListResponse.Product>
    ) = productList.map { mapFromProduct(it) }
}
