package com.tokopedia.productcard.test

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.productcard.test.grid.ProductCardGridActivityTest
import com.tokopedia.productcard.test.grid.ProductCardGridViewStubActivityTest
import com.tokopedia.productcard.test.grid.productCardGridTestData
import com.tokopedia.productcard.test.grid.productCardGridViewStubTestData
import com.tokopedia.productcard.test.list.ProductCardListActivityTest
import com.tokopedia.productcard.test.list.ProductCardListViewStubActivityTest
import com.tokopedia.productcard.test.list.productCardListTestData
import com.tokopedia.productcard.test.list.productCardListViewStubTestData
import com.tokopedia.productcard.test.utils.generator.IDGeneratorHelper
import com.tokopedia.productcard.test.utils.productCardInPosition
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.Matcher
import org.junit.Test

@UiTest
internal class ProductCardTest {

    private lateinit var recyclerViewViewInteraction: ViewInteraction
    private lateinit var productCardModelMatcherData: List<ProductCardModelMatcher>
    private lateinit var activity: Activity
    private var recyclerViewId: Int = 0
    private val recyclerView by lazy { activity.findViewById<RecyclerView>(recyclerViewId) }

    @Test
    fun testProductCardGrid() {
        activity = startTestActivity(ProductCardGridActivityTest::class.java.name)
        recyclerViewId = R.id.productCardGridTestRecyclerView

        recyclerViewViewInteraction = onView(withId(R.id.productCardGridTestRecyclerView))
        productCardModelMatcherData = productCardGridTestData

        startTest()
    }

    @Test
    fun testProductCardList() {
        activity = startTestActivity(ProductCardListActivityTest::class.java.name)
        recyclerViewId = R.id.productCardListTestRecyclerView

        recyclerViewViewInteraction = onView(withId(R.id.productCardListTestRecyclerView))
        productCardModelMatcherData = productCardListTestData

        startTest()
    }

    @Test
    fun testProductCardGridViewStub() {
        activity = startTestActivity(ProductCardGridViewStubActivityTest::class.java.name)
        recyclerViewId = R.id.productCardGridTestRecyclerView

        recyclerViewViewInteraction = onView(withId(R.id.productCardGridTestRecyclerView))
        productCardModelMatcherData = productCardGridViewStubTestData

        startTest()
    }

    @Test
    fun testProductCardListViewStub() {
        activity = startTestActivity(ProductCardListViewStubActivityTest::class.java.name)
        recyclerViewId = R.id.productCardListTestRecyclerView

        recyclerViewViewInteraction = onView(withId(R.id.productCardListTestRecyclerView))
        productCardModelMatcherData = productCardListViewStubTestData

        startTest()
    }

    private fun startTestActivity(activityClassName: String): Activity {
        val intent = Intent(Intent.ACTION_MAIN).also {
            it.setClassName(getInstrumentation().targetContext.packageName, activityClassName)
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        return getInstrumentation().startActivitySync(intent)
    }

    private fun startTest() {
        productCardModelMatcherData.forEachIndexed { index, productCardModelMatcher ->
            recyclerViewViewInteraction.checkProductCardAtPosition(index, productCardModelMatcher.productCardMatcher)

            recyclerView.findViewHolderForAdapterPosition(index)?.let {
                IDGeneratorHelper.printView(
                    it,
                    productCardModelMatcherData[index].productCardModel.productName,
                )
            }
        }
    }

    private fun ViewInteraction.checkProductCardAtPosition(position: Int, elementMatchers: Map<Int, Matcher<View?>>): ViewInteraction {
        return perform(scrollToPosition<ProductCardGridActivityTest.ViewHolder>(position))
            .check(matches(productCardInPosition(position, elementMatchers)))
    }
}
