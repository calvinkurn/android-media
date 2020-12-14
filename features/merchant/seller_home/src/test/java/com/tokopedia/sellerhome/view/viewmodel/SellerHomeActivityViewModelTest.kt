package com.tokopedia.sellerhome.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.domain.usecase.GetNotificationUseCase
import com.tokopedia.sellerhome.domain.usecase.GetShopInfoUseCase
import com.tokopedia.sellerhome.view.model.NotificationSellerOrderStatusUiModel
import com.tokopedia.sellerhome.view.model.NotificationUiModel
import com.tokopedia.sellerhome.view.model.ShopInfoUiModel
import com.tokopedia.shop.common.constant.AdminPermissionGroup
import com.tokopedia.shop.common.domain.interactor.AdminInfoUseCase
import com.tokopedia.shop.common.domain.interactor.model.adminrevamp.*
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

/**
 * Created By @ilhamsuaib on 24/03/20
 */

@ExperimentalCoroutinesApi
class SellerHomeActivityViewModelTest {

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var getNotificationUseCase: GetNotificationUseCase

    @RelaxedMockK
    lateinit var getShopInfoUseCase: GetShopInfoUseCase

    @RelaxedMockK
    lateinit var adminInfoUseCase: AdminInfoUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var mViewModel: SellerHomeActivityViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mViewModel = createViewModel()
    }

    private fun createViewModel() =
        SellerHomeActivityViewModel(userSession, getNotificationUseCase, getShopInfoUseCase, adminInfoUseCase, coroutineTestRule.dispatchers)

    @Test
    fun `get notifications then returns success result`() {

        val notifications = NotificationUiModel(0, 0,
            NotificationSellerOrderStatusUiModel())

        coEvery {
            getNotificationUseCase.executeOnBackground()
        } returns notifications

        val viewModel = createViewModel()
        runBlocking {
            viewModel.getNotifications()
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            coVerify {
                getNotificationUseCase.executeOnBackground()
            }
            assertEquals(Success(notifications), viewModel.notifications.value)
        }
    }

    @Test
    fun `get notifications then returns failed result`() = runBlocking {

        val throwable = MessageErrorException()

        coEvery {
            getNotificationUseCase.executeOnBackground()
        } throws throwable

        val viewModel = createViewModel()
        viewModel.getNotifications()

        coVerify {
            getNotificationUseCase.executeOnBackground()
        }

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        assert(viewModel.notifications.value is Fail)
    }

    @Test
    fun `get shop info then returns success result`() {
        val userId = "123456"
        val shopInfo = ShopInfoUiModel()

        getShopInfoUseCase.params = GetShopInfoUseCase.getRequestParam(userId)

        every {
            userSession.userId
        } returns userId

        coEvery {
            getShopInfoUseCase.executeOnBackground()
        } returns shopInfo

        val viewModel = createViewModel()
        runBlocking {
            viewModel.getShopInfo()
            viewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            coVerify {
                userSession.userId
            }
            coVerify {
                getShopInfoUseCase.executeOnBackground()
            }
        }

        assertEquals(Success(shopInfo), viewModel.shopInfo.value)
    }

    @Test
    fun `get shop info then returns failed result`() = coroutineTestRule.runBlockingTest {
        val userId = "123456"
        val throwable = MessageErrorException()

        getShopInfoUseCase.params = GetShopInfoUseCase.getRequestParam(userId)

        every {
            userSession.userId
        } returns userId

        coEvery {
            getShopInfoUseCase.executeOnBackground()
        } throws throwable

        val viewModel = createViewModel()
        viewModel.getShopInfo()

        verify {
            userSession.userId
        }

        coVerify {
            getShopInfoUseCase.executeOnBackground()
        }

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        assert(viewModel.shopInfo.value is Fail)
    }

    @Test
    fun `get admin info then success return isEligible`() = coroutineTestRule.runBlockingTest {
        val adminInfoResult = AdminInfoResult.Success(AdminInfoData())

        coEvery {
            adminInfoUseCase.execute(any())
        } returns adminInfoResult

        mViewModel.getAdminInfo()

        coVerify {
            adminInfoUseCase.execute(any())
        }

        assert(mViewModel.isRoleEligible.value is Success)
    }

    @Test
    fun `get admin info then return failed result`() = coroutineTestRule.runBlockingTest {
        val throwable = MessageErrorException("")

        coEvery {
            adminInfoUseCase.execute(any())
        } throws throwable

        mViewModel.getAdminInfo()

        coVerify {
            adminInfoUseCase.execute(any())
        }

        assert(mViewModel.isRoleEligible.value is Fail)
    }

    @Test
    fun `when admin info is shop admin, get admin info will return isEligible true result`() = coroutineTestRule.runBlockingTest {
        val isShopAdmin = true
        val adminInfoResult = AdminInfoResult.Success(
                AdminInfoData(
                        detailInfo = AdminInfoDetailInformation(
                                adminRoleType = AdminRoleType(
                                        isShopAdmin = isShopAdmin
                                )
                        )
                )
        )

        coEvery {
            adminInfoUseCase.execute(any())
        } returns adminInfoResult

        mViewModel.getAdminInfo()

        coVerify {
            adminInfoUseCase.execute(any())
        }

        assert((mViewModel.isRoleEligible.value as? Success)?.data == true)
    }

    @Test
    fun `when admin info is not shop admin but is location admin, get admin info will return isEligible false result`() = coroutineTestRule.runBlockingTest {
        val isShopAdmin = false
        val isLocationAdmin = true
        val adminInfoResult = AdminInfoResult.Success(
                AdminInfoData(
                        detailInfo = AdminInfoDetailInformation(
                                adminRoleType = AdminRoleType(
                                        isShopAdmin = isShopAdmin,
                                        isLocationAdmin = isLocationAdmin
                                )
                        )
                )
        )

        coEvery {
            adminInfoUseCase.execute(any())
        } returns adminInfoResult

        mViewModel.getAdminInfo()

        coVerify {
            adminInfoUseCase.execute(any())
        }

        assert((mViewModel.isRoleEligible.value as? Success)?.data == false)
    }

    @Test
    fun `when admin info is not shop admin and not is location admin, get admin info will return isEligible true result`() = coroutineTestRule.runBlockingTest {
        val isShopAdmin = false
        val isLocationAdmin = false
        val adminInfoResult = AdminInfoResult.Success(
                AdminInfoData(
                        detailInfo = AdminInfoDetailInformation(
                                adminRoleType = AdminRoleType(
                                        isShopAdmin = isShopAdmin,
                                        isLocationAdmin = isLocationAdmin
                                )
                        )
                )
        )

        coEvery {
            adminInfoUseCase.execute(any())
        } returns adminInfoResult

        mViewModel.getAdminInfo()

        coVerify {
            adminInfoUseCase.execute(any())
        }

        assert((mViewModel.isRoleEligible.value as? Success)?.data == true)
    }

    @Test
    fun `when admin info returns fail result, will also make isEligible value true`() = coroutineTestRule.runBlockingTest {
        val adminInfoResult = AdminInfoResult.Fail(MessageErrorException(""))

        coEvery {
            adminInfoUseCase.execute(any())
        } returns adminInfoResult

        mViewModel.getAdminInfo()

        coVerify {
            adminInfoUseCase.execute(any())
        }

        assert((mViewModel.isRoleEligible.value as? Success)?.data == true)
    }

    @Test
    fun `success get admin info will change is order shop if permission list is not null`() = coroutineTestRule.runBlockingTest {
        val adminInfoResult = AdminInfoResult.Success(
                AdminInfoData(
                        permissionList = listOf()
                )
        )

        coEvery {
            adminInfoUseCase.execute(any())
        } returns adminInfoResult

        mViewModel.getAdminInfo()

        coVerify {
            adminInfoUseCase.execute(any())
        }

        assert(mViewModel.isOrderShopAdmin.value != null)
    }

    @Test
    fun `success get admin info will change is order shop to true if permission list contains order permission`() = coroutineTestRule.runBlockingTest {
        val adminInfoResult = AdminInfoResult.Success(
                AdminInfoData(
                        permissionList = listOf(
                                AdminPermission(id = AdminPermissionGroup.ORDER)
                        )
                )
        )

        coEvery {
            adminInfoUseCase.execute(any())
        } returns adminInfoResult

        mViewModel.getAdminInfo()

        coVerify {
            adminInfoUseCase.execute(any())
        }

        assert(mViewModel.isOrderShopAdmin.value == true)
    }

    @Test
    fun `success get admin info will change is order shop to false if permission list not null but not contains order permission`() = coroutineTestRule.runBlockingTest {
        val adminInfoResult = AdminInfoResult.Success(
                AdminInfoData(
                        permissionList = listOf(
                                AdminPermission(id = AdminPermissionGroup.CHAT)
                        )
                )
        )

        coEvery {
            adminInfoUseCase.execute(any())
        } returns adminInfoResult

        mViewModel.getAdminInfo()

        coVerify {
            adminInfoUseCase.execute(any())
        }

        assert(mViewModel.isOrderShopAdmin.value == false)
    }

    @Test
    fun `execute launch in custom base view model with custom onError block without custom context`() = runBlocking {
        val customOnErrorViewModel = CustomOnErrorViewModel(Job())
        customOnErrorViewModel.noCustomContext()
        customOnErrorViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
        assert(customOnErrorViewModel.mockLiveData.value is Fail)
    }

    @Test
    fun `execute launch in custom base view model with custom onError block with custom context`() = runBlocking {
        val job = Job()
        val customOnErrorViewModel = CustomOnErrorViewModel(job)
        customOnErrorViewModel.withCustomContext()
        job.children.forEach { it.join() }
        assert(customOnErrorViewModel.mockLiveData.value is Fail)
    }

    inner class CustomOnErrorViewModel(private val job: Job) : CustomBaseViewModel(coroutineTestRule.dispatchers) {

        private val _mockLiveData = MutableLiveData<Result<Any>>()
        val mockLiveData: LiveData<Result<Any>>
            get() = _mockLiveData

        fun noCustomContext() = executeCall(_mockLiveData, onError = {
            _mockLiveData.value = Fail(it)
        }, block = {
            getResult()
        })

        fun withCustomContext() = executeCall(_mockLiveData, job, onError = {
            _mockLiveData.value = Fail(it)
        }, block = {
            getResult()
        })

        private fun getResult(): Any {
            throw RuntimeException()
        }
    }
}