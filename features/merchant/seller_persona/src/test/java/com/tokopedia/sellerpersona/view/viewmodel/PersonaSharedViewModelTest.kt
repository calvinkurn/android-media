package com.tokopedia.sellerpersona.view.viewmodel

import com.tokopedia.sellerpersona.data.remote.model.PersonaStatusModel
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaStatusUseCase
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
 * Created by @ilhamsuaib on 17/02/23.
 */
@ExperimentalCoroutinesApi
class PersonaSharedViewModelTest : BaseViewModelTest() {

    @RelaxedMockK
    lateinit var getPersonaStatusUseCase: GetPersonaStatusUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    lateinit var viewModel: PersonaSharedViewModel

    override fun initVariables() {
        viewModel = PersonaSharedViewModel(
            { getPersonaStatusUseCase },
            { userSession },
            coroutineTestRule.dispatchers
        )
    }

    @Test
    fun `when fetch persona status then return success result`() {
        coroutineTestRule.runBlockingTest {
            val pageName = "seller-home"
            val shopId = anyString()
            val personaStatus = PersonaStatusModel()

            coEvery {
                getPersonaStatusUseCase.execute(shopId, pageName)
            } returns personaStatus

            viewModel.fetchPersonaStatus()

            coVerify {
                getPersonaStatusUseCase.execute(shopId, pageName)
            }

            viewModel.personaStatus.verifySuccessEquals(Success(personaStatus))
        }
    }

    @Test
    fun `when fetch persona status then return fail result`() {
        coroutineTestRule.runBlockingTest {
            val pageName = "seller-home"
            val shopId = anyString()
            val throwable = Throwable()

            coEvery {
                getPersonaStatusUseCase.execute(shopId, pageName)
            } throws throwable

            viewModel.fetchPersonaStatus()

            coVerify {
                getPersonaStatusUseCase.execute(shopId, pageName)
            }

            viewModel.personaStatus.verifyErrorEquals(Fail(throwable))
        }
    }
}