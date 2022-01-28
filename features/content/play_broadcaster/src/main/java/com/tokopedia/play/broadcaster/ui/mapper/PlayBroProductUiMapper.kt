package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.play.broadcaster.domain.model.campaign.GetCampaignListResponse
import com.tokopedia.play.broadcaster.domain.model.product.GetShopProductsResponse
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatusUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductsInEtalaseUiModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductUiMapper @Inject constructor() {

    private val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm 'WIB'", Locale.getDefault())

    fun mapCampaignList(response: GetCampaignListResponse): List<CampaignUiModel> {
        return response.getSellerCampaignList.campaigns.map {
            CampaignUiModel(
                id = it.campaignId,
                title = it.campaignName,
                imageUrl = it.coverImage,
                startDateFmt = sdf.format((it.startDate * 1000).forceToUTCWithoutTimezone()),
                endDateFmt = sdf.format((it.endDate * 1000).forceToUTCWithoutTimezone()),
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

    private fun Long.forceToUTCWithoutTimezone(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date(this)
        calendar.add(Calendar.MILLISECOND, TimeZone.getDefault().rawOffset * -1)
        return calendar.time
    }
}