package com.tokopedia.search.generator

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.search.R
import com.tokopedia.search.generator.utils.IDGeneratorHelper
import org.junit.Test

internal class SearchIDGenerator: BaseIDGenerator() {
    override val queryParam: String = "?q=samsung&srp_page_title=Title"
    override val mockModel: Int = com.tokopedia.search.test.R.raw.search_product_complete_response

    @Test
    fun generateSearchPageID() {
        performUserJourney()
    }

    private fun performUserJourney() {
        Espresso.onView(ViewMatchers.withId(recyclerViewId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        val rootView = activityRule.activity.findViewById<ConstraintLayout>(R.id.rootSearchResult)
        IDGeneratorHelper.printRootView(rootView)
        IDGeneratorHelper.scrollAndPrintView(recyclerView)
    }
}
