package com.tokopedia.productcard.test

import android.view.View
import com.tokopedia.productcard.v2.ProductCardModel
import org.hamcrest.Matcher

internal class ProductCardModelMatcher(
        val productCardModel: ProductCardModel,
        val productCardMatcher: Map<Int, Matcher<View?>>
)