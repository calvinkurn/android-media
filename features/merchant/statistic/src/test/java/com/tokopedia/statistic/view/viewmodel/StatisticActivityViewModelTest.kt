package com.tokopedia.statistic.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.domain.model.BenefitKeyValueModel
import com.tokopedia.gm.common.domain.model.GetElementBenefitByKeyBulkData
import com.tokopedia.gm.common.domain.usecase.GetElementBenefitByKeyBulkUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.statistic.domain.usecase.CheckWhitelistedStatusUseCase
import com.tokopedia.statistic.domain.usecase.GetUserRoleUseCase
import com.tokopedia.statistic.utils.TestConst
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyAll
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By @ilhamsuaib on 19/02/21
 */

@OptIn(ExperimentalCoroutinesApi::class)
class StatisticActivityViewModelTest {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getUserRoleUseCase: GetUserRoleUseCase

    @RelaxedMockK
    lateinit var getElementBenefitByKeyBulkUseCase: GetElementBenefitByKeyBulkUseCase

    @RelaxedMockK
    lateinit var checkWhitelistedStatusUseCase: CheckWhitelistedStatusUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private lateinit var viewModel: StatisticActivityViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = StatisticActivityViewModel(
            { userSession },
            { checkWhitelistedStatusUseCase },
            { getUserRoleUseCase },
            { getElementBenefitByKeyBulkUseCase },
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `should success when checking user white list status`() = runBlocking {
        val whiteListName = "statistic-traffic-apps"
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
        val whiteListName = "statistic-traffic-apps"
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

        coVerify {
            userSession.userId
        }

        coVerify {
            getUserRoleUseCase.executeOnBackground()
        }

        assert(viewModel.userRole.value is Fail)
    }

    @Test
    fun `when fetch paywall access should success then set the access is granted`() {
        fetchPaywallAccessTestScope { mockShopId, elementKey, source, results ->
            val granted = 1
            val mockResponse = GetElementBenefitByKeyBulkData(
                result = listOf(
                    BenefitKeyValueModel(
                        elementKey = elementKey,
                        value = granted
                    )
                )
            )

            coEvery {
                getElementBenefitByKeyBulkUseCase.execute(
                    shopId = mockShopId,
                    elementKeys = listOf(elementKey),
                    source = source,
                    useCache = true
                )
            } returns mockResponse

            viewModel.fetchPaywallAccessState()

            coVerifyAll {
                userSession.shopId
                getElementBenefitByKeyBulkUseCase.execute(
                    shopId = mockShopId,
                    elementKeys = listOf(elementKey),
                    source = source,
                    useCache = true
                )
            }

            assert(!results[0]) // initial value
            assert(results[1]) // resulting 1 (granted) which means the user has the access
        }
    }

    @Test
    fun `when fetch paywall access should success then set the access is not granted`() {
        fetchPaywallAccessTestScope { mockShopId, elementKey, source, results ->
            val granted = 0
            val mockResponse = GetElementBenefitByKeyBulkData(
                result = listOf(
                    BenefitKeyValueModel(
                        elementKey = elementKey,
                        value = granted
                    )
                )
            )

            coEvery {
                getElementBenefitByKeyBulkUseCase.execute(
                    shopId = mockShopId,
                    elementKeys = listOf(elementKey),
                    source = source,
                    useCache = true
                )
            } returns mockResponse

            viewModel.fetchPaywallAccessState()

            coVerifyAll {
                userSession.shopId
                getElementBenefitByKeyBulkUseCase.execute(
                    shopId = mockShopId,
                    elementKeys = listOf(elementKey),
                    source = source,
                    useCache = true
                )
            }

            assert(!results[0]) // initial value and resulting 0 (not granted) which means the user doesn't has the access
        }
    }

    @Test
    fun `when fetch paywall access should failed then set the access is not granted`() {
        fetchPaywallAccessTestScope { mockShopId, elementKey, source, results ->
            val exception = Exception()
            coEvery {
                getElementBenefitByKeyBulkUseCase.execute(
                    shopId = mockShopId,
                    elementKeys = listOf(elementKey),
                    source = source,
                    useCache = true
                )
            } throws exception

            viewModel.fetchPaywallAccessState()

            coVerifyAll {
                userSession.shopId
                getElementBenefitByKeyBulkUseCase.execute(
                    shopId = mockShopId,
                    elementKeys = listOf(elementKey),
                    source = source,
                    useCache = true
                )
            }

            assert(!results[0]) // initial value is false and throws exception which means the user doesn't has the access, so the value still false
        }
    }

    private fun fetchPaywallAccessTestScope(
        testBody: TestScope.(mockShopId: String, elementKey: String, source: String, results: List<Boolean>) -> Unit
    ) {
        runTest {
            val results = mutableListOf<Boolean>()
            val job = launch(UnconfinedTestDispatcher()) {
                viewModel.paywallAccess.collectLatest {
                    results.add(it)
                }
            }

            val mockShopId = "12345"
            val elementKey =
                GetElementBenefitByKeyBulkUseCase.Companion.Keys.STATISTIC_PAYWALL_ACCESS
            val source = GetElementBenefitByKeyBulkUseCase.Companion.Sources.STATISTIC

            every {
                userSession.shopId
            } returns mockShopId

            testBody(mockShopId, elementKey, source, results)

            job.cancel()
        }
    }

    private fun getMockParams(whiteListName: String): RequestParams {
        return RequestParams.create().apply {
            putString(TestConst.KEY_WHITE_LIST_NAME, whiteListName)
        }
    }
}
