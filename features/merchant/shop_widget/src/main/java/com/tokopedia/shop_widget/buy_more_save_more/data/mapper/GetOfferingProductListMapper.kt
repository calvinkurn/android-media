package com.tokopedia.shop_widget.buy_more_save_more.data.mapper

import com.tokopedia.campaign.data.response.OfferProductListResponse
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel
import javax.inject.Inject

class GetOfferingProductListMapper@Inject constructor() {

    fun map(response: OfferProductListResponse): OfferingProductListUiModel {
        return OfferingProductListUiModel(
            responseHeader = response.offeringProductList.responseHeader.toResponseHeaderModel(),
            productList = response.offeringProductList.productList.toProductListModel(response.offeringProductList.totalProduct),
            totalProduct = response.offeringProductList.totalProduct
        )
    }

    private fun OfferProductListResponse.ResponseHeader.toResponseHeaderModel(): OfferingProductListUiModel.ResponseHeader {
        return OfferingProductListUiModel.ResponseHeader(
            success,
            errorMessage
        )
    }

    private fun List<OfferProductListResponse.Product>.toProductListModel(totalProduct: Int): List<OfferingProductListUiModel.Product> {
        return map {
            OfferingProductListUiModel.Product(
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
                minOrder = it.minOrder,
                campaign = it.campaign.toCampaignModel(),
                labelGroup = it.labelGroup.toLabelGroup(),
                totalProduct = totalProduct
            )
        }
    }

    private fun OfferProductListResponse.Product.Campaign.toCampaignModel(): OfferingProductListUiModel.Product.Campaign {
        return OfferingProductListUiModel.Product.Campaign(
            name,
            originalPrice,
            discountedPrice,
            discountedPercentage,
            customStock
        )
    }

    private fun List<OfferProductListResponse.Product.LabelGroup>.toLabelGroup(): List<OfferingProductListUiModel.Product.LabelGroup> {
        return map {
            OfferingProductListUiModel.Product.LabelGroup(
                position = it.position,
                title = it.title,
                type = it.type,
                url = it.url
            )
        }
    }
}
