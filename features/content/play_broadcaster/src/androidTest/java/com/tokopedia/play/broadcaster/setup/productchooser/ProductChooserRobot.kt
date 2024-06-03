package com.tokopedia.play.broadcaster.setup.productchooser

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.content.product.picker.R as contentproductpickerR
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalyticImpl
import com.tokopedia.play.broadcaster.helper.analyticUserSession
import com.tokopedia.play.broadcaster.setup.ProductSetupContainer
import com.tokopedia.content.product.picker.seller.analytic.manager.ProductChooserAnalyticManager
import com.tokopedia.content.product.picker.seller.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.content.product.picker.seller.view.bottomsheet.ProductSortBottomSheet
import com.tokopedia.content.product.picker.seller.view.viewholder.ProductListViewHolder
import com.tokopedia.content.product.picker.seller.view.viewmodel.ContentProductPickerSellerViewModel
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.broadcaster.helper.containsEventAction
import io.mockk.mockk
import org.junit.Rule
import com.tokopedia.dialog.R as dialogR
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.empty_state.R as empty_stateR

/**
 * Created by kenny.hadisaputra on 02/03/22
 */
class ProductChooserRobot(
    listener: ProductChooserBottomSheet.Listener? = null,
    viewModel: (SavedStateHandle) -> ContentProductPickerSellerViewModel = {
        productSetupViewModel(handle = it)
    },
) {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val analyticFile = "tracker/content/playbroadcaster/play_broadcaster_analytic.json"

    private val context = InstrumentationRegistry.getInstrumentation().context

    private val onAttach: (Fragment) -> Unit = {
        when (it) {
            is ProductChooserBottomSheet -> it.setListener(listener)
        }
    }

    val scenario = launchFragment(themeResId = empty_stateR.style.AppTheme) {
        ProductSetupContainer(viewModel, onAttach) {
            when (it) {
                ProductSortBottomSheet::class.java.name -> {
                    ProductSortBottomSheet(mockk(relaxed = true))
                }
                else -> {
                    ProductChooserBottomSheet(
                        CoroutineDispatchersProvider,
                        mockk(relaxed = true),
                        analyticManager = ProductChooserAnalyticManager(
                            analytic = PlayBroSetupProductAnalyticImpl(
                                userSession = analyticUserSession,
                            ),
                            CoroutineDispatchersProvider,
                        ),
                        mockk(relaxed = true),
                    )
                }
            }
        }
    }

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)
        delay()
    }

    fun close() = chainable {
        onView(
            ViewMatchers.withId(unifycomponentsR.id.bottom_sheet_close)
        ).perform(click())
    }

    fun confirmClose() = chainable {
        onView(
            ViewMatchers.withId(dialogR.id.dialog_btn_secondary)
        ).perform(click())
    }

    fun cancelClose() = chainable {
        onView(
            ViewMatchers.withId(dialogR.id.dialog_btn_primary)
        ).perform(click())
    }

    fun selectProduct(position: Int = 0) = chainable {
        onView(
            ViewMatchers.withId(contentproductpickerR.id.rv_products)
        ).perform(RecyclerViewActions.actionOnItemAtPosition<ProductListViewHolder.Product>(
            position, click())
        )
    }

    fun saveProducts() = chainable {
        closeSoftKeyboard()
        onView(
            ViewMatchers.withId(contentproductpickerR.id.btn_next)
        ).perform(click())
    }

    fun clickSortChips() = chainable {
        onView(
            ViewMatchers.withId(contentproductpickerR.id.chips_sort)
        ).perform(click())

        delay(300)
    }

    fun clickEtalaseCampaignChips() = chainable {
        onView(
            ViewMatchers.withId(contentproductpickerR.id.chips_etalase)
        ).perform(click())
    }

    fun searchKeyword(keyword: String) = chainable {
        onView(
            ViewMatchers.withId(contentproductpickerR.id.et_search)
        ).perform(typeText(keyword))
    }

    fun selectSort(position: Int) = chainable {
        onView(
            ViewMatchers.withId(contentproductpickerR.id.rv_sort)
        ).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            position, click())
        )
    }

    fun saveSort() = chainable {
        onView(
            ViewMatchers.withId(unifycomponentsR.id.bottom_sheet_action)
        ).perform(click())
    }

    fun assertBottomSheet(isOpened: Boolean) {
        onView(
            ViewMatchers.withId(unifycomponentsR.id.bottom_sheet_close)
        ).check(
            if (isOpened) ViewAssertions.matches(isDisplayed())
            else doesNotExist()
        )
    }

    fun assertExitDialog(isShown: Boolean) {
        onView(
            ViewMatchers.withText(context.getString(contentproductpickerR.string.product_chooser_exit_dialog_title))
        ).check(
            if (isShown) ViewAssertions.matches(isDisplayed())
            else doesNotExist()
        )
    }

    fun performDelay(timeInMillis: Long = 500) = chainable {
        delay(timeInMillis)
    }

    fun assertEventAction(eventAction: String) = chainable {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction(eventAction)
        )
    }

    private fun chainable(fn: () -> Unit): ProductChooserRobot {
        fn()
        return this
    }
}
