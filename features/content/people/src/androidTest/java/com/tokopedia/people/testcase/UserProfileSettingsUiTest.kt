package com.tokopedia.people.testcase

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkManager
import com.tokopedia.people.builder.ProfileModelBuilder
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.di.DaggerUserProfileTestComponent
import com.tokopedia.people.di.UserProfileInjector
import com.tokopedia.people.di.UserProfileTestModule
import com.tokopedia.people.utils.UserProfileSharedPref
import com.tokopedia.people.views.activity.ProfileSettingsActivity
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import com.tokopedia.people.R
import com.tokopedia.people.views.uimodel.getReviewSettings
import com.tokopedia.test.application.compose.createAndroidIntentComposeRule

/**
 * Created By : Jonathan Darwin on August 08, 2023
 */
class UserProfileSettingsUiTest {

    @get:Rule
    val composeActivityTestRule = createAndroidIntentComposeRule<ProfileSettingsActivity> { context ->
        Intent(context, ProfileSettingsActivity::class.java).apply {
            data = "tokopedia://people/settings/$mockUserId"
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().context

    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockRepo: UserProfileRepository = mockk(relaxed = true)
    private val mockContentCoachMarkManager: ContentCoachMarkManager = mockk(relaxed = true)
    private val mockUserProfileSharedPref: UserProfileSharedPref = mockk(relaxed = true)
    private val mockRouter: Router = mockk(relaxed = true)

    private val profileModelBuilder = ProfileModelBuilder()

    private val mockUserId = "123123"
    private val mockProfileSettings = profileModelBuilder.buildProfileSettings()

    init {
        coEvery { mockRepo.getProfileSettings(mockUserId) } returns mockProfileSettings
        coEvery { mockRepo.setShowReview(any(), any(), true) } returns true
        coEvery { mockRepo.setShowReview(any(), any(), false) } returns false

        UserProfileInjector.set(
            DaggerUserProfileTestComponent.builder()
                .baseAppComponent(
                    (context.applicationContext as BaseMainApplication).baseAppComponent,
                )
                .userProfileTestModule(
                    UserProfileTestModule(
                        activityContext = context,
                        mockUserSession = mockUserSession,
                        mockRepo = mockRepo,
                        mockContentCoachMarkManager = mockContentCoachMarkManager,
                        mockUserProfileSharedPref = mockUserProfileSharedPref,
                        mockRouter = mockRouter,
                    )
                )
                .build()
        )
    }

    @Test
    fun userProfileSettings_toggleReviewSettings() {

        /** Note: need to comment NestHeader first because
         * Compose UI Test is failing due to this error within NestHeader:
         *
         * No static method weight$default(Landroidx/compose/foundation/layout/RowScope;Landroidx/compose/ui/Modifier;FZILjava/lang/Object;)
         * Landroidx/compose/ui/Modifier; in class Landroidx/compose/foundation/layout/RowScope
         */

        Thread.sleep(1000)

        composeActivityTestRule.onNodeWithText(mockProfileSettings.getReviewSettings().title).assertExists()

        composeActivityTestRule.onNodeWithTag("review_toggle").performClick()

        Thread.sleep(1000)

        composeActivityTestRule.onNodeWithTag("review_toggle").performClick()

        onView(withText(context.getString(R.string.up_error_unknown)))
            .check(matches(isDisplayed()))
    }
}
