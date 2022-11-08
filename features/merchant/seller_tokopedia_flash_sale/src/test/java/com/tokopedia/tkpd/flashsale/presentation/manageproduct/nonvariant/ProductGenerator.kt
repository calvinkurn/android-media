package com.tokopedia.tkpd.flashsale.presentation.manageproduct.nonvariant

import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult.*
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.ProductCriteria
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Price
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct.Product.Warehouse.DiscountSetup
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult

object ProductGenerator {

    fun generateWarehouse(
        name: String = "Jakarta Pusat",
        isDilayaniTokopedia: Boolean = false,
        isEligible: Boolean = true,
        isToggleOn: Boolean = false,
        isInputValid: Boolean = true
    ): Warehouse = Warehouse(
        warehouseId = if (isDilayaniTokopedia) 12181276 else 12214894,
        name = name.ifBlank { if (isDilayaniTokopedia) "Jakarta Barat" else "Jakarta Selatan" },
        stock = if (isEligible) 100 else 0,
        price = 10000,
        discountSetup = if (isInputValid) generateDiscountSetup() else DiscountSetup(
            price = 7500,
            stock = 25,
            discount = 2500
        ),
        isDilayaniTokopedia = isDilayaniTokopedia,
        isToggleOn = isToggleOn,
        isDisabled = !isEligible,
        disabledReason = if (!isEligible) "stock produk tidak cukup" else ""
    )

    fun generateMultiWarehouse(isPartialEligible: Boolean = false, isInputValid: Boolean = true) =
        if (isPartialEligible) listOf(
            generateWarehouse(
                name = "Jakarta Barat",
                isDilayaniTokopedia = true,
                isEligible = false,
                isInputValid = isInputValid
            ),
            generateWarehouse(
                name = "Jakarta Pusat",
                isDilayaniTokopedia = false,
                isEligible = true,
                isInputValid = isInputValid
            ),
            generateWarehouse(
                name = "Jakarta Selatan",
                isDilayaniTokopedia = false,
                isEligible = true,
                isInputValid = isInputValid
            ),
        ) else listOf(
            generateWarehouse(
                name = "Jakarta Barat",
                isDilayaniTokopedia = true,
                isEligible = true,
                isToggleOn = true,
                isInputValid = isInputValid
            ),
            generateWarehouse(
                name = "Jakarta Pusat",
                isDilayaniTokopedia = false,
                isEligible = true,
                isToggleOn = true,
                isInputValid = isInputValid
            ),
            generateWarehouse(
                name = "Jakarta Selatan",
                isDilayaniTokopedia = false,
                isEligible = true,
                isToggleOn = true,
                isInputValid = isInputValid
            ),
        )

    // Non-Variant Product
    fun createNonVariantProduct(
        name: String = "Judul Produk Bisa Sepanjang Dua Baris Kebawah",
        isMultiLocation: Boolean = false,
        isPartialEligible: Boolean = false,
        isInputValid: Boolean = true
    ) = Product(
        childProducts = listOf(),
        isMultiWarehouse = isMultiLocation,
        isParentProduct = true,
        name = name,
        picture = "https://placekitten.com/100/100",
        price = Price(
            price = 5000,
            lowerPrice = 4000,
            upperPrice = 6000
        ),
        productCriteria = if (isInputValid) generateCriteria() else ProductCriteria(
            criteriaId = 10507,
            maxCustomStock = 100,
            maxDiscount = 70,
            maxFinalPrice = 6000,
            minCustomStock = 11,
            minDiscount = 10,
            minFinalPrice = 2000
        ),
        productId = 2802620824,
        sku = "SK-0918",
        stock = 30,
        url = "",
        warehouses = if (isMultiLocation) {
            generateMultiWarehouse(
                isPartialEligible = isPartialEligible,
                isInputValid = isInputValid
            )
        } else listOf(
            generateWarehouse(
                isToggleOn = true,
                isInputValid = isInputValid
            )
        )
    )


    fun generateCriteria(
        minCustomStock: Int = 1,
        maxCustomStock: Int = 100,
        minDiscount: Long = 30,
        maxDiscount: Long = 70,
        minFinalPrice: Long = 1000,
        maxFinalPrice: Long = 100000
    ) = ProductCriteria(
        criteriaId = 10507,
        minCustomStock = minCustomStock,
        maxCustomStock = maxCustomStock,
        minDiscount = minDiscount,
        maxDiscount = maxDiscount,
        minFinalPrice = minFinalPrice,
        maxFinalPrice = maxFinalPrice
    )

    fun generateDiscountSetup(
        discount: Int = 70,
        price: Long = 100000,
        stock: Long = 100
    ) = DiscountSetup(
        discount = discount,
        price = price,
        stock = stock
    )

    fun generateValidationResult(
        isPriceError: Boolean = false,
        isPricePercentError: Boolean = false,
        isStockError: Boolean = false,
        priceMessage: String = "",
        pricePercentMessage: String = ""
    ) = ValidationResult(
        isPriceError = isPriceError,
        isPricePercentError = isPricePercentError,
        isStockError = isStockError,
        priceMessage = priceMessage,
        pricePercentMessage = pricePercentMessage
    )

    fun generatePriceCheck(
        isEligible: Boolean = true,
        min: Long = 1000,
        max: Long = 100000,
    ) = PriceCheckingResult(
        isEligible = isEligible,
        min = min,
        max = max
    )

    fun generateStockCheck(
        isEligible: Boolean = true,
        min: Long = 10,
        max: Long = 100,
    ) = StockCheckingResult(
        isEligible = isEligible,
        min = min,
        max = max
    )

    fun generateLocationCheck(
        cityName: String = "Jakarta Pusat",
        isDilayaniTokopedia: Boolean = false,
        isPriceEligible: Boolean = true,
        isStockEligible: Boolean = true
    ) = LocationCheckingResult(
        cityName = cityName,
        isDilayaniTokopedia = isDilayaniTokopedia,
        priceCheckingResult = generatePriceCheck(isEligible = isPriceEligible),
        stockCheckingResult = generateStockCheck(isEligible = isStockEligible)
    )

    fun generateLocationCriteria(
        name: String = "Judul Produk Bisa Sepanjang Dua Baris Kebawah",
        isPartialEligible: Boolean = false,
    ) = CriteriaCheckingResult(
        name = name,
        imageUrl = "https://placekitten.com/100/100",
        categoryResult = CategoryResult(),
        ratingResult = RatingResult(),
        countSoldResult = CountSoldResult(),
        minOrderCheckingResult = MinOrderCheckingResult(),
        maxAppearanceCheckingResult = MaxAppearanceCheckingResult(),
        priceCheckingResult = generatePriceCheck(isEligible = !isPartialEligible),
        stockCheckingResult = generateStockCheck(isEligible = !isPartialEligible),
        scoreCheckingResult = ScoreCheckingResult(),
        includeFreeOngkirCheckingResult = OtherCriteriaCheckingResult(),
        includeWholesaleCheckingResult = OtherCriteriaCheckingResult(),
        includePreOrderCheckingResult = OtherCriteriaCheckingResult(),
        includeSecondHandCheckingResult = OtherCriteriaCheckingResult(),
        isMultiloc = true,
        locationResult = if (isPartialEligible) {
            listOf(
                generateLocationCheck(
                    cityName = "Jakarta Pusat",
                    isDilayaniTokopedia = false,
                    isPriceEligible = false
                ),
                generateLocationCheck(
                    cityName = "Jakarta Selatan",
                    isDilayaniTokopedia = false,
                    isStockEligible = false
                ),
                generateLocationCheck(cityName = "Jakarta Barat", isDilayaniTokopedia = true)
            )
        } else {
            listOf(
                generateLocationCheck(cityName = "Jakarta Pusat", isDilayaniTokopedia = false),
                generateLocationCheck(cityName = "Jakarta Selatan", isDilayaniTokopedia = false),
                generateLocationCheck(cityName = "Jakarta Barat", isDilayaniTokopedia = true)
            )
        }
    )

}
