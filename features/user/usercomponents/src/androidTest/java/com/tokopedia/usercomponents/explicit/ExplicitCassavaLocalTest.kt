package com.tokopedia.usercomponents.explicit

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.usercomponents.common.stub.di.FakeAppModule
import com.tokopedia.usercomponents.explicit.ExplicitCassava.validateTracker
import com.tokopedia.usercomponents.explicit.di.DaggerFakeExplicitComponent
import com.tokopedia.usercomponents.explicit.fake_view.ExplicitDebugActivity
import com.tokopedia.usercomponents.explicit.fake_view.ExplicitDebugFragment.Companion.component
import com.tokopedia.usercomponents.explicit.stub.data.ExplicitRepositoryStub
import com.tokopedia.usercomponents.explicit.stub.data.TestState
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

    @Before
    fun before() {
        component = DaggerFakeExplicitComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .build()

        repositoryStub = component?.repository() as ExplicitRepositoryStub
    }

    @Test
    fun first_time_launch_then_show_question() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)

        //WHEN
        activityTestRule.launchActivity(Intent())

        //THEN
        cassavaRule.validateTracker(ExplicitCassavaState.TRACKER_ID_31560)
    }

    //error in this case caused response not match with submit answer model
    @Test
    fun submit_positive_answer_then_failed() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)

        //WHEN
        activityTestRule.launchActivity(Intent())
        clickButtonAnswer(isPositive = true)

        //THEN
        cassavaRule.validateTracker(ExplicitCassavaState.TRACKER_ID_31561)
    }

    //error in this case caused response not match with submit answer model
    @Test
    fun submit_negative_answer_then_failed() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)

        //WHEN
        activityTestRule.launchActivity(Intent())
        clickButtonAnswer(isPositive = false)

        //THEN
        cassavaRule.validateTracker(ExplicitCassavaState.TRACKER_ID_31562)
    }

    @Test
    fun submit_positive_answer_then_success() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(TestState.SUBMIT_QUESTION_SUCCESS)

        //WHEN
        clickButtonAnswer(isPositive = true)

        //THEN
        cassavaRule.validateTracker(ExplicitCassavaState.TRACKER_ID_31561)
    }

    @Test
    fun submit_negative_answer_then_success() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(TestState.SUBMIT_QUESTION_SUCCESS)

        //WHEN
        clickButtonAnswer(isPositive = false)

        //THEN
        cassavaRule.validateTracker(ExplicitCassavaState.TRACKER_ID_31562)
    }

    @Test
    fun click_dismiss_when_question_show_then_widget_gone() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())

        //WHEN
        clickOnDismiss(onQuestionPage = true)

        //THEN
        cassavaRule.validateTracker(ExplicitCassavaState.TRACKER_ID_31563)
    }

}