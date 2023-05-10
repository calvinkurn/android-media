package com.tokopedia.sellerpersona.view.viewmodel

import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaDataUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.TogglePersonaUseCase
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.model.PersonaStatus
import com.tokopedia.unit.test.ext.verifyErrorEquals
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

/**
 * Created by @ilhamsuaib on 13/02/23.
 */

@ExperimentalCoroutinesApi
class PersonaResultViewModelTest : BaseViewModelTest() {

    @RelaxedMockK
    lateinit var getPersonaDataUseCase: GetPersonaDataUseCase

    @RelaxedMockK
    lateinit var togglePersonaUseCase: TogglePersonaUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    private lateinit var viewModel: PersonaResultViewModel

    override fun initVariables() {
        viewModel = PersonaResultViewModel(
            { getPersonaDataUseCase },
            { togglePersonaUseCase },
            { userSession },
            coroutineTestRule.dispatchers
        )
    }

    @Test
    fun `when get persona data should return success result`() {
        coroutineTestRule.runTest {
            val response = PersonaDataUiModel()
            val page = "seller-home"

            coEvery {
                getPersonaDataUseCase.execute(anyString(), page)
            } returns response

            viewModel.fetchPersonaData()

            coVerify {
                getPersonaDataUseCase.execute(anyString(), page)
            }

            viewModel.personaData.verifySuccessEquals(Success(response))
        }
    }

    @Test
    fun `when get persona data should return fail result`() {
        coroutineTestRule.runTest {
            val exception = Throwable()
            val page = "seller-home"

            coEvery {
                getPersonaDataUseCase.execute(anyString(), page)
            } throws exception

            viewModel.fetchPersonaData()

            coVerify {
                getPersonaDataUseCase.execute(anyString(), page)
            }

            viewModel.personaData.verifyErrorEquals(Fail(exception))
        }
    }

    @Test
    fun `when toggle persona status should return success result`() {
        coroutineTestRule.runTest {
            val status = PersonaStatus.ACTIVE

            coEvery {
                togglePersonaUseCase.execute(anyString(), status)
            } returns status

            viewModel.toggleUserPersona(status)

            coVerify {
                togglePersonaUseCase.execute(anyString(), status)
            }

            viewModel.togglePersonaStatus.verifySuccessEquals(Success(status))
        }
    }

    @Test
    fun `when toggle persona status should return fail result`() {
        coroutineTestRule.runTest {
            val status = PersonaStatus.ACTIVE
            val throwable = Throwable()

            coEvery {
                togglePersonaUseCase.execute(anyString(), status)
            } throws throwable

            viewModel.toggleUserPersona(status)

            coVerify {
                togglePersonaUseCase.execute(anyString(), status)
            }

            viewModel.togglePersonaStatus.verifyErrorEquals(Fail(throwable))
        }
    }
}
