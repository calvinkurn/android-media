package com.tokopedia.tkpd.flashsale.presentation.detail.helper

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.ProductCriteriaModel

object ProductCriteriaHelper {
    private const val MINIMUM_CATEGORIES_COUNT = 2
    private const val PLUS_SYMBOL = "+"
    private const val OTHER_LABEL = "lainnya"

    fun getCriteriaData(flashSale: FlashSale): MutableList<ProductCriteriaModel> {
        val productCriteriaData: MutableList<ProductCriteriaModel> = mutableListOf()
        flashSale.productCriteria.forEach { productCriteria ->
            productCriteriaData.add(
                ProductCriteriaModel(
                    generateCategorySelectionText(productCriteria.categories),
                    generateStringListCategory(productCriteria.categories),
                    "",
                    ProductCriteriaModel.ValueRange(
                        productCriteria.minPrice.toLong(),
                        productCriteria.maxPrice.toLong()
                    ),
                    ProductCriteriaModel.ValueRange(
                        productCriteria.minFinalPrice.toLong(),
                        productCriteria.maxFinalPrice.toLong()
                    ),
                    productCriteria.minDiscount.toDouble(),
                    ProductCriteriaModel.ValueRange(
                        productCriteria.minCustomStock.toLong(),
                        productCriteria.maxCustomStock.toLong()
                    ),
                    productCriteria.minRating.toDouble(),
                    productCriteria.minProductScore.toLong(),
                    ProductCriteriaModel.ValueRange(
                        productCriteria.minQuantitySold.toLong(),
                        productCriteria.maxQuantitySold.toLong()
                    ),
                    productCriteria.minQuantitySold.toLong(),
                    productCriteria.maxSubmission.toLong(),
                    productCriteria.maxProductAppear.toLong(),
                    productCriteria.dayPeriodTimeAppear.toLong(),
                    showFullCategories = productCriteria.categories.size > MINIMUM_CATEGORIES_COUNT,
                    matchedProductCount = productCriteria.additionalInfo.matchedProduct,
                    categoriesCount = productCriteria.categories.size.toLong()
                )
            )
        }

        return productCriteriaData
    }

    private fun generateCategorySelectionText(categories: List<FlashSale.ProductCategories>): String {
        val stringCategories = mutableListOf<String>()
        categories.forEach { category ->
            stringCategories.add(category.categoryName)
        }
        return if (stringCategories.size <= MINIMUM_CATEGORIES_COUNT) {
            stringCategories.joinToString()
        } else {
            val remainingCategoriesCount = categories.size - Int.ONE
            stringCategories.firstOrNull() + (", $PLUS_SYMBOL $remainingCategoriesCount $OTHER_LABEL")
        }
    }

    private fun generateStringListCategory(categories: List<FlashSale.ProductCategories>): String {
        val stringCategories = mutableListOf<String>()
        categories.forEach { category ->
            stringCategories.add(category.categoryName)
        }
        return stringCategories.joinToString()
    }
}