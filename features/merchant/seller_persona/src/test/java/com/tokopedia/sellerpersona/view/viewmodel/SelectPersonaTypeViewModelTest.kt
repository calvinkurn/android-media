package com.tokopedia.sellerpersona.view.viewmodel

import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaListUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.SetPersonaUseCase
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
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
class SelectPersonaTypeViewModelTest : BaseViewModelTest() {

    @RelaxedMockK
    lateinit var getPersonaListUseCase: GetPersonaListUseCase

    @RelaxedMockK
    lateinit var setPersonaUseCase: SetPersonaUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    lateinit var viewModel: SelectPersonaTypeViewModel

    override fun initVariables() {
        viewModel = SelectPersonaTypeViewModel(
            { getPersonaListUseCase },
            { setPersonaUseCase },
            { userSession },
            coroutineTestRule.dispatchers
        )
    }

    @Test
    fun `when get fetch persona list should return success result`() {
        coroutineTestRule.runBlockingTest {
            val personaList = listOf(PersonaUiModel())

            coEvery {
                getPersonaListUseCase.execute()
            } returns personaList

            viewModel.fetchPersonaList()

            coVerify {
                getPersonaListUseCase.execute()
            }

            viewModel.personaList.verifySuccessEquals(Success(personaList))
        }
    }

    @Test
    fun `when get fetch persona list should return fail result`() {
        coroutineTestRule.runBlockingTest {
            val throwable = Throwable()

            coEvery {
                getPersonaListUseCase.execute()
            } throws throwable

            viewModel.fetchPersonaList()

            coVerify {
                getPersonaListUseCase.execute()
            }

            viewModel.personaList.verifyErrorEquals(Fail(throwable))
        }
    }

    @Test
    fun `when set persona then return success result`() {
        coroutineTestRule.runBlockingTest {
            val personaName = anyString()
            val shopId = anyString()

            every {
                userSession.shopId
            } returns shopId

            coEvery {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = personaName,
                    answers = emptyList()
                )
            } returns personaName

            viewModel.setPersona(personaName)

            coVerify {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = personaName,
                    answers = emptyList()
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

            every {
                userSession.shopId
            } returns shopId

            coEvery {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = personaName,
                    answers = emptyList()
                )
            } throws throwable

            viewModel.setPersona(personaName)

            coVerify {
                setPersonaUseCase.execute(
                    shopId = shopId,
                    persona = personaName,
                    answers = emptyList()
                )
            }

            viewModel.setPersonaResult.verifyErrorEquals(Fail(throwable))
        }
    }
}