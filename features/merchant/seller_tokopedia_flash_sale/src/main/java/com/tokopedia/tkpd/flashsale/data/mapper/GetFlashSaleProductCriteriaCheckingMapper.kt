package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleProductCriteriaCheckingResponse
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import javax.inject.Inject

class GetFlashSaleProductCriteriaCheckingMapper @Inject constructor() {

    fun map(response: GetFlashSaleProductCriteriaCheckingResponse) = response.getFlashSaleProductCriteriaChecking.productList.map {
        CriteriaCheckingResult(
            name = it.name,
            imageUrl = it.pictureUrl,
            categoryResult = mapCategory(it.category),
            ratingResult = mapRatingResult(it.rating),
            countSoldResult = mapCountSoldResult(it.countSold),
            minOrderCheckingResult = mapMinOrderCheckingResult(it.minOrder),
            maxAppearanceCheckingResult = mapMaxAppearanceCheckingResult(it.maxAppearance),
            priceCheckingResult = mapPriceCheckingResult(it.warehouses.firstOrNull()?.price ?: GetFlashSaleProductCriteriaCheckingResponse.Price()),
            stockCheckingResult = mapStockCheckingResult(it.warehouses.firstOrNull()?.stock ?: GetFlashSaleProductCriteriaCheckingResponse.Stock()),
            scoreCheckingResult = mapScoreCheckingResult(it.productScore),
            includeFreeOngkirCheckingResult = mapIncludeFreeOngkirCheckingResult(it.freeOngkir),
            includeWholesaleCheckingResult = mapIncludeWholesaleCheckingResult(it.excludeWholesale),
            includePreOrderCheckingResult = mapIncludePreOrderCheckingResult(it.excludePreOrder),
            includeSecondHandCheckingResult = mapIncludeSecondHandCheckingResult(it.excludeSecondHand),
            isMultiloc = it.isMultiwarehouse,
            locationResult = mapLocationResult(it.warehouses)
        )
    }

    private fun mapIncludeSecondHandCheckingResult(
        excludeSecondHand: GetFlashSaleProductCriteriaCheckingResponse.ExcludeSecondHand
    ) = CriteriaCheckingResult.OtherCriteriaCheckingResult (
        isEligible = excludeSecondHand.isEligible,
        isActive = excludeSecondHand.isActive
    )

    private fun mapIncludePreOrderCheckingResult(
        excludePreOrder: GetFlashSaleProductCriteriaCheckingResponse.ExcludePreOrder
    ) = CriteriaCheckingResult.OtherCriteriaCheckingResult (
        isEligible = excludePreOrder.isEligible,
        isActive = excludePreOrder.isActive
    )

    private fun mapIncludeWholesaleCheckingResult(
        excludeWholesale: GetFlashSaleProductCriteriaCheckingResponse.ExcludeWholesale
    ) = CriteriaCheckingResult.OtherCriteriaCheckingResult (
        isEligible = excludeWholesale.isEligible,
        isActive = excludeWholesale.isActive
    )

    private fun mapIncludeFreeOngkirCheckingResult(
        freeOngkir: GetFlashSaleProductCriteriaCheckingResponse.FreeOngkir
    ) = CriteriaCheckingResult.OtherCriteriaCheckingResult (
        isEligible = freeOngkir.isEligible,
        isActive = freeOngkir.isActive
    )

    private fun mapScoreCheckingResult(
        productScore: GetFlashSaleProductCriteriaCheckingResponse.ProductScore
    ) = CriteriaCheckingResult.ScoreCheckingResult (
        isEligible = productScore.isEligible,
        min = productScore.minProductScore
    )

    private fun mapStockCheckingResult(
        stock: GetFlashSaleProductCriteriaCheckingResponse.Stock
    ) =  CriteriaCheckingResult.StockCheckingResult (
        isEligible = stock.isEligible,
        min = stock.minStock
    )

    private fun mapPriceCheckingResult(
        price: GetFlashSaleProductCriteriaCheckingResponse.Price
    ) = CriteriaCheckingResult.PriceCheckingResult (
        isEligible = price.isEligible,
        min = price.minPrice,
        max = price.maxPrice
    )

    private fun mapMaxAppearanceCheckingResult(
        maxAppearance: GetFlashSaleProductCriteriaCheckingResponse.MaxAppearance
    ) = CriteriaCheckingResult.MaxAppearanceCheckingResult (
        isEligible = maxAppearance.isEligible,
        max = maxAppearance.maxAppearance,
        dayPeriod = maxAppearance.dayPeriodeAppearance,
    )

    private fun mapMinOrderCheckingResult(
        minOrder: GetFlashSaleProductCriteriaCheckingResponse.MinOrder
    ) = CriteriaCheckingResult.MinOrderCheckingResult (
        isEligible = minOrder.isEligible,
        min = minOrder.minOrder
    )

    private fun mapCountSoldResult(
        countSold: GetFlashSaleProductCriteriaCheckingResponse.CountSold
    ) = CriteriaCheckingResult.CountSoldResult (
        isEligible = countSold.isEligible,
        min = countSold.minCountSold,
        max = countSold.maxCountSold
    )

    private fun mapRatingResult(
        rating: GetFlashSaleProductCriteriaCheckingResponse.Rating
    ) = CriteriaCheckingResult.RatingResult (
        isEligible = rating.isEligible,
        min = rating.minRating
    )

    private fun mapLocationResult(
        warehouses: List<GetFlashSaleProductCriteriaCheckingResponse.Warehouses>
    ): List<CriteriaCheckingResult.LocationCheckingResult> = warehouses.map {
        CriteriaCheckingResult.LocationCheckingResult(
            cityName = it.cityName,
            isDilayaniTokopedia = it.isDilayaniTokopedia,
            priceCheckingResult = mapPriceCheckingResult(it.price),
            stockCheckingResult = mapStockCheckingResult(it.stock)
        )
    }

    private fun mapCategory(
        category: GetFlashSaleProductCriteriaCheckingResponse.Category
    ) = CriteriaCheckingResult.CategoryResult (
        isEligible = category.isEligible,
        name = category.name
    )

}
