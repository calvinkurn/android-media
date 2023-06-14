package com.tokopedia.people.robot

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkManager
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.people.builder.ProfileModelBuilder
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.di.UserProfileInjector
import com.tokopedia.people.di.DaggerUserProfileTestComponent
import com.tokopedia.people.di.UserProfileTestModule
import com.tokopedia.people.views.activity.FollowerFollowingListingActivity
import com.tokopedia.people.views.activity.ProfileSettingsActivity
import com.tokopedia.people.views.activity.UserProfileActivity
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule

/**
 * Created By : Jonathan Darwin on June 14, 2023
 */
class UserProfileRobot {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val context = InstrumentationRegistry.getInstrumentation().context

    val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    val mockRepo: UserProfileRepository = mockk(relaxed = true)
    val mockContentCoachMarkManager: ContentCoachMarkManager = mockk(relaxed = true)

    private val profileModelBuilder = ProfileModelBuilder()

    private val mockUserId = "123"
    private val mockProfile = profileModelBuilder.buildProfile(mockUserId)
    private val mockFollowInfo = profileModelBuilder.buildFollowInfo(mockUserId)
    private val mockProfileCreationInfo = profileModelBuilder.buildProfileCreationInfo()
    private val mockTab = profileModelBuilder.buildProfileTab()

    fun init() {
//        UserProfileInjector.set(
//            DaggerUserProfileTestComponent.builder()
//                .baseAppComponent(
//                    (context.applicationContext as BaseMainApplication).baseAppComponent,
//                )
//                .userProfileTestModule(
//                    UserProfileTestModule(
//                        activityContext = context,
//                        mockUserSession = mockUserSession,
//                        mockRepo = mockRepo,
//                        mockContentCoachMarkManager = mockContentCoachMarkManager,
//                    )
//                )
//                .build()
//        )
    }

    fun setUpForReviewAnalyticTest() {
        coEvery { mockUserSession.userId } returns mockUserId
        coEvery { mockUserSession.isLoggedIn } returns true

        coEvery { mockRepo.getProfile(any()) } returns mockProfile
        coEvery { mockRepo.getFollowInfo(any()) } returns mockFollowInfo
        coEvery { mockRepo.getCreationInfo() } returns mockProfileCreationInfo
        coEvery { mockRepo.getUserProfileTab(any()) } returns mockTab
    }

    fun launch() = chainable {
        val intent = RouteManager.getIntent(context, ApplinkConst.PROFILE, mockUserId)
        ActivityScenario.launch<UserProfileActivity>(intent).moveToState(Lifecycle.State.RESUMED)
    }

    private fun chainable(action: () -> Unit): UserProfileRobot {
        action()
        return this
    }
}
