package com.tokopedia.productcard.test.grid

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.ProductCardModel.LabelGroup
import com.tokopedia.productcard.test.ProductCardModelMatcher
import com.tokopedia.productcard.test.R
import com.tokopedia.productcard.test.productCardModelMatcherData
import com.tokopedia.productcard.test.utils.isDisplayedWithText
import com.tokopedia.productcard.test.utils.productImageUrl
import com.tokopedia.productcard.utils.LABEL_PRODUCT_STATUS
import com.tokopedia.productcard.utils.TRANSPARENT_BLACK
import org.hamcrest.Matcher

internal val productCardGridTestData =
    productCardModelMatcherData +
        listOf(
            testSimilarProductButton(),
        )

private fun testSimilarProductButton(): ProductCardModelMatcher {
    val productCardModel = ProductCardModel(
        productName = "Similar Product Button",
        productImageUrl = productImageUrl,
        formattedPrice = "Rp8.999.000",
        shopLocation = "DKI Jakarta",
        hasSimilarProductButton = true
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
        it[R.id.textViewShopLocation] = isDisplayedWithText(productCardModel.shopLocation)
        it[R.id.buttonSeeSimilarProduct] = isDisplayed()
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}
