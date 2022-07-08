package com.tokopedia.usercomponents.explicit

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.usercomponents.explicit.ExplicitAction.clickButtonAnswer
import com.tokopedia.usercomponents.explicit.ExplicitAction.clickOnDismiss
import com.tokopedia.usercomponents.explicit.ExplicitAction.initQuestionDisplayed
import com.tokopedia.usercomponents.explicit.ExplicitAction.isErrorDisplayed
import com.tokopedia.usercomponents.explicit.ExplicitAction.isHideQuestion
import com.tokopedia.usercomponents.explicit.ExplicitAction.isSuccessDisplayed
import com.tokopedia.usercomponents.explicit.fake_view.ExplicitDebugActivity
import com.tokopedia.usercomponents.explicit.stub.data.ExplicitUseCaseStub
import com.tokopedia.usercomponents.explicit.stub.data.TestState
import com.tokopedia.usercomponents.stub.di.FakeBaseAppComponentBuilder.getComponent
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

    private lateinit var repositoryStub: ExplicitUseCaseStub

    @Before
    fun before() {
        val fakeBaseComponent = getComponent(applicationContext)
        ApplicationProvider.getApplicationContext<BaseMainApplication>()
            .setComponent(fakeBaseComponent)
        repositoryStub = fakeBaseComponent.repo() as ExplicitUseCaseStub
    }

    @Test
    fun first_time_launch_then_show_question() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)

        //WHEN
        activityTestRule.launchActivity(Intent())

        //THEN
        initQuestionDisplayed()
    }

    @Test
    fun first_time_launch_then_hide_question() {
        //GIVEN
        repositoryStub.setState(TestState.HIDE_QUESTION)

        //WHEN
        activityTestRule.launchActivity(Intent())

        //THEN
        isHideQuestion()
    }

    //error in this case caused response not match with question model
    @Test
    fun first_time_launch_then_failed() {
        //GIVEN
        repositoryStub.setState(TestState.SUBMIT_QUESTION_SUCCESS)

        //WHEN
        activityTestRule.launchActivity(Intent())

        //THEN
        isErrorDisplayed()
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
        isErrorDisplayed()
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
        isErrorDisplayed()
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
        isSuccessDisplayed()
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
        isSuccessDisplayed()
    }

    @Test
    fun click_dismiss_when_question_show_then_widget_gone() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())

        //WHEN
        clickOnDismiss(onQuestionPage = true)

        //THEN
        isHideQuestion()
    }

    @Test
    fun click_dismiss_when_success_show_then_widget_gone() {
        //GIVEN
        repositoryStub.setState(TestState.SHOW_QUESTION)
        activityTestRule.launchActivity(Intent())
        repositoryStub.setState(TestState.SUBMIT_QUESTION_SUCCESS)

        //WHEN
        clickButtonAnswer(isPositive = true)
        clickOnDismiss(onQuestionPage = false)

        //THEN
        isHideQuestion()
    }

}