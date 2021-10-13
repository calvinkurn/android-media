package com.tokopedia.statistic.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.statistic.domain.usecase.CheckWhitelistedStatusUseCase
import com.tokopedia.statistic.domain.usecase.GetUserRoleUseCase
import com.tokopedia.statistic.utils.TestConst
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By @ilhamsuaib on 19/02/21
 */

class StatisticActivityViewModelTest {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getUserRoleUseCase: GetUserRoleUseCase

    @RelaxedMockK
    lateinit var checkWhitelistedStatusUseCase: CheckWhitelistedStatusUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: StatisticActivityViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = StatisticActivityViewModel(
            { userSession },
            { checkWhitelistedStatusUseCase },
            { getUserRoleUseCase },
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `should success when checking user white list status`() = runBlocking {
        val whiteListName = "statistic-operational"
        val params = getMockParams(whiteListName)

        coEvery {
            checkWhitelistedStatusUseCase.createParam(whiteListName)
        } returns params

        coEvery {
            checkWhitelistedStatusUseCase.execute(params)
        } returns true

        viewModel.checkWhiteListStatus()

        val expectedParams = checkWhitelistedStatusUseCase.createParam(whiteListName)

        coVerify {
            checkWhitelistedStatusUseCase.createParam(whiteListName)
        }

        coVerify {
            checkWhitelistedStatusUseCase.execute(expectedParams)
        }

        Assert.assertEquals(params.parameters.toString(), expectedParams.parameters.toString())
        assert(viewModel.whitelistedStatus.value == Success(true))
    }

    @Test
    fun `should return throwable when when checking user white list status `() = runBlocking {
        val whiteListName = "statistic-operational"
        val params = getMockParams(whiteListName)

        coEvery {
            checkWhitelistedStatusUseCase.createParam(whiteListName)
        } returns params

        val exception = MessageErrorException("error message")

        coEvery {
            checkWhitelistedStatusUseCase.execute(params)
        } throws exception

        viewModel.checkWhiteListStatus()

        val actualParams = checkWhitelistedStatusUseCase.createParam(whiteListName)

        coVerify {
            checkWhitelistedStatusUseCase.createParam(whiteListName)
        }

        coVerify {
            checkWhitelistedStatusUseCase.execute(actualParams)
        }

        Assert.assertEquals(params.parameters, actualParams.parameters)

        assert(viewModel.whitelistedStatus.value is Fail)
    }

    @Test
    fun `should success when get user role`() = runBlocking {
        val userId = "123456"
        val userRoles = listOf("a", "b", "c")
        getUserRoleUseCase.params = GetUserRoleUseCase.createParam(userId)

        every {
            userSession.userId
        } returns userId.toString()

        coEvery {
            getUserRoleUseCase.executeOnBackground()
        } returns userRoles

        viewModel.getUserRole()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            userSession.userId
        }

        coVerify {
            getUserRoleUseCase.executeOnBackground()
        }

        Assert.assertEquals(Success(userRoles), viewModel.userRole.value)
    }

    @Test
    fun `when failed to get user role then throws exception`() = runBlocking {
        val userId = "123456"
        val throwable = RuntimeException("error message")
        getUserRoleUseCase.params = GetUserRoleUseCase.createParam(userId)

        every {
            userSession.userId
        } returns userId.toString()

        coEvery {
            getUserRoleUseCase.executeOnBackground()
        } throws throwable

        viewModel.getUserRole()

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify {
            userSession.userId
        }

        coVerify {
            getUserRoleUseCase.executeOnBackground()
        }

        assert(viewModel.userRole.value is Fail)
    }

    private fun getMockParams(whiteListName: String): RequestParams {
        return RequestParams.create().apply {
            putString(TestConst.KEY_WHITE_LIST_NAME, whiteListName)
        }
    }
}