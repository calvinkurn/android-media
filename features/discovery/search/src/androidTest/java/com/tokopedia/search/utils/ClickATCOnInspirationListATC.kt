package com.tokopedia.search.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.util.HumanReadables
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.search.R
import org.hamcrest.Matcher
import com.tokopedia.carouselproductcard.R as carouselProductCardR

private class ClickATCOnInspirationListATC: ViewAction {
    override fun getConstraints(): Matcher<View> = isAnyView()

    override fun getDescription(): String =
        "Click ATC in Inspiration Carousel List ATC"

    override fun perform(uiController: UiController?, view: View?) {
        val carouselProductCard = view?.findViewById<CarouselProductCardView>(
            R.id.inspirationCarouselListAtcProductCarousel
        ) ?: throw carouselProductCardNullException(view)

        val carouselRecyclerView = carouselProductCard.findViewById<RecyclerView?>(
            carouselProductCardR.id.carouselProductCardRecyclerView
        ) ?: throw recyclerViewNullException(view)

        actionOnItemAtPosition<RecyclerView.ViewHolder>(
            FIRST_POSITION,
            clickAddToCartOnProductCard()
        ).perform(uiController, carouselRecyclerView)
    }

    private fun carouselProductCardNullException(view: View?) =
        PerformException.Builder()
            .withCause(IllegalStateException("Carousel product card view is null"))
            .withViewDescription(HumanReadables.describe(view))
            .withActionDescription(description)
            .build()

    private fun recyclerViewNullException(view: View?) =
        PerformException.Builder()
            .withCause(IllegalStateException("Recycler View in Carousel product card is null"))
            .withViewDescription(HumanReadables.describe(view))
            .withActionDescription(description)
            .build()

    companion object {
        private const val FIRST_POSITION = 0
    }
}

fun clickAddToCartOnInspirationListATC(): ViewAction = ClickATCOnInspirationListATC()
