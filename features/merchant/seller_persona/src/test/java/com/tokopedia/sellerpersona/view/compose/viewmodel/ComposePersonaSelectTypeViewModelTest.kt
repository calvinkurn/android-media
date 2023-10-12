package com.tokopedia.sellerpersona.view.compose.viewmodel

import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaListUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.SetPersonaUseCase
import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.compose.model.state.SelectTypeState
import com.tokopedia.sellerpersona.view.compose.model.uieffect.SelectTypeUiEffect
import com.tokopedia.sellerpersona.view.compose.model.uievent.SelectTypeUiEvent
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import java.lang.reflect.Field

/**
 * Created by @ilhamsuaib on 09/10/23.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ComposePersonaSelectTypeViewModelTest :
    BaseViewModelTest<SelectTypeState, SelectTypeUiEffect>() {

    @RelaxedMockK
    lateinit var getPersonaListUseCase: GetPersonaListUseCase

    @RelaxedMockK
    lateinit var setPersonaUseCase: SetPersonaUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    private lateinit var viewModel: ComposePersonaSelectTypeViewModel
    private lateinit var privateState: Field

    @Before
    override fun setup() {
        super.setup()

        viewModel = ComposePersonaSelectTypeViewModel(
            { getPersonaListUseCase },
            { setPersonaUseCase },
            { userSession },
            coroutineTestDispatchersProvider
        )

        privateState = viewModel::class.java.getDeclaredField("_state").apply {
            isAccessible = true
        }

        super.initStateAndUiEffect(viewModel.state, viewModel.uiEffect)
    }

    @Test
    fun `when fetch persona list at initial load with param then return success state`() {
        runStateAndUiEffectTest {
            /*--- GIVEN ---*/
            val defaultPersona = "persona-b"
            val personaList = listOf(
                PersonaUiModel(value = "persona-a", isSelected = false),
                PersonaUiModel(value = "persona-b", isSelected = false)
            )
            val expectedPersonaList = listOf(
                PersonaUiModel(value = "persona-a", isSelected = false),
                PersonaUiModel(value = "persona-b", isSelected = true)
            )

            /*--- WHEN ---*/
            coEvery {
                getPersonaListUseCase.execute()
            } returns personaList

            viewModel.onEvent(SelectTypeUiEvent.FetchPersonaList(getArguments(defaultPersona)))

            /*--- THEN ---*/
            coVerify {
                getPersonaListUseCase.execute()
            }

            //1. show loading state at initial page
            assert(states[0].state == SelectTypeState.State.Loading)
            //2. assert the state and persona list
            assert(states[1].state == SelectTypeState.State.Success)
            assert(states[1].data.personaList == expectedPersonaList)
        }
    }

    @Test
    fun `when fetch persona list at initial load with empty param then return success state`() {
        runStateAndUiEffectTest {
            /*--- GIVEN ---*/
            val defaultPersona = ""
            val personaList = listOf(
                PersonaUiModel(value = "persona-a", isSelected = false),
                PersonaUiModel(value = "persona-b", isSelected = false)
            )

            /*--- WHEN ---*/
            coEvery {
                getPersonaListUseCase.execute()
            } returns personaList

            viewModel.onEvent(SelectTypeUiEvent.FetchPersonaList(getArguments(defaultPersona)))

            /*--- THEN ---*/
            coVerify {
                getPersonaListUseCase.execute()
            }

            //1. show loading state at initial page
            assert(states[0].state == SelectTypeState.State.Loading)
            //2. assert the state and persona list
            assert(states[1].state == SelectTypeState.State.Success)
            assert(states[1].data.personaList == personaList)
        }
    }

    @Test
    fun `when fetch persona list at initial load then return error state`() {
        runStateAndUiEffectTest {
            /*--- GIVEN ---*/
            val errorState = getErrorState()
            val throwable = (errorState.state as SelectTypeState.State.Error).t
            val arguments = getArguments("persona-b")

            /*--- WHEN ---*/
            coEvery {
                getPersonaListUseCase.execute()
            } throws throwable

            viewModel.onEvent(SelectTypeUiEvent.FetchPersonaList(arguments))

            /*--- THEN ---*/
            coVerify {
                getPersonaListUseCase.execute()
            }

            //1. show loading state at initial page
            assert(states[0].state == SelectTypeState.State.Loading)
            //2. assert the error state
            assertEquals(errorState.toString(), states[1].toString())
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `given current persona name = persona-a then user select persona-b then submit should set new persona successfully`() {
        runStateAndUiEffectTest {
            /*--- GIVEN ---*/
            val selectedPersona = "persona-b"
            val defaultPersona = "persona-a"
            val shopId = ArgumentMatchers.anyString()

            val state = privateState.get(viewModel) as MutableStateFlow<SelectTypeState>
            val defaultState = getDefaultState()
            val successState = getSuccessState(paramPersona = defaultPersona)

            state.value = defaultState
            state.value = successState

            /*--- WHEN ---*/
            every {
                userSession.shopId
            } returns shopId

            coEvery {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = selectedPersona,
                    answers = emptyList()
                )
            } returns selectedPersona

            viewModel.onEvent(SelectTypeUiEvent.ClickSubmitButton)

            /*--- THEN ---*/
            coVerify {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = selectedPersona,
                    answers = emptyList()
                )
            }

            //show default state which is loading state
            assert(states[0] == defaultState)
            //show success state
            assert(states[1] == successState)
            //show loading button
            assert(states[2].data.ui.isSelectButtonLoading)
            //dismiss loading button
            assert(!states[3].data.ui.isSelectButtonLoading)
            //update persona data based on selected
            assertEquals(
                SelectTypeUiEffect.OnPersonaChanged(
                    persona = selectedPersona,
                    throwable = null
                ).toString(),
                uiEffects[0].toString()
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `given current persona name = persona-a then user select persona-b then submit but failed should return the throwable`() {
        runStateAndUiEffectTest {
            /*--- GIVEN ---*/
            val selectedPersona = "persona-b"
            val defaultPersona = "persona-a"
            val shopId = ArgumentMatchers.anyString()
            val throwable = Throwable("error message")

            val state = privateState.get(viewModel) as MutableStateFlow<SelectTypeState>
            val defaultState = getDefaultState()
            val successState = getSuccessState(paramPersona = defaultPersona)

            state.value = defaultState
            state.value = successState

            /*--- WHEN ---*/
            every {
                userSession.shopId
            } returns shopId

            coEvery {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = selectedPersona,
                    answers = emptyList()
                )
            } throws throwable

            viewModel.onEvent(SelectTypeUiEvent.ClickSubmitButton)

            /*--- THEN ---*/
            coVerify {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = selectedPersona,
                    answers = emptyList()
                )
            }

            //1. show default state which is loading state
            assert(states[0] == defaultState)
            //2. show success state
            assert(states[1] == successState)
            //3. show loading button
            assert(states[2].data.ui.isSelectButtonLoading)
            //4. dismiss loading button
            assert(!states[3].data.ui.isSelectButtonLoading)
            //5. return the throwable to UI effect
            assertEquals(
                SelectTypeUiEffect.OnPersonaChanged(
                    persona = "",
                    throwable = throwable
                ).toString(),
                uiEffects[0].toString()
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `given current persona name = persona-b then user select persona-b then click submit should close the page`() {
        runStateAndUiEffectTest {
            /*--- GIVEN ---*/
            val defaultPersona = "persona-b"
            val shopId = ArgumentMatchers.anyString()

            val state = privateState.get(viewModel) as MutableStateFlow<SelectTypeState>
            val defaultState = getDefaultState()
            val successState = getSuccessState(paramPersona = defaultPersona)

            state.value = defaultState
            state.value = successState

            /*--- WHEN ---*/
            viewModel.onEvent(SelectTypeUiEvent.ClickSubmitButton)

            /*--- THEN ---*/
            coVerify(inverse = true) {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = defaultPersona,
                    answers = emptyList()
                )
            }

            //show default state which is loading state
            assert(states[0] == defaultState)
            //show success state
            assert(states[1] == successState)
            //update persona data based on selected
            assertEquals(
                SelectTypeUiEffect.CloseThePage(defaultPersona).toString(),
                uiEffects[0].toString()
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `when user select a persona but persona list is empty then click submit should close the page`() {
        runStateAndUiEffectTest {
            /*--- GIVEN ---*/
            val defaultPersona = ""
            val shopId = ArgumentMatchers.anyString()

            val state = privateState.get(viewModel) as MutableStateFlow<SelectTypeState>
            val defaultState = getDefaultState()
            val data = getSuccessState(defaultPersona)
            val successState = data.copy(
                data = data.data.copy(
                    personaList = emptyList()
                )
            )

            state.value = defaultState
            state.value = successState

            /*--- WHEN ---*/
            viewModel.onEvent(SelectTypeUiEvent.ClickSubmitButton)

            /*--- THEN ---*/
            coVerify(inverse = true) {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = defaultPersona,
                    answers = emptyList()
                )
            }

            //show default state which is loading state
            assert(states[0] == defaultState)
            //show success state
            assert(states[1] == successState)
            //update persona data based on selected
            assertEquals(
                SelectTypeUiEffect.CloseThePage(defaultPersona).toString(),
                uiEffects[0].toString()
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `when user click reload then reload the page and return success state`() {
        runStateAndUiEffectTest {
            /*--- GIVEN ---*/
            val defaultPersona = "persona-b"
            val errorState = getErrorState()
            val successState = getSuccessState(paramPersona = defaultPersona)
            val personaList = successState.data.personaList

            val state = privateState.get(viewModel) as MutableStateFlow<SelectTypeState>
            state.value = errorState

            /*--- WHEN ---*/
            coEvery {
                getPersonaListUseCase.execute()
            } returns personaList

            viewModel.onEvent(SelectTypeUiEvent.Reload)

            /*--- THEN ---*/
            coVerify {
                getPersonaListUseCase.execute()
            }

            //1. show loading state at initial page
            assert(states[0].state == SelectTypeState.State.Loading)
            //2. show error state first
            assertEquals(errorState.toString(), states[1].toString())
            //3. show loading state
            assert(states[2].state == SelectTypeState.State.Loading)
            //4. assert the state and persona list
            assert(states[3].state == SelectTypeState.State.Success)
            assert(states[3].data.personaList == personaList)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @Test
    fun `when user click reload then reload the page and return error state`() {
        runStateAndUiEffectTest {
            /*--- GIVEN ---*/
            val errorState = getErrorState()
            val throwable = Throwable("error message")

            val state = privateState.get(viewModel) as MutableStateFlow<SelectTypeState>
            state.value = errorState

            /*--- WHEN ---*/
            coEvery {
                getPersonaListUseCase.execute()
            } throws throwable

            viewModel.onEvent(SelectTypeUiEvent.Reload)

            /*--- THEN ---*/
            coVerify {
                getPersonaListUseCase.execute()
            }

            //1. show loading state at initial page
            assert(states[0].state == SelectTypeState.State.Loading)
            //2. show error state first
            assertEquals(errorState.toString(), states[1].toString())
            //3. show loading state
            assert(states[2].state == SelectTypeState.State.Loading)
            //4. assert the error state
            assertEquals(
                SelectTypeState.State.Error(throwable).toString(),
                states[3].state.toString()
            )
        }
    }

    @Test
    fun `when user reselect active persona card then nothing changes on the UI state`() {
        runStateAndUiEffectTest {
            /*--- GIVEN ---*/
            val paramPersona = "persona-b"
            val successState = getSuccessState(paramPersona)
            val selectedPersona = "persona-b"

            /*--- WHEN ---*/
            coEvery {
                getPersonaListUseCase.execute()
            } returns successState.data.personaList

            viewModel.onEvent(SelectTypeUiEvent.FetchPersonaList(getArguments(paramPersona)))

            /*--- THEN ---*/
            coVerify {
                getPersonaListUseCase.execute()
            }

            /*--- USER SELECT ---*/
            viewModel.onEvent(SelectTypeUiEvent.ClickPersonaCard(selectedPersona))

            /*--- THEN ---*/
            //1. initial state
            assert(states[0].state == SelectTypeState.State.Loading)
            //2. do nothing
            assertEquals(successState.data.toString(), states[1].data.toString())
        }
    }

    @Test
    fun `when user select persona card then update the UI state`() {
        runStateAndUiEffectTest {
            /*--- GIVEN ---*/
            val paramPersona = "persona-b"
            val successState = getSuccessState(paramPersona)

            val selectedPersona = "persona-a"
            val updatedPersonaList = listOf(
                PersonaUiModel(value = selectedPersona, isSelected = true),
                PersonaUiModel(value = "persona-b", isSelected = false)
            )
            val updatedState = SelectTypeState(
                state = SelectTypeState.State.Success,
                data = SelectTypeState.Data(
                    personaList = updatedPersonaList,
                    args = getArguments(paramPersona),
                    ui = SelectTypeState.Ui(
                        isSelectButtonLoading = false,
                        selectedIndex = updatedPersonaList.indexOfFirst { it.isSelected }
                    )
                )
            )

            /*--- WHEN ---*/
            coEvery {
                getPersonaListUseCase.execute()
            } returns successState.data.personaList

            viewModel.onEvent(SelectTypeUiEvent.FetchPersonaList(getArguments(paramPersona)))

            /*--- THEN ---*/
            coVerify {
                getPersonaListUseCase.execute()
            }

            /*--- USER SELECT ---*/
            viewModel.onEvent(SelectTypeUiEvent.ClickPersonaCard(selectedPersona))

            /*--- THEN ---*/
            //1. initial state
            assert(states[0].state == SelectTypeState.State.Loading)
            //2. show state before selecting persona
            assertEquals(successState.data.toString(), states[1].data.toString())
            //3. show state after selecting persona
            assertEquals(updatedState.data.toString(), states[2].data.toString())
        }
    }

    @Test
    fun `when user select persona card but persona is empty then nothing changes on the UI state`() {
        runStateAndUiEffectTest {
            /*--- GIVEN ---*/
            val paramPersona = "persona-b"
            val successState = getSuccessState(paramPersona)

            val selectedPersona = ""

            /*--- WHEN ---*/
            coEvery {
                getPersonaListUseCase.execute()
            } returns successState.data.personaList

            viewModel.onEvent(SelectTypeUiEvent.FetchPersonaList(getArguments(paramPersona)))

            /*--- THEN ---*/
            coVerify {
                getPersonaListUseCase.execute()
            }

            /*--- USER SELECT ---*/
            viewModel.onEvent(SelectTypeUiEvent.ClickPersonaCard(selectedPersona))

            /*--- THEN ---*/
            //1. initial state
            assert(states[0].state == SelectTypeState.State.Loading)
            //2. show state before selecting persona
            assertEquals(successState.data.toString(), states[1].data.toString())
            //3. nothing changes on the UI
        }
    }

    @Test
    fun `when user select persona card but persona list is empty then nothing changes on the UI state`() {
        runStateAndUiEffectTest {
            /*--- GIVEN ---*/
            val paramPersona = ""
            val data = getSuccessState(paramPersona)
            val successState = data.copy(
                data = data.data.copy(
                    personaList = data.data.personaList.map {
                        val p = it.copy()
                        p.isSelected = false
                        p
                    },
                    ui = data.data.ui.copy(selectedIndex = -1)
                )
            )

            val selectedPersona = ""

            /*--- WHEN ---*/
            coEvery {
                getPersonaListUseCase.execute()
            } returns successState.data.personaList

            viewModel.onEvent(SelectTypeUiEvent.FetchPersonaList(getArguments(paramPersona)))

            /*--- THEN ---*/
            coVerify {
                getPersonaListUseCase.execute()
            }

            /*--- USER SELECT ---*/
            viewModel.onEvent(SelectTypeUiEvent.ClickPersonaCard(selectedPersona))

            /*--- THEN ---*/
            //1. initial state
            assert(states[0].state == SelectTypeState.State.Loading)
            //2. show state before selecting persona
            assertEquals(successState.data.toString(), states[1].data.toString())
            //3. nothing changes on the UI
        }
    }

    private fun getDefaultState(): SelectTypeState {
        return SelectTypeState(state = SelectTypeState.State.Loading)
    }

    private fun getErrorState(): SelectTypeState {
        return SelectTypeState(SelectTypeState.State.Error(Throwable()))
    }

    private fun getSuccessState(paramPersona: String): SelectTypeState {
        val personaList = listOf(
            PersonaUiModel(value = "persona-a", isSelected = false),
            PersonaUiModel(value = "persona-b", isSelected = true)
        )
        return SelectTypeState(
            state = SelectTypeState.State.Success,
            data = SelectTypeState.Data(
                personaList = personaList,
                args = getArguments(paramPersona),
                ui = SelectTypeState.Ui(
                    isSelectButtonLoading = false,
                    selectedIndex = personaList.indexOfFirst { it.isSelected }
                )
            )
        )
    }

    private fun getArguments(paramPersona: String = ""): PersonaArgsUiModel {
        return PersonaArgsUiModel(paramPersona = paramPersona)
    }
}