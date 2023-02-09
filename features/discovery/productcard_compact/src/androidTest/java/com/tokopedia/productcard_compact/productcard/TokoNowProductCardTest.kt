package com.tokopedia.productcard_compact.productcard

import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.productcard_compact.productcard.presentation.adapter.TokoNowProductCardViewHolder
import com.tokopedia.productcard_compact.productcard.presentation.model.TokoNowProductCardMatcherModel
import com.tokopedia.productcard_compact.productcard.presentation.activity.TokoNowProductCardGridActivityTest
import com.tokopedia.productcard_compact.productcard.presentation.activity.TokoNowProductCardLinearActivityTest
import com.tokopedia.productcard_compact.productcard.utils.TokoNowProductCardModelMatcherData.getProductCardModelMatcherData
import com.tokopedia.productcard_compact.productcard.utils.ViewMatchersUtil.withComponentsInProductCardMatched
import org.hamcrest.Matcher
import com.tokopedia.productcard_compact.test.R
import org.junit.Test

@MediumTest
internal class TokoNowProductCardTest {

    private lateinit var recyclerViewViewInteraction: ViewInteraction
    private lateinit var matcherModels: List<TokoNowProductCardMatcherModel>

    @Test
    fun testProductCardLinear() {
        startTestActivity(TokoNowProductCardLinearActivityTest::class.java.name)

        recyclerViewViewInteraction = onView(withId(R.id.rv_product_card))
        matcherModels = getProductCardModelMatcherData(isCarousel = true)

        startTest()
    }

    @Test
    fun testProductCardGrid() {
        startTestActivity(TokoNowProductCardGridActivityTest::class.java.name)

        recyclerViewViewInteraction = onView(withId(R.id.rv_product_card))
        matcherModels = getProductCardModelMatcherData(isCarousel = false)

        startTest()
    }

    private fun startTestActivity(activityClassName: String) {
        val intent = Intent(Intent.ACTION_MAIN).also {
            it.setClassName(getInstrumentation().targetContext.packageName, activityClassName)
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        getInstrumentation().startActivitySync(intent)
    }

    private fun startTest() {
        matcherModels.forEachIndexed { position, matcherModel ->
            recyclerViewViewInteraction.checkProductCardAtPosition(
                position = position,
                matchers = matcherModel.matchers
            )
        }
    }

    private fun ViewInteraction.checkProductCardAtPosition(
        position: Int,
        matchers: Map<Int, Matcher<View?>>
    ): ViewInteraction {
        return perform(scrollToPosition<TokoNowProductCardViewHolder>(position))
            .check(
                matches(
                    withComponentsInProductCardMatched(
                        position = position,
                        matchers = matchers
                    )
                )
            )
    }
}
