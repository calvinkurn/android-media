package com.tokopedia.buy_more_get_more.olp.data.mapper

import com.tokopedia.buy_more_get_more.olp.data.response.OfferProductListResponse
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import javax.inject.Inject

class GetOfferProductListMapper @Inject constructor() {

    fun map(response: OfferProductListResponse): OfferProductListUiModel {
        return OfferProductListUiModel(
            responseHeader = response.offeringProductList.responseHeader.toResponseHeaderModel(),
            productList = response.offeringProductList.productList.toProductListModel(),
            totalProduct = response.offeringProductList.totalProduct
        )
    }

    private fun OfferProductListResponse.ResponseHeader.toResponseHeaderModel(): OfferProductListUiModel.ResponseHeader {
        return OfferProductListUiModel.ResponseHeader(
            success,
            errorMessage
        )
    }

    private fun List<OfferProductListResponse.Product>.toProductListModel(): List<OfferProductListUiModel.Product> {
        return map {
            OfferProductListUiModel.Product(
                offerId = it.offerId,
                parentId = it.parentId,
                productId = it.productId,
                warehouseId = it.warehouseId,
                productUrl = it.productUrl,
                imageUrl = it.imageUrl,
                name = it.name,
                price = it.price,
                rating = it.rating,
                soldCount = it.soldCount,
                stock = it.stock,
                isVbs = it.isVbs,
                campaign = it.campaign.toCampaignModel(),
                labelGroup = it.labelGroup.toLabelGroup()
            )
        }
    }

    private fun OfferProductListResponse.Product.Campaign.toCampaignModel(): OfferProductListUiModel.Product.Campaign {
        return OfferProductListUiModel.Product.Campaign(
            name,
            originalPrice,
            discountedPrice,
            discountedPercentage,
            customStock
        )
    }

    private fun List<OfferProductListResponse.Product.LabelGroup>.toLabelGroup(): List<OfferProductListUiModel.Product.LabelGroup> {
        return map {
            OfferProductListUiModel.Product.LabelGroup(
                position = it.position,
                title = it.title,
                type = it.type,
                url = it.url
            )
        }
    }
}
