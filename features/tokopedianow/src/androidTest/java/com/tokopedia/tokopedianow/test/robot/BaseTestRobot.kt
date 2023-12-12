package com.tokopedia.tokopedianow.test.robot

import androidx.test.espresso.Espresso
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import com.tokopedia.productcard.compact.productcardcarousel.presentation.viewholder.ProductCardCompactCarouselItemViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions.clickChildViewWithId
import com.tokopedia.tokopedianow.R
import org.hamcrest.Matchers.allOf
import com.tokopedia.productcard.compact.R as productcardcompactR

open class BaseTestRobot {

    fun addToCartOnView(
        withId: Int = R.id.recycler_view,
        withParentId: Int = R.id.product_card_carousel,
        position: Int
    ) {
        Espresso.onView(allOf(
            withId(withId),
            withParent(withId(withParentId))
        )).perform(
            actionOnItemAtPosition<ProductCardCompactCarouselItemViewHolder>(
                position,
                clickChildViewWithId(productcardcompactR.id.add_button)
            )
        )
    }
}
