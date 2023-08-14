package com.tokopedia.buy_more_get_more.olp.data.mapper

import com.tokopedia.buy_more_get_more.olp.data.response.OfferProductListResponse
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import javax.inject.Inject

class GetOfferProductListMapper @Inject constructor() {

    fun map(response: OfferProductListResponse): OfferProductListUiModel {
        return OfferProductListUiModel(
            responseHeader = response.offeringProductList.responseHeader.toResponseHeaderModel(),
            productList = response.offeringProductList.productList.toProductListModel()
        )
    }

    private fun OfferProductListResponse.ResponseHeader.toResponseHeaderModel(): OfferProductListUiModel.ResponseHeader {
        return OfferProductListUiModel.ResponseHeader(
            success,
            error_code,
            processTime
        )
    }

    private fun List<OfferProductListResponse.Product>.toProductListModel(): List<OfferProductListUiModel.Product> {
        return map {
            OfferProductListUiModel.Product(
                offerId = it.offerId,
                parentId = it.parentId,
                productId = it.productId,
                warehouseId = it.warehouseId,
                imageUrl = it.imageUrl,
                name = it.name,
                price = it.price,
                rating = it.rating,
                soldCount = it.soldCount,
                minOrder = it.minOrder,
                maxOrder = it.maxOrder,
                stock = it.stock,
                isVbs = it.isVbs,
                campaign = it.campaign.toCampaignModel()
            )
        }
    }

    private fun OfferProductListResponse.Product.Campaign.toCampaignModel(): OfferProductListUiModel.Product.Campaign {
        return OfferProductListUiModel.Product.Campaign(
            name,
            originalPrice,
            discountedPrice,
            discountedPercentage,
            minOrder,
            maxOrder,
            customStock
        )
    }
}
