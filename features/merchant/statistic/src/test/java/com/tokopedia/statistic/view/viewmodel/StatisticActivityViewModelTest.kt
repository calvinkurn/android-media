package com.tokopedia.statistic.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.constant.PMStatusConst
import com.tokopedia.gm.common.data.source.local.model.PMStatusUiModel
import com.tokopedia.gm.common.domain.interactor.GetPMStatusUseCase
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
import io.mockk.verify
import io.mockk.verifyAll
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
    lateinit var getPMStatusUseCase: GetPMStatusUseCase

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
            { getPMStatusUseCase },
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `should success when checking user white list status`() = runBlocking {
        val whiteListName = "statistic-operational"
        val params = getMockParams(whiteListName)
        val result = true

        coEvery {
            checkWhitelistedStatusUseCase.createParam(whiteListName)
        } returns params

        coEvery {
            checkWhitelistedStatusUseCase.execute(params)
        } returns result

        viewModel.checkWhiteListStatus()

        val expectedParams = checkWhitelistedStatusUseCase.createParam(whiteListName)

        coVerify {
            checkWhitelistedStatusUseCase.createParam(whiteListName)
        }

        coVerify {
            checkWhitelistedStatusUseCase.execute(expectedParams)
        }

        Assert.assertEquals(params.parameters.toString(), expectedParams.parameters.toString())
        Assert.assertEquals(Success(result), viewModel.whitelistedStatus.value)
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
        } returns userId

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
        } returns userId

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

    @Test
    fun `when fetch shop status is Official Store should update the shop status on userSession`() {
        runBlocking {
            val mockShopId = "12345"
            val mockResponse = PMStatusUiModel(
                isOfficialStore = true,
                status = PMStatusConst.ACTIVE
            )

            every {
                userSession.shopId
            } returns mockShopId

            coEvery {
                getPMStatusUseCase.executeOnBackground()
            } returns mockResponse

            viewModel.fetchPMStatus()

            verifyAll {
                userSession.shopId
                userSession.setIsShopOfficialStore(true)
                userSession.setIsGoldMerchant(true)
                userSession.setIsPowerMerchantIdle(false)
            }
        }
    }

    @Test
    fun `when fetch shop status is Power Merchant Active should update the shop status on userSession`() {
        runBlocking {
            val mockShopId = "12345"
            val mockResponse = PMStatusUiModel(
                isOfficialStore = false,
                status = PMStatusConst.ACTIVE
            )

            every {
                userSession.shopId
            } returns mockShopId

            coEvery {
                getPMStatusUseCase.executeOnBackground()
            } returns mockResponse

            viewModel.fetchPMStatus()

            verifyAll {
                userSession.shopId
                userSession.setIsShopOfficialStore(false)
                userSession.setIsPowerMerchantIdle(false)
                userSession.setIsGoldMerchant(true)
            }
        }
    }

    @Test
    fun `when fetch shop status is Power Merchant Idle should update the shop status on userSession`() {
        runBlocking {
            val mockShopId = "12345"

            val mockResponse = PMStatusUiModel(
                isOfficialStore = false,
                status = PMStatusConst.IDLE
            )

            every {
                userSession.shopId
            } returns mockShopId

            coEvery {
                getPMStatusUseCase.executeOnBackground()
            } returns mockResponse

            viewModel.fetchPMStatus()

            verifyAll {
                userSession.shopId
                userSession.setIsShopOfficialStore(false)
                userSession.setIsPowerMerchantIdle(true)
                userSession.setIsGoldMerchant(true)
            }
        }
    }

    @Test
    fun `when fetch shop status then throws exception should do nothing`() {
        val throwable = Throwable()

        coEvery {
            getPMStatusUseCase.executeOnBackground()
        } throws throwable

        viewModel.fetchPMStatus()
    }

    private fun getMockParams(whiteListName: String): RequestParams {
        return RequestParams.create().apply {
            putString(TestConst.KEY_WHITE_LIST_NAME, whiteListName)
        }
    }
}