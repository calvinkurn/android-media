@file:Suppress("UNCHECKED_CAST")

package com.tokopedia.tokopedianow.test.utils

import android.view.View
import com.tokopedia.tokopedianow.test.utils.matcher.TokoNowItemPositionDisplayedMatcher
import com.tokopedia.tokopedianow.test.common.productcard.utils.matcher.TokoNowProductCardNameTypographyDisplayedMatcher
import com.tokopedia.tokopedianow.test.common.productcard.utils.matcher.TokoNowProductCardProgressBarDisplayedMatcher
import com.tokopedia.tokopedianow.test.utils.matcher.TokoNowQuantityEditorDisplayedMatcher
import com.tokopedia.tokopedianow.test.utils.matcher.TokoNowWishlistButtonDisplayedMatcher
import com.tokopedia.tokopedianow.test.utils.matcher.ViewDisplayedWithTextMatcher
import com.tokopedia.tokopedianow.test.utils.matcher.ViewNotDisplayedMatcher
import org.hamcrest.Matcher

internal object ViewMatchersUtil {

    fun isDisplayedWithText(text: String) = ViewDisplayedWithTextMatcher(text)

    fun isNotDisplayed() = ViewNotDisplayedMatcher()

    fun isTokoNowQuantityEditorViewDisplayed(
        minOrder: Int,
        maxOrder: Int,
        orderQuantity: Int
    ) = TokoNowQuantityEditorDisplayedMatcher(
        minOrder = minOrder,
        maxOrder = maxOrder,
        orderQuantity = orderQuantity
    ) as Matcher<View>

    fun isTokoNowProductCardNameTypographyDisplayed(
        productName: String,
        needToChangeMaxLinesName: Boolean,
        promoLabelAvailable: Boolean
    ) = TokoNowProductCardNameTypographyDisplayedMatcher(
        productName = productName,
        needToChangeMaxLinesName = needToChangeMaxLinesName,
        promoLabelAvailable = promoLabelAvailable
    ) as Matcher<View>

    fun isTokoNowProductCardProgressBarDisplayed(
        progressBarPercentage: Int
    ) = TokoNowProductCardProgressBarDisplayedMatcher(
        progressBarPercentage = progressBarPercentage
    ) as Matcher<View>

    fun isTokoNowWishlistButtonDisplayedMatcher(hasBeenSelected: Boolean) = TokoNowWishlistButtonDisplayedMatcher(hasBeenSelected) as Matcher<View>

    fun isTokoNowProductCardInThePosition(
        position: Int,
        itemMatcherList: Map<Int, Matcher<View>>
    ): Matcher<View?> = TokoNowItemPositionDisplayedMatcher(
        position = position,
        itemMatcherList = itemMatcherList
    )

}
