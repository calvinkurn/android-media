package com.tokopedia.play.broadcaster.setup.productsummary

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalyticImpl
import com.tokopedia.play.broadcaster.helper.analyticUserSession
import com.tokopedia.play.broadcaster.setup.ProductSetupContainer
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductSummaryBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.ProductSummaryViewHolder
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.play.broadcaster.view.fragment.loading.LoadingDialogFragment
import com.tokopedia.content.test.espresso.clickOnViewChild
import com.tokopedia.content.test.espresso.delay
import io.mockk.mockk
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import com.tokopedia.unifycomponents.R as unifyR

/**
 * Created by kenny.hadisaputra on 08/03/22
 */
class ProductSummaryRobot(
    viewModel: (SavedStateHandle) -> PlayBroProductSetupViewModel = {
        productSetupViewModel(handle = it)
    },
) {

    private val context = InstrumentationRegistry.getInstrumentation().context

    val scenario = launchFragment(themeResId = R.style.AppTheme) {
        ProductSetupContainer(viewModel) {
            when (it) {
                LoadingDialogFragment::class.java.name -> LoadingDialogFragment()
                else -> ProductSummaryBottomSheet(
                    analytic = PlayBroadcastAnalytic(
                        analyticUserSession,
                        setupProductAnalytic = PlayBroSetupProductAnalyticImpl(
                            userSession = analyticUserSession,
                        ),
                        interactiveAnalytic = mockk(relaxed = true),
                        setupMenuAnalytic = mockk(relaxed = true),
                        setupTitleAnalytic = mockk(relaxed = true),
                        setupCoverAnalytic = mockk(relaxed = true),
                        summaryAnalytic = mockk(relaxed = true),
                        scheduleAnalytic = mockk(relaxed = true),
                        pinProductAnalytic = mockk(relaxed = true),
                        accountAnalytic = mockk(relaxed = true)
                    )
                )
            }
        }
    }

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)
        delay()
    }

    fun addMoreProduct() {
        Espresso.onView(
            ViewMatchers.withId(unifyR.id.bottom_sheet_action)
        ).perform(ViewActions.click())
    }

    fun deleteProduct(position: Int = 0) {
        Espresso.onView(
            ViewMatchers.withId(R.id.rv_product_summaries)
        ).perform(
            RecyclerViewActions.actionOnHolderItem(
                summaryBodyMatcher(), clickOnViewChild(R.id.ic_product_summary_delete)
            ).atPosition(position)
        )
    }

    fun clickDone() {
        Espresso.onView(
            ViewMatchers.withId(R.id.btn_done)
        ).perform(ViewActions.click())
    }

    private fun summaryBodyMatcher() =
        object : TypeSafeMatcher<ProductSummaryViewHolder.Body>(
            ProductSummaryViewHolder.Body::class.java
        ) {

            override fun describeTo(description: Description) {
                description.appendText(
                    "with condition matches summary body card"
                )
            }

            override fun matchesSafely(item: ProductSummaryViewHolder.Body): Boolean {
                return true
            }
        }

}
