package com.tokopedia.sellerpersona.view.compose.viewmodel

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerpersona.data.remote.model.QuestionnaireAnswerParam
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaQuestionnaireUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.SetPersonaUseCase
import com.tokopedia.sellerpersona.view.compose.model.state.QuestionnaireState
import com.tokopedia.sellerpersona.view.compose.model.uieffect.QuestionnaireUiEffect
import com.tokopedia.sellerpersona.view.compose.model.uievent.QuestionnaireUserEvent
import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel
import com.tokopedia.sellerpersona.view.model.QuestionnaireDataUiModel
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

/**
 * Created by @ilhamsuaib on 09/10/23.
 */

@OptIn(ExperimentalCoroutinesApi::class)
class ComposeQuestionnaireViewModelTest :
    BaseViewModelTest<QuestionnaireState, QuestionnaireUiEffect>() {

    @RelaxedMockK
    lateinit var getPersonaQuestionnaireUseCase: GetPersonaQuestionnaireUseCase

    @RelaxedMockK
    lateinit var setPersonaUseCase: SetPersonaUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    private lateinit var viewModel: ComposeQuestionnaireViewModel

    override fun setup() {
        super.setup()

        viewModel = ComposeQuestionnaireViewModel(
            { getPersonaQuestionnaireUseCase },
            { setPersonaUseCase },
            { userSession },
            coroutineTestDispatchersProvider
        )

        super.initStateAndUiEffect(viewModel.state, viewModel.uiEffect)
    }

    @Test
    fun `when fetch questionnaire then return success state`() {
        runStateAndUiEffectTest {

            val successState = getSuccessState()

            runTestFetchQuestionnaire(successState)

            assert(states[0] == QuestionnaireState.Loading)
            assertEquals(successState.toString(), states[1].toString())
        }
    }

    @Test
    fun `when fetch questionnaire then return error state`() {
        runStateAndUiEffectTest {

            val throwable = Throwable()

            coEvery {
                getPersonaQuestionnaireUseCase.execute()
            } throws throwable

            viewModel.onEvent(QuestionnaireUserEvent.FetchQuestionnaire)

            coVerify {
                getPersonaQuestionnaireUseCase.execute()
            }

            //default state
            assert(states[0] == QuestionnaireState.Loading)

            assertEquals(QuestionnaireState.Error, states[1])
        }
    }

    @Test
    fun `when user swipe the page twice should update the currentPage value twice as well`() {
        runStateAndUiEffectTest {
            val defaultPage = 1
            val data = QuestionnaireDataUiModel(currentPage = defaultPage)
            val firstSwipe =
                QuestionnaireState.Success(data = data.copy(currentPage = defaultPage.plus(1)))
            val secondSwipe =
                QuestionnaireState.Success(data = data.copy(currentPage = defaultPage.plus(1)))

            viewModel.onEvent(QuestionnaireUserEvent.OnPagerSwipe(firstSwipe.data.currentPage))

            //default state
            assert(states[0] == QuestionnaireState.Loading)
            assertEquals(firstSwipe.toString(), states[1].toString())

            viewModel.onEvent(QuestionnaireUserEvent.OnPagerSwipe(secondSwipe.data.currentPage))

            assertEquals(secondSwipe.toString(), states[1].toString())
        }
    }

    @Test
    fun `when click next button and current page is less then page count should move to next page`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState()

            runTestClickNextEventToMoveToNextPage(successState)
        }
    }

    @Test
    fun `when click next button and reach max page should submit answer then success`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState()

            runTestClickNextEventToMoveToNextPage(successState)

            val shopId = anyString()
            val personaName = anyString()
            val newPersona = "newPersona"
            val lastPageIndex = successState.data.questionnaireList.size.minus(1)

            val answers = successState.data.questionnaireList.map { pager ->
                QuestionnaireAnswerParam(id = pager.id.toLongOrZero(),
                    answers = pager.options.filter { it.isSelected }.map { it.value })
            }

            every {
                userSession.shopId
            } returns shopId

            coEvery {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = personaName,
                    answers = answers
                )
            } returns newPersona

            //third click next
            viewModel.onEvent(QuestionnaireUserEvent.ClickNext)

            val nextLoadingButton = successState.copy(
                data = successState.data.copy(
                    currentPage = lastPageIndex,
                    isNextButtonLoading = true
                )
            )

            coVerify {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = personaName,
                    answers = answers
                )
            }

            assertEquals(nextLoadingButton.toString(), states[4].toString())

            val dismissLoadingButton = nextLoadingButton.copy(
                data = nextLoadingButton.data.copy(
                    currentPage = nextLoadingButton.data.currentPage,
                    isNextButtonLoading = false
                )
            )

            assertEquals(dismissLoadingButton.toString(), states[5].toString())
            assertEquals(
                QuestionnaireUiEffect.NavigateToResultPage(newPersona).toString(),
                uiEffects[0].toString()
            )
        }
    }

    @Test
    fun `when click next button and reach max page should submit answer then failed`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState()

            //make one option item selected for each question
            successState.data.questionnaireList.forEach {
                it.options.forEachIndexed { index, option ->
                    option.isSelected = index == 0
                }
            }

            runTestClickNextEventToMoveToNextPage(successState)

            val shopId = anyString()
            val personaName = anyString()
            val lastPageIndex = successState.data.questionnaireList.size.minus(1)

            val answers = successState.data.questionnaireList.map { pager ->
                QuestionnaireAnswerParam(id = pager.id.toLongOrZero(),
                    answers = pager.options.filter { it.isSelected }.map { it.value })
            }

            every {
                userSession.shopId
            } returns shopId

            coEvery {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = personaName,
                    answers = answers
                )
            } throws Throwable()

            //third click next
            viewModel.onEvent(QuestionnaireUserEvent.ClickNext)

            val nextLoadingButton = successState.copy(
                data = successState.data.copy(
                    currentPage = lastPageIndex,
                    isNextButtonLoading = true
                )
            )

            coVerify {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = personaName,
                    answers = answers
                )
            }

            assertEquals(nextLoadingButton.toString(), states[4].toString())

            val dismissLoadingButton = nextLoadingButton.copy(
                data = nextLoadingButton.data.copy(
                    currentPage = nextLoadingButton.data.currentPage,
                    isNextButtonLoading = false
                )
            )

            assertEquals(dismissLoadingButton.toString(), states[5].toString())
            assert(uiEffects[0] == QuestionnaireUiEffect.ShowGeneralErrorToast)
        }
    }

    @Test
    fun `when click previous button and current page is more then 0 should move to previous page`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState()

            runTestFetchQuestionnaire(successState)

            assert(states[0] == QuestionnaireState.Loading)
            assertEquals(successState.toString(), states[1].toString())

            //move to next 2 page
            viewModel.onEvent(QuestionnaireUserEvent.ClickNext)
            viewModel.onEvent(QuestionnaireUserEvent.ClickNext)

            //first click prev button
            viewModel.onEvent(QuestionnaireUserEvent.ClickPrevious)

            val clickPreviousState = successState.copy(
                data = successState.data.copy(
                    currentPage = successState.data.questionnaireList.size.minus(1)
                )
            )

            val firstClick = clickPreviousState.copy(
                data = clickPreviousState.data.copy(
                    currentPage = clickPreviousState.data.currentPage.minus(1)
                )
            )
            assertEquals(firstClick.toString(), states[4].toString())

            //second click prev button
            viewModel.onEvent(QuestionnaireUserEvent.ClickPrevious)

            val secondClick = firstClick.copy(
                data = firstClick.data.copy(
                    currentPage = firstClick.data.currentPage.minus(1)
                )
            )
            assertEquals(secondClick.toString(), states[5].toString())

            //third times click prev button
            viewModel.onEvent(QuestionnaireUserEvent.ClickPrevious)

            /**
             * Based on above events, these are the states covered :
             * [0] Loading state as default state
             * [1] Fetch questionnaire successfully
             * [2] Click next button
             * [3] Click next button
             * [4] Click previous button
             * [5] Click previous button
             * [6] Click previous button
             */
        }
    }

    @Test
    fun `when get anyChanges status should return true if any question answered`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState()

            runTestFetchQuestionnaire(successState)

            assert(states[0] == QuestionnaireState.Loading)
            assertEquals(successState.toString(), states[1].toString())

            successState.data.questionnaireList.forEach {
                it.options.forEach { o ->
                    o.isSelected = true
                }
            }

            val actual = viewModel.isAnyChanges()

            assert(actual)
        }
    }

    @Test
    fun `when get anyChanges status should return false if no questions answered`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState()

            runTestFetchQuestionnaire(successState)

            assert(states[0] == QuestionnaireState.Loading)
            assertEquals(successState.toString(), states[1].toString())

            val actual = viewModel.isAnyChanges()

            assert(!actual)
        }
    }

    @Test
    fun `when click single questionnaire answer option should update the state data`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState()
            runTestFetchQuestionnaire(successState)

            assert(states[0] == QuestionnaireState.Loading)
            assertEquals(successState.toString(), states[1].toString())

            val pagePosition = 0
            val questionnaireList = successState.data.questionnaireList.toMutableList()
            val question = successState.data.questionnaireList[pagePosition]
            val options = question.options.toMutableList()
            val option = options[0]

            viewModel.onEvent(
                QuestionnaireUserEvent.OnOptionItemSelected(
                    pagePosition = pagePosition,
                    option = option,
                    isChecked = true
                )
            )

            options[0] = option.copyData(true)
            questionnaireList[pagePosition] = question.copy(options = options.toList())

            val newStateData = successState.copy(data = successState.data.copy(
                questionnaireList = questionnaireList.toList()
            ))

            assertEquals(newStateData.toString(), states[2].toString())

            //when click selected answer should done nothing

            viewModel.onEvent(
                QuestionnaireUserEvent.OnOptionItemSelected(
                    pagePosition = pagePosition,
                    option = option.copyData(true),
                    isChecked = true
                )
            )
        }
    }

    @Test
    fun `when click multiple questionnaire answer option should update the state data`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState()
            runTestFetchQuestionnaire(successState)

            assert(states[0] == QuestionnaireState.Loading)
            assertEquals(successState.toString(), states[1].toString())

            val pagePosition = 1
            val questionnaireList = successState.data.questionnaireList.toMutableList()
            val question = successState.data.questionnaireList[pagePosition]
            val options = question.options.toMutableList()
            val option = options[0]

            viewModel.onEvent(
                QuestionnaireUserEvent.OnOptionItemSelected(
                    pagePosition = pagePosition,
                    option = option,
                    isChecked = true
                )
            )

            options[0] = option.copyData(true)
            questionnaireList[pagePosition] = question.copy(options = options.toList())

            val newStateData = successState.copy(data = successState.data.copy(
                questionnaireList = questionnaireList.toList()
            ))

            assertEquals(newStateData.toString(), states[2].toString())
        }
    }

    private fun TestScope.runTestFetchQuestionnaire(successState: QuestionnaireState.Success) {
        coEvery {
            getPersonaQuestionnaireUseCase.execute()
        } returns successState.data.questionnaireList

        viewModel.onEvent(QuestionnaireUserEvent.FetchQuestionnaire)

        coVerify {
            getPersonaQuestionnaireUseCase.execute()
        }
    }

    private fun TestScope.runTestClickNextEventToMoveToNextPage(successState: QuestionnaireState.Success) {
        coEvery {
            getPersonaQuestionnaireUseCase.execute()
        } returns successState.data.questionnaireList

        //fetch questionnaire list
        viewModel.onEvent(QuestionnaireUserEvent.FetchQuestionnaire)

        coVerify {
            getPersonaQuestionnaireUseCase.execute()
        }

        assert(states[0] == QuestionnaireState.Loading)
        assertEquals(successState.toString(), states[1].toString())

        //first click next
        viewModel.onEvent(QuestionnaireUserEvent.ClickNext)

        val firstNextClickState = successState.copy(
            data = successState.data.copy(
                currentPage = 1
            )
        )

        assertEquals(firstNextClickState.toString(), states[2].toString())

        //second click next
        viewModel.onEvent(QuestionnaireUserEvent.ClickNext)

        val secondNextClickState = firstNextClickState.copy(
            data = firstNextClickState.data.copy(
                currentPage = 2
            )
        )

        assertEquals(secondNextClickState.toString(), states[3].toString())
    }

    private fun getSuccessState(): QuestionnaireState.Success {
        return QuestionnaireState.Success(
            QuestionnaireDataUiModel(
                currentPage = 0,
                isNextButtonLoading = false,
                questionnaireList = listOf(
                    QuestionnairePagerUiModel(
                        id = "question-a",
                        type = QuestionnairePagerUiModel.QuestionnaireType.SINGLE_ANSWER,
                        options = listOf(
                            BaseOptionUiModel.QuestionOptionSingleUiModel(
                                value = "a"
                            ),
                            BaseOptionUiModel.QuestionOptionSingleUiModel(
                                value = "b"
                            ),
                            BaseOptionUiModel.QuestionOptionSingleUiModel(
                                value = "c"
                            )
                        )
                    ),
                    QuestionnairePagerUiModel(
                        id = "question-b",
                        type = QuestionnairePagerUiModel.QuestionnaireType.MULTIPLE_ANSWER,
                        options = listOf(
                            BaseOptionUiModel.QuestionOptionMultipleUiModel(
                                value = "a"
                            ),
                            BaseOptionUiModel.QuestionOptionMultipleUiModel(
                                value = "b"
                            ),
                            BaseOptionUiModel.QuestionOptionMultipleUiModel(
                                value = "c"
                            )
                        )
                    ),
                    QuestionnairePagerUiModel(
                        id = "question-c",
                        type = QuestionnairePagerUiModel.QuestionnaireType.MULTIPLE_ANSWER,
                        options = listOf(
                            BaseOptionUiModel.QuestionOptionMultipleUiModel(
                                value = "a"
                            ),
                            BaseOptionUiModel.QuestionOptionMultipleUiModel(
                                value = "b"
                            ),
                            BaseOptionUiModel.QuestionOptionMultipleUiModel(
                                value = "c"
                            )
                        )
                    )
                )
            )
        )
    }
}