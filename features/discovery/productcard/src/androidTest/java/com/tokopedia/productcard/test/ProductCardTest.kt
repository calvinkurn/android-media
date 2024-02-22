package com.tokopedia.productcard.test

import android.content.Intent
import android.view.View
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
import com.tokopedia.productcard.test.reimagine.ProductCardGridCarouselActivityTest
import com.tokopedia.productcard.test.reimagine.productCardReimagineCarouselGridTestData
import com.tokopedia.productcard.test.reimagine.productCardReimagineGridTestData
import com.tokopedia.productcard.test.reimagine.productCardReimagineListCarouselTestData
import com.tokopedia.productcard.test.reimagine.productCardReimagineListTestData
import com.tokopedia.productcard.test.utils.productCardInPosition
import com.tokopedia.test.application.annotations.UiTest
import org.hamcrest.Matcher
import org.junit.Test
import com.tokopedia.productcard.test.reimagine.ProductCardGridActivityTest as ProductCardReimagineGridActivityTest
import com.tokopedia.productcard.test.reimagine.ProductCardListActivityTest as ProductCardReimagineListActivityTest
import com.tokopedia.productcard.test.reimagine.ProductCardListCarouselActivityTest as ProductCardReimagineListCarouselActivityTest

@UiTest
internal class ProductCardTest {

    private lateinit var recyclerViewViewInteraction: ViewInteraction
    private lateinit var productCardModelMatcherData: List<Map<Int, Matcher<View?>>>

    @Test
    fun testProductCardGrid() {
        startTestActivity(ProductCardGridActivityTest::class.java.name)

        recyclerViewViewInteraction = onView(withId(R.id.productCardGridTestRecyclerView))
        productCardModelMatcherData = productCardGridTestData.map { it.productCardMatcher }

        startTest()
    }

    @Test
    fun testProductCardList() {
        startTestActivity(ProductCardListActivityTest::class.java.name)

        recyclerViewViewInteraction = onView(withId(R.id.productCardListTestRecyclerView))
        productCardModelMatcherData = productCardListTestData.map { it.productCardMatcher }

        startTest()
    }

    @Test
    fun testProductCardGridViewStub() {
        startTestActivity(ProductCardGridViewStubActivityTest::class.java.name)

        recyclerViewViewInteraction = onView(withId(R.id.productCardGridTestRecyclerView))
        productCardModelMatcherData = productCardGridViewStubTestData.map { it.productCardMatcher }

        startTest()
    }

    @Test
    fun testProductCardListViewStub() {
        startTestActivity(ProductCardListViewStubActivityTest::class.java.name)

        recyclerViewViewInteraction = onView(withId(R.id.productCardListTestRecyclerView))
        productCardModelMatcherData = productCardListViewStubTestData.map { it.productCardMatcher }

        startTest()
    }

    @Test
    fun testProductCardReimagineGrid() {
        startTestActivity(ProductCardReimagineGridActivityTest::class.java.name)

        recyclerViewViewInteraction = onView(withId(R.id.productCardReimagineGridTestRecyclerView))
        productCardModelMatcherData = productCardReimagineGridTestData.map { it.second }

        startTest()
    }

    @Test
    fun testProductCardReimagineGridCarousel() {
        startTestActivity(ProductCardGridCarouselActivityTest::class.java.name)

        recyclerViewViewInteraction = onView(withId(R.id.productCardReimagineGridCarouselTestRecyclerView))
        productCardModelMatcherData = productCardReimagineCarouselGridTestData.map { it.second }

        startTest()
    }

    @Test
    fun testProductCardReimagineList() {
        startTestActivity(ProductCardReimagineListActivityTest::class.java.name)

        recyclerViewViewInteraction = onView(withId(R.id.productCardReimagineListTestRecyclerView))
        productCardModelMatcherData = productCardReimagineListTestData.map { it.second }

        startTest()
    }

    @Test
    fun testProductCardReimagineListCarousel() {
        startTestActivity(ProductCardReimagineListCarouselActivityTest::class.java.name)

        recyclerViewViewInteraction = onView(withId(R.id.productCardReimagineListCarouselTestRecyclerView))
        productCardModelMatcherData = productCardReimagineListCarouselTestData.map { it.second }

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
        productCardModelMatcherData.forEachIndexed { index, productCardModelMatcher ->
            recyclerViewViewInteraction.checkProductCardAtPosition(index, productCardModelMatcher)
        }
    }

    private fun ViewInteraction.checkProductCardAtPosition(position: Int, elementMatchers: Map<Int, Matcher<View?>>): ViewInteraction {
        return perform(scrollToPosition<ProductCardGridActivityTest.ViewHolder>(position))
                .check(matches(productCardInPosition(position, elementMatchers)))
    }
}
