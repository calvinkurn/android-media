package com.tokopedia.play.broadcaster.setup.productchooser

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalyticImpl
import com.tokopedia.play.broadcaster.helper.analyticUserSession
import com.tokopedia.play.broadcaster.setup.ProductSetupContainer
import com.tokopedia.play.broadcaster.setup.product.analytic.ProductChooserAnalyticManager
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductSortBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.ProductListViewHolder
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.content.test.espresso.delay
import io.mockk.mockk
import com.tokopedia.dialog.R as unifyDialogR
import com.tokopedia.unifycomponents.R as unifyR

/**
 * Created by kenny.hadisaputra on 02/03/22
 */
class ProductChooserRobot(
    listener: ProductChooserBottomSheet.Listener? = null,
    viewModel: (SavedStateHandle) -> PlayBroProductSetupViewModel = {
        productSetupViewModel(handle = it)
    },
) {

    private val context = InstrumentationRegistry.getInstrumentation().context

    private val onAttach: (Fragment) -> Unit = {
        when (it) {
            is ProductChooserBottomSheet -> it.setListener(listener)
        }
    }

    val scenario = launchFragment(themeResId = R.style.AppTheme) {
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
            ViewMatchers.withId(unifyR.id.bottom_sheet_close)
        ).perform(click())
    }

    fun confirmClose() {
        onView(
            ViewMatchers.withId(unifyDialogR.id.dialog_btn_secondary)
        ).perform(click())
    }

    fun cancelClose() {
        onView(
            ViewMatchers.withId(unifyDialogR.id.dialog_btn_primary)
        ).perform(click())
    }

    fun selectProduct(position: Int = 0) = chainable {
        onView(
            ViewMatchers.withId(R.id.rv_products)
        ).perform(RecyclerViewActions.actionOnItemAtPosition<ProductListViewHolder.Product>(
            position, click())
        )
    }

    fun saveProducts() = chainable {
        onView(
            ViewMatchers.withId(R.id.btn_next)
        ).perform(click())
    }

    fun clickSortChips() {
        onView(
            ViewMatchers.withId(R.id.chips_sort)
        ).perform(click())

        delay(300)
    }

    fun clickEtalaseCampaignChips() {
        onView(
            ViewMatchers.withId(R.id.chips_etalase)
        ).perform(click())
    }

    fun searchKeyword(keyword: String) {
        onView(
            ViewMatchers.withId(R.id.et_search)
        ).perform(typeText(keyword))
    }

    fun selectSort(position: Int) {
        onView(
            ViewMatchers.withId(R.id.rv_sort)
        ).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
            position, click())
        )
    }

    fun saveSort() {
        onView(
            ViewMatchers.withId(unifyR.id.bottom_sheet_action)
        ).perform(click())
    }

    fun assertBottomSheet(isOpened: Boolean) {
        onView(
            ViewMatchers.withId(unifyR.id.bottom_sheet_close)
        ).check(
            if (isOpened) ViewAssertions.matches(isDisplayed())
            else doesNotExist()
        )
    }

    fun assertExitDialog(isShown: Boolean) {
        onView(
            ViewMatchers.withText(context.getString(R.string.play_bro_product_chooser_exit_dialog_title))
        ).check(
            if (isShown) ViewAssertions.matches(isDisplayed())
            else doesNotExist()
        )
    }

    private fun chainable(fn: () -> Unit): ProductChooserRobot {
        fn()
        return this
    }
}
