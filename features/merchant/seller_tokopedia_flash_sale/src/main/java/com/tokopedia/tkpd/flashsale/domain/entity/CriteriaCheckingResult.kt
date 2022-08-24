package com.tokopedia.tkpd.flashsale.domain.entity

data class CriteriaCheckingResult(
    val name: String = "",
    val imageUrl: String = "",
    val categoryResult: CategoryResult = CategoryResult(),
    val ratingResult: RatingResult = RatingResult(),
    val productScoreResult: ProductScoreResult = ProductScoreResult(),
    val countSoldResult: CountSoldResult = CountSoldResult(),
    val minOrderCheckingResult: MinOrderCheckingResult = MinOrderCheckingResult(),
    val maxAppearanceCheckingResult: MaxAppearanceCheckingResult = MaxAppearanceCheckingResult(),
    val priceCheckingResult: PriceCheckingResult = PriceCheckingResult(),
    val stockCheckingResult: StockCheckingResult = StockCheckingResult(),
    val scoreCheckingResult: ScoreCheckingResult = ScoreCheckingResult(),
    val freeOngkirCheckingResult: FreeOngkirCheckingResult = FreeOngkirCheckingResult(),
    val excludePreOrderCheckingResult: ExcludePreOrderCheckingResult = ExcludePreOrderCheckingResult(),
    val excludeSecondHandCheckingResult: ExcludeSecondHandCheckingResult = ExcludeSecondHandCheckingResult(),
    val locationResult: List<LocationCheckingResult> = emptyList()
) {

    data class CategoryResult (
        val isEligible: Boolean = false,
        val name: String = ""
    )

    data class RatingResult (
        val isEligible: Boolean = false,
        val min: Long = 0
    )

    data class ProductScoreResult (
        val isEligible: Boolean = false,
        val min: Long = 0
    )

    data class CountSoldResult (
        val isEligible: Boolean = false,
        val min: Long = 0,
        val max: Long = 0
    )

    data class MinOrderCheckingResult (
        val isEligible: Boolean = false,
        val min: Long = 0
    )

    data class MaxAppearanceCheckingResult (
        val isEligible: Boolean = false,
        val max: Long = 0
    )

    data class PriceCheckingResult (
        val isEligible: Boolean = false,
        val min: Long = 0,
        val max: Long = 0,
    )

    data class StockCheckingResult (
        val isEligible: Boolean = false,
        val min: Long = 0
    )

    data class ScoreCheckingResult (
        val isEligible: Boolean = false
    )

    data class FreeOngkirCheckingResult (
        val isEligible: Boolean = false,
        val isActive: Boolean = false
    )

    data class ExcludePreOrderCheckingResult (
        val isEligible: Boolean = false,
        val isActive: Boolean = false
    )

    data class ExcludeSecondHandCheckingResult (
        val isEligible: Boolean = false,
        val isActive: Boolean = false
    )

    data class LocationCheckingResult (
        val cityName: String = "",
        val isDilayaniTokopedia: Boolean = false,
        val priceCheckingResult: PriceCheckingResult = PriceCheckingResult(),
        val stockCheckingResult: StockCheckingResult = StockCheckingResult(),
    )
}