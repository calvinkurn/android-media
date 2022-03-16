package com.tokopedia.home_account.explicitprofile

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@RunWith(AndroidJUnit4::class)
class ExplicitProfileUiTest : BaseExplicitProfileTest() {

    @Test
    fun selectTabKategoriLain() {
        explicitProfileRobot {
            /**
             * Simulate swipe tab to left
             * 2x swipe to make sure latest tab is displayed
             */
            swipeTabLeft()
            swipeTabLeft()

            selectTabWithText("Kategori Lain")
        } validateComponent {
            shouldViewEmptyPage()
        } validateTracker {
            cassavaRule.validateTrackerOnClickTab("Kategori Lain")
        }
    }

    @Test
    fun saveShoppingPreferences() {
        explicitProfileRobot {
            /**
             * click on chips with text
             * it's expected with duplicate code, to simulate user answers with `Halal` option back to default
             */
            clickAnswerWithText("Halal")
            clickAnswerWithText("Vegan")
            clickAnswerWithText("Halal")
        } validateComponent {
            shouldButtonSaveEnabled()
        } validateTracker {
            cassavaRule.validateTrackerOnClickAnswer("Halal")
            cassavaRule.validateTrackerOnClickAnswer("Vegan")
            cassavaRule.validateTrackerOnSavePreference(true)
        }
    }

    @Test
    fun userAnswerSameWithDefault() {
        explicitProfileRobot {
            /**
             * click on chips with text
             * it's expected with duplicate code, to simulate user answers back to default
             */
            clickAnswerWithText("Halal")
            clickAnswerWithText("Vegan")
            clickAnswerWithText("Halal")
            clickAnswerWithText("Halal")
            clickAnswerWithText("Halal")
            clickAnswerWithText("Vegan")
        } validateComponent {
            shouldButtonSaveDisabled()
        } validateTracker {
            cassavaRule.validateTrackerOnClickAnswer("Halal")
            cassavaRule.validateTrackerOnClickAnswer("Vegan")
        }
    }

    @Test
    fun showSectionInfoBottomSheet() {
        explicitProfileRobot {
            clickOnInfoSection()
        } validateComponent {
            shouldViewBottomSheetSectionInfo()
        } validateTracker {
            cassavaRule.validateTrackerOnClickSectionInfo()
        }
    }
}