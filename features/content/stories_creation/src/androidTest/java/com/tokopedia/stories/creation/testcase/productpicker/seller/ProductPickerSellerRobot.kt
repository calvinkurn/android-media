package com.tokopedia.stories.creation.testcase.productpicker.seller

import android.content.Context
import androidx.test.espresso.Espresso
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.product.picker.R as contentproductpickerR
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.util.click
import com.tokopedia.content.test.util.clickItemRecyclerView
import com.tokopedia.stories.creation.helper.ProductPickerLauncher
import com.tokopedia.stories.creation.helper.StoriesCreationCassavaValidator
import com.tokopedia.unifycomponents.R as unifycomponentsR

/**
 * Created By : Jonathan Darwin on October 20, 2023
 */
class ProductPickerSellerRobot(
    context: Context,
    cassavaTestRule: CassavaTestRule
) {

    private val cassavaValidator = StoriesCreationCassavaValidator.buildForStoriesCreation(cassavaTestRule)

    private val launcher = ProductPickerLauncher(context)

    fun launchActivity() = chainable {
        launcher.launchActivity()
    }

    fun verifyOpenScreen(screenName: String) = chainable {
        cassavaValidator.verifyOpenScreen(screenName)
    }

    fun verifyAction(eventAction: String) = chainable {
        cassavaValidator.verify(eventAction)
    }

    fun performDelay(delayInMillis: Long = 500) = chainable {
        delay(delayInMillis)
    }

    fun closeSoftKeyboard() = chainable {
        Espresso.closeSoftKeyboard()
    }

    fun selectProduct(idx: Int = 0) = chainable {
        delay()
        clickItemRecyclerView(contentproductpickerR.id.rv_products, idx)
    }

    fun clickSubmitProductTag() = chainable {
        closeSoftKeyboard()
        click(contentproductpickerR.id.btn_next)
    }

    fun clickCloseProductSummaryBottomSheet() = chainable {
        click(unifycomponentsR.id.bottom_sheet_close)
    }

    fun clickSearchBarProductPickerSGC() = chainable {
        click(contentproductpickerR.id.search_bar)
    }

    fun clickSortChip() = chainable {
        click(contentproductpickerR.id.chips_sort)
    }

    fun clickEtalaseAndCampaignChip() = chainable {
        click(contentproductpickerR.id.chips_etalase)
    }

    fun selectSortType(position: Int = 1) = chainable {
        clickItemRecyclerView(contentproductpickerR.id.rv_sort, 1)
    }

    fun clickSaveSort() = chainable {
        click(unifycomponentsR.id.bottom_sheet_action)
    }

    fun selectEtalaseOrCampaign(idx: Int) = chainable {
        clickItemRecyclerView(contentproductpickerR.id.rv_etalase, idx)
    }

    fun clickAddMoreProduct() = chainable {
        click(unifycomponentsR.id.bottom_sheet_action)
    }

    fun clickDeleteOnFirstProduct() = chainable {
        clickItemRecyclerView(contentproductpickerR.id.rv_product_summaries, 1, contentproductpickerR.id.ic_product_summary_delete)
    }

    fun clickNextOnProductPickerSummary() = chainable {
        click(contentproductpickerR.id.btn_done)
    }

    private fun chainable(action: () -> Unit): ProductPickerSellerRobot {
        action()
        return this
    }
}
