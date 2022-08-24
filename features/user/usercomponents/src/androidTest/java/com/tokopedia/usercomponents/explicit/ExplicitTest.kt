package com.tokopedia.usercomponents.explicit

import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.usercomponents.common.stub.di.FakeAppModule
import com.tokopedia.usercomponents.explicit.di.DaggerFakeExplicitComponent
import com.tokopedia.usercomponents.explicit.fake_view.ExplicitDebugActivity
import com.tokopedia.usercomponents.explicit.fake_view.ExplicitDebugFragment.Companion.component
import com.tokopedia.usercomponents.explicit.stub.data.ExplicitRepositoryStub
import com.tokopedia.usercomponents.explicit.stub.data.TestState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UiTest
@RunWith(AndroidJUnit4::class)
class ExplicitTest {

    @get:Rule
    var activityTestRule = IntentsTestRule(
        ExplicitDebugActivity::class.java, false, false
    )

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    lateinit var repositoryStub: ExplicitRepositoryStub

    @Before
    fun before() {
        component = DaggerFakeExplicitComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .build()

        repositoryStub = component?.repository() as ExplicitRepositoryStub
    }

    @Test
    fun first_time_launch_then_show_question() {
        // Given
        repositoryStub.setState(TestState.SHOW_QUESTION)

        // When
        activityTestRule.launchActivity(Intent())

        // Then
        initQuestionDisplayed()
    }

    @Test
    fun first_time_launch_then_hide_question() {
        // Given
        repositoryStub.setState(TestState.HIDE_QUESTION)

        // When
        activityTestRule.launchActivity(Intent())

        // Then
        isHideQuestion()
    }

    //failed in this case caused response not match with question model
    @Test
    fun first_time_launch_then_shown_failed_view() {
        // Given
        repositoryStub.setState(TestState.SUBMIT_QUESTION_SUCCESS)

        // When
        activityTestRule.launchActivity(Intent())

        // Then
        isFailedDisplayed()
    }

    //failed in this case caused response not match with submit answer model
    @Test
    fun submit_positive_answer_then_shown_failed_view() {
        // Given
        repositoryStub.setState(TestState.SHOW_QUESTION)

        // When
        activityTestRule.launchActivity(Intent())
        clickButtonAnswer(isPositive = true)

        // Then
        isFailedDisplayed()
    }

    //failed in this case caused response not match with submit answer model
    @Test
    fun submit_negative_answer_then_shown_failed_view() {
        // Given
        repositoryStub.setState(TestState.SHOW_QUESTION)

        // When
        activityTestRule.launchActivity(Intent())
        clickButtonAnswer(isPositive = false)

        // Then
        isFailedDisplayed()
    }

    @Test
    fun submit_positive_answer_then_success() {
        // Given
        repositoryStub.setState(TestState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(TestState.SUBMIT_QUESTION_SUCCESS)

        // When
        clickButtonAnswer(isPositive = true)

        // Then
        isSuccessDisplayed()
    }

    @Test
    fun submit_negative_answer_then_success() {
        // Given
        repositoryStub.setState(TestState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(TestState.SUBMIT_QUESTION_SUCCESS)

        // When
        clickButtonAnswer(isPositive = false)

        // Then
        isSuccessDisplayed()
    }

    @Test
    fun click_dismiss_when_question_show_then_widget_gone() {
        // Given
        repositoryStub.setState(TestState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())

        // When
        clickOnDismiss(onQuestionPage = true)

        // Then
        isHideQuestion()
    }

    @Test
    fun click_dismiss_when_success_show_then_widget_gone() {
        // Given
        repositoryStub.setState(TestState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(TestState.SUBMIT_QUESTION_SUCCESS)

        // When
        clickButtonAnswer(isPositive = true)
        clickOnDismiss(onQuestionPage = false)

        // Then
        isHideQuestion()
    }

}