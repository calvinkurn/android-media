package com.tkpd.atc_variant.util

import com.tkpd.atc_variant.data.uidata.*
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

/**
 * Created by Yehezkiel on 11/05/21
 */
object AtcCommonMapper {

    fun mapToCartRedirectionData(selectedChild: VariantChild?, cartTypeData: Map<String, CartTypeData>?, isShopOwner: Boolean = false): PartialButtonDataModel {
        return PartialButtonDataModel(selectedChild?.isBuyable
                ?: false, isShopOwner, cartTypeData?.get(selectedChild?.productId ?: ""))
    }

    fun mapToVisitable(selectedChild: VariantChild?,
                       isTokoNow: Boolean,
                       initialSelectedVariant: MutableMap<String, String>,
                       processedVariant: List<VariantCategory>?): List<AtcVariantVisitable>? {
        if (processedVariant == null) return null

        var idCounter = 0L
        val result: MutableList<AtcVariantVisitable> = mutableListOf()

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
                            productId = selectedChild?.productId ?: "")
            ).also {
                idCounter += 1
            }
        }

        return result
    }

    fun updateVisitable(oldList: List<AtcVariantVisitable>,
                        processedVariant: List<VariantCategory>?,
                        isPartiallySelected: Boolean,
                        selectedVariantIds: MutableMap<String, String>?,
                        selectedVariantChild: VariantChild?,
                        variantImage: String,
                        selectedProductFulfillment: Boolean): List<AtcVariantVisitable> {

        return oldList.map {
            when (it) {
                is VariantComponentDataModel -> {
                    it.copy(listOfVariantCategory = processedVariant,
                            mapOfSelectedVariant = selectedVariantIds
                                    ?: mutableMapOf(),
                            stockWording = if (isPartiallySelected) "" else selectedVariantChild?.stock?.stockWordingHTML
                                    ?: "",
                            tokoCabangWording = if(selectedProductFulfillment)"toko cabang" else "")
                }
                is VariantHeaderDataModel -> {
                    if (isPartiallySelected) {
                        //update image only when exist
                        it.copy(productImage = variantImage)
                    } else {
                        val headerData = generateHeaderDataModel(selectedVariantChild)
                        it.copy(productImage = headerData.first, headerData = headerData.second)
                    }
                }
                else -> {
                    it
                }
            }
        }
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
                        ?: "",
                isProductBuyable = selectedChild?.isBuyable ?: false
        )
        return productImage to headerData
    }

    fun <T : Any> T.asSuccess(): Success<T> = Success(this)
    fun Throwable.asFail(): Fail = Fail(this)
}