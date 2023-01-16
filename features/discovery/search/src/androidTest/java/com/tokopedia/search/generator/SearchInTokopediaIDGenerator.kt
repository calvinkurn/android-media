package com.tokopedia.search.generator

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.search.generator.utils.IDGeneratorHelper
import org.junit.Test

internal class SearchInTokopediaIDGenerator: BaseIDGenerator() {
    override val queryParam: String = "?q=hairdryer&srp_page_id=383958&navsource=tokocabang&srp_page_titleDilayani+Tokopedia&srp-component_id=02.01.00.00"
    override val mockModel: Int = com.tokopedia.search.test.R.raw.search_product_last_page_response

    @Test
    fun generateSearchInTokopediaID() {
        performUserJourney()
    }

    private fun performUserJourney() {
        Espresso.onView(ViewMatchers.withId(recyclerViewId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        IDGeneratorHelper.scrollAndPrintView(recyclerView)
    }
}
