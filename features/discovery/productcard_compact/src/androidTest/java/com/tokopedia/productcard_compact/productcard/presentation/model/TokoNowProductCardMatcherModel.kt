package com.tokopedia.productcard_compact.productcard.presentation.model

import android.view.View
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel
import org.hamcrest.Matcher

internal class TokoNowProductCardMatcherModel(
    val model: TokoNowProductCardViewUiModel,
    val matchers: Map<Int, Matcher<View?>>
)
