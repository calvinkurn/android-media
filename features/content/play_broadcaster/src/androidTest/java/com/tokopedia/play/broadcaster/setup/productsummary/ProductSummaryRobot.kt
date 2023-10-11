package com.tokopedia.play.broadcaster.setup.productsummary

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.content.test.espresso.clickOnViewChild
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.product.picker.R as contentproductpickerR
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalyticImpl
import com.tokopedia.play.broadcaster.helper.analyticUserSession
import com.tokopedia.play.broadcaster.setup.ProductSetupContainer
import com.tokopedia.content.product.picker.seller.view.bottomsheet.ProductSummaryBottomSheet
import com.tokopedia.content.product.picker.seller.view.viewholder.ProductSummaryViewHolder
import com.tokopedia.content.product.picker.seller.view.viewmodel.ContentProductPickerSGCViewModel
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.content.common.view.fragment.LoadingDialogFragment
import com.tokopedia.play.broadcaster.analytic.pinproduct.PlayBroadcastPinProductAnalyticImpl
import io.mockk.mockk
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import com.tokopedia.unifycomponents.R as unifycomponentsR
import com.tokopedia.empty_state.R as empty_stateR

/**
 * Created by kenny.hadisaputra on 08/03/22
 */
class ProductSummaryRobot(
    viewModel: (SavedStateHandle) -> ContentProductPickerSGCViewModel = {
        productSetupViewModel(handle = it)
    },
) {

    private val context = InstrumentationRegistry.getInstrumentation().context

    val scenario = launchFragment(themeResId = empty_stateR.style.AppTheme) {
        ProductSetupContainer(viewModel) {
            when (it) {
                LoadingDialogFragment::class.java.name -> LoadingDialogFragment()
                else -> ProductSummaryBottomSheet(
                    coachMarkSharedPref = mockk(relaxed = true),
                    analytic = PlayBroSetupProductAnalyticImpl(
                        userSession = analyticUserSession,
                    ),
                    pinnedProductAnalytic = PlayBroadcastPinProductAnalyticImpl(
                        userSession = analyticUserSession,
                        configStore = mockk(relaxed = true)
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
            ViewMatchers.withId(unifycomponentsR.id.bottom_sheet_action)
        ).perform(ViewActions.click())
    }

    fun deleteProduct(position: Int = 0) {
        Espresso.onView(
            ViewMatchers.withId(contentproductpickerR.id.rv_product_summaries)
        ).perform(
            RecyclerViewActions.actionOnHolderItem(
                summaryBodyMatcher(), clickOnViewChild(contentproductpickerR.id.ic_product_summary_delete)
            ).atPosition(position)
        )
    }

    fun clickDone() {
        Espresso.onView(
            ViewMatchers.withId(contentproductpickerR.id.btn_done)
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
