package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.play.broadcaster.domain.model.campaign.GetCampaignListResponse
import com.tokopedia.play.broadcaster.domain.model.campaign.GetCampaignProductResponse
import com.tokopedia.play.broadcaster.domain.model.product.GetShopProductsResponse
import com.tokopedia.play.broadcaster.domain.model.socket.SectionedProductTagSocketResponse
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatusUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseProductListMap
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.ProductSectionKey
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
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

    fun mapProductsInEtalase(response: GetShopProductsResponse): PagedDataUiModel<ProductUiModel> {
        return PagedDataUiModel(
            dataList = response.response.data.map { data ->
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
            },
            hasNextPage = response.response.links.next.isNotBlank(),
        )
    }

    fun mapProductsInCampaign(response: GetCampaignProductResponse): PagedDataUiModel<ProductUiModel> {
        return PagedDataUiModel(
            dataList = response.getCampaignProduct.products.map { data ->
                ProductUiModel(
                    id = if (data.isVariant) data.parentId else data.id.toString(),
                    name = data.name,
                    imageUrl = data.imageUrl,
                    stock = data.campaign.customStock,
                    price = if (data.campaign.discountPercentage > 0) {
                        DiscountedPrice(
                            originalPrice = data.campaign.originalPrice,
                            originalPriceNumber = data.campaign.originalPriceFmt.toDoubleOrNull() ?: 0.0,
                            discountPercent = data.campaign.discountPercentage.toInt(),
                            discountedPrice = data.campaign.discountedPrice,
                            discountedPriceNumber = data.campaign.discountedPriceFmt.toDoubleOrNull() ?: 0.0,
                        )
                    } else OriginalPrice(
                        price = data.campaign.originalPrice,
                        priceNumber = data.campaign.originalPriceFmt.toDoubleOrNull() ?: 0.0,
                    )
                )
            },
            hasNextPage = response.getCampaignProduct.products.isNotEmpty(),
        )
    }

    fun mapSectionedProduct(response: SectionedProductTagSocketResponse): EtalaseProductListMap {
        return response.sections.associateBy(
            keySelector = { ProductSectionKey(it.title, it.type) },
            valueTransform = {
                it.products.map { product ->
                    ProductUiModel(
                        id = product.id,
                        name = product.name,
                        imageUrl = product.imageUrl,
                        stock = product.quantity.toInt(),
                        price = if (product.discount <= 0) {
                            OriginalPrice(product.originalPriceFmt, product.originalPrice)
                        } else {
                            DiscountedPrice(
                                originalPrice = product.originalPriceFmt,
                                originalPriceNumber = product.originalPrice,
                                discountPercent = product.discount.toInt(),
                                discountedPrice = product.priceFmt,
                                discountedPriceNumber = product.price,
                            )
                        }
                    )
                }
            }
        )
    }

    private fun Long.forceToUTCWithoutTimezone(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date(this)
        calendar.add(Calendar.MILLISECOND, TimeZone.getDefault().rawOffset * -1)
        return calendar.time
    }
}