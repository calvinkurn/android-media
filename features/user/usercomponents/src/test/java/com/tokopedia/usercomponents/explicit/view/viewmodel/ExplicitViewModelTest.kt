package com.tokopedia.usercomponents.explicit.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.explicit.domain.GetQuestionUseCase
import com.tokopedia.usercomponents.explicit.domain.SaveAnswerUseCase
import com.tokopedia.usercomponents.explicit.domain.UpdateStateUseCase
import com.tokopedia.usercomponents.explicit.domain.model.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ExplicitViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getQuestionUseCase = mockk<GetQuestionUseCase>(relaxed = true)
    private val saveAnswerUseCase = mockk<SaveAnswerUseCase>(relaxed = true)
    private val updateStateUseCase = mockk<UpdateStateUseCase>(relaxed = true)
    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val dispatcherProviderTest = CoroutineTestDispatchersProvider

    private lateinit var viewModel: ExplicitViewModel

    @Before
    fun setUp() {
        viewModel = ExplicitViewModel(getQuestionUseCase, saveAnswerUseCase, updateStateUseCase, userSessionInterface, dispatcherProviderTest)
    }

    @Test
    fun `get status is logged in then return authorized`() {
        val isLoggedIn = true

        coEvery {
            userSessionInterface.isLoggedIn
        } returns isLoggedIn

        val result = viewModel.isLoggedIn()
        assertTrue(result)
    }

    @Test
    fun `get status is logged in then return unauthorized`() {
        val isLoggedIn = false

        coEvery {
            userSessionInterface.isLoggedIn
        } returns isLoggedIn

        val result = viewModel.isLoggedIn()
        assertTrue(!result)
    }

    @Test
    fun `get question then success and response widget not active`() {
        val templateName = "food_preference"
        val data = QuestionDataModel()
        val expected = Pair(false, null)

        coEvery {
            getQuestionUseCase(templateName)
        } returns data
        viewModel.getExplicitContent(templateName)

        val result = viewModel.explicitContent.getOrAwaitValue()
        assertEquals(Success(expected), result)
    }

    @Test
    fun `get question then success and response options is empty`() {
        val templateName = "food_preference"
        val data = QuestionDataModel(
            ExplicitprofileGetQuestion(
                ActiveConfig(value = true),
                template = Template(
                    sections = listOf(
                        SectionsItem(
                            questions = listOf(
                                QuestionsItem(
                                    property = Property(
                                        options = emptyList()
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
        val expected = Pair(false, null)

        coEvery {
            getQuestionUseCase(templateName)
        } returns data
        viewModel.getExplicitContent(templateName)

        val result = viewModel.explicitContent.getOrAwaitValue()
        assertEquals(Success(expected), result)
    }

    @Test
    fun `get question then success and response sections is empty`() {
        val templateName = "food_preference"
        val data = QuestionDataModel(
            ExplicitprofileGetQuestion(
                ActiveConfig(value = true),
                template = Template(
                    sections = listOf(
                        SectionsItem(
                            questions = emptyList()
                        )
                    )
                )
            )
        )
        val expected = Pair(false, null)

        coEvery {
            getQuestionUseCase(templateName)
        } returns data
        viewModel.getExplicitContent(templateName)

        val result = viewModel.explicitContent.getOrAwaitValue()
        assertEquals(Success(expected), result)
    }

    @Test
    fun `get question then success and response questions is empty`() {
        val templateName = "food_preference"
        val data = QuestionDataModel(
            ExplicitprofileGetQuestion(
                ActiveConfig(value = true),
                template = Template(
                    sections = emptyList()
                )
            )
        )
        val expected = Pair(false, null)

        coEvery {
            getQuestionUseCase(templateName)
        } returns data
        viewModel.getExplicitContent(templateName)

        val result = viewModel.explicitContent.getOrAwaitValue()
        assertEquals(Success(expected), result)
    }

    @Test
    fun `get question then success and response widget active`() {
        val templateName = "food_preference"
        val data = QuestionDataModel(
            ExplicitprofileGetQuestion(ActiveConfig(value = true))
        )
        val property = data.explicitprofileGetQuestion.template.sections[0].questions[0].property
        val expected = Pair(true, property)

        coEvery {
            getQuestionUseCase(templateName)
        } returns data
        viewModel.getExplicitContent(templateName)

        val result = viewModel.explicitContent.getOrAwaitValue()
        assertEquals(Success(expected), result)
    }

    @Test
    fun `get question then failed and response throwable`() {
        val templateName = "food_preference"
        val data = Throwable()

        coEvery {
            getQuestionUseCase(templateName)
        } throws data
        viewModel.getExplicitContent(templateName)

        val result = viewModel.explicitContent.getOrAwaitValue()
        assertTrue(result is Fail)
    }

    @Test
    fun `user click positive button and app send answer then response success`() {
        val answer = true
        val data = AnswerDataModel()
        val expected = Success(Pair(OptionsItem(), data.explicitprofileSaveMultiAnswers.message))
        val param = InputParam()

        coEvery {
            saveAnswerUseCase(param)
        } returns data
        viewModel.sendAnswer(answer)

        val result = viewModel.statusSaveAnswer.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `user click positive button and app send answer then response failed`() {
        val answer = true
        val data = Throwable()
        val param = InputParam()

        coEvery {
            saveAnswerUseCase(param)
        } throws data
        viewModel.sendAnswer(answer)

        val result = viewModel.statusSaveAnswer.getOrAwaitValue()
        assertTrue(result is Fail)
    }

    @Test
    fun `user click negative button and app send answer then response success`() {
        val answer = false
        val data = AnswerDataModel()
        val param = InputParam()
        val expected = Success(Pair(OptionsItem(), data.explicitprofileSaveMultiAnswers.message))

        coEvery {
            saveAnswerUseCase(param)
        } returns data
        viewModel.sendAnswer(answer)

        val result = viewModel.statusSaveAnswer.getOrAwaitValue()
        assertEquals(expected, result)
    }

    @Test
    fun `user click negative button and app send answer then response failed`() {
        val answer = false
        val data = Throwable()
        val param = InputParam()

        coEvery {
            saveAnswerUseCase(param)
        } throws data
        viewModel.sendAnswer(answer)

        val result = viewModel.statusSaveAnswer.getOrAwaitValue()
        assertTrue(result is Fail)
    }

    @Test
    fun `send update state and make sure update state only call once then response success`()  {
        val preferenceUpdateState = UpdateStateParam()

        viewModel.updateState()

        coVerify (exactly = 1) {
            updateStateUseCase(preferenceUpdateState)
        }
    }

    @Test
    fun `send update state and make sure update state only call once then response error`()  {
        val preferenceUpdateState = UpdateStateParam()
        val data = Throwable()

        coEvery {
            updateStateUseCase(preferenceUpdateState)
        } throws data

        viewModel.updateState()

        coVerify (exactly = 1) {
            updateStateUseCase(preferenceUpdateState)
        }
    }

    @Test
    fun `send update state then response error`()  {
        val preferenceUpdateState = UpdateStateParam()
        val data = Throwable()

        coEvery {
            updateStateUseCase(preferenceUpdateState)
        } throws data
        viewModel.updateState()

        val result = viewModel.statusUpdateState.getOrAwaitValue()
        assertTrue(!result)
    }

    @Test
    fun `send update state then response success`()  {
        val preferenceUpdateState = UpdateStateParam()
        val data = UpdateStateDataModel()

        coEvery {
            updateStateUseCase(preferenceUpdateState)
        } returns data
        viewModel.updateState()

        val result = viewModel.statusUpdateState.getOrAwaitValue()
        assertTrue(result)
    }

}