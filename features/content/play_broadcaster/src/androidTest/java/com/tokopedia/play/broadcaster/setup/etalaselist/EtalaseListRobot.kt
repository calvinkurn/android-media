package com.tokopedia.play.broadcaster.setup.etalaselist

import android.view.View
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.setup.product.PlayBroSetupProductAnalyticImpl
import com.tokopedia.play.broadcaster.databinding.ItemEtalaseListBodyBinding
import com.tokopedia.play.broadcaster.helper.analyticUserSession
import com.tokopedia.play.broadcaster.setup.ProductSetupContainer
import com.tokopedia.play.broadcaster.setup.product.analytic.EtalaseListAnalyticManager
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.EtalaseListBottomSheet
import com.tokopedia.play.broadcaster.setup.product.view.viewholder.EtalaseListViewHolder
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.content.test.espresso.delay
import io.mockk.mockk
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Created by kenny.hadisaputra on 08/03/22
 */
class EtalaseListRobot(
    viewModel: (SavedStateHandle) -> PlayBroProductSetupViewModel = {
        productSetupViewModel(handle = it)
    },
) {

    private val context = InstrumentationRegistry.getInstrumentation().context

    val scenario = launchFragment(themeResId = com.tokopedia.empty_state.R.style.AppTheme) {
        ProductSetupContainer(viewModel) {
            EtalaseListBottomSheet(
                mockk(relaxed = true),
                analyticManager = EtalaseListAnalyticManager(
                    analytic = PlayBroSetupProductAnalyticImpl(
                        userSession = analyticUserSession,
                    ),
                    CoroutineDispatchersProvider,
                )
            )
        }
    }

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)
        delay()
    }

    fun selectEtalase(position: Int = 0) {
        Espresso.onView(
            ViewMatchers.withId(R.id.rv_etalase)
        ).perform(
            RecyclerViewActions.actionOnHolderItem(
                etalaseMatcher(), click()
            ).atPosition(position)
        )
    }

    fun selectCampaign(position: Int = 0) {
        Espresso.onView(
            ViewMatchers.withId(R.id.rv_etalase)
        ).perform(
            RecyclerViewActions.actionOnHolderItem(
                campaignMatcher(), click()
            ).atPosition(position)
        )
    }

    private fun etalaseMatcher() =
        object : TypeSafeMatcher<EtalaseListViewHolder.Body>() {

            override fun describeTo(description: Description) {
                description.appendText(
                    "with condition matches etalase card"
                )
            }

            override fun matchesSafely(item: EtalaseListViewHolder.Body): Boolean {
                val binding = ItemEtalaseListBodyBinding.bind(item.itemView)
                return binding.labelStatus.visibility == View.GONE
            }
        }

    private fun campaignMatcher() =
        object : TypeSafeMatcher<EtalaseListViewHolder.Body>() {
            override fun describeTo(description: Description) {
                description.appendText(
                    "with condition matches campaign card"
                )
            }

            override fun matchesSafely(item: EtalaseListViewHolder.Body): Boolean {
                val binding = ItemEtalaseListBodyBinding.bind(item.itemView)

                return binding.labelStatus.visibility == View.VISIBLE
            }
        }
}
