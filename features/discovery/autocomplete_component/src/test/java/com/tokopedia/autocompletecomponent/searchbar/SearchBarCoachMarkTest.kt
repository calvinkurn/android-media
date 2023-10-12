package com.tokopedia.autocompletecomponent.searchbar

import com.tokopedia.autocompletecomponent.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.Test

internal class SearchBarCoachMarkTest: SearchBarViewModelTestFixtures() {
    @Test
    fun `mark CoachMark icon plus already displayed`() {
        `Given coachMarkLocalCache markCoachMarkIconPlusAlreadyDisplayed called just runs`()
        
        `When markCoachMarkIconPlusAlreadyDisplayed called`()

        viewModel.isCoachMarkIconPlusAlreadyDisplayed shouldBe true
        `Then verify coachMark local cache mark show icon coach mark called`()
    }

    private fun `Given coachMarkLocalCache markCoachMarkIconPlusAlreadyDisplayed called just runs`() {
        every {
            coachMarkLocalCache.markShowPlusIconCoachMark()
        } just runs
    }

    private fun `When markCoachMarkIconPlusAlreadyDisplayed called`() {
        viewModel.markCoachMarkIconPlusAlreadyDisplayed()
    }

    private fun `Then verify coachMark local cache mark show icon coach mark called`() {
        verify {
            coachMarkLocalCache.markShowPlusIconCoachMark()
        }
    }
    @Test
    fun `mark CoachMark keyword added already displayed`() {
        `When markCoachMarkKeywordAddedAlreadyDisplayed called`()

        viewModel.isCoachMarkKeywordAddedAlreadyDisplayed shouldBe true
    }

    private fun `When markCoachMarkKeywordAddedAlreadyDisplayed called`() {
        viewModel.markCoachMarkKeywordAddedAlreadyDisplayed()
    }
}
