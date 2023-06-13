package com.tokopedia.search.generator

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.search.QUERY_PARAMS_WITH_KEYWORD
import com.tokopedia.search.generator.utils.IDGeneratorHelper
import org.junit.Test

internal class TopNavCardIDGenerator: BaseIDGenerator() {
    override val queryParam: String = QUERY_PARAMS_WITH_KEYWORD
    override val mockModel: Int = com.tokopedia.search.test.R.raw.search_product_response_keyword_kfc

    @Test
    fun generateTopNavCardID() {
        performUserJourney()
    }

    private fun performUserJourney() {
        Espresso.onView(ViewMatchers.withId(recyclerViewId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        IDGeneratorHelper.scrollAndPrintView(recyclerView)
    }
}
