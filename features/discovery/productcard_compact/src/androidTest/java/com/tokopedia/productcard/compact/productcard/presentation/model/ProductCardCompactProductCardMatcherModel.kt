package com.tokopedia.productcard.compact.productcard.presentation.model

import android.view.View
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import org.hamcrest.Matcher

internal class ProductCardCompactProductCardMatcherModel(
    val model: ProductCardCompactUiModel,
    val matchers: Map<Int, Matcher<View?>>
)
