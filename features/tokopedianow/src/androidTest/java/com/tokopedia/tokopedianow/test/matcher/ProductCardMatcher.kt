package com.tokopedia.tokopedianow.test.matcher

import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.test.matcher.EditTextMatcher.isEditTextValueEqualTo
import com.tokopedia.tokopedianow.test.util.ViewIdlingResourceHelper.waitForViewDisplayed
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import com.tokopedia.productcard.compact.R as productcardcompactR

class ProductCardMatcher(private val containerId: Int = R.id.product_recommendation) {

    fun scrollProductToPosition(index: Int) {
        val recyclerView = allOf(
            withId(productcardcompactR.id.recycler_view),
            isDescendantOfA(withId(containerId))
        )
        waitForViewDisplayed(recyclerView, isDisplayed())

        onView(recyclerView).perform(RecyclerViewActions
            .scrollToPosition<RecyclerView.ViewHolder>(index))
    }

    fun assertProductName(index: Int, productName: String) {
        val name = HtmlCompat
            .fromHtml(productName, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

        val atPositionOnView = RecyclerViewMatcher(R.id.recycler_view)
            .atPositionOnView(index, productcardcompactR.id.product_name_typography)

        onView(atPositionOnView).check(matches(withText(name)))
    }

    fun assertPromoLabel(index: Int, discount: String?, promoText: String?) {
        if(!discount.isNullOrBlank()) {
            val atPositionOnView = RecyclerViewMatcher(R.id.recycler_view)
                .atPositionOnView(index, productcardcompactR.id.promo_label)
            onView(atPositionOnView).check(matches(withText(discount)))
        } else if(promoText != null) {
            val atPositionOnView = RecyclerViewMatcher(R.id.recycler_view)
                .atPositionOnView(index, productcardcompactR.id.promo_label)
            onView(atPositionOnView).check(matches(withText(promoText)))
        } else {
            val atPositionOnView = RecyclerViewMatcher(R.id.recycler_view)
                .atPositionOnView(index, productcardcompactR.id.promo_layout_view_stub)
            onView(atPositionOnView).check(matches(not(isDisplayed())))
        }
    }

    fun assertValueLabel(index: Int, valueText: String?) {
        if(valueText != null) {
            val atPositionOnView = RecyclerViewMatcher(R.id.recycler_view)
                .atPositionOnView(index, productcardcompactR.id.assigned_value_typography)
            onView(atPositionOnView).check(matches(withText(valueText)))
        } else {
            val atPositionOnView = RecyclerViewMatcher(R.id.recycler_view)
                .atPositionOnView(index, productcardcompactR.id.assigned_value_typography)
            onView(atPositionOnView).check(matches(not(isDisplayed())))
        }
    }

    fun assertProductRating(index: Int, productRating: String) {
        if(productRating.isNotBlank()) {
            val atPositionOnRatingIconView = RecyclerViewMatcher(R.id.recycler_view)
                .atPositionOnView(index, productcardcompactR.id.rating_icon)

            val atPositionOnRatingTextView = RecyclerViewMatcher(R.id.recycler_view)
                .atPositionOnView(index, productcardcompactR.id.rating_typography)

            onView(atPositionOnRatingIconView).check(matches(isDisplayed()))
            onView(atPositionOnRatingTextView).check(matches(withText(productRating)))
        } else {
            val atPositionOnRatingIconView = RecyclerViewMatcher(R.id.recycler_view)
                .atPositionOnView(index, productcardcompactR.id.rating_icon)

            val atPositionOnRatingTextView = RecyclerViewMatcher(R.id.recycler_view)
                .atPositionOnView(index, productcardcompactR.id.rating_typography)

            onView(atPositionOnRatingIconView).check(matches(not(isDisplayed())))
            onView(atPositionOnRatingTextView).check(matches(not(isDisplayed())))
        }
    }

    fun assertAddToCartQuantity(index: Int, quantity: Int?) {
        if(quantity != null) {
            val atPositionOnView = RecyclerViewMatcher(R.id.recycler_view)
                .atPositionOnView(index, productcardcompactR.id.edit_text)

            waitForViewDisplayed(atPositionOnView, isEditTextValueEqualTo(quantity.toString()))
        } else {
            val atPositionOnView = RecyclerViewMatcher(R.id.recycler_view)
                .atPositionOnView(index, productcardcompactR.id.edit_text)
            onView(atPositionOnView).check(matches(isEditTextValueEqualTo("")))
        }
    }

    fun assertProductPrice(index: Int, productPrice: String) {
        val atPositionOnView = RecyclerViewMatcher(R.id.recycler_view)
            .atPositionOnView(index, productcardcompactR.id.main_price_typography)
        onView(atPositionOnView).check(matches(withText(productPrice)))
    }

    fun assertSlashPrice(index: Int, slashPrice: String) {
        if(slashPrice.isNotBlank()) {
            val atPositionOnView = RecyclerViewMatcher(R.id.recycler_view)
                .atPositionOnView(index, productcardcompactR.id.slash_price_typography)
            onView(atPositionOnView).check(matches(withText(slashPrice)))
        }
    }
}
