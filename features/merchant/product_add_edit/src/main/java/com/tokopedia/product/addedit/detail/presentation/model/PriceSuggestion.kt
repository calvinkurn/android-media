package com.tokopedia.product.addedit.detail.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceSuggestion(
        val suggestedPrice: Double? = 0.0,
        val suggestedPriceMin: Double? = 0.0,
        val suggestedPriceMax: Double? = 0.0,
        val similarProducts: List<SimilarProduct> = listOf()
) : Parcelable

@Parcelize
data class SimilarProduct(
        val productId: String? = "",
        val displayPrice: Double? = 0.0,
        val imageURL: String? = "",
        val title: String? = "",
        val totalSold: Int? = 0,
        val rating: Double? = 0.0
) : Parcelable
