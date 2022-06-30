package com.tokopedia.shop.flashsale.common.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.extension.convertRupiah
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductErrorType
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductErrorType.*
import com.tokopedia.shop.flashsale.domain.entity.enums.ProductInputValidationResult
import javax.inject.Inject

class ProductErrorStatusHandler @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val MIN_CAMPAIGN_STOCK = 1
        private const val MIN_CAMPAIGN_ORDER = 1L
        private const val MAX_CAMPAIGN_DISCOUNT_PERCENTAGE = 0.99
        private const val MIN_CAMPAIGN_DISCOUNT_PERCENTAGE = 0.01
    }

    fun getErrorType(productMapData: SellerCampaignProductList.ProductMapData): ManageProductErrorType {
        val minDiscountedPrice = getProductMinDiscountedPrice(productMapData.originalPrice)
        return when {
            productMapData.discountedPrice > productMapData.originalPrice -> {
                return when {
                    productMapData.customStock > productMapData.originalStock -> MAX_DISCOUNT_PRICE_AND_OTHER
                    productMapData.discountedPrice < minDiscountedPrice -> MAX_DISCOUNT_PRICE_AND_OTHER
                    productMapData.customStock < MIN_CAMPAIGN_STOCK -> MAX_DISCOUNT_PRICE_AND_OTHER
                    productMapData.maxOrder > productMapData.customStock -> MAX_DISCOUNT_PRICE_AND_OTHER
                    else -> MAX_DISCOUNT_PRICE
                }
            }

            productMapData.customStock > productMapData.originalStock -> {
                return when {
                    productMapData.discountedPrice < minDiscountedPrice -> MAX_STOCK_AND_OTHER
                    productMapData.customStock < MIN_CAMPAIGN_STOCK -> MAX_STOCK_AND_OTHER
                    productMapData.maxOrder > productMapData.customStock -> MAX_STOCK_AND_OTHER
                    else -> MAX_STOCK
                }
            }

            productMapData.discountedPrice < minDiscountedPrice -> {
                return when {
                    productMapData.discountedPrice.isMoreThanZero() -> {
                        return when {
                            productMapData.customStock < MIN_CAMPAIGN_STOCK -> MIN_DISCOUNT_PRICE_AND_OTHER
                            productMapData.maxOrder > productMapData.customStock -> MIN_DISCOUNT_PRICE_AND_OTHER
                            else -> MIN_DISCOUNT_PRICE
                        }
                    }
                    else -> NOT_ERROR
                }
            }

            productMapData.customStock < MIN_CAMPAIGN_STOCK -> {
                return when {
                    productMapData.customStock.isMoreThanZero() -> {
                        return when {
                            productMapData.maxOrder > productMapData.customStock -> MIN_STOCK_AND_OTHER
                            else -> MIN_STOCK
                        }
                    }
                    else -> NOT_ERROR
                }
            }

            productMapData.maxOrder > productMapData.customStock -> return MAX_ORDER

            else -> NOT_ERROR
        }
    }

    fun getErrorInputType(productMapData: SellerCampaignProductList.ProductMapData): ProductInputValidationResult {
        val maxDiscountedPrice = getProductMaxDiscountedPrice(productMapData.originalPrice)
        val minDiscountedPrice = getProductMinDiscountedPrice(productMapData.originalPrice)
        val result: MutableList<ManageProductErrorType> = mutableListOf()

        if (productMapData.discountedPrice >= productMapData.originalPrice) result.add(MAX_DISCOUNT_PRICE)
        if (productMapData.customStock > productMapData.originalStock) result.add(MAX_STOCK)
        if (productMapData.discountedPrice < minDiscountedPrice) result.add(MIN_DISCOUNT_PRICE)
        if (productMapData.customStock < MIN_CAMPAIGN_STOCK) result.add(MIN_STOCK)
        if (productMapData.maxOrder > productMapData.customStock) result.add(MAX_ORDER)
        if (productMapData.maxOrder < MIN_CAMPAIGN_ORDER) result.add(MIN_ORDER)

        return ProductInputValidationResult(
            errorList = result,
            maxPrice = maxDiscountedPrice,
            minPrice = minDiscountedPrice,
            maxStock = productMapData.originalStock,
            minStock = MIN_CAMPAIGN_STOCK,
            minOrder = MIN_CAMPAIGN_ORDER,
            maxOrder = productMapData.customStock,
            minPricePercent = DiscountUtil.getPercentLong(MIN_CAMPAIGN_DISCOUNT_PERCENTAGE),
            maxPricePercent = DiscountUtil.getPercentLong(MAX_CAMPAIGN_DISCOUNT_PERCENTAGE)
        )
    }

    fun getProductListErrorMessage(
        errorType: ManageProductErrorType,
        productMapData: SellerCampaignProductList.ProductMapData
    ): String {
        var errorMsg = ""
        val maxDiscountedPriceInCurrency =
            getProductMaxDiscountedPrice(productMapData.originalPrice).convertRupiah()
        val minDiscountedPriceInCurrency =
            getProductMinDiscountedPrice(productMapData.originalPrice).convertRupiah()
        when (errorType) {
            MAX_DISCOUNT_PRICE -> {
                errorMsg = String.format(
                    context.getString(R.string.error_msg_max_discounted_price),
                    maxDiscountedPriceInCurrency
                )
            }
            MAX_DISCOUNT_PRICE_AND_OTHER -> {
                errorMsg = String.format(
                    context.getString(R.string.error_msg_max_discounted_price),
                    maxDiscountedPriceInCurrency
                ) + context.getString(R.string.error_msg_other)
            }
            MAX_STOCK -> {
                errorMsg = String.format(
                    context.getString(R.string.error_msg_max_campaign_stock),
                    productMapData.originalStock
                )
            }
            MAX_STOCK_AND_OTHER -> {
                errorMsg = String.format(
                    context.getString(R.string.error_msg_max_campaign_stock),
                    productMapData.originalStock
                ) + context.getString(R.string.error_msg_other)
            }
            MIN_DISCOUNT_PRICE -> {
                errorMsg = String.format(
                    context.getString(R.string.error_msg_min_discounted_price),
                    minDiscountedPriceInCurrency
                )
            }
            MIN_DISCOUNT_PRICE_AND_OTHER -> {
                errorMsg = String.format(
                    context.getString(R.string.error_msg_min_discounted_price),
                    minDiscountedPriceInCurrency
                ) + context.getString(R.string.error_msg_other)
            }
            MIN_STOCK -> {
                errorMsg = context.getString(R.string.error_msg_min_campaign_stock)
            }
            MIN_STOCK_AND_OTHER -> {
                errorMsg =
                    context.getString(R.string.error_msg_min_campaign_stock) + context.getString(R.string.error_msg_other)
            }
            MAX_ORDER -> {
                errorMsg = context.getString(R.string.error_msg_max_campaign_order)
            }
            else -> {}
        }
        return errorMsg
    }

    private fun getProductMaxDiscountedPrice(originalPrice: Long): Int {
        return (originalPrice * MAX_CAMPAIGN_DISCOUNT_PERCENTAGE).toInt()
    }

    private fun getProductMinDiscountedPrice(originalPrice: Long): Int {
        return (originalPrice * MIN_CAMPAIGN_DISCOUNT_PERCENTAGE).toInt()
    }
}