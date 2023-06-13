package com.tokopedia.search.utils

import android.view.View
import com.tokopedia.productcard.IProductCardView
import org.hamcrest.BaseMatcher
import org.hamcrest.Description

private class ProductCardMatcher: BaseMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("is Product Card")
    }

    override fun matches(item: Any?): Boolean = item is IProductCardView
}

internal fun isProductCard(): BaseMatcher<View> = ProductCardMatcher()
