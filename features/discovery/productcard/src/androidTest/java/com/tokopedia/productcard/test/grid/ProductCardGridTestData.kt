package com.tokopedia.productcard.test.grid

import com.tokopedia.productcard.test.productCardModelMatcherData

internal val productCardGridTestData = productCardModelMatcherData + mutableListOf<ProductCardModelMatcher>().also {
    it.add(testOutOfStock())
}

private fun testOutOfStock(): ProductCardModelMatcher {
    val labelProductStatus = ProductCardModel.LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Stok habis", type = TRANSPARENT_BLACK)
    val productCardModel = ProductCardModel(
            isOutOfStock = true,
            productImageUrl = productImageUrl,
            labelGroupList = mutableListOf<ProductCardModel.LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
            },
            productName = "Out of stock",
            formattedPrice = "Rp7.999.000"
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = ViewMatchers.isDisplayed()
        it[R.id.outOfStockOverlay] = ViewMatchers.isDisplayed()
        it[R.id.labelProductStatus] = isDisplayedWithText(labelProductStatus.title)
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}