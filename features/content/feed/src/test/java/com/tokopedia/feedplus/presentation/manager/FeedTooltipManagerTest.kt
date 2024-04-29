package com.tokopedia.feedplus.presentation.manager

import android.content.Context
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.content.test.util.assertEqualTo
import com.tokopedia.content.test.util.assertFalse
import com.tokopedia.content.test.util.assertTrue
import com.tokopedia.feedplus.data.sharedpref.FeedTooltipPreferences
import com.tokopedia.feedplus.presentation.model.FeedDate
import com.tokopedia.feedplus.presentation.model.FeedTooltipEvent
import com.tokopedia.feedplus.presentation.tooltip.FeedSearchTooltipCategory
import com.tokopedia.feedplus.presentation.tooltip.FeedTooltipManagerImpl
import com.tokopedia.unit.test.dispatcher.UnconfinedTestDispatchers
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Created by Jonathan Darwin on 25 April 2024
 */
@OptIn(ExperimentalCoroutinesApi::class)
class FeedTooltipManagerTest {

    private val tooltipPreferences: FeedTooltipPreferences = mockk(relaxed = true)
    private val uiEventManager: UiEventManager<FeedTooltipEvent> = mockk(relaxed = true)
    private val dispatchers = UnconfinedTestDispatchers

    private val eligibleContentPosition = 3
    private val today = "2024-04-26"
    private val samePeriodWithToday = "2024-04-27"
    private val diffPeriodWithToday = "2024-05-01"
    private val sameDayDiffMonth = "2024-05-26"
    private val sameDayMonthDiffYear = "2025-04-26"

    private lateinit var tooltipManager: FeedTooltipManagerImpl

    @Before
    fun setUp() {
        mockkObject(FeedDate.Companion)
        coEvery { FeedDate.getCurrentDate() } returns FeedDate(today)

        tooltipManager = FeedTooltipManagerImpl(
            tooltipPreferences = tooltipPreferences,
            uiEventManager = uiEventManager,
            dispatchers = dispatchers
        )
    }

    @Test
    fun `isShowTooltip - content position requirement is not met - false`() {
        tooltipManager.isShowTooltip(2).assertFalse()
    }

    @Test
    fun `isShowTooltip - no last time shown - true`() {
        coEvery { tooltipPreferences.getLastTimeSearchTooltipShown() } returns FeedDate("")
        tooltipManager.isShowTooltip(eligibleContentPosition).assertTrue()
    }

    @Test
    fun `isShowTooltip - last time shown period is same with today - false`() {
        coEvery { tooltipPreferences.getLastTimeSearchTooltipShown() } returns FeedDate(samePeriodWithToday)

        tooltipManager.isShowTooltip(eligibleContentPosition).assertFalse()
    }

    @Test
    fun `isShowTooltip - last time shown period is different with today - true`() {
        coEvery { tooltipPreferences.getLastTimeSearchTooltipShown() } returns FeedDate(diffPeriodWithToday)

        tooltipManager.isShowTooltip(eligibleContentPosition).assertTrue()
    }

    @Test
    fun `isShowTooltip - last time shown day is same with today but different month - true`() {
        coEvery { tooltipPreferences.getLastTimeSearchTooltipShown() } returns FeedDate(sameDayDiffMonth)

        tooltipManager.isShowTooltip(eligibleContentPosition).assertTrue()
    }

    @Test
    fun `isShowTooltip - last time shown day & month is same with today but different year - true`() {
        coEvery { tooltipPreferences.getLastTimeSearchTooltipShown() } returns FeedDate(sameDayMonthDiffYear)

        tooltipManager.isShowTooltip(eligibleContentPosition).assertTrue()
    }

    @Test
    fun `isShowTooltip - something went wrong or error - false`() {
        val exception = Exception("Error parsing")
        coEvery { tooltipPreferences.getLastTimeSearchTooltipShown() } throws exception

        tooltipManager.isShowTooltip(eligibleContentPosition).assertFalse()
    }


    @Test
    fun `showTooltipEvent - should trigger both show & dismiss tooltip`() = runTest(dispatchers.coroutineDispatcher) {
        tooltipManager.showTooltipEvent()

        coVerify(exactly = 1) { uiEventManager.emitEvent(FeedTooltipEvent.ShowTooltip(FeedSearchTooltipCategory.Promo)) }
        coVerify(exactly = 1) { uiEventManager.emitEvent(FeedTooltipEvent.DismissTooltip) }
    }

    @Test
    fun `setHasShownTooltip - should have save the current date to shared preferences`() = runTest(dispatchers.coroutineDispatcher) {
        tooltipManager.setHasShownTooltip()

        coVerify(exactly = 1) { tooltipPreferences.setLastTimeSearchTooltipShown(FeedDate(today)) }
    }

    @Test
    fun `clearTooltipEvent - should have clear event on uiEventManager`() = runTest(dispatchers.coroutineDispatcher) {
        val eventId = 123L

        tooltipManager.clearTooltipEvent(eventId)

        coVerify(exactly = 1) { uiEventManager.clearEvent(eventId) }
    }

    @Test
    fun `test feed tooltip category`() {
        for (i in 1..31) {
            val category = FeedSearchTooltipCategory.getByDay(i)

            if (i <= 6) {
                category.assertEqualTo(FeedSearchTooltipCategory.UserAffinity)
            } else if (i <= 12) {
                category.assertEqualTo(FeedSearchTooltipCategory.Creator)
            } else if (i <= 18) {
                category.assertEqualTo(FeedSearchTooltipCategory.Story)
            } else if (i <= 24) {
                category.assertEqualTo(FeedSearchTooltipCategory.Trending)
            } else {
                category.assertEqualTo(FeedSearchTooltipCategory.Promo)
            }
        }
    }
}
