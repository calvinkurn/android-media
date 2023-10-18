package com.tokopedia.tokofood.data

import com.tokopedia.localizationchooseaddress.domain.response.ChosenAddressDataResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCart
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCartCustomResponse
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCartOption
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCartRules
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCartVariant
import com.tokopedia.tokofood.common.domain.response.CartListCartMetadata
import com.tokopedia.tokofood.common.domain.response.CartListCartMetadataVariant
import com.tokopedia.tokofood.common.domain.response.KeroEditAddress
import com.tokopedia.tokofood.common.domain.response.KeroEditAddressData
import com.tokopedia.tokofood.common.domain.response.KeroEditAddressResponse
import com.tokopedia.tokofood.feature.merchant.domain.model.response.GetMerchantDataResponse
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodCatalogDetail
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodCatalogVariantDetail
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodCatalogVariantOptionDetail
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodCategoryCatalog
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodFmtWarningContent
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodGetMerchantData
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodMerchantOpsHour
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodMerchantProfile
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodTickerDetail
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodTopBanner
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.OptionUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel

fun generateTestMerchantData(): GetMerchantDataResponse {
    return GetMerchantDataResponse(
        TokoFoodGetMerchantData(
            ticker = TokoFoodTickerDetail(),
            topBanner = TokoFoodTopBanner(),
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
    val umamiEdamame = TokoFoodCatalogDetail(
        id = "bf3eba99-534d-4344-9cf8-6a46326feae1",
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
    val crispyFrenchFries = TokoFoodCatalogDetail(
        id = "8829ce4a-6f00-4406-8112-721f569f0d4c",
        name = "Crispy French Fries",
        description = "4 pcs of deep-fried breaded chicken tenders served with sauce on the side",
        imageURL = "https =//i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/27cb56de-1c92-4ca6-b797-f98777351524_master-menu-item-image_1612932163220.jpg",
        price = 55000.0,
        priceFmt = "Rp55.000",
        slashPrice = 0.0,
        slashPriceFmt = "",
        isOutOfStock = false,
        variants = generateTestTokoFoodCatalogVariantDetails()
    )
    return listOf(garlicKnots, umamiEdamame, battingUpChicken, crispyFrenchFries)
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
    // single toggle selection
    val secondMultipleOptionalVariant = TokoFoodCatalogVariantDetail(
        id = "d105b801-75de-4306-93a6-cc7124193042",
        name = "Spicy",
        options = generateTestTokoFoodCatalogVariantOptionDetails(),
        isRequired = false,
        maxQty = 0,
        minQty = 0
    )
    return listOf(singleRequiredVariant, singleOptionalVariant, multipleOptionalVariant, secondMultipleOptionalVariant)
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

fun generateTestCheckoutTokoFoodProduct(): List<CartListCartGroupCart> {
    val garlicKnots = CartListCartGroupCart(
        cartId = "cartId-garlicKnots",
        productId = "bf3eba99-534d-4344-9cf8-6a46326feae0",
        price = 38000.0,
        priceFmt = "Rp38.000",
        quantity = 1,
        metadata = CartListCartMetadata(
            notes = "",
            variants = listOf()
        ),
        nullableCustomResponse = CartListCartGroupCartCustomResponse(
            categoryId = "244f0661-515b-45ed-8969-c9f41fad2979",
            name = "Home-plate Garlic Knots",
            description = "What\u0027s better than garlic bread? 5 pcs of Garlic tangled in knots!",
            imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e561a5c-55ef-47f6-8b6a-967ca35d229e_master-menu-item-image_1612931947827.jpg",
            originalPrice = 38000.0,
            originalPriceFmt = "Rp38.000",
            discountPercentage = ""
        )
    )
    val umamiEdamame = CartListCartGroupCart(
        cartId = "cartId-umamiEdamame",
        productId = "bf3eba99-534d-4344-9cf8-6a46326feae1",
        price = 38000.0,
        priceFmt = "Rp38.000",
        quantity = 1,
        metadata = CartListCartMetadata(
            notes = "",
            variants = listOf()
        ),
        nullableCustomResponse = CartListCartGroupCartCustomResponse(
            categoryId = "244f0661-515b-45ed-8969-c9f41fad2979",
            name = "Home-plate Garlic Knots",
            description = "What\u0027s better than garlic bread? 5 pcs of Garlic tangled in knots!",
            imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e561a5c-55ef-47f6-8b6a-967ca35d229e_master-menu-item-image_1612931947827.jpg",
            originalPrice = 38000.0,
            originalPriceFmt = "Rp38.000",
            discountPercentage = ""
        )
    )
    val originalVariantOption = CartListCartGroupCartOption(
        isSelected = true,
        optionId = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
        name = "Original",
        price = 0.0,
        priceFmt = "Gratis",
        status = 1
    )
    val originalVariant = CartListCartGroupCartVariant(
        variantId = "d105b801-75de-4306-93a6-cc7124193042",
        name = "Spicy",
        rules = CartListCartGroupCartRules(),
        options = listOf(originalVariantOption)
    )
    val battingUpChickenOriginal = CartListCartGroupCart(
        cartId = "cartId-battingUpChicken",
        productId = "8829ce4a-6f00-4406-8112-721f569f0d4b",
        price = 55000.0,
        priceFmt = "Rp55.000",
        quantity = 1,
        nullableCustomResponse = CartListCartGroupCartCustomResponse(
            categoryId = "244f0661-515b-45ed-8969-c9f41fad2979",
            name = "Batting up chicken",
            description = "4 pcs of deep-fried breaded chicken tenders served with sauce on the side",
            imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e561a5c-55ef-47f6-8b6a-967ca35d229e_master-menu-item-image_1612931947827.jpg",
            originalPrice = 55000.0,
            originalPriceFmt = "Rp55.000",
            discountPercentage = "",
            variants = listOf(originalVariant)
        )
    )
    val crispyFrenchFries = CartListCartGroupCart(
        cartId = "cartId-crispyFrenchFries",
        productId = "8829ce4a-6f00-4406-8112-721f569f0d4c",
        price = 55000.0,
        priceFmt = "Rp55.000",
        quantity = 1,
        nullableCustomResponse = CartListCartGroupCartCustomResponse(
            categoryId = "244f0661-515b-45ed-8969-c9f41fad2979",
            name = "Crispy French Fries",
            description = "4 pcs of deep-fried breaded chicken tenders served with sauce on the side",
            imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/2e561a5c-55ef-47f6-8b6a-967ca35d229e_master-menu-item-image_1612931947827.jpg",
            originalPrice = 55000.0,
            originalPriceFmt = "Rp55.000",
            discountPercentage = "",
            variants = listOf(originalVariant)
        )
    )
    return listOf(garlicKnots, umamiEdamame, battingUpChickenOriginal, crispyFrenchFries)
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
    val original = OptionUiModel(
        isSelected = true,
        id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
        status = 1,
        name = "Original",
        price = 0.0,
        priceFmt = "Gratis",
        selectionControlType = SelectionControlType.SINGLE_SELECTION
    )
    val hot = OptionUiModel(
        isSelected = false,
        id = "8af415a2-3406-4536-b2b6-0561f7b68148",
        status = 1,
        name = "Hot",
        price = 0.0,
        priceFmt = "Gratis",
        selectionControlType = SelectionControlType.SINGLE_SELECTION
    )
    val spicyAddOnUiModel = AddOnUiModel(
        id = "d105b801-75de-4306-93a6-cc7124193042",
        name = "Spicy",
        isRequired = true,
        isSelected = true,
        maxQty = 1,
        minQty = 1,
        options = listOf(hot, original),
        outOfStockWording = "Stok habis"
    )
    val customListItem = CustomListItem(
        listItemType = CustomListItemType.PRODUCT_ADD_ON,
        addOnUiModel = spicyAddOnUiModel
    )
    return CustomOrderDetail(
        cartId = "cartId-garlicKnots",
        subTotal = 0.0,
        subTotalFmt = "",
        qty = 1,
        customListItems = listOf(customListItem)
    )
}

fun generateTestCartTokoFood(): CartListCartGroupCart {
    return CartListCartGroupCart(
        cartId = "cartId-garlicKnots",
        productId = "bf3eba99-534d-4344-9cf8-6a46326feae0",
        quantity = 1,
        metadata = CartListCartMetadata(
            notes = "ini notes",
            variants = listOf(
                CartListCartMetadataVariant(
                    variantId = "d105b801-75de-4306-93a6-cc7124193042",
                    optionId = "379913bf-e89e-4a26-a2e6-a650ebe77aef"
                )
            )
        )
    )
}

fun generateTestProductUiModelWithVariant(
    isAddOnUiModelNull: Boolean = false,
    isOptionEmpty: Boolean = false
): ProductUiModel {
    val original = OptionUiModel(
        isSelected = true,
        id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
        status = 1,
        name = "Original",
        price = 0.0,
        priceFmt = "Gratis",
        selectionControlType = SelectionControlType.SINGLE_SELECTION
    )
    val hot = OptionUiModel(
        isSelected = false,
        id = "8af415a2-3406-4536-b2b6-0561f7b68148",
        status = 1,
        name = "Hot",
        price = 0.0,
        priceFmt = "Gratis",
        selectionControlType = SelectionControlType.SINGLE_SELECTION
    )
    val spicyAddOnUiModel = AddOnUiModel(
        id = "d105b801-75de-4306-93a6-cc7124193042",
        name = "Spicy",
        isRequired = true,
        isSelected = true,
        maxQty = 1,
        minQty = 1,
        options = if (isOptionEmpty) listOf() else listOf(hot, original),
        outOfStockWording = "Stok habis"
    )
    val customListItem = CustomListItem(
        listItemType = CustomListItemType.PRODUCT_ADD_ON,
        addOnUiModel = if (isAddOnUiModelNull) null else spicyAddOnUiModel
    )
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
        customListItems = listOf(customListItem)
    )
}

fun generateTestKeroEditAddressResponse(): KeroEditAddressResponse {
    return KeroEditAddressResponse(
        keroEditAddress = KeroEditAddress(
            data = KeroEditAddressData(
                addressId = "123",
                isSuccess = 1
            )
        )
    )
}

fun generateTestGetStateChosenAddressQglResponse(): GetStateChosenAddressQglResponse {
    return GetStateChosenAddressQglResponse(
        response = GetStateChosenAddressResponse(
            data = ChosenAddressDataResponse(
                districtId = 1,
                cityId = 1,
                addressId = 1
            )
        )
    )
}

fun generateTestDeliveryCoverageResult(): GetMerchantDataResponse {
    return GetMerchantDataResponse(
        TokoFoodGetMerchantData(
            ticker = TokoFoodTickerDetail(),
            topBanner = TokoFoodTopBanner(),
            merchantProfile = TokoFoodMerchantProfile(
                deliverable = true
            ),
            filters = listOf(),
            categories = listOf()
        )
    )
}

fun generateTestUiAddOnUiModel(): AddOnUiModel {
    val original = OptionUiModel(
        isSelected = true,
        id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
        status = 1,
        name = "Original",
        price = 0.0,
        priceFmt = "Gratis",
        selectionControlType = SelectionControlType.SINGLE_SELECTION
    )
    val hot = OptionUiModel(
        isSelected = false,
        id = "8af415a2-3406-4536-b2b6-0561f7b68148",
        status = 1,
        name = "Hot",
        price = 0.0,
        priceFmt = "Gratis",
        selectionControlType = SelectionControlType.SINGLE_SELECTION
    )
    return AddOnUiModel(
        id = "d105b801-75de-4306-93a6-cc7124193042",
        name = "Spicy",
        isRequired = true,
        isSelected = true,
        maxQty = 1,
        minQty = 1,
        options = listOf(hot, original),
        outOfStockWording = "Stok habis"
    )
}

fun generateCustomListItemsWithError(): CustomListItem {
    val original = OptionUiModel(
        isSelected = false,
        id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
        status = 1,
        name = "Original",
        price = 0.0,
        priceFmt = "Gratis",
        selectionControlType = SelectionControlType.SINGLE_SELECTION
    )
    val hot = OptionUiModel(
        isSelected = false,
        id = "8af415a2-3406-4536-b2b6-0561f7b68148",
        status = 1,
        name = "Hot",
        price = 0.0,
        priceFmt = "Gratis",
        selectionControlType = SelectionControlType.SINGLE_SELECTION
    )
    val spicyAddOnUiModel = AddOnUiModel(
        id = "d105b801-75de-4306-93a6-cc7124193042",
        name = "Spicy",
        isRequired = true,
        isSelected = true,
        maxQty = 1,
        minQty = 1,
        options = listOf(hot, original),
        outOfStockWording = "Stok habis"
    )
    return CustomListItem(
        listItemType = CustomListItemType.PRODUCT_ADD_ON,
        addOnUiModel = spicyAddOnUiModel
    )
}

fun generateCustomListItemsWithoutError(): CustomListItem {
    val original = OptionUiModel(
        isSelected = true,
        id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
        status = 1,
        name = "Original",
        price = 0.0,
        priceFmt = "Gratis",
        selectionControlType = SelectionControlType.SINGLE_SELECTION
    )
    val hot = OptionUiModel(
        isSelected = false,
        id = "8af415a2-3406-4536-b2b6-0561f7b68148",
        status = 1,
        name = "Hot",
        price = 0.0,
        priceFmt = "Gratis",
        selectionControlType = SelectionControlType.SINGLE_SELECTION
    )
    val spicyAddOnUiModel = AddOnUiModel(
        id = "d105b801-75de-4306-93a6-cc7124193042",
        name = "Spicy",
        isRequired = true,
        isSelected = true,
        maxQty = 1,
        minQty = 1,
        options = listOf(hot, original),
        outOfStockWording = "Stok habis"
    )
    return CustomListItem(
        listItemType = CustomListItemType.PRODUCT_ADD_ON,
        addOnUiModel = spicyAddOnUiModel
    )
}

fun generateTestDataGetCustomListItems(
    cartId: String,
    customCartId: String? = null,
    isAddOnUiModelNull: Boolean = false,
    isAddOnOptionEmpty: Boolean = false,
    isCustomOrderDetailEmpty: Boolean = false
): ProductUiModel {
    val original = OptionUiModel(
        isSelected = cartId.isNotBlank(),
        id = "379913bf-e89e-4a26-a2e6-a650ebe77aef",
        status = 1,
        name = "Original",
        price = 0.0,
        priceFmt = "Gratis",
        selectionControlType = SelectionControlType.SINGLE_SELECTION
    )
    val hot = OptionUiModel(
        isSelected = false,
        id = "8af415a2-3406-4536-b2b6-0561f7b68148",
        status = 1,
        name = "Hot",
        price = 0.0,
        priceFmt = "Gratis",
        selectionControlType = SelectionControlType.SINGLE_SELECTION
    )
    val spicyAddOnUiModel = AddOnUiModel(
        id = "d105b801-75de-4306-93a6-cc7124193042",
        name = "Spicy",
        isRequired = true,
        isSelected = cartId.isNotBlank(),
        maxQty = 1,
        minQty = 1,
        options = if (isAddOnOptionEmpty) listOf() else listOf(hot, original),
        outOfStockWording = "Stok habis"
    )
    val customListItem = CustomListItem(
        listItemType = CustomListItemType.PRODUCT_ADD_ON,
        addOnUiModel = if (isAddOnUiModelNull) null else spicyAddOnUiModel
    )
    return if (cartId.isBlank()) {
        ProductUiModel(
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
            customListItems = listOf(customListItem)
        )
    } else {
        val customOrderDetail = CustomOrderDetail(
            cartId = customCartId ?: cartId,
            customListItems = listOf(customListItem)
        )
        ProductUiModel(
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
            customOrderDetails = if (isCustomOrderDetailEmpty) {
                mutableListOf()
            } else {
                mutableListOf(customOrderDetail)
            }
        )
    }
}
