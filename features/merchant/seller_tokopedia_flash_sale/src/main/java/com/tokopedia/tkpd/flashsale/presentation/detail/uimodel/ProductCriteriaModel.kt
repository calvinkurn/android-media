package com.tokopedia.tkpd.flashsale.presentation.detail.uimodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductCriteriaModel (
    val categorySelectionsText: String = "",
    val allStringCategory: String = "",
    val productSelectionsText: String = "",
    val originalPriceRange: ValueRange = ValueRange(0,0),
    val discountedPriceRange: ValueRange = ValueRange(0,0),
    val minDiscount: Double = Double.NaN,
    val stockCampaignRange: ValueRange = ValueRange(0,0),
    val minRating: Double = Double.NaN,
    val minScore: Long = 0,
    val productSold: ValueRange = ValueRange(0,0),
    val minSold: Long = 0,
    val maxProductSubmission: Long = 0,
    val maxShownCount: Long = 0,
    val maxShownDays: Long = 0,
    val showFullCategories: Boolean = false,
    val otherCriteria: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val matchedProductCount: Long = 0,
    val categoriesCount: Long = 0
) : Parcelable {
    @Parcelize
    data class ValueRange (
        val min: Long = 0,
        val max: Long = 0
    ) : Parcelable
}
