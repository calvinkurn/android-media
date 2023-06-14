package com.tokopedia.people.robot

import android.content.Intent
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkManager
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.util.click
import com.tokopedia.content.test.util.clickTabLayout
import com.tokopedia.content.test.util.pressBack
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.people.builder.ProfileModelBuilder
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.di.UserProfileInjector
import com.tokopedia.people.di.DaggerUserProfileTestComponent
import com.tokopedia.people.di.UserProfileTestModule
import com.tokopedia.people.views.activity.UserProfileActivity
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import com.tokopedia.people.R
import com.tokopedia.people.activity.UserProfileEmptyActivity
import com.tokopedia.people.helper.PeopleCassavaValidator
import com.tokopedia.people.utils.UserProfileSharedPref
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel
import com.tokopedia.people.views.uimodel.content.UserPlayVideoUiModel
import com.tokopedia.unifycomponents.TabsUnify
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

/**
 * Created By : Jonathan Darwin on June 14, 2023
 */
class UserProfileRobot {

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val context = InstrumentationRegistry.getInstrumentation().context

    private val cassavaValidator = PeopleCassavaValidator.buildForUserReview(cassavaTestRule)

    val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    val mockRepo: UserProfileRepository = mockk(relaxed = true)
    val mockContentCoachMarkManager: ContentCoachMarkManager = mockk(relaxed = true)
    val mockUserProfileSharedPref: UserProfileSharedPref = mockk(relaxed = true)
    val mockRouter: Router = mockk(relaxed = true)

    private val profileModelBuilder = ProfileModelBuilder()

    private val mockUserId = "123"
    private val mockProfile = profileModelBuilder.buildProfile(mockUserId)
    private val mockFollowInfo = profileModelBuilder.buildFollowInfo(mockUserId)
    private val mockProfileCreationInfo = profileModelBuilder.buildProfileCreationInfo()
    private val mockTab = profileModelBuilder.buildProfileTab()
    private val mockProfileSettings = profileModelBuilder.buildProfileSettings()
    private val mockReviewList = profileModelBuilder.buildReviewList()

    fun init() {
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

    fun setUpForReviewAnalyticTest() {
        coEvery { mockUserSession.userId } returns mockUserId
        coEvery { mockUserSession.isLoggedIn } returns true

        coEvery { mockRepo.getProfile(any()) } returns mockProfile
        coEvery { mockRepo.getFollowInfo(any()) } returns mockFollowInfo
        coEvery { mockRepo.getCreationInfo() } returns mockProfileCreationInfo
        coEvery { mockRepo.getUserProfileTab(any()) } returns mockTab
        coEvery { mockRepo.getShopRecom(any()) } returns ShopRecomUiModel()

        coEvery { mockRepo.getPlayVideo(any(), any(), any()) } returns UserPlayVideoUiModel.Empty
        coEvery { mockRepo.getFeedPosts(any(), any(), any()) } returns UserFeedPostsUiModel()
        coEvery { mockRepo.getUserReviewList(any(), any(), any()) } returns mockReviewList

        coEvery { mockRepo.getProfileSettings(any()) } returns mockProfileSettings

        coEvery { mockUserProfileSharedPref.hasBeenShown(any()) } returns true
        coEvery { mockRouter.getIntent(any(), any(), any()) } returns Intent(context, UserProfileEmptyActivity::class.java)
    }

    fun launch() = chainable {
        val intent = RouteManager.getIntent(context, ApplinkConst.PROFILE, mockUserId)
        val scenario = ActivityScenario.launch<UserProfileActivity>(intent)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    fun clickReviewTab() = chainable {
        clickTabLayout(R.id.tab_layout, 2)
    }

    fun clickProfileOptionButton() = chainable {
        click(R.id.btn_option)
    }

    fun clickBack() = chainable {
        pressBack()
    }

    fun verifyEventAction(eventAction: String) = chainable {
        cassavaValidator.verify(eventAction)
    }

    fun verifyOpenScreen(screenName: String) = chainable {
        cassavaValidator.verifyOpenScreen(screenName)
    }

    fun performDelay(delayInMillis: Long = 500) = chainable {
        delay(delayInMillis)
    }

    private fun chainable(action: () -> Unit): UserProfileRobot {
        action()
        return this
    }
}
