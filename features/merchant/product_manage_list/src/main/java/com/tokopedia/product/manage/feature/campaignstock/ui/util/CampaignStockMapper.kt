package com.tokopedia.product.manage.feature.campaignstock.ui.util

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageAccess
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationDetailReserve
import com.tokopedia.product.manage.feature.campaignstock.domain.model.response.GetStockAllocationDetailSellable
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.data.mapper.ProductManageVariantMapper.mapVariantCampaignTypeToProduct
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.VariantReservedEventInfoUiModel
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.utils.time.DateFormatUtils

object CampaignStockMapper {

    private const val RESERVED_INFO_DATE_FORMAT = "dd MMM yy, HH:ss"
    private const val DIGITS_TO_SECONDS = 9
    private const val MULTIPLIERS_FROM_MILLIS_TO_NANOS = 1000000

    fun mapToParcellableSellableProduct(sellableList: List<GetStockAllocationDetailSellable>,
                                        productVariantList: List<ProductVariant>): ArrayList<SellableStockProductUIModel> {
        val sellableSequence = sellableList.asSequence()
        val productVariantSequence = productVariantList.asSequence().apply {
            sortedWith(compareBy {
                it.id.toLongOrZero()
            })
        }
        val isAllStockEmpty = sellableList.all { it.stock.toIntOrZero() == 0 }
        val sellableProducts = sellableSequence
            .filter { sellable ->
                productVariantSequence.any { product -> product.id == sellable.productId }
            }
            .sortedWith(compareBy {
                it.productId.toLongOrZero()
            })
            .zip(productVariantSequence) { sellable, variant ->
                SellableStockProductUIModel(
                    productId = sellable.productId,
                    productName = sellable.productName,
                    stock = sellable.stock,
                    maxStock = variant.maxStock,
                    isActive = variant.status == ProductStatus.ACTIVE,
                    isAllStockEmpty = isAllStockEmpty,
                    access = variant.access,
                    isCampaign = variant.isCampaign && !sellable.campaignTypeList.isNullOrEmpty(),
                    campaignTypeList = mapVariantCampaignTypeToProduct(
                        sellable.campaignTypeList
                    )
                )
            }.toList()
        return ArrayList(sellableProducts)
    }


    fun mapToParcellableReserved(dataModel: GetStockAllocationDetailReserve,
                                 reservedStock: String? = null): ReservedEventInfoUiModel =
            with(dataModel.eventInfo) {
                ReservedEventInfoUiModel(
                    eventType = eventType,
                    campaignName = eventName,
                    campaignIconUrl = campaignType.iconUrl,
                    startTime = getPeriodString(startTimeNanos),
                    endTime = getPeriodString(endTimeNanos),
                    periodStatus = getPeriodStatus(startTimeNanos),
                    stock = reservedStock ?: stock
                )
            }


    fun mapToVariantReserved(dataModels: List<GetStockAllocationDetailReserve>): List<VariantReservedEventInfoUiModel> {
        val variantInfoList = mutableListOf<VariantReservedEventInfoUiModel>()
        dataModels.forEach { detail ->
            detail.eventInfo.product.forEach { product ->
                if (variantInfoList.find { it.variantId == product.productId } == null) {
                    variantInfoList.add(
                        VariantReservedEventInfoUiModel(
                            variantId = product.productId,
                            variantName = product.productName,
                            totalCampaign = Int.ONE,
                            totalStock = product.stock.toIntOrZero(),
                            reservedEventInfos = mutableListOf(mapToParcellableReserved(detail, product.stock))
                        )
                    )
                } else {
                    variantInfoList.indexOfFirst { it.variantId == product.productId }.takeIf { it > -Int.ONE }?.let { updatedIndex ->
                        val currentInfo = variantInfoList[updatedIndex]
                        val updatedInfo = currentInfo.copy(
                            totalCampaign = currentInfo.totalCampaign + Int.ONE,
                            totalStock = currentInfo.totalStock + product.stock.toIntOrZero(),
                            reservedEventInfos = currentInfo.reservedEventInfos.apply {
                                add(mapToParcellableReserved(detail, product.stock))
                            }
                        )
                        variantInfoList.set(updatedIndex, updatedInfo)
                    }
                }
            }
        }
        return variantInfoList
    }

    fun getSellableProduct(id: String,
                           isActive: Boolean,
                           access: ProductManageAccess,
                           isCampaign: Boolean,
                           maxStock: Int?,
                           sellableList: List<GetStockAllocationDetailSellable>): List<SellableStockProductUIModel> {
        return sellableList
                .filter { it.productId == id }
                .map { sellable ->
                    SellableStockProductUIModel(
                            productId = sellable.productId,
                            productName = sellable.productName,
                            stock = sellable.stock,
                            isActive = isActive,
                            isAllStockEmpty = sellable.stock.toIntOrZero() == 0,
                            access = access,
                            isCampaign = isCampaign,
                            maxStock = maxStock,
                            campaignTypeList = mapVariantCampaignTypeToProduct(sellable.campaignTypeList)
                    )
                }
    }

    private fun getPeriodString(timeNanos: String): String {
        val timeInSecond = convertNanoSecStringToSecString(timeNanos)
        return DateFormatUtils.getFormattedDate(timeInSecond, RESERVED_INFO_DATE_FORMAT)
    }

    private fun getPeriodStatus(startTimeNanosString: String): String {
        val currentTime = getCurrentTimeInNanos()
        val startTimeMillis = startTimeNanosString.toLongOrZero()

        // We only check for start time because there are only upcoming and ongoing status in the requirement
        return if (currentTime < startTimeMillis) {
            ReservedEventInfoUiModel.PeriodStatus.UPCOMING
        } else {
            ReservedEventInfoUiModel.PeriodStatus.ONGOING
        }
    }

    private fun convertNanoSecStringToSecString(nanoSecString: String): String {
        return try {
            nanoSecString.dropLast(DIGITS_TO_SECONDS)
        } catch (ex: Exception) {
            nanoSecString
        }
    }

    private fun getCurrentTimeInNanos(): Long {
        return System.currentTimeMillis() * MULTIPLIERS_FROM_MILLIS_TO_NANOS
    }

}

