package com.tokopedia.usercomponents.explicit.cassava

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.usercomponents.common.stub.di.FakeAppModule
import com.tokopedia.usercomponents.explicit.cassava.ExplicitCassava.validateTracker
import com.tokopedia.usercomponents.explicit.clickButtonAnswer
import com.tokopedia.usercomponents.explicit.clickOnDismiss
import com.tokopedia.usercomponents.explicit.di.DaggerFakeExplicitComponent
import com.tokopedia.usercomponents.explicit.fake_view.ExplicitDebugActivity
import com.tokopedia.usercomponents.explicit.fake_view.ExplicitDebugFragment.Companion.component
import com.tokopedia.usercomponents.explicit.stub.data.ExplicitRepositoryStub
import com.tokopedia.usercomponents.explicit.stub.data.ExplicitRepositoryState
import com.tokopedia.usercomponents.explicit.stub.data.UserSessionState
import com.tokopedia.usercomponents.explicit.stub.data.UserSessionStub
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@RunWith(AndroidJUnit4::class)
class ExplicitCassavaLocalTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        ExplicitDebugActivity::class.java, false, false
    )

    @get:Rule
    var cassavaRule = CassavaTestRule()

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    private lateinit var repositoryStub: ExplicitRepositoryStub
    private lateinit var userSessionStub: UserSessionStub

    @Before
    fun before() {
        component = DaggerFakeExplicitComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .build()

        repositoryStub = component?.repository() as ExplicitRepositoryStub
        userSessionStub = component?.userSession() as UserSessionStub
    }

    @Test
    fun first_time_launch_then_show_question() {
        // Given
        userSessionStub.setLoggedIn(UserSessionState.AUTHORIZED)
        repositoryStub.setState(ExplicitRepositoryState.SHOW_QUESTION)

        // When
        activityTestRule.launchActivity(Intent())

        // Then
        cassavaRule.validateTracker(ExplicitCassavaState.TRACKER_ID_31560)
    }

    //failed in this case caused response not match with submit answer model
    @Test
    fun submit_positive_answer_then_shown_failed_view() {
        // Given
        userSessionStub.setLoggedIn(UserSessionState.AUTHORIZED)
        repositoryStub.setState(ExplicitRepositoryState.SHOW_QUESTION)

        // When
        activityTestRule.launchActivity(Intent())
        clickButtonAnswer(isPositive = true)

        // Then
        cassavaRule.validateTracker(ExplicitCassavaState.TRACKER_ID_31561)
    }

    //failed in this case caused response not match with submit answer model
    @Test
    fun submit_negative_answer_then_shown_failed_view() {
        // Given
        userSessionStub.setLoggedIn(UserSessionState.AUTHORIZED)
        repositoryStub.setState(ExplicitRepositoryState.SHOW_QUESTION)

        // When
        activityTestRule.launchActivity(Intent())
        clickButtonAnswer(isPositive = false)

        // Then
        cassavaRule.validateTracker(ExplicitCassavaState.TRACKER_ID_31562)
    }

    @Test
    fun submit_positive_answer_then_success() {
        // Given
        userSessionStub.setLoggedIn(UserSessionState.AUTHORIZED)
        repositoryStub.setState(ExplicitRepositoryState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(ExplicitRepositoryState.SUBMIT_QUESTION_SUCCESS)

        // When
        clickButtonAnswer(isPositive = true)

        // Then
        cassavaRule.validateTracker(ExplicitCassavaState.TRACKER_ID_31561)
    }

    @Test
    fun submit_negative_answer_then_success() {
        // Given
        userSessionStub.setLoggedIn(UserSessionState.AUTHORIZED)
        repositoryStub.setState(ExplicitRepositoryState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(ExplicitRepositoryState.SUBMIT_QUESTION_SUCCESS)

        // When
        clickButtonAnswer(isPositive = false)

        // Then
        cassavaRule.validateTracker(ExplicitCassavaState.TRACKER_ID_31562)
    }

    @Test
    fun click_dismiss_when_question_show_then_widget_gone() {
        // Given
        userSessionStub.setLoggedIn(UserSessionState.AUTHORIZED)
        repositoryStub.setState(ExplicitRepositoryState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())

        // When
        clickOnDismiss(onQuestionPage = true)

        // Then
        cassavaRule.validateTracker(ExplicitCassavaState.TRACKER_ID_31563)
    }

}