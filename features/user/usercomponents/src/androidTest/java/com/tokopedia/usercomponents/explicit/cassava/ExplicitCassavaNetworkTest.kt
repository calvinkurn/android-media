package com.tokopedia.usercomponents.explicit.cassava

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.usercomponents.common.stub.di.FakeAppModule
import com.tokopedia.usercomponents.explicit.clickButtonAnswer
import com.tokopedia.usercomponents.explicit.clickOnDismiss
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
class ExplicitCassavaNetworkTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        ExplicitDebugActivity::class.java, false, false
    )

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = true)

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
    fun test() {
        //WHEN
        first_time_launch_then_show_question()
        submit_positive_answer_then_shown_failed_view()
        submit_negative_answer_then_shown_failed_view()
        submit_positive_answer_then_success()
        submit_negative_answer_then_success()
        click_dismiss_when_question_show_then_widget_gone()
        click_dismiss_when_success_show_then_widget_gone()

        //THEN
        assertThat(
            cassavaRule.validate(TRACKER_JOURNEY_ID),
            hasAllSuccess()
        )
    }

    private fun first_time_launch_then_show_question() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)

        //WHEN
        activityTestRule.launchActivity(Intent())

        //THEN
        activityTestRule.finishActivity()
    }

    //failed in this case caused response not match with submit answer model
    private fun submit_positive_answer_then_shown_failed_view() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)

        //WHEN
        activityTestRule.launchActivity(Intent())
        clickButtonAnswer(isPositive = true)

        //THEN
        activityTestRule.finishActivity()
    }

    //failed in this case caused response not match with submit answer model
    private fun submit_negative_answer_then_shown_failed_view() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)

        //WHEN
        activityTestRule.launchActivity(Intent())
        clickButtonAnswer(isPositive = false)

        //THEN
        activityTestRule.finishActivity()
    }

    private fun submit_positive_answer_then_success() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(TestState.SUBMIT_QUESTION_SUCCESS)

        //WHEN
        clickButtonAnswer(isPositive = true)

        //THEN
        activityTestRule.finishActivity()
    }

    private fun submit_negative_answer_then_success() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(TestState.SUBMIT_QUESTION_SUCCESS)

        //WHEN
        clickButtonAnswer(isPositive = false)

        //THEN
        activityTestRule.finishActivity()
    }

    private fun click_dismiss_when_question_show_then_widget_gone() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())

        //WHEN
        clickOnDismiss(onQuestionPage = true)

        //THEN
        activityTestRule.finishActivity()
    }

    private fun click_dismiss_when_success_show_then_widget_gone() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(TestState.SUBMIT_QUESTION_SUCCESS)

        //WHEN
        clickButtonAnswer(isPositive = true)
        clickOnDismiss(onQuestionPage = false)

        //THEN
        activityTestRule.finishActivity()
    }

    companion object {
        private const val TRACKER_JOURNEY_ID = "255"
    }

}