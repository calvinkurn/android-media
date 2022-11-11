package com.tokopedia.search.utils

import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.Matcher
import com.tokopedia.productcard.R as productCardR

private class ClickATCOnProductCard: ViewAction {

    override fun getConstraints(): Matcher<View> = isProductCard()

    override fun getDescription(): String = "Click ATC button on Product Card"

    override fun perform(uiController: UiController?, view: View?) {
        val constraints = constraints

        if (view == null) throw nullView()
        if (!constraints.matches(view)) throw notProductCard(view)

        val addToCartButton = getAddToCartButton(view) ?: throw doesNotHaveAddToCartButton(view)

        if (!isDisplayed().matches(addToCartButton)) throw addToCartNotVisible(addToCartButton)

        addToCartButton.performClick()
    }

    private fun nullView() =
        PerformException.Builder()
            .withCause(IllegalStateException("View is null"))
            .withActionDescription(description)
            .build()

    private fun notProductCard(view: View?) =
        PerformException.Builder()
            .withCause(IllegalStateException("Not Product Card"))
            .withViewDescription(HumanReadables.describe(view))
            .withActionDescription(description)
            .build()

    private fun getAddToCartButton(view: View) =
        view.findViewById<View?>(productCardR.id.buttonAddToCart)

    private fun doesNotHaveAddToCartButton(view: View) =
        PerformException.Builder()
            .withCause(IllegalStateException("Add to Cart Button is null"))
            .withViewDescription(HumanReadables.describe(view))
            .withActionDescription(description)
            .build()

    private fun addToCartNotVisible(addToCartButton: View) =
        PerformException.Builder()
            .withActionDescription(description)
            .withCause(IllegalStateException("Add to Cart on Product Card is not visible"))
            .withViewDescription(HumanReadables.describe(addToCartButton))
            .build()
}

internal fun clickAddToCartOnProductCard(): ViewAction = ClickATCOnProductCard()
