package com.tokopedia.play.broadcaster.ui.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.broadcaster.domain.model.GetProductsByEtalaseResponse
import com.tokopedia.play.broadcaster.domain.model.campaign.GetCampaignListResponse
import com.tokopedia.play.broadcaster.domain.model.campaign.GetCampaignProductResponse
import com.tokopedia.play.broadcaster.domain.model.campaign.GetProductTagSummarySectionResponse
import com.tokopedia.play.broadcaster.domain.model.socket.SectionedProductTagSocketResponse
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.type.OriginalPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatus
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignStatusUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.CampaignUiModel
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.etalase.EtalaseUiModel
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
import com.tokopedia.play.broadcaster.ui.model.pinnedproduct.PinProductUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 26/01/22
 */
class PlayBroProductUiMapper @Inject constructor() {

    private val priceFormatSymbol = DecimalFormatSymbols().apply {
        groupingSeparator = '.'
    }
    private val priceFormat = DecimalFormat("Rp ###,###", priceFormatSymbol)

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

    fun mapProductsInEtalase(
        response: GetProductsByEtalaseResponse,
    ): PagedDataUiModel<ProductUiModel> {
        return PagedDataUiModel(
            dataList = response.wrapper.products.map { data ->
                ProductUiModel(
                    id = data.id,
                    name = data.name,
                    imageUrl = data.pictures.firstOrNull()?.urlThumbnail.orEmpty(),
                    stock = data.stock.toLong(),
                    price = OriginalPrice(
                        priceFormat.format(BigDecimal(data.price.min.orZero())),
                        data.price.min.orZero()
                    ),
                    hasCommission = false,
                    commissionFmt = "",
                    commission = 0L,
                    extraCommission = false,
                    pinStatus = PinProductUiModel.Empty,
                    number = "",
                )
            },
            hasNextPage = response.wrapper.pagerCursor.hasNext,
            cursor = response.wrapper.pagerCursor.cursor,
        )
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

    fun mapSectionedProduct(
        response: SectionedProductTagSocketResponse
    ): List<ProductTagSectionUiModel> {
        return response.sections.map {
            ProductTagSectionUiModel(
                name = it.title,
                campaignStatus = mapCampaignStatusFromType(it.type),
                products = it.products.map { product ->
                    ProductUiModel(
                        id = product.id,
                        name = product.name,
                        imageUrl = product.imageUrl,
                        stock = product.quantity,
                        price = if (product.discount <= 0) {
                            OriginalPrice(product.originalPriceFmt, product.originalPrice)
                        } else {
                            DiscountedPrice(
                                originalPrice = product.originalPriceFmt,
                                originalPriceNumber = product.originalPrice,
                                discountPercent = product.discount.toLong(),
                                discountedPrice = product.priceFmt,
                                discountedPriceNumber = product.price,
                            )
                        },
                        pinStatus = getPinStatus(isPinned = product.isPinned, canPin = product.isPinnable),
                        number = product.productNumber.toString(),
                        hasCommission = false,
                        commissionFmt = "",
                        commission = 0L,
                        extraCommission = false,
                    )
                }
            )
        }
    }

    fun mapProductTagSection(
        response: GetProductTagSummarySectionResponse
    ): List<ProductTagSectionUiModel> {
        return response.broadcasterGetProductTagSection.sections.map {
            ProductTagSectionUiModel(
                name = it.name,
                campaignStatus = mapCampaignStatusFromType(it.statusFmt),
                products = it.products.map { product ->
                    ProductUiModel(
                        id = product.productID,
                        name = product.productName,
                        hasCommission = product.hasCommission,
                        commissionFmt = product.commissionFmt,
                        commission = product.commission,
                        extraCommission = product.extraCommission,
                        imageUrl = product.imageURL,
                        stock = product.quantity.toLong(),
                        price = if(product.discount == "0") {
                            OriginalPrice(product.originalPriceFmt, product.originalPrice.toDouble())
                        }
                        else DiscountedPrice(
                            originalPrice = product.originalPriceFmt,
                            originalPriceNumber = product.originalPrice.toDouble(),
                            discountPercent = product.discount.toLong(),
                            discountedPrice = product.priceFmt,
                            discountedPriceNumber = product.price.toDouble(),
                        ),
                        pinStatus = getPinStatus(isPinned = product.isPinned, canPin = product.isPinnable),
                        number = product.productNumber.toString(),
                    )
                }
            )
        }
    }

    private fun mapCampaignStatusFromType(type: String): CampaignStatus {
        return when(type.lowercase()) {
            "mendatang", "upcoming" -> CampaignStatus.Ready
            "berlangsung", "active" -> CampaignStatus.Ongoing
            else -> CampaignStatus.Unknown
        }
    }

    private fun Long.forceToUTCWithoutTimezone(): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date(this)
        calendar.add(Calendar.MILLISECOND, TimeZone.getDefault().rawOffset * -1)
        return calendar.time
    }

    /**
     * Pinned Product
     * isPinnable -> not eligible to pin product-> hide pin container [case: out of stock]
     * isPinned -> show [status: Lepas / Pin]
     */
    private fun getPinStatus(isPinned: Boolean, canPin: Boolean): PinProductUiModel =
        PinProductUiModel(isPinned = isPinned, canPin = if (isPinned) true else canPin)
}
