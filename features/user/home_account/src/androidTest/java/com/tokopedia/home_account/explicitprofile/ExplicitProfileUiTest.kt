package com.tokopedia.home_account.explicitprofile

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.home_account.explicitprofile.fakes.ExplicitProfileInterceptor
import com.tokopedia.home_account.explicitprofile.fakes.FakeExplicitProfileActivity
import com.tokopedia.test.application.annotations.CassavaTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@RunWith(AndroidJUnit4::class)
class ExplicitProfileUiTest {

    @get:Rule
    var activityRule = IntentsTestRule(
        FakeExplicitProfileActivity::class.java, false, false
    )

    @get:Rule
    var cassavaRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        GraphqlClient.reInitRetrofitWithInterceptors(
            listOf(ExplicitProfileInterceptor(context)),
            context
        )
    }

    @After
    fun tearDown() {
        activityRule.finishActivity()
    }

    @Test
    fun selectTabKategoriLain() {
        activityRule.launchActivity(null)
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
        activityRule.launchActivity(null)


        explicitProfileRobot {
            /**
             * click on chips with text
             * it's expected with duplicate code, to simulate user answers with `Halal` option back to default
             */
            clickAnswerWithText("Halal")
            clickAnswerWithText("Vegan")
            clickAnswerWithText("Halal")
            clickButtonSave()
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
        activityRule.launchActivity(null)
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
        activityRule.launchActivity(null)
        explicitProfileRobot {
            clickOnInfoSection()
        } validateComponent {
            shouldViewBottomSheetSectionInfo()
        } validateTracker {
            cassavaRule.validateTrackerOnClickSectionInfo()
        }
    }
}