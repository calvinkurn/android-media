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

internal val productCardGridTestData = productCardModelMatcherData + mutableListOf<ProductCardModelMatcher>().also {
    it.add(testOutOfStock())
}

private fun testOutOfStock(): ProductCardModelMatcher {
    val labelProductStatus = LabelGroup(position = LABEL_PRODUCT_STATUS, title = "Stok habis", type = TRANSPARENT_BLACK)
    val productCardModel = ProductCardModel(
            isOutOfStock = true,
            productImageUrl = productImageUrl,
            labelGroupList = mutableListOf<LabelGroup>().also { labelGroups ->
                labelGroups.add(labelProductStatus)
            },
            productName = "Out of stock",
            formattedPrice = "Rp7.999.000"
    )

    val productCardMatcher = mutableMapOf<Int, Matcher<View?>>().also {
        it[R.id.imageProduct] = isDisplayed()
        it[R.id.outOfStockOverlay] = isDisplayed()
        it[R.id.labelProductStatus] = isDisplayedWithText(labelProductStatus.title)
        it[R.id.textViewProductName] = isDisplayedWithText(productCardModel.productName)
        it[R.id.textViewPrice] = isDisplayedWithText(productCardModel.formattedPrice)
    }

    return ProductCardModelMatcher(productCardModel, productCardMatcher)
}
