package com.tokopedia.sellerpersona.view.compose.viewmodel

import com.tokopedia.sellerpersona.data.local.PersonaSharedPrefInterface
import com.tokopedia.sellerpersona.data.remote.model.PersonaStatusModel
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaDataUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaListUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaStatusUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.TogglePersonaUseCase
import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.compose.model.state.PersonaResultState
import com.tokopedia.sellerpersona.view.compose.model.uievent.ResultUiEffect
import com.tokopedia.sellerpersona.view.compose.model.uievent.ResultUiEvent
import com.tokopedia.sellerpersona.view.model.PERSONA_STATUS_ACTIVE
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.model.PersonaStatus
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

/**
 * Created by @ilhamsuaib on 09/10/23.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ComposePersonaResultViewModelTest : BaseViewModelTest<PersonaResultState, ResultUiEffect>() {

    @RelaxedMockK
    lateinit var getPersonaStatusUseCase: GetPersonaStatusUseCase

    @RelaxedMockK
    lateinit var getPersonaListUseCase: GetPersonaListUseCase

    @RelaxedMockK
    lateinit var sharedPref: PersonaSharedPrefInterface

    @RelaxedMockK
    lateinit var togglePersonaUseCase: TogglePersonaUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    lateinit var getPersonaDataUseCase: GetPersonaDataUseCase

    private lateinit var viewModel: ComposePersonaResultViewModel

    override fun setup() {
        super.setup()

        getPersonaDataUseCase = GetPersonaDataUseCase(
            getPersonaStatusUseCase,
            getPersonaListUseCase,
            sharedPref,
            userSession,
            coroutineTestDispatchersProvider
        )

        viewModel = ComposePersonaResultViewModel(
            { getPersonaDataUseCase },
            { togglePersonaUseCase },
            { userSession },
            coroutineTestDispatchersProvider
        )

        initStateAndUiEffect(viewModel.personaState, viewModel.uiEffect)
    }

    @Test
    fun `when fetch result data with no persona then return success`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState("")

            runTestFetchPersonaDataWithSuccessResult(successState)

            assert(states[0] == getDefaultState())
            assertEquals(successState.toString(), states[1].toString())
        }
    }

    @Test
    fun `when fetch result data with persona then return success`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState(getMockDefaultPersona())

            runTestFetchPersonaDataWithSuccessResult(successState)

            assert(states[0] == getDefaultState())
            assertEquals(successState.toString(), states[1].toString())
        }
    }

    @Test
    fun `when fetch result data then return failed`() {
        runStateAndUiEffectTest {
            runTestFetchPersonaDataWithErrorResult()
        }
    }

    @Test
    fun `when reload the page then re-fetch result data then return success result`() {
        runStateAndUiEffectTest {
            //fetch initial data with error result
            runTestFetchPersonaDataWithErrorResult()

            //reload the page
            val successState = getSuccessState()
            val shopId = anyString()
            val page = "seller-home" //must be equal to Constants.PERSONA_PAGE_PARAM
            val mockPersonaStatus = PersonaStatusModel(
                persona = getMockDefaultPersona(),
                status = PERSONA_STATUS_ACTIVE
            )

            coEvery {
                userSession.shopId
            } returns shopId

            coEvery {
                getPersonaStatusUseCase.execute(shopId, page)
            } returns mockPersonaStatus

            coEvery {
                getPersonaListUseCase.execute()
            } returns getMockPersonaList()

            viewModel.onEvent(ResultUiEvent.Reload)

            coVerify {
                getPersonaListUseCase.execute()
            }

            coVerify {
                getPersonaStatusUseCase.execute(shopId, page)
            }

            assertEquals(successState.toString(), states[3].toString())
        }
    }

    @Test
    fun `when reload the page then re-fetch result data then return failed result`() {
        runStateAndUiEffectTest {
            viewModel.onEvent(ResultUiEvent.Reload)

            runTestFetchPersonaDataWithErrorResult()
        }
    }

    @Test
    fun `when toggle persona switch should update the states and emit tracking ui effect`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState(getMockDefaultPersona())

            runTestFetchPersonaDataWithSuccessResult(successState)

            assertEquals(getDefaultState().toString(), states[0].toString())
            assertEquals(successState.toString(), states[1].toString())

            viewModel.onEvent(ResultUiEvent.OnSwitchCheckChanged(false))

            val switchToUnchecked =
                successState.copy(data = successState.data.copy(isSwitchChecked = false))
            assertEquals(switchToUnchecked.toString(), states[2].toString())
            assert(uiEffects[0] == ResultUiEffect.SendSwitchCheckedChangedTracking)

            viewModel.onEvent(ResultUiEvent.OnSwitchCheckChanged(true))

            val switchToChecked =
                successState.copy(data = successState.data.copy(isSwitchChecked = true))
            assertEquals(switchToChecked.toString(), states[3].toString())
            assert(uiEffects[1] == ResultUiEffect.SendSwitchCheckedChangedTracking)
        }
    }

    @Test
    fun `given persona status active then apply changes should success`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState(getMockDefaultPersona())

            runTestFetchPersonaDataWithSuccessResult(successState)

            assertEquals(getDefaultState().toString(), states[0].toString())
            assertEquals(successState.toString(), states[1].toString())

            /** Emit Event ApplyPersona **/

            val shopId = anyString()
            val personaStatus = PersonaStatus.ACTIVE

            every { userSession.shopId } returns shopId
            coEvery { togglePersonaUseCase.execute(shopId, personaStatus) } returns personaStatus

            val applyChangesEvent = ResultUiEvent.ApplyChanges(
                persona = successState.data.persona,
                isActive = successState.data.isSwitchChecked
            )
            viewModel.onEvent(applyChangesEvent)

            verify { userSession.shopId }
            coVerify { togglePersonaUseCase.execute(shopId, any()) }

            //send tracker
            assert(
                uiEffects[0] == ResultUiEffect.SendClickApplyTracking(
                    persona = applyChangesEvent.persona,
                    isActive = applyChangesEvent.isActive
                )
            )

            //show button loading state
            val loadingButton = successState.copy(
                data = successState.data.copy(isApplyLoading = true)
            )
            assertEquals(loadingButton.toString(), states[2].toString())

            //dismiss loading button and update UI
            val personaStatusState = loadingButton.copy(
                data = loadingButton.data.copy(isApplyLoading = false, personaStatus = personaStatus),
                hasImpressed = false
            )
            assertEquals(personaStatusState.toString(), states[3].toString())

            //set on persona status changed
            val actualStatus = (uiEffects[1] as ResultUiEffect.OnPersonaStatusChanged).personaStatus
            assertEquals(personaStatus, actualStatus)
        }
    }

    @Test
    fun `given persona status inactive then apply changes should success`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState(getMockDefaultPersona())

            runTestFetchPersonaDataWithSuccessResult(successState)

            assertEquals(getDefaultState().toString(), states[0].toString())
            assertEquals(successState.toString(), states[1].toString())

            viewModel.onEvent(ResultUiEvent.OnSwitchCheckChanged(false))

            val switchToUnchecked =
                successState.copy(data = successState.data.copy(isSwitchChecked = false))
            assertEquals(switchToUnchecked.toString(), states[2].toString())
            assert(uiEffects[0] == ResultUiEffect.SendSwitchCheckedChangedTracking)

            /** Emit Event ApplyPersona **/

            val shopId = anyString()
            val personaStatus = PersonaStatus.INACTIVE

            every { userSession.shopId } returns shopId
            coEvery { togglePersonaUseCase.execute(shopId, personaStatus) } returns personaStatus

            val applyChangesEvent = ResultUiEvent.ApplyChanges(
                persona = switchToUnchecked.data.persona,
                isActive = switchToUnchecked.data.isSwitchChecked
            )
            viewModel.onEvent(applyChangesEvent)

            verify { userSession.shopId }
            coVerify { togglePersonaUseCase.execute(shopId, any()) }

            //send tracker
            assert(
                uiEffects[1] == ResultUiEffect.SendClickApplyTracking(
                    persona = applyChangesEvent.persona,
                    isActive = applyChangesEvent.isActive
                )
            )

            //show button loading state
            val loadingButton = switchToUnchecked.copy(
                data = switchToUnchecked.data.copy(isApplyLoading = true)
            )
            assertEquals(loadingButton.toString(), states[3].toString())

            //dismiss loading button and update UI
            val personaStatusState = loadingButton.copy(
                data = loadingButton.data.copy(isApplyLoading = false, personaStatus = personaStatus),
                hasImpressed = false
            )
            assertEquals(personaStatusState.toString(), states[4].toString())

            //set on persona status changed
            val actualStatus = (uiEffects[2] as ResultUiEffect.OnPersonaStatusChanged).personaStatus
            assertEquals(personaStatus, actualStatus)
        }
    }

    @Test
    fun `given persona status active or inactive then apply changes should failed`() {
        runStateAndUiEffectTest {
            val successState = getSuccessState(getMockDefaultPersona())

            runTestFetchPersonaDataWithSuccessResult(successState)

            assertEquals(getDefaultState().toString(), states[0].toString())
            assertEquals(successState.toString(), states[1].toString())

            /** Emit Event ApplyPersona **/

            val shopId = anyString()
            val personaStatus = PersonaStatus.ACTIVE
            val throwable = Throwable()

            every { userSession.shopId } returns shopId
            coEvery { togglePersonaUseCase.execute(shopId, personaStatus) } throws throwable

            val applyChangesEvent = ResultUiEvent.ApplyChanges(
                persona = successState.data.persona,
                isActive = successState.data.isSwitchChecked
            )
            viewModel.onEvent(applyChangesEvent)

            verify { userSession.shopId }
            coVerify { togglePersonaUseCase.execute(shopId, any()) }

            //send tracker
            assert(
                uiEffects[0] == ResultUiEffect.SendClickApplyTracking(
                    persona = applyChangesEvent.persona,
                    isActive = applyChangesEvent.isActive
                )
            )

            //show button loading state
            val loadingButton = successState.copy(
                data = successState.data.copy(isApplyLoading = true)
            )
            assertEquals(loadingButton.toString(), states[2].toString())

            //dismiss loading button and update UI
            val dismissLoading = loadingButton.copy(
                data = loadingButton.data.copy(isApplyLoading = false)
            )
            assertEquals(dismissLoading.toString(), states[3].toString())

            //set effect on persona status changed failed
            val actualThrowable = (uiEffects[1] as ResultUiEffect.OnPersonaStatusChanged).throwable
            assertEquals(throwable.toString(), actualThrowable.toString())
        }
    }

    @Test
    fun `when retake quiz should navigate to questionnaire page`() {
        runStateAndUiEffectTest {
            viewModel.onEvent(ResultUiEvent.RetakeQuiz)

            assert(uiEffects[0] == ResultUiEffect.NavigateToQuestionnaire)
        }
    }

    @Test
    fun `when page impression invoked should update the data state`() {
        runStateAndUiEffectTest {
            //on fetch initial data
            val successState = getSuccessState(getMockDefaultPersona())
            runTestFetchPersonaDataWithSuccessResult(successState)
            assertEquals(getDefaultState().toString(), states[0].toString())
            assertEquals(successState.toString(), states[1].toString())

            viewModel.onEvent(ResultUiEvent.OnResultImpressedEvent)

            val impressedState = successState.copy(hasImpressed = true)
            assertEquals(impressedState.toString(), states[2].toString())
        }
    }

    @Test
    fun `given select persona event should navigate to select persona page`() {
        runStateAndUiEffectTest {
            //on fetch initial data
            val successState = getSuccessState(getMockDefaultPersona())
            runTestFetchPersonaDataWithSuccessResult(successState)
            assertEquals(getDefaultState().toString(), states[0].toString())
            assertEquals(successState.toString(), states[1].toString())

            val currentPersona = successState.data.persona

            viewModel.onEvent(ResultUiEvent.SelectPersona(currentPersona))

            assert(uiEffects[0] == ResultUiEffect.NavigateToSelectPersona(currentPersona))
        }
    }

    private fun getErrorState(): PersonaResultState {
        return getDefaultState().copy(
            state = PersonaResultState.State.Error
        )
    }

    private fun TestScope.runTestFetchPersonaDataWithErrorResult() {
        val shopId = anyString()
        val page = "seller-home" //must be equal to Constants.PERSONA_PAGE_PARAM
        val throwable = Throwable()

        coEvery {
            userSession.shopId
        } returns shopId

        coEvery {
            getPersonaStatusUseCase.execute(shopId, page)
        } throws throwable

        coEvery {
            getPersonaListUseCase.execute()
        } returns getMockPersonaList()

        viewModel.onEvent(ResultUiEvent.FetchPersonaData(getArguments()))

        coVerify {
            getPersonaStatusUseCase.execute(shopId, page)
        }

        assert(states[0] == getDefaultState())
        assertEquals(getErrorState().toString(), states[1].toString())
    }

    private fun TestScope.runTestFetchPersonaDataWithSuccessResult(state: PersonaResultState) {
        val shopId = anyString()
        val page = "seller-home" //must be equal to Constants.PERSONA_PAGE_PARAM
        val mockPersonaStatus = PersonaStatusModel(
            persona = getMockDefaultPersona(),
            status = PERSONA_STATUS_ACTIVE
        )

        coEvery {
            userSession.shopId
        } returns shopId

        coEvery {
            getPersonaStatusUseCase.execute(shopId, page)
        } returns mockPersonaStatus

        coEvery {
            getPersonaListUseCase.execute()
        } returns getMockPersonaList()

        viewModel.onEvent(ResultUiEvent.FetchPersonaData(state.data.args))

        coVerify {
            getPersonaListUseCase.execute()
        }

        coVerify {
            getPersonaStatusUseCase.execute(shopId, page)
        }
    }

    private fun getMockDefaultPersona(): String {
        return "persona-x"
    }

    private fun getArguments(persona: String = anyString()): PersonaArgsUiModel {
        return PersonaArgsUiModel(paramPersona = persona)
    }

    private fun getSuccessState(paramPersona: String = anyString()): PersonaResultState {
        return getDefaultState().copy(
            state = PersonaResultState.State.Success,
            data = PersonaDataUiModel(
                persona = getMockDefaultPersona(),
                personaData = PersonaUiModel(value = getMockDefaultPersona()),
                args = getArguments(paramPersona),
                personaStatus = PersonaStatus.ACTIVE,
                isSwitchChecked = true
            )
        )
    }

    private fun getMockPersonaList(): List<PersonaUiModel> {
        return listOf(
            PersonaUiModel(
                value = "persona-x"
            ),
            PersonaUiModel(
                value = "persona-y"
            ),
            PersonaUiModel(
                value = "persona-z"
            )
        )
    }

    private fun getDefaultState() = PersonaResultState(state = PersonaResultState.State.Loading)
}