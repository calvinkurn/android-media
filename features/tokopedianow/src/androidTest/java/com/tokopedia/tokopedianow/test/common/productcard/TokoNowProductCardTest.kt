package com.tokopedia.tokopedianow.test.common.productcard

import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokopedianow.test.R
import com.tokopedia.tokopedianow.test.common.productcard.adapter.TokoNowProductCardViewHolder
import com.tokopedia.tokopedianow.test.common.productcard.model.TokoNowProductCardMatcherModel
import com.tokopedia.tokopedianow.test.common.productcard.utils.TokoNowProductCardModelMatcherData.getProductCardModelMatcherData
import com.tokopedia.tokopedianow.test.utils.ViewMatchersUtil.isTokoNowProductCardInThePosition
import org.hamcrest.Matcher
import org.junit.Test

@UiTest
internal class TokoNowProductCardTest {

    private lateinit var recyclerViewViewInteraction: ViewInteraction
    private lateinit var tokoNowProductCardMatcherModelData: List<TokoNowProductCardMatcherModel>

    @Test
    fun testProductCardGrid() {
        startTestActivity(TokoNowProductCardActivityTest::class.java.name)
        Thread.sleep(6000)

        recyclerViewViewInteraction = onView(withId(R.id.rv_product_card))
        tokoNowProductCardMatcherModelData = getProductCardModelMatcherData()

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
        tokoNowProductCardMatcherModelData.forEachIndexed { index, productCardModelMatcher ->
            recyclerViewViewInteraction.checkProductCardAtPosition(index, productCardModelMatcher.matcher)
        }
    }

    private fun ViewInteraction.checkProductCardAtPosition(
        position: Int,
        elementMatchers: Map<Int, Matcher<View>>
    ): ViewInteraction {
        return perform(scrollToPosition<TokoNowProductCardViewHolder>(position)).check(matches(isTokoNowProductCardInThePosition(position, elementMatchers)))
    }
}
