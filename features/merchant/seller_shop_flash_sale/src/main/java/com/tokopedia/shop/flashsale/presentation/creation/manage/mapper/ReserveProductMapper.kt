package com.tokopedia.shop.flashsale.presentation.creation.manage.mapper

import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignValidatedProductListResponse
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel

object ReserveProductMapper {
    fun mapFromProduct(product: GetSellerCampaignValidatedProductListResponse.Product) =
        ReserveProductModel (
            title = product.productName
        )

    fun mapFromProductList(
        productList: List<GetSellerCampaignValidatedProductListResponse.Product>
    ) = productList.map { mapFromProduct(it) }
}
