package com.tokopedia.sellerpersona.view.viewmodel

import com.tokopedia.sellerpersona.data.remote.model.QuestionnaireAnswerParam
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaQuestionnaireUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.SetPersonaUseCase
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

/**
 * Created by @ilhamsuaib on 17/02/23.
 */

@ExperimentalCoroutinesApi
class QuestionnaireViewModelTest : BaseViewModelTest() {

    @RelaxedMockK
    lateinit var getQuestionnaireUseCase: GetPersonaQuestionnaireUseCase

    @RelaxedMockK
    lateinit var setPersonaUseCase: SetPersonaUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    lateinit var viewModel: QuestionnaireViewModel

    override fun initVariables() {
        viewModel = QuestionnaireViewModel(
            { getQuestionnaireUseCase },
            { setPersonaUseCase },
            { userSession },
            coroutineTestRule.dispatchers
        )
    }

    @Test
    fun `when fetch questionnaire then return success result`() {
        coroutineTestRule.runBlockingTest {
            val questionList = listOf(
                QuestionnairePagerUiModel(),
                QuestionnairePagerUiModel(),
                QuestionnairePagerUiModel()
            )

            coEvery {
                getQuestionnaireUseCase.execute()
            } returns questionList

            viewModel.fetchQuestionnaire()

            coVerify {
                getQuestionnaireUseCase.execute()
            }

            viewModel.questionnaire.verifySuccessEquals(Success(questionList))
        }
    }

    @Test
    fun `when fetch questionnaire then return fail result`() {
        coroutineTestRule.runBlockingTest {
            val throwable = Throwable()

            coEvery {
                getQuestionnaireUseCase.execute()
            } throws throwable

            viewModel.fetchQuestionnaire()

            coVerify {
                getQuestionnaireUseCase.execute()
            }

            viewModel.questionnaire.verifyErrorEquals(Fail(throwable))
        }
    }

    @Test
    fun `when set persona then return success result`() {
        coroutineTestRule.runBlockingTest {
            val personaName = anyString()
            val shopId = anyString()
            val answers = listOf(
                QuestionnaireAnswerParam(1L, listOf("a", "c")),
                QuestionnaireAnswerParam(2L, listOf("c", "b")),
            )

            every {
                userSession.shopId
            } returns shopId

            coEvery {
                setPersonaUseCase.execute(
                    shopId = shopId, persona = personaName, answers = answers
                )
            } returns personaName

            viewModel.submitAnswer(answers)

            coVerify {
                setPersonaUseCase.execute(
                    shopId = shopId, persona = personaName, answers = answers
                )
            }

            viewModel.setPersonaResult.verifySuccessEquals(Success(personaName))
        }
    }

    @Test
    fun `when set persona then return fail result`() {
        coroutineTestRule.runBlockingTest {
            val throwable = Throwable()
            val personaName = anyString()
            val shopId = anyString()
            val answers = listOf(
                QuestionnaireAnswerParam(1L, listOf("a", "c")),
                QuestionnaireAnswerParam(2L, listOf("c", "b")),
            )

            every {
                userSession.shopId
            } returns shopId

            coEvery {
                setPersonaUseCase.execute(
                    shopId = shopId, persona = personaName, answers = answers
                )
            } throws throwable

            viewModel.submitAnswer(answers)

            coVerify {
                setPersonaUseCase.execute(
                    shopId = shopId, persona = personaName, answers = answers
                )
            }

            viewModel.setPersonaResult.verifyErrorEquals(Fail(throwable))
        }
    }
}