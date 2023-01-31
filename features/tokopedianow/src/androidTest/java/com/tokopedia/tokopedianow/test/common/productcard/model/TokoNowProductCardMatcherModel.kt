package com.tokopedia.tokopedianow.test.common.productcard.model

import android.view.View
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import org.hamcrest.Matcher

internal class TokoNowProductCardMatcherModel(
    val model: TokoNowProductCardViewUiModel,
    val matchers: Map<Int, Matcher<View?>>
)
