package com.tokopedia.play.broadcaster.setup.productchooser

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.helper.waitFor
import com.tokopedia.play.broadcaster.setup.ProductSetupContainer
import com.tokopedia.play.broadcaster.setup.product.analytic.ProductChooserAnalyticManager
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.unifycomponents.R as unifyR
import io.mockk.mockk
import org.hamcrest.Matchers.not

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
            ProductChooserBottomSheet(
                CoroutineDispatchersProvider,
                mockk(relaxed = true),
                analyticManager = ProductChooserAnalyticManager(
                    analytic = mockk(relaxed = true),
                    CoroutineDispatchersProvider,
                )
            )
        }
    }

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)
        delay()
    }

    fun close() {
        onView(
            ViewMatchers.withId(unifyR.id.bottom_sheet_close)
        ).perform(ViewActions.click())
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

    fun delay(delayInMilis: Long = 500) {
        onView(isRoot()).perform(waitFor(delayInMilis))
    }
}