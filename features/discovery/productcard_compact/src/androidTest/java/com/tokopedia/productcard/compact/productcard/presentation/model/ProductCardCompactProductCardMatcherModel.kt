package com.tokopedia.productcard.compact.productcard.presentation.model

import android.view.View
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel
import org.hamcrest.Matcher

internal class ProductCardCompactProductCardMatcherModel(
    val model: TokoNowProductCardViewUiModel,
    val matchers: Map<Int, Matcher<View?>>
)
