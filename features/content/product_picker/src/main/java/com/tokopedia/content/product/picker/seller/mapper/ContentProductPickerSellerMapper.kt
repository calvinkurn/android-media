package com.tokopedia.content.product.picker.seller.mapper

import com.tokopedia.content.product.picker.seller.domain.model.campaign.GetCampaignListResponse
import com.tokopedia.content.product.picker.seller.domain.model.campaign.GetCampaignProductResponse
import com.tokopedia.content.product.picker.seller.model.DiscountedPrice
import com.tokopedia.content.product.picker.seller.model.OriginalPrice
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignStatus
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignStatusUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.seller.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.pinnedproduct.PinProductUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.util.forceToUTCWithoutTimezone
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel

/**
 * Created By : Jonathan Darwin on October 13, 2023
 */
abstract class ContentProductPickerSellerMapper {

    /** Campaign */
    fun mapCampaignList(response: GetCampaignListResponse): List<CampaignUiModel> {
        return response.getSellerCampaignList.campaigns.map {
            CampaignUiModel(
                id = it.campaignId,
                title = it.campaignName,
                imageUrl = it.coverImage,
                startDateFmt = PlayDateTimeFormatter.formatDate(
                    date = (it.startDate * 1000).forceToUTCWithoutTimezone(),
                    outputPattern = PlayDateTimeFormatter.ddMMMyyy_HHmmWIB
                ).orEmpty(),
                endDateFmt = PlayDateTimeFormatter.formatDate(
                    date = (it.endDate * 1000).forceToUTCWithoutTimezone(),
                    outputPattern = PlayDateTimeFormatter.ddMMMyyy_HHmmWIB
                ).orEmpty(),
                status = CampaignStatusUiModel(
                    status = CampaignStatus.getById(it.statusId),
                    text = it.statusText,
                ),
                totalProduct = it.productSummary.totalItem,
            )
        }
    }

    fun mapProductsInCampaign(response: GetCampaignProductResponse): PagedDataUiModel<ProductUiModel> {
        return PagedDataUiModel(
            dataList = response.getCampaignProduct.products.map { data ->
                ProductUiModel(
                    id = if (data.isVariant) data.parentId else data.id.toString(),
                    name = data.name,
                    imageUrl = data.imageUrl,
                    stock = data.campaign.customStock.toLong(),
                    price = if (data.campaign.discountPercentage > 0) {
                        DiscountedPrice(
                            originalPrice = data.campaign.originalPrice,
                            originalPriceNumber = data.campaign.originalPriceFmt.toDoubleOrNull() ?: 0.0,
                            discountPercent = data.campaign.discountPercentage.toLong(),
                            discountedPrice = data.campaign.discountedPrice,
                            discountedPriceNumber = data.campaign.discountedPriceFmt.toDoubleOrNull() ?: 0.0,
                        )
                    } else OriginalPrice(
                        price = data.campaign.originalPrice,
                        priceNumber = data.campaign.originalPriceFmt.toDoubleOrNull() ?: 0.0,
                    ),
                    hasCommission = false,
                    commissionFmt = "",
                    commission = 0L,
                    extraCommission = false,
                    pinStatus = PinProductUiModel.Empty,
                    number = "",
                )
            },
            hasNextPage = response.getCampaignProduct.products.isNotEmpty(),
        )
    }

    /** Etalase */
    fun mapEtalaseList(response: List<ShopEtalaseModel>): List<EtalaseUiModel> {
        return response.map {
            EtalaseUiModel(
                id = it.id,
                imageUrl = it.imageUrl.orEmpty(),
                title = it.name,
                totalProduct = it.count,
            )
        }
    }

    /** Util */
    protected fun mapCampaignStatusFromType(type: String): CampaignStatus {
        return when(type.lowercase()) {
            "mendatang", "upcoming" -> CampaignStatus.Ready
            "berlangsung", "active" -> CampaignStatus.Ongoing
            else -> CampaignStatus.Unknown
        }
    }

    /**
     * Pinned Product
     * isPinnable -> not eligible to pin product-> hide pin container [case: out of stock]
     * isPinned -> show [status: Lepas / Pin]
     */
    protected fun getPinStatus(isPinned: Boolean, canPin: Boolean): PinProductUiModel =
        PinProductUiModel(isPinned = isPinned, canPin = if (isPinned) true else canPin)
}
