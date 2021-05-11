package com.tkpd.atc_variant.util

import com.tkpd.atc_variant.data.uidata.ProductHeaderData
import com.tkpd.atc_variant.data.uidata.VariantComponentDataModel
import com.tkpd.atc_variant.data.uidata.VariantHeaderDataModel
import com.tkpd.atc_variant.data.uidata.VariantQuantityDataModel
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregator
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

/**
 * Created by Yehezkiel on 11/05/21
 */
object AtcCommonMapper {

    fun mapToVisitable(productId: String,
                       isTokoNow: Boolean,
                       initialSelectedVariant: MutableMap<String, String>,
                       data: ProductVariantAggregator,
                       processedVariant: List<VariantCategory>?): List<AtcVariantVisitable>? {
        if (processedVariant == null) return null

        var idCounter = 0L
        val result: MutableList<AtcVariantVisitable> = mutableListOf()

        val selectedChild = data.variantData.getChildByProductId(productId)
        val headerData = generateHeaderDataModel(selectedChild)
        result.add(
                VariantHeaderDataModel(
                        position = idCounter,
                        productImage = headerData.first,
                        headerData = headerData.second)
        ).also {
            idCounter += 1
        }

        result.add(
                VariantComponentDataModel(
                        position = idCounter,
                        listOfVariantCategory = processedVariant,
                        mapOfSelectedVariant = initialSelectedVariant)
        ).also {
            idCounter += 1
        }

        if (isTokoNow) {
            result.add(
                    VariantQuantityDataModel(
                            position = idCounter,
                            productId = productId)
            ).also {
                idCounter += 1
            }
        }

        return result
    }

    fun generateHeaderDataModel(selectedChild: VariantChild?): Pair<String, ProductHeaderData> {
        val productImage = selectedChild?.picture?.original ?: ""
        val headerData = ProductHeaderData(
                productMainPrice = selectedChild?.price?.getCurrencyFormatted()
                        ?: "",
                productDiscountedPercentage = selectedChild?.campaign?.discountedPercentage?.toInt()
                        ?: 0,
                productCampaignIdentifier = selectedChild?.campaign?.campaignIdentifier
                        ?: 0,
                productSlashPrice = selectedChild?.campaign?.discountedPriceFmt
                        ?: "",
                productStockWording = selectedChild?.stock?.stockWordingHTML
                        ?: ""
        )
        return productImage to headerData
    }

    fun <T : Any> T.asSuccess(): Success<T> = Success(this)
    fun Throwable.asFail(): Fail = Fail(this)

}