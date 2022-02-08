package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.play.broadcaster.domain.model.campaign.GetCampaignListResponse
import com.tokopedia.play.broadcaster.domain.model.campaign.GetProductTagSummarySectionResponse
import com.tokopedia.play.broadcaster.domain.model.product.GetShopProductsResponse
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatusUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import java.util.*
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductUiMapper @Inject constructor() {

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

    fun mapProductsInEtalase(response: GetShopProductsResponse): List<ProductUiModel> {
        return response.response.data.map { data ->
            ProductUiModel(
                id = data.productId,
                name = data.name,
                imageUrl = data.primaryImage.thumbnail,
                stock = data.stock,
                price = if (data.campaign.discountedPercentage == "0") {
                    OriginalPrice(data.price.textIdr, 0.0)
                } else DiscountedPrice(
                    originalPrice = data.campaign.originalPriceFmt,
                    originalPriceNumber = 0.0,
                    discountPercent = data.campaign.discountedPercentage.toInt(),
                    discountedPrice = data.campaign.discountedPriceFmt,
                    discountedPriceNumber = 0.0,
                )
            )
        }
    }

    fun mapProductTagSection(response: GetProductTagSummarySectionResponse): List<ProductTagSectionUiModel> {
        return response.section.map { section ->
            ProductTagSectionUiModel(
                name = section.name,
                campaignStatus = when(section.statusFmt) {
                    "mendatang" -> CampaignStatus.Ready
                    "berlangsung" -> CampaignStatus.Ongoing
                    else -> CampaignStatus.Unknown
                },
                products = section.products.map { product ->
                    ProductUiModel(
                        id = product.productID,
                        name = product.productName,
                        imageUrl = product.imageURL,
                        stock = product.quantity,
                        price = if(product.discount == "0") {
                            OriginalPrice(product.priceFmt, product.price.toDouble())
                        }
                        else DiscountedPrice(
                            originalPrice = product.originalPriceFmt,
                            originalPriceNumber = product.originalPrice.toDouble(),
                            discountPercent = product.discount.toInt(),
                            discountedPrice = product.priceFmt,
                            discountedPriceNumber = product.price.toDouble(),
                        )
                    )
                }
            )
        }
    }

    private fun Long.forceToUTCWithoutTimezone(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date(this)
        calendar.add(Calendar.MILLISECOND, TimeZone.getDefault().rawOffset * -1)
        return calendar.time
    }
}