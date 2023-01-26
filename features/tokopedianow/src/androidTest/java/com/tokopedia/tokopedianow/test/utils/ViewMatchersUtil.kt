@file:Suppress("UNCHECKED_CAST")

package com.tokopedia.tokopedianow.test.utils

import android.view.View
import com.tokopedia.tokopedianow.test.utils.matcher.TokoNowItemPositionDisplayedMatcher
import com.tokopedia.tokopedianow.test.utils.matcher.TokoNowProductCardNameTypographyDisplayedMatcher
import com.tokopedia.tokopedianow.test.utils.matcher.TokoNowQuantityEditorViewDisplayedMatcher
import com.tokopedia.tokopedianow.test.utils.matcher.ViewDisplayedWithTextMatcher
import com.tokopedia.tokopedianow.test.utils.matcher.ViewNotDisplayedMatcher
import org.hamcrest.Matcher

internal object ViewMatchersUtil {

    fun isDisplayedWithText(text: String): Matcher<View> {
        return ViewDisplayedWithTextMatcher(text)
    }

    fun isNotDisplayed(): Matcher<View> = ViewNotDisplayedMatcher()

    fun isTokoNowQuantityEditorViewDisplayed(
        minOrder: Int,
        maxOrder: Int,
        orderQuantity: Int
    ): Matcher<View> {
        return TokoNowQuantityEditorViewDisplayedMatcher(
            minOrder = minOrder,
            maxOrder = maxOrder,
            orderQuantity = orderQuantity
        ) as Matcher<View>
    }

    fun isTokoNowProductCardNameTypographyDisplayed(
        productName: String,
        needToChangeMaxLinesName: Boolean
    ): Matcher<View> {
        return TokoNowProductCardNameTypographyDisplayedMatcher(
            productName = productName,
            needToChangeMaxLinesName = needToChangeMaxLinesName
        ) as Matcher<View>
    }

    fun isTokoNowProductCardInThePosition(position: Int, itemMatcherList: Map<Int, Matcher<View>>): Matcher<View?> {
        return TokoNowItemPositionDisplayedMatcher(position, itemMatcherList)
    }

}
