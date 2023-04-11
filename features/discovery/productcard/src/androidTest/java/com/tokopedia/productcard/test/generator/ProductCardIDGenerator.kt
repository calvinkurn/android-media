package com.tokopedia.productcard.test.generator

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.tokopedia.productcard.test.R
import com.tokopedia.productcard.test.generator.utils.IDGeneratorHelper
import org.hamcrest.Matcher
import org.junit.Test
class ProductCardIDGenerator  {

    private lateinit var recyclerViewViewInteraction: ViewInteraction
    private lateinit var activity: Activity
    private val recyclerView by lazy {
        activity.findViewById<RecyclerView>(R.id.productCardIDGeneratorTestRecyclerView)
    }

    @Test
    fun generateProductCardID() {
        activity = startTestActivity(ProductCardIDGeneratorActivityTest::class.java.name)

        recyclerViewViewInteraction = onView(withId(R.id.productCardIDGeneratorTestRecyclerView))

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
        recyclerView.adapter?.let {
            for (index in 0 until it.itemCount) {
                recyclerViewViewInteraction.goToProductCardAtPosition(index)

                recyclerView.findViewHolderForAdapterPosition(index)?.let { viewHolder ->
                    IDGeneratorHelper.printView(
                        viewHolder,
                        "product card $index",
                    )
                }
            }
        }
    }

    private fun ViewInteraction.goToProductCardAtPosition(position: Int): ViewInteraction {
        return perform(scrollToPosition<ProductCardIDGeneratorActivityTest.BaseTestViewHolder>(position))
    }
}
