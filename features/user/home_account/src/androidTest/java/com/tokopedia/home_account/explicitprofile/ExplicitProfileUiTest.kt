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
            // simulate swipe tab
            swipeTabLeft()
            swipeTabLeft()

            // select tab `Kategori Lain`
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