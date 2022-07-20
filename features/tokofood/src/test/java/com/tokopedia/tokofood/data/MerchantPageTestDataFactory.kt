package com.tokopedia.tokofood.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProduct
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariant
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariantOption
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariantRules
import com.tokopedia.tokofood.feature.merchant.domain.model.response.*
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel

fun generateTestMerchantData(): GetMerchantDataResponse {
    return GetMerchantDataResponse(
            TokoFoodGetMerchantData(
                    ticker = TokoFoodTickerDetail(),
                    merchantProfile = TokoFoodMerchantProfile(),
                    filters = listOf(),
                    categories = listOf()
            )
    )
}

fun generateTestTokoFoodMerchantProfile(): TokoFoodMerchantProfile {
    return TokoFoodMerchantProfile(
            totalRatingFmt = "totalRatingFmt",
            ratingFmt = "ratingFmt",
            distanceFmt = TokoFoodFmtWarningContent(
                    content = "distanceInformation",
                    isWarning = false
            ),
            etaFmt = TokoFoodFmtWarningContent(
                    content = "etaInformation",
                    isWarning = false
            ),
            opsHourFmt = TokoFoodFmtWarningContent(
                    content = "opsHourInformation",
                    isWarning = false
            )
    )
}

fun generateTestMerchantOpsHour(): List<TokoFoodMerchantOpsHour> {
    val monday = TokoFoodMerchantOpsHour(
            day = "Senin",
            time = "11:00 - 21:45",
            isWarning = false
    )
    val tuesday = TokoFoodMerchantOpsHour(
            day = "Selasa",
            time = "11:00 - 21:45",
            isWarning = false
    )
    val wednesday = TokoFoodMerchantOpsHour(
            day = "Rabu",
            time = "11:00 - 21:45",
            isWarning = false
    )
    val thursday = TokoFoodMerchantOpsHour(
            day = "Kamis",
            time = "11:00 - 21:45",
            isWarning = false
    )
    val friday = TokoFoodMerchantOpsHour(
            day = "Jumat",
            time = "11:00 - 21:45",
            isWarning = false
    )
    val saturday = TokoFoodMerchantOpsHour(
            day = "Sabtu",
            time = "11:00 - 21:45",
            isWarning = false
    )
    val sunday = TokoFoodMerchantOpsHour(
            day = "Minggu",
            time = "11:00 - 21:45",
            isWarning = false
    )
    return listOf(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
}

fun generateTestFoodCategories(): List<TokoFoodCategoryCatalog> {
    val kickOffCategory = TokoFoodCategoryCatalog(
            id = "244f0661-515b-45ed-8969-c9f41fad2979",
            key = "",
            categoryName = "Kick Off",
            catalogs = generateTestTokoFoodCatalogDetails()
    )
    return listOf(kickOffCategory)
}

fun generateTestTokoFoodCatalogDetails(): List<TokoFoodCatalogDetail> {
    val garlicKnots = TokoFoodCatalogDetail(
            id = "bf3eba99-534d-4344-9cf8-6a46326feae0",
            name = "Home-plate Garlic Knots",
            description = "What\u0027s better than garlic bread? 5 pcs of Garlic tangled in knots!",
            imageURL = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e561a5c-55ef-47f6-8b6a-967ca35d229e_master-menu-item-image_1612931947827.jpg",
            price = 38000.0,
            priceFmt = "Rp38.000",
            slashPrice = 0.0,
            slashPriceFmt = "",
            isOutOfStock = false,
            variants = listOf()
    )
    val battingUpChicken = TokoFoodCatalogDetail(
            id = "8829ce4a-6f00-4406-8112-721f569f0d4b",
            name = "Batting up chicken",
            description = "4 pcs of deep-fried breaded chicken tenders served with sauce on the side",
            imageURL = "https =//i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/27cb56de-1c92-4ca6-b797-f98777351524_master-menu-item-image_1612932163220.jpg",
            price = 55000.0,
            priceFmt = "Rp55.000",
            slashPrice = 0.0,
            slashPriceFmt = "",
            isOutOfStock = false,
            variants = generateTestTokoFoodCatalogVariantDetails()
    )
    return listOf(garlicKnots, battingUpChicken)
}

fun generateTestTokoFoodCatalogVariantDetails(): List<TokoFoodCatalogVariantDetail> {
    // single mandatory selection
    val singleRequiredVariant = TokoFoodCatalogVariantDetail(
            id = "d105b801-75de-4306-93a6-cc7124193042",
            name = "Spicy",
            options = generateTestTokoFoodCatalogVariantOptionDetails(),
            isRequired = true,
            maxQty = 1,
            minQty = 1
    )
    // single optional selection
    val singleOptionalVariant = TokoFoodCatalogVariantDetail(
            id = "d105b801-75de-4306-93a6-cc7124193042",
            name = "Spicy",
            options = generateTestTokoFoodCatalogVariantOptionDetails(),
            isRequired = false,
            maxQty = 1,
            minQty = 0
    )
    // multiple optional selection
    val multipleOptionalVariant = TokoFoodCatalogVariantDetail(
            id = "d105b801-75de-4306-93a6-cc7124193042",
            name = "Spicy",
            options = generateTestTokoFoodCatalogVariantOptionDetails(),
            isRequired = false,
            maxQty = 2,
            minQty = 0
    )
    return listOf(singleRequiredVariant, singleOptionalVariant, multipleOptionalVariant)
}

fun generateTestTokoFoodCatalogVariantOptionDetails(): List<TokoFoodCatalogVariantOptionDetail> {
    val original = TokoFoodCatalogVariantOptionDetail(
            id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
            name = "Original",
            price = 0.0,
            priceFmt = "Gratis",
            status = 1
    )
    val hot = TokoFoodCatalogVariantOptionDetail(
            id = "8af415a2-3406-4536-b2b6-0561f7b68148",
            name = "Hot",
            price = 0.0,
            priceFmt = "Gratis",
            status = 1
    )
    return listOf(original, hot)
}

fun generateTestCheckoutTokoFoodProduct(): List<CheckoutTokoFoodProduct> {
    val garlicKnots = CheckoutTokoFoodProduct(
            cartId = "cartId-garlicKnots",
            productId = "bf3eba99-534d-4344-9cf8-6a46326feae0",
            categoryId = "244f0661-515b-45ed-8969-c9f41fad2979",
            productName = "Home-plate Garlic Knots",
            description = "What\u0027s better than garlic bread? 5 pcs of Garlic tangled in knots!",
            imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e561a5c-55ef-47f6-8b6a-967ca35d229e_master-menu-item-image_1612931947827.jpg",
            price = 38000.0,
            priceFmt = "Rp38.000",
            originalPrice = 38000.0,
            originalPriceFmt = "Rp38.000",
            discountPercentage = "",
            notes = "",
            quantity = 1,
            variants = listOf()
    )
    val originalVariantOption = CheckoutTokoFoodProductVariantOption(
            isSelected = true,
            optionId = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
            name = "Original",
            price = 0.0,
            priceFmt = "Gratis",
            status = 1
    )
    val originalVariant = CheckoutTokoFoodProductVariant(
            variantId = "d105b801-75de-4306-93a6-cc7124193042",
            name = "Spicy",
            rules = CheckoutTokoFoodProductVariantRules(),
            options = listOf(originalVariantOption)
    )
    val battingUpChickenOriginal = CheckoutTokoFoodProduct(
            cartId = "cartId-battingUpChicken",
            productId = "8829ce4a-6f00-4406-8112-721f569f0d4b",
            categoryId = "244f0661-515b-45ed-8969-c9f41fad2979",
            productName = "Batting up chicken",
            description = "4 pcs of deep-fried breaded chicken tenders served with sauce on the side",
            imageUrl = "https =//i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/27cb56de-1c92-4ca6-b797-f98777351524_master-menu-item-image_1612932163220.jpg",
            price = 55000.0,
            priceFmt = "Rp55.000",
            originalPrice = 55000.0,
            originalPriceFmt = "Rp55.000",
            discountPercentage = "",
            notes = "",
            quantity = 1,
            variants = listOf(originalVariant)
    )
    return listOf(garlicKnots, battingUpChickenOriginal)
}

fun generateTestProductUiModel(): ProductUiModel {
    return ProductUiModel(
            cartId = "cartId-garlicKnots",
            isAtc = true,
            id = "bf3eba99-534d-4344-9cf8-6a46326feae0",
            name = "Home-plate Garlic Knots",
            description = "What\u0027s better than garlic bread? 5 pcs of Garlic tangled in knots!",
            imageURL = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e561a5c-55ef-47f6-8b6a-967ca35d229e_master-menu-item-image_1612931947827.jpg",
            price = 38000.0,
            priceFmt = "Rp38.000",
            slashPrice = 0.0,
            slashPriceFmt = "",
            isOutOfStock = false,
            isShopClosed = false,
            customListItems = listOf()
    )
}

fun generateTestCustomOrderDetail(): CustomOrderDetail {
    return CustomOrderDetail(
            cartId = "cartId-garlicKnots",
            subTotal = 0.0,
            subTotalFmt = "",
            qty = 1,
            customListItems = listOf()
    )
}